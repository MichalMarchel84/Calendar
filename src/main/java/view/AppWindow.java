package view;

import info.clearthought.layout.TableLayout;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

public class AppWindow extends JFrame{

    private static final String[] langs = getAvailableLangs();

    private static ResourceBundle lang;

    public AppWindow() {

        setInitialLang();

        double[] mainCols = {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL};
        double[] mainRows = {TableLayout.FILL, TableLayout.PREFERRED, TableLayout.FILL};
        TableLayout lay = new TableLayout(mainCols, mainRows);
        lay.setVGap(10);
        lay.setHGap(10);
        this.getContentPane().setLayout(lay);

        JButton language = new JButton(Locale.getDefault().getLanguage().toUpperCase(Locale.ROOT));
        String flagURL = "src/main/resources/flag_icons/" + lang.getLocale().getLanguage() + ".gif";
        language.setIcon(new ImageIcon(flagURL));
        this.add(language, "2 0 c c");

        this.add(new LoginPanel(), "1 1");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setPreferredSize(new Dimension(800, 600));
        this.setMinimumSize(new Dimension(300, 600));
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static String getPhrase(String key){
        return lang.getString(key);
    }

    private static String[] getAvailableLangs(){
        ArrayList<String> list = new ArrayList<>();
        File file = new File("src/main/resources/i18n");
        String[] fileList = file.list();
        for(String fl : fileList){
            int index = fl.indexOf('_');
            if(index > -1) {
                list.add(fl.substring(fl.indexOf('_') + 1, fl.indexOf('_') + 3));
            }
        }
        return list.toArray(new String[0]);
    }

    private void setInitialLang(){
        Locale locale = null;
        for(String l : langs){
            if(Locale.getDefault().getLanguage().equals(l)){
                locale = new Locale(l);
                break;
            }
        }
        if(locale == null){
            locale = new Locale("");
        }
        lang = ResourceBundle.getBundle("i18n.language", locale);
    }

    private String getFlagForLanguage(int langNum){
        if(langNum < langs.length) {
            ResourceBundle temp = ResourceBundle.getBundle("i18n.language", new Locale(langs[langNum]));
            return "src/main/resources/flag_icons/" + temp.getString("language_flag");
        }
        else return null;
    }
}
