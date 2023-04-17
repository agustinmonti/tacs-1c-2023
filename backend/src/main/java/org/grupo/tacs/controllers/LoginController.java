package org.grupo.tacs.controllers;

import com.google.gson.Gson;
import org.grupo.tacs.excepciones.PasswordIncorrectaException;
import org.grupo.tacs.excepciones.UsuarioInexistenteException;
import org.grupo.tacs.model.User;
import org.grupo.tacs.repos.RepositorioUsuario;
import spark.Request;
import spark.Response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static javax.swing.UIManager.put;

public class LoginController {

    public static User obtenerUsuarioSession(Request request, Response response){
        existeAtributoEnSession(request,response,"id");
        return RepositorioUsuario.instancia.findById(request.session().attribute("id"));
    }
    public static void existeAtributoEnSession(Request request, Response response,String atributo) {
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
    public static Object login(Request request, Response response) {
        List<User> usuarios = RepositorioUsuario.instancia.findAll();
        Map<String, Long> myMap = new HashMap<String, Long>();
        Gson gson = new Gson();
        try{
            //String email = request.queryParams("email");
            //String password = request.queryParams("password");
            String requestBody = request.body();
            Map<String, Object> jsonMap = gson.fromJson(requestBody, Map.class);
            String email = (String) jsonMap.get("email");
            String password = (String) jsonMap.get("password");
            User usuario = usuarios.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElseThrow(UsuarioInexistenteException::new);
            if(!usuario.getPasswordHash().equals(password)){
                throw new PasswordIncorrectaException();
            }
            request.session(true);
            request.session().attribute("id", usuario.getId());
            response.removeCookie("error");
            myMap.put("id", usuario.getId());
            response.body(gson.toJson(myMap));

        }catch(PasswordIncorrectaException | UsuarioInexistenteException e){
            response.cookie("error", "Credenciales incorrectas", 1);
            response.redirect("/");
        }catch (Exception e) {
            response.status(500);
        }finally {
            return gson.toJson(myMap);
        }
    }
    public Response logout(Request request, Response response){
        request.session().invalidate();
        response.redirect("/");
        return response;
    }

    public static Object obtenerOptions(Request request, Response response) {
        String allowedMethods = "OPTIONS, GET, POST";
        response.status(200);
        return response;
    }
}
