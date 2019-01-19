package com.example.ecko.spots.model;

import java.util.LinkedList;

public class User {
    private String email;
   // private Parque parquedoSpot = ParquesManager.INSTANCE.getDefaultParque();
    private int indexMySpot = 4;
    private Boolean isAutenticado;
    private String preference;
    private String password;
    private LinkedList<Spot> spotsFavoritos;
    private Spot lugarOcupado;

    public User(String email, Boolean isAutenticado, String preference, String password, LinkedList<Spot> spotsFavoritos, Spot lugarOcupado) {
        this.email = email;
        this.isAutenticado = isAutenticado;
        this.preference = preference;
        this.password = password;
        this.spotsFavoritos = spotsFavoritos;
        this.lugarOcupado = lugarOcupado;
    }

    public Boolean getAutenticado() {
        return isAutenticado;
    }

    public void setAutenticado(Boolean autenticado) {
        isAutenticado = autenticado;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Spot getLugarOcupado() {
        return lugarOcupado;
    }

    public void setLugarOcupado(Spot lugarOcupado) {
        this.lugarOcupado = lugarOcupado;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   /* public Parque getParquedoSpot() {
        return parquedoSpot;
    }

    public void setParquedoSpot(Parque parquedoSpot) {
        this.parquedoSpot = parquedoSpot;
    }*/

    public int getIndexMySpot() {
        return indexMySpot;
    }

    public void setIndexMySpot(int indexMySpot) {
        this.indexMySpot = indexMySpot;
    }

    public String getPreference() {
        return preference;
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public LinkedList<Spot> getSpotsFavoritos() {
        return spotsFavoritos;
    }

    public void addFavorito(Spot spot){
        this.spotsFavoritos.add(spot);
    }

    public void removeFavorito(Spot spot){
        this.spotsFavoritos.remove(spot);
    }

    public void cleanFavorito(Spot spot){
        this.spotsFavoritos = new LinkedList<>();
    }

}
