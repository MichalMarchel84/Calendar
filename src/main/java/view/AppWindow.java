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

        double[] mainCols = {TableLayout.FILL};
        double[] mainRows = {30, TableLayout.FILL};
        TableLayout lay = new TableLayout(mainCols, mainRows);
        this.getContentPane().setLayout(lay);

        languageSelection = new JComboBox<>(I18n.getAvailableLangs());
        languageSelection.setRenderer(new FlagListRenderer());
        languageSelection.setSelectedIndex(I18n.getLangIndex());
        languageSelection.addActionListener(this);
        I18n.addLanguageListener(this);

        this.add(languageSelection, "0 0 r c");
        this.displayPanel(panels.login);

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
    }

    @Override
    public void languageChanged() {
        //this.repaint();
    }
}
