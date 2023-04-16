package org.grupo.tacs;

import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.RepositorioUsuario;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public void run() {
        RepositorioUsuario.instancia.save(new User("Ana","1","a@gmail.com"));
        RepositorioUsuario.instancia.save(new User("Bob","2","b@hotmail.com"));
        RepositorioUsuario.instancia.save(new User("Celeste","3","c@yahoo.com"));
    }
}