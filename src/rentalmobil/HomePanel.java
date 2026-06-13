package rentalmobil;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import javax.swing.border.EmptyBorder;

public class HomePanel extends JPanel implements Refreshable {
    JLabel mobil = new JLabel("0"), pelanggan = new JLabel("0"), rental = new JLabel("0"), aktif = new JLabel("0"), pendapatan = new JLabel("Rp0"), tersedia = new JLabel("0");

    public HomePanel(){
        setLayout(new BorderLayout(0,18));
        setBackground(UI.BG);
        setBorder(new EmptyBorder(24,24,24,24));
        JLabel h = new JLabel("Dashboard");
        h.setFont(UI.TITLE);
        h.setForeground(UI.TEXT);
        add(h, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(2, 3, 16, 16));
        grid.setOpaque(false);
        grid.add(card("Data Mobil", mobil));
        grid.add(card("Mobil Tersedia", tersedia));
        grid.add(card("Data Pelanggan", pelanggan));
        grid.add(card("Total Rental", rental));
        grid.add(card("Rental Aktif", aktif));
        grid.add(card(AppSession.isAdmin() ? "Pendapatan" : "Riwayat Selesai", pendapatan));
        add(grid, BorderLayout.CENTER);
    }

    JPanel card(String title, JLabel val){
        JPanel c = UI.card();
        JLabel t = UI.muted(title);
        val.setFont(new Font("Segoe UI", Font.BOLD, 31));
        val.setForeground(UI.TEXT);
        c.add(t, BorderLayout.NORTH);
        c.add(val, BorderLayout.CENTER);
        return c;
    }

    public void refreshData(){
        mobil.setText(String.valueOf(DB.count("mobil")));
        tersedia.setText(String.valueOf(DB.countWhere("mobil", "status='Tersedia'")));
        pelanggan.setText(String.valueOf(DB.count("pelanggan")));
        rental.setText(String.valueOf(DB.count("rental")));
        aktif.setText(String.valueOf(DB.countWhere("rental", "status='Berjalan'")));
        if (AppSession.isAdmin()) {
            BigDecimal total = DB.sum("SELECT COALESCE(SUM(total_akhir),0) FROM pengembalian");
            if (total.compareTo(BigDecimal.ZERO) == 0) total = DB.sum("SELECT COALESCE(SUM(total),0) FROM rental WHERE status<>'Batal'");
            pendapatan.setText(DB.rupiah(total));
        } else {
            pendapatan.setText(String.valueOf(DB.countWhere("rental", "status='Selesai'")));
        }
    }
}
