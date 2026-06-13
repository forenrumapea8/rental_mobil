package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class PetugasPanel extends JPanel implements Refreshable {
    DefaultTableModel model;
    int id = 0;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    JTable table = new JTable();
    JTextField nama = new JTextField(), user = new JTextField(), pass = new JTextField(), hp = new JTextField(), alamat = new JTextField(), cari = new JTextField();
    JComboBox<String> status = new JComboBox<String>(new String[]{"Aktif","Nonaktif"});

    // GUI Builder fields
    private JLabel lblTitle;
    private JPanel mainPanel, formWrapPanel, formPanel, buttonPanel;
    private JScrollPane scrollPane;
    private JButton btnSave, btnDelete, btnReset, btnCari, btnRefresh;

    private javax.swing.JLabel lblNama, lblUsername, lblPassword, lblNoHP, lblAlamat, lblStatus, lblCari;
    // End of variables declaration//GEN-END:variables
    public PetugasPanel(){
        initComponents();
        btnSave.addActionListener(e -> save());
        btnDelete.addActionListener(e -> delete());
        btnReset.addActionListener(e -> clear());
        btnCari.addActionListener(e -> load(cari.getText()));
        btnRefresh.addActionListener(e -> { cari.setText(""); load(""); });

        status.setModel(new DefaultComboBoxModel<String>(new String[]{"Aktif","Nonaktif"}));

        lblTitle.setText("Data Petugas");
        model = new DefaultTableModel(new Object[]{"ID","Nama","Username","No HP","Alamat","Status"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        for(JComponent c : new JComponent[]{nama,user,pass,hp,alamat,status,cari,cari}) UI.input(c);
        table.getSelectionModel().addListSelectionListener(e -> pilih());
    }

    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setPreferredSize(new Dimension(1000, 620));
        setBackground(UI.BG);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        lblTitle = new JLabel("Data Petugas");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 18, 500, 28));
        lblNama = new JLabel("Nama");
        add(lblNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 140, 22));
        add(nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 60, 180, 26));
        lblUsername = new JLabel("Username");
        add(lblUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 64, 140, 22));
        add(user, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 60, 180, 26));
        lblPassword = new JLabel("Password");
        add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 64, 100, 22));
        add(pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 60, 170, 26));
        lblNoHP = new JLabel("No HP");
        add(lblNoHP, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 140, 22));
        add(hp, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 100, 180, 26));
        lblAlamat = new JLabel("Alamat");
        add(lblAlamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 104, 140, 22));
        add(alamat, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 100, 180, 26));
        lblStatus = new JLabel("Status");
        add(lblStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 104, 100, 22));
        add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 100, 170, 26));
        lblCari = new JLabel("Cari");
        add(lblCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 144, 140, 22));
        add(cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 140, 180, 26));
        btnSave = new JButton("Simpan");
        add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 192, 90, 28));
        btnDelete = new JButton("Hapus");
        add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 192, 90, 28));
        btnReset = new JButton("Reset");
        add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(216, 192, 80, 28));
        btnCari = new JButton("Cari");
        add(btnCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 192, 70, 28));
        btnRefresh = new JButton("Refresh");
        add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(382, 192, 90, 28));
        scrollPane = new JScrollPane(table);
        add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 960, 360));
    }// </editor-fold>//GEN-END:initComponents
    JPanel form(){
        JPanel wrap = new JPanel(new BorderLayout(0,10));
        wrap.setOpaque(false);
        JPanel g = new JPanel(new GridBagLayout());
        g.setOpaque(false);
        for(JComponent c : new JComponent[]{nama,user,pass,hp,alamat,cari,status}) UI.input(c);
        g.add(UI.label("Nama"),UI.gbc(0,0)); g.add(nama,UI.gbc(1,0));
        g.add(UI.label("Username"),UI.gbc(2,0)); g.add(user,UI.gbc(3,0));
        g.add(UI.label("Password"),UI.gbc(4,0)); g.add(pass,UI.gbc(5,0));
        g.add(UI.label("No HP"),UI.gbc(0,1)); g.add(hp,UI.gbc(1,1));
        g.add(UI.label("Alamat"),UI.gbc(2,1)); g.add(alamat,UI.gbc(3,1));
        g.add(UI.label("Status"),UI.gbc(4,1)); g.add(status,UI.gbc(5,1));
        g.add(UI.label("Cari"),UI.gbc(0,2)); g.add(cari,UI.gbc(1,2));
        wrap.add(g, BorderLayout.CENTER);

        JPanel b = UI.leftFlow();
        JButton sim = UI.button("Simpan");
        JButton hap = UI.dangerButton("Hapus");
        JButton res = UI.lightButton("Reset");
        JButton car = UI.darkButton("Cari");
        JButton ref = UI.lightButton("Refresh");
        sim.addActionListener(e -> save());
        hap.addActionListener(e -> delete());
        res.addActionListener(e -> clear());
        car.addActionListener(e -> load(cari.getText()));
        ref.addActionListener(e -> { cari.setText(""); load(""); });
        b.add(sim); b.add(hap); b.add(res); b.add(car); b.add(ref);
        wrap.add(b, BorderLayout.SOUTH);
        return wrap;
    }

    void pilih(){
        if(table.getSelectedRow() < 0) return;
        int r = table.convertRowIndexToModel(table.getSelectedRow());
        id = Integer.parseInt(model.getValueAt(r,0).toString());
        nama.setText(DB.str(model.getValueAt(r,1)));
        user.setText(DB.str(model.getValueAt(r,2)));
        hp.setText(DB.str(model.getValueAt(r,3)));
        alamat.setText(DB.str(model.getValueAt(r,4)));
        status.setSelectedItem(DB.str(model.getValueAt(r,5)));
        pass.setText("");
    }

    void clear(){
        id = 0;
        nama.setText(""); user.setText(""); pass.setText(""); hp.setText(""); alamat.setText(""); status.setSelectedIndex(0);
        table.clearSelection();
    }

    void save(){
        try(Connection c = DB.getConnection()){
            if(nama.getText().trim().isEmpty() || user.getText().trim().isEmpty()){
                UI.warn(this,"Nama dan username wajib diisi."); return;
            }
            String sql = id == 0 ?
                "INSERT INTO petugas(nama,username,password,no_hp,alamat,status) VALUES(?,?,?,?,?,?)" :
                (pass.getText().trim().isEmpty() ?
                    "UPDATE petugas SET nama=?,username=?,no_hp=?,alamat=?,status=? WHERE id_petugas=?" :
                    "UPDATE petugas SET nama=?,username=?,password=?,no_hp=?,alamat=?,status=? WHERE id_petugas=?");
            PreparedStatement ps = c.prepareStatement(sql);
            if(id == 0){
                ps.setString(1,nama.getText().trim()); ps.setString(2,user.getText().trim());
                ps.setString(3,pass.getText().trim().isEmpty() ? "petugas" : pass.getText().trim());
                ps.setString(4,hp.getText().trim()); ps.setString(5,alamat.getText().trim()); ps.setString(6,status.getSelectedItem().toString());
            } else if(pass.getText().trim().isEmpty()){
                ps.setString(1,nama.getText().trim()); ps.setString(2,user.getText().trim()); ps.setString(3,hp.getText().trim());
                ps.setString(4,alamat.getText().trim()); ps.setString(5,status.getSelectedItem().toString()); ps.setInt(6,id);
            } else {
                ps.setString(1,nama.getText().trim()); ps.setString(2,user.getText().trim()); ps.setString(3,pass.getText().trim());
                ps.setString(4,hp.getText().trim()); ps.setString(5,alamat.getText().trim()); ps.setString(6,status.getSelectedItem().toString()); ps.setInt(7,id);
            }
            ps.executeUpdate();
            clear(); load("");
            UI.info(this,"Data petugas tersimpan.");
        }catch(Exception e){ UI.error(this,e); }
    }

    void delete(){
        if(id == 0){ UI.warn(this,"Pilih petugas dulu."); return; }
        if(!UI.confirm(this,"Hapus petugas?")) return;
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM petugas WHERE id_petugas=?")){
            ps.setInt(1,id);
            ps.executeUpdate();
            clear(); load("");
        }catch(Exception e){ UI.error(this,e); }
    }

    void load(String q){
        model.setRowCount(0);
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM petugas WHERE nama LIKE ? OR username LIKE ? OR no_hp LIKE ? ORDER BY id_petugas DESC")){
            String s = "%" + q + "%";
            ps.setString(1,s); ps.setString(2,s); ps.setString(3,s);
            ResultSet r = ps.executeQuery();
            while(r.next()) model.addRow(new Object[]{ r.getInt("id_petugas"), r.getString("nama"), r.getString("username"), r.getString("no_hp"), r.getString("alamat"), r.getString("status") });
        }catch(Exception e){ UI.error(this,e); }
    }

    public void refreshData(){ load(""); }
}
