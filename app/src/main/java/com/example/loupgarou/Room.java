package com.example.loupgarou;

import java.util.List;
import java.util.Map;

public class Room {
    private String code;
    private int gameStep;
    private List<User> users;
    private int nbStarts = 0;



    public Room() {
        // Default constructor required for Firebase serialization
    }

    public Room(String code, int gameStep, List<User> users,int nbStarts) {
        this.code = code;
        this.gameStep = gameStep;
        this.users = users;
        this.nbStarts = nbStarts;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getGameStep() {
        return gameStep;
    }

    public void setGameStep(int gameStep) {
        this.gameStep = gameStep;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public int getnbStarts() { return nbStarts;}
    public void setNbStarts(int nbStarts) { this.nbStarts = nbStarts;}
}
