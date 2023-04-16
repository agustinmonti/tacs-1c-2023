package org.grupo.tacs.controllers;

import com.google.gson.Gson;
import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.RepositorioUsuario;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserController {
    /* Como obtener el User a partir de una session creada con login
    public static User obtenerUsuarioSession(Request request, Response response){
        //aca me fijaria si existe una session y si existe obtener el "id" para ir a buscarlo al repo
        if(Objects.isNull(request.session().attribute("id"))){
            response.status(500);
            response.redirect("/");
        }
        //busco el id del recurso, en este caso un User en un repositorio
        return RepositorioUsuarios.instancia.buscar(request.session().attribute("id"));
    }
    */

    /**
     * El método {@code obtenerUsuarios} obtiene todos los usuarios
     * Es usado en Router para GET /usuarios
     * @param request nada importante.
     * @param response nada importante.
     */
    public static Object obtenerUsuarios(Request request, Response response) {
        Map<String,Object> parametros = new HashMap<>();
        response.status(200);
        Gson gson = new Gson();
        String respuesta = gson.toJson( RepositorioUsuario.instancia.findAll());
        return respuesta;
    }

    /**
     * El método {@code obtenerUsuario} obtiene un id especifico
     * Es usado en Router para GET /usuario/{id}
     * @param request contiene el parametro id.
     * @param response nada importante.
     */
    public static Object obtenerUsuario(Request request, Response response) {
        Map<String,Object> parametros = new HashMap<>();
        //aca le pegaria a un RepositorioUsuarios y un usuario en particular
        //ej User usuario = RepositorioUsuarios.instancia.buscar(Long.parseLong(request.params(":idUser")));
        System.out.println("entro en obtenerUsuario");
        User usuario = new User("Bob","abcd1234","bob@yahoo.com.ar");
        parametros.put("usuario",usuario);
        response.status(200);
        Gson gson = new Gson();
        return gson.toJson(usuario);
        //return new ModelAndView(parametros, "usuarios/usuario.html");
    }

    /**
     * El método {@code nuevoUsuario} crea un User apartir del Body en request.
     * Es usado en Router para POST /usuarios
     * @param request contiene los datos del usuario a crear en el Body.
     * @param response no se usa.
     */
    public static Object nuevoUsuario(Request request, Response response){

        System.out.println("nuevo Usuario!!");
        // deberia usar un try catch()
        //obtengo los datos del request
        /* Ejemplo
        String nombre          = request.queryParams("nombre del User")
        String password     = request.queryParams("password");
        String email     = request.queryParams("email");
         */
        /*
        Me fijaria que no exista el usuario con ese nombre:
        List<User> usuarios = RepositorioUsuarios.instancia.listar()
        if(usuarios.stream().anyMatch(user->user.getNombre()==nombre)){
            tiro alguna exepcion ej: throw new Exception("Ya existe un usuario con ese nombre")
        }
         */
        /*
        Comienzo a crear el usuario
        User nuevo = new User(nombre,password,email)
         */
        response.status(201);
        System.out.println(request.body().toString());
        return "{\"response\":\"nuevo usuario " +  request.body() + "\"}";
        //return response;
    }
}
