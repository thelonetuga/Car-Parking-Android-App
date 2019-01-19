package com.example.ecko.spots.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parque {
    private String nome;
    private int comprimento;
    private int largura;
    private double coordenadaX;
    private double coordenadaY;
    private List<Spot> spots;

    public Parque(String nome, int comprimento, int largura, double coordenadaX, double coordenadaY, List spots) {
        this.nome = nome;
        this.comprimento = comprimento;
        this.largura = largura;
        this.coordenadaX = coordenadaX;
        this.coordenadaY = coordenadaY;
        this.spots = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getComprimento() {
        return comprimento;
    }

    public void setComprimento(int comprimento) {
        this.comprimento = comprimento;
    }

    public int getLargura() {
        return largura;
    }

    public void setLargura(int largura) {
        this.largura = largura;
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

    @Override
    public String toString() {
        return "Parque " + nome + '(' +comprimento + "x" + largura + ')' ;
    }

    public List<Spot> getSpots() {
        return spots;
    }

    public void addSpot(Spot spot) {
        spots.add(spot);
    }

    public void removeSpot(Spot spot){
        spots.remove(spot);
    }

    public void cleanSpots(){
        spots = new LinkedList<>();
    }

    public boolean isFull(){
        for (Spot spot:spots) {
            if (spot.getEstado() == 0){ //nao esta full
                return false;
            }
        }
        return true;
    }
}
