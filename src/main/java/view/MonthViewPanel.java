package view;

import controller.MonthViewController;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class MonthViewPanel extends JPanel implements LanguageListener{

    private static final String[] daysOfWeek=  {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
    private final ArrayList<JLabel> dayList = new ArrayList<>();
    private final JPanel content = createContent();
    private final JScrollPane scroll = createScrollPanel();
    private final ArrayList<MonthPanel> monthList = new ArrayList<>();
    public boolean inhibit = true;
    private final MonthViewController controller;

    private final int monthsInBuffer = 3;   //amount before and after presented month (total 2*monthsInBuffer + 1)
    private final int weeksOnDisplay = 8;
    private final int gapBetweenMonths = 50;
    private final int bufferUpdate = 2;     //amount of months buffer shifts when reach end

    public MonthViewPanel(MonthViewController controller){

        this.controller = controller;
        I18n.addLanguageListener(this);

        double[] cols = {TableLayout.FILL};
        double[] rows = {30, TableLayout.FILL};
        this.setLayout(new TableLayout(cols, rows));

        this.add(createDayLabels(), "0 0 f f");
        this.add(scroll, "0 1");
        setContent();
    }

    private JScrollPane createScrollPanel(){
        JScrollPane panel = new JScrollPane(content);
        panel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        panel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.getVerticalScrollBar().setUnitIncrement(10);
        panel.getVerticalScrollBar().setValue(50);
        panel.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                resizeContent();
                centerContent();
            }
        });
        panel.getVerticalScrollBar().addAdjustmentListener(e -> {
            if(!inhibit) {
                if (e.getValue() == 0) {
                    changeBufferUp();
                } else if (e.getValue() == (scroll.getVerticalScrollBar().getMaximum() - scroll.getViewport().getHeight())) {
                    changeBufferDown();
                }
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

    public void setContent(){
        content.removeAll();
        monthList.clear();
        for(int i = 0; i < (2*monthsInBuffer + 1); i++){
            MonthPanel p = new MonthPanel(LocalDate.now().minusMonths(monthsInBuffer - i), controller);
            monthList.add(p);
            content.add(p, "0 " + i + " f f");
        }
        resizeContent();
    }

    private void changeBufferUp(){
        LocalDate date = monthList.get(0).getDate();
        for (int i = 0; i < bufferUpdate; i++){
            monthList.add(0, new MonthPanel(date.minusMonths(i + 1), controller));
            monthList.remove(monthList.size() - 1);
        }
        content.removeAll();
        for(int i = 0; i < (2*monthsInBuffer + 1); i++){
            content.add(monthList.get(i), "0 " + i + " f f");
        }
        resizeContent();
        int offsetY = 0;
        for (int i = 0; i < bufferUpdate; i++){
            offsetY += monthList.get(i).getPreferredSize().getHeight();
            offsetY += gapBetweenMonths;
        }
        scroll.getVerticalScrollBar().setValue(offsetY);
    }

    private void changeBufferDown(){
        LocalDate date = monthList.get(monthList.size() - 1).getDate();
        for (int i = 0; i < bufferUpdate; i++){
            monthList.add(new MonthPanel(date.plusMonths(i + 1), controller));
            monthList.remove(0);
        }
        content.removeAll();
        for(int i = 0; i < (2*monthsInBuffer + 1); i++){
            content.add(monthList.get(i), "0 " + i + " f f");
        }
        resizeContent();
        int offsetY = -gapBetweenMonths;
        for(int i = 0; i < (monthList.size() - bufferUpdate); i++){
            offsetY += monthList.get(i).getPreferredSize().getHeight();
            offsetY += gapBetweenMonths;
        }
        offsetY -= scroll.getViewport().getHeight();
        scroll.getVerticalScrollBar().setValue(offsetY);
    }

    private void resizeContent(){
        int w = scroll.getViewport().getWidth();
        int h = scroll.getViewport().getHeight()/weeksOnDisplay;
        int totalRows = 0;
        for(MonthPanel m : monthList){
            m.setPreferredSize(new Dimension(w, h*m.getWeeks()));
            totalRows += m.getWeeks();
        }
        content.setPreferredSize(new Dimension(w, totalRows*h + 2*monthsInBuffer*gapBetweenMonths));
    }

    public void centerContent(){
        int sv = (scroll.getVerticalScrollBar().getMaximum() - scroll.getViewport().getHeight())/2;
        scroll.getVerticalScrollBar().setValue(sv);
    }

    private void setTexts(){
        for (int i = 0; i < 7; i++){
            dayList.get(i).setText(I18n.getPhrase(daysOfWeek[i]));
        }
    }

    @Override
    public void languageChanged() {
        monthList.forEach(Component::repaint);
        setTexts();
    }
}
