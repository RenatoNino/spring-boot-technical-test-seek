package com.renato.pruebatecnica.seek.prueba_tecnica_seek.enums;

public enum UserRole {
    ROLE_ADMIN("Admin");

    private final String alias;

    UserRole(String alias) {
        this.alias = alias;
    }

    public String getAlias() {
        return alias;
    }

    public String getRoleName() {
        return this.name();
    }
}
