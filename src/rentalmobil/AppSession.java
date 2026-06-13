package rentalmobil;

public class AppSession {
    public static int userId = 0;
    public static String nama = "";
    public static String username = "";
    public static String role = "";

    public static boolean isAdmin() { return "admin".equalsIgnoreCase(role); }
    public static boolean isPetugas() { return "petugas".equalsIgnoreCase(role); }
    public static String roleLabel() { return isAdmin() ? "Admin" : "Petugas"; }
    public static void clear() { userId = 0; nama = ""; username = ""; role = ""; }
}
