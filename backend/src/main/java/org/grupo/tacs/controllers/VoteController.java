package org.grupo.tacs.controllers;

import com.google.gson.Gson;
import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.RepositorioUsuario;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.*;

import static javax.swing.text.html.HTML.Tag.OL;
import static org.grupo.tacs.controllers.UserController.armarResponse;

public class VoteController {
    public static Object opcionesVotos(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST, DELETE";
        return armarResponse(response, allowedMethods);
    }
    public static Map<String, Object> HashVoto(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("fechaDeVotacion", LocalDateTime.now());
        return data;
    }

    public static Object obtenerVotos(Request request, Response response) {
        List<User> users = RepositorioUsuario.instancia.findAll();
        List<Map<String,Object>> data = new ArrayList<>();
        users.forEach(u->{
            data.add(HashVoto(u));
        });
        Gson gson = new Gson();
        System.out.println(gson.toJson(data));
        return gson.toJson(data);
    }

    public static Object crearVoto(Request request, Response response) {
        List<User> users = RepositorioUsuario.instancia.findAll();
        Gson gson = new Gson();
        response.status(201);
        HashMap<String,Object> data = (HashMap<String,Object>)HashVoto(users.get(0));
        data.put("id",OL);
        return gson.toJson(data);
    }

    public static Object eliminarVotos(Request request, Response response) {
        response.status(200);
        return response;
    }

    public static Object opcionesVoto(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, PUT, DELETE";
        return armarResponse(response, allowedMethods);
    }

    public static Object obtenerVoto(Request request, Response response) {
        List<User> users = RepositorioUsuario.instancia.findAll();
        Map<String, Object> data = new HashMap<>();
        data.put("usuario", users.get(0));
        data.put("fechaDeVotacion", LocalDateTime.now());
        Gson gson = new Gson();
        String json = gson.toJson(data);
        return gson.toJson(data);
    }

    public static Object editarVoto(Request request, Response response) {
        return response;
    }

    public static Object eliminarVoto(Request request, Response response) {
        response.status(200);
        return response;
    }
}
