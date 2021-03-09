package controller;

public class LoginRequest extends Request{
    String login;
    char[] pass;

    public LoginRequest(String login, char[] pass) {
        this.login = login;
        this.pass = pass;
    }
}
