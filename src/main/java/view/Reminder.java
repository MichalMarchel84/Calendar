package view;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.time.LocalDateTime;

class Reminder extends JPanel {

    private LocalDateTime time;
    final EntryLabel label;

    public Reminder(LocalDateTime time) {
        label = new EntryLabel(this);
        this.time = time;
        label.setTime(time);
        this.setOpaque(true);
        this.setBackground(new Color(0,0,0,0));
    }

    LocalDateTime getTime() {
        return time;
    }

    void setTime(LocalDateTime time) {
        this.time = time;
        label.setTime(time);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.ORANGE);
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
