package com.tfg_project.model.firestore;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.model.utils.UtilsProject;

public class FirebaseOperations {
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private UtilsProject utilsProject;
    private Context context;

    public FirebaseOperations(Context context) {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.utilsProject = new UtilsProject(context);
    }

    public FirebaseFirestore getFirebaseFirestore() {
        return firebaseFirestore;
    }

    public void setFirebaseFirestore(FirebaseFirestore firebaseFirestore) {
        this.firebaseFirestore = firebaseFirestore;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public void setFirebaseAuth(FirebaseAuth firebaseAuth) {
        this.firebaseAuth = firebaseAuth;
    }

    public UtilsProject getUtilsProject() {
        return utilsProject;
    }

    public void setUtilsProject(UtilsProject utilsProject) {
        this.utilsProject = utilsProject;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
