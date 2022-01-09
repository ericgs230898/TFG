package com.tfg_project.model.beans;

public class Equip {
    final String nomEquip;
    final String linkEquip;

    public Equip(String nomEquip, String linkEquip) {
        this.nomEquip = nomEquip;
        this.linkEquip = linkEquip;
    }

    public String getNomEquip() {
        return nomEquip;
    }

    public String getLinkEquip() {
        return linkEquip;
    }
}
