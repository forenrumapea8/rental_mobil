package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;
import java.io.*;
import java.util.concurrent.TimeUnit;
import javax.swing.border.EmptyBorder;

public class RentalPanel extends JPanel implements Refreshable {
    JTable table = new JTable();
    DefaultTableModel model;
    JComboBox<ComboItem> pelanggan = new JComboBox<ComboItem>(), mobil = new JComboBox<ComboItem>();
    JComboBox<String> metodeBayar = new JComboBox<String>(new String[]{"Cash", "Transfer", "QRIS"});
    DatePickerField tglPinjam = new DatePickerField(), tglKembali = new DatePickerField();
    JTextField lama = new JTextField(), tarif = new JTextField(), total = new JTextField(), cari = new JTextField();
    JTextField jumlahBayar = new JTextField(), kembalian = new JTextField(), statusBayar = new JTextField();
    int selectedId = 0;
    BigDecimal currentTarif = BigDecimal.ZERO;
    BigDecimal currentTotal = BigDecimal.ZERO;

    public RentalPanel(){
        setLayout(new BorderLayout(0,14));
        setBackground(UI.BG);
        setBorder(new EmptyBorder(24,24,24,24));
        JLabel h = new JLabel("Transaksi Rental / Peminjaman");
        h.setFont(UI.TITLE);
        add(h, BorderLayout.NORTH);

        JPanel main = UI.card();
        add(main, BorderLayout.CENTER);
        model = new DefaultTableModel(new Object[]{"ID","Pelanggan","Mobil","Tgl Pinjam","Tgl Kembali","Lama","Total","Metode","Bayar","Kembali","Status Bayar","Status"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        main.add(form(), BorderLayout.NORTH);
        main.add(UI.table(table), BorderLayout.CENTER);
        mobil.addActionListener(e -> loadTarif());
        tglPinjam.addChange(() -> hitung());
        tglKembali.addChange(() -> hitung());
        jumlahBayar.getDocument().addDocumentListener(new SimpleDocListener(){ public void update(){ hitungPembayaran(); } });
        metodeBayar.addActionListener(e -> hitungPembayaran());
    }

    JPanel form(){
        JPanel wrap = new JPanel(new BorderLayout(0,10));
        wrap.setOpaque(false);
        for(JComponent c : new JComponent[]{pelanggan,mobil,lama,tarif,total,cari,metodeBayar,jumlahBayar,kembalian,statusBayar}) UI.input(c);
        lama.setEditable(false); tarif.setEditable(false); total.setEditable(false); kembalian.setEditable(false); statusBayar.setEditable(false);
        JPanel g = new JPanel(new GridBagLayout());
        g.setOpaque(false);
        g.add(UI.label("Pelanggan"),UI.gbc(0,0)); g.add(pelanggan,UI.gbc(1,0));
        g.add(UI.label("Mobil Tersedia"),UI.gbc(2,0)); g.add(mobil,UI.gbc(3,0));
        g.add(UI.label("Tgl Pinjam"),UI.gbc(0,1)); g.add(tglPinjam,UI.gbc(1,1));
        g.add(UI.label("Tgl Kembali"),UI.gbc(2,1)); g.add(tglKembali,UI.gbc(3,1));
        g.add(UI.label("Lama Sewa"),UI.gbc(0,2)); g.add(lama,UI.gbc(1,2));
        g.add(UI.label("Tarif/Hari"),UI.gbc(2,2)); g.add(tarif,UI.gbc(3,2));
        g.add(UI.label("Total Tagihan"),UI.gbc(0,3)); g.add(total,UI.gbc(1,3));
        g.add(UI.label("Metode Pembayaran"),UI.gbc(2,3)); g.add(metodeBayar,UI.gbc(3,3));
        g.add(UI.label("Jumlah Bayar"),UI.gbc(0,4)); g.add(jumlahBayar,UI.gbc(1,4));
        g.add(UI.label("Kembalian"),UI.gbc(2,4)); g.add(kembalian,UI.gbc(3,4));
        g.add(UI.label("Status Pembayaran"),UI.gbc(0,5)); g.add(statusBayar,UI.gbc(1,5));
        g.add(UI.label("Cari"),UI.gbc(2,5)); g.add(cari,UI.gbc(3,5));
        wrap.add(g, BorderLayout.CENTER);

        JPanel b = UI.leftFlow();
        JButton sim = UI.button("Simpan Rental");
        JButton struk = UI.darkButton("Cetak Struk Rental");
        JButton batal = UI.warningButton("Batalkan Rental");
        JButton res = UI.lightButton("Reset");
        JButton car = UI.darkButton("Cari");
        JButton ref = UI.lightButton("Refresh");
        sim.addActionListener(e -> save());
        struk.addActionListener(e -> printSelected());
        batal.addActionListener(e -> cancelRental());
        res.addActionListener(e -> clear());
        car.addActionListener(e -> loadTable(cari.getText()));
        ref.addActionListener(e -> { cari.setText(""); refreshData(); });
        b.add(sim); b.add(struk);
        if (AppSession.isAdmin()) b.add(batal);
        b.add(res); b.add(car); b.add(ref);
        wrap.add(b, BorderLayout.SOUTH);

        table.getSelectionModel().addListSelectionListener(e -> {
            if(table.getSelectedRow() >= 0){
                int r = table.convertRowIndexToModel(table.getSelectedRow());
                selectedId = Integer.parseInt(model.getValueAt(r,0).toString());
            }
        });
        return wrap;
    }

    void loadCombo(){
        pelanggan.removeAllItems();
        mobil.removeAllItems();
        try(Connection c = DB.getConnection()){
            ResultSet r = c.createStatement().executeQuery("SELECT id_pelanggan,nama FROM pelanggan ORDER BY nama");
            while(r.next()) pelanggan.addItem(new ComboItem(r.getInt(1), r.getString(2)));
            r = c.createStatement().executeQuery("SELECT id_mobil,merk,tipe,plat_nomor FROM mobil WHERE status='Tersedia' ORDER BY merk");
            while(r.next()) mobil.addItem(new ComboItem(r.getInt(1), r.getString(2) + " " + r.getString(3) + " - " + r.getString(4)));
        }catch(Exception e){ UI.error(this,e); }
        loadTarif();
    }

    void loadTarif(){
        ComboItem item = (ComboItem)mobil.getSelectedItem();
        if(item == null){ currentTarif = BigDecimal.ZERO; tarif.setText(""); hitung(); return; }
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT tarif_per_hari FROM mobil WHERE id_mobil=?")){
            ps.setInt(1,item.id);
            ResultSet r = ps.executeQuery();
            if(r.next()){
                currentTarif = r.getBigDecimal(1);
                tarif.setText(DB.rupiah(currentTarif));
            }
        }catch(Exception e){ }
        hitung();
    }

    void hitung(){
        try{
            long diff = tglKembali.getSqlDate().getTime() - tglPinjam.getSqlDate().getTime();
            int days = (int)TimeUnit.MILLISECONDS.toDays(diff);
            if(days < 1) days = 1;
            lama.setText(String.valueOf(days));
            currentTotal = currentTarif.multiply(new BigDecimal(days));
            total.setText(DB.rupiah(currentTotal));
            hitungPembayaran();
        }catch(Exception e){ }
    }

    void hitungPembayaran(){
        BigDecimal bayar = DB.money(jumlahBayar.getText());
        BigDecimal kembali = bayar.subtract(currentTotal);
        if(kembali.compareTo(BigDecimal.ZERO) < 0) kembali = BigDecimal.ZERO;
        kembalian.setText(DB.rupiah(kembali));
        if(currentTotal.compareTo(BigDecimal.ZERO) == 0){
            statusBayar.setText("-");
        } else if(bayar.compareTo(currentTotal) >= 0){
            statusBayar.setText("Lunas");
        } else {
            statusBayar.setText("Kurang");
        }
    }

    void clear(){
        selectedId = 0;
        jumlahBayar.setText("");
        kembalian.setText("");
        statusBayar.setText("");
        metodeBayar.setSelectedIndex(0);
        table.clearSelection();
        loadCombo();
    }

    void save(){
        ComboItem p = (ComboItem)pelanggan.getSelectedItem();
        ComboItem m = (ComboItem)mobil.getSelectedItem();
        if(p == null || m == null){ UI.warn(this,"Data pelanggan dan mobil tersedia wajib ada."); return; }
        Connection c = null;
        try{
            c = DB.getConnection();
            c.setAutoCommit(false);
            int l = Integer.parseInt(lama.getText());
            BigDecimal ttl = currentTarif.multiply(new BigDecimal(l));
            BigDecimal bayar = DB.money(jumlahBayar.getText());
            if(bayar.compareTo(ttl) < 0){
                UI.warn(this,"Jumlah bayar masih kurang. Total tagihan: " + DB.rupiah(ttl));
                return;
            }
            BigDecimal kembali = bayar.subtract(ttl);
            if(kembali.compareTo(BigDecimal.ZERO) < 0) kembali = BigDecimal.ZERO;
            String metode = String.valueOf(metodeBayar.getSelectedItem());
            PreparedStatement ps = c.prepareStatement("INSERT INTO rental(id_pelanggan,id_mobil,id_petugas,tanggal_pinjam,tanggal_kembali,lama_sewa,tarif_per_hari,total,metode_pembayaran,jumlah_bayar,kembalian,status_pembayaran,status) VALUES(?,?,?,?,?,?,?,?,?,?,?,'Lunas','Berjalan')", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,p.id);
            ps.setInt(2,m.id);
            if(AppSession.isPetugas()) ps.setInt(3,AppSession.userId); else ps.setNull(3,Types.INTEGER);
            ps.setDate(4,tglPinjam.getSqlDate());
            ps.setDate(5,tglKembali.getSqlDate());
            ps.setInt(6,l);
            ps.setBigDecimal(7,currentTarif);
            ps.setBigDecimal(8,ttl);
            ps.setString(9,metode);
            ps.setBigDecimal(10,bayar);
            ps.setBigDecimal(11,kembali);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if(keys.next()) selectedId = keys.getInt(1);
            PreparedStatement up = c.prepareStatement("UPDATE mobil SET status='Dipinjam' WHERE id_mobil=?");
            up.setInt(1,m.id);
            up.executeUpdate();
            c.commit();
            refreshData();
            UI.info(this,"Rental tersimpan, pembayaran lunas, dan status mobil menjadi Dipinjam.");
        }catch(Exception e){
            try { if(c != null) c.rollback(); } catch(Exception ex) {}
            UI.error(this,e);
        } finally {
            try { if(c != null) c.close(); } catch(Exception ex) {}
        }
    }

    void cancelRental(){
        if(selectedId == 0){ UI.warn(this,"Pilih data rental dulu."); return; }
        if(!UI.confirm(this,"Batalkan rental ini? Status mobil akan dikembalikan menjadi Tersedia.")) return;
        Connection c = null;
        try{
            c = DB.getConnection();
            c.setAutoCommit(false);
            PreparedStatement cek = c.prepareStatement("SELECT id_mobil,status FROM rental WHERE id_rental=?");
            cek.setInt(1, selectedId);
            ResultSet r = cek.executeQuery();
            if(!r.next()){ UI.warn(this,"Data rental tidak ditemukan."); return; }
            if(!"Berjalan".equals(r.getString("status"))){ UI.warn(this,"Hanya rental berstatus Berjalan yang bisa dibatalkan."); return; }
            int idMobil = r.getInt("id_mobil");
            PreparedStatement ps = c.prepareStatement("UPDATE rental SET status='Batal' WHERE id_rental=?");
            ps.setInt(1, selectedId); ps.executeUpdate();
            PreparedStatement up = c.prepareStatement("UPDATE mobil SET status='Tersedia' WHERE id_mobil=?");
            up.setInt(1, idMobil); up.executeUpdate();
            c.commit();
            refreshData();
            UI.info(this,"Rental dibatalkan.");
        }catch(Exception e){
            try { if(c != null) c.rollback(); } catch(Exception ex) {}
            UI.error(this,e);
        } finally { try { if(c != null) c.close(); } catch(Exception ex) {} }
    }

    void loadTable(String q){
        model.setRowCount(0);
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT r.id_rental,p.nama,CONCAT(m.merk,' ',m.tipe,' - ',m.plat_nomor) mobil,r.tanggal_pinjam,r.tanggal_kembali,r.lama_sewa,r.total,r.metode_pembayaran,r.jumlah_bayar,r.kembalian,r.status_pembayaran,r.status FROM rental r JOIN pelanggan p ON r.id_pelanggan=p.id_pelanggan JOIN mobil m ON r.id_mobil=m.id_mobil WHERE p.nama LIKE ? OR m.merk LIKE ? OR m.plat_nomor LIKE ? OR r.status LIKE ? OR r.metode_pembayaran LIKE ? ORDER BY r.id_rental DESC")){
            String s = "%" + q + "%";
            ps.setString(1,s); ps.setString(2,s); ps.setString(3,s); ps.setString(4,s); ps.setString(5,s);
            ResultSet r = ps.executeQuery();
            while(r.next()) model.addRow(new Object[]{ r.getInt(1), r.getString(2), r.getString(3), r.getDate(4), r.getDate(5), r.getInt(6), DB.rupiah(r.getBigDecimal(7)), r.getString(8), DB.rupiah(r.getBigDecimal(9)), DB.rupiah(r.getBigDecimal(10)), r.getString(11), r.getString(12) });
        }catch(Exception e){ UI.error(this,e); }
    }

    void printSelected(){
        if(selectedId == 0 && table.getSelectedRow() >= 0){
            int r = table.convertRowIndexToModel(table.getSelectedRow());
            selectedId = Integer.parseInt(model.getValueAt(r,0).toString());
        }
        if(selectedId == 0){ UI.warn(this,"Pilih data rental dulu."); return; }
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT r.*,p.nama,p.no_hp,p.alamat,CONCAT(m.merk,' ',m.tipe,' - ',m.plat_nomor) mobil,m.plat_nomor FROM rental r JOIN pelanggan p ON r.id_pelanggan=p.id_pelanggan JOIN mobil m ON r.id_mobil=m.id_mobil WHERE r.id_rental=?")){
            ps.setInt(1, selectedId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                File dir = new File("struk"); dir.mkdirs();
                File f = new File(dir,"struk_rental_" + selectedId + ".txt");
                PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
                String metode = rs.getString("metode_pembayaran");
                StrukUtil.header(w, "STRUK RENTAL MOBIL", StrukUtil.noTransaksi("RNT", selectedId));
                StrukUtil.section(w, "Data Pelanggan");
                StrukUtil.row(w, "Nama", rs.getString("nama"));
                StrukUtil.row(w, "No. HP", rs.getString("no_hp"));
                StrukUtil.row(w, "Alamat", rs.getString("alamat"));
                StrukUtil.section(w, "Detail Rental");
                StrukUtil.row(w, "Mobil", rs.getString("mobil"));
                StrukUtil.row(w, "Tanggal Pinjam", rs.getDate("tanggal_pinjam"));
                StrukUtil.row(w, "Tanggal Kembali", rs.getDate("tanggal_kembali"));
                StrukUtil.row(w, "Lama Sewa", rs.getInt("lama_sewa") + " hari");
                StrukUtil.money(w, "Tarif/Hari", rs.getBigDecimal("tarif_per_hari"));
                StrukUtil.money(w, "Total Tagihan", rs.getBigDecimal("total"));
                StrukUtil.section(w, "Pembayaran");
                StrukUtil.row(w, "Metode", metode);
                StrukUtil.money(w, "Jumlah Bayar", rs.getBigDecimal("jumlah_bayar"));
                StrukUtil.money(w, "Kembalian", rs.getBigDecimal("kembalian"));
                StrukUtil.row(w, "Status Bayar", rs.getString("status_pembayaran"));
                StrukUtil.row(w, "Status Rental", rs.getString("status"));
                StrukUtil.footer(w, metode);
                w.close();
                UI.info(this,"Struk rental tersimpan di: " + f.getPath());
            }
        }catch(Exception e){ UI.error(this,e); }
    }

    public void refreshData(){ loadCombo(); loadTable(""); }

    abstract class SimpleDocListener implements DocumentListener {
        public abstract void update();
        public void insertUpdate(DocumentEvent e){ update(); }
        public void removeUpdate(DocumentEvent e){ update(); }
        public void changedUpdate(DocumentEvent e){ update(); }
    }
}
