package org.grupo.tacs.repos;

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
        return events.stream().filter(event -> event.getId() == id).findFirst().get();
    }

    @Override
    public List<Event> findAll() {
        return events;
    }

    @Override
    public void save(Event event) {
        event.setId(events.stream().count());
        events.add(event);
    }

    @Override
    public void update(Event entidad) {
        Event unEvent = events.stream().filter(event -> event.getId() == entidad.getId()).findFirst().get();
        unEvent.setIsPublic(entidad.getIsPublic());
        unEvent.setName(entidad.getName());
        unEvent.setCreatedBy(entidad.getCreatedBy());
        unEvent.setGuests(entidad.getGuests());
    }

    @Override
    public void delete(Event entidad) {
        events.remove(entidad);
    }

    @Override
    public void deleteAll() {
        events.clear();
    }

    @Override
    public void deleteById(long l) {
        Optional<Event> eventToDelete = events.stream()
                .filter(u -> u.getId() == l)
                .findFirst();
        if (eventToDelete.isPresent()) {
            events.remove(eventToDelete.get());
        } else {
            throw new NoSuchElementException("Event with ID " + l + " not found");
        }
    }
}
