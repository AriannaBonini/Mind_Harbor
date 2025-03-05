package com.example.mindharbor.beans;


public class CredenzialiLoginBean {
    private String username;
    private final String password;

    public CredenzialiLoginBean(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }


}
