package org.grupo.tacs.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.model.Filters;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.grupo.tacs.excepciones.WrongPasswordException;
import org.grupo.tacs.excepciones.UserDoesNotExistException;
import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.UserRepository;
import spark.Request;
import spark.Response;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

@Api(tags = {"login"})
public class LoginController {
    static String JWT_ISSUSER = "TACS-1C-2023-backend";
    static String JWT_H256_SECRET_PHRASE = "%WP*Ia&wYwdOlHE(kTS(FWxn.N,SuPBg";

    public static User getUserSession(Request request, Response response){
        existingSession(request,response,"id");
        return UserRepository.instance.findById(request.session().attribute("id"));
    }
    public static void existingSession(Request request, Response response, String atributo) {
        if(Objects.isNull(request.session().attribute(atributo))){
            response.status(500);
            response.redirect("/");
        }
    }

    /**
     *
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "Authenticate a user", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authentication successful"),
            @ApiResponse(code = 401, message = "Invalid credentials")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public static Object login(Request request, Response response) {
        List<User> usuarios = UserRepository.instance.findAll();
        Map<String, ObjectId> myMap = new HashMap<String, ObjectId>();
        Gson gson = new Gson();
        try{
            //String email = request.queryParams("email");
            //String password = request.queryParams("password");
            String requestBody = request.body();
            Map<String, Object> jsonMap = gson.fromJson(requestBody, Map.class);
            String email = (String) jsonMap.get("email");
            String password = (String) jsonMap.get("password");
            Bson condition = Filters.eq("email", email);
            User usuario = usuarios.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElseThrow(UserDoesNotExistException::new);
            if(!usuario.getPassword().equals(password)){
                throw new WrongPasswordException();
            }
            request.session(true);
            request.session().attribute("id", usuario.getId());
            response.removeCookie("error");
            myMap.put("id", usuario.getId());
            //myMap.put("id", Long.decode("0x"+usuario.getId().toHexString()));
            response.body(gson.toJson(myMap));

        }catch(WrongPasswordException | UserDoesNotExistException e){
            response.cookie("error", "Wrong Credentials", 1);
            response.redirect("/");
        }catch (Exception e) {
            response.status(500);
        }finally {
            return gson.toJson(myMap);
        }
    }
    @ApiOperation(value = "Log out a user", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Logout successful"),
            @ApiResponse(code = 401, message = "Unauthorized")
    })
    @GET
    public Response logout(Request request, Response response){
        request.session().invalidate();
        response.redirect("/");
        return response;
    }

    public static Object getOptions(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST";
        response.status(200);
        return response;
    }

    public static Object loginJWT(Request request, Response response) {
        List<User> usuarios = UserRepository.instance.findAll();
        Map<String, ObjectId> myMap = new HashMap<String, ObjectId>();
        Gson gson = new Gson();
        try {
            //String email = request.queryParams("email");
            //String password = request.queryParams("password");
            String requestBody = request.body();
            Map<String, Object> jsonMap = gson.fromJson(requestBody, Map.class);
            String email = (String) jsonMap.get("email");
            String password = (String) jsonMap.get("password");
            Bson condition = Filters.eq("email", email);
            User usuario = usuarios.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElseThrow(UserDoesNotExistException::new);
            String token = generateToken(usuario);
            response.header("Authorization", "Bearer " + token);
            if (!usuario.getPassword().equals(password)) {
                throw new WrongPasswordException();
            }
        }catch(WrongPasswordException | UserDoesNotExistException e){
            response.cookie("error", "Wrong Credentials", 1);
            response.redirect("/");
        }catch (Exception e) {
            response.status(500);
        }finally {
            return gson.toJson(myMap);
        }
    }

    private static String generateToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_H256_SECRET_PHRASE);
        String token = JWT.create()
                .withIssuer(JWT_ISSUSER)
                .withSubject(user.getId().toHexString())
                .withExpiresAt(Date.from(Instant.now().plus(Duration.ofDays(30))))
                .sign(algorithm);
        //System.out.println("Login:"+token);
        return token;
    }
    static String getVerifiedUserFromToken(Request request) {
        String token = request.headers("Authorization").replace("Bearer ", "");
        //System.out.println("Verify:"+token);
        Algorithm algorithm = Algorithm.HMAC256(JWT_H256_SECRET_PHRASE);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(JWT_ISSUSER)
                .build();

        DecodedJWT jwt = verifier.verify(token);
        String userId = jwt.getSubject();
        return userId;
    }
}
