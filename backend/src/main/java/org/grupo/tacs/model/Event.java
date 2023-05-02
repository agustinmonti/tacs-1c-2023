package org.grupo.tacs.model;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.util.List;

public class Event {
    @BsonProperty("id")
    ObjectId id;
    @BsonProperty(value = "name")
    String name;
    @BsonProperty(value = "desc")
    String desc;
    @BsonProperty(value = "isActive")
    Boolean isActive;
    @BsonProperty(value = "createdDate")
    LocalDateTime createdDate;
    @BsonProperty(value = "createdBy")
    User createdBy;
    @BsonProperty(value = "options")
    List<EventOption> options;
    @BsonProperty(value = "participants")
    List<User> participants;

    /**
     * El método {@code Event} es el constructor de la clase {@code Event}.
     * @param name Es el nombre de evento de la nueva instancia.
     * @param createdBy Es el usuario creador del evento.

     */
    public Event(String name, String desc, Boolean isActive, List<EventOption> options, User createdBy, List<User> participants) {
        this.name = name;
        this.desc = desc;
        this.isActive = isActive;
        this.options = options;
        this.createdDate = LocalDateTime.now();
        this.createdBy = createdBy;
        this.participants = participants;
    }

    public Event() {

    }

    public ObjectId getId() {return id;}

    public String getName() {return name;}

    public String getDesc() {return desc;}

    public Boolean getIsActive() {return isActive;}

    public LocalDateTime getCreatedDate() {return createdDate;}

    public User getCreatedBy() {return createdBy;}

    public List<EventOption> getOptions() {return options;}
    public List<User> getParticipants() {return participants;}

    public Event setId(ObjectId id) {
        this.id = id;
        return this;
    }

    public void setName(String name) {this.name = name;}

    public void setDesc(String desc) {this.desc = desc;}

    public void setIsActive(Boolean isActive) {this.isActive = isActive;}

    public void setCreatedDate(LocalDateTime createdDate) {this.createdDate = createdDate;}

    public void setCreatedBy(User createdBy) {this.createdBy = createdBy;}

    public void setOptions(List<EventOption> options) {this.options = options;}

    public void setParticipants(List<User> participants) {this.participants = participants;}


}
