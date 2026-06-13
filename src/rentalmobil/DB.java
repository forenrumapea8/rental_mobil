package rentalmobil;

import java.sql.*;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class DB {
    public static final String DB_NAME = "rental_mobil";
    public static final String SERVER_URL = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
    public static final String URL = "jdbc:mysql://localhost:3306/" + DB_NAME + "?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
    public static final String USER = "root";
    public static final String PASS = "";

    private static void loadDriver() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            Class.forName("com.mysql.jdbc.Driver");
        }
    }

    public static Connection getConnection() throws Exception {
        loadDriver();
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public static Connection getServerConnection() throws Exception {
        loadDriver();
        return DriverManager.getConnection(SERVER_URL, USER, PASS);
    }

    public static int count(String table) {
        try (Connection c = getConnection(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*) FROM " + table)) {
            if (r.next()) return r.getInt(1);
        } catch (Exception e) { }
        return 0;
    }

    public static int countWhere(String table, String where) {
        try (Connection c = getConnection(); Statement s = c.createStatement(); ResultSet r = s.executeQuery("SELECT COUNT(*) FROM " + table + " WHERE " + where)) {
            if (r.next()) return r.getInt(1);
        } catch (Exception e) { }
        return 0;
    }

    public static BigDecimal sum(String sql) {
        try (Connection c = getConnection(); Statement s = c.createStatement(); ResultSet r = s.executeQuery(sql)) {
            if (r.next()) return r.getBigDecimal(1) == null ? BigDecimal.ZERO : r.getBigDecimal(1);
        } catch (Exception e) { }
        return BigDecimal.ZERO;
    }

    public static String rupiah(BigDecimal n) {
        if (n == null) n = BigDecimal.ZERO;
        NumberFormat f = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        f.setMaximumFractionDigits(0);
        return f.format(n);
    }

    public static BigDecimal money(String s) {
        if (s == null || s.trim().isEmpty()) return BigDecimal.ZERO;
        String clean = s.replace("Rp", "").replace("rp", "").replace(".", "").replace(",", "").replace(" ", "").trim();
        if (clean.isEmpty()) return BigDecimal.ZERO;
        try { return new BigDecimal(clean); } catch(Exception e) { return BigDecimal.ZERO; }
    }

    public static String str(Object o) { return o == null ? "" : String.valueOf(o); }

    public static String sqlValue(Object o) {
        if (o == null) return "NULL";
        if (o instanceof Number) return o.toString();
        if (o instanceof java.sql.Date || o instanceof java.sql.Timestamp || o instanceof java.sql.Time) return "'" + o.toString() + "'";
        String s = o.toString().replace("\\", "\\\\").replace("'", "\\'");
        return "'" + s + "'";
    }
}
