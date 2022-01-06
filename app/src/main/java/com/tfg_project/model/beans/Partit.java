package com.tfg_project.model.beans;

public class Partit {
    String equipLocal;
    String equipVisitante;
    int golesLocal;
    int golesVisitante;
    String acta;
    String condicioPartit;
    String competicio;
    String grup;
    String jornada;

    public Partit(){

    }

    public Partit(String equipLocal, String equipVisitante, int golesLocal, int golesVisitante, String acta, String condicioPartit, String competicio, String grup, String jornada) {
        this.equipLocal = equipLocal;
        this.equipVisitante = equipVisitante;
        this.golesLocal = golesLocal;
        this.golesVisitante = golesVisitante;
        this.acta = acta;
        this.condicioPartit = condicioPartit;
        this.competicio = competicio;
        this.grup = grup;
        this.jornada = jornada;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public String getCompeticio() {
        return competicio;
    }

    public void setCompeticio(String competicio) {
        this.competicio = competicio;
    }

    public String getGrup() {
        return grup;
    }

    public void setGrup(String grup) {
        this.grup = grup;
    }

    public String getCondicioPartit() {
        return condicioPartit;
    }

    public void setCondicioPartit(String condicioPartit) {
        this.condicioPartit = condicioPartit;
    }

    public String getEquipLocal() {
        return equipLocal;
    }

    public void setEquipLocal(String equipLocal) {
        this.equipLocal = equipLocal;
    }

    public String getEquipVisitante() {
        return equipVisitante;
    }

    public void setEquipVisitante(String equipVisitante) {
        this.equipVisitante = equipVisitante;
    }

    public int getGolesLocal() {
        return golesLocal;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public int getGolesVisitante() {
        return golesVisitante;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public String getActa() {
        return acta;
    }

    public void setActa(String acta) {
        this.acta = acta;
    }
}
