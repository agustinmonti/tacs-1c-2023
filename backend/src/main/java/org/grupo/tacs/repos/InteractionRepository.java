package org.grupo.tacs.repos;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.grupo.tacs.MongoDB;
import org.grupo.tacs.model.Interaction;

import java.util.ArrayList;
import java.util.List;

public class InteractionRepository implements Repository<Interaction>{
    MongoClient mongoClient;
    public static InteractionRepository instance = new InteractionRepository();
    @Override
    public Interaction findById(String id) {
        return null;
    }

    @Override
    public List<Interaction> findAll() {
        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Interaction> collection = mongodb.getCollection("Interacions", Interaction.class);
            //filtrar por 2 horas o hacer un nuevo metodo para esos
            return collection.find().into(new ArrayList<>());
        }  finally {
            mongoClient.close(); //cerras el cliente
        }
    }

    @Override
    public void save(Interaction entidad) {
        mongoClient = MongoDB.getMongoClient();
        try {
            MongoDatabase mongodb = mongoClient.getDatabase("mydb");
            MongoCollection<Interaction> collection = mongodb.getCollection("Interacions", Interaction.class);
            collection.insertOne(entidad);
        } finally {
            mongoClient.close(); //cerras el cliente
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
}
