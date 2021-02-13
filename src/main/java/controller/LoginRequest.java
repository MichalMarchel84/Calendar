package controller;

import javax.swing.*;

public class LoginRequest extends Request{
    String login;
    char[] pass;
    JPanel source;

    public LoginRequest(String login, char[] pass, JPanel source) {
        this.login = login;
        this.pass = pass;
        this.source = source;
    }
}
