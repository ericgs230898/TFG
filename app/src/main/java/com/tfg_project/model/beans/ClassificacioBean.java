package com.tfg_project.model.beans;

public class ClassificacioBean {
    private final String username;
    private final String punts;

    public ClassificacioBean(String username, String punts) {
        this.username = username;
        this.punts = punts;
    }

    public String getUsername() {
        return username;
    }

    public String getPunts() {
        return punts;
    }
}
