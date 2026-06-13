package rentalmobil;

import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.util.*;
import java.sql.Date;

public class DatePickerField extends JPanel {
    private JTextField text = new JTextField(12);
    private JButton btn = UI.lightButton("Kalender");
    private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");

    public DatePickerField() {
        setLayout(new BorderLayout(8,0)); setOpaque(false);
        UI.input(text); text.setEditable(false);
        setDate(new java.util.Date());
        add(text, BorderLayout.CENTER); add(btn, BorderLayout.EAST);
        btn.addActionListener(e -> openDialog());
    }

    public void setDate(java.util.Date d) { text.setText(fmt.format(d)); }
    public java.sql.Date getSqlDate() {
        try { return new java.sql.Date(fmt.parse(text.getText()).getTime()); } catch(Exception e) { return new java.sql.Date(System.currentTimeMillis()); }
    }
    public String getText() { return text.getText(); }
    public void addChange(Runnable r) { text.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){ public void insertUpdate(javax.swing.event.DocumentEvent e){r.run();} public void removeUpdate(javax.swing.event.DocumentEvent e){r.run();} public void changedUpdate(javax.swing.event.DocumentEvent e){r.run();} }); }

    private void openDialog() {
        JSpinner sp = new JSpinner(new SpinnerDateModel(getSqlDate(), null, null, Calendar.DAY_OF_MONTH));
        sp.setEditor(new JSpinner.DateEditor(sp, "yyyy-MM-dd"));
        int opt = JOptionPane.showConfirmDialog(this, sp, "Pilih Tanggal", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opt == JOptionPane.OK_OPTION) setDate((java.util.Date)sp.getValue());
    }
}
