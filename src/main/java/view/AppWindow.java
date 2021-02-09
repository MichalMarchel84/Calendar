package view;

import javax.swing.*;
import java.awt.*;

public class AppWindow {


    JFrame frame = new JFrame();

    public AppWindow() {
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

}
