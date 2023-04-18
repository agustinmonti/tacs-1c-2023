package org.grupo.tacs.model;

public class User {
    Long id;
    String name;
    String email;
    String passwordHash;

    /**
     * El método {@code Usuario} es el constructor de la clase {@code Usuario}.
     * @param name Es el nombre de usuario de la nueva instancia.
     * @param passwordHash Es el hashcode del password de la nueva instancia.
     * @param email Es el tipo de usuario que será la nueva instancia.
     */
    public User(String name, String passwordHash, String email) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public String getName() {
        return name;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
