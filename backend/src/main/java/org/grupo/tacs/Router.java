package org.grupo.tacs;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.grupo.tacs.controllers.*;
import org.grupo.tacs.extras.SwaggerConfig;
import spark.Spark;

import static spark.Spark.*;

public class Router {
    private static SwaggerConfig swaggerConfig;
    public static void main(String[] args){
        // HTTP port
        port(8080);
        swaggerConfig = new SwaggerConfig();
        Router.config();
        swaggerConfig.configureSwagger();

    }

    /**
     * El método {@code config} crea una instancia de cada Controller y define las rutas
     */
    public static void config(){
        before((request, response) -> {
            response.status(200);
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.type("application/json");
        });


        options("/auth/login",LoginController::getOptions);
        post("/auth/login",LoginController::login);

        options("/v2/auth/login",LoginController::getOptions);
        post("/v2/auth/login",LoginController::loginJWT);

        options("/users",UserController::getUsersOptions);
        post("/users",UserController::newUser);
        get("/users", UserController::getUsers);
        //Defino Rutas

        get("/users/:id", UserController::getUser);//traer user y eventos(nombre, desc y id) en los que participa (solo si es el user logueado), eventos(nombre, desc, status, totalParticipants y id) en los que participa (solo si es el user logueado)

        options("/events",EventController::getEventsOptions);
        post("/events",EventController::newEvent); // crea un evento
        options("/v2/events",EventController::getEventsOptions);
        post("/v2/events",EventController::createEventJWT);
        get("/events/:id", EventController::getEvent); // trae un evento en especifico, con todas sus opciones  y votos

        options("/events/:id",EventController::getEventOptions);
        put("/events/:id",EventController::updateEvent); //  cerrar evento
        delete("/events/:id",EventController::deleteEvent); // eliminar un evento
        put("/events/:id/vote", EventController::updateVote); // agregar o remover el voto de una opcion
        get("/monitoring", EventController::monitoring); // monitoring ()
        put("/events/:id/participant", EventController::updateParticipant);//anotarse y desanotarse

        get("/swagger.json", (request, response) -> {
            try {
                response.status(200);
                response.type("application/json");
                ObjectMapper mapper = new ObjectMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                String json = mapper.writeValueAsString(swaggerConfig.getSwagger());
                //System.out.println(json);
                return json;
            } catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                return "Error generating Swagger JSON";
            }
        });
        //Los que se van:

        /* options("/users",UserController::getUsersOptions);
        get("/users", UserController::getUsers);

        delete("/users",UserController::deleteAll);

        options("/users/:id",UserController::getUserOptions);
        put("/users/:id",UserController::updateUser);
        delete("/users/:id",UserController::delete);

        get("/events?user=:uid", EventController::getEvents);
        options("/events",EventController::getEventsOptions);
        get("/events", EventController::getEvents);
        options("/events/:id",EventController::getEventOptions);
        options("/events/:idEvent/options", EventOptionController::getOptionsMethodOptions);
        get("/events/:idEvent/options", EventOptionController::getOptions);
        post("/events/:idEvent/options", EventOptionController::newOption);
        delete("/events/:idEvent/options", EventOptionController::deleteAllOptions);

        options("/events/:idEvent/options/:id", EventOptionController::getOptionMethodOptions);
        get("/events/:idEvent/options/:id", EventOptionController::getOption);
        put("/events/:idEvent/options/:id", EventOptionController::updateOption);
        delete("/events/:idEvent/options/:id", EventOptionController::deleteOption);

        options("/events/:idEvent/options/:idOption/votes",VoteController::getVotesOptions);
        get("/events/:idEvent/options/:idOption/votes",VoteController::getVotes);
        post("/events/:idEvent/options/:idOption/votes",VoteController::createVote);
        delete("/events/:idEvent/options/:idOption/votes",VoteController::deleteAllVotes);

        options("/events/:idEvent/options/:idOption/votes/:id",VoteController::getVoteOptions);
        get("/events/:idEvent/options/:idOption/votes/:id",VoteController::getVote);
        delete("/events/:idEvent/options/:idOption/votes/:id",VoteController::deleteVote);*/

    }
}


