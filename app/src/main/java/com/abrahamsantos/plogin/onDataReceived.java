package com.abrahamsantos.plogin;

import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface onDataReceived {
    void setRutas(ArrayList<Ruta> nuevo);
    ArrayList<Ruta> getRutas();
    void setNombres(String[] nuevo);
    String[] getNombres();
}
