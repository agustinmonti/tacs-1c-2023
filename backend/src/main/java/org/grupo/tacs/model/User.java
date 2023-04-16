package org.grupo.tacs.model;

public class User {
    String nombre;
    String email;
    String passwordHash;

    /**
     * El método {@code Usuario} es el constructor de la clase {@code Usuario}.
     * @param nombre Es el nombre de usuario de la nueva instancia.
     * @param passwordHash Es el hashcode del password de la nueva instancia.
     * @param email Es el tipo de usuario que será la nueva instancia.
     */
    public User(String nombre, String passwordHash, String email) {
        this.nombre = nombre;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}
