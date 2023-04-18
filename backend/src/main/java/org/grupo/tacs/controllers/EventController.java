package org.grupo.tacs.controllers;

import com.google.gson.Gson;
import org.grupo.tacs.model.Event;
import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.EventRepository;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class EventController {

    /**
     * El método {@code getEvents} obtiene todos los usuarios
     * Es usado en Router para GET /events
     * @param request nada importante.
     * @param response nada importante.
     */
    public static Object getEvents(Request request, Response response) {
        Map<String,Object> param = new HashMap<>();
        response.status(200);
        Gson gson = new Gson();
        String events = gson.toJson( EventRepository.instance.findAll());
        return events;
    }

    /**
     * El método {@code getEvent} obtiene un id especifico
     * Es usado en Router para GET /events/{id}
     * @param request contiene el parametro id.
     * @param response nada importante.
     */
    public static Object getEvent(Request request, Response response) {
        //aca le pegaria a un EventRepository y un event en particular
        Gson gson = new Gson();
        String eventJson = "";
        try {
            Event event = EventRepository.instance.findById(Long.parseLong(request.params(":id")));
            response.status(200);
            eventJson = gson.toJson(event);
        }catch (NoSuchElementException e){
            response.status(404);
            eventJson = gson.toJson("No lo encontre!");
        }
        return eventJson;
    }

    /**
     * El método {@code newEvent} crea un Event apartir del Body en request.
     * Es usado en Router para POST /events
     * @param request contiene los datos del event a crear en el Body.
     * @param response no se usa.
     */
    public static Object newEvent(Request request, Response response){
        System.out.println("nuevo evento!!");
        response.status(201);
        System.out.println(request.body().toString());
        Gson gson = new Gson();
        Event newEvent = gson.fromJson(request.body().toString(),Event.class);
        EventRepository.instance.save(newEvent);
        return gson.toJson(newEvent);
    }

    /**
     * El método {@code getEventsOptions} envia un status 200 para OPTIONS porque CORS se le da que se tiene que fijar si
     * puede usar POST con un OPTIONS antes de hacer el fetch POST.
     * @param request
     * @param response
     * @return 200
     */
    public static Object getEventsOptions(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST, DELETE";
        return getResponse(response, allowedMethods);
    }

    private static Response getResponse(Response response, String allowedMethods) {
        response.status(200);
        response.header("Allow", allowedMethods);
        return response;
    }

    public static Object getEventOptions(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST, PUT, DELETE";
        return getResponse(response, allowedMethods);
    }
    public static Object updateEvent(Request request, Response response) {
        Event old = EventRepository.instance.findById(Long.parseLong(request.params(":id")));
        response.status(200);
        Gson gson = new Gson();
        User user = new User("","","");
        User array[] =  new User[10];
        Event event = new Event("",true, user,array);
        event = gson.fromJson(request.body(),Event.class);
        if (!event.getName().equals("")) {
            old.setName(event.getName());
        }
        String eventJson = gson.toJson(old);
        return gson.toJson(eventJson);
    }

    public static Object deleteEvents(Request request, Response response) {
        EventRepository.instance.deleteAll();
        response.status(200);
        return response;
    }

    public static Object deleteEvent(Request request, Response response) {
        try {
            EventRepository.instance.deleteById(Long.parseLong(request.params(":id")));
            response.status(200);
        }catch(NoSuchElementException e){
            response.status(404);
        }
        return response;
    }
}
