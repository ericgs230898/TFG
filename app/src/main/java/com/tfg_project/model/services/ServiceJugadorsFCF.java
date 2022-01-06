package com.tfg_project.model.services;

import android.os.NetworkOnMainThreadException;

import com.tfg_project.model.beans.Equip;
import com.tfg_project.model.beans.Jugador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class ServiceJugadorsFCF {

    private List<Jugador> jugadors;

    public List<Jugador> getJugadorsEquip(Equip equip) {

        Thread thread = new Thread(() -> {
            jugadors = new ArrayList<>();
            String linkEquip = equip.getLinkEquip();
            String nomEquip = equip.getNomEquip();
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
            //Log.e("SIZE", String.valueOf(jugadors.size()));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            return jugadors;
        }
        //return jugadors;
    }
}
