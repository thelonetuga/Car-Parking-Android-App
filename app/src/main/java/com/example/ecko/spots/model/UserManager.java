package com.example.ecko.spots.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;

public enum UserManager {
    INSTANCE;

    private LinkedList<User> users;
    final ArrayList<Boolean> hasSpot = new ArrayList<>();

    private long numeroUsers;

    public long getNumeroUsers() {
        return numeroUsers;
    }

    public void setNumeroUsers(long numeroUsers) {
        this.numeroUsers = numeroUsers;
    }

    // ...

    UserManager() {
        this.users = new LinkedList<>();
    }


    public LinkedList<User> getUsers() {
        return users;
    }

    public User getUser(int userPosition) {
        return users.get(userPosition);
    }

    public void setUsers(LinkedList<User> users) {
        this.users = users;
    }

    public boolean isAutenticado(User user){
        if(user.getAutenticado()){
            return true;
        }
        return false;
    }

    public boolean hasSpot(){
        String bolToString = hasSpot.get(0).toString();
        System.out.println(bolToString);
        if (bolToString.equals("true")){
            hasSpot.clear();
            return true;
        }
        hasSpot.clear();
        return false;
    }

    public void verifyDatabaseIfHasSpot() {
        final FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase.getInstance().getReference("Users/" + userAuth.getUid() + "/MySpot").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    hasSpot.clear();
                    hasSpot.add(true);
                }else {
                    hasSpot.clear();
                    hasSpot.add(false);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
