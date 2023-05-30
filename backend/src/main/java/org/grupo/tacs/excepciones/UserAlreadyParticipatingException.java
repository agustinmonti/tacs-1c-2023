package org.grupo.tacs.excepciones;

public class UserAlreadyParticipatingException extends RuntimeException {
    public UserAlreadyParticipatingException(){super("User is already a participant");}
}
