package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class MobilPanel extends JPanel implements Refreshable {
    DefaultTableModel model;
    int id = 0;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    JTable table = new JTable();
    JTextField merk = new JTextField(), tipe = new JTextField(), plat = new JTextField(), tahun = new JTextField(), tarif = new JTextField(), cari = new JTextField();
    JComboBox<String> status = new JComboBox<String>(new String[]{"Tersedia","Dipinjam","Servis"});

    // GUI Builder fields
    private JLabel lblTitle;
    private JPanel mainPanel, formWrapPanel, formPanel, buttonPanel;
    private JScrollPane scrollPane;
    private JButton btnSave, btnDelete, btnReset, btnCari, btnRefresh;

    private javax.swing.JLabel lblMerk, lblTipe, lblPlat, lblTahun, lblTarifHari, lblStatus, lblCari;
    // End of variables declaration//GEN-END:variables
    public MobilPanel(){
        initComponents();
        btnSave.addActionListener(e -> save());
        btnDelete.addActionListener(e -> delete());
        btnReset.addActionListener(e -> clear());
        btnCari.addActionListener(e -> load(cari.getText()));
        btnRefresh.addActionListener(e -> { cari.setText(""); load(""); });

        status.setModel(new DefaultComboBoxModel<String>(new String[]{"Tersedia","Dipinjam","Servis"}));

        lblTitle.setText(AppSession.isAdmin() ? "Data Mobil" : "Status Mobil");
        model = new DefaultTableModel(new Object[]{"ID","Merk","Tipe","Plat","Tahun","Tarif/Hari","Status"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        for(JComponent c : new JComponent[]{merk,tipe,plat,tahun,tarif,status,cari,cari}) UI.input(c);
        if (AppSession.isAdmin()) table.getSelectionModel().addListSelectionListener(e -> pilih());
        btnSave.setVisible(AppSession.isAdmin()); btnDelete.setVisible(AppSession.isAdmin()); btnReset.setVisible(AppSession.isAdmin());
        if (!AppSession.isAdmin()) applyPetugasStatusLayout();
    }

    private void moveComponent(Component comp, int x, int y, int w, int h) {
        remove(comp);
        add(comp, new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, w, h));
        comp.setBounds(x, y, w, h);
    }

    private void applyPetugasStatusLayout() {
        for (JComponent c : new JComponent[]{lblMerk, merk, lblTipe, tipe, lblPlat, plat, lblTahun, tahun, lblTarifHari, tarif, lblStatus, status}) {
            c.setVisible(false);
        }
        lblCari.setText("Cari Mobil / Plat / Status");
        moveComponent(lblCari, 20, 64, 150, 22);
        moveComponent(cari, 180, 60, 240, 26);
        moveComponent(btnCari, 430, 60, 80, 28);
        moveComponent(btnRefresh, 520, 60, 100, 28);
        moveComponent(scrollPane, 20, 105, 960, 485);
        revalidate();
        repaint();
    }

    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setPreferredSize(new Dimension(1000, 620));
        setBackground(UI.BG);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        lblTitle = new JLabel("Data Mobil");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 18, 500, 28));
        lblMerk = new JLabel("Merk");
        add(lblMerk, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 140, 22));
        add(merk, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 60, 180, 26));
        lblTipe = new JLabel("Tipe");
        add(lblTipe, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 64, 140, 22));
        add(tipe, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 60, 180, 26));
        lblPlat = new JLabel("Plat");
        add(lblPlat, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 64, 100, 22));
        add(plat, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 60, 170, 26));
        lblTahun = new JLabel("Tahun");
        add(lblTahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 140, 22));
        add(tahun, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 100, 180, 26));
        lblTarifHari = new JLabel("Tarif/Hari");
        add(lblTarifHari, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 104, 140, 22));
        add(tarif, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 100, 180, 26));
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
        for(JComponent c : new JComponent[]{merk,tipe,plat,tahun,tarif,cari,status}) UI.input(c);

        if (AppSession.isAdmin()) {
            JPanel g = new JPanel(new GridBagLayout());
            g.setOpaque(false);
            g.add(UI.label("Merk"), UI.gbc(0,0)); g.add(merk, UI.gbc(1,0));
            g.add(UI.label("Tipe"), UI.gbc(2,0)); g.add(tipe, UI.gbc(3,0));
            g.add(UI.label("Plat"), UI.gbc(4,0)); g.add(plat, UI.gbc(5,0));
            g.add(UI.label("Tahun"), UI.gbc(0,1)); g.add(tahun, UI.gbc(1,1));
            g.add(UI.label("Tarif/Hari"), UI.gbc(2,1)); g.add(tarif, UI.gbc(3,1));
            g.add(UI.label("Status"), UI.gbc(4,1)); g.add(status, UI.gbc(5,1));
            g.add(UI.label("Cari"), UI.gbc(0,2)); g.add(cari, UI.gbc(1,2));
            wrap.add(g, BorderLayout.CENTER);
        } else {
            JPanel p = UI.leftFlow();
            p.add(UI.label("Cari Mobil/Plat"));
            p.add(cari);
            wrap.add(p, BorderLayout.CENTER);
        }

        JPanel b = UI.leftFlow();
        if (AppSession.isAdmin()) {
            JButton sim = UI.button("Simpan");
            JButton hap = UI.dangerButton("Hapus");
            JButton res = UI.lightButton("Reset");
            sim.addActionListener(e -> save());
            hap.addActionListener(e -> delete());
            res.addActionListener(e -> clear());
            b.add(sim); b.add(hap); b.add(res);
        }
        JButton car = UI.darkButton("Cari");
        JButton ref = UI.lightButton("Refresh");
        car.addActionListener(e -> load(cari.getText()));
        ref.addActionListener(e -> { cari.setText(""); load(""); });
        b.add(car); b.add(ref);
        wrap.add(b, BorderLayout.SOUTH);
        return wrap;
    }

    void pilih(){
        if (table.getSelectedRow() < 0) return;
        int r = table.convertRowIndexToModel(table.getSelectedRow());
        id = Integer.parseInt(model.getValueAt(r,0).toString());
        merk.setText(DB.str(model.getValueAt(r,1)));
        tipe.setText(DB.str(model.getValueAt(r,2)));
        plat.setText(DB.str(model.getValueAt(r,3)));
        tahun.setText(DB.str(model.getValueAt(r,4)));
        tarif.setText(DB.str(model.getValueAt(r,5)).replace("Rp","").replace(".","").trim());
        status.setSelectedItem(DB.str(model.getValueAt(r,6)));
    }

    void clear(){
        id = 0;
        merk.setText(""); tipe.setText(""); plat.setText(""); tahun.setText(""); tarif.setText("");
        status.setSelectedIndex(0);
        table.clearSelection();
    }

    void save(){
        if (!AppSession.isAdmin()) { UI.warn(this,"Petugas hanya boleh melihat data/status mobil."); return; }
        try(Connection c = DB.getConnection()){
            if(merk.getText().trim().isEmpty() || plat.getText().trim().isEmpty()){
                UI.warn(this,"Merk dan plat nomor wajib diisi."); return;
            }
            if(DB.money(tarif.getText()).signum() < 0){ UI.warn(this,"Tarif tidak valid."); return; }
            String sql = id == 0 ?
                "INSERT INTO mobil(merk,tipe,plat_nomor,tahun,tarif_per_hari,status) VALUES(?,?,?,?,?,?)" :
                "UPDATE mobil SET merk=?,tipe=?,plat_nomor=?,tahun=?,tarif_per_hari=?,status=? WHERE id_mobil=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1, merk.getText().trim());
            ps.setString(2, tipe.getText().trim());
            ps.setString(3, plat.getText().trim());
            ps.setString(4, tahun.getText().trim());
            ps.setBigDecimal(5, DB.money(tarif.getText()));
            ps.setString(6, status.getSelectedItem().toString());
            if(id != 0) ps.setInt(7, id);
            ps.executeUpdate();
            clear(); load("");
            UI.info(this,"Data mobil tersimpan.");
        }catch(Exception e){ UI.error(this,e); }
    }

    void delete(){
        if (!AppSession.isAdmin()) return;
        if(id == 0){ UI.warn(this,"Pilih data mobil dulu."); return; }
        if(!UI.confirm(this,"Hapus data mobil?")) return;
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM mobil WHERE id_mobil=?")){
            ps.setInt(1, id);
            ps.executeUpdate();
            clear(); load("");
        }catch(Exception e){ UI.error(this,e); }
    }

    void load(String q){
        model.setRowCount(0);
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT * FROM mobil WHERE merk LIKE ? OR tipe LIKE ? OR plat_nomor LIKE ? OR status LIKE ? ORDER BY id_mobil DESC")){
            String s = "%" + q + "%";
            ps.setString(1,s); ps.setString(2,s); ps.setString(3,s); ps.setString(4,s);
            ResultSet r = ps.executeQuery();
            while(r.next()) model.addRow(new Object[]{
                r.getInt("id_mobil"), r.getString("merk"), r.getString("tipe"), r.getString("plat_nomor"),
                r.getString("tahun"), DB.rupiah(r.getBigDecimal("tarif_per_hari")), r.getString("status")
            });
        }catch(Exception e){ UI.error(this,e); }
    }

    public void refreshData(){ load(""); }
}
