package org.grupo.tacs.excepciones;

public class WrongPasswordException extends RuntimeException{
    public WrongPasswordException(){super("Password Incorrecta");}
}
