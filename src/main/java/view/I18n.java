package view;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

class I18n {

    private static final String defaultLang = "en";

    private static final String[] langs = getLangs();

    private static ResourceBundle lang = setInitialLang();

    private I18n(){}

    private static ResourceBundle setInitialLang(){
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
        return ResourceBundle.getBundle("i18n.language", locale);
    }

    private static String[] getLangs(){
        ArrayList<String> list = new ArrayList<>();
        list.add(defaultLang);
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

    static String[] getAvailableLangs(){
        String[] res = new String[langs.length];
        for(int i = 0; i < res.length; i++){
            res[i] = langs[i].toUpperCase();
        }
        return res;
    }

    static String getPhrase(String key){
        try {
            return lang.getString(key);
        }
        catch(MissingResourceException e){
            return "Missing: " + key;
        }
    }

    static String getFlagForLanguage(int langNum){
        if((langNum < langs.length) && (langNum > -1)) {
            String url = "src/main/resources/flag_icons/" + langs[langNum] + ".gif";
            File file = new File(url);
            if(file.exists()) {
                return url;
            }
            else{
                return null;
            }
        }
        else return null;
    }

    static void setLang(String l){
        for(String pattern : langs){
            if(l.equals(pattern)){
                if(l.equals(defaultLang)){
                    l = "";
                }
                lang = ResourceBundle.getBundle("i18n.language", new Locale(l));
                break;
            }
        }
    }
}
