package com.tfg_project.model.services;

import com.tfg_project.model.beans.Partit;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServicePartits {
    private final static List<String> competicions = Arrays.asList("quarta-catalana");
    private final static List<String> jornades = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15",
            "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40");

    private final static List<String> grups = Arrays.asList("20","21","23","24","27","28","29","30");

    private final String linea = "<tr class=\"linia\">";
    private final String resultatLink = "<div class=\"tc fs-17 white bg-darkgrey p-r\">";

    public ServicePartits() {
    }

    private final ArrayList<Partit> partits = new ArrayList<>();

    public ArrayList<Partit> ejecutar(){
        List<Thread> threads = new ArrayList<>();
        for ( int i=0; i<competicions.size(); i++ ) {
            for (int j=0; j<grups.size(); j++) {
                for (int k=0; k<jornades.size(); k++ ) {
                    int finalJ = j;
                    int finalI = i;
                    int finalK = k;
                    Thread thread = new Thread(() -> {
                        String linkJornada = "https://www.fcf.cat/resultats/2022/futbol-11/" + competicions.get(finalI) + "/grup-" + grups.get(finalJ) + "/jornada-" + jornades.get(finalK);
                        System.out.println(linkJornada);
                        try {
                            URL oracle = new URL(linkJornada);
                            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(oracle.openStream()));

                            String inputLine;
                            StringBuilder text = new StringBuilder();
                            while ((inputLine = in.readLine()) != null) {
                                text.append(inputLine);
                            }
                            in.close();
                            String textString = text.toString().trim();
                            int indexLinia = textString.indexOf(linea);
                            int count = 0;
                            while (indexLinia != -1) {
                                count++;
                                int indexLiniaNext = textString.indexOf(linea, indexLinia + 1);
                                Partit p = null;
                                if (indexLiniaNext != -1){
                                    p = getPartit(textString.substring(indexLinia, indexLiniaNext), competicions.get(finalI), grups.get(finalJ), jornades.get(finalK));
                                }
                                else p = getPartit(textString.substring(indexLinia), competicions.get(finalI), grups.get(finalJ), jornades.get(finalK));
                                if ( p != null) partits.add(p);
                                indexLinia = indexLiniaNext;
                            }
                            //System.out.println("SIZZEEEE ---- " + String.valueOf(partits.size()));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return partits;
    }

    private Partit getPartit(String textString, String competicio, String grup, String jornada) {
        String string = textString.trim();
        Partit partit = new Partit();
        partit.setCompeticio(competicio);
        partit.setGrup("grup"+grup);
        partit.setJornada("jornada"+jornada);
        String equipLocalLink = "<td class=\"p-5 resultats-w-equip tr\">";
        int localIndexInici = string.indexOf(equipLocalLink);
        if ( localIndexInici != -1 ) partit.setEquipLocal(getEquip(string, localIndexInici));
        else partit.setEquipLocal("");
        String equipVisitantLink = "<td class=\"p-5 resultats-w-equip tl\">";
        int visitanteIndexInici = string.indexOf(equipVisitantLink);
        if ( visitanteIndexInici != -1 ) partit.setEquipVisitante(getEquip(string, visitanteIndexInici));
        else partit.setEquipVisitante("");
        String actaLink1 = "<td class=\"p-5 resultats-w-resultat tc\">";
        int resultIndex = string.indexOf(actaLink1);
        String subStringResultat = string.substring(resultIndex, visitanteIndexInici);
        if (textString.contains("ACTA TANCADA")) {
            if (resultIndex != -1 && visitanteIndexInici != -1) {
                partit.setCondicioPartit("-");
                String result = getResultat(string);
                if (result == "") return null;
                int indexGuion = result.indexOf('-');
                String golesLocal = result.substring(0, indexGuion);
                String golesVisitante = result.substring(indexGuion + 1);
                partit.setGolesLocal(Integer.valueOf(golesLocal.trim()));
                partit.setGolesVisitante(Integer.valueOf(golesVisitante.trim()));
                int actaIndex = string.indexOf(actaLink1);
                String actaLink = getActa(string, actaIndex);
                partit.setActa(actaLink);
            }
            return partit;
        }
        if (subStringResultat.indexOf("R") > 0) {
            partit.setCondicioPartit("R");
        } else if (subStringResultat.indexOf("Descansa") > 0) {
            partit.setCondicioPartit("D");
        } else partit.setCondicioPartit("NJ");
        partit.setGolesLocal(-1);
        partit.setGolesVisitante(-1);
        partit.setActa("");
        return partit;
    }

    private String getActa(String textString, int indexActa) {
        StringBuilder acta = new StringBuilder();
        int count = 0;
        for ( int i=indexActa; i<textString.length(); i++){
            if (textString.charAt(i) == '"' ) count++;
            else if ( count == 3 ) acta.append(textString.charAt(i));
            else if ( count == 4 ) break;
        }
        return acta.toString();
    }

    private String getResultat(String textString) {
        StringBuilder resultat = new StringBuilder();
        String resultString = textString.trim();
        int indexResultat = resultString.indexOf("<div class=\"tc fs-17 white bg-darkgrey p-r\">");
        int count = 0;
        if ( indexResultat != -1 ) {
            for (int i = indexResultat; i < resultString.length(); i++) {
                if (count == 1) {
                    if (resultString.charAt(i) == '<') break;
                    else resultat.append(resultString.charAt(i));
                }
                if (resultString.charAt(i) == '>') count++;
            }
            return resultat.toString();
        }
        return "";
    }

    private String getEquip(String textString, int indexEquip) {
        StringBuilder equip = new StringBuilder();
        int count = 0;
        for ( int i=indexEquip; i<textString.length(); i++ ) {
            if ( count == 2 ) {
                if ( textString.charAt(i) == '<') break;
                else equip.append(textString.charAt(i));
            }
            if (textString.charAt(i) == '>' ) count++;
        }
        if ( equip.toString().contains("\t\t")) return "";
        else return equip.toString();
    }
}
