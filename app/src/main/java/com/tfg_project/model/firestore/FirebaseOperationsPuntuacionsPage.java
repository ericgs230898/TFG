package com.tfg_project.model.firestore;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseOperationsPuntuacionsPage extends FirebaseOperations {
    private final Map<String, String> mapPuntuacionsJoc;

    public FirebaseOperationsPuntuacionsPage(Context context) {
        super(context);
        mapPuntuacionsJoc = new HashMap<>();
    }

    public Map<String, String> getMapPuntuacionsJoc() {
        return mapPuntuacionsJoc;
    }

    public Task<DocumentSnapshot> getPuntuacionsJoc(){
        return super.getFirebaseFirestore().collection("PuntuacionsJoc").document("PuntuacionsJoc").get().addOnCompleteListener(task -> {
            mapPuntuacionsJoc.put("golMarcat", (String) Objects.requireNonNull(task.getResult()).get("golMarcat"));
            mapPuntuacionsJoc.put("golMarcatPenalti", (String) task.getResult().get("golMarcatPenalti"));
            mapPuntuacionsJoc.put("golMarcatPropia", (String) task.getResult().get("golMarcatPropia"));
            mapPuntuacionsJoc.put("golRebutDefensa", (String) task.getResult().get("golRebutDefensa"));
            mapPuntuacionsJoc.put("golRebutPorter", (String) task.getResult().get("golRebutPorter"));
            mapPuntuacionsJoc.put("minutJugat", (String) task.getResult().get("minutJugat"));
            mapPuntuacionsJoc.put("porteria0Defensa", (String) task.getResult().get("porteria0Defensa"));
            mapPuntuacionsJoc.put("porteria0Porter", (String) task.getResult().get("porteria0Porter"));
            mapPuntuacionsJoc.put("tarjetaAmarilla", (String) task.getResult().get("tarjetaAmarilla"));
            mapPuntuacionsJoc.put("tarjetaRoja", (String) task.getResult().get("tarjetaRoja"));
        });
    }
}
