package org.grupo.tacs.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import com.sun.org.apache.xpath.internal.operations.Bool;

import java.util.List;

public class Event {
    @BsonId
    Long id;
    @BsonProperty(value = "name")
    String name;
    @BsonProperty(value = "isPublic")
    Boolean isPublic;
    @BsonProperty(value = "createdBy")
    User createdBy;
    @BsonProperty(value = "guests")
    List<User> guests;

    /**
     * El método {@code Event} es el constructor de la clase {@code Event}.
     * @param name Es el nombre de evento de la nueva instancia.
     * @param isPublic Es indica si el evento es público o no.
     * @param createdBy Es el usuario creador del evento.
     * @param guests Es la lista de usuarios invitados al evento.

     */
    public Event(String name, Boolean isPublic, User createdBy, List<User> guests) {
        this.name = name;
        this.isPublic = isPublic;
        this.createdBy = createdBy;
        this.guests = guests;
    }

    public Event() {

    }

    public Long getId() {return id;}

    public String getName() {return name;}

    public Boolean getIsPublic() {return isPublic;}

    public User getCreatedBy() {return createdBy;}

    public List<User> getGuests() {return guests;}

    public Event setId(Long id) {
        this.id = id;
        return this;
    }

    public void setName(String name) {this.name = name;}

    public void setIsPublic(Boolean isPublic) {this.isPublic = isPublic;}

    public void setCreatedBy(User createdBy) {this.createdBy = createdBy;}

    public void setGuests(List<User> guests) {this.guests = guests;}


}
