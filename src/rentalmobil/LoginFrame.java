package rentalmobil;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class LoginFrame extends JFrame {
    private JTextField     user   = new JTextField();
    private JPasswordField pass   = new JPasswordField();
    private JLabel         status = UI.muted("");
    private JButton        login  = UI.button("Login");
    private JButton        test   = UI.lightButton("Cek Koneksi");

    public LoginFrame() {
        setTitle("Login - Sistem Rental Mobil");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(860, 520);
        setMinimumSize(new Dimension(860, 520));
        setLocationRelativeTo(null);
        setResizable(false);

        /* ?? root ???????????????????????????????????????????????? */
        JPanel root = new JPanel(new BorderLayout());
        root.setBackground(UI.BG);
        root.setBorder(new EmptyBorder(28, 28, 28, 28));
        setContentPane(root);

        JPanel box = new JPanel(new GridLayout(1, 2, 26, 0));
        box.setBackground(Color.WHITE);
        box.setBorder(javax.swing.BorderFactory.createCompoundBorder(
                javax.swing.BorderFactory.createLineBorder(UI.BORDER),
                new EmptyBorder(0, 0, 0, 0)));
        root.add(box, BorderLayout.CENTER);

        /* ?? kiri (branding) ????????????????????????????????????? */
        JPanel left = new JPanel(null);
        left.setBackground(UI.NAVY);
        box.add(left);

        JLabel logo = new JLabel("RM", SwingConstants.CENTER);
        logo.setOpaque(true);
        logo.setBackground(UI.BLUE);
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logo.setBounds(30, 36, 60, 60);
        left.add(logo);

        JLabel appTitle = new JLabel("Rental Mobil");
        appTitle.setForeground(Color.WHITE);
        appTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        appTitle.setBounds(30, 140, 280, 40);
        left.add(appTitle);

        JTextArea desc = UI.note(
                "Sistem manajemen rental mobil.");
        desc.setForeground(new Color(226, 232, 240));
        desc.setBounds(30, 192, 290, 72);
        left.add(desc);

        JLabel foot = new JLabel("Java NetBeans + MySQL");
        foot.setForeground(new Color(148, 163, 184));
        foot.setFont(UI.FONT);
        foot.setBounds(30, 370, 260, 22);
        left.add(foot);

        /* ?? kanan (form) ???????????????????????????????????????? */
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Color.WHITE);
        right.setBorder(new EmptyBorder(30, 32, 24, 32));
        box.add(right);

        GridBagConstraints gc = new GridBagConstraints();
        gc.fill      = GridBagConstraints.HORIZONTAL;
        gc.weightx   = 1.0;
        gc.gridx     = 0;
        gc.gridy     = 0;
        gc.insets    = new Insets(0, 0, 0, 0);
        gc.anchor    = GridBagConstraints.NORTHWEST;

        /* judul */
        JLabel heading = new JLabel("Login");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 26));
        heading.setForeground(UI.TEXT);
        gc.gridy = 0; gc.insets = new Insets(0, 0, 20, 0);
        right.add(heading, gc);

        /* label Username */
        gc.gridy = 1; gc.insets = new Insets(0, 0, 4, 0);
        right.add(UI.label("Username"), gc);

        /* field Username */
        UI.input(user);
        user.setPreferredSize(new Dimension(0, 38));
        gc.gridy = 2; gc.insets = new Insets(0, 0, 14, 0);
        right.add(user, gc);

        /* label Password */
        gc.gridy = 3; gc.insets = new Insets(0, 0, 4, 0);
        right.add(UI.label("Password"), gc);

        /* field Password */
        UI.input(pass);
        pass.setPreferredSize(new Dimension(0, 38));
        gc.gridy = 4; gc.insets = new Insets(0, 0, 8, 0);
        right.add(pass, gc);

        /* checkbox tampilkan password */
        JCheckBox show = new JCheckBox("Tampilkan password");
        show.setOpaque(false);
        show.setForeground(UI.MUTED);
        show.setFont(UI.FONT);
        show.addActionListener(e -> pass.setEchoChar(show.isSelected() ? (char) 0 : '\u2022'));
        gc.gridy = 5; gc.insets = new Insets(0, 0, 16, 0);
        right.add(show, gc);

        /* tombol Login */
        login.setPreferredSize(new Dimension(0, 42));
        gc.gridy = 6; gc.insets = new Insets(0, 0, 8, 0);
        right.add(login, gc);

        /* tombol Cek Koneksi */
        test.setPreferredSize(new Dimension(0, 36));
        gc.gridy = 7; gc.insets = new Insets(0, 0, 12, 0);
        right.add(test, gc);

        /* status */
        gc.gridy = 8; gc.insets = new Insets(0, 0, 0, 0);
        right.add(status, gc);

        /* spacer bawah (dorong semua ke atas) */
        gc.gridy   = 9;
        gc.weighty = 1.0;
        gc.fill    = GridBagConstraints.BOTH;
        right.add(Box.createVerticalGlue(), gc);

        /* hint akun default */
        gc.gridy   = 10;
        gc.weighty = 0;
        gc.fill    = GridBagConstraints.HORIZONTAL;
        gc.insets  = new Insets(0, 0, 0, 0);
        JPanel hints = new JPanel(new GridLayout(2, 1, 0, 2));
        hints.setOpaque(false);
        hints.add(UI.muted(""));
        hints.add(UI.muted(""));
        right.add(hints, gc);

        /* ?? listener ???????????????????????????????????????????? */
        login.addActionListener(e -> doLogin());
        test.addActionListener(e -> testDB());
        getRootPane().setDefaultButton(login);
    }

    /* ?? Login ??????????????????????????????????????????????????? */
    private void doLogin() {
        String u = user.getText().trim();
        String p = new String(pass.getPassword()).trim();
        if (u.isEmpty() || p.isEmpty()) {
            status.setText("Username dan password wajib diisi.");
            UI.warn(this, "Username dan password wajib diisi.");
            return;
        }
        login.setEnabled(false);
        status.setText("Memproses login...");
        try (Connection c = DB.getConnection()) {

            /* cek admin */
            PreparedStatement ps = c.prepareStatement(
                    "SELECT id_admin,nama,username FROM admin WHERE username=? AND password=? AND status='Aktif'");
            ps.setString(1, u); ps.setString(2, p);
            ResultSet r = ps.executeQuery();
            if (r.next()) {
                AppSession.userId   = r.getInt(1);
                AppSession.nama     = r.getString(2);
                AppSession.username = r.getString(3);
                AppSession.role     = "admin";
                openDashboard(); return;
            }

            /* cek petugas */
            ps = c.prepareStatement(
                    "SELECT id_petugas,nama,username FROM petugas WHERE username=? AND password=? AND status='Aktif'");
            ps.setString(1, u); ps.setString(2, p);
            r = ps.executeQuery();
            if (r.next()) {
                AppSession.userId   = r.getInt(1);
                AppSession.nama     = r.getString(2);
                AppSession.username = r.getString(3);
                AppSession.role     = "petugas";
                openDashboard(); return;
            }

            status.setText("Login gagal. Cek username, password, atau status akun.");
            UI.warn(this, "Login gagal. Username/password salah atau akun nonaktif.");

        } catch (Exception ex) {
            status.setText("Gagal koneksi database. Pastikan MySQL aktif & database sudah di-import.");
            UI.error(this, ex);
        } finally {
            login.setEnabled(true);
        }
    }

    private void openDashboard() {
        new DashboardFrame().setVisible(true);
        dispose();
    }

    /* ?? Cek Koneksi ????????????????????????????????????????????? */
    private void testDB() {
        try (Connection c = DB.getConnection()) {
            status.setText("Koneksi database berhasil.");
            UI.info(this, "Koneksi database berhasil.");
        } catch (Exception e) {
            status.setText("Koneksi gagal. Pastikan MySQL aktif dan database sudah di-import.");
            UI.error(this, e);
        }
    }
}