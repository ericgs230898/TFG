package com.tfg_project.model.beans;

import java.io.Serializable;

public class Resultat implements Serializable {
    private final String equipLocal;
    private final String equipVisitant;
    private final int golesLocal;
    private final int golesVisitant;
    private final String condicio;

    public Resultat(String equipLocal, String equipVisitant, int golesLocal, int golesVisitant, String condicio) {
        this.equipLocal = equipLocal;
        this.equipVisitant = equipVisitant;
        this.golesLocal = golesLocal;
        this.golesVisitant = golesVisitant;
        this.condicio = condicio;
    }

    public String getEquipLocal() {
        return equipLocal;
    }

    public String getEquipVisitant() {
        return equipVisitant;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public int getGolesVisitant() {
        return golesVisitant;
    }

    public String getCondicio() {
        return condicio;
    }
}
