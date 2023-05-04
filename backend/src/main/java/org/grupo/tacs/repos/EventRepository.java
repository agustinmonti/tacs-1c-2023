package org.grupo.tacs.repos;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
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
import org.grupo.tacs.model.Vote;

import javax.print.Doc;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;


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

    @Override //SOLO SE USA PARA EL ESTADO POR AHORA
    public void update(Event event) {

        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);
            Bson condition = Filters.eq("_id", event.getId());
            //collection.replaceOne(condition,event); //CON ESTO PODES MODIFICARLO COMPLETO
            collection.updateOne(condition,Updates.set("isActive",event.getIsActive())); //CON ESTO UPDATEAS SOLO EL ESTADO
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            mongoClient.close(); //cerras el cliente
        }

        /*
        Event unEvent = events.stream().filter(event -> event.getId() == entidad.getId()).findFirst().get();
        unEvent.setIsPublic(entidad.getIsPublic());
        unEvent.setName(entidad.getName());
        unEvent.setCreatedBy(entidad.getCreatedBy());
        unEvent.setGuests(entidad.getGuests());
        */
    }

    public void updateVote(Event event) { //SE USA PARA AGREGAR O REMOVER UN VOTO DE UNA OPCION
        

    }

    public List<Integer> monitoring() { //FUNCION SOLO DE ADMINS

        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);
            Bson condition = Filters.and(Filters.gte("createdDate", LocalDateTime.now().minusHours(2)),Filters.lt("fieldName", LocalDateTime.now()));
            //List<Event> lista = collection.find().into(new ArrayList<>());
            List<Event> eventList = collection.find(condition).into(new ArrayList<>());
            List<Vote> voteList = new ArrayList<>();
            //SI ALMACENAMOS VOTES NO TENDRIAMOS QUE USAR ESTOS FOR Y EL CONDITION SE PUEDE REUTILIZAR QUEDARIA ESTO MISMO EN 2 LINEAS
            for (Event event : collection.find()) {
                for (EventOption eventOption : event.getOptions()) {
                    LocalDateTime startDate = LocalDateTime.now().minusHours(2);
                    LocalDateTime endDate = LocalDateTime.now();
                    voteList.addAll(eventOption.getVotes().stream().filter(vote -> vote.getVotingDateDate().isAfter(startDate) &&  vote.getVotingDateDate().isBefore(endDate)).collect(Collectors.toList()));
                }
            }
            List<Integer> totalEventsAndVotes = new ArrayList<>();
            totalEventsAndVotes.add(eventList.size());
            totalEventsAndVotes.add(voteList.size());
            return totalEventsAndVotes;
        } finally {
            mongoClient.close(); //cerras el cliente
        }

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
        } catch (MongoException e) {
            e.printStackTrace();
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
