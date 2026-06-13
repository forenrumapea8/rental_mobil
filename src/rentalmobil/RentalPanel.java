package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.*;
import java.math.BigDecimal;
import java.io.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.border.EmptyBorder;

public class RentalPanel extends JPanel implements Refreshable {
    DefaultTableModel model;
    int selectedId = 0;
    BigDecimal currentTarif = BigDecimal.ZERO;
    BigDecimal currentTotal = BigDecimal.ZERO;
    boolean adjustingDates = false;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    JTable table = new JTable();
    JComboBox<ComboItem> pelanggan = new JComboBox<ComboItem>(), mobil = new JComboBox<ComboItem>();
    JComboBox<String> metodeBayar = new JComboBox<String>(new String[]{"Cash", "Transfer", "QRIS"});
    DatePickerField tglPinjam = new DatePickerField(), tglKembali = new DatePickerField();
    JTextField lama = new JTextField(), tarif = new JTextField(), total = new JTextField(), cari = new JTextField();
    JTextField jumlahBayar = new JTextField(), kembalian = new JTextField(), statusBayar = new JTextField();

    // GUI Builder fields
    private JLabel lblTitle;
    private JPanel mainPanel, formWrapPanel, formPanel, buttonPanel;
    private JScrollPane scrollPane;
    private JButton btnSave, btnStruk, btnBatal, btnReset, btnCari, btnRefresh;

    private javax.swing.JLabel lblPelanggan, lblMobilTersedia, lblTglPinjam, lblTglKembali, lblLamaSewa, lblTarifHari, lblTotalTagihan, lblMetodePembayaran, lblJumlahBayar, lblKembalian, lblStatusPembayaran, lblCari;
    // End of variables declaration//GEN-END:variables
    public RentalPanel(){
        initComponents();
        btnSave.addActionListener(e -> save());
        btnStruk.addActionListener(e -> printSelected());
        btnBatal.addActionListener(e -> cancelRental());
        btnReset.addActionListener(e -> clear());
        btnCari.addActionListener(e -> loadTable(cari.getText()));
        btnRefresh.addActionListener(e -> { cari.setText(""); refreshData(); });

        metodeBayar.setModel(new DefaultComboBoxModel<String>(new String[]{"Cash", "Transfer", "QRIS"}));

        model = new DefaultTableModel(new Object[]{"ID","Pelanggan","Mobil","Tgl Pinjam","Tgl Kembali","Lama","Total","Metode","Bayar","Kembali","Status Bayar","Status"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        for(JComponent c : new JComponent[]{pelanggan,mobil,lama,tarif,total,cari,metodeBayar,jumlahBayar,kembalian,statusBayar}) UI.input(c);
        lama.setEditable(false); tarif.setEditable(false); total.setEditable(false); kembalian.setEditable(false); statusBayar.setEditable(false);
        mobil.addActionListener(e -> loadTarif());
        tglPinjam.addChange(() -> onTanggalPinjamChanged());
        tglKembali.addChange(() -> hitung());
        jumlahBayar.getDocument().addDocumentListener(new SimpleDocListener(){ public void update(){ hitungPembayaran(); } });
        metodeBayar.addActionListener(e -> hitungPembayaran());
        table.getSelectionModel().addListSelectionListener(e -> { if(table.getSelectedRow() >= 0){ int r = table.convertRowIndexToModel(table.getSelectedRow()); selectedId = Integer.parseInt(model.getValueAt(r,0).toString()); } });
        btnBatal.setVisible(AppSession.isAdmin());
        hitung();
    }

    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setPreferredSize(new Dimension(1000, 620));
        setBackground(UI.BG);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        lblTitle = new JLabel("Transaksi Rental / Peminjaman");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 18, 500, 28));
        lblPelanggan = new JLabel("Pelanggan");
        add(lblPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 140, 22));
        add(pelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 60, 180, 26));
        lblMobilTersedia = new JLabel("Mobil Tersedia");
        add(lblMobilTersedia, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 64, 140, 22));
        add(mobil, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 60, 180, 26));
        lblTglPinjam = new JLabel("Tgl Pinjam");
        add(lblTglPinjam, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 64, 100, 22));
        add(tglPinjam, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 60, 170, 26));
        lblTglKembali = new JLabel("Tgl Kembali");
        add(lblTglKembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 140, 22));
        add(tglKembali, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 100, 180, 26));
        lblLamaSewa = new JLabel("Lama Sewa");
        add(lblLamaSewa, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 104, 140, 22));
        add(lama, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 100, 180, 26));
        lblTarifHari = new JLabel("Tarif/Hari");
        add(lblTarifHari, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 104, 100, 22));
        add(tarif, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 100, 170, 26));
        lblTotalTagihan = new JLabel("Total Tagihan");
        add(lblTotalTagihan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 144, 140, 22));
        add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 140, 180, 26));
        lblMetodePembayaran = new JLabel("Metode Pembayaran");
        add(lblMetodePembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 144, 140, 22));
        add(metodeBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 140, 180, 26));
        lblJumlahBayar = new JLabel("Jumlah Bayar");
        add(lblJumlahBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 144, 100, 22));
        add(jumlahBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 140, 170, 26));
        lblKembalian = new JLabel("Kembalian");
        add(lblKembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 184, 140, 22));
        add(kembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 180, 180, 26));
        lblStatusPembayaran = new JLabel("Status Pembayaran");
        add(lblStatusPembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 184, 140, 22));
        add(statusBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 180, 180, 26));
        lblCari = new JLabel("Cari");
        add(lblCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 184, 100, 22));
        add(cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 180, 170, 26));
        btnSave = new JButton("Simpan Rental");
        add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 232, 120, 28));
        btnStruk = new JButton("Cetak Struk Rental");
        add(btnStruk, new org.netbeans.lib.awtextra.AbsoluteConstraints(148, 232, 150, 28));
        btnBatal = new JButton("Batalkan Rental");
        add(btnBatal, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 232, 130, 28));
        btnReset = new JButton("Reset");
        add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(444, 232, 80, 28));
        btnCari = new JButton("Cari");
        add(btnCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(532, 232, 70, 28));
        btnRefresh = new JButton("Refresh");
        add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(610, 232, 90, 28));
        scrollPane = new JScrollPane(table);
        add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 960, 320));
    }// </editor-fold>//GEN-END:initComponents
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

    void onTanggalPinjamChanged(){
        if(adjustingDates){ hitung(); return; }
        try{
            LocalDate pinjam = tglPinjam.getSqlDate().toLocalDate();
            LocalDate kembali = tglKembali.getSqlDate().toLocalDate();
            if(kembali.isBefore(pinjam)){
                adjustingDates = true;
                tglKembali.setDate(pinjam.plusDays(1));
                adjustingDates = false;
            }
        }catch(Exception ignored){}
        hitung();
    }

    void hitung(){
        try{
            LocalDate pinjam = tglPinjam.getSqlDate().toLocalDate();
            LocalDate kembali = tglKembali.getSqlDate().toLocalDate();
            long selisihHari = ChronoUnit.DAYS.between(pinjam, kembali);
            int days = (int) selisihHari;
            if(days < 1) days = 1;

            lama.setText(String.valueOf(days));
            currentTotal = currentTarif.multiply(new BigDecimal(days));
            total.setText(DB.rupiah(currentTotal));
            hitungPembayaran();
        }catch(Exception e){
            lama.setText("1");
            currentTotal = currentTarif;
            total.setText(DB.rupiah(currentTotal));
            hitungPembayaran();
        }
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
        LocalDate pinjamDate = tglPinjam.getSqlDate().toLocalDate();
        LocalDate kembaliDate = tglKembali.getSqlDate().toLocalDate();
        if(kembaliDate.isBefore(pinjamDate)){
            UI.warn(this,"Tanggal kembali tidak boleh lebih awal dari tanggal pinjam.");
            return;
        }
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
