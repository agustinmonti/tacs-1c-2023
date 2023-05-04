package org.grupo.tacs;

import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.UserRepository;

import java.time.LocalDateTime;

public class TestDatabaseSeeder {
    public static void main(String[] args) {
        System.out.println("Hello world!");
    }

    public void run() {
        UserRepository.instance.save(new User("Ana","Maria","1",true,"a@gmail.com"));
        UserRepository.instance.save(new User("Bob","Ferro","2",true,"b@hotmail.com"));
        UserRepository.instance.save(new User("Celeste","Ailen","3",false,"c@yahoo.com"));
    }
}