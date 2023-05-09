package org.grupo.tacs;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.grupo.tacs.controllers.*;
import org.grupo.tacs.extras.SwaggerConfig;

import java.io.File;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static com.sun.org.apache.xalan.internal.utils.SecuritySupport.getResourceAsStream;
import static spark.Spark.*;

public class Router {
    private static SwaggerConfig swaggerConfig;
    public static void main(String[] args){
        // HTTP port
        port(8080);
        swaggerConfig = new SwaggerConfig();
        Router.config();

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


        //options("/auth/login",LoginController::getOptions);
        //post("/auth/login",LoginController::login);

        options("/v2/auth/login",LoginController::getOptions);
        post("/v2/auth/login",LoginController::loginJWT);

        options("/users",UserController::getUsersOptions);
        post("/users",UserController::newUser);
        get("/users", UserController::getUsers);
        //Defino Rutas

        get("/users/:id", UserController::getUser);//traer user y eventos(nombre, desc y id) en los que participa (solo si es el user logueado), eventos(nombre, desc, status, totalParticipants y id) en los que participa (solo si es el user logueado)

        //options("/events",EventController::getEventsOptions);
        //post("/events",EventController::newEvent); // crea un evento
        options("/v2/events",EventController::getEventsOptions);
        post("/v2/events",EventController::createEventJWT);
        get("/v2/events",EventController::getEvents);
        get("/v2/events/:id", EventController::getEvent); // trae un evento en especifico, con todas sus opciones  y votos

        options("/v2/events/:id",EventController::getEventOptions);
        put("/v2/events/:id",EventController::updateEvent); //  cerrar evento
        delete("/v2/events/:id",EventController::deleteEvent); // eliminar un evento
        options("/v2/events/:id/vote", EventController::soloPut);
        put("/v2/events/:id/vote", EventController::updateVoteWithOutId); // agregar o remover el voto de una opcion
        get("/monitoring", EventController::monitoring); // monitoring ()
        options("/v2/events/:id/participant",EventController::soloPut);
        put("/v2/events/:id/participant", EventController::updateParticipant);//anotarse y desanotarse

        get("/swagger.json", (request, response) -> {
            try {
                response.status(200);
                response.type("application/json");
                ObjectMapper mapper = new ObjectMapper();
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
                String json = mapper.writeValueAsString(swaggerConfig.getSwagger());
                return json;
            } catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                return "Error generating Swagger JSON";
            }
        });

        get("/swagger-auth.json", (request, response) -> {
            try {
                response.status(200);
                response.type("application/json");
                String json = "{\"swagger\":\"2.0\",\"info\":{\"description\":\"A sample RESTful API built with Spark Java and Swagger\",\"version\":\"1.0.0\",\"title\":\"TACS-1C-2023\"},\"securityDefinitions\":{\"JWT\":{\"type\":\"apiKey\",\"name\":\"Authorization\",\"in\":\"header\",\"description\":\"Enter JWT token\"}},\"tags\":[{\"name\":\"login\",\"description\":\"Operaciones de autenticación\"},{\"name\":\"users\",\"description\":\"Operaciones con cuentas de usuario\"},{\"name\":\"events\",\"description\":\"Operaciones con eventos\"},{\"name\":\"monitoring\",\"description\":\"Muestra datos de marketing\"}],\"paths\":{\"/v2/auth/login\":{\"post\":{\"tags\":[\"login\"],\"summary\":\"Generate JWT token for user\",\"description\":\"Generate JWT token for an existing user\",\"parameters\":[{\"in\":\"body\",\"name\":\"credentials\",\"description\":\"Credenciales de autenticación\",\"required\":false,\"schema\":{\"properties\":{\"email\":{\"type\":\"string\",\"description\":\"User email\"},\"password\":{\"type\":\"string\",\"description\":\"User password\"}},\"description\":\"Login information\",\"example\":\"{ \\\"email\\\": \\\"b@hotmail.com\\\", \\\"password\\\": \\\"2\\\" }\"}}],\"responses\":{\"200\":{\"description\":\"Autenticación exitosa\",\"headers\":{\"Authorization\":{\"type\":\"string\",\"description\":\"JWT token\"}}},\"401\":{\"description\":\"Credenciales inválidas\"}}}},\"/users\":{\"post\":{\"tags\":[\"users\"],\"summary\":\"Crear una cuenta de usuario\",\"description\":\"Crea una cuenta de usuario nueva\",\"parameters\":[{\"in\":\"body\",\"name\":\"user\",\"description\":\"Datos del usuario a crear\",\"required\":false,\"schema\":{\"properties\":{\"name\":{\"type\":\"string\",\"description\":\"User name\"},\"lastname\":{\"type\":\"string\",\"description\":\"User lastname\"},\"email\":{\"type\":\"string\",\"description\":\"User email\"},\"password\":{\"type\":\"string\",\"description\":\"User password\"}},\"description\":\"User object\",\"example\":\"{ \\\"name\\\": \\\"Bob\\\",\\\"lastname\\\":\\\"Esponja\\\",\\\"email\\\": \\\"bobesponja@proton.me\\\",\\\"password\\\": \\\"abcd1234\\\"}\"}}],\"responses\":{\"201\":{\"description\":\"User successfully created\"},\"409\":{\"description\":\"This email is already in use\"}}}},\"/v2/events\":{\"get\":{\"tags\":[\"events\"],\"summary\":\"Obtener todos los eventos\",\"description\":\"Obtiene una lista con todos los eventos registrados\",\"parameters\":[],\"responses\":{\"200\":{\"description\":\"Lista de eventos\"}}},\"post\":{\"tags\":[\"events\"],\"summary\":\"Crear un nuevo evento\",\"description\":\"El usuario crea un nuevo evento con un numero de opciones\",\"parameters\":[{\"in\":\"body\",\"name\":\"event\",\"description\":\"Datos del evento a crear\",\"required\":false,\"schema\":{\"properties\":{\"name\":{\"type\":\"string\",\"description\":\"Event name\"},\"desc\":{\"type\":\"string\",\"description\":\"Event description\"},\"createdBy\":{\"type\":\"integer\",\"format\":\"int64\",\"description\":\"Created by user\"},\"options\":{\"type\":\"array\",\"description\":\"Possible Event time segments\",\"items\":{\"type\":\"object\",\"properties\":{\"end\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"End time\"},\"start\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"Start time\"},\"votes\":{\"type\":\"array\",\"description\":\"Votes by users\",\"items\":{\"type\":\"object\",\"properties\":{\"user\":{\"type\":\"object\",\"description\":\"User that voted this option\"},\"votingDate\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"Time of Vote\"}}}}}}},\"participants\":{\"type\":\"array\",\"description\":\"Participant List\",\"items\":{\"type\":\"object\",\"properties\":{\"User\":{\"type\":\"object\",\"description\":\"A participant\"}}}}},\"description\":\"Event object\",\"example\":\"{ \\\"name\\\": \\\"Evento Aburrido\\\",\\\"desc\\\":\\\"Un evento que no es divertido\\\",\\\"participants\\\": [],\\\"options\\\":[{\\\"start\\\": \\\"2022-01-01T12:00:00Z\\\",\\\"end\\\": \\\"2022-01-01T14:00:00Z\\\",\\\"votes\\\": []},{\\\"start\\\": \\\"2022-01-01T15:00:00Z\\\",\\\"end\\\": \\\"2022-01-01T17:00:00Z\\\",\\\"votes\\\": []}]}\"}}],\"security\":[{\"JWT\":[]}],\"responses\":{\"201\":{\"description\":\"Evento creado satisfactoriamente\"},\"401\":{\"description\":\"Unauthorized\"}}}},\"/v2/events/{id}\":{\"get\":{\"tags\":[\"events\"],\"summary\":\"Obtener un evento por ID\",\"description\":\"Obtiene un evento registrado por ID\",\"parameters\":[{\"name\":\"id\",\"in\":\"path\",\"description\":\"ID del evento a buscar\",\"required\":true,\"type\":\"string\",\"format\":\"ObjectId\"}],\"responses\":{\"200\":{\"description\":\"Evento encontrado\"},\"404\":{\"description\":\"Evento no encontrado\"}}}},\"/v2/events/{id}/vote\":{\"put\":{\"tags\":[\"events\"],\"summary\":\"Votar una opcion de evento\",\"description\":\"A partir del id del evento el usuario vota una opcion\",\"parameters\":[{\"name\":\"id\",\"in\":\"path\",\"description\":\"Id del evento a buscar\",\"required\":true,\"type\":\"string\",\"format\":\"ObjectId\"},{\"in\":\"body\",\"name\":\"EventOption\",\"required\":false,\"schema\":{\"properties\":{\"_id\":{\"type\":\"string\",\"description\":\"Used to identify the EventOption to vote\"}},\"description\":\"EventOption id or index\",\"example\":\"{\\\"_id\\\": \\\"1\\\"}\"}}],\"security\":[{\"JWT\":[]}],\"responses\":{\"201\":{\"description\":\"voto realizado o retirado\"},\"401\":{\"description\":\"Unauthorized\"},\"404\":{\"description\":\"No se encontro el evento o su opcion a votar\"}}}},\"/v2/events/{id}/participant\":{\"put\":{\"tags\":[\"events\"],\"summary\":\"Permite subscribirse a un evento\",\"description\":\"Un usuario logeado puede subscribirse a un evento\",\"parameters\":[{\"name\":\"id\",\"in\":\"path\",\"description\":\"Id del evento a buscar\",\"required\":true,\"type\":\"string\",\"format\":\"ObjectId\"}],\"security\":[{\"JWT\":[]}],\"responses\":{\"201\":{\"description\":\"participación actualizada\"},\"401\":{\"description\":\"Unauthorized\"},\"404\":{\"description\":\"No se encontro el evento\"}}}},\"/monitoring\":{\"get\":{\"tags\":[\"monitoring\"],\"summary\":\"Trae datos relevantes a marketing en un JSON\",\"description\":\"Trae datos relevantes a marketing en un JSON, cantidad de opciones de eventos votadas en las ultimas dos horas\",\"parameters\":[],\"responses\":{\"200\":{\"description\":\"ok\"}}}}},\"definitions\":{\"User\":{\"properties\":{\"name\":{\"type\":\"string\",\"description\":\"User name\"},\"lastname\":{\"type\":\"string\",\"description\":\"User lastname\"},\"email\":{\"type\":\"string\",\"description\":\"User email\"},\"password\":{\"type\":\"string\",\"description\":\"User password\"}},\"description\":\"User object\",\"example\":\"{ \\\"name\\\": \\\"Bob\\\",\\\"lastname\\\":\\\"Esponja\\\",\\\"email\\\": \\\"bobesponja@proton.me\\\",\\\"password\\\": \\\"abcd1234\\\"}\"},\"Event\":{\"properties\":{\"name\":{\"type\":\"string\",\"description\":\"Event name\"},\"desc\":{\"type\":\"string\",\"description\":\"Event description\"},\"createdBy\":{\"type\":\"integer\",\"format\":\"int64\",\"description\":\"Created by user\"},\"options\":{\"type\":\"array\",\"description\":\"Possible Event time segments\",\"items\":{\"type\":\"object\",\"properties\":{\"end\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"End time\"},\"start\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"Start time\"},\"votes\":{\"type\":\"array\",\"description\":\"Votes by users\",\"items\":{\"type\":\"object\",\"properties\":{\"user\":{\"type\":\"object\",\"description\":\"User that voted this option\"},\"votingDate\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"Time of Vote\"}}}}}}},\"participants\":{\"type\":\"array\",\"description\":\"Participant List\",\"items\":{\"type\":\"object\",\"properties\":{\"User\":{\"type\":\"object\",\"description\":\"A participant\"}}}}},\"description\":\"Event object\",\"example\":\"{ \\\"name\\\": \\\"Evento Aburrido\\\",\\\"desc\\\":\\\"Un evento que no es divertido\\\",\\\"participants\\\": [],\\\"options\\\":[{\\\"start\\\": \\\"2022-01-01T12:00:00Z\\\",\\\"end\\\": \\\"2022-01-01T14:00:00Z\\\",\\\"votes\\\": []},{\\\"start\\\": \\\"2022-01-01T15:00:00Z\\\",\\\"end\\\": \\\"2022-01-01T17:00:00Z\\\",\\\"votes\\\": []}]}\"},\"EventOption\":{\"properties\":{\"_id\":{\"type\":\"string\",\"description\":\"Used to identify the EventOption to vote\"}},\"description\":\"EventOption id or index\",\"example\":\"{\\\"_id\\\": \\\"1\\\"}\"}}}";
                return json;
            } catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                return "Error generating Swagger JSON";
            }
        });

        //Este tira duplicated mapping key
        /*get("/swagger.json", (request, response) -> {
            try {
                response.status(200);
                response.type("application/json");
                ObjectMapper mapper = new ObjectMapper();
                SimpleModule module = new SimpleModule();
                SerializerProvider provider = new DefaultSerializerProvider.Impl();
                provider.setNullKeySerializer(new MyNullKeySerializer());
                module.addKeySerializer(Object.class, new MyNullKeySerializer());
                mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
                mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
                mapper.getSerializerProvider().setNullKeySerializer(new MyNullKeySerializer());
                mapper.registerModule(module);
                String json = mapper.writeValueAsString(swaggerConfig.getSwagger());
                return json;
            } catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                return "Error generating Swagger JSON";
            }
        });*/

        //TODO ver que onda con las cosas que no reconoce de Swagger
        /*get("/swagger.yaml", (request, response) -> {
            try {
                response.status(200);
                response.type("application/yaml");

                //Elimino Nulls
                Representer representer = new Representer();
                representer.setPropertyUtils(new PropertyUtils() {
                    @Override
                    public Property getProperty(Class<?> type, String name) {
                        Property property = super.getProperty(type, name);
                        if (property == null || property.get(type) == null) {
                            return null;
                        }
                        return property;
                    }
                });

                Yaml yaml = new Yaml(representer);
                yaml.setBeanAccess(BeanAccess.FIELD);

                String yamlString = yaml.dump(swaggerConfig.getSwagger());
                return yamlString;
            } catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                return "Error generating Swagger YAML";
            }
        });*/

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


