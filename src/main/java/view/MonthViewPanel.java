package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

class MonthViewPanel extends JPanel implements LanguageListener{

    private static final String[] daysOfWeek=  {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
    private final ArrayList<JLabel> dayList = new ArrayList<>();
    private final JPanel dayLabels = createDayLabels();
    private final JPanel content = createContent();
    private final JScrollPane scroll = createScrollPanel();
    private final ArrayList<MonthPanel> monthList = new ArrayList<>();

    private final int monthsInBuffer = 3;   //amount before and after presented month (total 2*monthsInBuffer + 1)
    private final int weeksOnDisplay = 6;
    private final int gapBetweenMonths = 50;

    MonthViewPanel(){

        I18n.addLanguageListener(this);

        double[] cols = {TableLayout.FILL};
        double[] rows = {30, TableLayout.FILL};
        this.setLayout(new TableLayout(cols, rows));

        this.add(dayLabels, "0 0 f f");
        this.add(scroll, "0 1");
        setContent();
    }

    private JScrollPane createScrollPanel(){
        JScrollPane panel = new JScrollPane(content);
        panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.getVerticalScrollBar().setUnitIncrement(10);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int w = panel.getViewport().getWidth();
                int h = panel.getViewport().getHeight()/weeksOnDisplay;
                int totalRows = 0;
                for(MonthPanel m : monthList){
                    m.setPreferredSize(new Dimension(w, h*m.getWeeks()));
                    totalRows += m.getWeeks();
                }
                content.setPreferredSize(new Dimension(w, totalRows*h + 2*monthsInBuffer*gapBetweenMonths));
            }
        });
        return panel;
    }

    private JPanel createContent(){
        JPanel panel = new JPanel();
        double[] cols = {TableLayout.FILL};
        double[] rows = new double[2*monthsInBuffer + 1];
        Arrays.fill(rows, TableLayout.PREFERRED);
        TableLayout lay = new TableLayout(cols, rows);
        lay.setVGap(gapBetweenMonths);
        panel.setLayout(lay);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                int sv = (scroll.getVerticalScrollBar().getMaximum() - scroll.getViewport().getHeight())/2;
                scroll.getVerticalScrollBar().setValue(sv);
            }
        });
        return panel;
    }

    private JPanel createDayLabels(){
        JPanel panel = new JPanel();
        double[] cols = new double[9];
        double[] rows = {TableLayout.FILL};
        double leftGap = 0.1;
        double rightGap = 5;
        Arrays.fill(cols, TableLayout.FILL);
        cols[0] = leftGap;
        cols[8] = rightGap;
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        panel.setLayout(lay);
        for(int i = 0; i < 7; i++){
            JLabel label = new JLabel(I18n.getPhrase(daysOfWeek[i]));
            if(i == 6){
                label.setForeground(Color.RED);
            }
            dayList.add(label);
            panel.add(label, (i+1) + " 0 c c");
        }
        return panel;
    }

    void setContent(){
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.add(Calendar.MONTH, -monthsInBuffer);
        for(int i = 0; i < (2*monthsInBuffer + 1); i++){
            MonthPanel p = new MonthPanel(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
            monthList.add(p);
            content.add(p, "0 " + i + " f f");
            calendar.add(Calendar.MONTH, 1);
        }
        monthList.get(monthsInBuffer)
                    .getDayList()
                    .get(Calendar.getInstance(Locale.getDefault())
                    .get(Calendar.DAY_OF_MONTH) - 1)
                    .setBorder(BorderFactory.createLineBorder(Color.RED, 5));
    }

    private void setTexts(){
        for (int i = 0; i < 7; i++){
            dayList.get(i).setText(I18n.getPhrase(daysOfWeek[i]));
        }
    }

    @Override
    public void languageChanged() {
        monthList.forEach((m) -> m.repaint());
        setTexts();
    }
}
