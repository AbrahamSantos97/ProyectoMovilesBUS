package com.abrahamsantos.plogin;

public class Camiones {
    private String ruta;
    private double Latitud;
    private double Longitud;

    public Camiones(String ruta, double latitud, double longitud) {
        this.ruta = ruta;
        this.Latitud = latitud;
        this.Longitud = longitud;
    }

    public String getRuta() {
        return ruta;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }
}
