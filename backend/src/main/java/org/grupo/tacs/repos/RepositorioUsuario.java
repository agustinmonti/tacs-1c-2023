package org.grupo.tacs.repos;

import org.grupo.tacs.model.User;

import java.util.ArrayList;
import java.util.List;

public class RepositorioUsuario implements Repository<User>{
    public static RepositorioUsuario instancia = new RepositorioUsuario();

    List<User> usuarios = new ArrayList<>();
    @Override
    public User findById(String id) {
        return null;
    }

    @Override
    public List<User> findAll() {
        return usuarios;
    }

    @Override
    public void save(User entidad) {
        usuarios.add(entidad);
    }

    @Override
    public void update(User entidad) {
        //deberia buscar por id y no por nombre, de todas formas esto es un mock.
        usuarios.stream().filter(user -> user.getNombre().equals(entidad.getNombre()));
    }

    @Override
    public void delete(User entidad) {
        usuarios.remove(entidad);
    }
}
