package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class HakAksesPanel extends JPanel implements Refreshable {
    JTable table = new JTable();
    DefaultTableModel model;
    JComboBox<String> role = new JComboBox<String>(new String[]{"Admin","Petugas"});
    JComboBox<String> status = new JComboBox<String>(new String[]{"Aktif","Nonaktif"});
    JTextField nama = new JTextField(), username = new JTextField(), password = new JTextField(), nohp = new JTextField(), alamat = new JTextField(), cari = new JTextField();
    int selectedId = 0;
    String selectedRole = "";

    public HakAksesPanel(){
        setLayout(new BorderLayout(0,14));
        setBackground(UI.BG);
        setBorder(new EmptyBorder(24,24,24,24));
        JLabel h = new JLabel("Mengatur Hak Akses User");
        h.setFont(UI.TITLE);
        add(h, BorderLayout.NORTH);

        JPanel main = UI.card();
        add(main, BorderLayout.CENTER);
        model = new DefaultTableModel(new Object[]{"Role","ID","Nama","Username","No HP","Alamat","Status"},0){ public boolean isCellEditable(int r,int c){ return false; } };
        table.setModel(model);
        main.add(form(), BorderLayout.NORTH);
        main.add(UI.table(table), BorderLayout.CENTER);
        table.getSelectionModel().addListSelectionListener(e -> pilih());
        role.addActionListener(e -> toggleFields());
    }

    JPanel form(){
        JPanel wrap = new JPanel(new BorderLayout(0,10));
        wrap.setOpaque(false);
        for(JComponent c : new JComponent[]{role,status,nama,username,password,nohp,alamat,cari}) UI.input(c);
        JPanel g = new JPanel(new GridBagLayout());
        g.setOpaque(false);
        g.add(UI.label("Role"),UI.gbc(0,0)); g.add(role,UI.gbc(1,0));
        g.add(UI.label("Nama"),UI.gbc(2,0)); g.add(nama,UI.gbc(3,0));
        g.add(UI.label("Username"),UI.gbc(4,0)); g.add(username,UI.gbc(5,0));
        g.add(UI.label("Password"),UI.gbc(0,1)); g.add(password,UI.gbc(1,1));
        g.add(UI.label("No HP"),UI.gbc(2,1)); g.add(nohp,UI.gbc(3,1));
        g.add(UI.label("Alamat"),UI.gbc(4,1)); g.add(alamat,UI.gbc(5,1));
        g.add(UI.label("Status"),UI.gbc(0,2)); g.add(status,UI.gbc(1,2));
        g.add(UI.label("Cari"),UI.gbc(2,2)); g.add(cari,UI.gbc(3,2));
        wrap.add(g, BorderLayout.CENTER);

        JPanel b = UI.leftFlow();
        JButton sim = UI.button("Simpan User");
        JButton hap = UI.dangerButton("Hapus User");
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

    void toggleFields(){
        boolean petugas = "Petugas".equals(role.getSelectedItem().toString());
        nohp.setEnabled(petugas);
        alamat.setEnabled(petugas);
    }

    void pilih(){
        if(table.getSelectedRow() < 0) return;
        int r = table.convertRowIndexToModel(table.getSelectedRow());
        selectedRole = DB.str(model.getValueAt(r,0));
        selectedId = Integer.parseInt(model.getValueAt(r,1).toString());
        role.setSelectedItem(selectedRole);
        nama.setText(DB.str(model.getValueAt(r,2)));
        username.setText(DB.str(model.getValueAt(r,3)));
        nohp.setText(DB.str(model.getValueAt(r,4)));
        alamat.setText(DB.str(model.getValueAt(r,5)));
        status.setSelectedItem(DB.str(model.getValueAt(r,6)));
        password.setText("");
        toggleFields();
    }

    void clear(){
        selectedId = 0; selectedRole = "";
        role.setSelectedIndex(0); status.setSelectedIndex(0);
        nama.setText(""); username.setText(""); password.setText(""); nohp.setText(""); alamat.setText("");
        table.clearSelection();
        toggleFields();
    }

    boolean isCurrentAdmin(){
        return "Admin".equals(selectedRole) && selectedId == AppSession.userId;
    }

    void save(){
        String newRole = role.getSelectedItem().toString();
        String st = status.getSelectedItem().toString();
        if(nama.getText().trim().isEmpty() || username.getText().trim().isEmpty()){
            UI.warn(this,"Nama dan username wajib diisi."); return;
        }
        if(isCurrentAdmin() && (!"Admin".equals(newRole) || "Nonaktif".equals(st))){
            UI.warn(this,"Akun admin yang sedang login tidak boleh dinonaktifkan atau diubah menjadi petugas."); return;
        }
        try(Connection c = DB.getConnection()){
            if(selectedId == 0){
                insertUser(c, newRole);
            } else if(!selectedRole.equals(newRole)){
                insertUser(c, newRole);
                deleteUser(c, selectedRole, selectedId);
            } else {
                updateUser(c, newRole);
            }
            clear(); load("");
            UI.info(this,"Hak akses user tersimpan.");
        }catch(Exception e){ UI.error(this,e); }
    }

    void insertUser(Connection c, String r) throws Exception {
        String pwd = password.getText().trim().isEmpty() ? r.toLowerCase() : password.getText().trim();
        if("Admin".equals(r)){
            PreparedStatement ps = c.prepareStatement("INSERT INTO admin(nama,username,password,status) VALUES(?,?,?,?)");
            ps.setString(1,nama.getText().trim()); ps.setString(2,username.getText().trim()); ps.setString(3,pwd); ps.setString(4,status.getSelectedItem().toString());
            ps.executeUpdate();
        } else {
            PreparedStatement ps = c.prepareStatement("INSERT INTO petugas(nama,username,password,no_hp,alamat,status) VALUES(?,?,?,?,?,?)");
            ps.setString(1,nama.getText().trim()); ps.setString(2,username.getText().trim()); ps.setString(3,pwd); ps.setString(4,nohp.getText().trim()); ps.setString(5,alamat.getText().trim()); ps.setString(6,status.getSelectedItem().toString());
            ps.executeUpdate();
        }
    }

    void updateUser(Connection c, String r) throws Exception {
        if("Admin".equals(r)){
            PreparedStatement ps;
            if(password.getText().trim().isEmpty()){
                ps = c.prepareStatement("UPDATE admin SET nama=?,username=?,status=? WHERE id_admin=?");
                ps.setString(1,nama.getText().trim()); ps.setString(2,username.getText().trim()); ps.setString(3,status.getSelectedItem().toString()); ps.setInt(4,selectedId);
            } else {
                ps = c.prepareStatement("UPDATE admin SET nama=?,username=?,password=?,status=? WHERE id_admin=?");
                ps.setString(1,nama.getText().trim()); ps.setString(2,username.getText().trim()); ps.setString(3,password.getText().trim()); ps.setString(4,status.getSelectedItem().toString()); ps.setInt(5,selectedId);
            }
            ps.executeUpdate();
        } else {
            PreparedStatement ps;
            if(password.getText().trim().isEmpty()){
                ps = c.prepareStatement("UPDATE petugas SET nama=?,username=?,no_hp=?,alamat=?,status=? WHERE id_petugas=?");
                ps.setString(1,nama.getText().trim()); ps.setString(2,username.getText().trim()); ps.setString(3,nohp.getText().trim()); ps.setString(4,alamat.getText().trim()); ps.setString(5,status.getSelectedItem().toString()); ps.setInt(6,selectedId);
            } else {
                ps = c.prepareStatement("UPDATE petugas SET nama=?,username=?,password=?,no_hp=?,alamat=?,status=? WHERE id_petugas=?");
                ps.setString(1,nama.getText().trim()); ps.setString(2,username.getText().trim()); ps.setString(3,password.getText().trim()); ps.setString(4,nohp.getText().trim()); ps.setString(5,alamat.getText().trim()); ps.setString(6,status.getSelectedItem().toString()); ps.setInt(7,selectedId);
            }
            ps.executeUpdate();
        }
    }

    void delete(){
        if(selectedId == 0){ UI.warn(this,"Pilih user dulu."); return; }
        if(isCurrentAdmin()){ UI.warn(this,"Akun admin yang sedang login tidak boleh dihapus."); return; }
        if(!UI.confirm(this,"Hapus user ini?")) return;
        try(Connection c = DB.getConnection()){
            deleteUser(c, selectedRole, selectedId);
            clear(); load("");
            UI.info(this,"User berhasil dihapus.");
        }catch(Exception e){ UI.error(this,e); }
    }

    void deleteUser(Connection c, String r, int id) throws Exception {
        PreparedStatement ps;
        if("Admin".equals(r)) ps = c.prepareStatement("DELETE FROM admin WHERE id_admin=?");
        else ps = c.prepareStatement("DELETE FROM petugas WHERE id_petugas=?");
        ps.setInt(1,id);
        ps.executeUpdate();
    }

    void load(String q){
        model.setRowCount(0);
        try(Connection c = DB.getConnection()){
            String s = "%" + q + "%";
            PreparedStatement a = c.prepareStatement("SELECT id_admin,nama,username,status FROM admin WHERE nama LIKE ? OR username LIKE ? ORDER BY id_admin DESC");
            a.setString(1,s); a.setString(2,s);
            ResultSet r = a.executeQuery();
            while(r.next()) model.addRow(new Object[]{"Admin", r.getInt(1), r.getString(2), r.getString(3), "-", "-", r.getString(4)});
            PreparedStatement p = c.prepareStatement("SELECT id_petugas,nama,username,no_hp,alamat,status FROM petugas WHERE nama LIKE ? OR username LIKE ? OR no_hp LIKE ? ORDER BY id_petugas DESC");
            p.setString(1,s); p.setString(2,s); p.setString(3,s);
            r = p.executeQuery();
            while(r.next()) model.addRow(new Object[]{"Petugas", r.getInt(1), r.getString(2), r.getString(3), r.getString(4), r.getString(5), r.getString(6)});
        }catch(Exception e){ UI.error(this,e); }
    }

    public void refreshData(){ load(""); toggleFields(); }
}
