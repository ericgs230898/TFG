package com.tfg_project.model.firestore;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.tfg_project.model.beans.PartitJugador;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FirebaseOperationsResultatsPage extends FirebaseOperations {
    private final List<PartitJugador> jugadorsLocal;
    private final List<PartitJugador> jugadorsVisitant;
    public FirebaseOperationsResultatsPage(Context context) {
        super(context);
        jugadorsLocal = new ArrayList<>();
        jugadorsVisitant = new ArrayList<>();
    }

    public List<PartitJugador> getJugadorsLocal() {
        return jugadorsLocal;
    }

    public List<PartitJugador> getJugadorsVisitant() {
        return jugadorsVisitant;
    }

    public Task<QuerySnapshot> jugadoresLocales (String competicio, String grup, String finalJornadaSpinner, String equipLocal, String equipVisitant){
        DocumentReference doc = super.getFirebaseFirestore()
                .collection("PartitsJugats")
                .document(competicio).collection("Grups")
                .document("grup" + grup)
                .collection("Jornades")
                .document(finalJornadaSpinner)
                .collection("Partits")
                .document(equipLocal + "-" + equipVisitant);
        return doc.collection("JugadorsLocals").get().addOnCompleteListener(task -> {
            for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult()).getDocuments()) {
                String nomJugador = documentSnapshot.getId();
                String docEquipLocal = (String) documentSnapshot.get("EquipLocal");
                String docEquipVisitant = (String) documentSnapshot.get("EquipVisitant");
                int golesMarcados = ((Long) Objects.requireNonNull(documentSnapshot.get("GolesMarcados"))).intValue();
                int golesMarcadosPenalti = ((Long) Objects.requireNonNull(documentSnapshot.get("GolesMarcadosPenalti"))).intValue();
                int golesMarcadosPropiaPuerta = ((Long) Objects.requireNonNull(documentSnapshot.get("GolesMarcadosPropiaPuerta"))).intValue();
                int golesEncajados = 0;
                try {
                    golesEncajados = ((Long) Objects.requireNonNull(documentSnapshot.get("GolesEncajados"))).intValue();
                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                }
                int minutInici = ((Long) Objects.requireNonNull(documentSnapshot.get("MinutInici"))).intValue();
                int minutFinal = ((Long) Objects.requireNonNull(documentSnapshot.get("MinutFi"))).intValue();
                boolean porteriaA0 = (Boolean) documentSnapshot.get("PorteriaA0");
                boolean portero = (Boolean) documentSnapshot.get("Portero");
                boolean amarilla1 = (Boolean) documentSnapshot.get("TarjetaAmarilla1");
                boolean amarilla2 = (Boolean) documentSnapshot.get("TarjetaAmarilla2");
                boolean roja = (Boolean) documentSnapshot.get("TarjetaRoja");
                jugadorsLocal.add(new PartitJugador(nomJugador, docEquipLocal, docEquipVisitant,
                        golesMarcados, golesMarcadosPropiaPuerta, golesMarcadosPenalti, golesEncajados,
                        amarilla1, amarilla2, roja, portero, porteriaA0, minutInici, minutFinal));
            }
        });
    }

    public Task<QuerySnapshot> jugadoresVisitantes (String competicio, String grup, String finalJornadaSpinner, String equipLocal, String equipVisitant){
        DocumentReference doc = super.getFirebaseFirestore()
                .collection("PartitsJugats")
                .document(competicio).collection("Grups")
                .document("grup" + grup)
                .collection("Jornades")
                .document(finalJornadaSpinner)
                .collection("Partits")
                .document(equipLocal + "-" + equipVisitant);
        return doc.collection("JugadorsVisitant").get().addOnCompleteListener(task -> {
            for (DocumentSnapshot documentSnapshot : Objects.requireNonNull(task.getResult()).getDocuments()) {
                String nomJugador = documentSnapshot.getId();
                String docEquipLocal = (String) documentSnapshot.get("EquipLocal");
                String docEquipVisitant = (String) documentSnapshot.get("EquipVisitant");
                int golesMarcados = ((Long) Objects.requireNonNull(documentSnapshot.get("GolesMarcados"))).intValue();
                int golesMarcadosPenalti = ((Long) Objects.requireNonNull(documentSnapshot.get("GolesMarcadosPenalti"))).intValue();
                int golesMarcadosPropiaPuerta = ((Long) Objects.requireNonNull(documentSnapshot.get("GolesMarcadosPropiaPuerta"))).intValue();
                int golesEncajados = 0;
                try {
                    golesEncajados = ((Long) Objects.requireNonNull(documentSnapshot.get("GolesEncajados"))).intValue();
                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                }
                int minutInici = ((Long) Objects.requireNonNull(documentSnapshot.get("MinutInici"))).intValue();
                int minutFinal = ((Long) Objects.requireNonNull(documentSnapshot.get("MinutFi"))).intValue();
                boolean porteriaA0 = (Boolean) documentSnapshot.get("PorteriaA0");
                boolean portero = (Boolean) documentSnapshot.get("Portero");
                boolean amarilla1 = (Boolean) documentSnapshot.get("TarjetaAmarilla1");
                boolean amarilla2 = (Boolean) documentSnapshot.get("TarjetaAmarilla2");
                boolean roja = (Boolean) documentSnapshot.get("TarjetaRoja");
                jugadorsVisitant.add(new PartitJugador(nomJugador, docEquipLocal, docEquipVisitant,
                        golesMarcados, golesMarcadosPropiaPuerta, golesMarcadosPenalti, golesEncajados,
                        amarilla1, amarilla2, roja, portero, porteriaA0, minutInici, minutFinal));
            }
        });
    }
}
