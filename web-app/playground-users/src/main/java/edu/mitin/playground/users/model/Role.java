package edu.mitin.playground.users.model;

import java.util.Set;


public enum Role {
    USER(Set.of(Permission.USER_PROFILE)),
    PLAYER(Set.of(Permission.USER_PROFILE, Permission.PLAYER_PROFILE)),
    ADMIN(Set.of(Permission.USER_PROFILE, Permission.PLAYER_PROFILE, Permission.ORGANIZER_PROFILE, Permission.ADMIN_PROFILE)),
    ORGANIZER(Set.of(Permission.USER_PROFILE, Permission.ORGANIZER_PROFILE));

    private Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }


}
