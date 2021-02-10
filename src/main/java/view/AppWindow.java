package view;

import javax.swing.*;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppWindow extends JFrame{

    private static ResourceBundle lang = ResourceBundle.getBundle("i18n.language", Locale.getDefault());

    public AppWindow() {
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.add(new LoginPanel());
        this.setVisible(true);
    }

    public static String getPhrase(String key){
        return lang.getString(key);
    }
}
