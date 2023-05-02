package org.grupo.tacs;

import org.grupo.tacs.excepciones.ThisEmailIsAlreadyInUseException;
import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.UserRepository;

import java.time.LocalDateTime;

public class TestDatabaseSeeder {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        run();
    }

    public static void run() {
        saveUser(new User("Ana","Maria","1","a@gmail.com"));
        saveUser(new User("Bob","Ferro","2","b@hotmail.com"));
        saveUser(new User("Celeste","Ailen","3","c@yahoo.com"));
    }
    private static void saveUser(User user){
        try{
            UserRepository.instance.save(user);
        }catch (ThisEmailIsAlreadyInUseException e){
            System.out.println("Ya existe un usuairo con email:"+user.getEmail());
        }
    }
}