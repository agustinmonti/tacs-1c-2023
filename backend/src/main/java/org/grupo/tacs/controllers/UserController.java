package org.grupo.tacs.controllers;

import com.google.gson.Gson;
import io.swagger.annotations.Api;
import org.grupo.tacs.excepciones.ThisEmailIsAlreadyInUseException;
import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.UserRepository;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
@Api(tags = {"user"})
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
     * El método {@code getUser} obtiene un user con un id especifico
     * Es usado en Router para GET /users/{id}
     * @param request contiene el parametro id.
     * @param response nada importante.
     */
    public static Object getUser(Request request, Response response) {
        //aca le pegaria a un RepositorioUsuarios y un usuario en particular
        Gson gson = new Gson();
        String resp = "";
        try {
            User user = UserRepository.instance.findById(request.params(":id"));
            if(user == null){
                throw new NoSuchElementException();
            }
            response.status(200);
            resp = gson.toJson(user);
        }catch (NoSuchElementException e){
            response.status(404);
            resp = gson.toJson("User not Found!");
        }
        return resp;
        //return new ModelAndView(parametros, "usuarios/usuario.html");
    }

    /**
     * El método {@code newUser} crea un User apartir del Body en request.
     * Es usado en Router para POST /users
     * @param request contiene los datos del usuario a crear en el Body.
     * @param response no se usa.
     */
    public static Object newUser(Request request, Response response){
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
        try{
            response.status(201);
            Gson gson = new Gson();
            User nuevo = gson.fromJson(request.body(),User.class);
            UserRepository.instance.save(nuevo);
            return gson.toJson(nuevo);
        }catch(ThisEmailIsAlreadyInUseException e){
            response.status(409);//Email already taken
        }
        return "";
    }

    /**
     * El método {@code getUsers} obtiene todos los usuarios
     * Es usado en Router para GET /users
     * @param request nada importante.
     * @param response nada importante.*/

    public static Object getUsers(Request request, Response response) {
        Map<String,Object> parametros = new HashMap<>();
        response.status(200);
        Gson gson = new Gson();
        String respuesta = gson.toJson(UserRepository.instance.findAll());
        return respuesta;
    }


     /** El método {@code getUsersOptions} envia un status 200 para OPTIONS porque CORS se le da que se tiene que fijar si
     * puede usar POST con un OPTIONS antes de hacer el fetch POST.
     * @param request
     * @param response
     * @return 200*/

    public static Object getUsersOptions(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST, DELETE";
        return buildResponse(response, allowedMethods);
    }

    static Response buildResponse(Response response, String allowedMethods) {
        response.status(200);
        response.header("Allow", allowedMethods);
        return response;
    }

    public static Object getUserOptions(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST, PUT, DELETE";
        return buildResponse(response, allowedMethods);
    }
    public static Object updateUser(Request request, Response response) {
        Gson gson = new Gson();
        Object requestBody = gson.fromJson(response.body(),Object.class);
        User old = UserRepository.instance.findById(/*Long.parseLong*/(request.params(":id")));
        response.status(200);
        if (requestBody instanceof Map) {
            Map<String, Object> requestMap = (Map<String, Object>) requestBody;
            if (requestMap.containsKey("name")) {
                old.setName((String)requestMap.get("name"));
            }
            if (requestMap.containsKey("email")) {
                old.setEmail((String)requestMap.get("email"));
            }if (requestMap.containsKey("passwordHash")) {
                old.setPassword((String)requestMap.get("passwordHash"));
            }
        }
        UserRepository.instance.save(old);
        String respuesta = gson.toJson(old);
        return gson.toJson(respuesta);
    }

    public static Object deleteAll(Request request, Response response) {
        UserRepository.instance.deleteAll();
        response.status(200);
        return response;
    }

    public static Object delete(Request request, Response response) {
        try {
            UserRepository.instance.deleteById(/*Long.parseLong*/(request.params(":id")));
            response.status(200);
        }catch(NoSuchElementException e){
            response.status(404);
        }
        return response;
    }
}
