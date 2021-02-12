package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;

class LoginPanel extends JPanel implements LanguageListener{

    private JTextField userName = new JTextField(20);
    private JPasswordField pass = new JPasswordField(20);
    private JButton signIn = new JButton();
    private JButton newUser = new JButton();
    private JLabel msg = new JLabel();
    private JLabel username_label = new JLabel();
    private JLabel pass_label = new JLabel();
    private Dimension txtFieldSize = new Dimension(100, 30);
    private Dimension buttonSize = new Dimension(80, 40);

    public LoginPanel() {

        setTexts();
        I18n.addLanguageListener(this);

        double[] cols = {TableLayout.PREFERRED, TableLayout.PREFERRED};
        double[] rows = {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED};
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        lay.setVGap(10);
        this.setLayout(lay);

        msg.setPreferredSize(new Dimension(200, 200));
        msg.setHorizontalAlignment(JLabel.CENTER);
        this.add(msg, "0 0 1 0");

        this.add(username_label, "0 1 1 1");

        userName.setPreferredSize(txtFieldSize);
        this.add(userName, "0 2 1 2 c c");

        this.add(pass_label, "0 3 1 3");

        pass.setPreferredSize(txtFieldSize);
        this.add(pass, "0 4 1 4 c c");

        //signIn.setText(I18n.getPhrase("login_button"));
        signIn.setPreferredSize(buttonSize);
        this.add(signIn, "0 5");

        //newUser.setText(I18n.getPhrase("new_user_button"));
        newUser.setPreferredSize(buttonSize);
        this.add(newUser, "1 5");
    }

    private void setTexts(){
        signIn.setText(I18n.getPhrase("login_button"));
        newUser.setText(I18n.getPhrase("new_user_button"));
        msg.setText(I18n.getPhrase("login_message"));
        username_label.setText(I18n.getPhrase("username_label"));
        pass_label.setText(I18n.getPhrase("pass_label"));
    }

    @Override
    public void languageChanged() {
        setTexts();
        this.repaint();
    }
}
