package org.grupo.tacs;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.grupo.tacs.controllers.EventController;
import org.grupo.tacs.controllers.LoginController;
import org.grupo.tacs.controllers.UserController;
import org.grupo.tacs.extras.*;

import static spark.Spark.*;

public class Router {
    private static SwaggerConfig swaggerConfig;
    public static void main(String[] args){
        // HTTP port
        System.out.println("Connection string: " + System.getenv("MONGODB_CONNECTION_STRING"));
        System.out.println("Redis Connection string: " + System.getenv("REDIS_CONNECTION_STRING"));
        port(443);
        swaggerConfig = new SwaggerConfig();
        Router.config();

    }

    /**
     * El método {@code config} crea una instancia de cada Controller y define las rutas
     */
    public static void config(){
        //RateLimiter basado en token bucket algorithm
        //RateLimiter rateLimiter = new RateLimiter(10, 10, 1000); // 10 req por segundo
        //before(new RateLimitFilter(rateLimiter));

        before(new DistributedRateLimiterFilter(System.getenv("REDIS_CONNECTION_STRING")));

        before(new CorsOptionsFilter(3600)); //Cache CORS, dura una hora

        before((request, response) -> {
            response.status(200);
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.type("application/json");
        });

        options("/v2/auth/login",LoginController::getOptions);
        post("/v2/auth/login",LoginController::loginJWT);
        options("/v2/auth/renew",LoginController::getOptions);
        post("/v2/auth/renew",LoginController::renew);

        options("/v2/users",UserController::getUsersOptions);
        post("/v2/users",UserController::newUser);
        get("/v2/users", UserController::getUsers);

        options("/v2/users/:id",UserController::getUsersOptions);//CORS Error
        get("/v2/users/:id", UserController::getUser);//traer user y eventos(nombre, desc y id) en los que participa (solo si es el user logueado), eventos(nombre, desc, status, totalParticipants y id) en los que participa (solo si es el user logueado)

        options("/v2/events",EventController::getEventsOptions);
        post("/v2/events",EventController::createEventJWT);
        get("/v2/events",EventController::getEventsByUser); // get /v2/events?userId=
        get("/v2/events/:id", EventController::getEvent); // trae un evento en especifico, con todas sus opciones  y votos

        options("/v2/events/:id",EventController::getEventOptions);
        put("/v2/events/:id",EventController::updateEvent); //  cerrar evento
        delete("/v2/events/:id",EventController::deleteEvent); // eliminar un evento
        options("/v2/events/:id/vote", EventController::putAndDelete);
        put("/v2/events/:id/vote", EventController::updateVoteWithOutId); // agregar o remover el voto de una opcion //TODO PATCH o Body| insert condicional
        delete("/v2/events/:id/vote",EventController::deleteVote);
        options("/v2/monitoring", EventController::getEventOptions);
        get("/v2/monitoring", EventController::monitoring); // monitoring ()

        options("/v2/events/:id/participant",EventController::putAndDelete);
        put("/v2/events/:id/participant", EventController::addParticipant);//anotarse
        delete("/v2/events/:id/participant", EventController::deleteParticipant);//desanotarse

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
                String json = "{\"swagger\":\"2.0\",\"info\":{\"description\":\"A sample RESTful API built with Spark Java and Swagger\",\"version\":\"1.0.0\",\"title\":\"TACS-1C-2023\"},\"tags\":[{\"name\":\"users\",\"description\":\"Operaciones con cuentas de usuario\"},{\"name\":\"login\",\"description\":\"Operaciones de autenticación\"},{\"name\":\"events\",\"description\":\"Operaciones con eventos\"},{\"name\":\"monitoring\",\"description\":\"Muestra datos de marketing\"}],\"paths\":{\"/v2/users\":{\"post\":{\"tags\":[\"users\"],\"summary\":\"Crear una cuenta de usuario\",\"description\":\"Crea una cuenta de usuario nueva\",\"parameters\":[{\"in\":\"body\",\"name\":\"user\",\"description\":\"Datos del usuario a crear\",\"required\":false,\"schema\":{\"properties\":{\"name\":{\"type\":\"string\",\"description\":\"User name\"},\"lastname\":{\"type\":\"string\",\"description\":\"User lastname\"},\"email\":{\"type\":\"string\",\"description\":\"User email\"},\"password\":{\"type\":\"string\",\"description\":\"User password\"}},\"description\":\"User object\",\"example\":\"{ \\\"name\\\": \\\"Bob\\\",\\\"lastname\\\":\\\"Esponja\\\",\\\"email\\\": \\\"bobesponja@proton.me\\\",\\\"password\\\": \\\"abcd1234\\\",\\\"confirmPassword\\\": \\\"abcd1234\\\"}\"}}],\"responses\":{\"201\":{\"description\":\"User successfully created\"},\"409\":{\"description\":\"This email is already in use\"},\"500\":{\"description\":\"\"}}}},\"/v2/users/{id}\":{\"get\":{\"tags\":[\"users\"],\"summary\":\"Trae informacion del usuario\",\"description\":\"Trae informacion del usuario, con informacion adicional si tiene autorizacion\",\"parameters\":[{\"name\":\"id\",\"in\":\"path\",\"description\":\"ID del usuario a buscar\",\"required\":true,\"type\":\"string\",\"format\":\"ObjectId\"}],\"security\":[{\"JWT\":[]}],\"responses\":{\"200\":{\"description\":\"Ok\"},\"404\":{\"description\":\"Usuario no encontrado!\"}}}},\"/v2/auth/login\":{\"post\":{\"tags\":[\"login\"],\"summary\":\"Generate JWT token for user\",\"description\":\"Generate JWT token for an existing user\",\"parameters\":[{\"in\":\"body\",\"name\":\"credentials\",\"description\":\"Credenciales de autenticación\",\"required\":false,\"schema\":{\"properties\":{\"email\":{\"type\":\"string\",\"description\":\"User email\"},\"password\":{\"type\":\"string\",\"description\":\"User password\"}},\"description\":\"Login information\",\"example\":\"{ \\\"email\\\": \\\"b@hotmail.com\\\", \\\"password\\\": \\\"2\\\" }\"}}],\"responses\":{\"200\":{\"description\":\"Autenticación exitosa\"},\"401\":{\"description\":\"Credenciales inválidas\"},\"500\":{\"description\":\"\"}}}},\"/v2/auth/renew\":{\"post\":{\"tags\":[\"login\"],\"summary\":\"Regenrate JWT token for user\",\"description\":\"regenerate JWT token for an existing user\",\"parameters\":[],\"security\":[{\"JWT\":[]}],\"responses\":{\"200\":{\"description\":\"Ok\"},\"401\":{\"description\":\"Unauthorized\"},\"500\":{\"description\":\"Server Error\"}}}},\"/v2/events\":{\"get\":{\"tags\":[\"events\"],\"summary\":\"Retorna los eventos que creo un usuario o en los que esta participando\",\"description\":\"Retorna todos los eventos que creo un usuario o en los que esta participando\",\"parameters\":[{\"name\":\"userId\",\"in\":\"query\",\"description\":\"ObjectId of a User\",\"type\":\"string\",\"required\":false}],\"security\":[{\"JWT\":[]}],\"responses\":{\"200\":{\"description\":\"ok\"},\"400\":{\"description\":\"Invalid userId parameter\"},\"401\":{\"description\":\"Unauthorized\"}}},\"post\":{\"tags\":[\"events\"],\"summary\":\"Crear un nuevo evento\",\"description\":\"El usuario crea un nuevo evento con un numero de opciones\",\"parameters\":[{\"in\":\"body\",\"name\":\"event\",\"description\":\"Datos del evento a crear\",\"required\":false,\"schema\":{\"properties\":{\"name\":{\"type\":\"string\",\"description\":\"Event name\"},\"desc\":{\"type\":\"string\",\"description\":\"Event description\"},\"createdBy\":{\"type\":\"integer\",\"format\":\"int64\",\"description\":\"Created by user\"},\"options\":{\"type\":\"array\",\"description\":\"Possible Event time segments\",\"items\":{\"type\":\"object\",\"properties\":{\"end\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"End time\"},\"start\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"Start time\"},\"votes\":{\"type\":\"array\",\"description\":\"Votes by users\",\"items\":{\"type\":\"object\",\"properties\":{\"user\":{\"type\":\"object\",\"description\":\"User that voted this option\"},\"votingDate\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"Time of Vote\"}}}}}}},\"participants\":{\"type\":\"array\",\"description\":\"Participant List\",\"items\":{\"type\":\"object\",\"properties\":{\"User\":{\"type\":\"object\",\"description\":\"A participant\"}}}}},\"description\":\"Event object\",\"example\":\"{ \\\"name\\\": \\\"Evento Aburrido\\\",\\\"desc\\\":\\\"Un evento que no es divertido\\\",\\\"participants\\\": [],\\\"options\\\":[{\\\"start\\\": \\\"2022-01-01T12:00:00Z\\\",\\\"end\\\": \\\"2022-01-01T14:00:00Z\\\",\\\"votes\\\": []},{\\\"start\\\": \\\"2022-01-01T15:00:00Z\\\",\\\"end\\\": \\\"2022-01-01T17:00:00Z\\\",\\\"votes\\\": []}]}\"}}],\"security\":[{\"JWT\":[]}],\"responses\":{\"201\":{\"description\":\"Evento creado satisfactoriamente\"},\"401\":{\"description\":\"Unauthorized\"},\"500\":{\"description\":\"\"}}}},\"/v2/events/{id}\":{\"get\":{\"tags\":[\"events\"],\"summary\":\"Obtener un evento por ID\",\"description\":\"Obtiene un evento registrado por ID\",\"parameters\":[{\"name\":\"id\",\"in\":\"path\",\"description\":\"ID del evento a buscar\",\"required\":true,\"type\":\"string\",\"format\":\"ObjectId\"}],\"responses\":{\"200\":{\"description\":\"Evento encontrado\"},\"404\":{\"description\":\"Evento no encontrado\"},\"500\":{\"description\":\"\"}}},\"put\":{\"tags\":[\"events\"],\"summary\":\"Actualiza el estado del Evento\",\"description\":\"Cambia el estado de un evento entre Abierto y Cerrado\",\"parameters\":[{\"name\":\"id\",\"in\":\"path\",\"description\":\"ID del evento a buscar\",\"required\":true,\"type\":\"string\",\"format\":\"ObjectId\"}],\"security\":[{\"JWT\":[]}],\"responses\":{\"201\":{\"description\":\"Evento actualizado\"},\"404\":{\"description\":\"Evento no encontrado\"}}}},\"/v2/events/{id}/vote\":{\"put\":{\"tags\":[\"events\"],\"summary\":\"Votar una opcion de evento\",\"description\":\"A partir del id del evento el usuario vota una opcion\",\"parameters\":[{\"name\":\"id\",\"in\":\"path\",\"description\":\"Id del evento a buscar\",\"required\":true,\"type\":\"string\",\"format\":\"ObjectId\"},{\"in\":\"body\",\"name\":\"EventOption\",\"required\":false,\"schema\":{\"properties\":{\"optionIndex\":{\"type\":\"string\",\"description\":\"Used to identify the EventOption to vote\"}},\"description\":\"EventOption id or index\",\"example\":\"{\\\"optionIndex\\\": \\\"0\\\"}\"}}],\"security\":[{\"JWT\":[]}],\"responses\":{\"201\":{\"description\":\"voto realizado\"},\"401\":{\"description\":\"Unauthorized\"},\"404\":{\"description\":\"No se encontro el evento o su opcion a votar\"},\"500\":{\"description\":\"\"}}},\"delete\":{\"tags\":[\"events\"],\"summary\":\"Eliminar Voto\",\"description\":\"A partir del id del evento el usuario elimina el voto de una opcion\",\"parameters\":[{\"name\":\"id\",\"in\":\"path\",\"description\":\"Id del evento a buscar\",\"required\":true,\"type\":\"string\",\"format\":\"ObjectId\"},{\"in\":\"body\",\"name\":\"EventOption\",\"required\":false,\"schema\":{\"properties\":{\"optionIndex\":{\"type\":\"string\",\"description\":\"Used to identify the EventOption to vote\"}},\"description\":\"EventOption id or index\",\"example\":\"{\\\"optionIndex\\\": \\\"0\\\"}\"}}],\"security\":[{\"JWT\":[]}],\"responses\":{\"201\":{\"description\":\"voto eliminado\"},\"401\":{\"description\":\"Unauthorized\"},\"404\":{\"description\":\"No se encontro el evento o su opcion para eliminar voto\"},\"500\":{\"description\":\"\"}}}},\"/v2/events/{id}/participant\":{\"put\":{\"tags\":[\"events\"],\"summary\":\"Permite subscribirse a un evento\",\"description\":\"Un usuario logeado puede subscribirse a un evento\",\"parameters\":[{\"name\":\"id\",\"in\":\"path\",\"description\":\"Id del evento a buscar\",\"required\":true,\"type\":\"string\",\"format\":\"ObjectId\"}],\"security\":[{\"JWT\":[]}],\"responses\":{\"201\":{\"description\":\"participación actualizada\"},\"401\":{\"description\":\"Unauthorized\"},\"404\":{\"description\":\"No se encontro el evento\"},\"500\":{\"description\":\"\"}}},\"delete\":{\"tags\":[\"events\"],\"summary\":\"Permite eliminar subscripcion a un evento\",\"description\":\"Un usuario logeado puede eliminar su subscripcion a un evento\",\"parameters\":[{\"name\":\"id\",\"in\":\"path\",\"description\":\"Id del evento a buscar\",\"required\":true,\"type\":\"string\",\"format\":\"ObjectId\"}],\"security\":[{\"JWT\":[]}],\"responses\":{\"201\":{\"description\":\"participación actualizada\"},\"401\":{\"description\":\"Unauthorized\"},\"404\":{\"description\":\"No se encontro el evento\"},\"500\":{\"description\":\"\"}}}},\"/monitoring\":{\"get\":{\"tags\":[\"monitoring\"],\"summary\":\"Trae datos relevantes a marketing en un JSON\",\"description\":\"Trae datos relevantes a marketing en un JSON, cantidad de opciones de eventos votadas en las ultimas dos horas\",\"parameters\":[],\"responses\":{\"200\":{\"description\":\"ok\"},\"500\":{\"description\":\"\"}}}}},\"securityDefinitions\":{\"JWT\":{\"type\":\"apiKey\",\"name\":\"Authorization\",\"in\":\"header\"}},\"definitions\":{\"User\":{\"properties\":{\"name\":{\"type\":\"string\",\"description\":\"User name\"},\"lastname\":{\"type\":\"string\",\"description\":\"User lastname\"},\"email\":{\"type\":\"string\",\"description\":\"User email\"},\"password\":{\"type\":\"string\",\"description\":\"User password\"}},\"description\":\"User object\",\"example\":\"{ \\\"name\\\": \\\"Bob\\\",\\\"lastname\\\":\\\"Esponja\\\",\\\"email\\\": \\\"bobesponja@proton.me\\\",\\\"password\\\": \\\"abcd1234\\\",\\\"confirmPassword\\\": \\\"abcd1234\\\"}\"},\"Event\":{\"properties\":{\"name\":{\"type\":\"string\",\"description\":\"Event name\"},\"desc\":{\"type\":\"string\",\"description\":\"Event description\"},\"createdBy\":{\"type\":\"integer\",\"format\":\"int64\",\"description\":\"Created by user\"},\"options\":{\"type\":\"array\",\"description\":\"Possible Event time segments\",\"items\":{\"type\":\"object\",\"properties\":{\"end\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"End time\"},\"start\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"Start time\"},\"votes\":{\"type\":\"array\",\"description\":\"Votes by users\",\"items\":{\"type\":\"object\",\"properties\":{\"user\":{\"type\":\"object\",\"description\":\"User that voted this option\"},\"votingDate\":{\"type\":\"string\",\"format\":\"date-time\",\"description\":\"Time of Vote\"}}}}}}},\"participants\":{\"type\":\"array\",\"description\":\"Participant List\",\"items\":{\"type\":\"object\",\"properties\":{\"User\":{\"type\":\"object\",\"description\":\"A participant\"}}}}},\"description\":\"Event object\",\"example\":\"{ \\\"name\\\": \\\"Evento Aburrido\\\",\\\"desc\\\":\\\"Un evento que no es divertido\\\",\\\"participants\\\": [],\\\"options\\\":[{\\\"start\\\": \\\"2022-01-01T12:00:00Z\\\",\\\"end\\\": \\\"2022-01-01T14:00:00Z\\\",\\\"votes\\\": []},{\\\"start\\\": \\\"2022-01-01T15:00:00Z\\\",\\\"end\\\": \\\"2022-01-01T17:00:00Z\\\",\\\"votes\\\": []}]}\"},\"EventOption\":{\"properties\":{\"optionIndex\":{\"type\":\"string\",\"description\":\"Used to identify the EventOption to vote\"}},\"description\":\"EventOption id or index\",\"example\":\"{\\\"optionIndex\\\": \\\"0\\\"}\"}}}";
                return json;
            } catch (Exception e) {
                e.printStackTrace();
                response.status(500);
                return "Error generating Swagger JSON";
            }
        });
    }
}


