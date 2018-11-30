package com.abrahamsantos.plogin;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;

import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface onDataReceived {
    /*Establece etiquetas*/
    void setRutas(ArrayList<Ruta> nuevo);
    /*--- Buscador ---*/
    ArrayList<Ruta> getRutas();
    void setNombres(String[] nuevo);
    String[] getNombres();
    /*Recycler menu*/
    void setMenuRutas(ArrayList<Ruta> nuevo,View view);
    /*Establece un registro nuevo*/
    void nuevaParada(ArrayList<Ruta> lista_ruta);
}
