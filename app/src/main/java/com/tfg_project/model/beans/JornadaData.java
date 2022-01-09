package com.tfg_project.model.beans;

public class JornadaData {
    private final String jornada;
    private final String dataMin;
    private final String dataMax;

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
