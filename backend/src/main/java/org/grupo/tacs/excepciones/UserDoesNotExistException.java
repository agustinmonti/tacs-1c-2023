package org.grupo.tacs.excepciones;

public class UserDoesNotExistException extends RuntimeException{
    public UserDoesNotExistException(){super("No existe el Usuario");}
}
