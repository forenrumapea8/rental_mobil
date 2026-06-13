package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class MobilPanel extends JPanel implements Refreshable {
    JTable table = new JTable();
    DefaultTableModel model;
    JTextField merk = new JTextField(), tipe = new JTextField(), plat = new JTextField(), tahun = new JTextField(), tarif = new JTextField(), cari = new JTextField();
    JComboBox<String> status = new JComboBox<String>(new String[]{"Tersedia","Dipinjam","Servis"});
    int id = 0;

    public MobilPanel(){
        setLayout(new BorderLayout(0,14));
        setBackground(UI.BG);
        setBorder(new EmptyBorder(24,24,24,24));
        JLabel h = new JLabel(AppSession.isAdmin() ? "Data Mobil" : "Status Mobil");
        h.setFont(UI.TITLE);
        add(h, BorderLayout.NORTH);

        JPanel main = UI.card();
        add(main, BorderLayout.CENTER);
        model = new DefaultTableModel(new Object[]{"ID","Merk","Tipe","Plat","Tahun","Tarif/Hari","Status"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        main.add(form(), BorderLayout.NORTH);
        main.add(UI.table(table), BorderLayout.CENTER);
        if (AppSession.isAdmin()) table.getSelectionModel().addListSelectionListener(e -> pilih());
    }

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
