package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

class MonthViewPanel extends JPanel implements LanguageListener{

    MonthViewPanel(){
        double[] cols = new double[7];
        double[] rows = new double[30];
        Arrays.fill(cols, TableLayout.PREFERRED);
        Arrays.fill(rows, TableLayout.PREFERRED);
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        lay.setVGap(10);
        JPanel days = new JPanel();
        days.setLayout(lay);
        for(int i = 0; i < rows.length; i++){
            for(int j = 0; j < cols.length; j++){
                days.add(new JButton(Integer.toString(i*cols.length + j)), Integer.toString(j) + " " + Integer.toString(i));
            }
        }
        JScrollPane scroll = new JScrollPane(days);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setPreferredSize(new Dimension(400, 400));
        this.add(scroll);
    }

    @Override
    public void languageChanged() {

    }
}
