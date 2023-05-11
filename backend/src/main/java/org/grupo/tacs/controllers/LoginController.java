package org.grupo.tacs.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.model.Filters;
import io.swagger.annotations.*;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.grupo.tacs.excepciones.UnauthorizedException;
import org.grupo.tacs.excepciones.UserDoesNotExistException;
import org.grupo.tacs.excepciones.WrongPasswordException;
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
        response.header("Allow", allowedMethods);
        return response;
    }
    @ApiOperation(value = "Generate JWT token for user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Autenticación exitosa",
                    responseHeaders = {
                            @ResponseHeader(name = "Authorization", description = "JWT token", response = String.class)
                    }),
            @ApiResponse(code = 401, message = "Credenciales inválidas")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public static Object loginJWT(Request request, Response response) {
        List<User> users = UserRepository.instance.findAll();
        Gson gson = new Gson();
        String token = null;
        try {
            JsonObject jsonObject = gson.fromJson(request.body(), JsonObject.class);
            String email = jsonObject.get("email").getAsString();
            String password = jsonObject.get("password").getAsString();
            User user = users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElseThrow(UserDoesNotExistException::new);
            token = generateToken(user);
            response.header("Authorization", "Bearer " + token);
            if (!user.getPassword().equals(password)) {
                throw new WrongPasswordException();
            }
            response.status(200);
        } catch (WrongPasswordException | UserDoesNotExistException e) {
            response.status(401);
            return e;
        } catch (Exception e) {
            response.status(500);
        } finally {
            response.type("text/plain");
            return "Bearer "+token;
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
    static User getVerifiedUserFromToken(Request request) {
        String token = request.headers("Authorization").replace("Bearer ", "");
        if(token == null)
            throw new UnauthorizedException("Unauthorized, No Token");
        //System.out.println("Verify:"+token);
        Algorithm algorithm = Algorithm.HMAC256(JWT_H256_SECRET_PHRASE);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(JWT_ISSUSER)
                .build();

        DecodedJWT jwt = verifier.verify(token);
        String userId = jwt.getSubject();
        User user = UserRepository.instance.findById(userId);
        if(user == null)
            throw new UnauthorizedException("Invalid Token");
        return user;
    }
}
