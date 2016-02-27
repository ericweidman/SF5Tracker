package com.theironyard;

import java.util.ArrayList;

/**
 * Created by ericweidman on 2/25/16.
 */
public class User {

    String userName;
    String userPass;
    ArrayList<Stat> stats = new ArrayList<>();

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User(String userName, String userPass) {
        this.userName = userName;
        this.userPass = userPass;


    }
}
