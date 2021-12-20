package com.tfg_project.clases;

public class ClassificacioBean {
    private String username;
    private String punts;

    public ClassificacioBean(String username, String punts) {
        this.username = username;
        this.punts = punts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPunts() {
        return punts;
    }

    public void setPunts(String punts) {
        this.punts = punts;
    }
}
