package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class PetugasPanel extends JPanel implements Refreshable {
    JTable table = new JTable();
    DefaultTableModel model;
    JTextField nama = new JTextField(), user = new JTextField(), pass = new JTextField(), hp = new JTextField(), alamat = new JTextField(), cari = new JTextField();
    JComboBox<String> status = new JComboBox<String>(new String[]{"Aktif","Nonaktif"});
    int id = 0;

    public PetugasPanel(){
        setLayout(new BorderLayout(0,14));
        setBackground(UI.BG);
        setBorder(new EmptyBorder(24,24,24,24));
        JLabel h = new JLabel("Data Petugas");
        h.setFont(UI.TITLE);
        add(h, BorderLayout.NORTH);

        JPanel main = UI.card();
        add(main, BorderLayout.CENTER);
        model = new DefaultTableModel(new Object[]{"ID","Nama","Username","No HP","Alamat","Status"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        main.add(form(), BorderLayout.NORTH);
        main.add(UI.table(table), BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(e -> pilih());
    }

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
