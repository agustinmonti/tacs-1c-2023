package org.grupo.tacs;
import org.grupo.tacs.controllers.*;
import org.grupo.tacs.model.User;
import spark.Spark;

import static spark.Spark.*;

public class Router {
    public static void main(String[] args){
        // HTTP port
        port(8080);
        Spark.staticFileLocation("../frontend");
        //No creo que tengamos que cargar valores a la DB
        new Main().run(); //Crea algunos usuarios para probar GET /usuarios
        Router.config();

    }

    /**
     * El método {@code config} crea una instancia de cada Controller y define las rutas
     */
    public static void config(){
        before((request, response) -> {
            response.status(200);
            response.header("Access-Control-Allow-Origin", "http://localhost:5174");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.type("application/json");
        });

        UserController userController = new UserController();
        //Defino Rutas
        options("/usuarios",UserController::obtenerOptionsUsuarios);
        get("/usuarios", UserController::obtenerUsuarios);
        post("/usuarios",UserController::nuevoUsuario);
        delete("/usuarios",UserController::borrarTodos);

        options("/usuarios/:id",UserController::obtenerOptionsUsuario);
        get("/usuarios/:id", UserController::obtenerUsuario);
        put("/usuarios/:id",UserController::actualizarUsuario);
        delete("/usuarios/:id",UserController::borrar);

        options("/events",EventController::getEventsOptions);
        get("/events", EventController::getEvents);
        post("/events",EventController::newEvent);
        delete("/events",EventController::deleteEvents);

        options("/events/:id",EventController::getEventOptions);
        get("/events/:id", EventController::getEvent);
        put("/events/:id",EventController::updateEvent);
        delete("/events/:id",EventController::deleteEvent);
    }
}
