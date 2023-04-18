package org.grupo.tacs;
import org.grupo.tacs.controllers.*;
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
            response.header("Access-Control-Allow-Origin", "http://localhost:5173");
            response.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            response.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            response.type("application/json");
        });
        options("/login",LoginController::getOptions);
        post("/login",LoginController::login);

        //Defino Rutas
        options("/users",UserController::getUsersOptions);
        get("/users", UserController::getUsers);
        post("/users",UserController::newUser);
        delete("/users",UserController::deleteAll);

        options("/users/:id",UserController::getUserOptions);
        get("/users/:id", UserController::getUser);
        put("/users/:id",UserController::updateUser);
        delete("/users/:id",UserController::delete);

        options("/events/:idEvent/options/:idOption/votes",VoteController::getVotesOptions);
        get("/events/:idEvent/options/:idOption/votes",VoteController::getVotes);
        post("/events/:idEvent/options/:idOption/votes",VoteController::createVote);
        delete("/events/:idEvent/options/:idOption/votes",VoteController::deleteAllVotes);

        options("/events/:idEvent/options/:idOption/votes/:id",VoteController::getVoteOptions);
        get("/events/:idEvent/options/:idOption/votes/:id",VoteController::getVote);
        delete("/events/:idEvent/options/:idOption/votes/:id",VoteController::deleteVote);

        get("/monitoring", MonitorController::obtenerContadorEventosYHorarios);
    }
}
