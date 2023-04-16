package org.grupo.tacs.repos;

import org.grupo.tacs.model.User;

import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuario implements Repository<User>{
    public static RepositorioUsuario instancia = new RepositorioUsuario();

    List<User> usuarios = new ArrayList<>();
    @Override
    public User findById(Long id) {
        return usuarios.stream().filter(user -> user.getId() == id).findFirst().get();
    }

    @Override
    public List<User> findAll() {
        return usuarios;
    }

    @Override
    public void save(User entidad) {
        entidad.setId(usuarios.stream().count());
        usuarios.add(entidad);
    }

    @Override
    public void update(User entidad) {
        User unUser = usuarios.stream().filter(user -> user.getId() == entidad.getId()).findFirst().get();
        unUser.setEmail(entidad.getEmail());
        unUser.setNombre(entidad.getNombre());
        unUser.setPasswordHash(entidad.getPasswordHash());
    }

    @Override
    public void delete(User entidad) {
        usuarios.remove(entidad);
    }
}
