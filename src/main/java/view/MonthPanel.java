package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

class MonthPanel extends JPanel {

    private int month;
    private int year;
    private int weeks;
    private ArrayList<JButton> dayList = new ArrayList<>();
    private JPanel label;
    private static final String[] monthNames = {"january", "february", "march", "april", "may", "june", "july", "august", "september", "october", "november", "december"};

    MonthPanel(int m, int y) {
        this.month = m;
        this.year = y;
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m - 1);
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
        while(calendar.get(Calendar.MONTH) == (m - 1)){
            JButton b = new JButton(Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)));
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

        this.add(days, "1 0 f f");

        label = new JPanel(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2d = (Graphics2D) g;
                String txt = I18n.getPhrase(monthNames[month - 1]);
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
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        num += calendar.get(Calendar.WEEK_OF_YEAR);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        if(calendar.get(Calendar.MONTH) != Calendar.JANUARY){
            num -= calendar.get(Calendar.WEEK_OF_YEAR);
        }
        return num;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getWeeks() {
        return weeks;
    }

    public ArrayList<JButton> getDayList() {
        return dayList;
    }
}
