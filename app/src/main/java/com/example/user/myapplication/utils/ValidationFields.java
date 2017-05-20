package com.example.user.myapplication.utils;

/**
 * Created by User on 20.05.2017.
 */

public class ValidationFields {
    private ValidationFields(){}

    public static boolean isValidEmail(String email) {
        return email.matches(".{2,}@.+\\..+");
    }

    public static boolean isValidPassword(String password) {
        return password.matches(".{4,}");
    }
}
