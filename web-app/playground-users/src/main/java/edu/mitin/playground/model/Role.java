package edu.mitin.playground.model;

import java.util.Set;


public enum Role {
    USER(Set.of(Permission.USER_PROFILE));

    private Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }


}
