package com.lawyerhub.enums;

public enum UserRole {
    LAWYER,
    USER;

    public static UserRole toMyEnum (String myEnumString) {
        try {
            return valueOf(myEnumString);
        } catch (Exception ex) {
            // For error cases
            return USER;
        }
    }
}
