package com.example.ecko.spots.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public enum ParquesManager {
    INSTANCE;

    private List<Parque> parques;
    private List<Parque> defaultParques;
    private Parque defaultParque;
    private List<Spot> listaSpotsA;
    private List<Spot> listaSpotsESSLEI;
    private List<Spot> listaSpotsDefaultParque;
    private List<Spot> listaSpotsD;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference databaseParques = database.getReference("Parques");
    DatabaseReference databaseSpots = database.getReference("Spots");


    ParquesManager() {
        this.parques = new ArrayList<>();
        this.defaultParques = new ArrayList<>();
        this.listaSpotsA = new ArrayList<>();
        this.listaSpotsESSLEI = new ArrayList<>();
        this.listaSpotsDefaultParque = new ArrayList<>();
        this.listaSpotsD = new ArrayList<>();
        //spot nao ocupado
        setupInitialParques();
    }

    public void start() {
        setupInitialParques();
    }


    private void setupInitialParques() {
        this.listaSpotsDefaultParque = SpotsManager.INSTANCE.getDefaultParqueSpots();
        this.listaSpotsA = SpotsManager.INSTANCE.getListSpotA();
        this.listaSpotsD = SpotsManager.INSTANCE.getListSpotD();
        this.listaSpotsESSLEI = SpotsManager.INSTANCE.getListSpotESSLEI();

        //Criar Parques Iniciais
        Parque defaultPark = new Parque("DEFAULTPARK", 2, 4, 39.73519471, -8.82250696, listaSpotsDefaultParque);
        Parque parqueD = new Parque("ParqueD", 2, 4, 39.733867, -8.821259, listaSpotsD);
        Parque parqueA = new Parque("ParqueA", 1, 4, 39.73504388220136, -8.820464324194404, listaSpotsA);
        Parque parqueESSLEI = new Parque("ParqueESSLEI", 3, 2, 39.732351, -8.820572, listaSpotsESSLEI);

        //Adicionar Parques ás listas
        //Default Parque
        addDefaultParque(defaultPark, defaultPark.getNome(), listaSpotsDefaultParque);
        //Adicionar Parques Suportados
        add(parqueD, parqueD.getNome(), listaSpotsD);
        add(parqueA, parqueA.getNome(), listaSpotsA);
        add(parqueESSLEI, parqueESSLEI.getNome(), listaSpotsESSLEI);
        defaultParque = defaultParques.get(0);
    }

    public void add(Parque parque, String nome, List spots) {
        //databaseParques.child(nome).setValue(parque);
        //databaseSpots.child(nome).setValue(spots);
        parques.add(parque);
    }

    public void addDefaultParque(Parque parque, String nome, List spots) {
       //databaseParques.child(nome).setValue(parque);
       //databaseSpots.child(nome).setValue(spots);
        defaultParques.add(parque);
    }

    public List<Parque> getDefaultParques() {
        return defaultParques;
    }

    public void setDefaultParques(List<Parque> defaultParques) {
        this.defaultParques = defaultParques;
    }

    public void remove(Parque parque) {
        parques.remove(parque);
    }

    public List<Parque> getParques() {
        return new ArrayList<>(parques);
    }

    public void clearAllParques() {
        parques = new ArrayList<>();
    }

    public boolean hasParques() {
        return !parques.isEmpty();
    }

    public Parque getParque(int parquePosition) {
        return parques.get(parquePosition);
    }

    public Parque getParqueByName(String parqueName) {
        for (Parque p : parques) {
            if (p.getNome().equals(parqueName)) {
                return p;
            }
        }
        return null;
    }


    public List<Spot> getListaSpotsDefaultParque() {
        return listaSpotsDefaultParque;
    }

    public void setListaSpotsDefaultParque(List<Spot> listaSpotsDefaultParque) {
        this.listaSpotsDefaultParque = listaSpotsDefaultParque;
    }

    public Parque getDefaultParque() {
        return defaultParque;
    }

    public int getTamanhoParque(Parque parque) {
        return parque.getComprimento() * parque.getLargura();
    }

    public void setDefaultParque(Parque defaultParque) {
        this.defaultParque = defaultParque;
    }

    public boolean hasDefaultParque() {
        if (getDefaultParque() == null) {
            return false;
        } else {
            return true;
        }
    }

    public String hasNoDefaultParque() {
        if (getDefaultParque() == null) {
            return "no";
        } else {
            return "yes";
        }
    }


    public Boolean isLugarOcupado(Spot spot) {
        if (spot.getOcupado() == true) {
            return true;
        }
        return false;
    }

    public int getNumSpotsOcupados(List<Spot> listaSpots) {
        int countOcupados = 0;
        for (Spot spot : listaSpots) {
            if (spot.getEstado() == 1) {
                countOcupados++;
            }
        }
        return countOcupados;
    }

    public String hasAvailableSpots(List<Spot> listaSpots, Parque parque) {
        if (getNumSpotsOcupados(listaSpots) < getTamanhoParque(parque)) {
            return "yes";
        } else {
            return "no";
        }
    }



    public boolean hasSpot(String parqueName) {
        Spot spot = SpotsManager.INSTANCE.getSpotNoParque(parqueName);
        if (spot != null) {
            if (spot.getEstado() == 2) {
                return true;
            }
            return false;
        } else {
            return false;
        }

    }

    public List<Spot> getListaSpotsNomeParque(String parqueName) {
        if (parqueName.equals("ParqueD")) {
            return SpotsManager.INSTANCE.getListSpotD();
        } else if (parqueName.equals("ParqueA")) {
            return SpotsManager.INSTANCE.getListSpotA();
        } else if (parqueName.equals("ParqueESSLEI")) {
            return SpotsManager.INSTANCE.getListSpotESSLEI();
        }
        return null;
    }

    public List<Spot> getListaSpotsD() {
        return listaSpotsD;
    }

    public int getPosicaoSpotNoParque(String parqueName) {
        if (parqueName.equals("ParqueD")){
            for (Spot spot:listaSpotsD) {
                if (spot.getEstado()==2){
                   return listaSpotsD.indexOf(spot);
                }
            }
        }else if(parqueName.equals("ParqueA")){
            for (Spot spot:listaSpotsA) {
                if (spot.getEstado()==2){
                    return listaSpotsA.indexOf(spot);
                }
            }
        }else {
            for (Spot spot:listaSpotsESSLEI) {
                if (spot.getEstado()==2){
                    return listaSpotsESSLEI.indexOf(spot);
                }
            }
        }
        return 0;
    }


    public List<Spot> getListaSpotsA() {
        return listaSpotsA;
    }

    public List<Spot> getListaSpotsESSLEI() {
        return listaSpotsESSLEI;
    }
}
