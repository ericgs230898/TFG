package com.tfg_project.services;

import android.util.Log;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.clases.Jugador;
import com.tfg_project.clases.JugadorResultat;
import com.tfg_project.clases.PartitJugador;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ServicePartitsJugadors {
    String linkJornada = "https://www.fcf.cat/acta/2022/futbol-11/quarta-catalana/grup-1/4cat/alt-bergueda-ccr-b/4cat/vilada-ce-a";
    private ArrayList<PartitJugador> partitJugadorsLocal;
    private ArrayList<PartitJugador> partitJugadorsVisitant;

    public ServicePartitsJugadors() {
        partitJugadorsLocal = new ArrayList<>();
        partitJugadorsVisitant = new ArrayList<>();
    }

    public void ejecutar(String link, String grup, String competicio, String jornada){
        ArrayList<String> dates = new ArrayList<>();
        if ( !link.equals("")) {
            Thread thread = new Thread(() -> {
                partitJugadorsLocal = new ArrayList<>();
                partitJugadorsVisitant = new ArrayList<>();
                try {
                    URL oracle = new URL(link);
                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(oracle.openStream()));

                    String inputLine;
                    StringBuilder text = new StringBuilder();
                    while ((inputLine = in.readLine()) != null) {
                        text.append(inputLine);
                    }
                    in.close();
                    String textString = text.toString().trim();

                    int indexEquipLocal = textString.indexOf("<div class=\"acta-equip\">");
                    int indexEquipVisitant = textString.indexOf("<div class=\"acta-equip\">", indexEquipLocal + 1);
                    int indexMarcador = textString.indexOf("<div class=\"acta-marcador\">");
                    int golesLocal = getGolesLocal(indexMarcador, textString);
                    int golesVisitant = getGolesVisitantes(indexMarcador, textString);
                    String nomEquipLocal = getNomEquip(indexEquipLocal, textString);
                    String nomEquipVisitant = getNomEquip(indexEquipVisitant, textString);
                    int titularsLocal = textString.indexOf("Titulars");
                    int titularsLocalFi = textString.indexOf("</tbody>", titularsLocal);
                    String camisetaPorteroLocal = getColorPorteroCamiseta(textString.substring(titularsLocal, titularsLocalFi));
                    getJugadors(textString.substring(titularsLocal, titularsLocalFi), true, true, nomEquipLocal, nomEquipVisitant, camisetaPorteroLocal);
                    int titularsVisitantes = textString.indexOf("Titulars", titularsLocal + 1);
                    int titularsVisitantesFi = textString.indexOf("</tbody>", titularsVisitantes);
                    getJugadors(textString.substring(titularsVisitantes, titularsVisitantesFi), false, true, nomEquipLocal, nomEquipVisitant, camisetaPorteroLocal);

                    int suplentsLocal = textString.indexOf("Suplents");
                    int suplentsLocalFi = textString.indexOf("</tbody>", suplentsLocal);
                    int suplentsVisitantes = textString.indexOf("Suplents", suplentsLocal + 1);
                    int suplentsVisitantesFi = textString.indexOf("</tbody>", suplentsVisitantes);
                    String camisetaPorteroVisitant = getColorPorteroCamiseta(textString.substring(titularsLocal, titularsLocalFi));
                    getJugadors(textString.substring(suplentsLocal, suplentsLocalFi), true, false, nomEquipLocal, nomEquipVisitant, camisetaPorteroVisitant);
                    getJugadors(textString.substring(suplentsVisitantes, suplentsVisitantesFi), false, false, nomEquipLocal, nomEquipVisitant, camisetaPorteroVisitant);

                    int substitucionsLocal = textString.indexOf("Substitucions");
                    int substitucionsLocalFi = textString.indexOf("</tbody>", substitucionsLocal);
                    int substitucionsVisitantes = textString.indexOf("Substitucions", substitucionsLocal + 1);
                    int substitucionsVisitantesFi = textString.indexOf("</tbody>", substitucionsVisitantes);
                    if (substitucionsLocal != -1 && substitucionsLocalFi != -1) {
                        getSubstitucions(textString.substring(substitucionsLocal, substitucionsLocalFi));
                    }
                    if (substitucionsVisitantes != -1 && substitucionsVisitantesFi != -1) {
                        getSubstitucions(textString.substring(substitucionsVisitantes, substitucionsVisitantesFi));
                    }

                    int targetesLocal = textString.indexOf("Targetes");
                    int targetesLocalFi = textString.indexOf("</tbody>", targetesLocal);
                    int targetesVisitantes = textString.indexOf("Targetes", targetesLocal + 1);
                    int targetesVisitantesFi = textString.indexOf("</tbody>", targetesVisitantes);
                    if (targetesLocal != -1)
                        getTargetes(textString.substring(targetesLocal, targetesLocalFi));
                    if (targetesVisitantes != -1)
                        getTargetes(textString.substring(targetesVisitantes, targetesVisitantesFi));

                    int gols = textString.indexOf("Gols");
                    int golsFi = textString.indexOf("</tbody>", gols);
                    getGols(textString.substring(gols, golsFi));

                    if (golesLocal == 0) {
                        for (PartitJugador partitJugador : partitJugadorsVisitant) {
                            partitJugador.setPorteriaA0(true);
                        }
                    }

                    if (golesVisitant == 0) {
                        for (PartitJugador partitJugador : partitJugadorsLocal) {
                            partitJugador.setPorteriaA0(true);
                        }
                    }

                    CollectionReference collectionLocals = FirebaseFirestore.getInstance().collection("PartitsJugats").document(competicio)
                            .collection("Grups").document(grup).collection("Jornades")
                            .document(jornada).collection("Partits").document(nomEquipLocal + "-" + nomEquipVisitant).collection("JugadorsLocals");

                    CollectionReference collectionVisitantes = FirebaseFirestore.getInstance().collection("PartitsJugats").document(competicio)
                            .collection("Grups").document(grup).collection("Jornades")
                            .document(jornada).collection("Partits").document(nomEquipLocal + "-" + nomEquipVisitant).collection("JugadorsVisitant");

                    for (PartitJugador partitJug : partitJugadorsLocal) {
                        puntuaJugador(partitJug, competicio, grup, jornada);
                        Map<String, Object> map = new HashMap<>();
                        map.put("EquipLocal", partitJug.getEquipLocal());
                        map.put("EquipVisitant", partitJug.getEquipVisitant());
                        map.put("GolesMarcados", partitJug.getGolesMarcados());
                        map.put("GolesMarcadosPenalti", partitJug.getGolesMarcadosPenalti());
                        map.put("GolesMarcadosPropiaPuerta", partitJug.getGolesMarcadosPropiaPuerta());
                        map.put("GolesEncajados", partitJug.getGolesEncajados());
                        map.put("TarjetaAmarilla1", partitJug.isTarjetaAmarilla1());
                        map.put("TarjetaAmarilla2", partitJug.isTarjetaAmarilla2());
                        map.put("TarjetaRoja", partitJug.isTarjetaRoja());
                        map.put("PorteriaA0", partitJug.isPorteriaA0());
                        map.put("Portero", partitJug.isPortero());
                        map.put("MinutInici", partitJug.getMinutInici());
                        map.put("MinutFi", partitJug.getMinutFi());
                        collectionLocals.document(partitJug.getNomJugador()).set(map)
                                .addOnCompleteListener(task -> {
                                    Log.d("TAGTAG", "HECHO");
                                    System.out.println("HECHO: " + partitJug.getEquipLocal() + " - " + partitJug.getEquipVisitant());
                                });
                    }

                    for (PartitJugador partitJug : partitJugadorsVisitant) {
                        puntuaJugador(partitJug, competicio, grup, jornada);
                        Map<String, Object> map = new HashMap<>();
                        map.put("EquipLocal", partitJug.getEquipLocal());
                        map.put("EquipVisitant", partitJug.getEquipVisitant());
                        map.put("GolesMarcados", partitJug.getGolesMarcados());
                        map.put("GolesMarcadosPenalti", partitJug.getGolesMarcadosPenalti());
                        map.put("GolesMarcadosPropiaPuerta", partitJug.getGolesMarcadosPropiaPuerta());
                        map.put("GolesEncajados", partitJug.getGolesEncajados());
                        map.put("TarjetaAmarilla1", partitJug.isTarjetaAmarilla1());
                        map.put("TarjetaAmarilla2", partitJug.isTarjetaAmarilla2());
                        map.put("TarjetaRoja", partitJug.isTarjetaRoja());
                        map.put("PorteriaA0", partitJug.isPorteriaA0());
                        map.put("Portero", partitJug.isPortero());
                        map.put("MinutInici", partitJug.getMinutInici());
                        map.put("MinutFi", partitJug.getMinutFi());
                        // map.put("MinutosJugados", FieldValue.delete());

                        collectionVisitantes.document(partitJug.getNomJugador()).set(map)
                                .addOnCompleteListener(task -> {
                                    Log.d("TAGTAG", "HECHO");
                                    System.out.println("HECHO: " + partitJug.getEquipLocal() + " - " + partitJug.getEquipVisitant());
                                });
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("LINK --> " + link);
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //return dataString;
        }
        //return dataString;
    }

    private void puntuaJugador(PartitJugador jugadorResultat, String comp, String grup, String jorn) {
        double punts = 0;
        int minutsJugats = 0;
        if ( jugadorResultat.getMinutInici() != -1 ) {
            if ( jugadorResultat.getMinutFi() == -1 ) {
                minutsJugats = 90 - jugadorResultat.getMinutInici();
            }
            else {
                minutsJugats = jugadorResultat.getMinutFi() - jugadorResultat.getMinutInici();
            }
        }
        punts = punts + minutsJugats*0.01;
        punts = punts + jugadorResultat.getGolesMarcados()*10;
        punts = punts + jugadorResultat.getGolesMarcadosPenalti()*7.5;
        punts = punts - jugadorResultat.getGolesMarcadosPropiaPuerta()*5;
        if ( jugadorResultat.isTarjetaAmarilla1() ) punts = punts - 3;
        if ( jugadorResultat.isTarjetaAmarilla2() ) punts = punts - 3;
        if ( jugadorResultat.isTarjetaRoja() ) punts = punts - 10;
        if ( jugadorResultat.isPortero() ) {
            if (minutsJugats > 0 && jugadorResultat.getGolesEncajados() == 0){
                punts = punts + 10;
            } else {
                punts = punts - jugadorResultat.getGolesEncajados()*1.5;
            }
            Map<String, String> mapPuntuacio = new HashMap<>();
            Map<String,String> mapJornada = new HashMap<>();
            mapJornada.put("jornada", jorn);
            mapPuntuacio.put("POR", String.valueOf(punts));
            FirebaseFirestore.getInstance().collection("Puntuacions").document(comp)
                    .collection("Grups").document(grup)
                    .collection("Jornades").document(jorn).set(mapJornada).addOnCompleteListener(task -> {

                    });
            FirebaseFirestore.getInstance().collection("Puntuacions").document(comp)
                    .collection("Grups").document(grup)
                    .collection("Jornades").document(jorn).collection("Jugadors").document(jugadorResultat.getNomJugador()).set(mapPuntuacio).addOnCompleteListener(task -> System.out.println("PORTER PUNTUAT"));
        } else {
            Map<String, String> mapPuntuacio = new HashMap<>();
            mapPuntuacio.put("DC", String.valueOf(punts));
            mapPuntuacio.put("MC", String.valueOf(punts));
            if (minutsJugats>0 && jugadorResultat.getGolesEncajados() == 0){
                punts = punts + 7.5;
            } else {
                punts = punts - jugadorResultat.getGolesEncajados();
            }
            mapPuntuacio.put("DFC", String.valueOf(punts));
            Map<String,String> mapJornada = new HashMap<>();
            mapJornada.put("jornada", jorn);
            FirebaseFirestore.getInstance().collection("Puntuacions").document(comp)
                    .collection("Grups").document(grup)
                    .collection("Jornades").document(jorn).set(mapJornada).addOnCompleteListener(task -> {

            });
            FirebaseFirestore.getInstance().collection("Puntuacions").document(comp)
                    .collection("Grups").document(grup)
                    .collection("Jornades").document(jorn)
                    .collection("Jugadors").document(jugadorResultat.getNomJugador()).set(mapPuntuacio).addOnCompleteListener(task -> System.out.println("JUGADOR PUNTUAT"));
        }

    }

    private int getGolesLocal(int indexMarcador, String textString) {
        int indexGuion = textString.indexOf("-", indexMarcador+20);
        StringBuilder stringBuilder = new StringBuilder();
        for ( int i=indexGuion-1; i>indexMarcador; i-- ) {
            char c = textString.charAt(i);
            if ( textString.charAt(i) == '>') break;
            else if ( textString.charAt(i) != ' ' && textString.charAt(i) != '\t') stringBuilder.append(textString.charAt(i));
        }
        return Integer.valueOf(stringBuilder.reverse().toString());
    }

    private int getGolesVisitantes (int indexMarcador, String textString ) {
        int indexGuion = textString.indexOf("-", indexMarcador+20);
        StringBuilder stringBuilder = new StringBuilder();
        String aux = textString.substring(indexGuion, indexGuion+30);
        for ( int i=indexGuion+1; i<textString.length(); i++ ) {
            if ( textString.charAt(i) == '<' ) break;
            else if ( textString.charAt(i) != ' ' && textString.charAt(i) != '\t' ) stringBuilder.append(textString.charAt(i));
        }
        return Integer.valueOf(stringBuilder.toString());
    }

    private void getGols(String substring) {
        ArrayList<String> minutsGolLocal = new ArrayList<>();
        ArrayList<String> minutsGolVisitant = new ArrayList<>();
        int indexGol = substring.indexOf("<div class=\"gol\">");
        while ( indexGol != -1 && indexGol != substring.length()) {
            String tipusGol = "";
            int indexNextGol = substring.indexOf("<div class=\"gol\">", indexGol+1);
            if ( indexNextGol == -1 ) indexNextGol = substring.length();
            int golNormal = substring.indexOf("normal", indexGol);
            if ( golNormal < indexNextGol && golNormal > indexGol ) tipusGol = "golNormal";
            else {
                int golPropia = substring.indexOf("propia", indexGol);
                if (golPropia < indexNextGol && golPropia > indexGol)
                    tipusGol = "golPropia";
                else {
                    int golPenal = substring.indexOf("penal", indexGol);
                    if (golPenal < indexNextGol && golPenal > indexGol) tipusGol = "golPenal";
                }
            }
            int indexJugador = substring.indexOf("href=", indexGol);
            boolean trobat = false;
            StringBuilder jugador = new StringBuilder();
            int finalJugador = 0;
            for ( int i=indexJugador; i<substring.length(); i++ ) {
                if ( trobat && substring.charAt(i) != '<' && substring.charAt(i) != '\t') jugador.append(substring.charAt(i));
                if ( substring.charAt(i) == '>' ) trobat = true;
                else if ( substring.charAt(i) == '<' && trobat ) break;
                finalJugador = i;
            }
            int indexGolMinut = substring.indexOf("'", finalJugador);
            StringBuilder minutGol = new StringBuilder();
            for ( int i=indexGolMinut-1; substring.charAt(i)!='>'; i--){
                minutGol.append(substring.charAt(i));
            }
            for ( int i=0; i<partitJugadorsLocal.size(); i++ ) {
                if ( partitJugadorsLocal.get(i).getNomJugador().equals(jugador.toString())){
                    if ( tipusGol.equals("golNormal")) {
                        partitJugadorsLocal.get(i).augmentaGolesMarcados();
                        minutsGolLocal.add(minutGol.reverse().toString());
                    } else if ( tipusGol.equals("golPropia")) {
                        partitJugadorsLocal.get(i).augmentaGolesPropiaPuerta();
                        minutsGolVisitant.add(minutGol.reverse().toString());
                    }
                    else if ( tipusGol.equals("golPenal")) {
                        partitJugadorsLocal.get(i).augmentaGolesPenalti();
                        minutsGolLocal.add(minutGol.reverse().toString());
                    }
                }
            }

            for ( int i=0; i<partitJugadorsVisitant.size(); i++ ) {
                if ( partitJugadorsVisitant.get(i).getNomJugador().equals(jugador.toString())){
                    if ( tipusGol.equals("golNormal")) {
                        partitJugadorsVisitant.get(i).augmentaGolesMarcados();
                        minutsGolVisitant.add(minutGol.reverse().toString());
                    } else if ( tipusGol.equals("golPropia")) {
                        partitJugadorsVisitant.get(i).augmentaGolesPropiaPuerta();
                        minutsGolLocal.add(minutGol.reverse().toString());
                    }
                    else if ( tipusGol.equals("golPenal")) {
                        partitJugadorsVisitant.get(i).augmentaGolesPenalti();
                        minutsGolVisitant.add(minutGol.reverse().toString());
                    }
                }
            }
            indexGol = indexNextGol;
        }
        for ( String minutGol : minutsGolLocal ) {
            int minutGolInteger = Integer.valueOf(minutGol);
            for ( PartitJugador pj : partitJugadorsLocal ) {
                if ( pj.getMinutInici() != -1 ) {
                    if ( pj.getMinutFi() == -1 ) {
                        if ( pj.getMinutInici() <= minutGolInteger && minutGolInteger <= 90) {
                            pj.augmentaGolesEncajados();
                        }
                    }
                    else {
                        if ( pj.getMinutInici() <= minutGolInteger && minutGolInteger <= pj.getMinutFi()){
                            pj.augmentaGolesEncajados();
                        }
                    }
                }
            }
            for ( PartitJugador pj : partitJugadorsVisitant ) {
                if ( pj.getMinutInici() != -1 ) {
                    if ( pj.getMinutFi() == -1 ) {
                        if ( pj.getMinutInici() <= minutGolInteger && minutGolInteger <= 90) {
                            pj.augmentaGolesEncajados();
                        }
                    }
                    else {
                        if ( pj.getMinutInici() <= minutGolInteger && minutGolInteger <= pj.getMinutFi()){
                            pj.augmentaGolesEncajados();
                        }
                    }
                }
            }
        }
    }

    private void getTargetes(String substring) {
        int indexJugador = substring.indexOf("href=");
        while ( indexJugador != -1 ) {
            boolean trobat = false;
            StringBuilder jugador = new StringBuilder();
            for ( int i=indexJugador; i<substring.length(); i++ ) {
                if ( trobat && substring.charAt(i) != '<') jugador.append(substring.charAt(i));
                if ( substring.charAt(i) == '>' ) trobat = true;
                else if ( substring.charAt(i) == '<' && trobat ) break;
            }
            //System.out.println("Jugador: " + jugador.toString());
            String tarjeta = "";
            int indexAmarilla = substring.indexOf("groga", indexJugador);
            int indexRoja = substring.indexOf("vermella", indexJugador);
            if ( indexRoja == -1 ) tarjeta = "amarilla";
            else if ( indexAmarilla == -1 ) tarjeta = "roja";
            else {
                if ( indexAmarilla < indexRoja ) tarjeta = "amarilla";
                else tarjeta = "roja";
            }
            for ( int i=0; i<partitJugadorsLocal.size(); i++ ) {
                if ( partitJugadorsLocal.get(i).getNomJugador().equals(jugador.toString())){
                    if ( tarjeta == "roja" ) {
                        partitJugadorsLocal.get(i).setTarjetaRoja(true);
                    } else {
                        if (partitJugadorsLocal.get(i).isTarjetaAmarilla1()) {
                            partitJugadorsLocal.get(i).setTarjetaAmarilla2(true);
                        } else {
                            partitJugadorsLocal.get(i).setTarjetaAmarilla1(true);
                        }
                    }
                }
            }
            for ( int i=0; i<partitJugadorsVisitant.size(); i++ ) {
                if ( partitJugadorsVisitant.get(i).getNomJugador().equals(jugador.toString())){
                    if ( tarjeta == "roja" ) {
                        partitJugadorsVisitant.get(i).setTarjetaRoja(true);
                    } else {
                        if (partitJugadorsVisitant.get(i).isTarjetaAmarilla1()) {
                            partitJugadorsVisitant.get(i).setTarjetaAmarilla2(true);
                        } else {
                            partitJugadorsVisitant.get(i).setTarjetaAmarilla1(true);
                        }
                    }
                }
            }
            indexJugador = substring.indexOf("href=", indexJugador+1);
        }
    }

    private void getSubstitucions(String substring) {
        int indexMinutSub = substring.indexOf("<td rowspan=\"2\" class=\"fs-30 w-40px p-5\">");
        while ( indexMinutSub != -1 ) {
            StringBuilder minutSub = new StringBuilder();
            for (int i = (indexMinutSub + 41); i < substring.length(); i++) {
                if (substring.charAt(i) == '<') break;
                else minutSub.append(substring.charAt(i));
            }
            int indexJugador1 = substring.indexOf("href=", indexMinutSub);
            boolean trobat1 = false;
            StringBuilder jugador1 = new StringBuilder();
            for ( int i=indexJugador1; i<substring.length(); i++ ) {
                if ( trobat1 && substring.charAt(i) != '<' && substring.charAt(i) != '\t') jugador1.append(substring.charAt(i));
                if ( substring.charAt(i) == '>' ) trobat1 = true;
                else if ( substring.charAt(i) == '<' && trobat1 ) break;
            }
            int indexJugador2 = substring.indexOf("href=", indexJugador1+1);
            boolean trobat2 = false;
            StringBuilder jugador2 = new StringBuilder();
            for ( int i=indexJugador2; i<substring.length(); i++ ) {
                if ( trobat2 && substring.charAt(i) != '<' && substring.charAt(i) != '\t') jugador2.append(substring.charAt(i));
                if ( substring.charAt(i) == '>' ) trobat2 = true;
                else if ( substring.charAt(i) == '<' && trobat2 ) break;
            }
            String minutString = minutSub.toString();
            int minut = Integer.valueOf(minutString.substring(0, minutString.length()-1));
            for ( int i=0; i<partitJugadorsLocal.size(); i++ ) {
                if ( partitJugadorsLocal.get(i).getNomJugador().equals(jugador1.toString())){
                    partitJugadorsLocal.get(i).setMinutFi(minut);
                }
                else if ( partitJugadorsLocal.get(i).getNomJugador().equals(jugador2.toString())){
                    partitJugadorsLocal.get(i).setMinutInici(minut);
                }
            }
            for ( int i=0; i<partitJugadorsVisitant.size(); i++ ) {
                if ( partitJugadorsVisitant.get(i).getNomJugador().equals(jugador1.toString())){
                    partitJugadorsVisitant.get(i).setMinutFi(minut);
                }
                else if ( partitJugadorsVisitant.get(i).getNomJugador().equals(jugador2.toString())){
                    partitJugadorsVisitant.get(i).setMinutInici(minut);
                }
            }
            indexMinutSub = substring.indexOf("<td rowspan=\"2\" class=\"fs-30 w-40px p-5\">", indexMinutSub+1);
        }
    }

    private String getColorPorteroCamiseta(String substring){
        int indexJugador = substring.indexOf("href=");
        StringBuilder camisetaPortero = new StringBuilder();
        int indexCamisetaPortero = substring.indexOf("color:");
        while ( true ) {
            camisetaPortero.append(substring.charAt(indexCamisetaPortero+6));
            if ( substring.charAt(indexCamisetaPortero+6) == ';' ) break;
            indexCamisetaPortero++;
        }
        return camisetaPortero.toString();
    }

    private void getJugadors(String substring, boolean local, boolean titulars, String equipLocal, String equipVisitant, String cPortero) {
        int indexJugador = substring.indexOf("href=");
        int beforeIndexJugador = 0;
        int indexCamisetaPortero = -1;
        while ( indexJugador != -1 ) {
            boolean trobat = false;
            StringBuilder jugador = new StringBuilder();
            for ( int i=indexJugador; i<substring.length(); i++ ) {
                if ( trobat && substring.charAt(i) != '<') jugador.append(substring.charAt(i));
                if ( substring.charAt(i) == '>' ) trobat = true;
                else if ( substring.charAt(i) == '<' && trobat ) break;
            }
            boolean portero = false;
            indexCamisetaPortero = substring.indexOf(cPortero);
            if ( indexCamisetaPortero != -1 && indexCamisetaPortero < indexJugador && indexCamisetaPortero > beforeIndexJugador ) {
                portero = true;
            }
            if ( indexCamisetaPortero > indexJugador ) indexCamisetaPortero = substring.indexOf(cPortero, indexJugador+1);
            if ( local ) {
                if ( titulars ) {
                    partitJugadorsLocal.add(new PartitJugador(jugador.toString(), equipLocal,
                            equipVisitant, 0, 0,
                            0, 0, false, false, false,
                            portero, false, 0, -1));
                } else {
                    partitJugadorsLocal.add(new PartitJugador(jugador.toString(), equipLocal,
                            equipVisitant, 0, 0, 0,
                            0, false, false, false,
                            portero, false, -1, -1));
                }
            } else {
                if ( titulars ) {
                    partitJugadorsVisitant.add(new PartitJugador(jugador.toString(), equipLocal,
                            equipVisitant, 0, 0, 0,
                            0, false, false, false,
                            portero, false, 0, -1));
                } else {
                    partitJugadorsVisitant.add(new PartitJugador(jugador.toString(), equipLocal,
                            equipVisitant, 0, 0, 0,
                            0, false, false, false,
                            portero, false, -1, -1));
                }
            }
            beforeIndexJugador = indexJugador;
            indexJugador = substring.indexOf("href=", indexJugador+1);
        }
    }

    private String getNomEquip(int indexEquip, String textString) {
        StringBuilder result = new StringBuilder();
        int count = 0;
        for ( int i=indexEquip; i<textString.length(); i++ ){
            if ( count == 3 ){
                if ( textString.charAt(i) != '<' ) {
                    result.append(textString.charAt(i));
                } else return result.toString();
            }
            if ( textString.charAt(i) == '>') count++;
        }
        return result.toString();
    }

    public ArrayList<PartitJugador> getPartitJugadorsLocal() {
        return partitJugadorsLocal;
    }

    public void setPartitJugadorsLocal(ArrayList<PartitJugador> partitJugadorsLocal) {
        this.partitJugadorsLocal = partitJugadorsLocal;
    }

    public ArrayList<PartitJugador> getPartitJugadorsVisitant() {
        return partitJugadorsVisitant;
    }

    public void setPartitJugadorsVisitant(ArrayList<PartitJugador> partitJugadorsVisitant) {
        this.partitJugadorsVisitant = partitJugadorsVisitant;
    }
}
