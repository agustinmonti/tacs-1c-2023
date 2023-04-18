package org.grupo.tacs.controllers;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.Map;

public class MonitorController {

    public static Object obtenerContadorEventosYHorarios(Request request, Response response) {
        Map<String, Object> data = new HashMap<>();
        data.put("events",50);
        data.put("votes",30);
        Gson gson = new Gson();
        return gson.toJson(data);
    }

}
