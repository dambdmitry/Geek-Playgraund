package edu.mitin.playground.model;

public enum Permission {
    USER_PROFILE("profile"), //Взаимодействия пользователя со своим профилем (просмотр изменение)
    VIEW("");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
