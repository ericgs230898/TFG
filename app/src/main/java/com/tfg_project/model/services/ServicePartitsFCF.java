package com.tfg_project.model.services;

import com.tfg_project.model.beans.Partit;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServicePartitsFCF {

    private final static List<String> competicions = Collections.singletonList("quarta-catalana");
    private final static List<String> jornades = Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
                                                        "11", "12", "13", "14", "15", "16", "17", "18", "19", "20",
                                                        "21", "22", "23", "24", "25", "26", "27", "28", "29", "30",
                                                        "31", "32", "33", "34", "35", "36", "37", "38", "39", "40");

    private static final List<String> grups = Collections.singletonList("1");

    private final String linea = "<tr class=\"linia\">";
    private final String resultatLink = "<div class=\"tc fs-17 white bg-darkgrey p-r\">";

    public ServicePartitsFCF() {
    }


    public void ejecutar(){
        /*for ( int i=0; i<competicions.size(); i++ ) {
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
                            while (indexLinia != -1) {
                                int indexLiniaNext = textString.indexOf(linea, indexLinia + 1);
                                Partit p = null;
                                if (indexLiniaNext != -1){
                                    p = getPartit(textString.substring(indexLinia, indexLiniaNext));
                                }
                                else p = getPartit(textString.substring(indexLinia));
                                final Map<String, Object> map = new HashMap<>();
                                map.put("equipLocal", p.getEquipLocal());
                                map.put("equipVisitante", p.getEquipVisitante());
                                map.put("golesLocal", p.getGolesLocal());
                                map.put("golesVisitante", p.getGolesVisitante());
                                map.put("acta", p.getActa());
                                map.put("condicion", p.getCondicioPartit());
                                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                                firebaseFirestore.collection("Partit").document(competicions.get(finalI))
                                        .collection("grup"+grups.get(finalJ))
                                        .document("jornada"+jornades.get(finalK))
                                        .collection("partit"+String.valueOf(indexLinia)).add(map).addOnCompleteListener(task -> {
                                    if ( task.isSuccessful() ) {
                                        Log.d("TAG", "Task successful");
                                    }
                                });
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
        }*/
    }

    private Partit getPartit(String textString) {
        String string = textString.trim();
        Partit partit = new Partit();
        String equipLocalLink = "<td class=\"p-5 resultats-w-equip tr\">";
        int localIndexInici = string.indexOf(equipLocalLink);
        partit.setEquipLocal(getEquip(string, localIndexInici));
        String equipVisitantLink = "<td class=\"p-5 resultats-w-equip tl\">";
        int visitanteIndexInici = string.indexOf(equipVisitantLink);
        partit.setEquipVisitante(getEquip(string, visitanteIndexInici));
        String actaLink1 = "<td class=\"p-5 resultats-w-resultat tc\">";
        int resultIndex = string.indexOf(actaLink1);
        if ( resultIndex != -1 && visitanteIndexInici != -1 ) {
            String subStringResultat = string.substring(resultIndex, visitanteIndexInici);
            if (subStringResultat.indexOf("R") > 0) {
                partit.setCondicioPartit("R");
                partit.setGolesLocal(-1);
                partit.setGolesVisitante(-1);
                partit.setActa("");
            }
            else if (subStringResultat.indexOf(">D<") > 0) {
                partit.setCondicioPartit("D");
                partit.setGolesLocal(-1);
                partit.setGolesVisitante(-1);
                partit.setActa("");
            } else {
                partit.setCondicioPartit("-");
                String result = getResultat(string);
                int indexGuion = result.indexOf('-');
                String golesLocal = result.substring(0, indexGuion);
                String golesVisitante = result.substring(indexGuion+1);
                partit.setGolesLocal(Integer.parseInt(golesLocal.trim()));
                partit.setGolesVisitante(Integer.parseInt(golesVisitante.trim()));
                int actaIndex = string.indexOf(actaLink1);
                String actaLink = getActa(string, actaIndex);
                partit.setActa(actaLink);
            }
        }
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
        return "0-123";
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
