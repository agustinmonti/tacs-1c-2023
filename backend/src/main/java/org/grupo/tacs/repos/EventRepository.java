package org.grupo.tacs.repos;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
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
import java.util.*;
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

    public void updateVote(Event event, EventOption eventOption, User user) { //SE USA PARA AGREGAR O REMOVER UN VOTO DE UNA OPCION

        mongoClient = MongoDB.getMongoClient();
        try {

            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);

            /*
            Bson conditionExistOption = Filters.and(Filters.eq("_id", event.getId()), Filters.elemMatch("options", Filters.eq("_id", eventOption.getId())));
            if (collection.find(conditionExistOption).first() != null){
                Bson conditionExistVoteOfUserInOption = Filters.and(Filters.eq("_id", event.getId()), Filters.elemMatch("options", Filters.and(Filters.eq("_id", eventOption.getId()), Filters.elemMatch("votes", Filters.eq("user._id", user.getId())))));
                if (collection.find(conditionExistVoteOfUserInOption).first() != null){
                    Bson update = Updates.pull("options.$.votes", new Vote(user));
                    collection.updateOne(conditionExistVoteOfUserInOption,update);
                } else{
                    Bson update = Updates.push("options.$.votes", new Vote(user));
                    collection.updateOne(conditionExistVoteOfUserInOption,update);
                }
            } else{
                System.out.println("El evento no posee esa opcion");
            }
            */

            EventOption option = event.getOptions().get(eventOption.getId().intValue());
            if (option == null){
                throw new NoSuchElementException();
            }
            List<Vote> votes = option.getVotes().stream().filter(vote -> Objects.equals(vote.getUser().getId(), user.getId())).collect(Collectors.toList());
            Bson condition = Filters.eq("_id", event.getId());
            if (votes.isEmpty()){
                UpdateOptions options = new UpdateOptions().arrayFilters(Arrays.asList(Filters.eq("filter._id", option.getId())));
                collection.updateOne(condition,Updates.push("options.$[filter].votes", new Vote(user)),options);
            } else{
                UpdateOptions options = new UpdateOptions().arrayFilters(Arrays.asList(Filters.eq("filter._id", option.getId())));
                collection.updateOne(condition,Updates.pull("options.$[filter].votes", votes.get(0)),options);
            }


        } catch (MongoException e) {
            e.printStackTrace();
        } catch(Exception e){
            e.printStackTrace();
        } finally{
            mongoClient.close(); //cerras el cliente
        }
    }

    public void updateParticipant(Event event, User user) { //SE USA PARA AGREGAR O REMOVER UN PARTICIPANTE DEL EVENTO

        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);
            List<User> participants = event.getParticipants().stream().filter(participant -> Objects.equals(participant.getId(), user.getId())).collect(Collectors.toList());
            Bson condition = Filters.eq("_id", event.getId());
            if(participants.isEmpty()){
                collection.updateOne(condition,Updates.push("participants", user));
            } else{
                collection.updateOne(condition,Updates.pull("participants", user));
            }
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            mongoClient.close(); //cerras el cliente
        }
    }

    public List<Integer> monitoring() { //FUNCION SOLO DE ADMINS

        mongoClient = MongoDB.getMongoClient();
        /* //ESTO LO HICE PARA PROBAR LOS FILTER DE UPDATEVOTE()
        try{
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);
            Bson conditionExistOption = Filters.and(Filters.eq("_id", new ObjectId("6453f46e8151ab556a37db12")), Filters.elemMatch("options", Filters.eq("_id", 2L)));
            Bson conditionExistVoteOfUserInOption = Filters.and(Filters.eq("_id", new ObjectId("6453f46e8151ab556a37db12")), Filters.elemMatch("options", Filters.and(Filters.eq("_id", 2L), Filters.elemMatch("votes", Filters.eq("user.name", "pepe")))));
            for (Event event : collection.find(conditionExistVoteOfUserInOption)) {
                System.out.println(event.getId());
                User user = new User("Celeste","Ailen","3",false,"c@yahoo.com");
                Bson update = Updates.pull("options.$.votes", new Vote(user));
                collection.updateOne(conditionExistVoteOfUserInOption,update);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mongoClient.close(); //cerras el cliente
        }
        */
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);
            Bson condition = Filters.and(Filters.gte("createdDate", LocalDateTime.now().minusHours(2)),Filters.lt("createdDate", LocalDateTime.now()));
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
