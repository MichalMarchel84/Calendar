package view;

import controller.LoginRequest;
import info.clearthought.layout.TableLayout;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
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
    private final Dimension buttonSize = new Dimension(120, 40);

    LoginPanel() {

        setTexts();
        I18n.addLanguageListener(this);

        double[] cols = {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL};
        double[] rows = new double[9];
        Arrays.fill(rows, TableLayout.PREFERRED);
        rows[0] = TableLayout.FILL;
        rows[8] = TableLayout.FILL;
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        lay.setVGap(10);
        this.setLayout(lay);

        msg.setPreferredSize(new Dimension(200, 200));
        msg.setHorizontalAlignment(JLabel.CENTER);
        this.add(msg, "1 1 2 1");

        this.add(username_label, "1 2 2 2 c c");

        userName.setPreferredSize(txtFieldSize);
        this.add(userName, "1 3 2 3 c c");

        this.add(pass_label, "1 4 2 4 c c");

        pass.setPreferredSize(txtFieldSize);
        this.add(pass, "1 5 2 5 c c");

        signIn.setPreferredSize(buttonSize);
        signIn.addActionListener(this);
        this.add(signIn, "1 6 r c");

        newUser.setPreferredSize(buttonSize);
        newUser.addActionListener(this);
        this.add(newUser, "2 6 l c");

        errorMessage.setForeground(Color.RED);
        this.add(errorMessage, "1 7 2 7 c c");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(300);
                }
                catch (Exception e){
                    e.printStackTrace();
                }
                userName.requestFocus();
            }
        });
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
