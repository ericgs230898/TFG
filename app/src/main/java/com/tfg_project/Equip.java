package com.tfg_project;

public class Equip {
    String nomEquip;
    String linkEquip;

    public Equip(String nomEquip, String linkEquip) {
        this.nomEquip = nomEquip;
        this.linkEquip = linkEquip;
    }

    public String getNomEquip() {
        return nomEquip;
    }

    public void setNomEquip(String nomEquip) {
        this.nomEquip = nomEquip;
    }

    public String getLinkEquip() {
        return linkEquip;
    }

    public void setLinkEquip(String linkEquip) {
        this.linkEquip = linkEquip;
    }
}
