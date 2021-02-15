package view;

import controller.NewUserRequest;
import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewUserPanel extends JPanel implements LanguageListener, ActionListener {

    private final JTextField userName = new JTextField(20);
    private final JPasswordField pass = new JPasswordField(20);
    private final JPasswordField repeatPass = new JPasswordField(20);
    private final JButton create = new JButton();
    private final JButton cancel = new JButton();
    private final JLabel username_label = new JLabel();
    private final JLabel pass_label = new JLabel();
    private final JLabel repeatPass_label = new JLabel();
    private final JLabel errorMessage = new JLabel();
    private final Dimension txtFieldSize = new Dimension(100, 30);
    private final Dimension buttonSize = new Dimension(80, 40);

    public NewUserPanel() {
        setTexts();
        I18n.addLanguageListener(this);

        double[] cols = {TableLayout.PREFERRED, TableLayout.PREFERRED};
        double[] rows = new double[8];
        for(int i = 0; i < rows.length; i++){
            rows[i] = TableLayout.PREFERRED;
        }
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        lay.setVGap(10);
        this.setLayout(lay);

        this.add(username_label, "0 0 1 0");

        userName.setPreferredSize(txtFieldSize);
        this.add(userName, "0 1 1 1 c c");

        this.add(pass_label, "0 2 1 2");

        pass.setPreferredSize(txtFieldSize);
        this.add(pass, "0 3 1 3 c c");

        this.add(repeatPass_label, "0 4 1 4");

        repeatPass.setPreferredSize(txtFieldSize);
        this.add(repeatPass, "0 5 1 5 c c");

        create.setPreferredSize(buttonSize);
        create.addActionListener(this);
        this.add(create, "0 6");

        cancel.setPreferredSize(buttonSize);
        cancel.addActionListener(this);
        this.add(cancel, "1 6");

        errorMessage.setForeground(Color.RED);
        this.add(errorMessage, "0 7 1 7");
    }

    private void setTexts(){
        create.setText(I18n.getPhrase("create_user_button"));
        cancel.setText(I18n.getPhrase("cancel_button"));
        username_label.setText(I18n.getPhrase("username_label"));
        pass_label.setText(I18n.getPhrase("pass_label"));
        repeatPass_label.setText(I18n.getPhrase("repeat_pass_label"));
        errorMessage.setText("");
        this.repaint();
    }

    public void setErrorMessage(String errMsg){
        errorMessage.setText(I18n.getPhrase(errMsg));
        this.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource().equals(create)){
            String p1 = new String(pass.getPassword());
            String p2 = new String(repeatPass.getPassword());
            if(p1.equals(p2)) {
                Comm.fireRequestEvent(new NewUserRequest(userName.getText(), pass.getPassword(), this));
            }
            else{
                this.setErrorMessage("error_on_pass_set");
            }
        }
        else if(e.getSource().equals(cancel)){
            AppWindow app = (AppWindow) this.getTopLevelAncestor();
            app.displayPanel(AppWindow.panels.login);
        }
    }

    @Override
    public void languageChanged() {
        setTexts();
    }
}
