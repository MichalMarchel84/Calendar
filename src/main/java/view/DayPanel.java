package view;

import model.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;

class DayPanel extends JPanel implements MouseListener, MouseMotionListener{

    private LocalDateTime time = LocalDateTime.now();
    private LocalDateTime endTime = time.plusDays(2*daysInBuffer + 1);

    static final int daysInBuffer = 2;

    private JPanel selected = null;
    private int startPoint = 0;
    private int endPoint = -1;
    private boolean modifyAllowed = true;
    HashMap<Integer, ArrayList<RepetitiveReminder>> repetitiveReminders = new HashMap<>();
    HashMap<Integer, ArrayList<RepetitiveEvent>> repetitiveEvents = new HashMap<>();

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
        ArrayList<ReminderModel> actual = App.controller.getRemindersBetween(time, endTime);
        for (ReminderModel model : actual) {
            addReminder(model);
        }
    }

    private void getEvents(){
        ArrayList<EventModel> actual = App.controller.getEventsBetween(time, endTime);
        for (EventModel model : actual) {
            addEvent(model);
        }
    }

    private void getRepetitiveReminders(){
        var list = App.controller.getRepetitiveRemindersBetween(time, endTime);
        for(RepetitiveReminderModel model : list){
            ArrayList<LocalDateTime> occurrences = model.getBetween(time, endTime);
            if(occurrences.size() > 0){
                ArrayList<RepetitiveReminder> reminders = new ArrayList<>();
                for (LocalDateTime time : occurrences){
                    RepetitiveReminder reminder = new RepetitiveReminder(model, time);
                    reminders.add(reminder);
                    this.add(reminder);
                    this.add(reminder.label);
                    resizeReminder(reminder);
                }
                repetitiveReminders.put(reminders.get(0).getModel().getEntryID(), reminders);
            }
        }
    }

    private void getRepetitiveEvents(){
        var list = App.controller.getRepetitiveEventsBetween(time, endTime);
        for(RepetitiveEventModel model : list){
            ArrayList<LocalDateTime> occurrences = model.getBetween(time, endTime);
            if(occurrences.size() > 0){
                ArrayList<RepetitiveEvent> events = new ArrayList<>();
                for (LocalDateTime time : occurrences){
                    RepetitiveEvent event = new RepetitiveEvent(model, time);
                    events.add(event);
                    this.add(event);
                    this.add(event.label);
                    resizeEvent(event);
                }
                repetitiveEvents.put(events.get(0).getModel().getEntryID(), events);
            }
        }
    }

    void setDate(LocalDate time){
        this.time = time.atStartOfDay();
        this.endTime = this.time.plusDays(2*daysInBuffer + 1);
        clearContent();
        getReminders();
        getEvents();
        getRepetitiveReminders();
        getRepetitiveEvents();
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

    private void resizeReminder(Entry reminder){
        reminder.setSize(new Dimension(timelineWidth + 20, 20));
        reminder.setLocation(timelineOffset, positionOf(reminder.getModel().getTime()) - reminder.getHeight()/2);
        reminder.label.setSize(new Dimension(this.getWidth() - timelineOffset - timelineWidth - labelOffset, reminder.getHeight()));
        reminder.label.setLocation(timelineOffset + timelineWidth + labelOffset, reminder.getY());
    }

    private void resizeEvent(Entry event){
        event.setSize(new Dimension(timelineWidth, positionOf(event.getTimeEnd()) - positionOf(event.getModel().getTime())));
        event.setLocation(timelineOffset, positionOf(event.getModel().getTime()));
        event.label.setSize(new Dimension(this.getWidth() - timelineOffset - timelineWidth - labelOffset, event.getHeight()));
        event.label.setLocation(timelineOffset + timelineWidth + labelOffset, event.getY());
    }

    private void resize(){

        for(Component comp : this.getComponents()){
            if((comp instanceof Reminder) || (comp instanceof RepetitiveReminder)){
                resizeReminder((Entry) comp);
            }
            else if((comp instanceof Event) || (comp instanceof RepetitiveEvent)){
                resizeEvent((Entry) comp);
            }
        }
    }

    private void updatePositions(){
        for(Component comp : this.getComponents()){
            if((comp instanceof Reminder) || (comp instanceof RepetitiveReminder)){
                Entry entry = (Entry) comp;
                entry.setLocation(entry.getX(), positionOf(entry.getModel().getTime()) - entry.getHeight()/2);
                entry.label.setLocation(entry.label.getX(), entry.getY());
            }
            else if((comp instanceof Event) || (comp instanceof RepetitiveEvent)){
                Entry entry = (Entry) comp;
                entry.setLocation(entry.getX(), positionOf(entry.getModel().getTime()));
                entry.label.setLocation(entry.label.getX(), entry.getY());
            }
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
        this.add(r);
        this.add(r.label);
        resizeReminder(r);
        r.label.revalidate();
        r.label.repaint();
        return r;
    }

    void addReminder(ReminderModel model){
        Reminder r = new Reminder(model);
        this.add(r);
        this.add(r.label);
        resizeReminder(r);
    }

    private Event addEvent(LocalDateTime t1, LocalDateTime t2){
        Event e = new Event(App.controller.createEventModel(t1, t1.until(t2, ChronoUnit.MINUTES)));
        this.add(e);
        this.add(e.label);
        resizeEvent(e);
        e.label.revalidate();
        e.label.repaint();
        return e;
    }

    void addEvent(EventModel model){
        Event e = new Event(model);
        this.add(e);
        this.add(e.label);
        resizeEvent(e);
    }

    void deleteEntry(Entry entry){

        if(entry instanceof RepetitiveReminder){
            for (RepetitiveReminder r : repetitiveReminders.get(entry.getModel().getEntryID())){
                this.remove(r);
                this.remove(r.label);
                repetitiveReminders.remove(entry.getModel().getEntryID());
            }
        }
        else if(entry instanceof RepetitiveEvent){
            for (RepetitiveEvent e : repetitiveEvents.get(entry.getModel().getEntryID())){
                this.remove(e);
                this.remove(e.label);
                repetitiveEvents.remove(entry.getModel().getEntryID());
            }
        }
        else {
            this.remove(entry);
            this.remove(entry.label);
        }
        App.controller.delete(entry.getModel());
        this.repaint();
    }

    void clearContent(){
        for (Component comp : this.getComponents()){
            if(comp instanceof Entry){
                Entry entry = (Entry) comp;
                this.remove(entry);
                this.remove(entry.label);
            }
        }
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
