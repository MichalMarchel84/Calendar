package view;

import model.RepetitiveReminderModel;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.time.LocalDateTime;

public class RepetitiveReminder extends RepetitiveEntry{

    RepetitiveReminder(RepetitiveReminderModel model, LocalDateTime time) {
        super(model, time);
        label.setTime(getTime());
        label.setTitle(getModel().getTitle());
        this.setOpaque(true);
        this.setBackground(new Color(0,0,0,0));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
        GeneralPath marker = new GeneralPath();
        marker.moveTo(this.getWidth() - this.getHeight(), this.getHeight()/2);
        marker.lineTo(this.getWidth(), 0);
        marker.lineTo(this.getWidth(), this.getHeight());
        marker.closePath();
        g2d.fill(marker);
    }

    @Override
    public RepetitiveReminderModel getModel(){
        return (RepetitiveReminderModel) super.getModel();
    }
}
