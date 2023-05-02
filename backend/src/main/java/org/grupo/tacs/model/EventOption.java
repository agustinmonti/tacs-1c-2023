package org.grupo.tacs.model;

import org.grupo.tacs.extras.Helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EventOption {
    Long id;
    //Long eventOptionParentId;
    LocalDateTime start;
    //String readAbleStart;
    LocalDateTime end;
    //String readAbleEnd;
    List<Vote> votes;

    public EventOption(/*Long idPadre,*/ LocalDateTime start, LocalDateTime end, List<Vote> votes){
        //this.eventOptionParentId = idPadre;
        this.start = start;
        this.end = end;
        this.votes = votes;
        //this.readAbleStart= Helper.getReadableDate(start);
        //this.readAbleEnd=Helper.getReadableDate(end);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
        //this.readAbleStart = Helper.getReadableDate(start);
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
        //this.readAbleEnd = Helper.getReadableDate(end);
    }

    /*public Long getEventOptionParentId() {
        return eventOptionParentId;
    }

    public String getReadAbleEnd() {
        return readAbleEnd;
    }

    public String getReadAbleStart() {
        return readAbleStart;
    }*/

    public List<Vote> getVotes() {return votes;}

    public void setVotes(List<Vote> votes) {this.votes = votes;}
}
