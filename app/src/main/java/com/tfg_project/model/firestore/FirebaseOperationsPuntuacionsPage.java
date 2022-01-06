package com.tfg_project.model.firestore;

import android.content.Context;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.tfg_project.model.beans.AlineacioJugadorPuntuacio;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseOperationsPuntuacionsPage extends FirebaseOperations {
    private Map<String, String> mapPuntuacionsJoc;
    private Map<String, AlineacioJugadorPuntuacio> mapPuntuacions;
    private List<DocumentSnapshot> listDocuments;
    private List<DocumentSnapshot> listDocuments2;
    private List<DocumentSnapshot> listDocuments3;
    private List<String> jornadesPossibles;

    public FirebaseOperationsPuntuacionsPage(Context context) {
        super(context);
        mapPuntuacionsJoc = new HashMap<>();
        listDocuments = new ArrayList<>();
        listDocuments2 = new ArrayList<>();
        listDocuments3 = new ArrayList<>();
        mapPuntuacions = new HashMap<>();
        jornadesPossibles = new ArrayList<>();
    }

    public List<String> getJornadesPossibles() {
        return jornadesPossibles;
    }

    public void setJornadesPossibles(List<String> jornadesPossibles) {
        this.jornadesPossibles = jornadesPossibles;
    }

    public Map<String, AlineacioJugadorPuntuacio> getMapPuntuacions() {
        return mapPuntuacions;
    }

    public void setMapPuntuacions(Map<String, AlineacioJugadorPuntuacio> mapPuntuacions) {
        this.mapPuntuacions = mapPuntuacions;
    }

    public Map<String, String> getMapPuntuacionsJoc() {
        return mapPuntuacionsJoc;
    }

    public void setMapPuntuacionsJoc(Map<String, String> mapPuntuacionsJoc) {
        this.mapPuntuacionsJoc = mapPuntuacionsJoc;
    }

    public List<DocumentSnapshot> getListDocuments() {
        return listDocuments;
    }

    public void setListDocuments(List<DocumentSnapshot> listDocuments) {
        this.listDocuments = listDocuments;
    }

    public List<DocumentSnapshot> getListDocuments2() {
        return listDocuments2;
    }

    public void setListDocuments2(List<DocumentSnapshot> listDocuments2) {
        this.listDocuments2 = listDocuments2;
    }

    public Task<DocumentSnapshot> getPuntuacionsJoc(){
        return super.getFirebaseFirestore().collection("PuntuacionsJoc").document("PuntuacionsJoc").get().addOnCompleteListener(task -> {
            mapPuntuacionsJoc.put("golMarcat", (String) task.getResult().get("golMarcat"));
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
