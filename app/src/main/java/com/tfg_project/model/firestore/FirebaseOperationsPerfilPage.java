package com.tfg_project.model.firestore;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.controlador.LoginPage;
import com.tfg_project.model.beans.Constants;

import java.util.List;

public class FirebaseOperationsPerfilPage extends FirebaseOperations {
    private String usuari;
    public FirebaseOperationsPerfilPage(Context context) {
        super(context);
    }

    public String getUsuari() {
        return usuari;
    }

    public void setUsuari(String usuari) {
        this.usuari = usuari;
    }

    public Task<DocumentSnapshot> getUsername(){
        return super.getFirebaseFirestore().collection(Constants.USUARI).document(super.getFirebaseAuth().getCurrentUser().getEmail()).get().addOnCompleteListener(task -> {
            usuari = (String) task.getResult().get("usuari");
        });
    }
    public void modificaDadesUsuari(String newUsername){
        super.getFirebaseFirestore().collection("Usuari").document(super.getFirebaseAuth().getCurrentUser().getEmail()).update("usuari", newUsername).addOnCompleteListener(task -> {
            super.getUtilsProject().makeToast("Dades modificades correctament.");
        });
    }

    public void eliminaUsuari(){
        String mail = super.getFirebaseAuth().getCurrentUser().getEmail();
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("UsuariLligaVirtual").document(mail);
        documentReference.get().addOnCompleteListener(task -> {
            List<String> lliguesUsuari = (List<String>) task.getResult().get("Lligues");
            int size = lliguesUsuari.size();
            final int[] count = {0};
            for ( String lligaUsuari : lliguesUsuari ) {
                FirebaseFirestore.getInstance().collection("LliguesVirtuals").document(lligaUsuari).update("usuaris", FieldValue.arrayRemove(mail)).addOnCompleteListener(task14 -> {
                    Log.d("TAG","Usuari eliminat de les lligues virtuals");
                    count[0]++;
                    if ( count[0] == size ) {
                        documentReference.delete().addOnCompleteListener(task13 -> {
                            Log.d("TAG", "Usuari eliminat de les lligues virtuals2");
                            FirebaseFirestore.getInstance().collection("Usuari").document(mail).delete().addOnCompleteListener(task12 -> {
                                Log.d("TAG", "Usuari eliminat");
                                FirebaseAuth.getInstance().getCurrentUser().delete().addOnCompleteListener(task1 -> {
                                    Log.d("TAG","Usuari eliminat FIREBASE AUTH");
                                    super.getUtilsProject().goToAnotherActivity(LoginPage.class, null);
                                });
                            });
                        });
                    }
                });
            }
        });
    }
}
