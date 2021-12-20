package com.tfg_project.clases;

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

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public List<Resultat> getResultats() {
        return resultats;
    }

    public void setResultats(List<Resultat> resultats) {
        this.resultats = resultats;
    }
}
