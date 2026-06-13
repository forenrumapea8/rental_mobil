package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class PelangganPanel extends JPanel implements Refreshable {
    JTable table = new JTable();
    DefaultTableModel model;
    JTextField nama = new JTextField(), nik = new JTextField(), hp = new JTextField(), alamat = new JTextField(), cari = new JTextField();
    JComboBox<String> jk = new JComboBox<String>(new String[]{"Laki-laki","Perempuan"});
    int id = 0;

    public PelangganPanel(){
        setLayout(new BorderLayout(0,14));
        setBackground(UI.BG);
        setBorder(new EmptyBorder(24,24,24,24));
        JLabel h = new JLabel(AppSession.isAdmin() ? "Data Pelanggan" : "Tambah Pelanggan");
        h.setFont(UI.TITLE);
        add(h, BorderLayout.NORTH);

        JPanel main = UI.card();
        add(main, BorderLayout.CENTER);
        model = new DefaultTableModel(new Object[]{"ID","Nama","NIK","No HP","Alamat","Jenis Kelamin"},0){ public boolean isCellEditable(int r,int c){ return false; } };
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
