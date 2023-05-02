package org.grupo.tacs.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

public class User {
    @BsonProperty("id")
    Long id;
    @BsonProperty(value = "name")
    String name;
    @BsonProperty(value = "email")
    String email;
    @BsonProperty(value = "passwordHash")
    String passwordHash;

    /**
     * El método {@code Usuario} es el constructor de la clase {@code Usuario}.
     * @param name Es el nombre de usuario de la nueva instancia.
     * @param passwordHash Es el hashcode del password de la nueva instancia.
     * @param email Es el tipo de usuario que será la nueva instancia.
     */

    public User (String name, String passwordHash, String email) {
        this.name = name;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    public User() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return this.passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
