package com.tfg_project.model.beans;

public class JugadorPosicio {
    private String nomJugador;
    private String posicio;

    public JugadorPosicio(String nomJugador, String posicio) {
        this.nomJugador = nomJugador;
        this.posicio = posicio;
    }

    public String getNomJugador() {
        return nomJugador;
    }

    public String getPosicio() {
        return posicio;
    }
}
