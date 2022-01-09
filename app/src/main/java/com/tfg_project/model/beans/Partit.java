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

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public void setCompeticio(String competicio) {
        this.competicio = competicio;
    }

    public void setGrup(String grup) {
        this.grup = grup;
    }

    public void setCondicioPartit(String condicioPartit) {
        this.condicioPartit = condicioPartit;
    }

    public void setEquipLocal(String equipLocal) {
        this.equipLocal = equipLocal;
    }

    public void setEquipVisitante(String equipVisitante) {
        this.equipVisitante = equipVisitante;
    }

    public void setGolesLocal(int golesLocal) {
        this.golesLocal = golesLocal;
    }

    public void setGolesVisitante(int golesVisitante) {
        this.golesVisitante = golesVisitante;
    }

    public void setActa(String acta) {
        this.acta = acta;
    }
}
