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
    private ArrayList<Event> events = new ArrayList<>();
    private JPanel selected = null;
    private int startPoint = 0;
    private int endPoint = -1;

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
        for(Event e : events){
            e.setSize(new Dimension((int) (this.getWidth() * timelineWidth), positionOf(e.getTimeEnd()) - positionOf(e.getTimeStart())));
            e.setLocation((int) (this.getWidth() * timelineOffset), positionOf(e.getTimeStart()));
        }
    }

    private void updatePositions(){
        for(Reminder r : reminders){
            r.setLocation(r.getX(), positionOf(r.getTime()) - r.getHeight()/2);
        }
        for(Event e : events){
            e.setLocation(e.getX(), positionOf(e.getTimeStart()));
        }
    }

    private void setPosition(Reminder r, int y){
        r.setTime(timeOf(y));
        r.setLocation(r.getX(), y - r.getHeight()/2);
    }

    private void addReminder(int y){
        Reminder r = new Reminder(timeOf(y));
        reminders.add(r);
        this.add(r);
        r.setSize(new Dimension((int) (this.getWidth() * timelineWidth) + 20, 20));
        r.setLocation((int) (this.getWidth() * timelineOffset), positionOf(r.getTime()) - r.getHeight() / 2);
    }

    private void addEvent(int y1, int y2){
        Event e;
        if(y1 < y2){
            e = new Event(timeOf(y1), timeOf(y2));
        }
        else {
            e = new Event(timeOf(y2), timeOf(y1));
        }
        events.add(e);
        this.add(e);
        e.setSize(new Dimension((int) (this.getWidth() * timelineWidth), positionOf(e.getTimeEnd()) - positionOf(e.getTimeStart())));
        if(y1 > y2){
            y1 = y2;
        }
        e.setLocation((int) (this.getWidth() * timelineOffset), y1);
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int xTimelineStart = (int)(this.getWidth()*timelineOffset);
        int xTimelineEnd = (int)(this.getWidth()*(timelineOffset + timelineWidth));
        Graphics2D g2d = (Graphics2D) g;
        //vertical left line
        g2d.setStroke(new BasicStroke(4));
        g2d.drawLine(xTimelineStart, 0, xTimelineStart, this.getHeight());
        //vertical right line
        g2d.drawLine(xTimelineEnd, 0, xTimelineEnd, this.getHeight());

        double hourHeight = (double) this.getHeight()/(24*(2*daysInBuffer + 1));
        DateTimeFormatter dtm = DateTimeFormatter.ofPattern("HH:mm");

        for(int i = 0; i < (24*(2*daysInBuffer + 1)); i++){
            //horizontal main lines
            g2d.setStroke(new BasicStroke(mainLineThickness));
            g2d.drawLine(xTimelineStart, (int)(i*hourHeight), xTimelineEnd, (int)(i*hourHeight));
            //auxiliary lines
            g2d.setStroke(new BasicStroke(auxLineThickness));
            for(int j = 1; j < 4; j++){
                int y1 = (int) (i * hourHeight + j * hourHeight / 4);
                g2d.drawLine(xTimelineStart, y1, xTimelineEnd, y1);
            }
            //time labels
            String t = time.plusHours(i).format(dtm);
            g2d.setFont(new Font(g2d.getFont().getName(), Font.BOLD, (int) (this.getVisibleRect().getHeight()*fontSize)));
            g2d.drawString(t,
                    xTimelineStart - g2d.getFontMetrics().stringWidth(t) - 5,
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
            g2d.drawLine(xTimelineEnd, y1, this.getWidth(), y1);
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
        //draw selected area
        if(endPoint > -1){
            g2d.setColor(new Color(0, 0, 255, 100));
            if(endPoint > startPoint) {
                g2d.fillRect(xTimelineStart, startPoint, xTimelineEnd - xTimelineStart, endPoint - startPoint);
            }
            else {
                g2d.fillRect(xTimelineStart, endPoint, xTimelineEnd - xTimelineStart, startPoint - endPoint);
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        Component c = this.getComponentAt(e.getPoint());
        if(c instanceof DayPanel) {
            addReminder(e.getY());
        }
        else{
            System.out.println("On component");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selected = (JPanel) this.getComponentAt(e.getPoint());
        startPoint = e.getY();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(endPoint > -1){
            addEvent(startPoint, endPoint);
            endPoint = -1;
            this.repaint();
        }
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
        else if(selected instanceof DayPanel){
            endPoint = e.getY();
            this.repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
