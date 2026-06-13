package rentalmobil;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel rootPanel;
    private JPanel formPanel;
    private JLabel lblTitle;
    private JLabel lblUsername;
    private JLabel lblPassword;
    private JTextField user;
    private JPasswordField pass;
    private JCheckBox showPassword;
    private JButton login;
    private JButton test;
    private JLabel status;
    // End of variables declaration//GEN-END:variables

    public LoginFrame() {
        initComponents();
        UI.decorate(this);
        login.addActionListener(e -> doLogin());
        test.addActionListener(e -> testDB());
        showPassword.addActionListener(e -> pass.setEchoChar(showPassword.isSelected() ? (char) 0 : '\u2022'));
        getRootPane().setDefaultButton(login);
        setLocationRelativeTo(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        rootPanel = new JPanel();
        formPanel = new JPanel();
        lblTitle = new JLabel("Login Sistem Rental Mobil");
        lblUsername = new JLabel("Username");
        user = new JTextField();
        lblPassword = new JLabel("Password");
        pass = new JPasswordField();
        showPassword = new JCheckBox("Tampilkan password");
        login = new JButton("Login");
        test = new JButton("Cek Koneksi");
        status = new JLabel(" ");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login - Sistem Rental Mobil");
        setSize(new Dimension(520, 420));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().setBackground(UI.BG);
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 25, 450, 30));
        getContentPane().add(lblUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 80, 380, 22));
        getContentPane().add(user, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 105, 380, 28));
        getContentPane().add(lblPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 145, 380, 22));
        getContentPane().add(pass, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 380, 28));
        getContentPane().add(showPassword, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 205, 250, 24));
        getContentPane().add(login, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 380, 32));
        getContentPane().add(test, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 285, 380, 32));
        getContentPane().add(status, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, 380, 24));
    }// </editor-fold>//GEN-END:initComponents

    private void doLogin() {
        String u = user.getText().trim();
        String p = new String(pass.getPassword()).trim();
        if (u.isEmpty() || p.isEmpty()) { status.setText("Username dan password wajib diisi."); UI.warn(this, "Username dan password wajib diisi."); return; }
        login.setEnabled(false); status.setText("Memproses login..."); boolean valid = false;
        try (Connection c = DB.getConnection()) {
            PreparedStatement ps = c.prepareStatement("SELECT id_admin,nama,username FROM admin WHERE username=? AND password=? AND status='Aktif'");
            ps.setString(1, u); ps.setString(2, p); ResultSet r = ps.executeQuery();
            if (r.next()) { AppSession.userId = r.getInt(1); AppSession.nama = r.getString(2); AppSession.username = r.getString(3); AppSession.role = "admin"; valid = true; }
            else { ps = c.prepareStatement("SELECT id_petugas,nama,username FROM petugas WHERE username=? AND password=? AND status='Aktif'"); ps.setString(1, u); ps.setString(2, p); r = ps.executeQuery(); if (r.next()) { AppSession.userId = r.getInt(1); AppSession.nama = r.getString(2); AppSession.username = r.getString(3); AppSession.role = "petugas"; valid = true; } }
        } catch (Exception ex) { status.setText("Gagal login karena database."); UI.error(this, ex); login.setEnabled(true); return; }
        if (!valid) { status.setText("Login gagal. Cek username/password/status akun."); UI.warn(this, "Login gagal. Username/password salah atau akun nonaktif."); login.setEnabled(true); return; }
        try {
            DashboardFrame dashboard = new DashboardFrame();
            dashboard.setExtendedState(JFrame.MAXIMIZED_BOTH);
            dashboard.setVisible(true);
            dispose();
        } catch (Exception ex) {
            status.setText("Login benar, tetapi dashboard gagal dibuka.");
            UI.error(this, ex);
            login.setEnabled(true);
        }
    }

    private void testDB() { try (Connection c = DB.getConnection()) { status.setText("Koneksi database berhasil."); UI.info(this, "Koneksi database berhasil."); } catch (Exception e) { status.setText("Koneksi gagal. Pastikan MySQL aktif dan database sudah di-import."); UI.error(this, e); } }
}
