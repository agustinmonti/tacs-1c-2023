package org.grupo.tacs.repos;

import com.mongodb.client.FindIterable;

import java.util.List;

public interface Repository<T> {
    T findById(Long id);
    List<T> findAll();
    void save(T entidad);
    void update(T entidad);
    void delete(T entidad);

    void deleteAll();

    void deleteById(long l);
}

