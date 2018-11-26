package com.abrahamsantos.plogin;

import java.io.Serializable;
import java.util.ArrayList;

public class Ruta implements Serializable {
    public String nombre;
    public ArrayList<Parada> parada;

    public String getNombre() {
        return nombre;
    }

    public ArrayList<Parada> getParada() {
        return parada;
    }
}
