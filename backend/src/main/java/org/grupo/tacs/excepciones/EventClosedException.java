package org.grupo.tacs.excepciones;

public class EventClosedException extends RuntimeException{
    public EventClosedException(){super("Vote failed because this Event is closed");}
}
