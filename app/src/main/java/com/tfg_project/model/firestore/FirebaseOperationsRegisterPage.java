package com.tfg_project.model.firestore;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.controlador.LoginPage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirebaseOperationsRegisterPage extends FirebaseOperations{

    public FirebaseOperationsRegisterPage(Context context) {
        super(context);
    }

    public void createUser(Activity activity, String email, String username, String password){
        super.getFirebaseAuth().createUserWithEmailAndPassword(email,
                password).addOnCompleteListener(task -> {
            if ( task.isSuccessful() ) {
                task.getResult().getUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                    if (task1.isSuccessful()){
                        Log.d("RGSR", "Email sent");
                        super.getUtilsProject().makeToast("S'ha enviat un mail per confirmar la teva compte!");
                        registerUserInDataBase(username, email);
                    }
                });
                Log.d("RGSR","Register Successful");
                super.getUtilsProject().goToAnotherActivity(LoginPage.class, null);
                activity.finish();
            }
            else {
                Log.w("RGSR", "CreateUserWithEmail:failure",task.getException());
            }
        });
    }

    private void registerUserInDataBase(String username, String mail) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String,String> userMap = new HashMap<>();
        userMap.put("usuari", username);
        db.collection("Usuari").document(mail).set(userMap).addOnCompleteListener(task -> Log.d("TAG", "completeInsertUsuari"));
        Map<String, List> mapList = new HashMap<>();
        mapList.put("Lligues", Collections.emptyList());
        db.collection("UsuariLligaVirtual").document(mail).set(mapList).addOnCompleteListener(task -> Log.d("TAG", "completeInsertUserLligaVirtual"));
    }
}
