package view;

import model.App;
import model.EventModel;
import model.ReminderModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

class DayPanel extends JPanel implements MouseListener, MouseMotionListener{

    private LocalDateTime time = LocalDateTime.now();

    static final int daysInBuffer = 2;

    private final ArrayList<Reminder> reminders = new ArrayList<>();
    private final ArrayList<Event> events = new ArrayList<>();
    private JPanel selected = null;
    private int startPoint = 0;
    private int endPoint = -1;
    private boolean modifyAllowed = true;

    private static final int timelineOffset = 120;
    private static final int fontSize = 20;
    private static final int timelineWidth = 60;
    private static final int labelOffset = 20;
    private static final int mainLineThickness = 3;
    private static final int auxLineThickness = 1;

    DayPanel(){
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

    private void getReminders(){
        ArrayList<ReminderModel> actual = App.controller.getRemindersBetween(time, time.plusDays(2 * daysInBuffer + 1));
        for (ReminderModel model : actual) {
            addReminder(model);
        }
    }

    private void getEvents(){
        ArrayList<EventModel> actual = App.controller.getEventsBetween(time, time.plusDays(2 * daysInBuffer + 1));
        for (EventModel model : actual) {
            addEvent(model);
        }
    }

    void setDate(LocalDate time){
        this.time = time.atStartOfDay();
        clearContent();
        getReminders();
        getEvents();
        this.updatePositions();
        this.revalidate();
        this.repaint();
    }

    void setDateCentered(LocalDate time){
        setDate(time.minusDays(daysInBuffer));
    }

    void moveBuffer(int val){
        setDate(LocalDate.from(time.plusDays(val)));
    }

    LocalDateTime timeOf(int y){
        int min = (int)((double)(2*daysInBuffer + 1)*1440*y/this.getHeight());
        return time.plusMinutes(min);
    }

    int positionOf(LocalDateTime t){
        long min = time.until(t, ChronoUnit.MINUTES);
        return (int)((double)min* this.getHeight()/((2*daysInBuffer + 1)*1440));
    }

    private void resizeReminder(Reminder r){
        r.setSize(new Dimension(timelineWidth + 20, 20));
        r.setLocation(timelineOffset, positionOf(r.getModel().getTime()) - r.getHeight()/2);
        r.label.setSize(new Dimension(this.getWidth() - timelineOffset - timelineWidth - labelOffset, r.getHeight()));
        r.label.setLocation(timelineOffset + timelineWidth + labelOffset, r.getY());
    }

    private void resizeEvent(Event e){
        e.setSize(new Dimension(timelineWidth, positionOf(e.getModel().getTimeEnd()) - positionOf(e.getModel().getTime())));
        e.setLocation(timelineOffset, positionOf(e.getModel().getTime()));
        e.label.setSize(new Dimension(this.getWidth() - timelineOffset - timelineWidth - labelOffset, e.getHeight()));
        e.label.setLocation(timelineOffset + timelineWidth + labelOffset, e.getY());
    }

    private void resize(){
        for(Reminder r : reminders){
            resizeReminder(r);
        }
        for(Event e : events){
            resizeEvent(e);
        }
    }

    private void updatePositions(){
        for(Reminder r : reminders){
            r.setLocation(r.getX(), positionOf(r.getModel().getTime()) - r.getHeight()/2);
            r.label.setLocation(r.label.getX(), r.getY());
        }
        for(Event e : events){
            e.setLocation(e.getX(), positionOf(e.getModel().getTime()));
            e.label.setLocation(e.label.getX(), e.getY());
        }
    }

    void setPosition(Reminder r, LocalDateTime t){
            r.getModel().setTime(t);
            r.setLocation(r.getX(), positionOf(t) - r.getHeight() / 2);
            r.label.setLocation(r.label.getX(), r.getY());
    }

    private void setPosition(Event e, LocalDateTime t){
        e.getModel().setTime(t);
        e.setLocation(e.getX(), positionOf(t));
        e.label.setLocation(e.label.getX(), e.getY());
    }

    void setTimeStart(Event e, LocalDateTime t){
        if(t.until(e.getModel().getTimeEnd(), ChronoUnit.MINUTES) > 5) {
            e.getModel().setDuration(t.until(e.getModel().getTimeEnd(), ChronoUnit.MINUTES));
            e.getModel().setTime(t);
            e.setLocation(e.getX(), positionOf(t));
            e.setSize(new Dimension(timelineWidth, positionOf(e.getModel().getTimeEnd()) - positionOf(e.getModel().getTime())));
            e.label.setLocation(e.label.getX(), e.getY());
            e.label.setSize(new Dimension(e.label.getWidth(), e.getHeight()));
        }
    }

    void setTimeEnd(Event e, LocalDateTime t){
        if(e.getModel().getTime().until(t, ChronoUnit.MINUTES) > 5) {
            e.getModel().setDuration(e.getModel().getTime().until(t, ChronoUnit.MINUTES));
            e.setSize(new Dimension(timelineWidth, positionOf(e.getModel().getTimeEnd()) - positionOf(e.getModel().getTime())));
            e.label.setSize(new Dimension(e.label.getWidth(), e.getHeight()));
        }
    }

    private Reminder addReminder(LocalDateTime t){
        Reminder r = new Reminder(App.controller.createReminderModel(t));
        reminders.add(r);
        this.add(r);
        this.add(r.label);
        resizeReminder(r);
        r.label.revalidate();
        r.label.repaint();
        return r;
    }

    void addReminder(ReminderModel model){
        Reminder r = new Reminder(model);
        reminders.add(r);
        this.add(r);
        this.add(r.label);
        resizeReminder(r);
    }

    private Event addEvent(LocalDateTime t1, LocalDateTime t2){
        Event e = new Event(App.controller.createEventModel(t1, t1.until(t2, ChronoUnit.MINUTES)));
        events.add(e);
        this.add(e);
        this.add(e.label);
        resizeEvent(e);
        e.label.revalidate();
        e.label.repaint();
        return e;
    }

    void addEvent(EventModel model){
        Event e = new Event(model);
        events.add(e);
        this.add(e);
        this.add(e.label);
        resizeEvent(e);
    }

    void removeEntry(Entry entry){

        if(entry instanceof Reminder){
            Reminder r = (Reminder) entry;
            reminders.remove(r);
            this.remove(r);
            this.remove(r.label);
        }
        else if(entry instanceof Event){
            Event e = (Event) entry;
            events.remove(e);
            this.remove(e);
            this.remove(e.label);
        }
        App.controller.delete(entry.getModel());
        this.repaint();
    }

    void clearContent(){
        for(Reminder r : reminders){
            this.remove(r);
            this.remove(r.label);
        }
        for(Event e : events){
            this.remove(e);
            this.remove(e.label);
        }
        reminders.clear();
        events.clear();
    }

    LocalDateTime round5min(LocalDateTime t){
        int modulo = t.getMinute() % 5;
        if(modulo < 3){
            t = t.minusMinutes(modulo);
        }
        else{
            t = t.plusMinutes(5 - modulo);
        }
        return t;
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        int xTimelineStart = timelineOffset;
        int xTimelineEnd = timelineOffset + timelineWidth;
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
            g2d.setFont(new Font(g2d.getFont().getName(), Font.BOLD, fontSize));
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
                    timelineOffset - 10 - g2d.getFontMetrics().stringWidth(time.format(dtm)), y1);
            //right horizontal line
            g2d.drawLine(xTimelineEnd, y1, this.getWidth(), y1);
        }
        //day labels
        g2d.rotate(-Math.PI/2);
        g2d.setFont(new Font(g2d.getFont().getName(), Font.BOLD, (int)(fontSize*1.5)));
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
            Reminder r = addReminder(round5min(timeOf(e.getY())));
            new EditWindow(r);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        selected = (JPanel) this.getComponentAt(e.getPoint());
        if(selected instanceof Entry){
            Entry sel = (Entry) selected;
            sel.getModel().beginTransaction();
        }
        if(selected instanceof Event){
            startPoint = e.getY() - selected.getY();
            if((selected.getHeight() - startPoint) < 10){
                startPoint = startPoint - selected.getHeight();
            }
        }
        else {
            startPoint = e.getY();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(endPoint > -1){
            Event event = addEvent(round5min(timeOf(startPoint)), round5min(timeOf(endPoint)));
            new EditWindow(event);
            endPoint = -1;
            this.repaint();
        }
        if(selected instanceof Entry){
            Entry sel = (Entry) selected;
            sel.getModel().commit();
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
            if(modifyAllowed) {
                LocalDateTime t = round5min(timeOf(e.getY()));
                if (!r.getModel().getTime().equals(t)) {
                    setPosition(r, t);
                }
            }
        }
        else if(selected instanceof Event){
            Event event = (Event) selected;
            if(modifyAllowed) {
                LocalDateTime t = round5min(timeOf(e.getY()));
                if (startPoint < 0) {
                    if (!event.getModel().getTimeEnd().equals(t)) {
                        setTimeEnd(event, t);
                    }
                } else if (startPoint < 10) {
                    if (!event.getModel().getTime().equals(t)) {
                        setTimeStart(event, t);
                    }
                } else {
                    t = round5min(timeOf(e.getY() - startPoint));
                    if (!event.getModel().getTime().equals(t)) {
                        setPosition(event, t);
                    }
                }
            }
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
