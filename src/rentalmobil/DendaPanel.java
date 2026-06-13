package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class DendaPanel extends JPanel implements Refreshable {
    DefaultTableModel model;
    int id = 0;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    JTable table = new JTable();
    JTextField nama = new JTextField("Denda Keterlambatan"), nominal = new JTextField(), ket = new JTextField();
    JComboBox<String> status = new JComboBox<String>(new String[]{"Aktif","Nonaktif"});

    // GUI Builder fields
    private JLabel lblTitle;
    private JPanel mainPanel, formWrapPanel, formPanel, buttonPanel;
    private JScrollPane scrollPane;
    private JButton btnSave, btnDelete, btnReset;

    private javax.swing.JLabel lblNama, lblNominalHari, lblStatus, lblKeterangan;
    // End of variables declaration//GEN-END:variables
    public DendaPanel(){
        initComponents();
        btnSave.addActionListener(e -> save());
        btnDelete.addActionListener(e -> delete());
        btnReset.addActionListener(e -> clear());

        status.setModel(new DefaultComboBoxModel<String>(new String[]{"Aktif","Nonaktif"}));
        nama.setText("Denda Keterlambatan");

        lblTitle.setText("Pengaturan Denda Keterlambatan");
        model = new DefaultTableModel(new Object[]{"ID","Nama Denda","Nominal/Hari","Status","Keterangan"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        for(JComponent c : new JComponent[]{nama,nominal,ket,status}) UI.input(c);
        table.getSelectionModel().addListSelectionListener(e -> pilih());
    }

    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setPreferredSize(new Dimension(1000, 620));
        setBackground(UI.BG);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        lblTitle = new JLabel("Pengaturan Denda");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 18, 500, 28));
        lblNama = new JLabel("Nama");
        add(lblNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 140, 22));
        add(nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 60, 180, 26));
        lblNominalHari = new JLabel("Nominal/Hari");
        add(lblNominalHari, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 64, 140, 22));
        add(nominal, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 60, 180, 26));
        lblStatus = new JLabel("Status");
        add(lblStatus, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 64, 100, 22));
        add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 60, 170, 26));
        lblKeterangan = new JLabel("Keterangan");
        add(lblKeterangan, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 104, 140, 22));
        add(ket, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 100, 180, 26));
        btnSave = new JButton("Simpan");
        add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 152, 90, 28));
        btnDelete = new JButton("Hapus");
        add(btnDelete, new org.netbeans.lib.awtextra.AbsoluteConstraints(118, 152, 90, 28));
        btnReset = new JButton("Reset");
        add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(216, 152, 80, 28));
        scrollPane = new JScrollPane(table);
        add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 960, 400));
    }// </editor-fold>//GEN-END:initComponents
    JPanel form(){
        JPanel wrap = new JPanel(new BorderLayout(0,10));
        wrap.setOpaque(false);
        JPanel g = new JPanel(new GridBagLayout());
        g.setOpaque(false);
        for(JComponent c : new JComponent[]{nama,nominal,ket,status}) UI.input(c);
        g.add(UI.label("Nama"),UI.gbc(0,0)); g.add(nama,UI.gbc(1,0));
        g.add(UI.label("Nominal/Hari"),UI.gbc(2,0)); g.add(nominal,UI.gbc(3,0));
        g.add(UI.label("Status"),UI.gbc(4,0)); g.add(status,UI.gbc(5,0));
        g.add(UI.label("Keterangan"),UI.gbc(0,1)); g.add(ket,UI.gbc(1,1));
        wrap.add(g, BorderLayout.CENTER);

        JPanel b = UI.leftFlow();
        JButton sim = UI.button("Simpan");
        JButton hap = UI.dangerButton("Hapus");
        JButton res = UI.lightButton("Reset");
        sim.addActionListener(e -> save()); hap.addActionListener(e -> delete()); res.addActionListener(e -> clear());
        b.add(sim); b.add(hap); b.add(res);
        wrap.add(b, BorderLayout.SOUTH);
        return wrap;
    }

    void pilih(){
        if(table.getSelectedRow() < 0) return;
        int r = table.convertRowIndexToModel(table.getSelectedRow());
        id = Integer.parseInt(model.getValueAt(r,0).toString());
        nama.setText(DB.str(model.getValueAt(r,1)));
        nominal.setText(DB.str(model.getValueAt(r,2)).replace("Rp","").replace(".","").trim());
        status.setSelectedItem(DB.str(model.getValueAt(r,3)));
        ket.setText(DB.str(model.getValueAt(r,4)));
    }

    void clear(){
        id = 0;
        nama.setText("Denda Keterlambatan"); nominal.setText(""); ket.setText(""); status.setSelectedIndex(0);
        table.clearSelection();
    }

    void save(){
        try(Connection c = DB.getConnection()){
            if(nama.getText().trim().isEmpty() || nominal.getText().trim().isEmpty()){
                UI.warn(this,"Nama dan nominal denda wajib diisi."); return;
            }
            String sql = id == 0 ?
                "INSERT INTO denda(nama_denda,nominal_per_hari,status,keterangan) VALUES(?,?,?,?)" :
                "UPDATE denda SET nama_denda=?,nominal_per_hari=?,status=?,keterangan=? WHERE id_denda=?";
            PreparedStatement ps = c.prepareStatement(sql);
            ps.setString(1,nama.getText().trim());
            ps.setBigDecimal(2,DB.money(nominal.getText()));
            ps.setString(3,status.getSelectedItem().toString());
            ps.setString(4,ket.getText().trim());
            if(id != 0) ps.setInt(5,id);
            ps.executeUpdate();
            clear(); load();
            UI.info(this,"Denda tersimpan.");
        }catch(Exception e){ UI.error(this,e); }
    }

    void delete(){
        if(id == 0){ UI.warn(this,"Pilih denda dulu."); return; }
        if(!UI.confirm(this,"Hapus denda?")) return;
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("DELETE FROM denda WHERE id_denda=?")){
            ps.setInt(1,id);
            ps.executeUpdate();
            clear(); load();
        }catch(Exception e){ UI.error(this,e); }
    }

    void load(){
        model.setRowCount(0);
        try(Connection c = DB.getConnection(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT * FROM denda ORDER BY id_denda DESC")){
            while(r.next()) model.addRow(new Object[]{ r.getInt("id_denda"), r.getString("nama_denda"), DB.rupiah(r.getBigDecimal("nominal_per_hari")), r.getString("status"), r.getString("keterangan") });
        }catch(Exception e){ UI.error(this,e); }
    }

    public void refreshData(){ load(); }
}
