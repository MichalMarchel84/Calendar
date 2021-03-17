package view;

import info.clearthought.layout.TableLayout;
import model.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

class NewUserPanel extends JPanel implements LanguageListener, ActionListener, ErrorDisplaying {

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
    private final Dimension buttonSize = new Dimension(120, 40);

    NewUserPanel() {
        setTexts();
        I18n.addLanguageListener(this);

        double[] cols = {TableLayout.FILL,TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.FILL};
        double[] rows = new double[10];
        Arrays.fill(rows, TableLayout.PREFERRED);
        rows[0] = TableLayout.FILL;
        rows[9] = TableLayout.FILL;
        TableLayout lay = new TableLayout(cols, rows);
        lay.setHGap(10);
        lay.setVGap(10);
        this.setLayout(lay);

        this.add(username_label, "1 1 2 1 c c");

        userName.setPreferredSize(txtFieldSize);
        this.add(userName, "1 2 2 2 c c");

        this.add(pass_label, "1 3 2 3 c c");

        pass.setPreferredSize(txtFieldSize);
        this.add(pass, "1 4 2 4 c c");

        this.add(repeatPass_label, "1 5 2 5 c c");

        repeatPass.setPreferredSize(txtFieldSize);
        this.add(repeatPass, "1 6 2 6 c c");

        create.setPreferredSize(buttonSize);
        create.addActionListener(this);
        this.add(create, "1 7 r c");

        cancel.setPreferredSize(buttonSize);
        cancel.addActionListener(this);
        this.add(cancel, "2 7 l c");

        errorMessage.setForeground(Color.RED);
        this.add(errorMessage, "1 8 2 8 c c");
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

    @Override
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
                App.controller.login(userName.getText(), String.copyValueOf(pass.getPassword()), true);
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
