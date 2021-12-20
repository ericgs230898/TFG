package com.tfg_project.clases;

import java.util.List;

public class AlineacioJugadorPuntuacio {
    private String alineacio;
    private List<JugadorPuntuacio> jugadorPuntuacioList;

    public AlineacioJugadorPuntuacio(String alineacio, List<JugadorPuntuacio> jugadorPuntuacioList) {
        this.alineacio = alineacio;
        this.jugadorPuntuacioList = jugadorPuntuacioList;
    }

    public String getAlineacio() {
        return alineacio;
    }

    public void setAlineacio(String alineacio) {
        this.alineacio = alineacio;
    }

    public List<JugadorPuntuacio> getJugadorPuntuacioList() {
        return jugadorPuntuacioList;
    }

    public void setJugadorPuntuacioList(List<JugadorPuntuacio> jugadorPuntuacioList) {
        this.jugadorPuntuacioList = jugadorPuntuacioList;
    }
}
