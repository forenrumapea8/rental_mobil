package rentalmobil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class DendaPanel extends JPanel implements Refreshable {
    JTable table = new JTable();
    DefaultTableModel model;
    JTextField nama = new JTextField("Denda Keterlambatan"), nominal = new JTextField(), ket = new JTextField();
    JComboBox<String> status = new JComboBox<String>(new String[]{"Aktif","Nonaktif"});
    int id = 0;

    public DendaPanel(){
        setLayout(new BorderLayout(0,14));
        setBackground(UI.BG);
        setBorder(new EmptyBorder(24,24,24,24));
        JLabel h = new JLabel("Pengaturan Denda Keterlambatan");
        h.setFont(UI.TITLE);
        add(h, BorderLayout.NORTH);

        JPanel main = UI.card();
        add(main, BorderLayout.CENTER);
        model = new DefaultTableModel(new Object[]{"ID","Nama Denda","Nominal/Hari","Status","Keterangan"},0){ public boolean isCellEditable(int r,int c){ return false; } };
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
