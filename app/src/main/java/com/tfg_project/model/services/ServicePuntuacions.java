package com.tfg_project.model.services;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ServicePuntuacions {

    public ServicePuntuacions() {
    }

    public void puntuarJornades(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        // lligues virtuals
        firebaseFirestore.collection("LliguesVirtuals").get().addOnCompleteListener(task -> {
            // lligues virtuals -> lliga virtual
            makeLog("LliguesVirtualsCompleted");
            for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult()).getDocuments()){
                // usuaris de la lliga virtual
                ArrayList<String> usuaris = (ArrayList<String>) documentSnapshot.get("usuaris");
                // competicio de la lliga virtual
                String competicio = getCompeticio((String) documentSnapshot.get("competicio"));
                // grup de la lliga virtual
                String grup = getGrup((String)documentSnapshot.get("grup"));
                DocumentReference documentReference = documentSnapshot.getReference();
                documentReference.collection("Jornada").get().addOnCompleteListener(task1 -> {
                    makeLog("LligaVirtualJornadaSuccess");
                    // por cada jornada de la liga virtual
                    for ( DocumentSnapshot documentSnapshot1 : Objects.requireNonNull(task1.getResult()).getDocuments()){
                        // jornada liga virtual
                        String jornada = documentSnapshot1.getId();
                        DocumentReference documentReference1 = documentSnapshot1.getReference();
                        // por cada usuari de la jornada de la liga virtual
                        for ( String usuari : Objects.requireNonNull(usuaris)) {
                            // por cada plantilla del usuari
                            documentReference1.collection(usuari).document("Plantilla").get().addOnCompleteListener(task22 -> {
                                System.out.println(Objects.requireNonNull(task22.getResult()).getId());
                                makeLog("LligaVirtualJornadaPlantillaSucces");
                                boolean isPuntuat = false;
                                try {
                                    isPuntuat = (boolean) task22.getResult().get("puntuat");
                                } catch ( Exception e ) {

                                }
                                if ( !isPuntuat ) {
                                    // jugadores de la plantilla
                                    Map<String, Object> map = task22.getResult().getData();
                                    // por cada jugador
                                    if ( map != null ) {
                                        int sizeMap = map.size();
                                        final int[] count = {0};
                                        for (Map.Entry<String, Object> entry : map.entrySet()) {
                                            if (!entry.getKey().equals("alineacio") && !entry.getKey().equals("puntuat") && !entry.getKey().equals("puntuacio") && !entry.getKey().equals("puntuat2")) {
                                                String jugador = entry.getKey();
                                                String posicio = (String) entry.getValue();
                                                // consultamos tu puntuacion
                                                firebaseFirestore.collection("Puntuacions")
                                                        .document(competicio).collection("Grups")
                                                        .document(grup).collection("Jornades")
                                                        .document(convertJornada(jornada)).collection("Jugadors").document(jugador).get().addOnCompleteListener(task2 -> {
                                                    makeLog("PuntuacionsJugadorsSuccess");
                                                    String punts = (String) Objects.requireNonNull(task2.getResult()).get(posicio);
                                                    if (!"".equals(punts) && punts != null) {
                                                        Map<String, String> mapAux2 = new HashMap<>();
                                                        mapAux2.put("punts", punts);
                                                        mapAux2.put("posicio", posicio);
                                                        documentReference1.collection(usuari).document("Plantilla").collection("Jugadors").document(jugador).set(mapAux2).addOnCompleteListener(task3 -> {
                                                            makeLog("SETPUNTUACIOJUGADOR--> " + jugador);
                                                            count[0]++;
                                                            if (count[0] == sizeMap) {
                                                                documentReference1.collection(usuari).document("Plantilla").update("puntuat", true);
                                                            }
                                                        });
                                                    }
                                                });
                                            } else {
                                                count[0]++;
                                                if (count[0] == sizeMap) {
                                                    documentReference1.collection(usuari).document("Plantilla").update("puntuat", true);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                });
            }
        });
    }

    public void puntuacionsTotals(){
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
        // lligues virtuals
        firebaseFirestore.collection("LliguesVirtuals").get().addOnCompleteListener(task -> {
            // lligues virtuals -> lliga virtual
            makeLog("LliguesVirtualsCompleted");
            for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult()).getDocuments()){
                // usuaris de la lliga virtual
                ArrayList<String> usuaris = (ArrayList<String>) documentSnapshot.get("usuaris");
                // competicio de la lliga virtual
                String competicio = getCompeticio((String) documentSnapshot.get("competicio"));
                // grup de la lliga virtual
                String grup = getGrup((String)documentSnapshot.get("grup"));
                DocumentReference documentReference = documentSnapshot.getReference();
                documentReference.collection("Jornada").get().addOnCompleteListener(task1 -> {
                    makeLog("LligaVirtualJornadaSuccess");
                    // por cada jornada de la liga virtual
                    for ( DocumentSnapshot documentSnapshot1 : Objects.requireNonNull(task1.getResult()).getDocuments()){
                        // jornada liga virtual
                        String jornada = documentSnapshot1.getId();
                        DocumentReference documentReference1 = documentSnapshot1.getReference();
                        // por cada usuari de la jornada de la liga virtual
                        for ( String usuari : Objects.requireNonNull(usuaris)) {
                            // por cada plantilla del usuari
                            documentReference1.collection(usuari).document("Plantilla").get().addOnCompleteListener(task22 -> {
                                try {
                                    if (!((boolean) Objects.requireNonNull(task22.getResult()).get("puntuat2"))) {
                                        documentReference1.collection(usuari).document("Plantilla").collection("Jugadors").get().addOnCompleteListener(task2 -> {
                                            List<String> punts = new ArrayList<>();
                                            for (DocumentSnapshot documentSnapshot2 : Objects.requireNonNull(task2.getResult()).getDocuments()) {
                                                String punt = (String) documentSnapshot2.get("punts");
                                                if (punt != null && !"".equals(punt)) {
                                                    punts.add(punt);
                                                }
                                            }
                                            String puntuacio = getPuntuacio(punts);
                                            documentReference1.collection(usuari).document("Plantilla").update("puntuacio", puntuacio).addOnCompleteListener(task3 -> {

                                            });
                                            documentSnapshot.getReference().collection("Classificacio").document(usuari).get().addOnCompleteListener(task32 -> {
                                                String puntsActuals = (String) Objects.requireNonNull(task32.getResult()).get("punts");
                                                double dPunts = Double.parseDouble(Objects.requireNonNull(puntsActuals));
                                                double dPuntsJornada = Double.parseDouble(puntuacio.replace(',', '.'));
                                                double suma = dPunts + dPuntsJornada;
                                                String sumaString = String.valueOf(suma);
                                                task32.getResult().getReference().update("punts", sumaString);
                                                documentReference1.collection(usuari).document("Plantilla").update("puntuat2", true).addOnCompleteListener(task33 -> {

                                                });
                                            });
                                        });
                                    }
                                } catch ( Exception e ){

                                }
                            });

                        }
                    }
                });
            }
        });
    }

    private String getPuntuacio(List<String> punts) {
        double d = 0;
        for ( String punt : punts){
            d = d + Double.parseDouble(punt);
        }
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        return df.format(d);
    }

    private void makeLog(String mssg) {
        System.out.println(mssg);
        Log.d("ServicePuntuacions", mssg);
    }

    private String convertJornada(String jornada){
        if ( jornada != null ) return jornada.toLowerCase().replaceAll("\\s", "").trim().replaceAll(" ","");
        return "";
    }

    private String getCompeticio(String competicio) {
        if ( competicio != null ) return competicio.toLowerCase().replaceAll("\\s", "-");
        return "";
    }

    private String getGrup(String grup){
        return "grup" + grup;
    }
}
