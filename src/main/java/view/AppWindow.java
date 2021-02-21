package view;

import info.clearthought.layout.TableLayout;
import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;

public class AppWindow extends JFrame implements ActionListener, LanguageListener{

    public enum panels {login, newUser, monthView, dayView}
    private JPanel onDisplay;
    private final JComboBox<String> languageSelection;
    private final LoginPanel login = new LoginPanel();
    private final NewUserPanel newUser = new NewUserPanel();
    private final MonthViewPanel monthView = new MonthViewPanel();
    final DayViewPanel dayView = new DayViewPanel();
    private final JButton returnButton = new BasicArrowButton(BasicArrowButton.WEST);
    private final JPanel topMenu = new JPanel();

    public AppWindow() {

        double[] mainCols = {TableLayout.FILL};
        double[] mainRows = {30, TableLayout.FILL};
        TableLayout lay = new TableLayout(mainCols, mainRows);
        this.getContentPane().setLayout(lay);

        languageSelection = new JComboBox<>(I18n.getAvailableLangs());
        languageSelection.setRenderer(new FlagListRenderer());
        languageSelection.setSelectedIndex(I18n.getLangIndex());
        languageSelection.addActionListener(this);
        I18n.addLanguageListener(this);

        double[] cols = {0.2, TableLayout.FILL, TableLayout.PREFERRED};
        double[] rows = {TableLayout.FILL};
        topMenu.setLayout(new TableLayout(cols, rows));
        returnButton.addActionListener(this);
        topMenu.add(languageSelection, "2 0 c c");

        this.add(topMenu, "0 0 f f");
        this.displayPanel(panels.login);
        //this.displayPanel(panels.monthView);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(300, 500));
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
                topMenu.add(returnButton, "0 0 f f");
                break;
            case monthView:
                onDisplay = monthView;
                this.setSize(new Dimension(1000, 700));
                this.setLocationRelativeTo(null);
                break;
            default:
                onDisplay = login;
                login.setErrorMessage("Unknown panel call");
        }
        this.add(onDisplay, "0 1 f f");
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
        else if(e.getSource().equals(returnButton)){
            displayPanel(panels.monthView);
            topMenu.remove(returnButton);
        }
    }

    @Override
    public void languageChanged() {
        //this.repaint();
    }
}
