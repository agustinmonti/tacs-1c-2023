package org.grupo.tacs.repos;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.grupo.tacs.model.Event;
import org.grupo.tacs.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


public class EventRepository implements Repository<Event>{
    public static EventRepository instance = new EventRepository();

    List<Event> events = new ArrayList<>();
    @Override
    public Event findById(Long id) {

        /*MongoCollection<Event> collection = mongodb().getCollection("Events", Event.class);
        Bson condition = Filters.eq("_id", id);
        FindIterable<Event> events = collection.find(condition);
        return events.first();*/

        return events.stream().filter(event -> event.getId() == id).findFirst().get();
    }

    @Override
    public List<Event> findAll() {
        /*MongoCollection<Event> collection = mongodb().getCollection("Events", Event.class);
        return collection.find().into(new ArrayList<>());*/

        return events;
    }

    @Override
    public void save(Event event) {
        event.setId(events.stream().count());
        events.add(event);
    }

    public void insert(Event event) {
        /*MongoCollection<Event> collection = mongodb().getCollection("Events", Event.class);

        //Esto seria para insertar uno manual:
        MongoCollection<User> users_collection = mongodb().getCollection("Users", User.class);
        List<User> users = users_collection.find().into(new ArrayList<>());
        Event event1 = new Event("Marcha", true, users.get(0), users).setId(2L);

        collection.insertOne(event1);*/
    }

    @Override
    public void update(Event entidad) {

        /*MongoCollection<Event> collection = mongodb().getCollection("Events", Event.class);

        Bson condition = Filters.eq("_id", entidad.getId());
        collection.findOneAndUpdate(condition, Updates.set("name","Huelga"));*/


        Event unEvent = events.stream().filter(event -> event.getId() == entidad.getId()).findFirst().get();
        unEvent.setIsPublic(entidad.getIsPublic());
        unEvent.setName(entidad.getName());
        unEvent.setCreatedBy(entidad.getCreatedBy());
        unEvent.setGuests(entidad.getGuests());
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
    public void deleteById(long id) {

        /*MongoCollection<Event> collection = mongodb().getCollection("Events", Event.class);
        Bson condition = Filters.eq("_id", id);

        try {
            collection.deleteOne(condition);
        } catch (MongoException e) {
            e.printStackTrace();
        }*/


        Optional<Event> eventToDelete = events.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
        if (eventToDelete.isPresent()) {
            events.remove(eventToDelete.get());
        } else {
            throw new NoSuchElementException("Event with ID " + id + " not found");
        }
    }
}
