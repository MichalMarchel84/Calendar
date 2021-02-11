package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class AppWindow extends JFrame{

    public AppWindow() {

        double[] mainCols = {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL};
        double[] mainRows = {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL};
        TableLayout lay = new TableLayout(mainCols, mainRows);
        lay.setVGap(10);
        lay.setHGap(10);
        this.getContentPane().setLayout(lay);

        /*JButton language = new JButton(Locale.getDefault().getLanguage().toUpperCase(Locale.ROOT));
        String flagURL = "src/main/resources/flag_icons/" + lang.getLocale().getLanguage() + ".gif";
        language.setIcon(new ImageIcon(flagURL));
        this.add(language, "2 0 c c");*/
        JComboBox box = new JComboBox(I18n.getAvailableLangs());

        this.add(new LoginPanel(), "1 1");
        this.add(box, "2 0 c c");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));
        this.setMinimumSize(new Dimension(300, 600));
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
}
