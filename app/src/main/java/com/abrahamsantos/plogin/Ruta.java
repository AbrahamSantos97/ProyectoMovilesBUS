package com.abrahamsantos.plogin;

import java.util.ArrayList;

public class Ruta {
    private String nombre;
    public ArrayList<Parada> paradas = new ArrayList<>();

    public Ruta(String nombre,Parada parada){
        this.nombre = nombre;
        this.paradas.add(parada);
    }

    public Parada getparada(int posicion){
        return this.paradas.get(posicion);
    }

}
