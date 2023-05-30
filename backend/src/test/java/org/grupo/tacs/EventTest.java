package org.grupo.tacs;

import org.grupo.tacs.excepciones.EventClosedException;
import org.grupo.tacs.model.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class EventTest {
    private User user;
    private Event eventOpen;
    private EventOption optionOne;
    private EventOption optionTwo;

    @Before
    public void inicializar(){
        this.user = new User();
        this.user.setName("Bob");
        this.user.setPassword("Password123");
        this.user.setConfirmPassword("Password123");
        this.eventOpen = new Event("OpenEvent", "An Open Event", new ArrayList<>(), this.user, new ArrayList<>());
        this.optionOne = new EventOption(LocalDateTime.now(), LocalDateTime.now().plusHours(2), new ArrayList<>());
        this.eventOpen.addOption(optionOne);
        this.optionTwo = new EventOption(LocalDateTime.now().plusDays(1), LocalDateTime.now().plusHours(2).plusDays(1), new ArrayList<>());
        this.eventOpen.addOption(optionTwo);
    }
    @Test
    public void voteTestWithOpenEvent(){
        EventOption option = this.eventOpen.getOptionToVoteWithIndex(0);
        option.addVote(new Vote(this.user));
        Assert.assertEquals(1,optionOne.getVotes().size());
        Assert.assertNotEquals(1,optionTwo.getVotes().size());
    }
    @Test(expected = EventClosedException.class)
    public void voteTestWithClosedEvent(){
        this.eventOpen.setIsActive(false);
        this.eventOpen.getOptionToVoteWithIndex(0);
    }
}
