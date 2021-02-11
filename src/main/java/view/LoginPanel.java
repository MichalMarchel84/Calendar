package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;

class LoginPanel extends JPanel{
    private JTextField userName = new JTextField(20);
    private JPasswordField pass = new JPasswordField(20);
    private JButton signIn = new JButton();
    private JButton newUser = new JButton();
    private Dimension txtFieldSize = new Dimension(100, 30);
    private Dimension buttonSize = new Dimension(80, 40);

    public LoginPanel() {

        double[] cols = {TableLayout.PREFERRED, TableLayout.PREFERRED};
        double[] rows = {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED};
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        lay.setVGap(10);
        this.setLayout(lay);

        JLabel msg = new JLabel(I18n.getPhrase("login_message"));
        msg.setPreferredSize(new Dimension(200, 200));
        msg.setHorizontalAlignment(JLabel.CENTER);
        this.add(msg, "0 0 1 0");

        this.add(new JLabel(I18n.getPhrase("username_label")), "0 1 1 1");

        userName.setPreferredSize(txtFieldSize);
        this.add(userName, "0 2 1 2 c c");

        this.add(new JLabel(I18n.getPhrase("pass_label")), "0 3 1 3");

        pass.setPreferredSize(txtFieldSize);
        this.add(pass, "0 4 1 4 c c");

        signIn.setText(I18n.getPhrase("login_button"));
        signIn.setPreferredSize(buttonSize);
        this.add(signIn, "0 5");

        newUser.setText(I18n.getPhrase("new_user_button"));
        newUser.setPreferredSize(buttonSize);
        this.add(newUser, "1 5");
    }
}
