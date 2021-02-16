package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

class MonthViewPanel extends JPanel implements LanguageListener{

    private static final String[] daysOfWeek=  {"mon", "tue", "wed", "thu", "fri", "sat", "sun"};
    private JLabel daysOfWeekLabels[] = new JLabel[7];
    private static final int weeksInBuffer = 20;
    private static final int weeksOnDisplay = 6;
    private final JPanel days = new DaysPanel();

    MonthViewPanel(){

        I18n.addLanguageListener(this);

        this.setTexts();

        double[] cols = {TableLayout.FILL};
        double[] rows = {30, TableLayout.FILL};
        this.setLayout(new TableLayout(cols, rows));
        JPanel temp = new JPanel();
        temp.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        temp.add(new JLabel("Temporary panel"));
        this.add(temp, "0 0 f f");
        this.add(days, "0 1 f f");
    }

    private class DaysPanel extends JPanel{

        private final double[] cellSize = {1d/7, 1d/weeksInBuffer};
        private final JPanel content = new JPanel();
        private final int hGap = 10;
        private final int vGap = 10;
        private final JScrollPane scroll = createDayContent();
        private final JPanel weekDays = createWeekDaysLabels();

        DaysPanel(){
            double[] cols = {TableLayout.FILL};
            double[] rows = {30, TableLayout.FILL};
            this.setLayout(new TableLayout(cols, rows));
            this.add(weekDays, "0 0 f f");
            this.add(scroll, "0 1 f f");
        }

        private JScrollPane createDayContent() {
            double[] cols = new double[7];
            double[] rows = new double[weeksInBuffer];
            Arrays.fill(cols, cellSize[0]);
            Arrays.fill(rows, cellSize[1]);
            TableLayout lay = new TableLayout(cols, rows);
            lay.setVGap(vGap);
            lay.setHGap(hGap);
            content.setLayout(lay);
            for (int i = 0; i < rows.length; i++) {
                for (int j = 0; j < cols.length; j++) {
                    JButton day = new JButton(Integer.toString(i * cols.length + j + 1));
                    content.add(day, Integer.toString(j) + " " + Integer.toString(i) + " f f");
                }
            }
            JScrollPane scroll = new JScrollPane(content);
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scroll.addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    super.componentResized(e);
                    content.setPreferredSize(new Dimension(e.getComponent().getWidth() - scroll.getVerticalScrollBar().getWidth(),
                            e.getComponent().getHeight() * weeksInBuffer / weeksOnDisplay));
                }
            });
            return scroll;
        }

        private JPanel createWeekDaysLabels(){

            for(int i = 0; i < daysOfWeekLabels.length; i++){
                daysOfWeekLabels[i] = new JLabel();
                daysOfWeekLabels[i].setFont(new Font(this.getFont().getName(), Font.BOLD, 15));
            }
            daysOfWeekLabels[6].setForeground(Color.RED);

            JPanel panel = new JPanel();
            double[] cols = new double[8];
            double[] rows = {TableLayout.FILL};
            Arrays.fill(cols, cellSize[0]);
            cols[7] = scroll.getVerticalScrollBar().getPreferredSize().getWidth();
            TableLayout lay = new TableLayout(cols, rows);
            panel.setLayout(lay);
            for(int i = 0; i < daysOfWeekLabels.length; i++){
                panel.add(daysOfWeekLabels[i], i + " 0 c c");
            }
            return panel;
        }

    }

    private void setTexts(){
        for(int i = 0; i < daysOfWeek.length; i++){
            daysOfWeekLabels[i].setText(I18n.getPhrase(daysOfWeek[i]));
        }
    }

    @Override
    public void languageChanged() {
        setTexts();
    }
}
