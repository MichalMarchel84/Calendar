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

    private final ArrayList<Reminder> reminders = new ArrayList<>();
    private final ArrayList<Event> events = new ArrayList<>();
    private final ArrayList<Repetitive> repetitives = new ArrayList<>();
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
        this.updatePositions();
        for(Repetitive r : repetitives){
            displayRepetitive(r);
        }
        this.revalidate();
        this.repaint();
    }

    void moveBuffer(int val){
        time = time.plusDays(val);
        updatePositions();
        for(Repetitive r : repetitives){
            displayRepetitive(r);
        }
        this.revalidate();
        this.repaint();
    }

    LocalDateTime timeOf(int y){
        int min = (int)((double)(2*daysInBuffer + 1)*1440*y/this.getHeight());
        return time.plusMinutes(min);
    }

    int positionOf(LocalDateTime t){
        long min = time.until(t, ChronoUnit.MINUTES);
        return (int)((double)min* this.getHeight()/((2*daysInBuffer + 1)*1440));
    }

    private void resize(){
        for(Reminder r : reminders){
            r.setSize(new Dimension(timelineWidth + 20, 20));
            r.setLocation(timelineOffset, positionOf(r.getTime()) - r.getHeight()/2);
            r.label.setSize(new Dimension(this.getWidth() - timelineOffset - timelineWidth - labelOffset, r.getHeight()));
            r.label.setLocation(timelineOffset + timelineWidth + labelOffset, r.getY());
        }
        for(Event e : events){
            e.setSize(new Dimension(timelineWidth, positionOf(e.getTimeEnd()) - positionOf(e.getTimeStart())));
            e.setLocation(timelineOffset, positionOf(e.getTimeStart()));
            e.label.setSize(new Dimension(this.getWidth() - timelineOffset - timelineWidth - labelOffset, e.getHeight()));
            e.label.setLocation(timelineOffset + timelineWidth + labelOffset, e.getY());
        }
    }

    private void updatePositions(){
        ArrayList<Entry> forDisposal = new ArrayList<>();
        for(Reminder r : reminders){
            r.setLocation(r.getX(), positionOf(r.getTime()) - r.getHeight()/2);
            r.label.setLocation(r.label.getX(), r.getY());
            if((r.getY() < 0)|| (r.getY() > this.getHeight())){
                forDisposal.add(r);
            }
        }
        for(Event e : events){
            e.setLocation(e.getX(), positionOf(e.getTimeStart()));
            e.label.setLocation(e.label.getX(), e.getY());
            if((e.getY() + e.getHeight() < 0)|| (e.getY() > this.getHeight())){
                forDisposal.add(e);
            }
        }
        for(Entry e : forDisposal){
            this.removeEntry(e);
        }
        ArrayList<Repetitive> repForDisposal = new ArrayList<>();
        for(Repetitive r : repetitives){
            if(r.isBetween(time, time.plusDays(2*daysInBuffer + 1))) {
                for (Entry e : r.getEntries()) {
                    if (e instanceof Reminder) {
                        Reminder rem = (Reminder) e;
                        rem.setLocation(rem.getX(), positionOf(rem.getTime()) - rem.getHeight() / 2);
                        rem.label.setLocation(rem.label.getX(), rem.getY());
                    } else if (e instanceof Event) {
                        Event ev = (Event) e;
                        ev.setLocation(ev.getX(), positionOf(ev.getTimeStart()));
                        ev.label.setLocation(ev.label.getX(), ev.getY());
                    }
                }
            }
            else {
                repForDisposal.add(r);
            }
        }
        for (Repetitive r : repForDisposal){
            this.removeRepetitive(r);
        }
    }

    void setPosition(Reminder r, LocalDateTime t){
            r.setTime(t);
            r.setLocation(r.getX(), positionOf(t) - r.getHeight() / 2);
            r.label.setLocation(r.label.getX(), r.getY());
    }

    private void setPosition(Event e, LocalDateTime t){
        long diff = t.until(e.getTimeStart(), ChronoUnit.MINUTES);
        e.setTimeStart(e.getTimeStart().minusMinutes(diff));
        e.setTimeEnd(e.getTimeEnd().minusMinutes(diff));
        e.setLocation(e.getX(), positionOf(t));
        e.label.setLocation(e.label.getX(), e.getY());
    }

    void setTimeStart(Event e, LocalDateTime t){
        if(t.until(e.getTimeEnd(), ChronoUnit.MINUTES) > 5) {
            e.setTimeStart(t);
            e.setLocation(e.getX(), positionOf(t));
            e.setSize(new Dimension(timelineWidth, positionOf(e.getTimeEnd()) - positionOf(e.getTimeStart())));
            e.label.setLocation(e.label.getX(), e.getY());
            e.label.setSize(new Dimension(e.label.getWidth(), e.getHeight()));
        }
    }

    void setTimeEnd(Event e, LocalDateTime t){
        if(e.getTimeStart().until(t, ChronoUnit.MINUTES) > 5) {
            e.setTimeEnd(t);
            e.setSize(new Dimension(timelineWidth, positionOf(e.getTimeEnd()) - positionOf(e.getTimeStart())));
            e.label.setSize(new Dimension(e.label.getWidth(), e.getHeight()));
        }
    }

    private Reminder addReminder(LocalDateTime t){
        Reminder r = new Reminder(t);
        reminders.add(r);
        this.add(r);
        this.add(r.label);
        r.setSize(new Dimension(timelineWidth + 20, 20));
        r.label.setSize(new Dimension(this.getWidth() - timelineOffset - timelineWidth - labelOffset, r.getHeight()));
        r.setLocation(timelineOffset, positionOf(r.getTime()) - r.getHeight() / 2);
        r.label.setLocation(timelineOffset + timelineWidth + labelOffset, r.getY());
        r.label.revalidate();
        r.label.repaint();
        return r;
    }

    void addReminder(Reminder r){
        if(!r.isRepetitive()) {
            reminders.add(r);
        }
        this.add(r);
        this.add(r.label);
        r.setSize(new Dimension(timelineWidth + 20, 20));
        r.label.setSize(new Dimension(this.getWidth() - timelineOffset - timelineWidth - labelOffset, r.getHeight()));
        r.setLocation(timelineOffset, positionOf(r.getTime()) - r.getHeight() / 2);
        r.label.setLocation(timelineOffset + timelineWidth + labelOffset, r.getY());
    }

    private Event addEvent(LocalDateTime t1, LocalDateTime t2){
        Event e;
        int y1 = positionOf(t1);
        int y2 = positionOf(t2);
        if(y1 < y2){
            e = new Event(t1, t2);
        }
        else {
            e = new Event(t2, t1);
        }
        events.add(e);
        this.add(e);
        this.add(e.label);
        e.setSize(new Dimension(timelineWidth, positionOf(e.getTimeEnd()) - positionOf(e.getTimeStart())));
        e.label.setSize(new Dimension(this.getWidth() - timelineOffset - timelineWidth - labelOffset, e.getHeight()));
        if(y1 > y2){
            y1 = y2;
        }
        e.setLocation(timelineOffset, y1);
        e.label.setLocation(timelineOffset + timelineWidth + labelOffset, e.getY());
        e.label.revalidate();
        e.label.repaint();
        return e;
    }

    void addEvent(Event e){
        if(!e.isRepetitive()) {
            events.add(e);
        }
        this.add(e);
        this.add(e.label);
        e.setSize(new Dimension(timelineWidth, positionOf(e.getTimeEnd()) - positionOf(e.getTimeStart())));
        e.label.setSize(new Dimension(this.getWidth() - timelineOffset - timelineWidth - labelOffset, e.getHeight()));
        e.setLocation(timelineOffset, positionOf(e.getTimeStart()));
        e.label.setLocation(timelineOffset + timelineWidth + labelOffset, e.getY());
    }

    void addRepetitive(Repetitive r){
        repetitives.add(r);
        displayRepetitive(r);
        this.revalidate();
        this.repaint();
    }

    void displayRepetitive(Repetitive r){
        for(Entry e : r.getEntries()){
            this.remove(e);
            this.remove(e.label);
        }
        r.setEntriesFor(time, time.plusDays(2*daysInBuffer + 1));
        ArrayList<Entry> elemnts = r.getEntries();
        if(elemnts.size() > 0){
            if(elemnts.get(0) instanceof Reminder){
                for (Entry e : elemnts){
                    Reminder reminder = (Reminder) e;
                    this.addReminder(reminder);
                }
            }
            else if(elemnts.get(0) instanceof Event){
                for (Entry e : elemnts){
                    Event event = (Event) e;
                    this.addEvent(event);
                }
            }
        }
    }

    void removeRepetitive(Repetitive r){
        for (Entry e : r.getEntries()){
            this.remove(e);
            this.remove(e.label);
        }
        repetitives.remove(r);
    }

    void removeEntry(Entry entry){
        if(entry.isRepetitive()){
            removeRepetitive(entry.getRepetitive());
        }
        else if(entry instanceof Reminder){
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
        for(Repetitive r : repetitives){
            for (Entry e : r.getEntries()){
                this.remove(e);
                this.remove(e.label);
            }
        }
        reminders.clear();
        events.clear();
        repetitives.clear();
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
            if(sel.isFirstRepetitive() || !sel.isRepetitive()){
                modifyAllowed = true;
            }
            else {
                modifyAllowed = false;
            }
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
            if(sel.isRepetitive() && modifyAllowed){
                sel.getRepetitive().setContent(sel);
                this.displayRepetitive(sel.getRepetitive());
                this.revalidate();
                this.repaint();
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
        if(selected instanceof Reminder){
            Reminder r = (Reminder) selected;
            if(modifyAllowed) {
                LocalDateTime t = round5min(timeOf(e.getY()));
                if (!r.getTime().equals(t)) {
                    setPosition(r, t);
                }
            }
        }
        else if(selected instanceof DayPanel){
            endPoint = e.getY();
            this.repaint();
        }
        else if(selected instanceof Event){
            Event event = (Event) selected;
            if(modifyAllowed) {
                LocalDateTime t = round5min(timeOf(e.getY()));
                if (startPoint < 0) {
                    if (!event.getTimeEnd().equals(t)) {
                        setTimeEnd(event, t);
                    }
                } else if (startPoint < 10) {
                    if (!event.getTimeStart().equals(t)) {
                        setTimeStart(event, t);
                    }
                } else {
                    t = round5min(timeOf(e.getY() - startPoint));
                    if (!event.getTimeStart().equals(t)) {
                        setPosition(event, t);
                    }
                }
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
