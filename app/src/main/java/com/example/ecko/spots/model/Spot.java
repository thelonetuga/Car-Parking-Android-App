package com.example.ecko.spots.model;

public class Spot {

    private double coordenadaX;
    private double coordenadaY;
    private String parqueNome;
    private int estado;   //(livre = 0) (marcado = 2) (ocupado = 1)
    private float rating;
    private Boolean isOcupado;

    public Spot(double coordenadaX, double coordenadaY, String parqueNome, Boolean isOcupado,int estado, float rating) {
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.parqueNome = parqueNome;
        this.estado = estado;
        this.rating = rating;
        this.isOcupado = isOcupado;
    }

    public Spot(){

    }


    public String getParqueNome() {
        return parqueNome;
    }

    public void setParqueNome(String parqueNome) {
        this.parqueNome = parqueNome;
    }

    public Boolean getOcupado() {
        return isOcupado;
    }

    public void setOcupado(Boolean ocupado) {
        isOcupado = ocupado;
    }

    public double getCoordenadaX() {
        return coordenadaX;
    }

    public void setCoordenadaX(double coordenadaX) {
        this.coordenadaX = coordenadaX;
    }

    public double getCoordenadaY() {
        return coordenadaY;
    }

    public void setCoordenadaY(double coordenadaY) {
        this.coordenadaY = coordenadaY;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "Parque " + parqueNome + " (" + coordenadaX + "," + coordenadaY + ") Rating: " + rating;
    }
}
