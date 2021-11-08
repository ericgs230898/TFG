package com.tfg_project;

public class Jugador {
    String nomJugador;
    String idEquip;

    public Jugador(String nomJugador, String idEquip) {
        this.nomJugador = nomJugador;
        this.idEquip = idEquip;
    }

    public String getNomJugador() {
        return nomJugador;
    }

    public void setNomJugador(String nomJugador) {
        this.nomJugador = nomJugador;
    }

    public String getIdEquip() {
        return idEquip;
    }

    public void setIdEquip(String idEquip) {
        this.idEquip = idEquip;
    }
}
