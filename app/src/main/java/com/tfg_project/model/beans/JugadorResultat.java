package com.tfg_project.model.beans;

public class JugadorResultat {
    private String nom;
    private int minutsJugats;
    private boolean tarjetaAmarilla1;
    private boolean tarjetaAmarilla2;
    private boolean tarjetaRoja1;
    private int golesMarcados;
    private int golesMarcadosPenalti;
    private int golesMarcadosPropia;
    private boolean portero;
    private int golesEncajados;

    public JugadorResultat(String nom, int minutsJugats, boolean tarjetaAmarilla1, boolean tarjetaAmarilla2, boolean tarjetaRoja1,
                           int golesMarcados, int golesMarcadosPenalti, int golesMarcadosPropia, boolean portero, int golesEncajados) {
        this.nom = nom;
        this.minutsJugats = minutsJugats;
        this.tarjetaAmarilla1 = tarjetaAmarilla1;
        this.tarjetaAmarilla2 = tarjetaAmarilla2;
        this.tarjetaRoja1 = tarjetaRoja1;
        this.golesMarcados = golesMarcados;
        this.golesMarcadosPenalti = golesMarcadosPenalti;
        this.golesMarcadosPropia = golesMarcadosPropia;
        this.portero = portero;
        this.golesEncajados = golesEncajados;
    }

    public int getGolesEncajados() {
        return golesEncajados;
    }

    public boolean isPortero() {
        return portero;
    }

    public String getNom() {
        return nom;
    }

    public int getMinutsJugats() {
        return minutsJugats;
    }

    public boolean isTarjetaAmarilla1() {
        return tarjetaAmarilla1;
    }

    public boolean isTarjetaAmarilla2() {
        return tarjetaAmarilla2;
    }

    public boolean isTarjetaRoja1() {
        return tarjetaRoja1;
    }

    public int getGolesMarcados() {
        return golesMarcados;
    }

    public int getGolesMarcadosPenalti() {
        return golesMarcadosPenalti;
    }

    public int getGolesMarcadosPropia() {
        return golesMarcadosPropia;
    }

}
