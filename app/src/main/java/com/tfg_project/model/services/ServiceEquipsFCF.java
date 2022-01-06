package com.tfg_project.model.services;

import android.os.NetworkOnMainThreadException;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.model.beans.Equip;
import com.tfg_project.model.beans.Jugador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceEquipsFCF {

    protected static final List<String> competicions = Arrays.asList("quarta-catalana");//, "tercera-catalana", "segona-catalana", "primera-catalana");
    protected static final List<String> grups = Arrays.asList("1", "2", "3","4", "5", "6", "7", "8", "9", "10", "11", "13", "14", "15", "16", "17", "20", "21", "23", "24", "27", "28", "29", "30");

    public void getEquipsParticipants() {
        List<Thread> threads = new ArrayList<>();
        for ( int i=0; i<competicions.size(); i++ ) {
            for (int j = 0; j< grups.size(); j++) {
                String link = "https://www.fcf.cat/classificacio/2022/futbol-11/" + competicions.get(i) + "/grup-" + grups.get(j);
                String link2 = "<td class=\"tl resumida\"><a href=\"https://www.fcf.cat/calendari-equip/2022/futbol-11/";
                int finalI = i;
                int finalJ = j;
                Thread thread = new Thread(() -> {
                    URL url = null;
                    try {
                        try {
                            url = new URL(link);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        BufferedReader in = null;
                        try {
                            if (url != null) {
                                in = new BufferedReader(
                                        new InputStreamReader(url.openStream()));
                            }
                        } catch (IOException | NetworkOnMainThreadException e) {
                            e.printStackTrace();
                        }
                        StringBuilder text = new StringBuilder();
                        if (in != null) {
                            try {
                                String inputLine;
                                while ((inputLine = in.readLine()) != null) {
                                    text.append(inputLine);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        List<Equip> listEquips = new ArrayList<>();
                        int aux = text.toString().indexOf(link2);
                        while (aux != -1 ){
                            listEquips.add(getNomEquip(text.toString(), aux + 33));
                            aux = text.toString().indexOf(link2, aux+33);
                        }
                        if (!listEquips.isEmpty()) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, String> equips = new HashMap<>();
                            for (Equip equip : listEquips) {
                                equips.put(equip.getNomEquip(), equip.getLinkEquip());
                            }
                            for ( Equip equip : listEquips ) {
                                ServiceJugadorsFCF serviceJugadorsFCF = new ServiceJugadorsFCF();
                                List<Jugador> jugadors = serviceJugadorsFCF.getJugadorsEquip(equip);
                                ArrayList<String> jugString = new ArrayList<>();
                                for ( Jugador jugador : jugadors ) {
                                    jugString.add(jugador.getNomJugador());
                                }
                                Map<String, Object> mapEquip = new HashMap<>();
                                mapEquip.put("acta", equip.getLinkEquip());
                                mapEquip.put("jugadors", jugString);

                                DocumentReference documentReference = db.collection("Equip")
                                        .document(competicions.get(finalI))
                                        .collection("Grups")
                                        .document("grup"+grups.get(finalJ))
                                        .collection("Equips").document(equip.getNomEquip());

                                documentReference.set(mapEquip).addOnCompleteListener(task -> {
                                    System.out.println("FINISH");
                                        });

                            }
                            /*db.collection("Equips").document(competicions.get(finalI) + grups.get(finalJ)).set(equips).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("DB", "DocumentSnapshot successfully written!");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w("DB", "Error writing document", e);
                                }
                            });*/
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                thread.start();
                threads.add(thread);
            }
        }
        for (Thread th:threads){
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private Equip getNomEquip(String text, int i) {
        StringBuilder equip = new StringBuilder();
        StringBuilder equipLink = new StringBuilder();
        boolean trobatEquip = false;
        boolean trobatLink = true;
        while ( text.charAt(i) != '<') {
            if (trobatEquip) {
                equip.append(text.charAt(i));
            }
            if (text.charAt(i) == '>') {
                trobatEquip = true;
            }
            if (text.charAt(i) == '"') {
                trobatLink = false;
            }
            if (trobatLink) {
                equipLink.append(text.charAt(i));
            }
            i++;
        }
        equipLink.replace(19,30,"/");
        equipLink.replace(31, 41, "");
        StringBuilder categoria = new StringBuilder();
        for ( int k=31; k<equipLink.length(); k++){
            if ( equipLink.charAt(k) == '/' ) break;
            categoria.append(equipLink.charAt(k));
        }
        String cat = "4cat";
        if (categoria.toString().equals("tercera-catalana")) cat = "3cat";
        else if (categoria.toString().equals("segona-catalana")) cat = "2cat";
        else if (categoria.toString().equals("primera-catalana")) cat = "1cat";
        equipLink.replace(31, 31+categoria.length(), cat);
        for ( int k=36; k<equipLink.length(); k++){
            if ( equipLink.charAt(k) == '/') {
                equipLink.replace(36, k+1, "");
            }
        }
        return new Equip(equip.toString(), equipLink.toString());
    }
}
