package org.grupo.tacs.model;

import java.time.LocalDateTime;

public class Vote {
    User user;
    LocalDateTime votingDate;
    //String readAbleVotingDate;
    //Long optionParentID;

    public Vote(/*Long idOpcion,*/ User votante){
        this.user = votante;
        //this.optionParentID = idOpcion;
        this.votingDate = LocalDateTime.now();
        //this.readAbleVotingDate= Helper.getReadableDate(this.votingDate);
    }

    public Vote(){

    }

    public User getUser() {return user;}

    public void setUser(User user) {this.user = user;}

    public LocalDateTime getVotingDateDate() {return votingDate;}

    public void setVotingDateDate(LocalDateTime votingDate) {this.votingDate = votingDate;}
}
