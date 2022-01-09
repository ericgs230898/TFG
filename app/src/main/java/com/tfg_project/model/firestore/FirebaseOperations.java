package com.tfg_project.model.firestore;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.tfg_project.model.utils.UtilsProject;

public class FirebaseOperations {
    private final FirebaseFirestore firebaseFirestore;
    private final FirebaseAuth firebaseAuth;
    private final UtilsProject utilsProject;

    public FirebaseOperations(Context context) {
        this.firebaseFirestore = FirebaseFirestore.getInstance();
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.utilsProject = new UtilsProject(context);
    }

    public FirebaseFirestore getFirebaseFirestore() {
        return firebaseFirestore;
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }

    public UtilsProject getUtilsProject() {
        return utilsProject;
    }
}
