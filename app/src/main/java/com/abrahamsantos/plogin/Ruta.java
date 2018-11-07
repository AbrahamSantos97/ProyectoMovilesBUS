package com.abrahamsantos.plogin;

import java.util.ArrayList;

public class Ruta {
    private String nombre;
    protected ArrayList<Parada> paradas;

    public Ruta(String nombre,Parada parada){
        this.nombre = nombre;
        this.paradas.add(parada);
    }

    protected Parada getparada(int posicion){
        return this.paradas.get(posicion);
    }

}
