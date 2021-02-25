package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

class DayPanel extends JPanel implements MouseListener, MouseMotionListener{

    private LocalDateTime time;

    static final int daysInBuffer = 2;

    private ArrayList<Reminder> reminders = new ArrayList<>();
    private JPanel selected = null;

    private static final double timelineOffset = 0.15;
    private static final double timelineWidth = 0.07;
    private static final double fontSize = 0.04;
    private static final int mainLineThickness = 3;
    private static final int auxLineThickness = 1;

    DayPanel(){
        setDate(LocalDate.now());
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.setLayout(null);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resize();
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
        updatePositions();
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

    private void resize(){
        for(Reminder r : reminders){
            r.setSize(new Dimension((int) (DayPanel.super.getWidth()*timelineWidth) + 20, 20));
            r.setLocation((int)(DayPanel.super.getWidth()*timelineOffset), positionOf(r.getTime()) - r.getHeight()/2);
        }
    }

    private void updatePositions(){
        for(Reminder r : reminders){
            r.setLocation(r.getX(), positionOf(r.getTime()) - r.getHeight()/2);
        }
    }

    private void setPosition(Reminder r, int y){
        r.setTime(timeOf(y));
        r.setLocation(r.getX(), y - r.getHeight()/2);
    }

    MouseListener ml = new MouseAdapter() {

        int y;
        Component c;

        @Override
        public void mousePressed(MouseEvent e) {
            super.mousePressed(e);
            y = e.getY();
            c = DayPanel.super.getComponentAt(e.getPoint());
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            super.mouseReleased(e);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
            if(c instanceof Reminder){
                Reminder r = (Reminder) c;
                r.setTime(timeOf(e.getY()));
                r.setLocation(r.getX(), positionOf(r.getTime()) - r.getHeight() / 2);
            }
            else {
                if (Math.abs(y - e.getY()) < 2) {
                    Reminder r = new Reminder(timeOf(y));
                    reminders.add(r);
                    DayPanel.super.add(r);
                    r.setSize(new Dimension((int) (DayPanel.super.getWidth() * timelineWidth) + 20, 20));
                    r.setLocation((int) (DayPanel.super.getWidth() * timelineOffset), positionOf(r.getTime()) - r.getHeight() / 2);
                } else {
                    System.out.println("event, from: " + timeOf(y).format(dtf) + " to: " + timeOf(e.getY()).format(dtf));
                }
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

    @Override
    public void mouseClicked(MouseEvent e) {
        Component c = this.getComponentAt(e.getPoint());
        if(c instanceof DayPanel) {
            Reminder r = new Reminder(timeOf(e.getY()));
            reminders.add(r);
            this.add(r);
            r.setSize(new Dimension((int) (DayPanel.super.getWidth() * timelineWidth) + 20, 20));
            r.setLocation((int) (DayPanel.super.getWidth() * timelineOffset), positionOf(r.getTime()) - r.getHeight() / 2);
        }
        else{
            System.out.println("On component");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selected = (JPanel) this.getComponentAt(e.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(selected instanceof Reminder){
            Reminder r = (Reminder) selected;
            setPosition(r, e.getY());
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
