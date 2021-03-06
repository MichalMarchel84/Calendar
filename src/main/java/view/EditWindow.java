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
    JPanel options = new JPanel();
    JPanel repetitiveOptions = new JPanel();
    JCheckBox repetitive = new JCheckBox();
    JComboBox<String> type = new JComboBox<>(Repetitive.types);
    JTextField period = new JTextField(6);
    JLabel periodLabel = new JLabel(I18n.getPhrase("days"));

    EditWindow(Entry entry){
        this.entry = entry;
        title.setText(entry.getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 20));
        description.setText(entry.getDescription());

        createRepetitiveOptions();

        double[] cols = {10, 0.3, TableLayout.FILL, TableLayout.FILL, 10};
        double[] rows = {10, 0.07, 0.15, 0.07, TableLayout.FILL, 0.15, 10};
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(5);
        lay.setVGap(10);
        JPanel p = new JPanel();
        p.setLayout(lay);

        double[] c = {0.45, 0.1, 0.45};
        double[] r = new double[3];
        Arrays.fill(r, TableLayout.PREFERRED);
        TableLayout l = new TableLayout(c, r);
        l.setVGap(5);
        l.setHGap(5);
        options.setLayout(l);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        from.setFont(new Font(from.getFont().getName(), Font.BOLD, 20));
        to.setFont(new Font(from.getFont().getName(), Font.BOLD, 20));
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
        repetitive.addActionListener(this);
        options.add(repetitive, "2 1 c c");

        if(entry.isRepetitive()){
            repetitive.setSelected(true);
            type.setSelectedIndex(entry.getRepetitive().getType());
            period.setText(Integer.toString(entry.getRepetitive().getPeriod()));
            options.add(repetitiveOptions, "0 2 2 2 f f");
        }

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
        this.setSize(new Dimension(600, 400));
        this.setResizable(false);
        this.setVisible(true);
        this.setLocationRelativeTo(entry.label);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setAlwaysOnTop(true);
    }

    void createRepetitiveOptions(){
        double[] cols = {0.5, 0.5};
        double[] rows = new double[4];
        Arrays.fill(rows, TableLayout.PREFERRED);
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(5);
        lay.setVGap(5);
        repetitiveOptions.setLayout(lay);
        repetitiveOptions.add(new JLabel("Repeat every"), "0 0 1 0 c c");
        type.addActionListener(this);
        repetitiveOptions.add(type, "0 1 1 1 c c");
        period.setText("7");
        period.setHorizontalAlignment(SwingConstants.CENTER);
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
                if(repetitive.isSelected()){
                    if(!entry.isRepetitive()){
                        entry.setRepetitive();
                    }
                    entry.getRepetitive().setPeriod(Integer.parseInt(period.getText()));
                    entry.getRepetitive().setType(type.getSelectedIndex());
                }
                this.dispose();
            }
            catch (Exception ex){
                if(ex instanceof DateTimeParseException) {}
                else if(ex instanceof NumberFormatException){
                    period.setForeground(Color.RED);
                    period.requestFocus();
                }
                else {
                    ex.printStackTrace();
                }
            }
        }
        else if(e.getSource().equals(cancel)){
            this.dispose();
        }
        else if(e.getSource().equals(repetitive)){
            if(repetitive.isSelected()){
                options.add(repetitiveOptions, "0 2 2 2 f f");
            }
            else {
                options.remove(repetitiveOptions);
            }
            options.revalidate();
            options.repaint();
        }
        else if(e.getSource().equals(type)){
            if(type.getSelectedIndex() == 2){
                repetitiveOptions.add(period, "0 3 c c");
                repetitiveOptions.add(periodLabel, "1 3 l c");
                type.requestFocus();
            }
            else{
                repetitiveOptions.remove(period);
                repetitiveOptions.remove(periodLabel);
            }
            repetitiveOptions.revalidate();
            repetitiveOptions.repaint();
        }
    }
}
