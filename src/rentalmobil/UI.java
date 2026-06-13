package rentalmobil;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

public class UI {
    public static final Color BG = new Color(246, 248, 252);
    public static final Color CARD = Color.WHITE;
    public static final Color NAVY = new Color(15, 23, 42);
    public static final Color NAVY2 = new Color(30, 41, 59);
    public static final Color BLUE = new Color(37, 99, 235);
    public static final Color BLUE_DARK = new Color(29, 78, 216);
    public static final Color GREEN = new Color(22, 163, 74);
    public static final Color ORANGE = new Color(234, 88, 12);
    public static final Color BORDER = new Color(221, 226, 235);
    public static final Color TEXT = new Color(17, 24, 39);
    public static final Color MUTED = new Color(71, 85, 105);
    public static final Color RED = new Color(220, 38, 38);
    public static final Font FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font TITLE = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);

    public static void setup() {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch(Exception e) {}
        UIManager.put("Label.font", FONT);
        UIManager.put("Button.font", FONT_BOLD);
        UIManager.put("TextField.font", FONT);
        UIManager.put("PasswordField.font", FONT);
        UIManager.put("ComboBox.font", FONT);
        UIManager.put("Table.font", FONT);
        UIManager.put("TableHeader.font", FONT_BOLD);
        UIManager.put("OptionPane.messageFont", FONT);
        UIManager.put("OptionPane.buttonFont", FONT_BOLD);
    }

    public static JPanel page(String title) {
        JPanel p = new JPanel(new BorderLayout(0, 16));
        p.setBackground(BG);
        p.setBorder(new EmptyBorder(24, 24, 24, 24));
        JLabel t = new JLabel(title);
        t.setFont(TITLE);
        t.setForeground(TEXT);
        p.add(t, BorderLayout.NORTH);
        return p;
    }

    public static JPanel card() {
        JPanel p = new JPanel(new BorderLayout(0, 12));
        p.setBackground(CARD);
        p.setBorder(new CompoundBorder(new LineBorder(BORDER), new EmptyBorder(18, 18, 18, 18)));
        return p;
    }

    public static JPanel softCard() {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(Color.WHITE);
        p.setBorder(new CompoundBorder(new LineBorder(new Color(229, 234, 242)), new EmptyBorder(16, 16, 16, 16)));
        return p;
    }

    public static JLabel label(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(TEXT);
        l.setFont(FONT_BOLD);
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
        a.setOpaque(false);
        a.setEditable(false);
        a.setFocusable(false);
        a.setForeground(MUTED);
        a.setFont(FONT);
        a.setLineWrap(true);
        a.setWrapStyleWord(true);
        return a;
    }

    public static void input(JComponent c) {
        c.setFont(FONT);
        c.setForeground(TEXT);
        c.setBackground(Color.WHITE);
        c.setBorder(new CompoundBorder(new LineBorder(new Color(203, 213, 225)), new EmptyBorder(8, 10, 8, 10)));
        if (c instanceof JTextField) ((JTextField)c).setColumns(16);
        if (c instanceof JComboBox) ((JComboBox)c).setFocusable(false);
    }

    public static JButton button(String text) { return button(text, BLUE, Color.WHITE); }
    public static JButton successButton(String text) { return button(text, GREEN, Color.WHITE); }
    public static JButton darkButton(String text) { return button(text, NAVY2, Color.WHITE); }
    public static JButton dangerButton(String text) { return button(text, RED, Color.WHITE); }
    public static JButton warningButton(String text) { return button(text, ORANGE, Color.WHITE); }
    public static JButton lightButton(String text) { return button(text, new Color(226, 232, 240), TEXT); }

    public static JButton button(String text, Color bg, Color fg) {
        JButton b = new JButton(text);
        b.setFont(FONT_BOLD);
        b.setForeground(fg);
        b.setBackground(bg);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setOpaque(true);
        b.setContentAreaFilled(true);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        b.setBorder(new EmptyBorder(10, 14, 10, 14));
        b.setUI(new javax.swing.plaf.basic.BasicButtonUI());
        b.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { if (b.isEnabled()) b.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { b.setBackground(bg); }
        });
        return b;
    }

    public static JScrollPane table(JTable t) {
        t.setRowHeight(32);
        t.setGridColor(new Color(226, 232, 240));
        t.setSelectionBackground(new Color(219, 234, 254));
        t.setSelectionForeground(TEXT);
        t.setFillsViewportHeight(true);
        t.getTableHeader().setBackground(new Color(241, 245, 249));
        t.getTableHeader().setForeground(TEXT);
        t.getTableHeader().setReorderingAllowed(false);
        t.setAutoCreateRowSorter(true);
        return new JScrollPane(t);
    }

    public static GridBagConstraints gbc(int x, int y) {
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = x; g.gridy = y; g.insets = new Insets(6, 6, 6, 6);
        g.fill = GridBagConstraints.HORIZONTAL; g.weightx = 1;
        return g;
    }

    public static JPanel leftFlow() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        p.setOpaque(false);
        return p;
    }

    public static void info(Component c, String m) { JOptionPane.showMessageDialog(c, m, "Informasi", JOptionPane.INFORMATION_MESSAGE); }
    public static void error(Component c, Exception e) { JOptionPane.showMessageDialog(c, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE); }
    public static void warn(Component c, String m) { JOptionPane.showMessageDialog(c, m, "Peringatan", JOptionPane.WARNING_MESSAGE); }
    public static boolean confirm(Component c, String m) { return JOptionPane.showConfirmDialog(c, m, "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION; }

    public static BigDecimal money(String s) { return DB.money(s); }
    public static String rupiah(BigDecimal n) { return DB.rupiah(n); }
}
