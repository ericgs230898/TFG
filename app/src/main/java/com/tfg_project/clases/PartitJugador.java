package com.tfg_project.clases;

public class PartitJugador {
    private String nomJugador;
    private String equipLocal;
    private String equipVisitant;
    private int golesMarcados;
    private int golesMarcadosPropiaPuerta;
    private int golesMarcadosPenalti;
    private int golesEncajados;
    private boolean tarjetaAmarilla1;
    private boolean tarjetaAmarilla2;
    private boolean tarjetaRoja;
    private boolean portero;
    private boolean porteriaA0;
    private int minutInici;
    private int minutFi;

    public PartitJugador() {

    }

    public PartitJugador(String nomJugador, String equipLocal, String equipVisitant,
                         int golesMarcados, int golesMarcadosPropiaPuerta, int golesMarcadosPenalti,
                         int golesEncajados, boolean tarjetaAmarilla1, boolean tarjetaAmarilla2,
                         boolean tarjetaRoja, boolean portero, boolean porteriaA0, int minutInici,
                         int minutFi) {
        this.nomJugador = nomJugador;
        this.equipLocal = equipLocal;
        this.equipVisitant = equipVisitant;
        this.golesMarcados = golesMarcados;
        this.golesMarcadosPropiaPuerta = golesMarcadosPropiaPuerta;
        this.golesMarcadosPenalti = golesMarcadosPenalti;
        this.golesEncajados = golesEncajados;
        this.tarjetaAmarilla1 = tarjetaAmarilla1;
        this.tarjetaAmarilla2 = tarjetaAmarilla2;
        this.tarjetaRoja = tarjetaRoja;
        this.portero = portero;
        this.porteriaA0 = porteriaA0;
        this.minutInici = minutInici;
        this.minutFi = minutFi;
    }

    public void augmentaGolesEncajados(){
        this.golesEncajados++;
    }
    public void disminuyeGolesEncajados(){
        this.golesEncajados--;
    }

    public int getGolesEncajados() {
        return golesEncajados;
    }

    public void setGolesEncajados(int golesEncajados) {
        this.golesEncajados = golesEncajados;
    }

    public int getMinutInici() {
        return minutInici;
    }

    public void setMinutInici(int minutInici) {
        this.minutInici = minutInici;
    }

    public int getMinutFi() {
        return minutFi;
    }

    public void setMinutFi(int minutFi) {
        this.minutFi = minutFi;
    }

    public String getNomJugador() {
        return nomJugador;
    }

    public void setNomJugador(String nomJugador) {
        this.nomJugador = nomJugador;
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

    public int getGolesMarcados() {
        return golesMarcados;
    }

    public void setGolesMarcados(int golesMarcados) {
        this.golesMarcados = golesMarcados;
    }

    public void augmentaGolesMarcados(){
        this.golesMarcados++;
    }

    public int getGolesMarcadosPropiaPuerta() {
        return golesMarcadosPropiaPuerta;
    }

    public void setGolesMarcadosPropiaPuerta(int golesMarcadosPropiaPuerta) {
        this.golesMarcadosPropiaPuerta = golesMarcadosPropiaPuerta;
    }

    public void augmentaGolesPropiaPuerta(){
        this.golesMarcadosPropiaPuerta++;
    }

    public int getGolesMarcadosPenalti() {
        return golesMarcadosPenalti;
    }

    public void setGolesMarcadosPenalti(int golesMarcadosPenalti) {
        this.golesMarcadosPenalti = golesMarcadosPenalti;
    }

    public void augmentaGolesPenalti(){
        this.golesMarcadosPenalti++;
    }

    public boolean isTarjetaAmarilla1() {
        return tarjetaAmarilla1;
    }

    public void setTarjetaAmarilla1(boolean tarjetaAmarilla1) {
        this.tarjetaAmarilla1 = tarjetaAmarilla1;
    }

    public boolean isTarjetaAmarilla2() {
        return tarjetaAmarilla2;
    }

    public void setTarjetaAmarilla2(boolean tarjetaAmarilla2) {
        this.tarjetaAmarilla2 = tarjetaAmarilla2;
    }

    public boolean isTarjetaRoja() {
        return tarjetaRoja;
    }

    public void setTarjetaRoja(boolean tarjetaRoja) {
        this.tarjetaRoja = tarjetaRoja;
    }

    public boolean isPortero() {
        return portero;
    }

    public void setPortero(boolean portero) {
        this.portero = portero;
    }

    public boolean isPorteriaA0() {
        return porteriaA0;
    }

    public void setPorteriaA0(boolean porteriaA0) {
        this.porteriaA0 = porteriaA0;
    }
}
