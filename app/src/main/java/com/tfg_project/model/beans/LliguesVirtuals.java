package com.tfg_project.model.beans;

public class LliguesVirtuals {
    private String nomLligaVirtual;
    private String participants;
    private String competicio;
    private String grup;

    public LliguesVirtuals() {
    }

    public LliguesVirtuals(String nomLligaVirtual, String participants, String competicio, String grup) {
        this.nomLligaVirtual = nomLligaVirtual;
        this.participants = participants;
        this.competicio = competicio;
        this.grup = grup;
    }

    public String getNomLligaVirtual() {
        return nomLligaVirtual;
    }

    public void setNomLligaVirtual(String nomLligaVirtual) {
        this.nomLligaVirtual = nomLligaVirtual;
    }

    public String getParticipants() {
        return participants;
    }

    public void setParticipants(String participants) {
        this.participants = participants;
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
}
