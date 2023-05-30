package org.grupo.tacs.model;

import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.types.ObjectId;
import org.grupo.tacs.excepciones.EventClosedException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event {
    @BsonProperty("id")
    ObjectId _id;
    @BsonProperty(value = "name")
    String name;
    @BsonProperty(value = "desc")
    String desc;
    @BsonProperty(value = "isActive")
    Boolean isActive;
    @BsonProperty(value = "createdDate")
    LocalDateTime createdDate;
    @BsonProperty(value = "createdBy")
    ObjectId createdBy;
    @BsonProperty(value = "options")
    List<EventOption> options;
    @BsonProperty(value = "participants")
    List<User> participants;

    /**
     * El método {@code Event} es el constructor de la clase {@code Event}.
     * @param name Es el nombre de evento de la nueva instancia.
     * @param createdBy Es el usuario creador del evento.

     */
    public Event(String name, String desc, List<EventOption> options, ObjectId createdBy, List<User> participants) {
        this.name = name;
        this.desc = desc;
        this.isActive = true;
        this.options = options;
        this.createdDate = LocalDateTime.now();
        this.createdBy = createdBy;
        this.participants = participants;
    }

    public Event() {

    }

    public ObjectId getId() {return _id;}

    public String getName() {return name;}

    public String getDesc() {return desc;}

    public Boolean getIsActive() {return isActive;}

    public LocalDateTime getCreatedDate() {return createdDate;}

    public ObjectId getCreatedBy() {return createdBy;}

    public List<EventOption> getOptions() {return options;}
    public List<User> getParticipants() {return participants;}

    public Event setId(ObjectId id) {
        this._id = id;
        return this;
    }

    public void setName(String name) {this.name = name;}

    public void setDesc(String desc) {this.desc = desc;}

    public void setIsActive(Boolean isActive) {this.isActive = isActive;}

    public void setCreatedDate(LocalDateTime createdDate) {this.createdDate = createdDate;}

    public void setCreatedBy(ObjectId createdBy) {this.createdBy = createdBy;}

    public void setOptions(List<EventOption> options) {this.options = options;}

    public void addOption(EventOption option) {
        if(this.options==null)
            this.options= new ArrayList<>();
        this.options.add(option);
    }

    public EventOption getOption(EventOption option) {
        for(EventOption eventOption : options) {
            if(eventOption.getId().equals(option.getId())){
                return eventOption;
            }
        }
        return null;
    }
    public EventOption getOptionToVoteWithIndex(Integer optionIndex){
        if(!this.getIsActive())
            throw new EventClosedException();
        return this.options.get(optionIndex);
    }
    public void setParticipants(List<User> participants) {this.participants = participants;}


}
