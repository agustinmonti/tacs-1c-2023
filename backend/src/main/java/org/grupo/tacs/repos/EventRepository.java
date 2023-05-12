package org.grupo.tacs.repos;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.*;
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
            event.setIsActive(true);
            event.setCreatedDate(LocalDateTime.now());
            collection.insertOne(event);
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            mongoClient.close(); //cerras el cliente
        }

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

    }

    public void updateVoteWithOutId(Event event, Integer OptionIndex, User user) { //SE USA PARA AGREGAR O REMOVER UN VOTO DE UNA OPCION

        mongoClient = MongoDB.getMongoClient();
        try {

            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);

            EventOption option = event.getOptions().get(OptionIndex);

            List<Vote> votes = option.getVotes().stream().filter(vote -> Objects.equals(vote.getUser().getId(), user.getId())).collect(Collectors.toList());
            Bson condition = Filters.eq("_id", event.getId());
            if (votes.isEmpty()){
                option.addVote(new Vote(user));
            } else{
                option.rmvVote(votes.get(0));
            }
            collection.replaceOne(condition,event);

        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        } catch (MongoException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            mongoClient.close(); //cerras el cliente
        }
    }

    public void updateVoteWithId(Event event, EventOption eventOption, User user) { //SE USA PARA AGREGAR O REMOVER UN VOTO DE UNA OPCION

        mongoClient = MongoDB.getMongoClient();
        try {

            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Event> collection = mongodb.getCollection("Events", Event.class);

            EventOption option = event.getOption(eventOption);

            List<Vote> votes = option.getVotes().stream().filter(vote -> Objects.equals(vote.getUser().getId(), user.getId())).collect(Collectors.toList());
            Bson condition = Filters.eq("_id", event.getId());
            UpdateOptions options = new UpdateOptions().arrayFilters(Arrays.asList(Filters.eq("filter._id", option.getId())));
            if (votes.isEmpty()){
                collection.updateOne(condition,Updates.push("options.$[filter].votes", new Vote(user)),options);
            } else{
                collection.updateOne(condition,Updates.pull("options.$[filter].votes", votes.get(0)),options);
            }

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


        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException();
        } catch (MongoException e) {
            e.printStackTrace();
        } catch (Exception e) {
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
    public Document getEventsByUser(ObjectId userId) {
        mongoClient = MongoDB.getMongoClient();
        Document result = null;
        try {
            List<Document> myEvents = new ArrayList<>();
            List<Document> participantEvents = new ArrayList<>();
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCursor<Document> cursor = mongodb.getCollection("Events").find(
                    Filters.or(
                            Filters.eq("createdBy._id", userId),
                            Filters.elemMatch("participants", Filters.eq("_id", userId))
                    )
            ).iterator();

            try {
                while (cursor.hasNext()) {
                    Document event = cursor.next();
                    User createdByUser = new User((Document) event.get("createdBy"));
                    List<User> participants = new ArrayList<>();
                    for (Document participantDoc : (List<Document>) event.get("participants")) {
                        participants.add(new User(participantDoc));
                    }
                    if (createdByUser.getId().equals(userId)) {
                        myEvents.add(new Document()
                                .append("id", event.getObjectId("_id").toString())
                                .append("name", event.getString("name"))
                                .append("description", event.getString("desc"))
                                .append("status", event.getString("status"))
                                .append("totalParticipants", participants.size()));
                    }
                    if (participants.stream().anyMatch(user -> user.getId().equals(userId))) {
                        participantEvents.add(new Document()
                                .append("id", event.getObjectId("_id").toString())
                                .append("name", event.getString("name"))
                                .append("description", event.getString("desc"))
                                .append("status", event.getString("status"))
                                .append("totalParticipants", participants.size()));
                    }
                }
            } finally {
                cursor.close();
            }
            result = new Document();
            result.append("myEvents", myEvents);
            result.append("participants", participantEvents);
        } catch (MongoException e) {
            e.printStackTrace();
        } finally {
            mongoClient.close();
        }
        return result;
    }

}
