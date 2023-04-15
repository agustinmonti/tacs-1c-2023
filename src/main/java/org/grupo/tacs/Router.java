package org.grupo.tacs;
import org.grupo.tacs.controllers.*;
import spark.Spark;

import static spark.Spark.*;

public class Router {
    public static void main(String[] args){
        // HTTP port
        port(8080);
        Spark.staticFileLocation("/public");
        //No creo que tengamos que cargar valores a la DB
        //Defino Rutas
        get("/usuarios", UserController::obtenerUsuarios);
        post("/usuarios",UserController::nuevoUsuario);

    }
}
