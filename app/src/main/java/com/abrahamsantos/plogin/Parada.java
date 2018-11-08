package com.abrahamsantos.plogin;

public class Parada {
    private int riesgo;
    private String imagen,direccion;
    private double coordenadaX,coordenadaY;

    public Parada(int riesgo,String imagen,String direccion,double x, double y){
        this.riesgo = riesgo;
        this.imagen = imagen;
        this.direccion = direccion;
        this.coordenadaX = x;
        this.coordenadaY = y;
    }

    private int getriesgo(){
        return this.riesgo;
    }

    private String getimagen(){
        return this.imagen;
    }

    private String getdireccion(){return this.direccion;}

    private double getcoordenadaX(){
        return this.coordenadaX;
    }

    private double getcoordenadaY(){
        return this.coordenadaY;
    }
}
