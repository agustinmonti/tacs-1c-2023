package org.grupo.tacs.extras;

import io.swagger.annotations.Api;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.jaxrs.config.DefaultJaxrsConfig;
import io.swagger.models.*;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.In;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.PathParameter;
import io.swagger.models.parameters.QueryParameter;
import io.swagger.models.properties.*;

import java.util.Arrays;

@Api(tags={"login","users"})
public class SwaggerConfig extends DefaultJaxrsConfig {
    public void configureSwagger() {
        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setVersion("1.0.0");
        beanConfig.setBasePath("/");
        beanConfig.setResourcePackage("org.grupo.tacs.controllers");
        beanConfig.setScan(true);
    }
    public Swagger getSwagger() {
        Swagger swagger = new Swagger();

        Model user = new ModelImpl()
                .name("User")
                .description("User object")
                .property("name", new StringProperty().description("User name"))
                .property("lastname", new StringProperty().description("User lastname"))
                .property("email", new StringProperty().description("User email"))
                .property("password", new StringProperty().description("User password"))
                .example("{ \"name\": \"Bob\",\"lastname\":\"Esponja\",\"email\": \"bobesponja@proton.me\",\"password\": \"abcd1234\",\"confirmPassword\": \"abcd1234\"}");
        swagger.addDefinition("User", user);

        Model credentials = new ModelImpl()
                .name("Credentials")
                .description("Login information")
                .property("email", new StringProperty().description("User email"))
                .property("password", new StringProperty().description("User password"))
                .example("{ \"email\": \"b@hotmail.com\", \"password\": \"2\" }");
        swagger.addDefinition("User", user);

        Model event = new ModelImpl()
                .name("Event")
                .description("Event object")
                .property("name", new StringProperty().description("Event name"))
                .property("desc", new StringProperty().description("Event description"))
                .property("createdBy", new LongProperty().description("Created by user"))
                .property("options",new ArrayProperty().description("Possible Event time segments")
                    .items(new ObjectProperty()
                            .property("start", new DateTimeProperty().description("Start time"))
                            .property("end", new DateTimeProperty().description("End time"))
                            .property("votes", new ArrayProperty().description("Votes by users")
                                    .items(new ObjectProperty()
                                            .property("user", new ObjectProperty().description("User that voted this option"))
                                            .property("votingDate",new DateTimeProperty().description("Time of Vote"))))))
                .property("participants", new ArrayProperty().description("Participant List")
                        .items(new ObjectProperty()
                                .property("User", new ObjectProperty().description("A participant"))))
                .example("{ \"name\": \"Evento Aburrido\",\"desc\":\"Un evento que no es divertido\",\"participants\": [],\"options\":[{\"start\": \"2022-01-01T12:00:00Z\",\"end\": \"2022-01-01T14:00:00Z\",\"votes\": []},{\"start\": \"2022-01-01T15:00:00Z\",\"end\": \"2022-01-01T17:00:00Z\",\"votes\": []}]}");
        swagger.addDefinition("Event",event);

        Model eventOption = new ModelImpl()
                .name("EventOption")
                .description("EventOption id or index")
                .property("optionIndex",new StringProperty().description("Used to identify the EventOption to vote"))
                .example("{\"optionIndex\": \"1\"}");
        swagger.addDefinition("EventOption",eventOption);

        swagger.info(new Info()
                .title("TACS-1C-2023")
                .version("1.0.0")
                .description("A sample RESTful API built with Spark Java and Swagger"));

        swagger.securityDefinition("JWT", new ApiKeyAuthDefinition("Authorization", In.HEADER));

        swagger.tag(new Tag().name("users").description("Operaciones con cuentas de usuario"));
        swagger.tag(new Tag().name("login").description("Operaciones de autenticación"));
        swagger.tag(new Tag().name("events").description("Operaciones con eventos"));
        swagger.tag(new Tag().name("monitoring").description("Muestra datos de marketing"));

        swagger.path("/v2/users", new Path()
                .post(new Operation()
                        .tags(Arrays.asList("users"))
                        .summary("Crear una cuenta de usuario")
                        .description("Crea una cuenta de usuario nueva")
                        .parameter(new BodyParameter()
                                .name("user")
                                .description("Datos del usuario a crear")
                                .schema(user))
                        .response(201, new Response()
                                .description("User successfully created"))
                        .response(409,new Response()
                                .description("This email is already in use"))
                        .response(500, new Response())));

        swagger.path("/v2/users/{id}", new Path()
                .get(new Operation()
                        .tag("users")
                        .summary("Trae informacion del usuario")
                        .description("Trae informacion del usuario, con informacion adicional si tiene autorizacion")
                        .parameter(new PathParameter()
                                .name("id")
                                .description("ID del usuario a buscar")
                                .required(true)
                                .type("string")
                                .format("ObjectId"))
                        .response(200,new Response().description("Ok"))
                        .response(404,new Response().description("Usuario no encontrado!"))));

        swagger.path("/v2/auth/login", new Path()
                .post(new Operation()
                        .tags(Arrays.asList("login"))
                        .summary("Generate JWT token for user")
                        .description("Generate JWT token for an existing user")
                        .parameter(new BodyParameter()
                                .name("credentials")
                                .description("Credenciales de autenticación")
                                .schema(credentials))
                        .response(200, new Response()
                                .description("Autenticación exitosa")
                                .header("Authorization", new StringProperty().description("JWT token")))
                        .response(401, new Response()
                                .description("Credenciales inválidas"))
                        .response(500, new Response())));

        swagger.path("/v2/auth/renew", new Path()
                .post(new Operation()
                        .tag("login")
                        .summary("Regenrate JWT token for user")
                        .description("regenerate JWT token for an existing user")
                        .response(200,new Response()
                                .description("Ok"))
                        .response(401,new Response()
                                .description("Unauthorized"))
                        .response(500,new Response().description("Server Error")))
        );

        swagger.path("/v2/events", new Path()
                .post(new Operation()
                        .tags(Arrays.asList("events"))
                        .summary("Crear un nuevo evento")
                        .description("El usuario crea un nuevo evento con un numero de opciones")
                        .parameter(new BodyParameter()
                                .name("event")
                                .description("Datos del evento a crear")
                                .schema(event))
                        //.security(new SecurityRequirement().requirement("jwt", Arrays.asList("read","write")))
                        .response(201, new Response()
                                .description("Evento creado satisfactoriamente"))
                        .response(401, new Response()
                                .description("Unauthorized"))
                        .response(500, new Response()))
                .get(new Operation()
                        .tag("events")
                        .summary("Retorna los eventos que creo un usuario o en los que esta participando")
                        .description("Retorna todos los eventos que creo un usuario o en los que esta participando")
                        .parameter(new QueryParameter()
                                .name("userId")
                                .description("ObjectId of a User"))
                        .response(200,new Response()
                                .description("ok"))
                        .response(400,new Response()
                                .description("Invalid userId parameter"))));


        swagger.path("/v2/events/{id}", new Path()
                .get(new Operation()
                        .tags(Arrays.asList("events"))
                        .summary("Obtener un evento por ID")
                        .description("Obtiene un evento registrado por ID")
                        .parameter(new PathParameter()
                                .name("id")
                                .description("ID del evento a buscar")
                                .required(true)
                                .type("string")
                                .format("ObjectId"))
                        .response(200, new Response()
                                .description("Evento encontrado"))
                        .response(404, new Response()
                                .description("Evento no encontrado"))
                        .response(500, new Response()))
                .put(new Operation()
                        .tag("events")
                        .summary("Actualiza el estado del Evento")
                        .description("Cambia el estado de un evento entre Abierto y Cerrado")
                        .parameter(new PathParameter()
                                .name("id")
                                .description("ID del evento a buscar")
                                .required(true)
                                .type("string")
                                .format("ObjectId"))
                        .response(201,new Response().description("Evento actualizado"))
                        .response(404,new Response().description("Evento no encontrado"))));

        swagger.path("/v2/events/{id}/vote",new Path()
                .put(new Operation()
                        .tags(Arrays.asList("events"))
                        .summary("Votar una opcion de evento")
                        .description("A partir del id del evento el usuario vota una opcion")
                        .parameter(new PathParameter()
                                .name("id")
                                .description("Id del evento a buscar")
                                .required(true)
                                .type("string")
                                .format("ObjectId"))
                        .parameter(new BodyParameter()
                                .name("EventOption")
                                .schema(eventOption))
                        .response(201, new Response()
                                .description("voto realizado o retirado"))
                        .response(404, new Response()
                                .description("No se encontro el evento o su opcion a votar"))
                        .response(401, new Response()
                                .description("Unauthorized"))
                        .response(500, new Response()))
                .delete(new Operation()
                        .tags(Arrays.asList("events"))
                        .summary("Eliminar Voto")
                        .description("A partir del id del evento el usuario elimina el voto de una opcion")
                        .parameter(new PathParameter()
                                .name("id")
                                .description("Id del evento a buscar")
                                .required(true)
                                .type("string")
                                .format("ObjectId"))
                        .parameter(new BodyParameter()
                                .name("EventOption")
                                .schema(eventOption))
                        .response(201, new Response()
                                .description("Voto Eliminado"))
                        .response(404, new Response()
                                .description("No se encontro el evento o su opcion a votar"))
                        .response(401, new Response()
                                .description("Unauthorized"))
                        .response(500, new Response())));

        swagger.path("/v2/events/{id}/participant",new Path()
                .put(new Operation()
                        .tag("events")
                        .summary("Permite subscribirse a un evento")
                        .description("Un usuario logeado puede subscribirse a un evento")
                        .parameter(new PathParameter()
                                .name("id")
                                .description("Id del evento a buscar")
                                .required(true)
                                .type("string")
                                .format("ObjectId"))
                        .response(201,new Response()
                                .description("participación actualizada"))
                        .response(404, new Response()
                                .description("No se encontro el evento"))
                        .response(401, new Response()
                                .description("Unauthorized"))
                        .response(500, new Response())));

        swagger.path("/v2/monitoring", new Path()
                .get(new Operation()
                        .tag("monitoring")
                        .summary("Trae datos relevantes a marketing en un JSON")
                        .description("Trae datos relevantes a marketing en un JSON, cantidad de opciones de eventos votadas en las ultimas dos horas")
                        .response(200, new Response()
                                .description("ok"))
                        .response(500, new Response())));

        return swagger;

    }
}
