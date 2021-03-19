package view;

import info.clearthought.layout.TableLayout;
import model.EntryModel;
import model.EventModel;
import model.ReminderModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class EntryLabel extends JPanel implements ActionListener {

    private final JLabel time = new JLabel();
    private final JLabel title = new JLabel();
    private final Entry target;
    private final JButton edit = new JButton("E");
    private final JButton delete = new JButton("D");

    EntryLabel(Entry target){
        this.target = target;
        setTitle(target.getModel().getTitle());
        double[] cols = {0.15, TableLayout.FILL, 50, 50};
        double[] rows = {TableLayout.FILL};
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(5);
        this.setLayout(lay);
        this.add(time, "0 0 c c");
        this.add(title, "1 0 l c");
        this.add(edit, "2 0 c t");
        this.add(delete, "3 0 c t");
        edit.addActionListener(this);
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

    void setTitle(String txt){
        title.setText(txt);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof EntryModel){
            if(e.getSource() instanceof ReminderModel){
                ReminderModel model = (ReminderModel) e.getSource();
                setTime(model.getTime());
            }
            else if(e.getSource() instanceof EventModel){
                EventModel model = (EventModel) e.getSource();
                setTime(model.getTime(), model.getTime().plusMinutes(model.getDuration()));
            }
            setTitle(((EntryModel) e.getSource()).getTitle());
            repaint();
        }
        else if(e.getSource().equals(delete)){
            DayPanel parent = (DayPanel)this.getParent();
            parent.removeEntry(target);
        }
        else if(e.getSource().equals(edit)){
            new EditWindow(target);
        }
    }
}
