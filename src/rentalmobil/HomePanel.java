package rentalmobil;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;

public class HomePanel extends JPanel implements Refreshable {
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JLabel lblTitle;
    private JPanel gridPanel;
    private JLabel lblMobilTitle;
    private JLabel mobil;
    private JLabel lblTersediaTitle;
    private JLabel tersedia;
    private JLabel lblPelangganTitle;
    private JLabel pelanggan;
    private JLabel lblRentalTitle;
    private JLabel rental;
    private JLabel lblAktifTitle;
    private JLabel aktif;
    private JLabel lblPendapatanTitle;
    private JLabel pendapatan;
    // End of variables declaration//GEN-END:variables

    public HomePanel() { initComponents(); }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setPreferredSize(new Dimension(1000, 620));
        setBackground(UI.BG);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        lblTitle = new JLabel("Dashboard");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 18, 500, 28));
        lblMobilTitle = new JLabel("Data Mobil"); add(lblMobilTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 200, 24));
        mobil = new JLabel("0"); add(mobil, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 70, 150, 24));
        lblTersediaTitle = new JLabel("Mobil Tersedia"); add(lblTersediaTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 200, 24));
        tersedia = new JLabel("0"); add(tersedia, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 110, 150, 24));
        lblPelangganTitle = new JLabel("Data Pelanggan"); add(lblPelangganTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 200, 24));
        pelanggan = new JLabel("0"); add(pelanggan, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 150, 150, 24));
        lblRentalTitle = new JLabel("Total Rental"); add(lblRentalTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 200, 24));
        rental = new JLabel("0"); add(rental, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 150, 24));
        lblAktifTitle = new JLabel("Rental Aktif"); add(lblAktifTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 200, 24));
        aktif = new JLabel("0"); add(aktif, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 230, 150, 24));
        lblPendapatanTitle = new JLabel("Pendapatan/Riwayat Selesai"); add(lblPendapatanTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 270, 220, 24));
        pendapatan = new JLabel("Rp0"); add(pendapatan, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 270, 150, 24));
    }// </editor-fold>//GEN-END:initComponents

    public void refreshData() {
        mobil.setText(String.valueOf(DB.count("mobil")));
        tersedia.setText(String.valueOf(DB.countWhere("mobil", "status='Tersedia'")));
        pelanggan.setText(String.valueOf(DB.count("pelanggan")));
        rental.setText(String.valueOf(DB.count("rental")));
        aktif.setText(String.valueOf(DB.countWhere("rental", "status='Berjalan'")));
        if (AppSession.isAdmin()) {
            BigDecimal total = DB.sum("SELECT COALESCE(SUM(total_akhir),0) FROM pengembalian");
            if (total.compareTo(BigDecimal.ZERO) == 0) total = DB.sum("SELECT COALESCE(SUM(total),0) FROM rental WHERE status<>'Batal'");
            lblPendapatanTitle.setText("Pendapatan");
            pendapatan.setText(DB.rupiah(total));
        } else {
            lblPendapatanTitle.setText("Riwayat Selesai");
            pendapatan.setText(String.valueOf(DB.countWhere("rental", "status='Selesai'")));
        }
    }
}
