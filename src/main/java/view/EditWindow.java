package view;

import controller.EditWindowController;
import info.clearthought.layout.TableLayout;
import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class EditWindow extends JFrame implements ActionListener {
    public final Entry entry;
    public final JTextField title = new JTextField(50);
    public final JTextArea description = new JTextArea();
    public final JButton accept = new JButton(I18n.getPhrase("save_button"));
    public final JButton cancel = new JButton(I18n.getPhrase("cancel_button"));
    public final JTextField from = new JTextField(5);
    public final JTextField to = new JTextField(5);
    private final JPanel options = new JPanel();
    private final JPanel repetitiveOptions = new JPanel();
    public final JCheckBox repetitive = new JCheckBox();
    public final JComboBox<String> type;
    public final JTextField period = new JTextField(6);
    private final JLabel periodLabel = new JLabel(I18n.getPhrase("days"));
    public final JButton finish = new JButton();
    private final JLabel beginLabel = new JLabel();
    private final JLabel endLabel = new JLabel();
    private final DayPanel parent;
    private final EditWindowController controller;
    public LocalDateTime repetitiveClosed = null;

    EditWindow(Entry entry){
        parent = (DayPanel) entry.getParent();
        this.entry = entry;
        String types[] = new String[RepetitiveModel.INTERVAL_TYPES.length];
        for(int i = 0; i < types.length; i++){
            types[i] = I18n.getPhrase(RepetitiveModel.INTERVAL_TYPES[i]);
        }
        type = new JComboBox<>(types);
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

        setTimeFields();
        options.add(new JLabel(I18n.getPhrase("repetitive")), "0 1 1 1 c c");
        setRepetitiveOptions();
        repetitive.addActionListener(this);
        options.add(repetitive, "2 1 c c");

        p.add(options, "1 1 1 5 f f");
        p.add(new JLabel("Title"), "2 1 l c");
        p.add(title, "2 2 3 2 f c");
        p.add(new JLabel("Description"), "2 3 l c");
        p.add(description, "2 4 3 4 f f");
        p.add(accept, "2 5 f f");
        p.add(cancel, "3 5 f f");
        controller = new EditWindowController(parent.controller, this);
        accept.addActionListener(controller);
        cancel.addActionListener(controller);
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

    private void createRepetitiveOptions(){
        double[] cols = {0.5, 0.5};
        double[] rows = new double[7];
        Arrays.fill(rows, TableLayout.PREFERRED);
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(5);
        lay.setVGap(10);
        repetitiveOptions.setLayout(lay);
        repetitiveOptions.add(new JLabel(I18n.getPhrase("repeat_interval")), "0 0 1 0 c c");
        type.addActionListener(this);
        repetitiveOptions.add(type, "0 1 1 1 c c");
        period.setText("7");
        period.setHorizontalAlignment(SwingConstants.CENTER);
        finish.addActionListener(this);
    }

    private void setRepetitiveOptions(){
        if(entry instanceof RepetitiveEntry){
            RepetitiveEntry re = (RepetitiveEntry) entry;
            repetitive.setSelected(true);
            displayRepetitiveOptions();
            int interval = re.getModel().getInterval();
            if(interval > 0){
                type.setSelectedIndex(2);
                period.setText(Integer.toString(interval));
                displayPeriod();
            }
            else if(interval == 0){
                type.setSelectedIndex(0);
            }
            else if(interval == -1){
                type.setSelectedIndex(1);
            }
            setBeginLabel(re.getStartAt());
            setEndLabel(re.getFinishedAt());
            repetitiveOptions.add(beginLabel, "0 3 1 3 c c");
            repetitiveOptions.add(endLabel, "0 4 1 4 c c");
            repetitiveOptions.add(finish, "0 5 1 5 c c");
        }
    }

    private void setTimeFields(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        from.setFont(new Font(from.getFont().getName(), Font.BOLD, 20));
        to.setFont(new Font(from.getFont().getName(), Font.BOLD, 20));
        if(entry instanceof EventPattern){
            options.add(from, "0 0 c c");
            from.setText(entry.getTime().format(dtf));
            from.setHorizontalAlignment(SwingConstants.CENTER);
            options.add(new JLabel("-"), "1 0 c c");
            options.add(to, "2 0 c c");
            to.setText(((EventPattern)entry).getTimeEnd().format(dtf));
            to.setHorizontalAlignment(SwingConstants.CENTER);
        }
        else {
            options.add(from, "0 0 2 0 c c");
            from.setText(entry.getTime().format(dtf));
            from.setHorizontalAlignment(SwingConstants.CENTER);
        }
    }

    private void displayRepetitiveOptions(){
        options.add(repetitiveOptions, "0 2 2 2");
        options.revalidate();
        options.repaint();
    }

    private void hideRepetitiveOptions(){
        options.remove(repetitiveOptions);
        options.revalidate();
        options.repaint();
    }

    private void displayPeriod(){
        repetitiveOptions.add(period, "0 2 c c");
        repetitiveOptions.add(periodLabel, "1 2 l c");
        repetitiveOptions.revalidate();
        repetitiveOptions.repaint();
    }

    private void hidePeriod(){
        repetitiveOptions.remove(period);
        repetitiveOptions.remove(periodLabel);
        options.revalidate();
        options.repaint();
    }

    private void setBeginLabel(LocalDateTime date){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        beginLabel.setText(I18n.getPhrase("startAt") + " " + date.format(dtf));
    }

    private void setEndLabel(LocalDateTime date){
        if(date == null){
            endLabel.setText(I18n.getPhrase("ongoing"));
            finish.setText(I18n.getPhrase("finish"));
        }
        else {
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            endLabel.setText(I18n.getPhrase("endAt") + date.format(dtf));
            finish.setText(I18n.getPhrase("resume"));
        }
        repetitiveClosed = date;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if(actionEvent.getSource().equals(repetitive)){
            if(repetitive.isSelected()){
                displayRepetitiveOptions();
            }
            else {
                hideRepetitiveOptions();
            }
        }
        else if(actionEvent.getSource().equals(type)){
            if(type.getSelectedIndex() == 2){
                displayPeriod();
            }
            else {
                hidePeriod();
            }
        }
        else if(actionEvent.getSource().equals(finish)){
            if(repetitiveClosed == null){
                setEndLabel(entry.getTime());
            }
            else {
                setEndLabel(null);
            }
        }
    }
}
