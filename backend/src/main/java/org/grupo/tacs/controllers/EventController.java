package org.grupo.tacs.controllers;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.grupo.tacs.excepciones.*;
import org.grupo.tacs.extras.EventData;
import org.grupo.tacs.extras.LocalDateTimeDeserializer;
import org.grupo.tacs.extras.LocalDateTimeSerializer;
import org.grupo.tacs.model.*;
import org.grupo.tacs.repos.EventRepository;
import org.grupo.tacs.repos.InteractionRepository;
import spark.Request;
import spark.Response;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.*;

import static org.grupo.tacs.controllers.LoginController.getUserSession;
import static org.grupo.tacs.controllers.LoginController.getVerifiedUserFromTokenInRequest;

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
        Map<Object, Object> myMap = new HashMap<Object, Object>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        String eventJson = "";
        try {
            Event event = EventRepository.instance.findById(request.params(":id"));
            if(event == null){
                throw new NoSuchElementException();
            }
            User user = getVerifiedUserFromTokenInRequest(request);
            response.status(200);
            EventData data = new EventData(event);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.GET,request.url(),"Get Event",200));
            myMap.put("event",data);
            myMap.put("optionsVoted",data.getVotados(user));
            eventJson = gson.toJson(myMap);
        } catch (NoSuchElementException e){
            response.status(404);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.GET,request.url(),"Get Event",404));
            myMap.put("msg","Evento no encontrado.");
            eventJson = gson.toJson(myMap);
        }catch (UnauthorizedException e){
            response.status(401);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.GET,request.url(),"Get Event",401));
            myMap.put("msg",e.getMessage());
            e.printStackTrace();
            return gson.toJson(myMap);
        } catch (Exception e) {
            response.status(500);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.GET,request.url(),"Get Event",500));
            e.printStackTrace();
            myMap.put("msg","Error getting event");
            eventJson = gson.toJson(myMap);
        }
        return eventJson;
    }

    /**
     * El método {@code newEvent} crea un Event apartir del Body en request.
     * Es usado en Router para POST /events
     * @param request contiene los datos del event a crear en el Body.
     * @param response no se usa.
     */
    public static Object newEvent(Request request, Response response){//Deprecado ya no usamos Spark session
        try{
            response.status(201);
            System.out.println(request.body().toString());
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                    .create();
            Event newEvent = gson.fromJson(request.body(),Event.class);
            newEvent.setCreatedBy(getUserSession(request,response));
            EventRepository.instance.save(newEvent);
            return gson.toJson(newEvent);
        }catch(Exception e){
            response.status(500);
            return e;
        }
    }

    /*public static User getUserFromSession(Request request, Response response){
        ObjectId objectId = request.session().attribute("id");
        return UserRepository.instance.findById(objectId.toHexString());
    }*/

    public static Object updateEvent(Request request, Response response) {
        Map<Object, Object> myMap = new HashMap<Object, Object>();
        Gson gson = new Gson();
        try{
            response.status(201);
            Event event = EventRepository.instance.findById(request.params(":id"));
            User owner = getVerifiedUserFromTokenInRequest(request);
            if(!owner.getId().equals(event.getCreatedBy().getId()))
                throw new UnauthorizedException("No posee autorización para editar este evento");
            event.setId(new ObjectId(request.params(":id")));
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"Close Event",201));
            EventRepository.instance.update(event);
            myMap.put("msg","evento modificado");
            return gson.toJson(myMap);
        }catch (UnauthorizedException e){
            response.status(401);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"Close Event",401));
            myMap.put("msg",e.getMessage());
            e.printStackTrace();
            return gson.toJson(myMap);
        }catch (NoSuchElementException e){
            response.status(404);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"Close Event",404));
            e.printStackTrace();
            myMap.put("msg",e.getMessage());
            return gson.toJson(myMap);
        }catch (Exception e){
            e.printStackTrace();
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"Close Event",500));
            myMap.put("msg",e.getMessage());
            return gson.toJson(myMap);
        }
    }

    public static Object deleteEvent(Request request, Response response) {
        try {
            EventRepository.instance.deleteById((request.params(":id")));
            response.status(200);
        }catch(NoSuchElementException e){
            response.status(404);
        }
        // return  response;

        return  "eliminado";
    }

    public static Object monitoring(Request request, Response response) {
        //List<Integer> result = EventRepository.instance.monitoring();
        try{
            response.status(200);
            //InteractionRepository.instance.save(new Interaction(InteractionMethod.GET,request.url(),"Monitoring",200));
        /*Map<String, Object> data = new HashMap<>();
        data.put("events",result.get(0));
        data.put("votes",result.get(1));*/
            Map<String, Object> data = InteractionRepository.instance.monitoring();
            Gson gson = new Gson();
            return gson.toJson(data);
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public static Object updateVoteWithOutId(Request request, Response response) {
        Map<Object, Object> myMap = new HashMap<Object, Object>();
        Gson gson = new Gson();
        try {

            //User user = getUserFromSession(request,response);
            User user = getVerifiedUserFromTokenInRequest(request); //JWT
            JsonObject jsonObject = gson.fromJson(request.body(), JsonObject.class);
            Integer optionIndex = jsonObject.get("optionIndex").getAsInt();
            Event event = EventRepository.instance.findById(request.params(":id"));
            if (event == null) {
                throw new EventDoesNotExistException();
            }
            EventRepository.instance.updateVoteWithOutId(event, optionIndex, user);
            response.status(201);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT, request.url(), "EventOption vote", 201));
            myMap.put("msg", "Votos registrados correctamente.");
            return gson.toJson(myMap);
        } catch (EventClosedException | AlreadyVotedException e){
            response.status(401);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT, request.url(), "EventOption vote, but Event was closed", 401));
            myMap.put("msg", e.getMessage());
            return gson.toJson(myMap);
        } catch(EventDoesNotExistException | NoSuchElementException e){
            response.status(404);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"EventOption vote, Event not found",404));
            myMap.put("msg","No encontrado.");
            return gson.toJson(myMap);
        } catch(UserDoesNotExistException | UnauthorizedException | JWTVerificationException e){
            response.status(401);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"EventOption vote, bad Credentials",401));
            myMap.put("msg","Errores de validación.");
            return gson.toJson(myMap);
        } catch (Exception e) {
            response.status(500);
            e.printStackTrace();
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"EventOption vote, server error",500));
            myMap.put("msg","Error al registrar el voto.");
            return gson.toJson(myMap);
        }
    }

    public static Object updateVoteWithId(Request request, Response response) {
        Gson gson = new Gson();
        //User user = getUserFromSession(request,response);
        User user =  getVerifiedUserFromTokenInRequest(request); //JWT
        EventOption eventOption = gson.fromJson(request.body(),EventOption.class);
        Event event = EventRepository.instance.findById(request.params(":id"));
        EventRepository.instance.updateVoteWithId(event,eventOption,user);
        response.status(201);
        return "voto realizado o retirado";
    }

    public static Object updateParticipant(Request request, Response response) {
        try{
            User user = getVerifiedUserFromTokenInRequest(request); //JWT
            Event event = EventRepository.instance.findById(request.params(":id"));
            if (event == null){
                throw new EventDoesNotExistException();
            }
            EventRepository.instance.updateParticipant(event,user);
            response.status(201);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"Event add/remove Participant",201));
            return "participación actualizada";
        } catch(EventDoesNotExistException e){
            response.status(404);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"Event add/remove Participant",404));
            return "Event does not exist";
        } catch(UserDoesNotExistException | UnauthorizedException | JWTVerificationException e){
            response.status(401);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"Event add/remove Participant",401));
            return "Unauthorized";
        } catch (Exception e) {
            response.status(500);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"Event add/remove Participant",500));
            System.out.println(e);
            return "Error updating participant";
        }
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
        try{
            request.queryParams("user");
            Map<String,Object> param = new HashMap<>();
            response.status(200);
            Gson gson = new Gson();
            String events = gson.toJson( EventRepository.instance.findAll());
            return gson.toJson(events);
        }catch (Exception e) {
            response.status(500);
            System.out.println(e);
            return "";
        }

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
        Map<Object, Object> myMap = new HashMap<Object, Object>();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
        try {
            User user =  getVerifiedUserFromTokenInRequest(request);
            Event newEvent = gson.fromJson(request.body(),Event.class);
            newEvent.setCreatedBy(user);
            EventRepository.instance.save(newEvent);
            response.status(201);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.POST,request.url(),"Create an Event",201));
            myMap.put("msg","Evento creado.");
            myMap.put("id",newEvent.getId().toHexString());
            return gson.toJson(myMap);
        } catch (UserDoesNotExistException | UnauthorizedException | JWTVerificationException e) {
            response.status(401);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.POST,request.url(),"Create an Event",401));
            myMap.put("msg","Unauthorized");
            return gson.toJson(myMap);
        } catch (Exception e) {
            response.status(500);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.POST,request.url(),"Create an Event",500));
            System.out.println(e);
            return "Error creating event";
        }
    }


    public static Object putAndDelete(Request request, Response response) {
        String allowedMethods = "PUT,DELETE";
        return getResponse(response, allowedMethods);
    }

    public static Object getEventsByUser(Request request, Response response){
        Map<Object, Object> myMap = new HashMap<Object, Object>();
        Gson gson = new Gson();
        String modifiedUrl = request.url() + "?userId=" + request.queryParams("userId");
        try{
            String userIdString = request.queryParams("userId");
            ObjectId userId = new ObjectId(userIdString);
            Document events = EventRepository.instance.getEventsByUser(userId);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.GET,modifiedUrl,"Get User's events",200));
            return events.toJson();
        }catch (Exception e){
            e.printStackTrace();
            response.status(400); // Bad Request
            InteractionRepository.instance.save(new Interaction(InteractionMethod.GET,modifiedUrl,"Get User's events",400));
            myMap.put("msg","Usuario no encontrado");
            return gson.toJson(myMap);
        }
    }

    public static Object deleteVote(Request request, Response response) {
        Map<Object, Object> myMap = new HashMap<Object, Object>();
        Gson gson = new Gson();
        try {

            //User user = getUserFromSession(request,response);
            User user = getVerifiedUserFromTokenInRequest(request); //JWT
            JsonObject jsonObject = gson.fromJson(request.body(), JsonObject.class);
            Integer optionIndex = jsonObject.get("optionIndex").getAsInt();
            Event event = EventRepository.instance.findById(request.params(":id"));
            if (event == null) {
                throw new EventDoesNotExistException();
            }
            EventRepository.instance.deleteVoteWithOutId(event, optionIndex, user);
            response.status(200);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.DELETE, request.url(), "EventOption delete vote", 201));
            myMap.put("msg", "Voto eliminado.");
            return gson.toJson(myMap);
        } catch (EventClosedException e){
            response.status(401);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT, request.url(), "EventOption vote, but Event was closed", 401));
            myMap.put("msg", "Votacion Cerrada.");
            return gson.toJson(myMap);
        } catch(AlreadyVotedException e) {
            response.status(401);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT, request.url(), "EventOption vote, but Event was closed", 401));
            myMap.put("msg", e.getMessage());
            return gson.toJson(myMap);
        }catch (EventDoesNotExistException | NoSuchElementException e){
            response.status(404);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"EventOption vote, Event not found",404));
            myMap.put("msg",e.getMessage());
            return gson.toJson(myMap);
        } catch(UserDoesNotExistException | UnauthorizedException | JWTVerificationException e){
            e.printStackTrace();
            response.status(401);
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"EventOption vote, bad Credentials",401));
            myMap.put("msg","Errores de validación.");
            return gson.toJson(myMap);
        } catch (Exception e) {
            response.status(500);
            e.printStackTrace();
            InteractionRepository.instance.save(new Interaction(InteractionMethod.PUT,request.url(),"EventOption vote, server error",500));
            myMap.put("msg","Error al eliminar el voto.");
            return gson.toJson(myMap);
        }
    }
}
