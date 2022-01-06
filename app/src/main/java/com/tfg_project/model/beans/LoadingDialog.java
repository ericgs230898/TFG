package com.tfg_project.model.beans;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.tfg_project.R;

public final class LoadingDialog {
    private static LoadingDialog loadingDialog;
    public Activity activity;
    private AlertDialog dialog;

    public LoadingDialog (Activity myActivity) {
        this.activity = myActivity;
    }

    public static LoadingDialog getInstance(Activity myActivity){
        if ( loadingDialog == null ) {
            loadingDialog = new LoadingDialog(myActivity);
        }
        return loadingDialog;
    }

    public AlertDialog getDialog() {
        return dialog;
    }

    public void setDialog(AlertDialog dialog) {
        this.dialog = dialog;
    }

    public void eliminateLoadingDialog(){
        this.loadingDialog = null;
    }

    public void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.alert_dialog_loading, null));
        builder.setCancelable(false);

        LoadingDialog.getInstance(activity).setDialog(builder.create());
        LoadingDialog.getInstance(activity).getDialog().show();
    }

    public void dissmisDialog(){
        if ( loadingDialog != null && loadingDialog.getDialog() != null ) loadingDialog.getDialog().dismiss();
    }
}
