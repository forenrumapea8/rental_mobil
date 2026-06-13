package rentalmobil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class DashboardFrame extends JFrame {
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JPanel rootPanel;
    private JPanel sidebarPanel;
    private JPanel headerPanel;
    private JPanel menuPanel;
    private JPanel content;
    private JLabel lblTitle;
    private JLabel lblUser;
    private JButton btnDashboard;
    private JButton btnMobil;
    private JButton btnPelanggan;
    private JButton btnPetugas;
    private JButton btnRental;
    private JButton btnPengembalian;
    private JButton btnTarif;
    private JButton btnDenda;
    private JButton btnLaporan;
    private JButton btnHakAkses;
    private JButton btnBackup;
    private JButton btnAkun;
    private JButton btnLogout;
    // End of variables declaration//GEN-END:variables

    // Tidak dimasukkan ke area generated NetBeans supaya tidak hilang saat Form Designer dibuka.
    private JButton activeButton;

    public DashboardFrame() {
        initComponents();
        applyFrameTheme();
        configureInitialWindow();
        lblUser.setText(AppSession.roleLabel().toUpperCase() + " - " + AppSession.nama);
        applyRoleAccess();
        registerMenuActions();
        showPanel(AppSession.isAdmin() ? new HomePanel() : new PetugasHomePanel(), btnDashboard);
    }

    private void configureInitialWindow() {
        // Supaya setelah login dashboard langsung terbuka besar, bukan ukuran kecil.
        setMinimumSize(new Dimension(1260, 760));
        setPreferredSize(new Dimension(1260, 760));
        setSize(new Dimension(1260, 760));
        setLocationRelativeTo(null);

        // Untuk monitor Windows, ini membuat frame langsung maximize saat pertama tampil.
        SwingUtilities.invokeLater(() -> {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            resizeMainLayout();
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeMainLayout();
            }
        });
    }

    private void resizeMainLayout() {
        int w = Math.max(getContentPane().getWidth(), 1260);
        int h = Math.max(getContentPane().getHeight(), 720);

        sidebarPanel.setBounds(0, 0, 220, h);
        content.setBounds(230, 10, Math.max(1000, w - 250), Math.max(700, h - 20));
        sidebarPanel.revalidate();
        content.revalidate();
        getContentPane().repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        rootPanel = new JPanel();
        sidebarPanel = new JPanel();
        headerPanel = new JPanel();
        menuPanel = new JPanel();
        content = new JPanel();
        lblTitle = new JLabel("Rental Mobil");
        lblUser = new JLabel("User");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistem Manajemen Rental Mobil");
        setSize(new Dimension(1260, 760));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        getContentPane().setBackground(UI.BG);
        sidebarPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        sidebarPanel.setBackground(UI.NAVY);
        getContentPane().add(sidebarPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 720));
        sidebarPanel.add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 18, 190, 28));
        sidebarPanel.add(lblUser, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 52, 190, 24));
        btnDashboard = new JButton("Dashboard");
        sidebarPanel.add(btnDashboard, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 95, 190, 30));
        btnMobil = new JButton("Data Mobil");
        sidebarPanel.add(btnMobil, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 137, 190, 30));
        btnPelanggan = new JButton("Data Pelanggan");
        sidebarPanel.add(btnPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 179, 190, 30));
        btnPetugas = new JButton("Data Petugas");
        sidebarPanel.add(btnPetugas, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 221, 190, 30));
        btnRental = new JButton("Transaksi Rental");
        sidebarPanel.add(btnRental, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 263, 190, 30));
        btnPengembalian = new JButton("Pengembalian");
        sidebarPanel.add(btnPengembalian, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 305, 190, 30));
        btnTarif = new JButton("Tarif Sewa Mobil");
        sidebarPanel.add(btnTarif, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 347, 190, 30));
        btnDenda = new JButton("Pengaturan Denda");
        sidebarPanel.add(btnDenda, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 389, 190, 30));
        btnLaporan = new JButton("Laporan");
        sidebarPanel.add(btnLaporan, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 431, 190, 30));
        btnHakAkses = new JButton("Hak Akses User");
        sidebarPanel.add(btnHakAkses, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 473, 190, 30));
        btnBackup = new JButton("Backup & Restore");
        sidebarPanel.add(btnBackup, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 515, 190, 30));
        btnAkun = new JButton("Pengaturan Akun");
        sidebarPanel.add(btnAkun, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 557, 190, 30));
        btnLogout = new JButton("Logout");
        sidebarPanel.add(btnLogout, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 599, 190, 30));
        content.setLayout(new BorderLayout());
        content.setBackground(UI.BG);
        getContentPane().add(content, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 10, 1000, 700));
    }// </editor-fold>//GEN-END:initComponents


    private void applyFrameTheme() {
        getContentPane().setBackground(UI.BG);
        sidebarPanel.setBackground(UI.NAVY);
        content.setBackground(UI.BG);
        lblTitle.setForeground(Color.WHITE);
        lblUser.setForeground(new Color(203, 213, 225));
        lblTitle.setFont(UI.FONT_BOLD);
        lblUser.setFont(UI.FONT);
        JButton[] buttons = new JButton[]{btnDashboard, btnMobil, btnPelanggan, btnPetugas, btnRental, btnPengembalian, btnTarif, btnDenda, btnLaporan, btnHakAkses, btnBackup, btnAkun, btnLogout};
        for (JButton b : buttons) {
            UI.styleSidebarButton(b, false);
        }
    }

    private void setActiveMenu(JButton active) {
        activeButton = active;
        JButton[] buttons = new JButton[]{btnDashboard, btnMobil, btnPelanggan, btnPetugas, btnRental, btnPengembalian, btnTarif, btnDenda, btnLaporan, btnHakAkses, btnBackup, btnAkun, btnLogout};
        for (JButton b : buttons) {
            UI.styleSidebarButton(b, b == activeButton);
        }
    }

    private void applyRoleAccess() {
        if (!AppSession.isAdmin()) {
            btnMobil.setText("Status Mobil");
            btnPelanggan.setText("Tambah Pelanggan");
            btnLaporan.setText("Riwayat Transaksi");

            btnPetugas.setVisible(false);
            btnTarif.setVisible(false);
            btnDenda.setVisible(false);
            btnHakAkses.setVisible(false);
            btnBackup.setVisible(false);

            // AbsoluteLayout tidak cukup jika hanya memakai setBounds().
            // Karena itu constraint layout-nya ikut diganti agar posisi saat run benar-benar rapat.
            int x = 15;
            int w = 190;
            int h = 30;
            int y = 95;
            int gap = 42;
            JButton[] petugasButtons = new JButton[]{
                btnDashboard,
                btnMobil,
                btnPelanggan,
                btnRental,
                btnPengembalian,
                btnLaporan,
                btnAkun,
                btnLogout
            };
            for (JButton b : petugasButtons) {
                sidebarPanel.remove(b);
                sidebarPanel.add(b, new org.netbeans.lib.awtextra.AbsoluteConstraints(x, y, w, h));
                b.setBounds(x, y, w, h);
                UI.styleSidebarButton(b, b == activeButton);
                y += gap;
            }
            sidebarPanel.revalidate();
            sidebarPanel.repaint();
        }
    }

    private void registerMenuActions() {
        btnDashboard.addActionListener(e -> showPanel(AppSession.isAdmin() ? new HomePanel() : new PetugasHomePanel(), btnDashboard));
        btnMobil.addActionListener(e -> showPanel(new MobilPanel(), btnMobil));
        btnPelanggan.addActionListener(e -> showPanel(new PelangganPanel(), btnPelanggan));
        btnPetugas.addActionListener(e -> showPanel(new PetugasPanel(), btnPetugas));
        btnRental.addActionListener(e -> showPanel(new RentalPanel(), btnRental));
        btnPengembalian.addActionListener(e -> showPanel(new PengembalianPanel(), btnPengembalian));
        btnTarif.addActionListener(e -> showPanel(new TarifPanel(), btnTarif));
        btnDenda.addActionListener(e -> showPanel(new DendaPanel(), btnDenda));
        btnLaporan.addActionListener(e -> showPanel(new LaporanPanel(), btnLaporan));
        btnHakAkses.addActionListener(e -> showPanel(new HakAksesPanel(), btnHakAkses));
        btnBackup.addActionListener(e -> showPanel(new BackupRestorePanel(), btnBackup));
        btnAkun.addActionListener(e -> showPanel(new PengaturanAkunPanel(), btnAkun));
        btnLogout.addActionListener(e -> { if (UI.confirm(this, "Logout dari sistem?")) { AppSession.clear(); new LoginFrame().setVisible(true); dispose(); } });
    }

    private void showPanel(JPanel panel, JButton active) {
        setActiveMenu(active);
        content.removeAll();
        if (panel instanceof Refreshable) ((Refreshable) panel).refreshData();
        UI.decorate(panel);
        content.add(panel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }
}
