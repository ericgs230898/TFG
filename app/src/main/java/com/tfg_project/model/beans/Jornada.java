package com.tfg_project.model.beans;

import java.io.Serializable;
import java.util.List;

public class Jornada implements Serializable {
    private String jornada;
    private List<Resultat> resultats;

    public Jornada(String jornada, List<Resultat> resultats) {
        this.jornada = jornada;
        this.resultats = resultats;
    }

    public String getJornada() {
        return jornada;
    }

    public List<Resultat> getResultats() {
        return resultats;
    }
}
