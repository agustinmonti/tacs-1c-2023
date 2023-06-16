package org.grupo.tacs.model;

import org.bson.types.ObjectId;

import java.time.LocalDateTime;

public class Vote {
    LocalDateTime votingDate;
    ObjectId userId;
    //String readAbleVotingDate;
    //Long optionParentID;

    public Vote(ObjectId userId){
        this.userId = userId;
        //this.optionParentID = idOpcion;
        this.votingDate = LocalDateTime.now();
        //this.readAbleVotingDate= Helper.getReadableDate(this.votingDate);
    }

    public Vote(User user){
        this.userId = user.getId();
    }
    public Vote(){ }

    public ObjectId getUserId() {return userId;}

    public void setUserId(ObjectId userId) {this.userId = userId;}

    public LocalDateTime getVotingDateDate() {return votingDate;}

    public void setVotingDateDate(LocalDateTime votingDate) {this.votingDate = votingDate;}
}
