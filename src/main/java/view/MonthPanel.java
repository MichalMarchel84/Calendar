package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

class MonthPanel extends JPanel implements ActionListener {

    private LocalDate date;
    private final int weeks;
    private final ArrayList<JButton> dayList = new ArrayList<>();
    private final JPanel label;
    private static final String[] monthNames = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};

    MonthPanel(LocalDate date) {
        this.date = date;
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.MONTH, date.getMonthValue() - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        weeks = getWeeksInMonth(calendar);
        double[] c = {0.1, TableLayout.FILL};
        double[] r = {TableLayout.FILL};
        this.setLayout(new TableLayout(c, r));

        JPanel days = new JPanel();
        double[] cols = new double[7];
        double[] rows = new double[weeks];
        Arrays.fill(cols, 1d/7);
        Arrays.fill(rows, 1d/weeks);
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        lay.setVGap(10);
        days.setLayout(lay);

        int week = 0;
        while(calendar.get(Calendar.MONTH) == (date.getMonthValue() - 1)){
            JButton b = new JButton(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
            b.addActionListener(this);
            dayList.add(b);
            if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY){
                b.setForeground(Color.RED);
            }
            days.add(b, getDayOfWeek(calendar) + " " + week + " f f");
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            if(calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY){
                week++;
            }
        }
        Calendar today = Calendar.getInstance();
        int todayYear = today.get(Calendar.YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        if((todayMonth + 1 == date.getMonthValue()) && (todayYear == date.getYear())){
            dayList.get(todayDay - 1).setBorder(BorderFactory.createLineBorder(Color.RED, 5));
        }

        this.add(days, "1 0 f f");

        label = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2d = (Graphics2D) g;
                String txt = I18n.getPhrase(monthNames[date.getMonthValue() - 1]) + " " + date.getYear();
                g2d.setFont(new Font(g2d.getFont().getName(), Font.BOLD, label.getWidth()/3));
                g2d.rotate(-Math.PI/2);
                int x = label.getHeight()/2 + g2d.getFontMetrics().stringWidth(txt)/2;
                int y = label.getWidth()/2 + g2d.getFontMetrics().getAscent()/2;
                g2d.drawString(txt, -x, y);

                super.paintComponent(g);
            }
        };
        this.add(label, "0 0 f f");
    }

    private int getDayOfWeek(Calendar calendar){
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        day -= 2;
        if(day < 0){
            day = 6;
        }
        return day;
    }

    int getWeeksInMonth(Calendar calendar){
        int num = 1;
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayOfWeek = getDayOfWeek(calendar);
        for(int i = 1; i < lastDay; i++){
            dayOfWeek++;
            if(dayOfWeek > 6){
                dayOfWeek = 0;
                num++;
            }
        }
        return num;
    }
    public LocalDate getDate(){
        return date;
    }

    public int getWeeks() {
        return weeks;
    }

    public ArrayList<JButton> getDayList() {
        return dayList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        AppWindow app = (AppWindow) this.getTopLevelAncestor();
        JButton b = (JButton) e.getSource();
        int d = Integer.parseInt(b.getText());
        app.displayPanel(AppWindow.panels.dayView);
        app.dayView.setDate(date.withDayOfMonth(Integer.parseInt(b.getText())));
    }
}
