package com.tfg_project.model.beans;

import java.util.List;

public class AlineacioJugadorPuntuacio {
    private final String alineacio;
    private final List<JugadorPuntuacio> jugadorPuntuacioList;

    public AlineacioJugadorPuntuacio(String alineacio, List<JugadorPuntuacio> jugadorPuntuacioList) {
        this.alineacio = alineacio;
        this.jugadorPuntuacioList = jugadorPuntuacioList;
    }

    public String getAlineacio() {
        return alineacio;
    }

    public List<JugadorPuntuacio> getJugadorPuntuacioList() {
        return jugadorPuntuacioList;
    }
}
