package com.abrahamsantos.plogin;

import java.util.ArrayList;

public class Ruta {
    public String nombre;
    public ArrayList<Parada> paradas = new ArrayList<>();

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Parada> getParadas() {
        return paradas;
    }

    public void setParadas(ArrayList<Parada> paradas) {
        this.paradas = paradas;
    }
}
