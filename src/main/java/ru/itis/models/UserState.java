package ru.itis.models;

import lombok.Getter;

public enum UserState {
    CONFIRMED("CONFIRMED"), NOT_CONFIRMED("NOT_CONFIRMED");

    @Getter
    private String value;

    UserState(String value) {
        this.value = value;
    }


}

