package view;

import info.clearthought.layout.TableLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AppWindow extends JFrame implements ActionListener, LanguageListener{

    public enum panels {login, newUser, monthView, dayView}
    private JPanel onDisplay;
    JComboBox<String> languageSelection;
    LoginPanel login = new LoginPanel();
    NewUserPanel newUser = new NewUserPanel();
    MonthViewPanel monthView = new MonthViewPanel();
    DayViewPanel dayView = new DayViewPanel();

    public AppWindow() {

        double[] mainCols = {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL};
        double[] mainRows = {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL};
        TableLayout lay = new TableLayout(mainCols, mainRows);
        lay.setVGap(10);
        lay.setHGap(10);
        this.getContentPane().setLayout(lay);

        languageSelection = new JComboBox<>(I18n.getAvailableLangs());
        languageSelection.setRenderer(new FlagListRenderer());
        languageSelection.setSelectedIndex(I18n.getLangIndex());
        languageSelection.addActionListener(this);
        I18n.addLanguageListener(this);

        this.displayPanel(panels.login);
        this.add(languageSelection, "2 0 c c");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));
        this.setMinimumSize(new Dimension(300, 600));
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

    }

    private static class FlagListRenderer extends DefaultListCellRenderer{

        public FlagListRenderer(){}

        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index,boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            Icon icon = new ImageIcon(I18n.getFlagForLanguage((String)value));
            label.setIcon(icon);
            return label;
        }
    }

    public void displayPanel(panels panelName){
        if(onDisplay != null){
            this.remove(onDisplay);
        }
        switch (panelName){
            case login:
                onDisplay = login;
                break;
            case newUser:
                onDisplay = newUser;
                break;
            case dayView:
                onDisplay = dayView;
                break;
            case monthView:
                onDisplay = monthView;
                break;
            default:
                onDisplay = login;
                login.setErrorMessage("Unknown panel call");
        }
        this.add(onDisplay, "1 1");
        this.revalidate();
        this.repaint();
    }

    public void displayError(String error){
        if(onDisplay instanceof ErrorDisplaying){
            ((ErrorDisplaying) onDisplay).setErrorMessage(error);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource().equals(languageSelection)){
            I18n.setLang(languageSelection.getSelectedIndex());
        }
    }

    @Override
    public void languageChanged() {
        //this.repaint();
    }
}
