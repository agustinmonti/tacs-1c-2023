package org.grupo.tacs.extras;

import io.swagger.annotations.*;
import io.swagger.jaxrs.config.*;
import io.swagger.models.*;
import io.swagger.models.Info;
import io.swagger.models.Tag;
import io.swagger.models.auth.*;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.auth.OAuth2Definition;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;

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
                .example("{ \"name\": \"Bob\",\"lastname\":\"Esponja\",\"email\": \"bobesponja@proton.me\",\"password\": \"abcd1234\"}");
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
                .property("createdBy", new LongProperty().description("Created by user"))
                .property("options",new ArrayProperty().description("Possible Event time segments")
                    .items(new ObjectProperty()
                            .property("start", new DateTimeProperty().description("Start time"))
                            .property("end", new DateTimeProperty().description("End time"))))
                .property("participants", new ArrayProperty().description("Participant List")
                        .items(new ObjectProperty()
                                .property("participantId", new StringProperty().description("ObjectId of users subscribed to this event"))))
                //.example("{ \"name\": \"Evento Aburrido\",\"desc\":\"Un evento que no es divertido\",\"options\":[{\"start\": \"2022-01-01T12:00:00Z\",\"end\": \"2022-01-01T14:00:00Z\"},{\"start\": \"2022-01-01T15:00:00Z\",\"end\": \"2022-01-01T17:00:00Z\"}], \"participants\": [\"64517fead086c42a7d2acc73\", \"64517fead086c42a7d2acc75\", \"64517fead086c42a7d2acc77\"] }");
                        .example("{ \"name\": \"Evento Aburrido\",\"desc\":\"Un evento que no es divertido\",\"options\":[{\"start\": \"2022-01-01T12:00:00Z\",\"end\": \"2022-01-01T14:00:00Z\"},{\"start\": \"2022-01-01T15:00:00Z\",\"end\": \"2022-01-01T17:00:00Z\"}]}");
        swagger.addDefinition("Event",event);

        Model eventOption = new ModelImpl()
                .name("EventOption")
                .description("Un JSON con el option id")
                .property("optionId",new StringProperty().description("ObjectId of an EventOption"))
                .example("{\"_id\": \"1\"}");
        swagger.addDefinition("EventOption",eventOption);

        //String exampleJson = "{ \"name\": \"Evento Aburrido\", \"createdBy\": 1, \"guests\": [\"Julia\", \"Bernardo\", \"Bob Esponja\"] }";

        swagger.info(new Info()
                .title("TACS-1C-2023")
                .version("1.0.0")
                .description("A sample RESTful API built with Spark Java and Swagger"));

        /*ApiKeyAuthDefinition securityScheme = new ApiKeyAuthDefinition();
        securityScheme.setType("apiKey");
        securityScheme.setIn(In.HEADER);
        securityScheme.setName("Authorization");
        securityScheme.setDescription("Enter the token with the `Bearer: ` prefix, e.g. \"Bearer abcde12345\".");

        swagger.securityDefinition("Bearer", securityScheme);
        SecurityRequirement securityRequirement = new SecurityRequirement();
        securityRequirement.requirement("Bearer", new ArrayList<>());*/

        swagger.securityDefinition("jwt", new ApiKeyAuthDefinition("Authorization", In.HEADER));

        swagger.tag(new Tag().name("login").description("Operaciones de autenticación"));
        swagger.tag(new Tag().name("users").description("Operaciones con cuentas de usuario"));
        swagger.tag(new Tag().name("events").description("Operaciones con eventos"));
        swagger.tag(new Tag().name("monitoring").description("Muestra datos de marketing"));

        /*swagger.path("/auth/login", new Path()
                .post(new Operation()
                        .tags(Arrays.asList("login"))
                        .summary("Autenticar un usuario")
                        .description("Autenticar a un usuario existente")
                        .parameter(new BodyParameter()
                                .name("credentials")
                                .description("Credenciales de autenticación")
                                .schema(credentials))
                        .response(200, new Response()
                                .description("Autenticación exitosa"))
                        .response(401, new Response()
                                .description("Credenciales inválidas"))));*/


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
                                .description("Credenciales inválidas"))));

        swagger.path("/users", new Path()
                .post(new Operation()
                        .tags(Arrays.asList("users"))
                        .summary("Crear una cuenta de usuario")
                        .description("Crea una cuenta de usuario nueva")
                        .parameter(new BodyParameter()
                                .name("user")
                                .description("Datos del usuario a crear")
                                .schema(user))
                        .response(201, new Response()
                                .description("Usuario creado satisfactoriamente"))));

        swagger.path("/v2/events", new Path()
                .get(new Operation()
                        .tags(Arrays.asList("events"))
                        .summary("Obtener todos los eventos")
                        .description("Obtiene una lista con todos los eventos registrados")
                        .response(200, new Response()
                                .description("Lista de eventos")))
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
                        .response(401, new Response().description("BOOM!"))));

        swagger.path("/events/{id}", new Path()
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
                                .description("Evento no encontrado"))));

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
                                .description("Se voto correctamente"))
                        .response(404, new Response()
                                .description("No se encontro el evento o su opcion a votar"))));

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
                                .description("El usuario se subscribio correctamente"))
                        .response(404, new Response()
                                .description("No se encontro el evento"))));

        swagger.path("/monitoring", new Path()
                .get(new Operation()
                        .tag("monitoring")
                        .summary("Trae datos relevantes a marketing en un JSON")
                        .description("Trae datos relevantes a marketing en un JSON, cantidad de opciones de eventos votadas en las ultimas dos horas")
                        .response(200, new Response()
                                .description("ok"))));

        return swagger;

    }
}
