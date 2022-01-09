package com.tfg_project.model.beans;

public class Jugador {
    final String nomJugador;
    final String idEquip;

    public Jugador(String nomJugador, String idEquip) {
        this.nomJugador = nomJugador;
        this.idEquip = idEquip;
    }

    public String getNomJugador() {
        return nomJugador;
    }

    public String getIdEquip() {
        return idEquip;
    }
}

