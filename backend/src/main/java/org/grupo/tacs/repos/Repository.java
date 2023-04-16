package org.grupo.tacs.repos;

import java.util.List;

public interface Repository<T> {
    T findById(String id);
    List<T> findAll();
    void save(T entidad);
    void update(T entidad);
    void delete(T entidad);
}

