package com.tfg_project.model.firestore;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.tfg_project.model.beans.JornadaData;
import com.tfg_project.model.beans.Jugador;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseOperationsPlantillaPage extends FirebaseOperations {
    private final static String LLIGUES_VIRTUALS = "LliguesVirtuals";
    private final static String JORNADA = "Jornada";
    private List<String> jornades;
    private List<JornadaData> jornadesData;
    private Map<String, Object> map;
    private List<String> equips;
    private List<String> jugadors;
    private List<Jugador> jugadorsEquip;


    public FirebaseOperationsPlantillaPage(Context context) {
        super(context);
        jornades = new ArrayList<>();
        jornadesData = new ArrayList<>();
        equips = new ArrayList<>();
        jugadors = new ArrayList<>();
        jugadorsEquip = new ArrayList<>();
    }

    public List<String> getEquips() {
        return equips;
    }

    public List<Jugador> getJugadorsEquip() {
        return jugadorsEquip;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public List<String> getJornades() {
        return jornades;
    }

    public List<JornadaData> getJornadesData() {
        return jornadesData;
    }

    public Task<QuerySnapshot> getJornadesPossibles(String competicio, String grup){
        return super.getFirebaseFirestore().collection("PartitsJugats").document(competicio)
                .collection("Grups").document(grup).collection("Jornades")
                .get().addOnCompleteListener(task -> {
                    List<JornadaData> jornadaDataAux = new ArrayList<>();
                    for ( DocumentSnapshot documentSnapshot : task.getResult().getDocuments() ) {
                        String jornada = documentSnapshot.getId();
                        jornada = jornada.substring(7);
                        jornadaDataAux.add(new JornadaData("Jornada " + jornada,
                                (String) documentSnapshot.get("dataInici"),
                                (String) documentSnapshot.get("dataFi")));
                    }
                    for ( int i=1; i<=jornadaDataAux.size(); i++ ){
                        String jorn = "Jornada " + i;
                        jornades.add(jorn);
                        for ( JornadaData jornadaDataIt: jornadaDataAux){
                            if ( jornadaDataIt.getJornada().equals(jorn)) {
                                jornadesData.add(jornadaDataIt);
                                break;
                            }
                        }
                    }
                });
    }

    public Task<DocumentSnapshot> getJugadorsDisponibles(String nomLligaVirtual, String jornada){
        return FirebaseFirestore.getInstance().collection(LLIGUES_VIRTUALS).document(nomLligaVirtual)
                .collection(JORNADA).document(jornada)
                .collection(super.getFirebaseAuth().getCurrentUser().getEmail()).document("Plantilla").get().addOnCompleteListener(task ->
            map = task.getResult().getData());
    }

    public Task<QuerySnapshot> getJugadorsCompeticioGrup(String competicio, String grup){
        return super.getFirebaseFirestore().collection("Equip").document(competicio).collection("Grups")
                .document(grup).collection("Equips").get().addOnCompleteListener(task -> {
            for (DocumentSnapshot documentSnapshot : task.getResult().getDocuments() ) {
                equips.add(documentSnapshot.getId());
                ArrayList<String> jugadorsFirestore = (ArrayList<String>) documentSnapshot.get("jugadors");
                if (jugadorsFirestore != null) {
                    for ( String nomJugador : jugadorsFirestore ) {
                        jugadorsEquip.add(new Jugador(nomJugador, documentSnapshot.getId()));
                    }
                }
                jugadors.addAll(jugadorsFirestore);
            }
        });
    }

    public void guardarPlantilla(String nomLligaVirtual, String jornada, String username, Map<String,Object> mapAux, Map<String,Object> mapJugadors){
        FirebaseFirestore.getInstance().collection(LLIGUES_VIRTUALS)
                .document(nomLligaVirtual).collection(JORNADA)
                .document(jornada).set(mapAux);
        FirebaseFirestore.getInstance().collection(LLIGUES_VIRTUALS)
                .document(nomLligaVirtual).collection(JORNADA)
                .document(jornada).collection(username).document("Plantilla").set(mapJugadors).addOnCompleteListener(task -> super.getUtilsProject().makeToast("Plantilla guardada correctament!"));
    }
}
