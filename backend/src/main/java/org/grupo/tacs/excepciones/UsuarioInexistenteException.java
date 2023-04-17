package org.grupo.tacs.excepciones;

public class UsuarioInexistenteException extends RuntimeException{
    public UsuarioInexistenteException(){super("No existe el Usuario");}
}
