package rentalmobil;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.border.EmptyBorder;

public class DashboardFrame extends JFrame {
    private JPanel content = new JPanel(new BorderLayout());
    private java.util.List<JButton> menuButtons = new ArrayList<JButton>();

    public DashboardFrame(){
        setTitle("Sistem Manajemen Rental Mobil - " + AppSession.roleLabel());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setMinimumSize(new Dimension(1100, 680));

        JPanel root = new JPanel(new BorderLayout());
        setContentPane(root);
        root.add(sidebar(), BorderLayout.WEST);
        content.setBackground(UI.BG);
        root.add(content, BorderLayout.CENTER);
        showPanel(new HomePanel());
    }

    private JPanel sidebar(){
        JPanel s = new JPanel(new BorderLayout());
        s.setBackground(UI.NAVY);
        s.setPreferredSize(new Dimension(270, 0));

        JPanel top = new JPanel(new GridLayout(0, 1, 0, 5));
        top.setOpaque(false);
        top.setBorder(new EmptyBorder(26, 18, 16, 18));
        JLabel title = new JLabel("Rental Mobil");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        JLabel sub = new JLabel(AppSession.roleLabel().toUpperCase() + " - " + AppSession.nama);
        sub.setForeground(new Color(203, 213, 225));
        sub.setFont(UI.FONT);
        top.add(title);
        top.add(sub);
        s.add(top, BorderLayout.NORTH);

        JPanel menus = new JPanel();
        menus.setOpaque(false);
        menus.setLayout(new GridLayout(0, 1, 0, 8));
        menus.setBorder(new EmptyBorder(8, 16, 10, 16));

        addMenu(menus, "Dashboard", new HomePanel());
        if (AppSession.isAdmin()) {
            addMenu(menus, "Data Mobil", new MobilPanel());
            addMenu(menus, "Data Pelanggan", new PelangganPanel());
            addMenu(menus, "Data Petugas", new PetugasPanel());
            addMenu(menus, "Transaksi Rental", new RentalPanel());
            addMenu(menus, "Pengembalian", new PengembalianPanel());
            addMenu(menus, "Tarif Sewa Mobil", new TarifPanel());
            addMenu(menus, "Pengaturan Denda", new DendaPanel());
            addMenu(menus, "Laporan", new LaporanPanel());
            addMenu(menus, "Hak Akses User", new HakAksesPanel());
            addMenu(menus, "Backup & Restore", new BackupRestorePanel());
        } else {
            addMenu(menus, "Status Mobil", new MobilPanel());
            addMenu(menus, "Tambah Pelanggan", new PelangganPanel());
            addMenu(menus, "Transaksi Rental", new RentalPanel());
            addMenu(menus, "Pengembalian", new PengembalianPanel());
            addMenu(menus, "Riwayat Transaksi", new LaporanPanel());
        }
        addMenu(menus, "Pengaturan Akun", new PengaturanAkunPanel());

        JButton logout = menuButton("Logout");
        logout.addActionListener(e -> {
            if(UI.confirm(this,"Logout dari sistem?")){
                AppSession.clear();
                new LoginFrame().setVisible(true);
                dispose();
            }
        });
        menus.add(logout);
        s.add(menus, BorderLayout.CENTER);
        return s;
    }

    private JButton menuButton(String text){
        JButton b = UI.button(text, UI.NAVY, new Color(226,232,240));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(new EmptyBorder(12, 15, 12, 15));
        return b;
    }

    private void addMenu(JPanel menus, String title, JPanel panel){
        JButton b = menuButton(title);
        menuButtons.add(b);
        b.addActionListener(e -> {
            setActive(b);
            showPanel(panel);
        });
        menus.add(b);
    }

    private void setActive(JButton a){
        for(JButton b : menuButtons){
            b.setBackground(UI.NAVY);
            b.setForeground(new Color(226,232,240));
        }
        a.setBackground(UI.BLUE);
        a.setForeground(Color.WHITE);
    }

    private void showPanel(JPanel panel){
        content.removeAll();
        if(panel instanceof Refreshable) ((Refreshable)panel).refreshData();
        content.add(panel, BorderLayout.CENTER);
        content.revalidate();
        content.repaint();
    }
}
