package org.grupo.tacs.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class User {
    @BsonProperty("id")
    ObjectId _id;
    @BsonProperty(value = "name")
    String name;
    @BsonProperty(value = "lastname")
    String lastname;
    @BsonProperty(value = "email")
    String email;
    @BsonProperty(value = "password")
    String password;
    @BsonProperty(value = "isAdmin")
    Boolean isAdmin;
    @BsonProperty(value = "createdDate")
    LocalDateTime createdDate;


    /**
     * El método {@code Usuario} es el constructor de la clase {@code Usuario}.
     * @param name Es el nombre de usuario de la nueva instancia.
     * @param password Es el hashcode del password de la nueva instancia.
     * @param email Es el tipo de usuario que será la nueva instancia.
     */

    public User (String name, String lastname, String password, Boolean isAdmin, String email) {
        this.name = name;
        this.lastname = lastname;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
        this.createdDate = LocalDateTime.now();
    }

    public User() {
    }

    public ObjectId getId() {
        return this._id;
    }

    public void setId(ObjectId id) {
        this._id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return this.lastname;
    }

    public void setLastName(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setIsAdmin(Boolean isAdmin) {this.isAdmin = isAdmin;}

    public Boolean getIsAdmin() {return isAdmin;}

    public LocalDateTime getCreatedDate() {return createdDate;}

    public void setCreatedDate(LocalDateTime createdDate) {this.createdDate = createdDate;}
}
