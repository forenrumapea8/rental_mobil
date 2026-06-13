package rentalmobil;

import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.util.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

public class DatePickerField extends JPanel {
    private JTextField text = new JTextField(12);
    private JButton btn = UI.lightButton("Kalender");
    private SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
    private java.util.List<Runnable> changeListeners = new ArrayList<Runnable>();

    public DatePickerField() {
        setLayout(new BorderLayout(8,0));
        setOpaque(false);
        UI.input(text);
        text.setEditable(false);
        text.setFocusable(false);
        setDateSilently(new java.util.Date());
        add(text, BorderLayout.CENTER);
        add(btn, BorderLayout.EAST);
        btn.addActionListener(e -> openDialog());
    }

    private void setDateSilently(java.util.Date d) {
        text.setText(fmt.format(d));
    }

    public void setDate(java.util.Date d) {
        text.setText(fmt.format(d));
        fireChange();
    }

    public void setDate(LocalDate d) {
        if (d == null) return;
        setDate(java.util.Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    }

    public LocalDate getLocalDate() {
        try {
            return fmt.parse(text.getText()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        } catch(Exception e) {
            return LocalDate.now();
        }
    }

    public java.sql.Date getSqlDate() {
        try {
            return new java.sql.Date(fmt.parse(text.getText()).getTime());
        } catch(Exception e) {
            return new java.sql.Date(System.currentTimeMillis());
        }
    }

    public String getText() {
        return text.getText();
    }

    public void addChange(Runnable r) {
        if(r != null) changeListeners.add(r);
    }

    private void fireChange() {
        for(Runnable r : new ArrayList<Runnable>(changeListeners)) {
            try { r.run(); } catch(Exception ignored) {}
        }
    }

    private void openDialog() {
        JSpinner sp = new JSpinner(new SpinnerDateModel(getSqlDate(), null, null, Calendar.DAY_OF_MONTH));
        sp.setEditor(new JSpinner.DateEditor(sp, "yyyy-MM-dd"));
        int opt = JOptionPane.showConfirmDialog(this, sp, "Pilih Tanggal", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (opt == JOptionPane.OK_OPTION) {
            setDate((java.util.Date)sp.getValue());
        }
    }
}
