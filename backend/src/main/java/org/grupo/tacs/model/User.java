package org.grupo.tacs.model;

public class User {
    Long id;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
