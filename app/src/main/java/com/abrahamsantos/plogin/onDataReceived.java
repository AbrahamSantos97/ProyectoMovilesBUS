package com.abrahamsantos.plogin;

import android.content.Intent;
import android.view.View;
import android.widget.ArrayAdapter;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface onDataReceived {
    void setRutas(ArrayList<Ruta> nuevo);
    ArrayList<Ruta> getRutas();
    void setNombres(String[] nuevo);
    String[] getNombres();
    void setMenuRutas(ArrayList<Ruta> nuevo,View view);
}
