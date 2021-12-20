package com.tfg_project.clases;

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

    public void setNomJugador(String nomJugador) {
        this.nomJugador = nomJugador;
    }

    public String getPosicio() {
        return posicio;
    }

    public void setPosicio(String posicio) {
        this.posicio = posicio;
    }
}
