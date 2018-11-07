package com.abrahamsantos.plogin;

import java.util.ArrayList;

public class Rutas {
    protected ArrayList<Ruta> rutas;

    public Rutas(Ruta nueva_ruta) {
        this.rutas.add(nueva_ruta);
    }

    protected Ruta getrutas(int posicion){
        return this.rutas.get(posicion);
    }

}
