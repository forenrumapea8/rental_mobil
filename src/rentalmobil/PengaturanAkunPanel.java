package rentalmobil;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class PengaturanAkunPanel extends JPanel implements Refreshable {
    // Variables declaration - do not modify//GEN-BEGIN:variables
    JTextField nama = new JTextField(), username = new JTextField();
    JPasswordField password = new JPasswordField();
    private JLabel lblTitle;
    private JPanel mainPanel, formWrapPanel, formPanel, buttonPanel;
    private JButton btnSave;

    private javax.swing.JLabel lblNama, lblUsername, lblPasswordBaru, lblEmpty, lblNote;
    // End of variables declaration//GEN-END:variables
    public PengaturanAkunPanel(){ initComponents();
        btnSave.addActionListener(e -> save());
 UI.input(nama); UI.input(username); UI.input(password); }

    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setPreferredSize(new Dimension(1000, 620));
        setBackground(UI.BG);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        lblTitle = new JLabel("Pengaturan Akun");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 18, 500, 28));
        lblNama = new JLabel("Nama");
        add(lblNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 68, 130, 22));
        add(nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 64, 420, 26));
        lblUsername = new JLabel("Username");
        add(lblUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 108, 130, 22));
        add(username, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 104, 420, 26));
        lblPasswordBaru = new JLabel("Password Baru");
        add(lblPasswordBaru, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 148, 130, 22));
        add(password, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 144, 420, 26));
        lblNote = new JLabel("Kosongkan password jika tidak ingin mengubah.");
        add(lblNote, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 182, 420, 22));
        btnSave = new JButton("Simpan Akun");
        add(btnSave, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 220, 130, 28));
    }// </editor-fold>//GEN-END:initComponents
    void save(){ String table=AppSession.isAdmin()?"admin":"petugas"; String idcol=AppSession.isAdmin()?"id_admin":"id_petugas"; String pass=new String(password.getPassword()); try(Connection c=DB.getConnection()){ PreparedStatement ps; if(pass.trim().isEmpty()){ ps=c.prepareStatement("UPDATE "+table+" SET nama=?,username=? WHERE "+idcol+"=?"); ps.setString(1,nama.getText()); ps.setString(2,username.getText()); ps.setInt(3,AppSession.userId); } else { ps=c.prepareStatement("UPDATE "+table+" SET nama=?,username=?,password=? WHERE "+idcol+"=?"); ps.setString(1,nama.getText()); ps.setString(2,username.getText()); ps.setString(3,pass); ps.setInt(4,AppSession.userId); } ps.executeUpdate(); AppSession.nama=nama.getText(); AppSession.username=username.getText(); password.setText(""); UI.info(this,"Akun berhasil diperbarui."); } catch(Exception e){ UI.error(this,e); } }
    public void refreshData(){ nama.setText(AppSession.nama); username.setText(AppSession.username); password.setText(""); }
}
