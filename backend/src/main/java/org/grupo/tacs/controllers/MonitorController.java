package org.grupo.tacs.controllers;

import com.google.gson.Gson;
import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.RepositorioUsuario;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class MonitorController {

    public static Object obtenerContadorEventosYHorarios(Request request, Response response) {
        Map<String, Object> data = new HashMap<>();
        data.put("events",50);
        data.put("votes",30);
        Gson gson = new Gson();
        return gson.toJson(data);
    }

}
