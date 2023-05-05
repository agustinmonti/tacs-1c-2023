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
        UserRepository.instance.save(new User("Ana","Maria","1",true,"a@gmail.com"));
        UserRepository.instance.save(new User("Bob","Ferro","2",true,"b@hotmail.com"));
        UserRepository.instance.save(new User("Celeste","Ailen","3",false,"c@yahoo.com"));
    }
    private static void saveUser(User user){
        try{
            UserRepository.instance.save(user);
        }catch (ThisEmailIsAlreadyInUseException e){
            System.out.println("Ya existe un usuairo con email:"+user.getEmail());
        }
    }
}