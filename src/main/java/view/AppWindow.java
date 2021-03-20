package view;

import controller.AppWindowController;
import info.clearthought.layout.TableLayout;
import model.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class AppWindow extends JFrame implements ActionListener, LanguageListener{


    public final JComboBox<String> languageSelection;
    public final JButton returnButton = new JButton(I18n.getPhrase("return"));
    public final JButton logout = new JButton(I18n.getPhrase("logout"));
    public final JPanel topMenu = new JPanel();

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

        double[] cols = {TableLayout.PREFERRED, TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED, 20};
        double[] rows = {TableLayout.FILL};
        TableLayout layout = new TableLayout(cols, rows);
        layout.setHGap(20);
        topMenu.setLayout(layout);
        topMenu.add(languageSelection, "3 0 c c");

        this.add(topMenu, "0 0 f f");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setMinimumSize(new Dimension(300, 500));
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void addLogoutButton(){
        topMenu.add(logout, "2 0 c c");
    }

    public void addReturnButton(){

        topMenu.add(returnButton, "0 0 c c");
        topMenu.revalidate();
        topMenu.repaint();
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

    @Override
    public void languageChanged() {
        returnButton.setText(I18n.getPhrase("return"));
        logout.setText(I18n.getPhrase("logout"));
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(languageSelection)) {
            I18n.setLang(languageSelection.getSelectedIndex());
        }
    }
}
