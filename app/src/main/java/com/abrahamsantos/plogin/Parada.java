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

    protected int getriesgo(){
        return this.riesgo;
    }

    protected String getimagen(){
        return this.imagen;
    }

    protected String getdireccion(){return this.direccion;}

    protected double getcoordenadaX(){
        return this.coordenadaX;
    }

    protected double getcoordenadaY(){
        return this.coordenadaY;
    }
}
