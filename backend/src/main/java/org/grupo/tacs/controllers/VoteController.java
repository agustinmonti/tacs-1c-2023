package org.grupo.tacs.controllers;

import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import org.grupo.tacs.extras.Helper;
import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.UserRepository;
import spark.Request;
import spark.Response;

import java.time.LocalDateTime;
import java.util.*;

import static javax.swing.text.html.HTML.Tag.OL;
import static org.grupo.tacs.controllers.UserController.buildResponse;

public class VoteController {
    public static Object getVotesOptions(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST, DELETE";
        return buildResponse(response, allowedMethods);
    }
    public static Map<String, Object> HashVoto(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("user", user);
        data.put("readAbleVotingDate", Helper.getReadableDate(LocalDateTime.now()));
        return data;
    }

    public static Object getVotes(Request request, Response response) {
        List<User> users = UserRepository.instance.findAll();
        List<Map<String,Object>> data = new ArrayList<>();
        users.forEach(u->{
            data.add(HashVoto(u));
        });
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public static Object createVote(Request request, Response response) {
        List<User> users = UserRepository.instance.findAll();
        Gson gson = new Gson();
        response.status(201);
        HashMap<String,Object> data = (HashMap<String,Object>)HashVoto(users.get(0));
        data.put("id",OL);
        return gson.toJson(data);
    }

    public static Object deleteAllVotes(Request request, Response response) {
        response.status(200);
        return response;
    }

    public static Object getVoteOptions(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, DELETE";
        return buildResponse(response, allowedMethods);
    }

    public static Object getVote(Request request, Response response) {
        String date = Helper.getReadableDate(LocalDateTime.now());
        List<User> users = UserRepository.instance.findAll();
        Map<String, Object> data = new HashMap<>();
        data.put("user", users.get(0));
        data.put("readAbleVotingDate", date);
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public static Object deleteVote(Request request, Response response) {
        response.status(200);
        return response;
    }
}
