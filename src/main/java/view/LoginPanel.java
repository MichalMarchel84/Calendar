package view;

import controller.LoginRequest;
import info.clearthought.layout.TableLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

class LoginPanel extends JPanel implements ActionListener, LanguageListener, ErrorDisplaying{

    private final JTextField userName = new JTextField(20);
    private final JPasswordField pass = new JPasswordField(20);
    private final JButton signIn = new JButton();
    private final JButton newUser = new JButton();
    private final JLabel msg = new JLabel();
    private final JLabel username_label = new JLabel();
    private final JLabel pass_label = new JLabel();
    private final JLabel errorMessage = new JLabel();
    private final Dimension txtFieldSize = new Dimension(100, 30);
    private final Dimension buttonSize = new Dimension(80, 40);

    LoginPanel() {

        setTexts();
        I18n.addLanguageListener(this);

        double[] cols = {TableLayout.PREFERRED, TableLayout.PREFERRED};
        double[] rows = new double[7];
        Arrays.fill(rows, TableLayout.PREFERRED);
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

        signIn.setPreferredSize(buttonSize);
        signIn.addActionListener(this);
        this.add(signIn, "0 5");

        newUser.setPreferredSize(buttonSize);
        newUser.addActionListener(this);
        this.add(newUser, "1 5");

        errorMessage.setForeground(Color.RED);
        this.add(errorMessage, "0 6 1 6");
    }

    private void setTexts(){
        signIn.setText(I18n.getPhrase("login_button"));
        newUser.setText(I18n.getPhrase("new_user_button"));
        msg.setText(I18n.getPhrase("login_message"));
        username_label.setText(I18n.getPhrase("username_label"));
        pass_label.setText(I18n.getPhrase("pass_label"));
        errorMessage.setText("");
        this.repaint();
    }

    @Override
    public void setErrorMessage(String errMsg){
        errorMessage.setText(I18n.getPhrase(errMsg));
        this.repaint();
    }

    @Override
    public void languageChanged() {
        setTexts();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(signIn)){
            Comm.fireRequestEvent(new LoginRequest(userName.getText(), pass.getPassword()));
        }
        else if(e.getSource().equals(newUser)){
            AppWindow app = (AppWindow) this.getTopLevelAncestor();
            app.displayPanel(AppWindow.panels.newUser);
        }
    }
}
