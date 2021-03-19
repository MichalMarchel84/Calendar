package view;

import info.clearthought.layout.TableLayout;
import model.RepetitiveModel;

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
    private final JButton accept = new JButton(I18n.getPhrase("save_button"));
    private final JButton cancel = new JButton(I18n.getPhrase("cancel_button"));
    private final JTextField from = new JTextField(5);
    private final JTextField to = new JTextField(5);
    private final JPanel options = new JPanel();
    private final JPanel repetitiveOptions = new JPanel();
    private final JCheckBox repetitive = new JCheckBox();
    private final JComboBox<String> type = new JComboBox<>(RepetitiveModel.intervalTypes);
    private final JTextField period = new JTextField(6);
    private final JLabel periodLabel = new JLabel(I18n.getPhrase("days"));
    private final JButton finish = new JButton();
    private final JLabel beginLabel = new JLabel();
    private final JLabel endLabel = new JLabel();
    DayPanel parent;

    EditWindow(Entry entry){
        parent = (DayPanel) entry.getParent();
        this.entry = entry;
        title.setText(entry.getModel().getTitle());
        title.setFont(new Font(title.getFont().getName(), Font.BOLD, 20));
        description.setText(entry.getModel().getDescription());

        createRepetitiveOptions();

        double[] cols = {10, 0.3, TableLayout.FILL, TableLayout.FILL, 10};
        double[] rows = {10, 0.07, 0.15, 0.07, TableLayout.FILL, 0.15, 10};
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
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
            from.setText(entry.getModel().getTime().format(dtf));
            from.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else if(entry instanceof Event){
            Event e = (Event) entry;
            options.add(from, "0 0 c c");
            from.setText(e.getModel().getTime().format(dtf));
            from.setHorizontalAlignment(SwingConstants.CENTER);
            options.add(new JLabel("-"), "1 0 c c");
            options.add(to, "2 0 c c");
            to.setText(e.getModel().getTimeEnd().format(dtf));
            to.setHorizontalAlignment(SwingConstants.CENTER);
        }
        options.add(new JLabel("Repetitive"), "0 1 1 1 c c");
        repetitive.addActionListener(this);
        options.add(repetitive, "2 1 c c");

        /*if(entry.isRepetitive()){
            repetitive.setSelected(true);
            if(!entry.isFirstRepetitive()) {
                repetitive.setEnabled(false);
                type.setEnabled(false);
                period.setEnabled(false);
                from.setEnabled(false);
                to.setEnabled(false);
                DateTimeFormatter dateDtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                beginLabel.setText(I18n.getPhrase("startAt") + " " + entry.getRepetitive().getContent().getTime().format(dateDtf));
                repetitiveOptions.add(beginLabel, "0 4 1 4 c c");
                if(entry.getRepetitive().getTimeEnd() == null){
                    endLabel.setText(I18n.getPhrase("ongoing"));
                }
                else {
                    endLabel.setText(I18n.getPhrase("endAt") + " " + entry.getRepetitive().getTimeEnd().format(dateDtf));
                }
                repetitiveOptions.add(endLabel, "0 5 1 5 c c");
                if(entry.getRepetitive().getTimeEnd() == null){
                    finish.setText(I18n.getPhrase("finish"));
                }
                else {
                    finish.setText(I18n.getPhrase("resume"));
                }
                finish.addActionListener(this);
                repetitiveOptions.add(finish, "0 6 1 6 f f");
            }
            type.setSelectedIndex(entry.getRepetitive().getType());
            period.setText(Integer.toString(entry.getRepetitive().getPeriod()));
            options.add(repetitiveOptions, "0 2 2 2 f f");
        }*/

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
        double[] rows = new double[7];
        Arrays.fill(rows, TableLayout.PREFERRED);
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(5);
        lay.setVGap(10);
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
            /*if(entry.isRepetitive()){
                entry.getRepetitive().setTitle(title.getText());
                entry.getRepetitive().setDescription(description.getText());
            }
            else {*/
                entry.getModel().setTitle(title.getText());
                entry.getModel().setDescription(description.getText());
            //}
            try {
                //boolean firstRepetitive = entry.isFirstRepetitive();
                if(entry instanceof Reminder){
                    parent.setPosition((Reminder) entry, txtFieldTime(from, entry.getModel().getTime()));
                }
                else if(entry instanceof Event){
                    Event event = (Event) entry;
                    LocalDateTime t1 = txtFieldTime(from, event.getModel().getTime());
                    LocalDateTime t2 = txtFieldTime(to, event.getModel().getTimeEnd());
                    parent.setTimeStart(event, t1);
                    parent.setTimeEnd(event, t2);
                }
                /*if(repetitive.isSelected()){
                    if(!entry.isRepetitive()){
                        Repetitive r = new Repetitive(entry);
                        r.setPeriod(Integer.parseInt(period.getText()));
                        r.setType(type.getSelectedIndex());
                        parent.addRepetitive(r);
                        parent.removeEntry(entry);
                    }
                    else {
                        if(firstRepetitive) {
                            int p = Integer.parseInt(period.getText());
                            entry.getRepetitive().setContent(entry);
                            entry.getRepetitive().setPeriod(p);
                            entry.getRepetitive().setType(type.getSelectedIndex());
                            parent.displayRepetitive(entry.getRepetitive());
                        }
                    }
                }
                else if(entry.isRepetitive()){
                    parent.removeRepetitive(entry.getRepetitive());
                    entry.removeRepetitive();
                    if(entry instanceof Reminder){
                        parent.addReminder((Reminder) entry);
                        parent.setPosition((Reminder) entry, txtFieldTime(from, entry.getTime()));
                    }
                    else if(entry instanceof Event){
                        Event event = (Event) entry;
                        event.setBackground(Event.singleColor);
                        LocalDateTime t1 = txtFieldTime(from, event.getTimeStart());
                        LocalDateTime t2 = txtFieldTime(to, event.getTimeEnd());
                        parent.setTimeStart(event, t1);
                        parent.setTimeEnd(event, t2);
                        parent.addEvent(event);
                    }
                }*/
                this.dispose();
                parent.revalidate();
                parent.repaint();
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
        /*else if(e.getSource().equals(finish)){
            if(entry.getRepetitive().getTimeEnd() == null){
                entry.getRepetitive().setTimeEnd(entry.getTime());
                finish.setText(I18n.getPhrase("resume"));
                DateTimeFormatter dateDtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                endLabel.setText(I18n.getPhrase("endAt") + " " + entry.getRepetitive().getTimeEnd().format(dateDtf));
            }
            else {
                entry.getRepetitive().setTimeEnd(null);
                finish.setText(I18n.getPhrase("finish"));
                endLabel.setText(I18n.getPhrase("ongoing"));
            }
            parent.displayRepetitive(entry.getRepetitive());
            parent.revalidate();
            parent.repaint();
        }*/
    }
}
