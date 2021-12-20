package com.tfg_project.clases;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.tfg_project.R;
import com.tfg_project.databinding.ActivityAdminPageBinding;

public class LoadingDialog {
    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog (Activity myActivity) {
        activity = myActivity;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.alert_dialog_loading, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    public void dissmisDialog(){
        dialog.dismiss();
    }
}
