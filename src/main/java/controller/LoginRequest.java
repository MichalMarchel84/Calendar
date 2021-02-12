package controller;

import javax.swing.*;

public class LoginRequest extends Request{
    String login;
    char[] pass;
    JLabel err;

    public LoginRequest(String login, char[] pass, JLabel err) {
        this.login = login;
        this.pass = pass;
        this.err = err;
    }
}
