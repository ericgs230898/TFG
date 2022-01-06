package com.tfg_project.model.beans;

public class JugadorPuntuacio {
    private String posicio;
    private String nomJugador;
    private String punts;

    public JugadorPuntuacio(String posicio, String nomJugador, String punts) {
        this.posicio = posicio;
        this.nomJugador = nomJugador;
        this.punts = punts;
    }

    public String getPosicio() {
        return posicio;
    }

    public void setPosicio(String posicio) {
        this.posicio = posicio;
    }

    public String getNomJugador() {
        return nomJugador;
    }

    public void setNomJugador(String nomJugador) {
        this.nomJugador = nomJugador;
    }

    public String getPunts() {
        return punts;
    }

    public void setPunts(String punts) {
        this.punts = punts;
    }
}
