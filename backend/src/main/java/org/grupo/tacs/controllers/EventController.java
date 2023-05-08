package org.grupo.tacs.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.types.ObjectId;
import org.grupo.tacs.excepciones.UnauthorizedException;
import org.grupo.tacs.extras.LocalDateTimeDeserializer;
import org.grupo.tacs.model.Event;
import org.grupo.tacs.model.EventOption;
import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.EventRepository;
import org.grupo.tacs.repos.UserRepository;
import spark.Request;
import spark.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.lang.Number;

import static org.grupo.tacs.controllers.LoginController.getVerifiedUserFromToken;

public class EventController {

    /**
     * El método {@code getEvent} obtiene un id especifico
     * Es usado en Router para GET /events/{id}
     * @param request contiene el parametro id.
     * @param response nada importante.
     */
    @ApiOperation(value = "Get a specific event", response = Event.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved the event"),
            @ApiResponse(code = 404, message = "The resource you were trying to reach is not found")
    })
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public static Object getEvent(Request request, Response response) {
        //aca le pegaria a un EventRepository y un event en particular
        Gson gson = new Gson();
        String eventJson = "";
        try {
            Event event = EventRepository.instance.findById(request.params(":id"));
            if(event == null){
                throw new NoSuchElementException();
            }
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
        try{
            response.status(201);
            System.out.println(request.body().toString());
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                    .create();
            Event newEvent = gson.fromJson(request.body(),Event.class);
            newEvent.setCreatedBy(getUserFromSession(request,response));
            EventRepository.instance.save(newEvent);
            return gson.toJson(newEvent);
        }catch(Exception e){
            System.out.println(e);
            return "";
        }


        //return request.body().toString();
    }

    public static User getUserFromSession(Request request, Response response){
        ObjectId objectId = request.session().attribute("id");
        return UserRepository.instance.findById(objectId.toHexString());
    }

    public static Object updateEvent(Request request, Response response) {
        /*Event old = EventRepository.instance.findById(Long.parseLong(request.params(":id")));
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
        return gson.toJson(eventJson);*/
        Gson gson = new Gson();
        Event event = gson.fromJson(request.body(),Event.class);
        event.setId(new ObjectId(request.params(":id")));
        EventRepository.instance.update(event);
        return "evento modificado";
    }

    public static Object deleteEvent(Request request, Response response) {
        try {
            EventRepository.instance.deleteById(/*Long.parseLong*/(request.params(":id")));
            response.status(200);
        }catch(NoSuchElementException e){
            response.status(404);
        }
        // return  response;

        return  "eliminado";
    }

    public static Object monitoring(Request request, Response response) {
        List<Integer> result = EventRepository.instance.monitoring();
        Map<String, Object> data = new HashMap<>();
        data.put("events",result.get(0));
        data.put("votes",result.get(1));
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public static Object updateVoteWithOutId(Request request, Response response) {
        /*Event old = EventRepository.instance.findById(Long.parseLong(request.params(":id")));
        response.status(200);
        Gson gson = new Gson();
        User user = new User("","","");
        User array[] =  new User[10];
        Event event = new Event("",true, user,array);
        event = gson.fromJson(request.body(),Event.class);
        if (!event.getName().equals("")) {
            old.setName(event.getName());
        }
        String eventJson = gson.toJson(old);*/
        //return gson.toJson(eventJson);

        Gson gson = new Gson();
        //User user = getUserFromSession(request,response);
        User user =  getVerifiedUserFromToken(request); //JWT
        JsonObject jsonObject = gson.fromJson(request.body(), JsonObject.class);
        Integer optionIndex = jsonObject.get("_id").getAsInt();
        Event event = EventRepository.instance.findById(request.params(":id"));
        EventRepository.instance.updateVoteWithOutId(event,optionIndex,user);
        response.status(200);
        return "voto realizado o retirado";
    }

    public static Object updateVoteWithId(Request request, Response response) {
        /*Event old = EventRepository.instance.findById(Long.parseLong(request.params(":id")));
        response.status(200);
        Gson gson = new Gson();
        User user = new User("","","");
        User array[] =  new User[10];
        Event event = new Event("",true, user,array);
        event = gson.fromJson(request.body(),Event.class);
        if (!event.getName().equals("")) {
            old.setName(event.getName());
        }
        String eventJson = gson.toJson(old);*/
        //return gson.toJson(eventJson);

        Gson gson = new Gson();
        //User user = getUserFromSession(request,response);
        User user =  getVerifiedUserFromToken(request); //JWT
        EventOption eventOption = gson.fromJson(request.body(),EventOption.class);
        Event event = EventRepository.instance.findById(request.params(":id"));
        EventRepository.instance.updateVoteWithId(event,eventOption,user);
        response.status(200);
        return "voto realizado o retirado";
    }

    public static Object updateParticipant(Request request, Response response) {
        /*Event old = EventRepository.instance.findById(Long.parseLong(request.params(":id")));
        response.status(200);
        Gson gson = new Gson();
        User user = new User("","","");
        User array[] =  new User[10];
        Event event = new Event("",true, user,array);
        event = gson.fromJson(request.body(),Event.class);
        if (!event.getName().equals("")) {
            old.setName(event.getName());
        }
        String eventJson = gson.toJson(old);*/
        //return gson.toJson(eventJson);
        //User user = getUserFromSession(request,response); //Spark java Session
        User user = getVerifiedUserFromToken(request); //JWT
        Event event = EventRepository.instance.findById(request.params(":id"));
        EventRepository.instance.updateParticipant(event,user);
        response.status(200);
        return "participación actualizada";
    }


    /* * El método {@code getEventsOptions} envia un status 200 para OPTIONS porque CORS se le da que se tiene que fijar si
     * puede usar POST con un OPTIONS antes de hacer el fetch POST.
     * @param request
     * @param response
     * @return 200*/

    public static Object getEventsOptions(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST, DELETE";
        return getResponse(response, allowedMethods);
    }

    public static Object getEvents(Request request, Response response) {
    Map<String,Object> param = new HashMap<>();
    response.status(200);
    Gson gson = new Gson();
    //filtrar por user
    String events = gson.toJson( EventRepository.instance.findAll());
    //return events;
    return gson.toJson( "eventos");
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

     /** El método {@code getEvents} obtiene todos los usuarios
     * Es usado en Router para GET /events
     * @param request nada importante.
     * @param response nada importante.*/


    public static Object deleteEvents(Request request, Response response) {
        EventRepository.instance.deleteAll();
        response.status(200);
        // return  response;
        return "eliminados";
    }

    public static Object createEventJWT(Request request, Response response) {
        try {
            User user =  getVerifiedUserFromToken(request);
            response.status(201);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                    .create();
            Event newEvent = gson.fromJson(request.body(),Event.class);
            System.out.println("user id:"+user.getId());
            System.out.println("user:"+user.getName());
            newEvent.setCreatedBy(user);
            System.out.println("Event Created");
            EventRepository.instance.save(newEvent);
            System.out.println("Event Saved");
            return "Event created";
        } catch (UnauthorizedException | JWTVerificationException e) {
            response.status(401);
            return "Unauthorized";
        } catch (Exception e) {
            response.status(500);
            System.out.println(e);
            return "Error creating event";
        }
    }


    public static Object soloPut(Request request, Response response) {
        String allowedMethods = "PUT";
        return getResponse(response, allowedMethods);
    }
}
