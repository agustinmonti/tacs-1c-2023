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
import java.util.NoSuchElementException;

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
        //aca le pegaria a un RepositorioUsuarios y un usuario en particular
        Gson gson = new Gson();
        String respuesta = "";
        try {
            User usuario = RepositorioUsuario.instancia.findById(Long.parseLong(request.params(":id")));
            response.status(200);
            respuesta = gson.toJson(usuario);
        }catch (NoSuchElementException e){
            response.status(404);
            respuesta = gson.toJson("No lo encontre!");
        }
        return respuesta;
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
        Gson gson = new Gson();
        User nuevo = gson.fromJson(request.body().toString(),User.class);
        RepositorioUsuario.instancia.save(nuevo);
        return gson.toJson(nuevo);
    }

    /**
     * El método {@code malditoCORS} envia un status 200 para OPTIONS porque CORS se le da que se tiene que fijar si
     * puede usar POST con un OPTIONS antes de hacer el fetch POST.
     * @param request
     * @param response
     * @return 200
     */
    public static Object obtenerOptionsUsuarios(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST, DELETE";
        return armarResponse(response, allowedMethods);
    }

    public static Response armarResponse(Response response, String allowedMethods) {
        response.status(200);
        response.header("Allow", allowedMethods);
        return response;
    }

    public static Object obtenerOptionsUsuario(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST, PUT, DELETE";
        return armarResponse(response, allowedMethods);
    }
    public static Object actualizarUsuario(Request request, Response response) {
        User viejo = RepositorioUsuario.instancia.findById(Long.parseLong(request.params(":id")));
        response.status(200);
        Gson gson = new Gson();
        User user = new User("","","");
        user = gson.fromJson(request.body(),User.class);
        if (!user.getNombre().equals("")) {
            viejo.setNombre(user.getNombre());
        }
        if (!user.getEmail().equals("")){
            viejo.setEmail(user.getEmail());
        }
        if (!user.getPasswordHash().equals("")){
            viejo.setPasswordHash(user.getPasswordHash());
        }
        String respuesta = gson.toJson(viejo);
        return gson.toJson(respuesta);
    }

    public static Object borrarTodos(Request request, Response response) {
        RepositorioUsuario.instancia.deleteAll();
        response.status(200);
        return response;
    }

    public static Object borrar(Request request, Response response) {
        try {
            RepositorioUsuario.instancia.deleteById(Long.parseLong(request.params(":id")));
            response.status(200);
        }catch(NoSuchElementException e){
            response.status(404);
        }
        return response;
    }
}
