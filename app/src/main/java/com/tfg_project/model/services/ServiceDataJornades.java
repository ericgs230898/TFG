package com.tfg_project.model.services;

import android.os.NetworkOnMainThreadException;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ServiceDataJornades {
    public ServiceDataJornades() {
    }

    public void ejecutar(String competicio, String grup, String jornada){
        Thread thread = new Thread(() -> {
            URL url = null;
            try {
                String link = "https://www.fcf.cat/resultats/2022/futbol-11/"+competicio+"/grup-"+grup+"/jornada-"+jornada;
                try {
                    url = new URL(link);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                BufferedReader in = null;
                try {
                    if (url != null) {
                        in = new BufferedReader(
                                new InputStreamReader(url.openStream()));
                    }
                } catch (IOException | NetworkOnMainThreadException e) {
                    e.printStackTrace();
                }
                StringBuilder text = new StringBuilder();
                if (in != null) {
                    try {
                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            text.append(inputLine);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                ArrayList<Date> dates = new ArrayList<Date>();
                String textString = text.toString();
                int indexData = textString.indexOf("lh-data");
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                while ( indexData != -1 ) {
                    StringBuilder dataBuilder = new StringBuilder();
                    int i=indexData+9;
                    while ( true ) {
                        if ( textString.charAt(i) == '<') break;
                        else if ( (textString.charAt(i) > 47 && textString.charAt(i) < 58) || textString.charAt(i) == '-' ) dataBuilder.append(textString.charAt(i));
                        i++;
                    }
                    if (!dataBuilder.toString().equals("")) {
                        Date date = sdf.parse(dataBuilder.toString());
                        dates.add(date);
                        System.out.println("DATA ----> " + dataBuilder.toString() + "  " + dataBuilder.toString().length());
                    }
                    indexData = textString.indexOf("lh-data", indexData+1);
                }
                Date minDate = Collections.min(dates);
                Date maxDate = Collections.max(dates);
                String minDateString = sdf.format(minDate);
                String maxDateString = sdf.format(maxDate);
                Map<String, String> mapDates = new HashMap<>();
                mapDates.put("dataInici", minDateString);
                mapDates.put("dataFi", maxDateString);
                FirebaseFirestore.getInstance().collection("PartitsJugats").document(competicio)
                        .collection("Grups").document("grup"+grup)
                        .collection("Jornades").document("jornada"+jornada)
                        .set(mapDates).addOnCompleteListener(task -> {

                        });

            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
