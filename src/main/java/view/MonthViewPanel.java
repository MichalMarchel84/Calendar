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
    private final JPanel content = createContent();
    private final JScrollPane scroll = createScrollPanel();
    private JPanel dayOfWeekLabels;
    private final ArrayList<MonthPanel> monthList = new ArrayList<>();
    private final int monthsInBuffer = 3;
    private final int weeksOnDisplay = 6;

    MonthViewPanel(){

        I18n.addLanguageListener(this);

        double[] cols = {TableLayout.FILL};
        double[] rows = {30, TableLayout.FILL};
        this.setLayout(new TableLayout(cols, rows));

        JPanel temp = new JPanel();
        temp.setBorder(BorderFactory.createBevelBorder(0));
        this.add(temp, "0 0 f f");
        this.add(scroll, "0 1");
        setContent(2, 2021);
    }

    private JScrollPane createScrollPanel(){
        JScrollPane panel = new JScrollPane(content);
        panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
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
                content.setPreferredSize(new Dimension(w, totalRows*h));
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
        lay.setVGap(50);
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

    void setContent(int month, int year){
        month--;
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.add(Calendar.MONTH, -monthsInBuffer);
        for(int i = 0; i < (2*monthsInBuffer + 1); i++){
            MonthPanel p = new MonthPanel(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR));
            monthList.add(p);
            content.add(p, "0 " + i + " f f");
            calendar.add(Calendar.MONTH, 1);
        }
    }

    private void setTexts(){

    }

    @Override
    public void languageChanged() {
        setTexts();
    }
}
