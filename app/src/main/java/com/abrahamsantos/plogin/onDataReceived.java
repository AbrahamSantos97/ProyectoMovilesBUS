package com.abrahamsantos.plogin;

import android.view.View;

import java.util.ArrayList;

public interface onDataReceived {
    /*Establece etiquetas*/
    void setRutas(ArrayList<Ruta> nuevo);
    /*--- Buscador ---*/
    ArrayList<Ruta> getRutas();
    void setMenuRutas(ArrayList<Ruta> nuevo,View view);
    /*Establece un registro nuevo*/
    void nuevaParada(ArrayList<Ruta> lista_ruta);
}
