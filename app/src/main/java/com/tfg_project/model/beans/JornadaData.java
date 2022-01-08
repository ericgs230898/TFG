package com.tfg_project.model.beans;

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

    public String getDataMin() {
        return dataMin;
    }

    public String getDataMax() {
        return dataMax;
    }

}
