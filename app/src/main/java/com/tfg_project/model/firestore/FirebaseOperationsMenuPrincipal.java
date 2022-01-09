package com.tfg_project.model.firestore;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.tfg_project.model.beans.LliguesVirtuals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FirebaseOperationsMenuPrincipal extends FirebaseOperations {
    private static final String LLIGUES_VIRTUALS = "LliguesVirtuals";
    private static final String COMPETICIO = "competicio";
    private List<String> listLliguesUsuari;
    private final List<LliguesVirtuals> lliguesVirtualsUsuariList;

    public FirebaseOperationsMenuPrincipal(Context context) {
        super(context);
        listLliguesUsuari = new ArrayList<>();
        lliguesVirtualsUsuariList = new ArrayList<>();
    }

    public List<String> getListLliguesUsuari() {
        return listLliguesUsuari;
    }

    public List<LliguesVirtuals> getLliguesVirtualsUsuariList() {
        return lliguesVirtualsUsuariList;
    }

    public Task<DocumentSnapshot> getLliguesUsuari (){
        return super.getFirebaseFirestore().collection("UsuariLligaVirtual").document(Objects.requireNonNull(super.getFirebaseAuth().getCurrentUser().getEmail())).get().addOnCompleteListener(task -> {
            listLliguesUsuari = (ArrayList<String>) Objects.requireNonNull(task.getResult()).get("Lligues");
            if (listLliguesUsuari == null){
                listLliguesUsuari = Collections.emptyList();
            }
        });
    }

    public Task<DocumentSnapshot> getInfoLligaVirtual (String nomLlVirtual){
        return super.getFirebaseFirestore().collection(LLIGUES_VIRTUALS).document(nomLlVirtual).get().addOnCompleteListener(task -> {
            if ( task.isSuccessful() ) {
                final DocumentSnapshot documentSnapshot = task.getResult();
                final String competicio = (String) Objects.requireNonNull(documentSnapshot).get(COMPETICIO);
                final String grup = (String) documentSnapshot.get("grup");
                final  ArrayList<String> participants = (ArrayList<String>) documentSnapshot.get("usuaris");
                if ( participants != null ) {
                    lliguesVirtualsUsuariList.add(new LliguesVirtuals(nomLlVirtual, String.valueOf(participants.size()), competicio, grup));
                }
                else lliguesVirtualsUsuariList.add(new LliguesVirtuals(nomLlVirtual, "1", competicio, grup));
            }
        });
    }

    public Task<DocumentSnapshot> addLligaVirtual(String nomLligaVirtual, String password){
        return super.getFirebaseFirestore().collection(LLIGUES_VIRTUALS).document(nomLligaVirtual).get().addOnCompleteListener(task -> {
            if ( task.isSuccessful() ) {
                DocumentSnapshot documentSnapshot = task.getResult();
                String passwordDB = (String) Objects.requireNonNull(documentSnapshot).get("password");
                if (password.equals(passwordDB)){
                    super.getUtilsProject().makeToast("Has sigut afegit a la lliga virtual!");
                    lliguesVirtualsUsuariList.add(new LliguesVirtuals(nomLligaVirtual, String.valueOf(lliguesVirtualsUsuariList.size()+1), (String) documentSnapshot.get(COMPETICIO), (String) documentSnapshot.get("grup")));
                    putNewUserToLligaVirtual(nomLligaVirtual, false, null);
                } else {
                    super.getUtilsProject().makeToast("El nom de la lliga o la contrasenya no són correctes");
                }
            }
            else {
                super.getUtilsProject().makeToast("El nom de la lliga o la contrasenya no són correctes");
            }
        });
    }

    public List<Task<Void>> putNewUserToLligaVirtual(String nomLligaVirtual, boolean lligaVirtualNova, Map<String, Object> map) {
        String email = Objects.requireNonNull(super.getFirebaseAuth().getCurrentUser()).getEmail();
        List<Task<Void>> tasks = new ArrayList<>();
        // equal
        DocumentReference documentReference = super.getFirebaseFirestore().collection("UsuariLligaVirtual").document(Objects.requireNonNull(email));
        tasks.add(documentReference.update("Lligues", FieldValue.arrayUnion(nomLligaVirtual)).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                Log.d("TAG","CORRECT");
            } else {
                Log.d("TAG", "INCORRECT");
            }
        }));

        DocumentReference documentReference1 = super.getFirebaseFirestore().collection(LLIGUES_VIRTUALS).document(nomLligaVirtual);
        if ( lligaVirtualNova ) {
            LliguesVirtuals lliguesVirtuals = new LliguesVirtuals();
            lliguesVirtuals.setNomLligaVirtual(nomLligaVirtual);
            for ( Map.Entry<String, Object> entry : map.entrySet() ){
                if ( COMPETICIO.equals(entry.getKey())){
                    lliguesVirtuals.setCompeticio((String) entry.getValue());
                }
                if ( "grup".equals(entry.getKey())){
                    lliguesVirtuals.setGrup((String) entry.getValue());
                }
            }
            lliguesVirtuals.setParticipants("1");
            lliguesVirtualsUsuariList.add(lliguesVirtuals);
            tasks.add(documentReference1.set(map).addOnCompleteListener(task -> {
                if (task.isSuccessful()){
                    Log.d("TAG", "TASK COMPLETED SUCCESSFULLY");
                }
            }));
        } else {
            tasks.add(documentReference1.update("usuaris", FieldValue.arrayUnion(email)).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Log.d("TAG", "CORRECT");
                } else {
                    Log.d("TAG", "INCORRECT");
                }
            }));
        }
        // equal
        Map<String, String> mapPunts = new HashMap<>();
        mapPunts.put("punts", "0");
        tasks.add(documentReference1.collection("Classificacio").document(email).set(mapPunts).addOnCompleteListener(task -> {

        }));
        return tasks;
    }
}
