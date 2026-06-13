package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class PelangganPanel extends JPanel implements Refreshable {
    DefaultTableModel model;
    int id = 0;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    JTable table = new JTable();
    JTextField nama = new JTextField(), nik = new JTextField(), hp = new JTextField(), alamat = new JTextField(), cari = new JTextField();
    JComboBox<String> jk = new JComboBox<String>(new String[]{"Laki-laki","Perempuan"});

    // GUI Builder fields
    private JLabel lblTitle;
    private JPanel mainPanel, formWrapPanel, formPanel, buttonPanel;
    private JScrollPane scrollPane;
    private JButton btnSave, btnDelete, btnReset, btnCari, btnRefresh;

    private javax.swing.JLabel lblNama, lblNIK, lblNoHP, lblAlamat, lblJenisKelamin, lblCari;
    // End of variables declaration//GEN-END:variables
    public PelangganPanel(){
        initComponents();
        btnSave.addActionListener(e -> save());
        btnDelete.addActionListener(e -> delete());
        btnReset.addActionListener(e -> clear());
        btnCari.addActionListener(e -> load(cari.getText()));
        btnRefresh.addActionListener(e -> { cari.setText(""); load(""); });

        jk.setModel(new DefaultComboBoxModel<String>(new String[]{"Laki-laki","Perempuan"}));

        lblTitle.setText(AppSession.isAdmin() ? "Data Pelanggan" : "Tambah Pelanggan");
        model = new DefaultTableModel(new Object[]{"ID","Nama","NIK","No HP","Alamat","Jenis Kelamin"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        for(JComponent c : new JComponent[]{nama,nik,hp,alamat,jk,cari,cari}) UI.input(c);
        table.getSelectionModel().addListSelectionListener(e -> pilih());
        btnDelete.setVisible(AppSession.isAdmin());
    }

    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setPreferredSize(new Dimension(1000, 620));
        setBackground(UI.BG);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        lblTitle = new JLabel("Data Pelanggan");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 18, 500, 28));
        lblNama = new JLabel("Nama");
        add(lblNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 140, 22));
        add(nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 60, 180, 26));
        lblNIK = new JLabel("NIK");
        add(lblNIK, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 64, 140, 22));
        add(nik, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 60, 180, 26));
        lblNoHP = new JLabel("No HP");
        add(lblNoHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 64, 100, 22));
        add(hp, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 60, 170, 26));
        lblAlamat = new JLabel("Alamat");
        add(lblAlamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 140, 22));
        add(alamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 100, 180, 26));
        lblJenisKelamin = new JLabel("Jenis Kelamin");
        add(lblJenisKelamin, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 104, 140, 22));
        add(jk, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 100, 180, 26));
        lblCari = new JLabel("Cari");
        add(lblCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 104, 100, 22));
        add(cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 100, 170, 26));
        btnSave = new JButton("Simpan");
        add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 152, 90, 28));
        btnDelete = new JButton("Hapus");
        add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 152, 90, 28));
        btnReset = new JButton("Reset");
        add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(216, 152, 80, 28));
        btnCari = new JButton("Cari");
        add(btnCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 152, 70, 28));
        btnRefresh = new JButton("Refresh");
        add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(382, 152, 90, 28));
        scrollPane = new JScrollPane(table);
        add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 960, 400));
    }// </editor-fold>//GEN-END:initComponents
    JPanel form(){
        JPanel wrap = new JPanel(new BorderLayout(0,10));
        wrap.setOpaque(false);
        JPanel g = new JPanel(new GridBagLayout());
        g.setOpaque(false);
        for(JComponent c : new JComponent[]{nama,nik,hp,alamat,cari,jk}) UI.input(c);
        g.add(UI.label("Nama"),UI.gbc(0,0)); g.add(nama,UI.gbc(1,0));
        g.add(UI.label("NIK"),UI.gbc(2,0)); g.add(nik,UI.gbc(3,0));
        g.add(UI.label("No HP"),UI.gbc(4,0)); g.add(hp,UI.gbc(5,0));
        g.add(UI.label("Alamat"),UI.gbc(0,1)); g.add(alamat,UI.gbc(1,1));
        g.add(UI.label("Jenis Kelamin"),UI.gbc(2,1)); g.add(jk,UI.gbc(3,1));
        g.add(UI.label("Cari"),UI.gbc(4,1)); g.add(cari,UI.gbc(5,1));
        wrap.add(g, BorderLayout.CENTER);

        JPanel b = UI.leftFlow();
        JButton sim = UI.button(AppSession.isAdmin() ? "Simpan" : "Tambah Pelanggan");
        JButton hap = UI.dangerButton("Hapus");
        JButton res = UI.lightButton("Reset");
        JButton car = UI.darkButton("Cari");
        JButton ref = UI.lightButton("Refresh");
        sim.addActionListener(e -> save());
        hap.addActionListener(e -> delete());
        res.addActionListener(e -> clear());
        car.addActionListener(e -> load(cari.getText()));
        ref.addActionListener(e -> { cari.setText(""); load(""); });
        b.add(sim);
        if(AppSession.isAdmin()) b.add(hap);
        b.add(res); b.add(car); b.add(ref);
        wrap.add(b, BorderLayout.SOUTH);
        return wrap;
    }

    void pilih(){
        if(table.getSelectedRow() < 0) return;
        int r = table.convertRowIndexToModel(table.getSelectedRow());
        id = Integer.parseInt(model.getValueAt(r,0).toString());
        nama.setText(DB.str(model.getValueAt(r,1)));
        nik.setText(DB.str(model.getValueAt(r,2)));
        hp.setText(DB.str(model.getValueAt(r,3)));
        alamat.setText(DB.str(model.getValueAt(r,4)));
        jk.setSelectedItem(DB.str(model.getValueAt(r,5)));
    }

    void clear(){
        id = 0;
        nama.setText(""); nik.setText(""); hp.setText(""); alamat.setText(""); jk.setSelectedIndex(0);
        table.clearSelection();
    }

    void save(){
        if(!AppSession.isAdmin() && id != 0){
            UI.warn(this,"Petugas hanya boleh menambah pelanggan baru, bukan mengubah data lama.");
            clear();
            return;
        }
        try(Connection c = DB.getConnection()){
            if(nama.getText().trim().isEmpty()){
                UI.warn(this,"Nama pelanggan wajib diisi."); return;
            }
            String sql = id == 0 ?
                "INSERT INTO pelanggan(nama,nik,no_hp,alamat,jenis_kelamin) VALUES(?,?,?,?,?)" :
                "UPDATE pelanggan SET nama=?,nik=?,no_hp=?,alamat=?,jenis_kelamin=? WHERE id_pelanggan=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,nama.getText().trim());
            ps.setString(2,nik.getText().trim());
            ps.setString(3,hp.getText().trim());
            ps.setString(4,alamat.getText().trim());
            ps.setString(5,jk.getSelectedItem().toString());
            if(id != 0) ps.setInt(6,id);
            ps.executeUpdate();
            clear(); load("");
            UI.info(this,"Data pelanggan tersimpan.");
        }catch(Exception e){ UI.error(this,e); }
    }

    void delete(){
        if(!AppSession.isAdmin()) return;
        if(id == 0){ UI.warn(this,"Pilih pelanggan dulu."); return; }
        if(!UI.confirm(this,"Hapus pelanggan?")) return;
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM pelanggan WHERE id_pelanggan=?")){
            ps.setInt(1,id);
            ps.executeUpdate();
            clear(); load("");
        }catch(Exception e){ UI.error(this,e); }
    }

    void load(String q){
        model.setRowCount(0);
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM pelanggan WHERE nama LIKE ? OR nik LIKE ? OR no_hp LIKE ? ORDER BY id_pelanggan DESC")){
            String s = "%" + q + "%";
            ps.setString(1,s); ps.setString(2,s); ps.setString(3,s);
            ResultSet r = ps.executeQuery();
            while(r.next()) model.addRow(new Object[]{
                r.getInt("id_pelanggan"), r.getString("nama"), r.getString("nik"), r.getString("no_hp"), r.getString("alamat"), r.getString("jenis_kelamin")
            });
        }catch(Exception e){ UI.error(this,e); }
    }

    public void refreshData(){ load(""); }
}
