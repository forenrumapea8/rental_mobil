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

public class PengembalianPanel extends JPanel implements Refreshable {
    DefaultTableModel model;
    int selectedId = 0, rentalId = 0, mobilId = 0;
    BigDecimal totalRental = BigDecimal.ZERO, dendaPerHari = BigDecimal.ZERO, currentDenda = BigDecimal.ZERO, currentTotalAkhir = BigDecimal.ZERO, currentTagihan = BigDecimal.ZERO;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    JTable table = new JTable();
    JComboBox<ComboItem> rental = new JComboBox<ComboItem>();
    JComboBox<String> metodeBayar = new JComboBox<String>(new String[]{"Cash", "Transfer", "QRIS"});
    DatePickerField aktual = new DatePickerField();
    JTextField telat = new JTextField(), denda = new JTextField(), total = new JTextField(), cari = new JTextField();
    JTextField tagihan = new JTextField(), jumlahBayar = new JTextField(), kembalian = new JTextField(), statusBayar = new JTextField();

    // GUI Builder fields
    private JLabel lblTitle;
    private JPanel mainPanel, formWrapPanel, formPanel, buttonPanel;
    private JScrollPane scrollPane;
    private JButton btnSave, btnStruk, btnDelete, btnReset, btnCari, btnRefresh;

    private javax.swing.JLabel lblRentalBerjalan, lblTanggalAktual, lblTerlambat, lblDenda, lblTotalAkhir, lblTagihanPengembalian, lblMetodePembayaran, lblJumlahBayar, lblKembalian, lblStatusPembayaran, lblCari;
    // End of variables declaration//GEN-END:variables
    public PengembalianPanel(){
        initComponents();
        btnSave.addActionListener(e -> save());
        btnStruk.addActionListener(e -> printSelected());
        btnDelete.addActionListener(e -> deletePengembalian());
        btnReset.addActionListener(e -> clear());
        btnCari.addActionListener(e -> loadTable(cari.getText()));
        btnRefresh.addActionListener(e -> { cari.setText(""); refreshData(); });

        metodeBayar.setModel(new DefaultComboBoxModel<String>(new String[]{"Cash", "Transfer", "QRIS"}));

        model = new DefaultTableModel(new Object[]{"ID","ID Rental","Pelanggan","Mobil","Tgl Aktual","Telat","Denda","Total Akhir","Tagihan","Metode","Bayar","Kembali","Status Bayar"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        for(JComponent c : new JComponent[]{rental,telat,denda,total,cari,tagihan,metodeBayar,jumlahBayar,kembalian,statusBayar}) UI.input(c);
        telat.setEditable(false); denda.setEditable(false); total.setEditable(false); tagihan.setEditable(false); kembalian.setEditable(false); statusBayar.setEditable(false);
        rental.addActionListener(e -> hitung());
        aktual.addChange(() -> hitung());
        jumlahBayar.getDocument().addDocumentListener(new SimpleDocListener(){ public void update(){ hitungPembayaran(); } });
        metodeBayar.addActionListener(e -> hitungPembayaran());
        table.getSelectionModel().addListSelectionListener(e -> { if(table.getSelectedRow()>=0){ int r=table.convertRowIndexToModel(table.getSelectedRow()); selectedId=Integer.parseInt(model.getValueAt(r,0).toString()); } });
        btnDelete.setVisible(AppSession.isAdmin());
    }

    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setPreferredSize(new Dimension(1000, 620));
        setBackground(UI.BG);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        lblTitle = new JLabel("Pengembalian Mobil");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 18, 500, 28));
        lblRentalBerjalan = new JLabel("Rental Berjalan");
        add(lblRentalBerjalan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 140, 22));
        add(rental, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 60, 180, 26));
        lblTanggalAktual = new JLabel("Tanggal Aktual");
        add(lblTanggalAktual, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 64, 140, 22));
        add(aktual, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 60, 180, 26));
        lblTerlambat = new JLabel("Terlambat");
        add(lblTerlambat, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 64, 100, 22));
        add(telat, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 60, 170, 26));
        lblDenda = new JLabel("Denda");
        add(lblDenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 140, 22));
        add(denda, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 100, 180, 26));
        lblTotalAkhir = new JLabel("Total Akhir");
        add(lblTotalAkhir, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 104, 140, 22));
        add(total, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 100, 180, 26));
        lblTagihanPengembalian = new JLabel("Tagihan");
        add(lblTagihanPengembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 104, 100, 22));
        add(tagihan, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 100, 170, 26));
        lblMetodePembayaran = new JLabel("Metode Pembayaran");
        add(lblMetodePembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 144, 140, 22));
        add(metodeBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 140, 180, 26));
        lblJumlahBayar = new JLabel("Jumlah Bayar");
        add(lblJumlahBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 144, 140, 22));
        add(jumlahBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 140, 180, 26));
        lblKembalian = new JLabel("Kembalian");
        add(lblKembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 144, 100, 22));
        add(kembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 140, 170, 26));
        lblStatusPembayaran = new JLabel("Status Pembayaran");
        add(lblStatusPembayaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 184, 140, 22));
        add(statusBayar, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 180, 180, 26));
        lblCari = new JLabel("Cari");
        add(lblCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 184, 140, 22));
        add(cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 180, 180, 26));
        btnSave = new JButton("Simpan Pengembalian");
        add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 232, 160, 28));
        btnStruk = new JButton("Cetak Struk");
        add(btnStruk, new org.netbeans.lib.awtextra.AbsoluteConstraints(188, 232, 110, 28));
        btnDelete = new JButton("Hapus");
        add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(306, 232, 90, 28));
        btnReset = new JButton("Reset");
        add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(404, 232, 80, 28));
        btnCari = new JButton("Cari");
        add(btnCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(492, 232, 70, 28));
        btnRefresh = new JButton("Refresh");
        add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 232, 90, 28));
        scrollPane = new JScrollPane(table);
        add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 960, 320));
    }// </editor-fold>//GEN-END:initComponents
    JPanel form(){
        JPanel wrap = new JPanel(new BorderLayout(0,10));
        wrap.setOpaque(false);
        for(JComponent c : new JComponent[]{rental,telat,denda,total,cari,tagihan,metodeBayar,jumlahBayar,kembalian,statusBayar}) UI.input(c);
        telat.setEditable(false); denda.setEditable(false); total.setEditable(false); tagihan.setEditable(false); kembalian.setEditable(false); statusBayar.setEditable(false);
        JPanel g = new JPanel(new GridBagLayout());
        g.setOpaque(false);
        g.add(UI.label("Rental Berjalan"),UI.gbc(0,0)); g.add(rental,UI.gbc(1,0));
        g.add(UI.label("Tanggal Kembali Aktual"),UI.gbc(2,0)); g.add(aktual,UI.gbc(3,0));
        g.add(UI.label("Telat"),UI.gbc(0,1)); g.add(telat,UI.gbc(1,1));
        g.add(UI.label("Denda"),UI.gbc(2,1)); g.add(denda,UI.gbc(3,1));
        g.add(UI.label("Total Rental + Denda"),UI.gbc(0,2)); g.add(total,UI.gbc(1,2));
        g.add(UI.label("Tagihan Pengembalian"),UI.gbc(2,2)); g.add(tagihan,UI.gbc(3,2));
        g.add(UI.label("Metode Pembayaran"),UI.gbc(0,3)); g.add(metodeBayar,UI.gbc(1,3));
        g.add(UI.label("Jumlah Bayar"),UI.gbc(2,3)); g.add(jumlahBayar,UI.gbc(3,3));
        g.add(UI.label("Kembalian"),UI.gbc(0,4)); g.add(kembalian,UI.gbc(1,4));
        g.add(UI.label("Status Pembayaran"),UI.gbc(2,4)); g.add(statusBayar,UI.gbc(3,4));
        g.add(UI.label("Cari"),UI.gbc(0,5)); g.add(cari,UI.gbc(1,5));
        wrap.add(g, BorderLayout.CENTER);

        JPanel b = UI.leftFlow();
        JButton sim = UI.button("Simpan Pengembalian");
        JButton str = UI.darkButton("Cetak Struk Pengembalian");
        JButton hap = UI.dangerButton("Hapus Pengembalian");
        JButton res = UI.lightButton("Reset");
        JButton car = UI.darkButton("Cari");
        JButton ref = UI.lightButton("Refresh");
        sim.addActionListener(e -> save());
        str.addActionListener(e -> printSelected());
        hap.addActionListener(e -> deletePengembalian());
        res.addActionListener(e -> clear());
        car.addActionListener(e -> loadTable(cari.getText()));
        ref.addActionListener(e -> { cari.setText(""); refreshData(); });
        b.add(sim); b.add(str);
        if(AppSession.isAdmin()) b.add(hap);
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
        rental.removeAllItems();
        try(Connection c = DB.getConnection()){
            ResultSet d = c.createStatement().executeQuery("SELECT nominal_per_hari FROM denda WHERE status='Aktif' ORDER BY id_denda DESC LIMIT 1");
            if(d.next()) dendaPerHari = d.getBigDecimal(1); else dendaPerHari = new BigDecimal(50000);
            ResultSet r = c.createStatement().executeQuery("SELECT r.id_rental,p.nama,CONCAT(m.merk,' ',m.tipe,' - ',m.plat_nomor) mobil FROM rental r JOIN pelanggan p ON r.id_pelanggan=p.id_pelanggan JOIN mobil m ON r.id_mobil=m.id_mobil WHERE r.status='Berjalan' ORDER BY r.id_rental DESC");
            while(r.next()) rental.addItem(new ComboItem(r.getInt(1), "#" + r.getInt(1) + " - " + r.getString(2) + " - " + r.getString(3)));
        }catch(Exception e){ UI.error(this,e); }
        ambilRental();
    }

    void ambilRental(){
        ComboItem item = (ComboItem)rental.getSelectedItem();
        if(item == null){ rentalId = 0; mobilId = 0; telat.setText(""); denda.setText(""); total.setText(""); tagihan.setText(""); jumlahBayar.setText(""); kembalian.setText(""); statusBayar.setText(""); return; }
        rentalId = item.id;
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id_mobil,total,tanggal_kembali FROM rental WHERE id_rental=?")){
            ps.setInt(1,rentalId);
            ResultSet r = ps.executeQuery();
            if(r.next()){
                mobilId = r.getInt(1);
                totalRental = r.getBigDecimal(2);
            }
        }catch(Exception e){ }
        hitung();
    }

    void hitung(){
        if(rentalId == 0) return;
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT tanggal_kembali,total FROM rental WHERE id_rental=?")){
            ps.setInt(1,rentalId);
            ResultSet r = ps.executeQuery();
            if(r.next()){
                long diff = aktual.getSqlDate().getTime() - r.getDate("tanggal_kembali").getTime();
                int hari = (int)TimeUnit.MILLISECONDS.toDays(diff);
                if(hari < 0) hari = 0;
                currentDenda = dendaPerHari.multiply(new BigDecimal(hari));
                currentTotalAkhir = r.getBigDecimal("total").add(currentDenda);
                currentTagihan = currentDenda;
                telat.setText(String.valueOf(hari));
                denda.setText(DB.rupiah(currentDenda));
                total.setText(DB.rupiah(currentTotalAkhir));
                tagihan.setText(DB.rupiah(currentTagihan));
                hitungPembayaran();
            }
        }catch(Exception e){ }
    }

    void hitungPembayaran(){
        BigDecimal bayar = DB.money(jumlahBayar.getText());
        BigDecimal kembali = bayar.subtract(currentTagihan);
        if(kembali.compareTo(BigDecimal.ZERO) < 0) kembali = BigDecimal.ZERO;
        kembalian.setText(DB.rupiah(kembali));
        if(currentTagihan.compareTo(BigDecimal.ZERO) == 0){
            statusBayar.setText("Lunas");
        } else if(bayar.compareTo(currentTagihan) >= 0){
            statusBayar.setText("Lunas");
        } else {
            statusBayar.setText("Kurang");
        }
    }

    void clear(){
        selectedId = 0;
        telat.setText(""); denda.setText(""); total.setText(""); tagihan.setText(""); jumlahBayar.setText(""); kembalian.setText(""); statusBayar.setText("");
        metodeBayar.setSelectedIndex(0);
        table.clearSelection();
        loadCombo();
    }

    void save(){
        if(rentalId == 0){ UI.warn(this,"Pilih rental berjalan dulu."); return; }
        Connection c = null;
        try{
            c = DB.getConnection();
            c.setAutoCommit(false);
            PreparedStatement cek = c.prepareStatement("SELECT COUNT(*) FROM pengembalian WHERE id_rental=?");
            cek.setInt(1,rentalId);
            ResultSet cr = cek.executeQuery();
            if(cr.next() && cr.getInt(1) > 0){ UI.warn(this,"Rental ini sudah pernah dikembalikan."); return; }
            int t = Integer.parseInt(telat.getText());
            BigDecimal den = DB.money(denda.getText());
            BigDecimal tot = DB.money(total.getText());
            BigDecimal tag = DB.money(tagihan.getText());
            BigDecimal bayar = DB.money(jumlahBayar.getText());
            if(bayar.compareTo(tag) < 0){
                UI.warn(this,"Jumlah bayar masih kurang. Tagihan pengembalian: " + DB.rupiah(tag));
                return;
            }
            BigDecimal kembali = bayar.subtract(tag);
            if(kembali.compareTo(BigDecimal.ZERO) < 0) kembali = BigDecimal.ZERO;
            String metode = String.valueOf(metodeBayar.getSelectedItem());
            PreparedStatement ps = c.prepareStatement("INSERT INTO pengembalian(id_rental,tanggal_kembali_aktual,terlambat_hari,denda,total_akhir,tagihan_pengembalian,metode_pembayaran,jumlah_bayar,kembalian,status_pembayaran,id_petugas) VALUES(?,?,?,?,?,?,?,?,?,'Lunas',?)", Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1,rentalId);
            ps.setDate(2,aktual.getSqlDate());
            ps.setInt(3,t);
            ps.setBigDecimal(4,den);
            ps.setBigDecimal(5,tot);
            ps.setBigDecimal(6,tag);
            ps.setString(7,metode);
            ps.setBigDecimal(8,bayar);
            ps.setBigDecimal(9,kembali);
            if(AppSession.isPetugas()) ps.setInt(10,AppSession.userId); else ps.setNull(10,Types.INTEGER);
            ps.executeUpdate();
            ResultSet keys = ps.getGeneratedKeys();
            if(keys.next()) selectedId = keys.getInt(1);
            PreparedStatement up = c.prepareStatement("UPDATE rental SET status='Selesai' WHERE id_rental=?");
            up.setInt(1,rentalId); up.executeUpdate();
            PreparedStatement um = c.prepareStatement("UPDATE mobil SET status='Tersedia' WHERE id_mobil=?");
            um.setInt(1,mobilId); um.executeUpdate();
            c.commit();
            refreshData();
            UI.info(this,"Pengembalian tersimpan, pembayaran lunas, dan status mobil menjadi Tersedia.");
        }catch(Exception e){
            try { if(c != null) c.rollback(); } catch(Exception ex) {}
            UI.error(this,e);
        } finally { try { if(c != null) c.close(); } catch(Exception ex) {} }
    }

    void deletePengembalian(){
        if(selectedId == 0){ UI.warn(this,"Pilih data pengembalian dulu."); return; }
        if(!UI.confirm(this,"Hapus pengembalian ini? Rental akan kembali berstatus Berjalan dan mobil menjadi Dipinjam.")) return;
        Connection c = null;
        try{
            c = DB.getConnection();
            c.setAutoCommit(false);
            PreparedStatement q = c.prepareStatement("SELECT r.id_rental, r.id_mobil FROM pengembalian pg JOIN rental r ON pg.id_rental=r.id_rental WHERE pg.id_pengembalian=?");
            q.setInt(1, selectedId);
            ResultSet rs = q.executeQuery();
            if(!rs.next()){ UI.warn(this,"Data pengembalian tidak ditemukan."); return; }
            int rid = rs.getInt(1), mid = rs.getInt(2);
            PreparedStatement del = c.prepareStatement("DELETE FROM pengembalian WHERE id_pengembalian=?");
            del.setInt(1, selectedId); del.executeUpdate();
            PreparedStatement ur = c.prepareStatement("UPDATE rental SET status='Berjalan' WHERE id_rental=?");
            ur.setInt(1, rid); ur.executeUpdate();
            PreparedStatement um = c.prepareStatement("UPDATE mobil SET status='Dipinjam' WHERE id_mobil=?");
            um.setInt(1, mid); um.executeUpdate();
            c.commit();
            selectedId = 0;
            refreshData();
            UI.info(this,"Pengembalian dihapus.");
        }catch(Exception e){
            try { if(c != null) c.rollback(); } catch(Exception ex) {}
            UI.error(this,e);
        } finally { try { if(c != null) c.close(); } catch(Exception ex) {} }
    }

    void loadTable(String q){
        model.setRowCount(0);
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT pg.id_pengembalian,r.id_rental,p.nama,CONCAT(m.merk,' ',m.tipe,' - ',m.plat_nomor) mobil,pg.tanggal_kembali_aktual,pg.terlambat_hari,pg.denda,pg.total_akhir,pg.tagihan_pengembalian,pg.metode_pembayaran,pg.jumlah_bayar,pg.kembalian,pg.status_pembayaran FROM pengembalian pg JOIN rental r ON pg.id_rental=r.id_rental JOIN pelanggan p ON r.id_pelanggan=p.id_pelanggan JOIN mobil m ON r.id_mobil=m.id_mobil WHERE p.nama LIKE ? OR m.merk LIKE ? OR m.plat_nomor LIKE ? OR pg.metode_pembayaran LIKE ? ORDER BY pg.id_pengembalian DESC")){
            String s = "%" + q + "%";
            ps.setString(1,s); ps.setString(2,s); ps.setString(3,s); ps.setString(4,s);
            ResultSet r = ps.executeQuery();
            while(r.next()) model.addRow(new Object[]{ r.getInt(1), r.getInt(2), r.getString(3), r.getString(4), r.getDate(5), r.getInt(6), DB.rupiah(r.getBigDecimal(7)), DB.rupiah(r.getBigDecimal(8)), DB.rupiah(r.getBigDecimal(9)), r.getString(10), DB.rupiah(r.getBigDecimal(11)), DB.rupiah(r.getBigDecimal(12)), r.getString(13) });
        }catch(Exception e){ UI.error(this,e); }
    }

    void printSelected(){
        if(selectedId == 0 && table.getSelectedRow() >= 0){
            int r = table.convertRowIndexToModel(table.getSelectedRow());
            selectedId = Integer.parseInt(model.getValueAt(r,0).toString());
        }
        if(selectedId == 0){ UI.warn(this,"Pilih data pengembalian dulu."); return; }
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT pg.*,r.id_rental,r.tanggal_pinjam,r.tanggal_kembali,r.lama_sewa,r.total total_rental,r.jumlah_bayar bayar_rental,r.metode_pembayaran metode_rental,p.nama,p.no_hp,p.alamat,CONCAT(m.merk,' ',m.tipe,' - ',m.plat_nomor) mobil FROM pengembalian pg JOIN rental r ON pg.id_rental=r.id_rental JOIN pelanggan p ON r.id_pelanggan=p.id_pelanggan JOIN mobil m ON r.id_mobil=m.id_mobil WHERE pg.id_pengembalian=?")){
            ps.setInt(1, selectedId);
            ResultSet rs = ps.executeQuery();
            if(rs.next()){
                File dir = new File("struk"); dir.mkdirs();
                File f = new File(dir,"struk_pengembalian_" + selectedId + ".txt");
                PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
                String metode = rs.getString("metode_pembayaran");
                StrukUtil.header(w, "STRUK PENGEMBALIAN MOBIL", StrukUtil.noTransaksi("KMB", selectedId));
                StrukUtil.section(w, "Data Pelanggan");
                StrukUtil.row(w, "Nama", rs.getString("nama"));
                StrukUtil.row(w, "No. HP", rs.getString("no_hp"));
                StrukUtil.row(w, "Alamat", rs.getString("alamat"));
                StrukUtil.section(w, "Detail Rental");
                StrukUtil.row(w, "ID Rental", rs.getInt("id_rental"));
                StrukUtil.row(w, "Mobil", rs.getString("mobil"));
                StrukUtil.row(w, "Tgl Rental", rs.getDate("tanggal_pinjam") + " s/d " + rs.getDate("tanggal_kembali"));
                StrukUtil.row(w, "Lama Sewa", rs.getInt("lama_sewa") + " hari");
                StrukUtil.money(w, "Total Rental", rs.getBigDecimal("total_rental"));
                StrukUtil.row(w, "Bayar Rental", rs.getString("metode_rental") + " - " + DB.rupiah(rs.getBigDecimal("bayar_rental")));
                StrukUtil.section(w, "Detail Pengembalian");
                StrukUtil.row(w, "Tanggal Aktual", rs.getDate("tanggal_kembali_aktual"));
                StrukUtil.row(w, "Terlambat", rs.getInt("terlambat_hari") + " hari");
                StrukUtil.money(w, "Denda", rs.getBigDecimal("denda"));
                StrukUtil.money(w, "Total Akhir", rs.getBigDecimal("total_akhir"));
                StrukUtil.section(w, "Pembayaran Pengembalian");
                StrukUtil.money(w, "Tagihan", rs.getBigDecimal("tagihan_pengembalian"));
                StrukUtil.row(w, "Metode", metode);
                StrukUtil.money(w, "Jumlah Bayar", rs.getBigDecimal("jumlah_bayar"));
                StrukUtil.money(w, "Kembalian", rs.getBigDecimal("kembalian"));
                StrukUtil.row(w, "Status Bayar", rs.getString("status_pembayaran"));
                StrukUtil.footer(w, metode);
                w.close();
                UI.info(this,"Struk pengembalian tersimpan di: " + f.getPath());
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
