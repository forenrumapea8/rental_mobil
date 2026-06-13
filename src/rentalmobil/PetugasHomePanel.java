package rentalmobil;

import javax.swing.*;
import java.awt.*;

public class PetugasHomePanel extends JPanel implements Refreshable {
    private JLabel lblTitle;
    private JLabel lblInfo;
    private JLabel lblMobilTersedia;
    private JLabel lblRentalAktif;
    private JLabel lblPelanggan;
    private JLabel lblRiwayat;
    private JLabel lblNote;

    public PetugasHomePanel() {
        initComponents();
    }

    private void initComponents() {
        setPreferredSize(new Dimension(1000, 620));
        setBackground(UI.BG);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        lblTitle = new JLabel("Dashboard Petugas");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 18, 500, 28));

        lblInfo = new JLabel("Ringkasan transaksi harian dan status mobil");
        add(lblInfo, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 48, 520, 24));

        add(new JLabel("Mobil Tersedia"), new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 100, 180, 24));
        lblMobilTersedia = new JLabel("0");
        add(lblMobilTersedia, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 100, 180, 24));

        add(new JLabel("Rental Aktif"), new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 140, 180, 24));
        lblRentalAktif = new JLabel("0");
        add(lblRentalAktif, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 180, 24));

        add(new JLabel("Data Pelanggan"), new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 180, 180, 24));
        lblPelanggan = new JLabel("0");
        add(lblPelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 180, 180, 24));

        add(new JLabel("Transaksi Selesai"), new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 180, 24));
        lblRiwayat = new JLabel("0");
        add(lblRiwayat, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 220, 180, 24));

        lblNote = new JLabel("Menu petugas: Status Mobil, Tambah Pelanggan, Transaksi Rental, Pengembalian, Riwayat Transaksi, Pengaturan Akun.");
        add(lblNote, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 285, 900, 24));
    }

    public void refreshData() {
        lblMobilTersedia.setText(String.valueOf(DB.countWhere("mobil", "status='Tersedia'")));
        lblRentalAktif.setText(String.valueOf(DB.countWhere("rental", "status='Berjalan'")));
        lblPelanggan.setText(String.valueOf(DB.count("pelanggan")));
        lblRiwayat.setText(String.valueOf(DB.countWhere("rental", "status='Selesai'")));
    }
}
