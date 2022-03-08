package edu.mitin.playground.users.model;

public enum Permission {
    USER_PROFILE("profile"), //Взаимодействия пользователя со своим профилем (просмотр изменение)
    PLAYER_PROFILE("player"),
    ADMIN_PROFILE("ADMIN"),
    ORGANIZER_PROFILE("ORGANIZER");

    private final String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }
}
