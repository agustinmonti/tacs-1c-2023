package org.grupo.tacs.excepciones;

public class ThisEmailIsAlreadyInUseException extends RuntimeException{
    public ThisEmailIsAlreadyInUseException(String email){super("This email: " + email + " is already in use");}
}
