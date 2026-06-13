package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class TarifPanel extends JPanel implements Refreshable {
    DefaultTableModel model;
    int idMobil = 0;

    // Variables declaration - do not modify//GEN-BEGIN:variables
    JTable table = new JTable();
    JTextField mobil = new JTextField(), tarif = new JTextField(), cari = new JTextField();

    // GUI Builder fields
    private JLabel lblTitle;
    private JPanel mainPanel, formWrapPanel, formPanel, buttonPanel;
    private JScrollPane scrollPane;
    private JButton btnSave, btnReset, btnCari, btnRefresh;

    private javax.swing.JLabel lblMobilTerpilih, lblTarifHari, lblCari;
    // End of variables declaration//GEN-END:variables
    public TarifPanel(){
        initComponents();
        btnSave.addActionListener(e -> save());
        btnReset.addActionListener(e -> clear());
        btnCari.addActionListener(e -> load(cari.getText()));
        btnRefresh.addActionListener(e -> { cari.setText(""); load(""); });

        lblTitle.setText("Kelola Tarif Sewa Mobil");
        model = new DefaultTableModel(new Object[]{"ID","Mobil","Plat Nomor","Tarif/Hari","Status"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        for(JComponent c : new JComponent[]{mobil,tarif,cari}) UI.input(c);
        table.getSelectionModel().addListSelectionListener(e -> pilih());
        mobil.setEditable(false);
    }

    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setPreferredSize(new Dimension(1000, 620));
        setBackground(UI.BG);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        lblTitle = new JLabel("Tarif Sewa Mobil");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 18, 500, 28));
        lblMobilTerpilih = new JLabel("Mobil Terpilih");
        add(lblMobilTerpilih, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 64, 140, 22));
        add(mobil, new org.netbeans.lib.awtextra.AbsoluteConstraints(168, 60, 180, 26));
        lblTarifHari = new JLabel("Tarif/Hari");
        add(lblTarifHari, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 64, 140, 22));
        add(tarif, new org.netbeans.lib.awtextra.AbsoluteConstraints(508, 60, 180, 26));
        lblCari = new JLabel("Cari");
        add(lblCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 64, 100, 22));
        add(cari, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 60, 170, 26));
        btnSave = new JButton("Update Tarif");
        add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 112, 110, 28));
        btnReset = new JButton("Reset");
        add(btnReset, new org.netbeans.lib.awtextra.AbsoluteConstraints(138, 112, 80, 28));
        btnCari = new JButton("Cari");
        add(btnCari, new org.netbeans.lib.awtextra.AbsoluteConstraints(226, 112, 70, 28));
        btnRefresh = new JButton("Refresh");
        add(btnRefresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(304, 112, 90, 28));
        scrollPane = new JScrollPane(table);
        add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 155, 960, 435));
    }// </editor-fold>//GEN-END:initComponents
    JPanel form(){
        JPanel wrap = new JPanel(new BorderLayout(0,10));
        wrap.setOpaque(false);
        for(JComponent c : new JComponent[]{mobil,tarif,cari}) UI.input(c);
        mobil.setEditable(false);

        JPanel g = new JPanel(new GridBagLayout());
        g.setOpaque(false);
        g.add(UI.label("Mobil Terpilih"), UI.gbc(0,0)); g.add(mobil, UI.gbc(1,0));
        g.add(UI.label("Tarif/Hari"), UI.gbc(2,0)); g.add(tarif, UI.gbc(3,0));
        g.add(UI.label("Cari"), UI.gbc(0,1)); g.add(cari, UI.gbc(1,1));
        wrap.add(g, BorderLayout.CENTER);

        JPanel b = UI.leftFlow();
        JButton sim = UI.button("Update Tarif");
        JButton res = UI.lightButton("Reset");
        JButton car = UI.darkButton("Cari");
        JButton ref = UI.lightButton("Refresh");
        sim.addActionListener(e -> save()); res.addActionListener(e -> clear());
        car.addActionListener(e -> load(cari.getText())); ref.addActionListener(e -> { cari.setText(""); load(""); });
        b.add(sim); b.add(res); b.add(car); b.add(ref);
        wrap.add(b, BorderLayout.SOUTH);
        return wrap;
    }

    void pilih(){
        if(table.getSelectedRow() < 0) return;
        int r = table.convertRowIndexToModel(table.getSelectedRow());
        idMobil = Integer.parseInt(model.getValueAt(r,0).toString());
        mobil.setText(DB.str(model.getValueAt(r,1)) + " - " + DB.str(model.getValueAt(r,2)));
        tarif.setText(DB.str(model.getValueAt(r,3)).replace("Rp","").replace(".","").trim());
    }

    void clear(){
        idMobil = 0;
        mobil.setText(""); tarif.setText(""); table.clearSelection();
    }

    void save(){
        if(idMobil == 0){ UI.warn(this,"Pilih mobil yang akan diubah tarifnya."); return; }
        if(tarif.getText().trim().isEmpty()){ UI.warn(this,"Tarif wajib diisi."); return; }
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("UPDATE mobil SET tarif_per_hari=? WHERE id_mobil=?")){
            ps.setBigDecimal(1, DB.money(tarif.getText()));
            ps.setInt(2, idMobil);
            ps.executeUpdate();
            clear(); load(cari.getText());
            UI.info(this,"Tarif sewa mobil berhasil diperbarui.");
        }catch(Exception e){ UI.error(this,e); }
    }

    void load(String q){
        model.setRowCount(0);
        try(Connection c = DB.getConnection(); PreparedStatement ps = c.prepareStatement("SELECT id_mobil, CONCAT(merk,' ',tipe) mobil, plat_nomor, tarif_per_hari, status FROM mobil WHERE merk LIKE ? OR tipe LIKE ? OR plat_nomor LIKE ? ORDER BY merk")){
            String s = "%" + q + "%";
            ps.setString(1,s); ps.setString(2,s); ps.setString(3,s);
            ResultSet r = ps.executeQuery();
            while(r.next()) model.addRow(new Object[]{ r.getInt(1), r.getString(2), r.getString(3), DB.rupiah(r.getBigDecimal(4)), r.getString(5) });
        }catch(Exception e){ UI.error(this,e); }
    }

    public void refreshData(){ load(""); }
}
