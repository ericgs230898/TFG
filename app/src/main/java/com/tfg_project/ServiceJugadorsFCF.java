package com.tfg_project;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceJugadorsFCF {
    public void getJugadorsEquip(List<Equip> listEquips, String grupCat) {
        for ( int i=0; i<listEquips.size(); i++ ) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                List<Jugador> jugadors = new ArrayList<>();
                String linkEquip = listEquips.get(finalI).getLinkEquip();
                String nomEquip = listEquips.get(finalI).getNomEquip();
                URL url = null;
                try {
                    try {
                        url = new URL(linkEquip);
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

                    int aux = text.indexOf("<th>Jugadors</th>");
                    int fiPlayers = text.indexOf("</tbody>", aux);
                    String textJugadors = text.substring(aux + 96, fiPlayers);
                    int j = 0;
                    StringBuilder jugador = new StringBuilder();
                    while (j < textJugadors.length()) {
                        jugador.append(textJugadors.charAt(j));
                        j++;
                        if (textJugadors.charAt(j) == (char) 9) {
                            j = j + 66;
                            //System.out.println(jugador.toString());
                            jugadors.add(new Jugador(jugador.toString(), nomEquip));
                            jugador = new StringBuilder();
                        }
                    }
                    if (!jugadors.isEmpty()){
                        Map<String, Object> docData = new HashMap<>();
                        List<String> jugadores = new ArrayList<>();
                        for (Jugador jug : jugadors ) {
                            jugadores.add(jug.getNomJugador());
                        }
                        docData.put("Jugadors", jugadores);
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Jugadors").document(grupCat).collection(nomEquip).document("Jugadors").set(docData).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("DB", "DB-Complete");
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            try {
                thread.join();
                //Log.e("SIZE", String.valueOf(jugadors.size()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //return jugadors;
    }
}
