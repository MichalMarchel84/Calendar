package view;

import info.clearthought.layout.TableLayout;
import model.RepetitiveEventModel;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;

class RepetitiveEvent extends RepetitiveEntry implements EventPattern{

    RepetitiveEvent(RepetitiveEventModel model, LocalDateTime time) {
        super(model, time);
        label.setTime(getTime(), getTimeEnd());
        label.setTitle(getModel().getTitle());
        this.setBackground(new Color(0, 255, 0, 100));
        this.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
        this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        double[] cols = {TableLayout.FILL};
        double[] rows = {10, TableLayout.FILL, 10};
        this.setLayout(new TableLayout(cols, rows));
        JPanel center = new JPanel();
        center.setBackground(new Color(0, 0, 0, 0));
        center.setCursor(new Cursor(Cursor.HAND_CURSOR));
        this.add(center, "0 1 f f");
    }

    @Override
    public LocalDateTime getTimeEnd(){
        return getTime().plusMinutes(getModel().getDuration());
    }

    @Override
    public long getDuration() {
        return getModel().getDuration();
    }

    @Override
    public void setDuration(long duration) {
        getModel().setDuration(duration);
    }

    @Override
    protected RepetitiveEventModel getModel(){
        return (RepetitiveEventModel) super.getModel();
    }
}
