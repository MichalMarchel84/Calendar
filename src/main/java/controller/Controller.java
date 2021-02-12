package controller;

import view.I18n;

import javax.swing.*;
import java.awt.*;

public class Controller implements RequestListener{

    public Controller() {
    }

    @Override
    public void requestReceived(Request req) {
        if(req instanceof LoginRequest){
            System.out.print("Login: ");
            System.out.println(((LoginRequest) req).login);
            System.out.print("pass: ");
            char[] chars = ((LoginRequest) req).pass;
            for(char c : chars){
                System.out.print(c);
            }
            JLabel l = ((LoginRequest) req).err;
            l.setText(I18n.getPhrase("error_not_implemented"));
            l.setForeground(Color.RED);
            l.repaint();
        }
        else if(req instanceof NewUserRequest){
            System.out.print("New user: ");
            System.out.println(((NewUserRequest) req).login);
            System.out.print("pass: ");
            char[] chars = ((NewUserRequest) req).pass;
            for(char c : chars){
                System.out.print(c);
            }
            JLabel l = ((NewUserRequest) req).err;
            l.setText(I18n.getPhrase("error_not_implemented"));
            l.setForeground(Color.RED);
            l.repaint();
        }
        else{
            System.out.println("Unknown request");
        }
    }
}
