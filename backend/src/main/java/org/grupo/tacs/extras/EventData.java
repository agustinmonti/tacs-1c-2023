package org.grupo.tacs.extras;

import com.google.gson.annotations.Expose;
import org.grupo.tacs.model.Event;
import org.grupo.tacs.model.EventOption;
import org.grupo.tacs.model.User;
import org.grupo.tacs.model.Vote;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class UserData{
    @Expose(serialize = true)
    String email;
    @Expose(serialize = true)
    String id;
    public UserData(String s, String hexString) {
        this.id = hexString;
        this.email = s;
    }
}
class EventOptionData {
    //String id;
    @Expose(serialize = true)
    LocalDateTime start;
    @Expose(serialize = true)
    LocalDateTime end;
    @Expose(serialize = true)
    int votes;
    @Expose(serialize = false)
    List <Vote> votesCollection;
    public EventOptionData(EventOption o) {
        //this.id=o.getId().toHexString(); Quedamos en usar la posicion como indice.
        this.start=o.getStart();
        this.end=o.getEnd();
        this.votes=o.getVotes().size();
        this.votesCollection = o.getVotes();
    }

    public int getVotes() {
        return votes;
    }

    public boolean voted(User user) {
        return this.votesCollection.stream().anyMatch(vote -> vote.getUser().getId().equals(user.getId()));
    }
}
public class EventData{
    @Expose(serialize = true)
    String id;
    @Expose(serialize = true)
    String name;
    @Expose(serialize = true)
    String description;
    @Expose(serialize = true)
    List<EventOptionData> options;
    @Expose(serialize = true)
    List<UserData> participants;
    // participants: [ { id, email } ]
    @Expose(serialize = true)
    String status;
    @Expose(serialize = true)
    LocalDateTime createdDate;
    @Expose(serialize = true)
    UserData owner;
    public EventData(Event event) {
        this.id=event.getId().toHexString();
        this.name=event.getName();
        this.description = event.getDesc();
        this.status = "Cerrado";
        if(event.getIsActive())
            this.status = "Active";
        this.participants = transformParticipants(event.getParticipants());
        this.owner = new UserData(event.getCreatedBy().getEmail(),event.getCreatedBy().getId().toHexString());
        this.createdDate=event.getCreatedDate();
        this.options=transformEventOptions(event.getOptions());
    }

    private List<UserData> transformParticipants(List<User> participants) {
        List<UserData> myParticipants = new ArrayList<>();
        participants.forEach(p->myParticipants.add(new UserData(p.getEmail(),p.getId().toHexString())));
        return myParticipants;
    }

    private List<EventOptionData> transformEventOptions(List<EventOption> options){
        List<EventOptionData> result=new ArrayList<>();
        options.forEach(o->result.add(new EventOptionData(o)));
        return result;
    }

    public List<EventOptionData> getOptions() {
        return options;
    }

    public List<Integer> getVotados(User user){
        List<Integer> votados = new ArrayList<>();
        int index = -1;
        for (int i = 0; i < this.options.size(); i++) {
            EventOptionData option = this.options.get(i);
            if(option.votes > 0 && option.voted(user))
                votados.add(i);
        }
        return votados;
    }
}