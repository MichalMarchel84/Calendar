package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

class DayPanel extends JPanel {

    private LocalDateTime time;

    static final int daysInBuffer = 2;

    private ArrayList<Reminder> reminders = new ArrayList<>();

    private static final double timelineOffset = 0.15;
    private static final double timelineWidth = 0.07;
    private static final double fontSize = 0.04;
    private static final int mainLineThickness = 3;
    private static final int auxLineThickness = 1;

    DayPanel(){
        setDate(LocalDate.now());
        this.addMouseListener(ml);
        this.setLayout(null);

        reminders.add(new Reminder(time.plusHours(3)));
        reminders.add(new Reminder(time.plusHours(12)));
        for (Reminder r : reminders){
            this.add(r);
        }
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                for(Reminder r : reminders){
                    r.setSize(new Dimension((int) (DayPanel.super.getWidth()*timelineWidth) + 20, 20));
                    r.setLocation((int)(DayPanel.super.getWidth()*timelineOffset), positionOf(r.getTime()) - r.getHeight()/2);
                }
            }
        });
    }

    void setDate(LocalDate time){
        this.time = time.atStartOfDay();
        this.time = this.time.minusDays(daysInBuffer);
        this.repaint();
    }

    void moveBuffer(int val){
        time = time.plusDays(val);
        this.repaint();
    }

    LocalDateTime timeOf(int y){
        int min = (int)((double)(2*daysInBuffer + 1)*1440*y/this.getHeight());
        LocalDateTime t = time.plusMinutes(min);
        return t;
    }

    int positionOf(LocalDateTime t){
        long min = time.until(t, ChronoUnit.MINUTES);
        int res = (int)((double)min*this.getHeight()/((2*daysInBuffer + 1)*1440));
        return res;
    }

    MouseListener ml = new MouseAdapter() {

        int y;

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            y = e.getY();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            if(Math.abs(y - e.getY()) < 2) {
                System.out.println("reminder, time: " + timeOf(y).format(dtf));
            }
            else{
                System.out.println("event, from: " + timeOf(y).format(dtf) + " to: " + timeOf(e.getY()).format(dtf));
            }
        }
    };

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //vertical left line
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine((int)(this.getWidth()*timelineOffset), 0,
                        (int)(this.getWidth()*timelineOffset), this.getHeight());
        //vertical right line
        g2d.drawLine((int)(this.getWidth()*(timelineOffset + timelineWidth)), 0,
                        (int)(this.getWidth()*(timelineOffset + timelineWidth)), this.getHeight());

        double hourHeight = (double) this.getHeight()/(24*(2*daysInBuffer + 1));
        DateTimeFormatter dtm = DateTimeFormatter.ofPattern("HH:mm");

        for(int i = 0; i < (24*(2*daysInBuffer + 1)); i++){
            //horizontal main lines
            g2d.setStroke(new BasicStroke(mainLineThickness));
            g2d.drawLine((int)(this.getWidth()*timelineOffset), (int)(i*hourHeight),
                        (int)(this.getWidth()*(timelineOffset + timelineWidth)), (int)(i*hourHeight));
            //auxiliary lines
            g2d.setStroke(new BasicStroke(auxLineThickness));
            for(int j = 1; j < 4; j++){
                int y1 = (int) (i * hourHeight + j * hourHeight / 4);
                g2d.drawLine((int)(this.getWidth()*timelineOffset), y1,
                        (int)(this.getWidth()*(timelineOffset + timelineWidth)), y1);
            }
            //time labels
            String t = time.plusHours(i).format(dtm);
            g2d.setFont(new Font(g2d.getFont().getName(), Font.BOLD, (int) (this.getVisibleRect().getHeight()*fontSize)));
            g2d.drawString(t,
                    (int)(this.getWidth()*timelineOffset) - g2d.getFontMetrics().stringWidth(t) - 5,
                    (int)((i*hourHeight) + 0.75*g2d.getFontMetrics().getAscent()/2));
        }
        //day separators
        g2d.setStroke(new BasicStroke(mainLineThickness));
        for(int i = 0; i < (2*daysInBuffer + 1); i++){
            //left horizontal line
            int y1 = (int) ((i * 24) * hourHeight);
            g2d.drawLine(0, y1,
                    (int)(this.getWidth() * timelineOffset * 0.9 - g2d.getFontMetrics().stringWidth(time.format(dtm))), y1);
            //right horizontal line
            g2d.drawLine((int)(this.getWidth() * (timelineOffset + timelineWidth)), y1, this.getWidth(), y1);
        }
        //day labels
        g2d.rotate(-Math.PI/2);
        dtm = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        for(int i = 0; i < (2*daysInBuffer + 1); i++){
            int x = (int) ((i * 24) * hourHeight);
            String t = time.plusDays(i).format(dtm);
            //upper date
            g2d.drawString(t, -x - g2d.getFontMetrics().stringWidth(t) - 10, g2d.getFontMetrics().getAscent());
            //lower date
            g2d.drawString(t, -x - (int)(24 * hourHeight) + 10, g2d.getFontMetrics().getAscent());
            //day name
            String dow = I18n.getPhrase(time.plusDays(i).getDayOfWeek().toString().toLowerCase());
            g2d.drawString(dow, -x - (int)(12 * hourHeight) - g2d.getFontMetrics().stringWidth(dow)/2, g2d.getFontMetrics().getAscent());
        }
        g2d.rotate(Math.PI/2);
    }
}
