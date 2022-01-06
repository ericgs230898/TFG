package com.tfg_project.model.firestore;

import android.app.Activity;
import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.R;
import com.tfg_project.controlador.AdminPage;
import com.tfg_project.controlador.MenuPrincipal;
import com.tfg_project.model.beans.Constants;
import com.tfg_project.model.beans.LoadingDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FirebaseOperationsLoginPage extends FirebaseOperations {

    public FirebaseOperationsLoginPage(Context context) {
        super(context);
    }

    // LOGIN PAGE
    public void getUsername() {
        super.getFirebaseFirestore().collection(Constants.USUARI).document(super.getFirebaseAuth().getCurrentUser().getEmail()).get().addOnCompleteListener(task -> {
            String usuari = (String) task.getResult().get(Constants.USUARI_LOWERCASE);
            Map<String, String> mapGoTo = new HashMap<>();
            mapGoTo.put(Constants.EMAIL, super.getFirebaseAuth().getCurrentUser().getEmail());
            mapGoTo.put(Constants.USERNAME, usuari);
            LoadingDialog.getInstance(null).dissmisDialog();
            LoadingDialog.getInstance(null).eliminateLoadingDialog();
            super.getUtilsProject().goToAnotherActivity(MenuPrincipal.class, mapGoTo);
        });
    }

    public void signIn(Activity activity, String email, String password){
        super.getFirebaseAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()){
                if ( Objects.requireNonNull(Objects.requireNonNull(task.getResult()).getUser()).isEmailVerified() ) {
                    if ( "eric.gonzalez.sole@estudiantat.upc.edu".equals(email)) {
                        LoadingDialog.getInstance(activity).dissmisDialog();
                        LoadingDialog.getInstance(activity).eliminateLoadingDialog();
                        super.getUtilsProject().goToAnotherActivity(AdminPage.class, null);
                        activity.finishAffinity();
                    }
                    else {
                        FirebaseFirestore.getInstance().collection(Constants.USUARI).document(email).get().addOnCompleteListener(task1 -> {
                            String usuari = (String) task1.getResult().get(Constants.USUARI_LOWERCASE);
                            Map<String, String> mapGoTo = new HashMap<>();
                            mapGoTo.put(Constants.EMAIL, email);
                            mapGoTo.put(Constants.USERNAME, usuari);
                            LoadingDialog.getInstance(activity).dissmisDialog();
                            LoadingDialog.getInstance(activity).eliminateLoadingDialog();
                            super.getUtilsProject().goToAnotherActivity(MenuPrincipal.class, mapGoTo);
                            activity.finish();
                        });
                    }
                } else {
                    LoadingDialog.getInstance(activity).dissmisDialog();
                    LoadingDialog.getInstance(activity).eliminateLoadingDialog();
                    super.getUtilsProject().makeToast("Encara no s'ha verificat l'adreça electrònica");
                }
            } else {
                LoadingDialog.getInstance(activity).dissmisDialog();
                LoadingDialog.getInstance(activity).eliminateLoadingDialog();
                //Log.w(TAG, "signInWithEmail:failure", task.getException());
                super.getUtilsProject().makeToast("L'usuari i contrasenya especificats no corresponen a cap usuari de l'aplicació");
            }
        });
    }

    public void sendPasswordResetEmail(String email){
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
            super.getUtilsProject().makeToast("S'ha enviat un correu electrònic a l'adreça electrònica per tal de recuperar la contrasenya.");
        });
    }


}
