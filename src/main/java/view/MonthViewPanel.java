package view;

import javax.swing.*;
import java.awt.*;

class MonthViewPanel extends JPanel {

    MonthViewPanel(){
        JLabel temp = new JLabel("Test");
        temp.setFont(new Font(temp.getFont().getName(), Font.BOLD, 30));
        this.add(temp);
    }
}
