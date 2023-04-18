package org.grupo.tacs.model;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class Event {
    Long id;
    String name;

    Boolean isPublic;
    User createdBy;

    User guests [];

    /**
     * El método {@code Event} es el constructor de la clase {@code Event}.
     * @param name Es el nombre de evento de la nueva instancia.
     * @param isPublic Es indica si el evento es público o no.
     * @param createdBy Es el usuario creador del evento.
     * @param guests Es la lista de usuarios invitados al evento.

     */
    public Event(String name, Boolean isPublic, User createdBy, User guests []) {
        this.name = name;
        this.isPublic = isPublic;
        this.createdBy = createdBy;
        this.guests = guests;
    }

    public Long getId() {return id;}

    public String getName() {return name;}

    public Boolean getIsPublic() {return isPublic;}

    public User getCreatedBy() {return createdBy;}

    public User[] getGuests() {return guests;}

    public void setId(Long id) {this.id = id;}

    public void setName(String name) {this.name = name;}

    public void setIsPublic(Boolean isPublic) {this.isPublic = isPublic;}

    public void setCreatedBy(User createdBy) {this.createdBy = createdBy;}

    public void setGuests(User[] guests) {this.guests = guests;}


}
