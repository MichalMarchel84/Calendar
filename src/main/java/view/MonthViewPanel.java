package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

class MonthViewPanel extends JPanel implements LanguageListener{

    MonthViewPanel(){
        double[] cols = new double[7];
        double[] rows = new double[30];
        Arrays.fill(cols, 1d/7);
        Arrays.fill(rows, TableLayout.PREFERRED);
        TableLayout lay = new TableLayout(cols, rows);
        lay.setVGap(10);
        JPanel days = new JPanel();
        days.setLayout(lay);
        for(int i = 0; i < rows.length; i++){
            for(int j = 0; j < cols.length; j++){
                days.add(new JButton(Integer.toString(i*cols.length + j + 1)), Integer.toString(j) + " " + Integer.toString(i) + " f c");
            }
        }
        JScrollPane scroll = new JScrollPane(days);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        this.setLayout(new BorderLayout());
        this.add(scroll, BorderLayout.CENTER);
    }

    @Override
    public void languageChanged() {

    }
}
