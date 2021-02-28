package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class EntryLabel extends JPanel implements ActionListener {

    private final JLabel time = new JLabel();
    private final JLabel title = new JLabel();
    private final JPanel target;
    private final JButton edit = new JButton("E");
    private final JButton delete = new JButton("D");

    EntryLabel(JPanel target){
        this.target = target;
        double[] cols = {0.15, TableLayout.FILL, 50, 50};
        double[] rows = {TableLayout.FILL};
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(5);
        this.setLayout(lay);
        this.add(time, "0 0 c c");
        this.add(title, "1 0 l c");
        this.add(edit, "2 0 c t");
        this.add(delete, "3 0 c t");
        delete.addActionListener(this);
        this.setBackground(new Color(100, 100, 255, 50));
    }

    void setTime(LocalDateTime t){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        time.setText(t.format(dtf));
    }

    void setTime(LocalDateTime tStart, LocalDateTime tEnd){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        time.setText(tStart.format(dtf) + " - " + tEnd.format(dtf));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(delete)){
            DayPanel parent = (DayPanel)this.getParent();
            parent.removeEntry(target);
        }
    }
}
