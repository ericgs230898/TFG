package com.tfg_project.clases;

import java.io.Serializable;

public class Resultat implements Serializable {
    private String equipLocal;
    private String equipVisitant;
    private int golesLocal;
    private int golesVisitant;
    private String condicio;

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

    public void setEquipLocal(String equipLocal) {
        this.equipLocal = equipLocal;
    }

    public String getEquipVisitant() {
        return equipVisitant;
    }

    public void setEquipVisitant(String equipVisitant) {
        this.equipVisitant = equipVisitant;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitant() {
        return golesVisitant;
    }

    public void setGolesVisitant(int golesVisitant) {
        this.golesVisitant = golesVisitant;
    }

    public String getCondicio() {
        return condicio;
    }

    public void setCondicio(String condicio) {
        this.condicio = condicio;
    }
}
