package org.grupo.tacs.extras;

import io.swagger.annotations.*;
import io.swagger.jaxrs.config.*;
import io.swagger.models.*;
import io.swagger.models.Info;
import io.swagger.models.Tag;
import io.swagger.models.auth.*;
import io.swagger.models.auth.ApiKeyAuthDefinition;
import io.swagger.models.parameters.*;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.LongProperty;
import io.swagger.models.properties.StringProperty;

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
                .property("email", new StringProperty().description("User email"))
                .property("passwordHash", new StringProperty().description("User password"))
                .example("{ \"name\": \"Bob Esponja\",\"email\": \"bobesponja@proton.me\",\"passwordHash\": \"abcd1234\"}");
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
                .property("guests", new ArrayProperty().description("Lista de Invitados"))
                .example("{ \"name\": \"Evento Aburrido\", \"createdBy\": 1, \"guests\": [\"Julia\", \"Bernardo\", \"Bob Esponja\"] }");

        swagger.addDefinition("Event",event);

        Model eventOption = new ModelImpl()
                .name("EventOption")
                .description("Un JSON con el option id")
                .property("optionId",new LongProperty().description("Option Id"))
                .example("{\"optionId\": 1}");
        swagger.addDefinition("EventOption",eventOption);

        //String exampleJson = "{ \"name\": \"Evento Aburrido\", \"createdBy\": 1, \"guests\": [\"Julia\", \"Bernardo\", \"Bob Esponja\"] }";

        swagger.info(new Info()
                .title("TACS-1C-2023")
                .version("1.0.0")
                .description("A sample RESTful API built with Spark Java and Swagger"));
        swagger.securityDefinition("api_key", new ApiKeyAuthDefinition("api_key", In.HEADER));

        swagger.tag(new Tag().name("login").description("Operaciones de autenticación"));
        swagger.tag(new Tag().name("users").description("Operaciones con cuentas de usuario"));
        swagger.tag(new Tag().name("events").description("Operaciones con eventos"));
        swagger.tag(new Tag().name("monitoring").description("Muestra datos de marketing"));

        swagger.path("/auth/login", new Path()
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

        swagger.path("/events", new Path()
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
                        .response(201, new Response()
                                .description("Evento creado satisfactoriamente"))
                        .response(401, new Response().description("BOOM!"))));

        swagger.path("/events/:id", new Path()
                .get(new Operation()
                        .tags(Arrays.asList("events"))
                        .summary("Obtener un evento por ID")
                        .description("Obtiene un evento registrado por ID")
                        .parameter(new PathParameter()
                                .name("id")
                                .description("ID del evento a buscar")
                                .required(true)
                                .type("integer")
                                .format("int64"))
                        .response(200, new Response()
                                .description("Evento encontrado"))
                        .response(404, new Response()
                                .description("Evento no encontrado"))));

        swagger.path("/events/:id/vote",new Path()
                .put(new Operation()
                        .tags(Arrays.asList("events"))
                        .summary("Votar una opcion de evento")
                        .description("A partir del id del evento el usuario vota una opcion")
                        .parameter(new PathParameter()
                                .name("id")
                                .description("Id del evento a buscar")
                                .required(true)
                                .type("integer")
                                .format("int64"))
                        .parameter(new BodyParameter()
                                .name("EventOption")
                                .schema(eventOption))
                        .response(201, new Response()
                                .description("Se voto correctamente"))
                        .response(404, new Response()
                                .description("No se encontro el evento o su opcion a votar"))));

        swagger.path("/events/:id/participant",new Path()
                .put(new Operation()
                        .tag("events")
                        .summary("Permite subscribirse a un evento")
                        .description("Un usuario logeado puede subscribirse a un evento")
                        .parameter(new PathParameter()
                                .name("id")
                                .description("Id del evento a buscar")
                                .required(true)
                                .format("int64"))
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
