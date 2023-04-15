package org.grupo.tacs.controllers;

import org.grupo.tacs.model.User;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

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
    public static Object obtenerUsuarios(Request request, Response response) {
        Map<String,Object> parametros = new HashMap<>();
        //aca le pegaria a un RepositorioUsuarios y retornaria todos los usuarios
        //Algo estilo parametros.put("Usuarios",RepositorioUsuarios.instancia.todos())
        String[] usuarios = {"Anabel","Bernardo","Conrado","Dora"};
        parametros.put("Usuarios",usuarios);
        return new ModelAndView(parametros, "usuarios/usuarios.html");
    }
    public static Object obtenerUsuario(Request request, Response response) {
        Map<String,Object> parametros = new HashMap<>();
        //aca le pegaria a un RepositorioUsuarios y un usuario en particular
        //ej User usuario = RepositorioUsuarios.instancia.buscar(Long.parseLong(request.params(":idUser")));
        User usuario = new User();
        parametros.put("usuario",usuario);
        return new ModelAndView(parametros, "usuarios/usuario.html");
    }
    public ModelAndView formRegistrarUsuario(Request request, Response response) {
        Map<String, Object> parametros = new HashMap<>();
        //Aca me fijaria que no este logeado, osea que no exista una session
        return new ModelAndView(parametros,"usuarios/signup.html");
    }
    public static Object nuevoUsuario(Request request, Response response){
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
        response.redirect("/users");
        return response;
    }
}
