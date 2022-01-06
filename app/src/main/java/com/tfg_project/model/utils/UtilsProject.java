package com.tfg_project.model.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import java.util.Map;
import java.util.regex.Pattern;

public class UtilsProject {
    private Context context;

    public UtilsProject(Context context) {
        this.context = context;
    }

    public void makeToast(String text){
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public void goToAnotherActivity(Object pageClass, Map<String,String> map) {
        Intent intent = new Intent(context, (Class<?>) pageClass);
        if (map != null){
            for (Map.Entry<String, String> entry : map.entrySet()) {
                intent.putExtra(entry.getKey(), entry.getValue());
            }
        }
        context.startActivity(intent);
    }

    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN
                = Pattern.compile(
                "[a-zA-Z0-9\\!\\@\\#\\$]{8,24}");

        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }

    public static boolean isValidMail(String s){
        Pattern MAIL_PATTERN
                = Pattern.compile("^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$");
        return !TextUtils.isEmpty(s) && MAIL_PATTERN.matcher(s).matches();
    }

    public static boolean isValidUsername(String s){
        Pattern USERNAME_PATTERN
                = Pattern.compile(
                "^[a-zA-Z0-9]([._-](?![._-])|[a-zA-Z0-9]){3,18}[a-zA-Z0-9]$");
        return !TextUtils.isEmpty(s) && USERNAME_PATTERN.matcher(s).matches();
    }

    public boolean checkParameters(String email, String password) {
        if (!isValidMail(email)){
            makeToast("El mail: " + email +  " no té un format correcte");
            return false;
        }
        if (!isValidPassword(password)){
            makeToast("El password no té un format correcte");
            return false;
        }
        return true;
    }

    public boolean checkParameters(String email, String username, String password1, String password2) {
        if (TextUtils.isEmpty(username) ||
                TextUtils.isEmpty(email) ||
                TextUtils.isEmpty(password1) ||
                TextUtils.isEmpty(password2)){
            makeToast("Alguno de los campos no está rellenado");
            return false;
        }
        else if (!password1.equals(password2)){
            makeToast("Las contraseñas no coinciden");
            return false;
        } else if ( !isValidPassword(password1)){
            makeToast("La contraseña no concuerda con el formato");
            return false;
        } else if ( !isValidMail(email)){
            makeToast("El mail no concuerda con el formato");
            return false;
        } else if (!isValidUsername(username)){
            makeToast("El username no concuerda con el formato");
            return false;
        }
        return true;
    }
}
