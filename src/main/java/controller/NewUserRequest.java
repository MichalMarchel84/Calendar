package controller;

import javax.swing.*;

public class NewUserRequest extends Request{
    String login;
    char[] pass;
    JPanel source;

    public NewUserRequest(String login, char[] pass, JPanel source) {
        this.login = login;
        this.pass = pass;
        this.source = source;
    }
}
