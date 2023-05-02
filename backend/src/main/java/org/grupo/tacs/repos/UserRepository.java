package org.grupo.tacs.repos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.grupo.tacs.MongoDB;
import org.grupo.tacs.model.User;

import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static org.grupo.tacs.MongoDB.getMongoClient;

public class UserRepository implements Repository<User>{
    public static UserRepository instance = new UserRepository();

    MongoClient mongoClient;
    private ObjectMapper objectMapper;
    List<User> users = new ArrayList<>();
    @Override
    public User findById(Long id) {
        List<User> allUsers = this.findAll();
        if(id<0 || id >= allUsers.stream().count()) {
            return null;
        }
        return allUsers.get(Math.toIntExact(id));
        //return users.stream().filter(user -> user.getId() == id).findFirst().get();
    }

    @Override
    public List<User> findAll() {
        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<User> collection = mongodb.getCollection("Users", User.class);
            List<User> userList = new ArrayList<>();
            for (User user : collection.find()) {
                userList.add(user);
            }
            return userList;
        } finally {
            mongoClient.close(); //cerras el cliente
        }
    }

    @Override
    public void save(User entidad) {
        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");

            // Usas el repo aca
            MongoCollection<User> collection = mongodb.getCollection("Users", User.class);
            User existingUser = collection.find(Filters.eq("email", entidad.getEmail())).first();
            if (existingUser != null) {
                throw new RuntimeException("User with email " + entidad.getEmail() + " already exists");
            }
            collection.insertOne(entidad);
        } finally {
            mongoClient.close(); //cerras el cliente
        }
    }

    public void insert(User user) {
        /*MongoCollection<User> collection = mongodb().getCollection("Users", User.class);
        //User pepe = new User("pepe", "pepe@gmail.com", "pepeHash").setId(2L);
        collection.insertOne(user);*/
    }

    @Override
    public void update(User entidad) {
        /*MongoCollection<User> collection = mongodb().getCollection("Users", User.class);

        Bson condition = Filters.eq("_id", entidad.getId());
        collection.findOneAndUpdate(condition, Updates.set("name","jorge"));*/

        User unUser = users.stream().filter(user -> user.getId() == entidad.getId()).findFirst().get();
        unUser.setEmail(entidad.getEmail());
        unUser.setName(entidad.getName());
        unUser.setPasswordHash(entidad.getPasswordHash());
    }

    @Override
    public void delete(User entidad) {

        /*MongoCollection<User> collection = mongodb().getCollection("Users", User.class);
        Bson condition = Filters.eq("_id", entidad.getId());

        try {
            collection.deleteOne(condition);
        } catch (MongoException e) {
                e.printStackTrace();
        }*/
    }

    @Override
    public void deleteAll() {
        users.clear();
    }

    @Override
    public void deleteById(long id) {

        /*MongoCollection<User> collection = mongodb().getCollection("Users", User.class);
        Bson condition = Filters.eq("_id", id);

        try {
            collection.deleteOne(condition);
        } catch (MongoException e) {
            e.printStackTrace();
        }*/


        Optional<User> userToDelete = users.stream()
                .filter(u -> u.getId() == id/*new ObjectId(Long.toHexString(id))*/)
                .findFirst();
        if (userToDelete.isPresent()) {
            users.remove(userToDelete.get());
        } else {
            throw new NoSuchElementException("User with ID " + id + " not found");
        }
    }
}
