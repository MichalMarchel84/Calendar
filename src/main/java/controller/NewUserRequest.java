package controller;

import javax.swing.*;

public class NewUserRequest extends Request{
    String login;
    char[] pass;
    JLabel err;

    public NewUserRequest(String login, char[] pass, JLabel err) {
        this.login = login;
        this.pass = pass;
        this.err = err;
    }
}
