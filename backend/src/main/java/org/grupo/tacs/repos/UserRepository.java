package org.grupo.tacs.repos;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoException;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.Updates;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.grupo.tacs.MongoDB;
import org.grupo.tacs.excepciones.ThisEmailIsAlreadyInUseException;
import org.grupo.tacs.model.User;

import java.time.LocalDateTime;
import java.util.*;

import static com.mongodb.client.model.Filters.eq;
import static org.grupo.tacs.MongoDB.getMongoClient;

public class UserRepository implements Repository<User>{
    public static UserRepository instance = new UserRepository();

    MongoClient mongoClient;
    private ObjectMapper objectMapper;
    List<User> users = new ArrayList<>();
    @Override
    public User findById(String id) {

        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<User> collection = mongodb.getCollection("Users", User.class);
            ObjectId objectId = new ObjectId(id);
            Bson condition = Filters.eq("_id", objectId);
            FindIterable<User> users = collection.find(condition);
            return users.first();
        }  finally {
            mongoClient.close(); //cerras el cliente
        }
    }

    @Override
    public List<User> findAll() {
        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<User> collection = mongodb.getCollection("Users", User.class);
            return collection.find().into(new ArrayList<>());
        }  finally {
            mongoClient.close(); //cerras el cliente
        }
    }

    @Override
    public void save(User user) {
        mongoClient = MongoDB.getMongoClient();
        System.out.println("tengo client");
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            System.out.println("db");
            MongoCollection<User> collection = mongodb.getCollection("Users", User.class);
            User existingUser = collection.find(Filters.eq("email", user.getEmail())).first();
            if (existingUser != null) {
                throw new ThisEmailIsAlreadyInUseException(user.getEmail());
            }else{
                user.setCreatedDate(LocalDateTime.now());
                collection.insertOne(user);
            }
        } finally {
            mongoClient.close(); //cerras el cliente
        }
    }

    @Override //NO SE USA
    public void update(User entidad) {

        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<User> collection = mongodb.getCollection("Users", User.class);
            Bson condition = Filters.eq("_id", entidad.getId());
            collection.findOneAndUpdate(condition, (Bson) entidad);
        } finally {
            mongoClient.close(); //cerras el cliente
        }

    }

    @Override //NO SE USA
    public void delete(User entidad) {

        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<User> collection = mongodb.getCollection("Users", User.class);
            Bson condition = Filters.eq("_id", entidad.getId());
            collection.deleteOne(condition);
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            mongoClient.close(); //cerras el cliente
        }
    }

    @Override //NO SE USA
    public void deleteAll() {
        users.clear();
    }

    @Override //NO SE USA
    public void deleteById(String id) {

        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<User> collection = mongodb.getCollection("Users", User.class);
            ObjectId objectId = new ObjectId(id);
            Bson condition = Filters.eq("_id", objectId);
            collection.deleteOne(condition);
        } finally {
            mongoClient.close(); //cerras el cliente
        }

        /*
        Optional<User> userToDelete = users.stream()
                .filter(u -> u.getId() == new ObjectId(Long.toHexString(id))
                .findFirst();
        if (userToDelete.isPresent()) {
            users.remove(userToDelete.get());
        } else {
            throw new NoSuchElementException("User with ID " + id + " not found");
        }
        */

    }
}
