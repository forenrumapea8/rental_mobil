package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.io.*;
import javax.swing.border.EmptyBorder;

public class LaporanPanel extends JPanel implements Refreshable {
    JTable table = new JTable();
    DefaultTableModel model;
    JComboBox<String> jenis;
    JTextField cari = new JTextField();

    public LaporanPanel(){
        setLayout(new BorderLayout(0,14));
        setBackground(UI.BG);
        setBorder(new EmptyBorder(24,24,24,24));
        JLabel h = new JLabel(AppSession.isAdmin() ? "Laporan Rental & Pendapatan" : "Riwayat Transaksi");
        h.setFont(UI.TITLE);
        add(h, BorderLayout.NORTH);

        if(AppSession.isAdmin()){
            jenis = new JComboBox<String>(new String[]{"Laporan Rental","Laporan Pengembalian","Status Mobil","Laporan Pendapatan"});
        } else {
            jenis = new JComboBox<String>(new String[]{"Riwayat Rental","Riwayat Pengembalian","Status Mobil"});
        }

        JPanel main = UI.card();
        add(main, BorderLayout.CENTER);
        model = new DefaultTableModel();
        table.setModel(model);
        main.add(filter(), BorderLayout.NORTH);
        main.add(UI.table(table), BorderLayout.CENTER);
    }

    JPanel filter(){
        JPanel p = UI.leftFlow();
        UI.input(jenis); UI.input(cari);
        JButton tampil = UI.button("Tampilkan");
        JButton cetak = UI.darkButton(AppSession.isAdmin() ? "Cetak Laporan" : "Cetak Riwayat");
        tampil.addActionListener(e -> load());
        cetak.addActionListener(e -> print());
        p.add(UI.label("Jenis")); p.add(jenis);
        p.add(UI.label("Cari")); p.add(cari);
        p.add(tampil); p.add(cetak);
        return p;
    }

    void setCols(Object[] c){
        model.setColumnCount(0);
        model.setRowCount(0);
        for(Object x : c) model.addColumn(x);
    }

    void load(){
        String j = jenis.getSelectedItem().toString();
        String q = "%" + cari.getText() + "%";
        try(Connection c = DB.getConnection()){
            if(j.equals("Laporan Rental") || j.equals("Riwayat Rental")){
                setCols(new Object[]{"ID","Pelanggan","Mobil","Pinjam","Kembali","Lama","Total","Status"});
                PreparedStatement ps = c.prepareStatement("SELECT r.id_rental,p.nama,CONCAT(m.merk,' ',m.tipe,' - ',m.plat_nomor),r.tanggal_pinjam,r.tanggal_kembali,r.lama_sewa,r.total,r.status FROM rental r JOIN pelanggan p ON r.id_pelanggan=p.id_pelanggan JOIN mobil m ON r.id_mobil=m.id_mobil WHERE p.nama LIKE ? OR m.merk LIKE ? OR m.plat_nomor LIKE ? OR r.status LIKE ? ORDER BY r.id_rental DESC");
                ps.setString(1,q); ps.setString(2,q); ps.setString(3,q); ps.setString(4,q);
                ResultSet r = ps.executeQuery();
                while(r.next()) model.addRow(new Object[]{ r.getInt(1), r.getString(2), r.getString(3), r.getDate(4), r.getDate(5), r.getInt(6), DB.rupiah(r.getBigDecimal(7)), r.getString(8) });
            } else if(j.equals("Laporan Pengembalian") || j.equals("Riwayat Pengembalian")){
                setCols(new Object[]{"ID","Rental","Pelanggan","Mobil","Aktual","Telat","Denda","Total Akhir"});
                PreparedStatement ps = c.prepareStatement("SELECT pg.id_pengembalian,r.id_rental,p.nama,CONCAT(m.merk,' ',m.tipe,' - ',m.plat_nomor),pg.tanggal_kembali_aktual,pg.terlambat_hari,pg.denda,pg.total_akhir FROM pengembalian pg JOIN rental r ON pg.id_rental=r.id_rental JOIN pelanggan p ON r.id_pelanggan=p.id_pelanggan JOIN mobil m ON r.id_mobil=m.id_mobil WHERE p.nama LIKE ? OR m.merk LIKE ? OR m.plat_nomor LIKE ? ORDER BY pg.id_pengembalian DESC");
                ps.setString(1,q); ps.setString(2,q); ps.setString(3,q);
                ResultSet r = ps.executeQuery();
                while(r.next()) model.addRow(new Object[]{ r.getInt(1), r.getInt(2), r.getString(3), r.getString(4), r.getDate(5), r.getInt(6), DB.rupiah(r.getBigDecimal(7)), DB.rupiah(r.getBigDecimal(8)) });
            } else if(j.equals("Status Mobil")){
                setCols(new Object[]{"ID","Merk","Tipe","Plat","Tarif/Hari","Status"});
                PreparedStatement ps = c.prepareStatement("SELECT * FROM mobil WHERE merk LIKE ? OR tipe LIKE ? OR plat_nomor LIKE ? OR status LIKE ? ORDER BY status,merk");
                ps.setString(1,q); ps.setString(2,q); ps.setString(3,q); ps.setString(4,q);
                ResultSet r = ps.executeQuery();
                while(r.next()) model.addRow(new Object[]{ r.getInt("id_mobil"), r.getString("merk"), r.getString("tipe"), r.getString("plat_nomor"), DB.rupiah(r.getBigDecimal("tarif_per_hari")), r.getString("status") });
            } else {
                setCols(new Object[]{"Keterangan","Nominal"});
                Statement s = c.createStatement();
                ResultSet r = s.executeQuery("SELECT COALESCE(SUM(total),0) FROM rental WHERE status<>'Batal'");
                if(r.next()) model.addRow(new Object[]{"Total Nilai Rental", DB.rupiah(r.getBigDecimal(1))});
                r = s.executeQuery("SELECT COALESCE(SUM(denda),0) FROM pengembalian");
                if(r.next()) model.addRow(new Object[]{"Total Denda", DB.rupiah(r.getBigDecimal(1))});
                r = s.executeQuery("SELECT COALESCE(SUM(total_akhir),0) FROM pengembalian");
                if(r.next()) model.addRow(new Object[]{"Total Pendapatan Pengembalian", DB.rupiah(r.getBigDecimal(1))});
                r = s.executeQuery("SELECT COUNT(*) FROM rental WHERE status='Selesai'");
                if(r.next()) model.addRow(new Object[]{"Jumlah Rental Selesai", r.getInt(1) + " transaksi"});
            }
        }catch(Exception e){ UI.error(this,e); }
    }

    void print(){
        if(model.getColumnCount() == 0) load();
        try{
            File dir = new File("struk"); dir.mkdirs();
            File f = new File(dir,"laporan_" + System.currentTimeMillis() + ".txt");
            PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"));
            w.println((AppSession.isAdmin() ? "LAPORAN " : "RIWAYAT ") + jenis.getSelectedItem());
            w.println("Dicetak oleh: " + AppSession.nama + " (" + AppSession.roleLabel() + ")");
            w.println("====================================================");
            for(int i=0;i<model.getColumnCount();i++) w.print(model.getColumnName(i) + "\t");
            w.println();
            for(int r=0;r<model.getRowCount();r++){
                for(int c=0;c<model.getColumnCount();c++) w.print(model.getValueAt(r,c) + "\t");
                w.println();
            }
            w.close();
            UI.info(this,"Laporan tersimpan di: " + f.getPath());
        }catch(Exception e){ UI.error(this,e); }
    }

    public void refreshData(){ load(); }
}
