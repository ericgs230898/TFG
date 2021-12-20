package com.tfg_project.clases;

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

    public void setGolesEncajados(int golesEncajados) {
        this.golesEncajados = golesEncajados;
    }

    public boolean isPortero() {
        return portero;
    }

    public void setPortero(boolean portero) {
        this.portero = portero;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public int getMinutsJugats() {
        return minutsJugats;
    }

    public void setMinutsJugats(int minutsJugats) {
        this.minutsJugats = minutsJugats;
    }

    public boolean isTarjetaAmarilla1() {
        return tarjetaAmarilla1;
    }

    public void setTarjetaAmarilla1(boolean tarjetaAmarilla1) {
        this.tarjetaAmarilla1 = tarjetaAmarilla1;
    }

    public boolean isTarjetaAmarilla2() {
        return tarjetaAmarilla2;
    }

    public void setTarjetaAmarilla2(boolean tarjetaAmarilla2) {
        this.tarjetaAmarilla2 = tarjetaAmarilla2;
    }

    public boolean isTarjetaRoja1() {
        return tarjetaRoja1;
    }

    public void setTarjetaRoja1(boolean tarjetaRoja1) {
        this.tarjetaRoja1 = tarjetaRoja1;
    }

    public int getGolesMarcados() {
        return golesMarcados;
    }

    public void setGolesMarcados(int golesMarcados) {
        this.golesMarcados = golesMarcados;
    }

    public int getGolesMarcadosPenalti() {
        return golesMarcadosPenalti;
    }

    public void setGolesMarcadosPenalti(int golesMarcadosPenalti) {
        this.golesMarcadosPenalti = golesMarcadosPenalti;
    }

    public int getGolesMarcadosPropia() {
        return golesMarcadosPropia;
    }

    public void setGolesMarcadosPropia(int golesMarcadosPropia) {
        this.golesMarcadosPropia = golesMarcadosPropia;
    }
}
