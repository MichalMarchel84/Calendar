package controller;

import javax.swing.*;

public class NewUserRequest extends Request{
    String login;
    char[] pass;

    public NewUserRequest(String login, char[] pass) {
        this.login = login;
        this.pass = pass;
    }
}
