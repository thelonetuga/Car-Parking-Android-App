package com.example.ecko.spots.model;

import java.util.ArrayList;
import java.util.List;

public enum SpotsManager {
    INSTANCE;

    private List<Spot> listSpotESSLEI;
    private List<Spot> listSpotD;
    private List<Spot> listSpotA;
    private List<Spot> defaultParqueSpots;
    private double latitude;
    private double longitude;


    SpotsManager() {
        this.defaultParqueSpots = new ArrayList<>();
        this.listSpotESSLEI = new ArrayList<>();
        this.listSpotD = new ArrayList<>();
        this.listSpotA = new ArrayList<>();
        //setupInitialSpots(); Usar quando base de dados nao tiver dados dos spots
    }

    private void setupInitialSpots(){
        //Criar spots para default park
        defaultParqueSpots.add(new Spot(39.734913, -8.820797, "DEFAULTPARK", false,0,5));//0
        defaultParqueSpots.add(new Spot(39.735196, -8.820207, "DEFAULTPARK",true,1,2));//1
        defaultParqueSpots.add(new Spot(39.735003, -8.820639, "DEFAULTPARK",false,0,3));//2
        defaultParqueSpots.add(new Spot(39.735193, -8.820333, "DEFAULTPARK",false,0,4));//3
        defaultParqueSpots.add(new Spot(39.735166, -8.820553, "DEFAULTPARK",true,1,3));//4
        defaultParqueSpots.add(new Spot(39.733556, -8.821206, "DEFAULTPARK",false,0,1));//5
        defaultParqueSpots.add(new Spot(39.734140, -8.821334, "DEFAULTPARK",false,0,3));//6
        defaultParqueSpots.add(new Spot(39.734079, -8.821500, "DEFAULTPARK",false,0,2));//7
        defaultParqueSpots.add(new Spot(39.733869, -8.821058, "DEFAULTPARK",false,1,4));//8/
        // /ParqueD
        listSpotD.add(new Spot(39.733565, -8.821351, "ParqueD" ,false,0,5));//0
        listSpotD.add(new Spot(39.734176, -8.821126, "ParqueD",false,2,2));//1
        listSpotD.add(new Spot(39.733832, -8.820951, "ParqueD",false,0,3));//2
        listSpotD.add(new Spot(39.734119, -8.821761, "ParqueD",false,0,4));//3
        listSpotD.add(new Spot(39.733522, -8.821090, "ParqueD",true,1,3));//4
        listSpotD.add(new Spot(39.733556, -8.821206, "ParqueD",false,0,1));//5
        listSpotD.add(new Spot(39.734140, -8.821334, "ParqueD",false,0,3));//6
        listSpotD.add(new Spot(39.734079, -8.821500, "ParqueD",false,0,2));//7
        System.out.println("Number of items in the list: " + listSpotD.size());
        //ParqueA
        listSpotA.add(new Spot(39.734913, -8.820797, "ParqueA" ,false,0,5));//0
        listSpotA.add(new Spot(39.735196, -8.820207, "ParqueA",false,0,2));//1
        listSpotA.add(new Spot(39.735003, -8.820639, "ParqueA",false,0,3));//2
        listSpotA.add(new Spot(39.735193, -8.820333, "ParqueA",false,0,4));//3
        //Parques ESSLEI
        listSpotESSLEI.add(new Spot(39.732533, -8.820833, "ParqueESSLEI",true,1,3));//4
        listSpotESSLEI.add(new Spot(39.732484, -8.820627, "ParqueESSLEI",false,0,1));//5
        listSpotESSLEI.add(new Spot(39.732336, -8.820641, "ParqueESSLEI",false,0,3));//6
        listSpotESSLEI.add(new Spot(39.732277, -8.820407, "ParqueESSLEI",false,0,2));//7
        listSpotESSLEI.add(new Spot(39.732189, -8.820320, "ParqueESSLEI",true,1,4));//8
        listSpotESSLEI.add(new Spot(39.732211, -8.820515, "ParqueESSLEI",true,1,3));//4
    }

    public Spot getSpot(int spotPosition,String parqueName ) {
        if (parqueName.equals("ParqueD")){
            return  listSpotD.get(spotPosition);
        }else if(parqueName.equals("ParqueA")){
            return  listSpotA.get(spotPosition);
        }else{
            return  listSpotESSLEI.get(spotPosition);
        }
    }

    public Spot getSpotNoParque(String parqueName) {
        if (parqueName.equals("ParqueD")) {
            for (Spot spot : listSpotD) {
                if (spot.getEstado() == 2) {
                    return spot;
                }
            }
        } else if (parqueName.equals("ParqueA")) {
            for (Spot spot : listSpotA) {
                if (spot.getEstado() == 2) {
                    return spot;
                }
            }

        }  else {
            for (Spot spot : listSpotESSLEI) {
                if (spot.getEstado() == 2) {
                    return spot;
                }
            }
        }
        return null;
    }

    public void setDefaultParqueSpots(List<Spot> defaultParqueSpots) {
        this.defaultParqueSpots = defaultParqueSpots;
    }

    public List<Spot> getDefaultParqueSpots() {
        return defaultParqueSpots;
    }

    public List<Spot> getListSpotESSLEI() {
        return listSpotESSLEI;
    }

    public List<Spot> getListSpotD() {
        return listSpotD;
    }

    public List<Spot> getListSpotA() {
        return listSpotA;
    }



    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setCurrentSpot(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Spot getSpotByCoords(double latitude, double longitude, String parqueName){
        if (parqueName.equals("ParqueD")){
            for (Spot spot : listSpotD){
                if (spot.getCoordenadaX() == latitude && spot.getCoordenadaY() == longitude){
                    return spot;
                }
            }
        }else if(parqueName.equals("ParqueA")){
            for (Spot spot : listSpotA){
                if (spot.getCoordenadaX() == latitude && spot.getCoordenadaY() == longitude){
                    return spot;
                }
            }
        }else {
            for (Spot spot : listSpotESSLEI){
                if (spot.getCoordenadaX() == latitude && spot.getCoordenadaY() == longitude){
                    return spot;
                }
            }
        }
        return  null;
    }

    public int getPosicaoBySpot(String parqueName, double latitude, double longitude){
        if (parqueName.equals("ParqueD")){
            for (Spot spott : listSpotD){
                if (spott.getCoordenadaX() == latitude){
                    if (spott.getCoordenadaY() == longitude){
                        return listSpotD.indexOf(spott);
                    }
                }
            }
        }else if(parqueName.equals("ParqueA")){
            for (Spot spott : listSpotA){
                if (spott.getCoordenadaX() == latitude){
                    if (spott.getCoordenadaY() == longitude){
                        return listSpotA.indexOf(spott);
                    }
                }
            }
        }else {
            for (Spot spott : listSpotESSLEI){
                if (spott.getCoordenadaX() == latitude){
                    if (spott.getCoordenadaY() == longitude){
                        return listSpotESSLEI.indexOf(spott);
                    }
                }
            }
        }
        return -1;
    }

    public void setListSpotsParques(String nomeParqueSelecionado, List<Spot> listaSpotsParqueSelecionando) {
       if (nomeParqueSelecionado.equals("ParqueESSLEI")){
           this.listSpotESSLEI = listaSpotsParqueSelecionando;
       }else if(nomeParqueSelecionado.equals("ParqueD")){
           this.listSpotD = listaSpotsParqueSelecionando;
       }else if(nomeParqueSelecionado.equals("ParqueA")){
           this.listSpotA = listaSpotsParqueSelecionando;
       }
    }
}
