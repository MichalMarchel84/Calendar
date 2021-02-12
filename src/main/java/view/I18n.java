package view;

import java.io.File;
import java.util.*;

public class I18n {

    private static final String defaultLang = "en";
    private static final String defaultFlag = "src/main/resources/flag_icons/gb.gif";
    private static final Set<LanguageListener> langListeners = new HashSet<>();

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
            if(fl.substring(0, 4).equalsIgnoreCase("lang")) {
                int index = fl.indexOf('_');
                if (index > -1) {
                    list.add(fl.substring(fl.indexOf('_') + 1, fl.indexOf('_') + 3));
                }
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

    public static String getPhrase(String key){
        try {
            return lang.getString(key);
        }
        catch(MissingResourceException e){
            return "Missing: " + key;
        }
    }

    static String getFlagForLanguage(String code){
        String url;
        if(code.equalsIgnoreCase(defaultLang)){
            url = defaultFlag;
        }
        else {
            url = "src/main/resources/flag_icons/" + code + ".gif";
        }
        File file = new File(url);
        if(file.exists()) {
            return url;
        }
        else{
            return null;
        }
    }

    static int getLangIndex(){
        int index = -1;
        if(lang.getLocale().getLanguage().equals("")){
            index = 0;
        }
        else {
            for (int i = 0; i < langs.length; i++) {
                if (langs[i].equalsIgnoreCase(lang.getLocale().getLanguage())) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    static void setLang(String code){
        for(String pattern : langs){
            if(code.equals(pattern)){
                if(code.equals(defaultLang)){
                    code = "";
                }
                lang = ResourceBundle.getBundle("i18n.language", new Locale(code));
                break;
            }
        }
        fireLanguageChangedEvent();
    }

    static void setLang(int index){
        if((index < langs.length) && (index > 0)){
            lang = ResourceBundle.getBundle("i18n.language", new Locale(langs[index]));
        }
        else if(index == 0){
            lang = ResourceBundle.getBundle("i18n.language", new Locale(""));
        }
        fireLanguageChangedEvent();
    }

    static void addLanguageListener(LanguageListener listener){
        langListeners.add(listener);
    }

    static LanguageListener[] getLanguageListeners(){
        return (LanguageListener[]) langListeners.toArray();
    }

    static void removeLanguageListener(LanguageListener listener){
        langListeners.remove(listener);
    }

    static void fireLanguageChangedEvent(){
        for (LanguageListener langListener : langListeners) {
            langListener.languageChanged();
        }
    }
}
