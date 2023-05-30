package org.grupo.tacs.excepciones;

public class AlreadyVotedException extends RuntimeException{
    public AlreadyVotedException(){super("User already voted this EventOption");}
}
