package org.grupo.tacs.repos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;
import org.grupo.tacs.MongoClientSingleton;
import org.grupo.tacs.model.Interaction;

import java.time.LocalDateTime;
import java.util.*;

public class InteractionRepository implements Repository<Interaction>{
    MongoClient mongoClient;
    public static final String DB_NAME = "mydb";
    public static final String INTERACTIONS_COLLECTION_NAME = "Interactions";
    public static InteractionRepository instance = new InteractionRepository();
    @Override
    public Interaction findById(String id) {
        return null;
    }

    @Override
    public List<Interaction> findAll() {
        mongoClient = MongoClientSingleton.getInstance();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Interaction> collection = mongodb.getCollection(INTERACTIONS_COLLECTION_NAME, Interaction.class);
            //filtrar por 2 horas o hacer un nuevo metodo para esos
            return collection.find().into(new ArrayList<>());
        }  finally {
            //mongoClient.close(); //cerras el cliente
        }
    }

    @Override
    public void save(Interaction entidad) {
        mongoClient = MongoClientSingleton.getInstance();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Interaction> collection = mongodb.getCollection(INTERACTIONS_COLLECTION_NAME, Interaction.class);
            entidad.setDateTime(LocalDateTime.now());
            collection.insertOne(entidad);
        } finally {
            //mongoClient.close(); //cerras el cliente
        }
    }

    @Override
    public void update(Interaction entidad) {

    }

    @Override
    public void delete(Interaction entidad) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void deleteById(String l) {

    }
    public Map<String, Object> monitoring() { //FUNCION SOLO DE ADMINS
        String fieldName = "dateTime";
        mongoClient = MongoClientSingleton.getInstance();

        try {
            MongoDatabase mongodb = mongoClient.getDatabase(DB_NAME);
            MongoCollection<Interaction> collection = mongodb.getCollection(INTERACTIONS_COLLECTION_NAME, Interaction.class);

            Bson conditionInteractionsEvents = Filters.and(Filters.gte(fieldName, LocalDateTime.now().minusHours(2)), Filters.lt(fieldName, LocalDateTime.now()), Filters.eq("urlPattern","/v2/events"), Filters.eq("method","POST"), Filters.eq("statusCode",201));
            Bson conditionInteractionsVotes = Filters.and(Filters.gte(fieldName, LocalDateTime.now().minusHours(2)), Filters.lt(fieldName, LocalDateTime.now()), Filters.eq("urlPattern","/v2/events/:id/vote"), Filters.eq("method","PUT"), Filters.eq("statusCode",201));
            Bson conditionInteractionsRemovedVotes = Filters.and(Filters.gte(fieldName, LocalDateTime.now().minusHours(2)), Filters.lt(fieldName, LocalDateTime.now()), Filters.eq("urlPattern","/v2/events/:id/vote"), Filters.eq("method","DELETE"), Filters.eq("statusCode",201));
            List<Interaction> filteredInteractions = collection.find(conditionInteractionsEvents).into(new ArrayList<>());
            List<Interaction> voteList = collection.find(conditionInteractionsVotes).into(new ArrayList<>());
            List<Interaction> voteRemovedList = collection.find(conditionInteractionsRemovedVotes).into(new ArrayList<>());

            /*
            Bson condition = Filters.and(Filters.gte(fieldName, LocalDateTime.now().minusHours(2)), Filters.lt(fieldName, LocalDateTime.now()));
            List<Interaction> interactionList = collection.find(condition).into(new ArrayList<>());
            List<Interaction> filteredInteractions = interactionList.stream()
                    .filter(interaction -> interaction.getUrlPattern().equals("/v2/events") && interaction.getMethod() == InteractionMethod.POST && interaction.getStatusCode()==201)
                    .collect(Collectors.toList());
            List<Interaction> voteList = interactionList.stream()
                    .filter(interaction -> interaction.getUrlPattern().equals("/v2/events/:id/vote") && interaction.getMethod() == InteractionMethod.PUT && interaction.getStatusCode()==201)
                    .collect(Collectors.toList());
            //El problema con usar Interacciones para esto es que si un usuario vota y elimina su voto N veces inflaria el numero de votos en las ultimas dos horas.
            //Para evitar esto agregue DELETE /v2/events/:id/vote, de esta forma puedo restar los votos anulados.
            List<Interaction> voteRemovedList = interactionList.stream()
                    .filter(interaction -> interaction.getUrlPattern().equals("/v2/events/:id/vote") && interaction.getMethod() == InteractionMethod.DELETE && interaction.getStatusCode()==201)
                    .collect(Collectors.toList());
            */
            Map<String, Object> data = new HashMap<>();
            data.put("events",filteredInteractions.size());
            data.put("votes",voteList.size()-voteRemovedList.size());
            return data;
        } finally {
            //mongoClient.close(); //cerras el cliente
        }

    }
}
