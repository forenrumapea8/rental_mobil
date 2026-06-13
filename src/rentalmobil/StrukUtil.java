package rentalmobil;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StrukUtil {
    private static final int WIDTH = 48;

    public static String noTransaksi(String prefix, int id) {
        return prefix + "-" + String.format("%06d", id);
    }

    public static void header(PrintWriter w, String title, String nomor) {
        line(w);
        center(w, "RENTAL MOBIL RM");
        center(w, "Sistem Manajemen Rental Mobil");
        center(w, "Kediri, Jawa Timur");
        center(w, "Telp/WA: 08xx-xxxx-xxxx");
        line(w);
        center(w, title);
        row(w, "No. Transaksi", nomor);
        row(w, "Dicetak", new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()));
        row(w, "Kasir", kasir());
        line(w);
    }

    public static void section(PrintWriter w, String title) {
        w.println();
        w.println(title.toUpperCase());
        line(w);
    }

    public static void row(PrintWriter w, String label, Object value) {
        String v = value == null ? "-" : String.valueOf(value);
        if (v.trim().length() == 0) v = "-";
        w.println(padRight(label, 18) + ": " + v);
    }

    public static void money(PrintWriter w, String label, java.math.BigDecimal value) {
        row(w, label, DB.rupiah(value));
    }

    public static void line(PrintWriter w) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < WIDTH; i++) sb.append('-');
        w.println(sb.toString());
    }

    public static void footer(PrintWriter w, String metode) {
        line(w);
        String m = metode == null ? "" : metode;
        if ("QRIS".equalsIgnoreCase(m)) {
            w.println("Catatan: Pembayaran QRIS sah setelah status");
            w.println("pembayaran berhasil/lunas.");
        } else if ("Transfer".equalsIgnoreCase(m)) {
            w.println("Catatan: Simpan bukti transfer sebagai");
            w.println("bukti pendukung pembayaran.");
        } else {
            w.println("Catatan: Struk ini sah sebagai bukti transaksi.");
        }
        w.println();
        center(w, "Terima kasih atas kepercayaan Anda");
        center(w, "Hati-hati di jalan");
        line(w);
    }

    private static String kasir() {
        String nama = AppSession.nama == null || AppSession.nama.trim().isEmpty() ? "-" : AppSession.nama;
        String role = AppSession.role == null || AppSession.role.trim().isEmpty() ? "" : " (" + AppSession.roleLabel() + ")";
        return nama + role;
    }

    private static void center(PrintWriter w, String text) {
        if (text == null) text = "";
        if (text.length() >= WIDTH) { w.println(text); return; }
        int left = (WIDTH - text.length()) / 2;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < left; i++) sb.append(' ');
        sb.append(text);
        w.println(sb.toString());
    }

    private static String padRight(String s, int n) {
        if (s == null) s = "";
        if (s.length() >= n) return s;
        StringBuilder sb = new StringBuilder(s);
        while (sb.length() < n) sb.append(' ');
        return sb.toString();
    }
}
