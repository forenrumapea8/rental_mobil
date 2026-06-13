package rentalmobil;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.io.*;
import java.util.*;
import javax.swing.border.EmptyBorder;

public class BackupRestorePanel extends JPanel implements Refreshable {
    String[] tables = new String[]{"admin","petugas","mobil","pelanggan","denda","rental","pengembalian"};

    // Variables declaration - do not modify//GEN-BEGIN:variables
    JTextArea log = new JTextArea();
    private JTextArea noteArea;

    // GUI Builder fields
    private JLabel lblTitle;
    private JPanel mainPanel, topPanel, buttonPanel;
    private JScrollPane scrollPane;
    private JButton btnBackup, btnRestore;

    // End of variables declaration//GEN-END:variables
    public BackupRestorePanel(){
        initComponents();
        btnBackup.addActionListener(e -> backup());
        btnRestore.addActionListener(e -> restore());

    }

    @SuppressWarnings("unchecked")

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        setPreferredSize(new Dimension(1000, 620));
        setBackground(UI.BG);
        setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        lblTitle = new JLabel("Backup & Restore Database");
        add(lblTitle, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 18, 500, 28));
        topPanel = new JPanel(new org.netbeans.lib.awtextra.AbsoluteLayout());
        add(topPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 960, 120));
        noteArea = new JTextArea("Gunakan Backup untuk menyimpan struktur dan data database ke file .sql. Gunakan Restore hanya untuk file backup sistem ini karena proses restore akan menimpa database rental_mobil.");
        noteArea.setEditable(false); noteArea.setLineWrap(true); noteArea.setWrapStyleWord(true); noteArea.setFocusable(false);
        topPanel.add(noteArea, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, 55));
        btnBackup = new JButton("Backup Database");
        topPanel.add(btnBackup, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 70, 150, 28));
        btnRestore = new JButton("Restore Database");
        topPanel.add(btnRestore, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 70, 150, 28));
        log.setRows(12);
        log.setText("Log backup/restore akan tampil di sini.\n");
        scrollPane = new JScrollPane(log);
        add(scrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 960, 400));
    }// </editor-fold>//GEN-END:initComponents
    void append(String s){ log.append(s + "\n"); log.setCaretPosition(log.getDocument().getLength()); }

    void backup(){
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Simpan Backup Database");
        fc.setSelectedFile(new File("backup_rental_mobil_" + System.currentTimeMillis() + ".sql"));
        if(fc.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        if(!f.getName().toLowerCase().endsWith(".sql")) f = new File(f.getParentFile(), f.getName() + ".sql");
        try(Connection c = DB.getConnection(); PrintWriter w = new PrintWriter(new OutputStreamWriter(new FileOutputStream(f), "UTF-8"))){
            w.println("DROP DATABASE IF EXISTS `" + DB.DB_NAME + "`;");
            w.println("CREATE DATABASE `" + DB.DB_NAME + "` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;");
            w.println("USE `" + DB.DB_NAME + "`;");
            w.println("SET FOREIGN_KEY_CHECKS=0;");
            Statement st = c.createStatement();
            for(String t : tables){
                ResultSet cr = st.executeQuery("SHOW CREATE TABLE `" + t + "`");
                if(cr.next()){
                    w.println("\nDROP TABLE IF EXISTS `" + t + "`;");
                    w.println(cr.getString(2) + ";");
                }
            }
            for(String t : tables){
                ResultSet rs = st.executeQuery("SELECT * FROM `" + t + "`");
                ResultSetMetaData md = rs.getMetaData();
                int cols = md.getColumnCount();
                while(rs.next()){
                    StringBuilder sb = new StringBuilder();
                    sb.append("INSERT INTO `").append(t).append("` VALUES(");
                    for(int i=1;i<=cols;i++){
                        if(i>1) sb.append(",");
                        sb.append(DB.sqlValue(rs.getObject(i)));
                    }
                    sb.append(");");
                    w.println(sb.toString());
                }
            }
            w.println("SET FOREIGN_KEY_CHECKS=1;");
            append("Backup berhasil: " + f.getAbsolutePath());
            UI.info(this,"Backup database berhasil disimpan di: " + f.getAbsolutePath());
        }catch(Exception e){ append("Backup gagal: " + e.getMessage()); UI.error(this,e); }
    }

    void restore(){
        if(!UI.confirm(this,"Restore akan menimpa database rental_mobil. Lanjutkan?")) return;
        JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Pilih File Backup SQL");
        if(fc.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File f = fc.getSelectedFile();
        try(Connection c = DB.getServerConnection()){
            executeSqlFile(c, f);
            append("Restore berhasil dari: " + f.getAbsolutePath());
            UI.info(this,"Restore database berhasil. Silakan logout/login ulang jika data belum berubah di tampilan.");
        }catch(Exception e){ append("Restore gagal: " + e.getMessage()); UI.error(this,e); }
    }

    void executeSqlFile(Connection c, File f) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f), "UTF-8"));
        Statement st = c.createStatement();
        StringBuilder sql = new StringBuilder();
        String line;
        while((line = br.readLine()) != null){
            String trim = line.trim();
            if(trim.isEmpty() || trim.startsWith("--") || trim.startsWith("#")) continue;
            sql.append(line).append('\n');
            if(trim.endsWith(";")){
                String cmd = sql.toString();
                cmd = cmd.substring(0, cmd.lastIndexOf(';')).trim();
                if(!cmd.isEmpty()) st.execute(cmd);
                sql.setLength(0);
            }
        }
        if(sql.length() > 0) st.execute(sql.toString());
        br.close();
        st.close();
    }

    public void refreshData(){ }
}
