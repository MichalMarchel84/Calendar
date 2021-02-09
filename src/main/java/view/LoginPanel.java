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

    public LoginPanel(int lang) {
        double[] mainCols = {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL};
        double[] mainRows = {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL};
        this.setLayout(new TableLayout(mainCols, mainRows));

        JPanel p = new JPanel();
        double[] cols = {TableLayout.PREFERRED, TableLayout.PREFERRED};
        double[] rows = {TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED};
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        lay.setVGap(10);
        p.setLayout(lay);
        JLabel msg = new JLabel(Dictionary.loginPanel_messageLabel[lang]);
        msg.setPreferredSize(new Dimension(200, 200));
        msg.setHorizontalAlignment(JLabel.CENTER);
        p.add(msg, "0 0 1 0");
        p.add(new JLabel(Dictionary.loginPanel_loginLabel[lang]), "0 1 1 1");
        userName.setPreferredSize(txtFieldSize);
        p.add(userName, "0 2 1 2 c c");
        p.add(new JLabel(Dictionary.loginPanel_passwordLabel[lang]), "0 3 1 3");
        pass.setPreferredSize(txtFieldSize);
        p.add(pass, "0 4 1 4 c c");
        signIn.setText(Dictionary.loginPanel_signInButton[lang]);
        signIn.setPreferredSize(buttonSize);
        p.add(signIn, "0 5");
        newUser.setText(Dictionary.loginPanel_newUserButton[lang]);
        newUser.setPreferredSize(buttonSize);
        p.add(newUser, "1 5");
        this.add(p, "1 1");
    }
}
