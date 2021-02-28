package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class EntryLabel extends JPanel {

    private final JLabel time = new JLabel();
    private final JLabel title = new JLabel();

    EntryLabel(){
        double[] cols = {0.15, TableLayout.FILL};
        double[] rows = {TableLayout.FILL};
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        this.setLayout(lay);
        this.add(time, "0 0 c c");
        this.add(title, "1 0 l c");
        this.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
    }

    void setTime(LocalDateTime t){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        time.setText(t.format(dtf));
    }

    void setTime(LocalDateTime tStart, LocalDateTime tEnd){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");
        time.setText(tStart.format(dtf) + " - " + tEnd.format(dtf));
    }
}
