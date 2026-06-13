package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class TarifPanel extends JPanel implements Refreshable {
    JTable table = new JTable();
    DefaultTableModel model;
    JTextField mobil = new JTextField(), tarif = new JTextField(), cari = new JTextField();
    int idMobil = 0;

    public TarifPanel(){
        setLayout(new BorderLayout(0,14));
        setBackground(UI.BG);
        setBorder(new EmptyBorder(24,24,24,24));
        JLabel h = new JLabel("Kelola Tarif Sewa Mobil");
        h.setFont(UI.TITLE);
        add(h, BorderLayout.NORTH);

        JPanel main = UI.card();
        add(main, BorderLayout.CENTER);
        model = new DefaultTableModel(new Object[]{"ID","Mobil","Plat Nomor","Tarif/Hari","Status"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        main.add(form(), BorderLayout.NORTH);
        main.add(UI.table(table), BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(e -> pilih());
    }

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
