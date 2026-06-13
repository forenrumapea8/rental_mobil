package rentalmobil;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.math.BigDecimal;

public class UI {
    public static final Color BG = new Color(241, 245, 249);          // background utama
    public static final Color CARD = new Color(255, 255, 255);        // area form/tabel
    public static final Color NAVY = new Color(15, 23, 42);           // navbar/sidebar
    public static final Color NAVY2 = new Color(30, 41, 59);          // button menu
    public static final Color NAVY_ACTIVE = new Color(37, 99, 235);   // menu aktif
    public static final Color BLUE = new Color(37, 99, 235);          // simpan/login
    public static final Color BLUE_DARK = new Color(29, 78, 216);     // cari/cetak
    public static final Color GREEN = new Color(22, 163, 74);         // sukses/simpan
    public static final Color ORANGE = new Color(245, 158, 11);       // batal/warning
    public static final Color PURPLE = new Color(124, 58, 237);       // cetak/laporan/backup
    public static final Color BORDER = new Color(203, 213, 225);
    public static final Color TEXT = new Color(15, 23, 42);
    public static final Color MUTED = new Color(71, 85, 105);
    public static final Color RED = new Color(220, 38, 38);           // hapus/logout
    public static final Color LIGHT_BUTTON = new Color(226, 232, 240);
    public static final Color WHITE = Color.WHITE;

    public static final Font FONT = new Font("Dialog", Font.PLAIN, 12);
    public static final Font FONT_BOLD = FONT.deriveFont(Font.BOLD);
    public static final Font TITLE = FONT.deriveFont(Font.BOLD, 18f);
    public static final Font SUBTITLE = FONT.deriveFont(Font.BOLD, 14f);

    public static void setup() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) {}
        UIManager.put("Panel.background", BG);
        UIManager.put("Label.foreground", TEXT);
        UIManager.put("Button.focus", new Color(147, 197, 253));
        UIManager.put("Button.disabledText", new Color(71, 85, 105));
        UIManager.put("TableHeader.background", NAVY2);
        UIManager.put("TableHeader.foreground", WHITE);
        UIManager.put("Table.selectionBackground", new Color(219, 234, 254));
        UIManager.put("Table.selectionForeground", TEXT);
    }

    public static JPanel page(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(BG);
        JLabel t = new JLabel(title);
        t.setFont(SUBTITLE);
        t.setForeground(TEXT);
        p.add(t, BorderLayout.NORTH);
        return p;
    }

    public static JPanel card() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(CARD);
        p.setBorder(new CompoundBorder(new LineBorder(BORDER), new EmptyBorder(10, 10, 10, 10)));
        return p;
    }

    public static JPanel softCard() { return card(); }

    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT);
        l.setFont(FONT);
        return l;
    }

    public static JLabel muted(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(MUTED);
        l.setFont(FONT);
        return l;
    }

    public static JTextArea note(String text) {
        JTextArea a = new JTextArea(text);
        a.setEditable(false);
        a.setFocusable(false);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        a.setBackground(BG);
        a.setForeground(TEXT);
        a.setFont(FONT);
        return a;
    }

    public static void input(JComponent c) {
        c.setFont(FONT);
        c.setForeground(TEXT);
        c.setBackground(Color.WHITE);
        c.setBorder(new LineBorder(BORDER));
        if (c instanceof JTextField) ((JTextField)c).setColumns(16);
    }

    public static JButton button(String text) { return styleByText(new JButton(text)); }
    public static JButton successButton(String text) { return styleButton(new JButton(text), GREEN, WHITE); }
    public static JButton darkButton(String text) { return styleButton(new JButton(text), BLUE_DARK, WHITE); }
    public static JButton dangerButton(String text) { return styleButton(new JButton(text), RED, WHITE); }
    public static JButton warningButton(String text) { return styleButton(new JButton(text), ORANGE, TEXT); }
    public static JButton lightButton(String text) { return styleButton(new JButton(text), LIGHT_BUTTON, TEXT); }
    public static JButton button(String text, Color bg, Color fg) { return styleButton(new JButton(text), bg, fg); }

    public static JButton styleByText(JButton b) {
        String t = b.getText() == null ? "" : b.getText().toLowerCase();
        if (t.contains("hapus") || t.contains("logout")) return styleButton(b, RED, WHITE);
        if (t.contains("batal")) return styleButton(b, ORANGE, TEXT);
        if (t.contains("cetak") || t.contains("laporan") || t.contains("backup") || t.contains("restore")) return styleButton(b, PURPLE, WHITE);
        if (t.contains("cari")) return styleButton(b, BLUE_DARK, WHITE);
        if (t.contains("refresh") || t.contains("reset") || t.contains("kalender") || t.contains("cek koneksi")) return styleButton(b, LIGHT_BUTTON, TEXT);
        if (t.contains("simpan") || t.contains("update") || t.contains("tambah") || t.contains("login")) return styleButton(b, GREEN, WHITE);
        return styleButton(b, BLUE, WHITE);
    }

    public static JButton styleButton(JButton b, Color bg, Color fg) {
        b.setUI(new BasicButtonUI());
        b.setFont(FONT);
        b.setBackground(bg);
        b.setForeground(fg);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(true);
        b.setFocusPainted(false);
        b.setRolloverEnabled(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new CompoundBorder(new LineBorder(darken(bg), 1), new EmptyBorder(4, 10, 4, 10)));
        return b;
    }

    public static JButton styleSidebarButton(JButton b, boolean active) {
        b.setUI(new BasicButtonUI());
        b.setFont(FONT);
        b.setBackground(active ? NAVY_ACTIVE : NAVY2);
        b.setForeground(WHITE);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setBorderPainted(true);
        b.setFocusPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setHorizontalAlignment(SwingConstants.LEFT);
        b.setBorder(new CompoundBorder(new LineBorder(active ? new Color(147, 197, 253) : new Color(51, 65, 85), 1), new EmptyBorder(4, 14, 4, 8)));
        return b;
    }

    private static Color darken(Color c) {
        return new Color(Math.max(c.getRed() - 25, 0), Math.max(c.getGreen() - 25, 0), Math.max(c.getBlue() - 25, 0));
    }

    public static JScrollPane table(JTable t) {
        styleTable(t);
        JScrollPane sp = new JScrollPane(t);
        sp.getViewport().setBackground(Color.WHITE);
        sp.setBorder(new LineBorder(BORDER));
        return sp;
    }

    public static void styleTable(JTable t) {
        t.setFillsViewportHeight(true);
        t.setAutoCreateRowSorter(true);
        t.setFont(FONT);
        t.setRowHeight(22);
        t.setForeground(TEXT);
        t.setBackground(Color.WHITE);
        t.setGridColor(BORDER);
        t.setSelectionBackground(new Color(219, 234, 254));
        t.setSelectionForeground(TEXT);
        JTableHeader h = t.getTableHeader();
        if (h != null) {
            h.setFont(FONT_BOLD);
            h.setForeground(WHITE);
            h.setBackground(NAVY2);
            h.setOpaque(true);
            DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
            headerRenderer.setFont(FONT_BOLD);
            headerRenderer.setForeground(WHITE);
            headerRenderer.setBackground(NAVY2);
            headerRenderer.setOpaque(true);
            headerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
            headerRenderer.setBorder(new CompoundBorder(new LineBorder(BORDER), new EmptyBorder(3, 6, 3, 6)));
            h.setDefaultRenderer(headerRenderer);
            h.repaint();
        }
    }

    public static void decorate(Container root) {
        if (root == null) return;
        if (root instanceof JPanel) {
            JPanel p = (JPanel) root;
            if (p.getBackground() == null || p.getBackground().equals(UIManager.getColor("Panel.background")) || isDefaultGray(p.getBackground())) {
                p.setBackground(BG);
            }
        }
        if (root instanceof JFrame) {
            ((JFrame) root).getContentPane().setBackground(BG);
        }
        for (Component c : root.getComponents()) {
            if (c instanceof JButton) styleByText((JButton) c);
            else if (c instanceof JTable) styleTable((JTable) c);
            else if (c instanceof JScrollPane) {
                JScrollPane sp = (JScrollPane) c;
                sp.getViewport().setBackground(Color.WHITE);
                sp.setBorder(new LineBorder(BORDER));
                Component view = sp.getViewport().getView();
                if (view instanceof JTable) styleTable((JTable) view);
            }
            else if (c instanceof JLabel) {
                JLabel l = (JLabel) c;
                l.setForeground(TEXT);
                l.setFont(FONT);
            }
            else if (c instanceof JTextField || c instanceof JComboBox || c instanceof JPasswordField || c instanceof JTextArea) {
                if (c instanceof JComponent) input((JComponent)c);
            }
            else if (c instanceof JCheckBox) {
                JCheckBox cb = (JCheckBox)c;
                cb.setForeground(TEXT);
                cb.setBackground(BG);
                cb.setFont(FONT);
            }
            if (c instanceof Container) decorate((Container)c);
        }
    }

    private static boolean isDefaultGray(Color c) {
        return c.equals(new Color(238,238,238)) || c.equals(new Color(240,240,240)) || c.equals(new Color(242,242,242));
    }

    public static GridBagConstraints gbc(int x, int y) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = x; g.gridy = y;
        g.insets = new Insets(4, 4, 4, 4);
        g.fill = GridBagConstraints.HORIZONTAL;
        g.weightx = 1;
        return g;
    }

    public static JPanel leftFlow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        p.setBackground(BG);
        return p;
    }

    public static void info(Component c, String m) { JOptionPane.showMessageDialog(c, m, "Informasi", JOptionPane.INFORMATION_MESSAGE); }
    public static void error(Component c, Exception e) { String msg = e.getMessage(); if (msg == null || msg.trim().isEmpty()) msg = e.getClass().getName(); JOptionPane.showMessageDialog(c, msg, "Error", JOptionPane.ERROR_MESSAGE); e.printStackTrace(); }
    public static void warn(Component c, String m) { JOptionPane.showMessageDialog(c, m, "Peringatan", JOptionPane.WARNING_MESSAGE); }
    public static boolean confirm(Component c, String m) { return JOptionPane.showConfirmDialog(c, m, "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; }
    public static BigDecimal money(String s) { return DB.money(s); }
    public static String rupiah(BigDecimal n) { return DB.rupiah(n); }
}
