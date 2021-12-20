package com.tfg_project.clases;

import android.content.Context;
import android.widget.Toast;

public class UtilsProject {

    public UtilsProject() {
    }

    public void makeToast(Context context, String text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }
}
