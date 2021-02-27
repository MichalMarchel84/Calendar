package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;

class EntryLabel extends JPanel {

    private final JTextField title = new JTextField(50);
    private final JTextArea description = new JTextArea();

    EntryLabel(){
        double[] cols = {10, 0.2, TableLayout.FILL, 10};
        double[] rows = {10, 0.2, TableLayout.FILL, 10};
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        lay.setVGap(10);
        this.setLayout(lay);
        this.add(title, "2 1 f f");
        this.add(description, "2 2 f f");
        JPanel temp = new JPanel();
        temp.setBackground(Color.GREEN);
        this.add(temp, "1 1 1 2 f f");
        this.setBorder(BorderFactory.createBevelBorder(0));
    }
}
