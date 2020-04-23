package ru.itis.models;

public enum ChatRoomType {
    TO_USERS("TO_USERS"), TO_SUPPORT("TO_SUPPORT");

    String val;

    ChatRoomType(String val) {
        this.val = val;
    }

    public String getVal() {
        return val;
    }
}
