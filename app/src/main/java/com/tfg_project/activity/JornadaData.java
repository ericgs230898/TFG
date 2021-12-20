package com.tfg_project.activity;

public class JornadaData {
    private String jornada;
    private String dataMin;
    private String dataMax;

    public JornadaData(String jornada, String dataMin, String dataMax) {
        this.jornada = jornada;
        this.dataMin = dataMin;
        this.dataMax = dataMax;
    }

    public String getJornada() {
        return jornada;
    }

    public void setJornada(String jornada) {
        this.jornada = jornada;
    }

    public String getDataMin() {
        return dataMin;
    }

    public void setDataMin(String dataMin) {
        this.dataMin = dataMin;
    }

    public String getDataMax() {
        return dataMax;
    }

    public void setDataMax(String dataMax) {
        this.dataMax = dataMax;
    }
}
