package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

class MonthPanel extends JPanel {

    private int month;
    private int year;
    private int weeks;
    private ArrayList<JButton> dayList = new ArrayList<>();

    MonthPanel(int m, int y) {
        this.month = m;
        this.year = y;
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(Calendar.YEAR, y);
        calendar.set(Calendar.MONTH, m - 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        weeks = getWeeksInMonth(calendar);
        double[] cols = new double[7];
        double[] rows = new double[weeks];
        Arrays.fill(cols, 1d/7);
        Arrays.fill(rows, 1d/weeks);
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        lay.setVGap(10);
        this.setLayout(lay);
        while(calendar.get(Calendar.MONTH) == (m - 1)){
            JButton b = new JButton(calendar.get(Calendar.DAY_OF_MONTH) + "." + (calendar.get(Calendar.MONTH) + 1) + "." + calendar.get(Calendar.YEAR));
            dayList.add(b);
            this.add(b, getDayOfWeek(calendar) + " " + (calendar.get(Calendar.WEEK_OF_MONTH) - 1));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
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
