package com.tfg_project;

import android.os.NetworkOnMainThreadException;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServiceJugadorsFCF {

    private List<Jugador> jugadors = new ArrayList<>();

    public List<Jugador> getJugadorsEquip(List<Equip> listEquips) {
        for ( int i=0; i<listEquips.size(); i++ ) {
            String linkEquip = listEquips.get(i).getLinkEquip();
            String nomEquip = listEquips.get(i).getNomEquip();
            Thread thread = new Thread(() -> {
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

                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            thread.start();
            try {
                thread.join();
                Log.e("SIZE", String.valueOf(jugadors.size()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return jugadors;
    }

    public List<Jugador> getJugadors() {
        return jugadors;
    }

    public void setJugadors(List<Jugador> jugadors) {
        this.jugadors = jugadors;
    }
}
