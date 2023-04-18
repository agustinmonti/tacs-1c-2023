package org.grupo.tacs.controllers;

import com.google.gson.Gson;
import org.grupo.tacs.model.EventOption;
import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.EventOptionRepository;
import org.grupo.tacs.repos.UserRepository;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.*;

import static org.grupo.tacs.controllers.UserController.buildResponse;

public class EventOptionController {
    public static HashMap<String, Object> HashOption(Long optionId,Long eventId) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("id",optionId);
        data.put("idEvent", eventId);
        data.put("start", LocalDateTime.now());
        data.put("end",LocalDateTime.of(2023,4,18,19,0,0));
        return data;
    }

    public static Object getOptionsMethodOptions(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST, DELETE";
        return buildResponse(response, allowedMethods);
    }

    public static Object getOptions(Request request, Response response) {
        List<HashMap<String,Object>> data = new ArrayList<>();
        for (Long i=0L;i<=3L;i++){
            data.add(HashOption(i,0L));
        }
        Gson gson = new Gson();
        response.status(200);
        return gson.toJson(data);
    }

    public static Object newOption(Request request, Response response) {
        //TODO Verificar con la session que el usuario sea el creador del evento
        Gson gson = new Gson();
        EventOption newEventOption = gson.fromJson(request.body(),EventOption.class);
        EventOptionRepository.instance.save(newEventOption);
        return gson.toJson(newEventOption);
    }

    public static Object deleteAllOptions(Request request, Response response) {
        /*TODO agregar un metodo a EventOptionRepository para borrar todas las Opciones que tengan
          el eventOptionParentId==eventId.
        * */
        EventOptionRepository.instance.deleteAll();
        response.status(200);
        return response;
    }

    public static Object getOptionMethodOptions(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, PUT, DELETE";
        return buildResponse(response, allowedMethods);
    }

    public static Object getOption(Request request, Response response) {
        Gson gson = new Gson();
        String respuesta = "";
        try {
            EventOption eventOption = EventOptionRepository.instance.findById(Long.parseLong(request.params(":id")));
            response.status(200);
            respuesta = gson.toJson(eventOption);
        }catch (NoSuchElementException e){
            response.status(404);
            respuesta = gson.toJson("Not Found!");
        }
        return respuesta;
    }

    public static Object updateOption(Request request, Response response) {
        //TODO Verificar con la session que el usuario sea el creador del evento
        Gson gson = new Gson();
        Object requestBody = gson.fromJson(response.body(),Object.class);
        EventOption old = EventOptionRepository.instance.findById(Long.parseLong(request.params(":id")));
        response.status(200);
        if (requestBody instanceof Map) {
            Map<String, Object> requestMap = (Map<String, Object>) requestBody;
            if (requestMap.containsKey("start")) {
                old.setStart((LocalDateTime) requestMap.get("start"));
            }
            if (requestMap.containsKey("end")) {
                old.setEnd((LocalDateTime) requestMap.get("end"));
            }
        }
        EventOptionRepository.instance.save(old);
        String respuesta = gson.toJson(old);
        return respuesta;
    }

    public static Object deleteOption(Request request, Response response) {
        //TODO Verificar con la session que el usuario sea el creador del evento
        EventOptionRepository.instance.deleteById(Long.parseLong(request.params(":id")));
        return response;
    }
}
