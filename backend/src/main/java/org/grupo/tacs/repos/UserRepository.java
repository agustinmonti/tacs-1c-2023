package org.grupo.tacs.repos;

import org.grupo.tacs.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class UserRepository implements Repository<User>{
    public static UserRepository instance = new UserRepository();

    List<User> users = new ArrayList<>();
    @Override
    public User findById(Long id) {
        return users.stream().filter(user -> user.getId() == id).findFirst().get();
    }

    @Override
    public List<User> findAll() {
        return users;
    }

    @Override
    public void save(User entidad) {
        if(entidad.getId()==null){
            entidad.setId(users.stream().count());
            users.add(entidad);
        }
    }

    @Override
    public void update(User entidad) {
        User unUser = users.stream().filter(user -> user.getId() == entidad.getId()).findFirst().get();
        unUser.setEmail(entidad.getEmail());
        unUser.setName(entidad.getName());
        unUser.setPasswordHash(entidad.getPasswordHash());
    }

    @Override
    public void delete(User entidad) {
        users.remove(entidad);
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public void deleteById(long l) {
        Optional<User> userToDelete = users.stream()
                .filter(u -> u.getId() == l)
                .findFirst();
        if (userToDelete.isPresent()) {
            users.remove(userToDelete.get());
        } else {
            throw new NoSuchElementException("User with ID " + l + " not found");
        }
    }
}
