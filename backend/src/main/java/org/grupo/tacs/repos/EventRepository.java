package org.grupo.tacs.repos;

import com.google.gson.Gson;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.grupo.tacs.MongoDB;
import org.grupo.tacs.model.Event;
import org.grupo.tacs.model.EventOption;
import org.grupo.tacs.model.User;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


public class EventRepository implements Repository<Event>{
    public static EventRepository instance = new EventRepository();
    MongoClient mongoClient;
    List<Event> events = new ArrayList<>();
    @Override
    public Event findById(String id) {

        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);
            ObjectId objectId = new ObjectId(id);
            Bson condition = Filters.eq("_id", objectId);
            FindIterable<Event> events = collection.find(condition);
            return events.first();
        }  finally {
            mongoClient.close(); //cerras el cliente
        }

        //return events.stream().filter(event -> event.getId() == id).findFirst().get();
    }

    @Override
    public List<Event> findAll() {
        /*MongoCollection<Event> collection = mongodb().getCollection("Events", Event.class);
        return collection.find().into(new ArrayList<>());*/

        return events;
    }

    @Override
    public void save(Event event) {

        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);

            //POR SI QUERES HARDCODEARLO
            //MongoCollection<User> users_collection = mongodb.getCollection("Users", User.class);
            //List<User> users = users_collection.find().into(new ArrayList<>()); //SI USERS ESTA VACIO ROMPE, PROCURA QUE HAYA USERS PARA HACER ESTO
            //List<EventOption> options = new ArrayList<>();
            //Event event1 = new Event("Marcha", "Aumento de salario", true,options, users.get(0), users);

            event.setCreatedDate(LocalDateTime.now());
            collection.insertOne(event);
        } finally {
            mongoClient.close(); //cerras el cliente
        }

        //event.setId(events.stream().count());
        //events.add(event);
    }

    @Override //POR AHORA SOLO UPDATEA EL ESTADO
    public void update(Event event) {

        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);
            Bson condition = Filters.eq("_id", event.getId());
            collection.updateOne(condition,Updates.set("isActive",event.getIsActive()));
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            mongoClient.close(); //cerras el cliente
        }


        /*MongoCollection<Event> collection = mongodb().getCollection("Events", Event.class);

        Bson condition = Filters.eq("_id", entidad.getId());
        collection.findOneAndUpdate(condition, Updates.set("name","Huelga"));*/

        /*
        Event unEvent = events.stream().filter(event -> event.getId() == entidad.getId()).findFirst().get();
        unEvent.setIsPublic(entidad.getIsPublic());
        unEvent.setName(entidad.getName());
        unEvent.setCreatedBy(entidad.getCreatedBy());
        unEvent.setGuests(entidad.getGuests());
        */
    }

    @Override
    public void delete(Event entidad) {

        /*MongoCollection<Event> collection = mongodb().getCollection("Events", Event.class);
        Bson condition = Filters.eq("_id", entidad.getId());

        try {
            collection.deleteOne(condition);
        } catch (MongoException e) {
            e.printStackTrace();
        }*/

        events.remove(entidad);
    }

    @Override
    public void deleteAll() {
        events.clear();
    }

    @Override
    public void deleteById(String id) {

        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);
            ObjectId objectId = new ObjectId(id);
            Bson condition = Filters.eq("_id", objectId);
            collection.deleteOne(condition);
        } finally {
            mongoClient.close(); //cerras el cliente
        }

        /*
        Optional<Event> eventToDelete = events.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
        if (eventToDelete.isPresent()) {
            events.remove(eventToDelete.get());
        } else {
            throw new NoSuchElementException("Event with ID " + id + " not found");
        }
        */
    }
}
