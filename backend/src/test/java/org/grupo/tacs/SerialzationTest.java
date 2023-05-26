package org.grupo.tacs;

import org.grupo.tacs.model.Interaction;
import org.grupo.tacs.model.InteractionMethod;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class SerialzationTest {
    @Before
    public void inicializar(){}
    @Test
    public void urlPatternTest(){
        String url = "http://localhost:8080/v2/events/6470ae9b8e2f821d5939bb88";
        Interaction interaction = new Interaction(InteractionMethod.GET,url,"GET EVENT",200);
        Assert.assertEquals("/v2/events/:id",interaction.getUrlPattern(url));

        url = "http://localhost:8080/v2/events/6470ae9b8e2f821d5939bb88/participant";
        Assert.assertEquals("/v2/events/:id/participant",interaction.getUrlPattern(url));

        url = "http://localhost:8080/v2/events/6470ae9b8e2f821d5939bb88/vote";
        Assert.assertEquals("/v2/events/:id/vote",interaction.getUrlPattern(url));
    }
    @Test
    public void urlPatternWithQueryParamsTest(){
        String url = "http://localhost:8080/v2/events?userId=6470ae658e2f821d5939bb84";
        Interaction interaction = new Interaction(InteractionMethod.GET,url,"Get events from user id",200);
        Assert.assertEquals("/v2/events?userId=UserId",interaction.getUrlPattern(url));
    }
}
