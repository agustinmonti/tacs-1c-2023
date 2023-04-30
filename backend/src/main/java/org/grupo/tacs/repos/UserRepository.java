package org.grupo.tacs.repos;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.grupo.tacs.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.grupo.tacs.MongoDB.mongodb;

public class UserRepository implements Repository<User>{
    public static UserRepository instance = new UserRepository();

    List<User> users = new ArrayList<>();
    @Override
    public User findById(Long id) {

        MongoCollection<User> collection = mongodb().getCollection("Users", User.class);
        Bson condition = Filters.eq("_id", id);
        FindIterable<User> users = collection.find(condition);
        return users.first();

        //return users.stream().filter(user -> user.getId() == id).findFirst().get();
    }

    @Override
    public List<User> findAll() {
        MongoCollection<User> collection = mongodb().getCollection("Users", User.class);
        return collection.find().into(new ArrayList<>());
    }

    @Override
    public void save(User entidad) {
        if(entidad.getId()==null){
        //    entidad.setId(users.stream().count());
            users.add(entidad);
        }
    }

    public void insert(User user) {
        MongoCollection<User> collection = mongodb().getCollection("Users", User.class);
        //User pepe = new User("pepe", "pepe@gmail.com", "pepeHash").setId(2L);
        collection.insertOne(user);
    }

    @Override
    public void update(User entidad) {
        MongoCollection<User> collection = mongodb().getCollection("Users", User.class);

        Bson condition = Filters.eq("_id", entidad.getId());
        collection.findOneAndUpdate(condition, Updates.set("name","jorge"));

        //User unUser = users.stream().filter(user -> user.getId() == entidad.getId()).findFirst().get();
        //unUser.setEmail(entidad.getEmail());
        //unUser.setName(entidad.getName());
        //unUser.setPasswordHash(entidad.getPasswordHash());
    }

    @Override
    public void delete(User entidad) {

        MongoCollection<User> collection = mongodb().getCollection("Users", User.class);
        Bson condition = Filters.eq("_id", entidad.getId());

        try {
            collection.deleteOne(condition);
        } catch (MongoException e) {
                e.printStackTrace();
        }
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public void deleteById(long id) {

        MongoCollection<User> collection = mongodb().getCollection("Users", User.class);
        Bson condition = Filters.eq("_id", id);

        try {
            collection.deleteOne(condition);
        } catch (MongoException e) {
            e.printStackTrace();
        }


        /*Optional<User> userToDelete = users.stream()
                .filter(u -> u.getId() == l)
                .findFirst();
        if (userToDelete.isPresent()) {
            users.remove(userToDelete.get());
        } else {
            throw new NoSuchElementException("User with ID " + l + " not found");
        }*/
    }
}
