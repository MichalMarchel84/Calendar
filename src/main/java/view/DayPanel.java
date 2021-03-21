package view;

import controller.DayViewController;
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

public class DayPanel extends JPanel implements MouseListener, MouseMotionListener{

    private LocalDateTime time = LocalDateTime.now();
    private LocalDateTime endTime = time.plusDays(2*daysInBuffer + 1);
    final DayViewController controller;

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

    DayPanel(DayViewController controller){
        this.controller = controller;
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
        ArrayList<ReminderModel> actual = controller.getRemindersBetween(time, endTime);
        for (ReminderModel model : actual) {
            addReminder(model);
        }
    }

    private void getEvents(){
        ArrayList<EventModel> actual = controller.getEventsBetween(time, endTime);
        for (EventModel model : actual) {
            addEvent(model);
        }
    }

    private void getRepetitiveReminders(){
        var list = controller.getRepetitiveRemindersBetween(time, endTime);
        for(RepetitiveReminderModel model : list){
            addRepetitiveReminder(model);
        }
    }

    private void getRepetitiveEvents(){
        var list = controller.getRepetitiveEventsBetween(time, endTime);
        for(RepetitiveEventModel model : list){
            addRepetitiveEvent(model);
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
        reminder.setLocation(timelineOffset, positionOf(reminder.getTime()) - reminder.getHeight()/2);
        reminder.label.setSize(new Dimension(this.getWidth() - timelineOffset - timelineWidth - labelOffset, reminder.getHeight()));
        reminder.label.setLocation(timelineOffset + timelineWidth + labelOffset, reminder.getY());
    }

    private void resizeEvent(Entry event){
        event.setSize(new Dimension(timelineWidth, positionOf(((EventPattern)event).getTimeEnd()) - positionOf(event.getTime())));
        event.setLocation(timelineOffset, positionOf(event.getTime()));
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
                entry.setLocation(entry.getX(), positionOf(entry.getTime()) - entry.getHeight()/2);
                entry.label.setLocation(entry.label.getX(), entry.getY());
            }
            else if((comp instanceof Event) || (comp instanceof RepetitiveEvent)){
                Entry entry = (Entry) comp;
                entry.setLocation(entry.getX(), positionOf(entry.getTime()));
                entry.label.setLocation(entry.label.getX(), entry.getY());
            }
        }
    }

    void setPosition(Entry entry, LocalDateTime t){

        entry.setTime(t);
        if(entry instanceof EventPattern){
            entry.setLocation(entry.getX(), positionOf(t));
        }
        else {
            entry.setLocation(entry.getX(), positionOf(t) - entry.getHeight() / 2);
        }
        entry.label.setLocation(entry.label.getX(), entry.getY());
    }

    void setTimeStart(Entry e, LocalDateTime t){
        if(t.until(((EventPattern)e).getTimeEnd(), ChronoUnit.MINUTES) > 5) {
            ((EventPattern)e).setDuration(t.until(((EventPattern)e).getTimeEnd(), ChronoUnit.MINUTES));
            e.setTime(t);
            e.setLocation(e.getX(), positionOf(t));
            e.setSize(new Dimension(timelineWidth, positionOf(((EventPattern)e).getTimeEnd()) - positionOf(e.getTime())));
            e.label.setLocation(e.label.getX(), e.getY());
            e.label.setSize(new Dimension(e.label.getWidth(), e.getHeight()));
        }
    }

    void setTimeEnd(Entry e, LocalDateTime t){
        if(e.getTime().until(t, ChronoUnit.MINUTES) > 5) {
            ((EventPattern)e).setDuration(e.getModel().getTime().until(t, ChronoUnit.MINUTES));
            e.setSize(new Dimension(timelineWidth, positionOf(((EventPattern)e).getTimeEnd()) - positionOf(e.getTime())));
            e.label.setSize(new Dimension(e.label.getWidth(), e.getHeight()));
        }
    }

    private Reminder addReminder(LocalDateTime t){
        Reminder r = new Reminder(controller.createReminderModel(t));
        this.add(r);
        this.add(r.label);
        resizeReminder(r);
        r.label.revalidate();
        r.label.repaint();
        return r;
    }

    public void addReminder(ReminderModel model){
        Reminder r = new Reminder(model);
        this.add(r);
        this.add(r.label);
        resizeReminder(r);
    }

    private Event addEvent(LocalDateTime t1, LocalDateTime t2){
        if(t1.isAfter(t2)){
            LocalDateTime temp = t1;
            t1 = t2;
            t2 = temp;
        }
        Event e = new Event(controller.createEventModel(t1, t1.until(t2, ChronoUnit.MINUTES)));
        this.add(e);
        this.add(e.label);
        resizeEvent(e);
        e.label.revalidate();
        e.label.repaint();
        return e;
    }

    public void addEvent(EventModel model){
        Event e = new Event(model);
        this.add(e);
        this.add(e.label);
        resizeEvent(e);
    }

    public void addRepetitiveReminder(RepetitiveReminderModel model){
        if(repetitiveReminders.containsKey(model.getEntryID())){
            removeRepetitiveReminder(model.getEntryID());
        }
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
        this.revalidate();
        this.repaint();
    }

    public void addRepetitiveEvent(RepetitiveEventModel model){
        if(repetitiveEvents.containsKey(model.getEntryID())){
            removeRepetitiveEvent(model.getEntryID());
        }
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
        this.revalidate();
        this.repaint();
    }

    public void removeSingle(Entry entry){
        this.remove(entry);
        this.remove(entry.label);
    }

    public void removeRepetitiveReminder(Integer id){
        for (RepetitiveReminder r : repetitiveReminders.get(id)){
            this.remove(r);
            this.remove(r.label);
        }
        repetitiveReminders.remove(id);
    }

    public void removeRepetitiveEvent(Integer id){
        for (RepetitiveEvent e : repetitiveEvents.get(id)){
            this.remove(e);
            this.remove(e.label);
        }
        repetitiveEvents.remove(id);
    }

    public void deleteEntry(Entry entry){

        if(entry instanceof RepetitiveReminder){
            removeRepetitiveReminder(entry.getModel().getEntryID());
        }
        else if(entry instanceof RepetitiveEvent){
            removeRepetitiveEvent(entry.getModel().getEntryID());
        }
        else {
            this.remove(entry);
            this.remove(entry.label);
        }
        controller.delete(entry.getModel());
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
        repetitiveReminders.clear();
        repetitiveEvents.clear();
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
            if(sel instanceof RepetitiveEntry){
                RepetitiveEntry re = (RepetitiveEntry) sel;
                modifyAllowed = re.isFirstOccurrence();
            }
            else modifyAllowed = true;
            if(modifyAllowed) {
                sel.getModel().beginTransaction();
            }
        }
        if((selected instanceof Event) || (selected instanceof RepetitiveEvent)){
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
        if((selected instanceof Entry) && modifyAllowed){
            Entry sel = (Entry) selected;
            sel.getModel().commit();
            if(sel instanceof RepetitiveReminder){
                addRepetitiveReminder(((RepetitiveReminder)sel).getModel());
            }
            else if(sel instanceof RepetitiveEvent){
                addRepetitiveEvent(((RepetitiveEvent)sel).getModel());
            }
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
        if((selected instanceof Reminder) || (selected instanceof RepetitiveReminder)){
            Entry r = (Entry) selected;
            if(modifyAllowed) {
                LocalDateTime t = round5min(timeOf(e.getY()));
                if (!r.getTime().equals(t)) {
                    setPosition(r, t);
                }
            }
        }
        else if((selected instanceof Event) || (selected instanceof RepetitiveEvent)){
            Entry event = (Entry) selected;
            if(modifyAllowed) {
                LocalDateTime t = round5min(timeOf(e.getY()));
                if (startPoint < 0) {
                    if (!((EventPattern)event).getTimeEnd().equals(t)) {
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
