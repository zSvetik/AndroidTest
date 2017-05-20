package com.example.user.myapplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 20.05.2017.
 */

public class RepoSingleton {
    private static RepoSingleton instance;
    private Map<String, String> userPass;

    private RepoSingleton() {
        userPass = new HashMap<>();
    }

    public static RepoSingleton getInstance() {
        if (instance == null) {
            instance = new RepoSingleton();
        }
        return instance;
    }

    public void addUser(String email, String password) {
        userPass.put(email, password);
    }

    public boolean isContainUser(String email) {
        return userPass.containsKey(email);
    }

    public String getPassword(String email) {
        return userPass.get(email);
    }
}
