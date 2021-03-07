package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.time.LocalDateTime;

class Reminder extends Entry {

    public Reminder(LocalDateTime time) {
        super(time);
        label.setTime(time);
        this.setOpaque(true);
        this.setBackground(new Color(0,0,0,0));
        this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        if(super.isRepetitive()) {
            g2d.setColor(Color.GREEN);
        }
        else {
            g2d.setColor(Color.BLUE);
        }
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(0, this.getHeight()/2, this.getWidth(), this.getHeight()/2);
        GeneralPath marker = new GeneralPath();
        marker.moveTo(this.getWidth() - this.getHeight(), this.getHeight()/2);
        marker.lineTo(this.getWidth(), 0);
        marker.lineTo(this.getWidth(), this.getHeight());
        marker.closePath();
        g2d.fill(marker);
    }
}
