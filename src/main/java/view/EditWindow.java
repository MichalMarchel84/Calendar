package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;

class EditWindow extends JFrame implements ActionListener {
    private final Entry entry;
    private final JTextField title = new JTextField(50);
    private final JTextArea description = new JTextArea();
    JButton accept = new JButton(I18n.getPhrase("save_button"));
    JButton cancel = new JButton(I18n.getPhrase("cancel_button"));
    JTextField from = new JTextField(5);
    JTextField to = new JTextField(5);

    EditWindow(Entry entry){
        this.entry = entry;
        title.setText(entry.getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 20));
        description.setText(entry.getDescription());
        double[] cols = {10, 0.3, TableLayout.FILL, TableLayout.FILL, 10};
        double[] rows = {10, 0.07, 0.15, 0.07, TableLayout.FILL, 0.15, 10};
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(5);
        lay.setVGap(10);
        JPanel p = new JPanel();
        p.setLayout(lay);
        JPanel options = new JPanel();
        double[] c = {0.45, 0.1, 0.45};
        double[] r = new double[3];
        Arrays.fill(r, TableLayout.PREFERRED);
        TableLayout l = new TableLayout(c, r);
        l.setVGap(5);
        l.setHGap(5);
        options.setLayout(l);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        if(entry instanceof Reminder){
            options.add(from, "0 0 2 0 c c");
            from.setText(entry.getTime().format(dtf));
            from.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if(entry instanceof Event){
            Event e = (Event) entry;
            options.add(from, "0 0 c c");
            from.setText(e.getTimeStart().format(dtf));
            from.setHorizontalAlignment(SwingConstants.CENTER);
            options.add(new JLabel("-"), "1 0 c c");
            options.add(to, "2 0 c c");
            to.setText(e.getTimeEnd().format(dtf));
            to.setHorizontalAlignment(SwingConstants.CENTER);
        }
        options.add(new JLabel("Repetitive"), "0 1 1 1 c c");
        options.add(new JCheckBox(), "2 1 c c");
        p.add(options, "1 1 1 5 f f");
        p.add(new JLabel("Title"), "2 1 l c");
        p.add(title, "2 2 3 2 f c");
        p.add(new JLabel("Description"), "2 3 l c");
        p.add(description, "2 4 3 4 f f");
        p.add(accept, "2 5 f f");
        p.add(cancel, "3 5 f f");
        accept.addActionListener(this);
        cancel.addActionListener(this);
        accept.setFont(new Font(accept.getFont().getName(), Font.BOLD, 15));
        cancel.setFont(new Font(accept.getFont().getName(), Font.BOLD, 15));
        this.add(p);
        this.setSize(new Dimension(500, 300));
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(entry.label);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setAlwaysOnTop(true);
    }

    LocalDateTime txtFieldTime(JTextField tf, LocalDateTime date) throws DateTimeParseException{
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String time = date.format(dtf) + " " + tf.getText();
        tf.setForeground(Color.RED);
        tf.requestFocus();
        dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime t = LocalDateTime.parse(time, dtf);
        tf.setForeground(Color.BLACK);
        return t;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(accept)){
            entry.setTitle(title.getText());
            entry.setDescription(description.getText());
            try {
                DayPanel parent = (DayPanel) entry.getParent();
                if(entry instanceof Reminder){
                    parent.setPosition((Reminder) entry, txtFieldTime(from, entry.getTime()));
                }
                else if(entry instanceof Event){
                    Event event = (Event) entry;
                    LocalDateTime t1 = txtFieldTime(from, event.getTimeStart());
                    LocalDateTime t2 = txtFieldTime(to, event.getTimeEnd());
                    parent.setTimeStart(event, t1);
                    parent.setTimeEnd(event, t2);
                }
                this.dispose();
            }
            catch (DateTimeParseException ex){
            }
        }
        else if(e.getSource().equals(cancel)){
            this.dispose();
        }
    }
}
