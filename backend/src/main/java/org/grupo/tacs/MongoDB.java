package org.grupo.tacs;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.grupo.tacs.model.User;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDB {
    public static MongoDatabase mongodb() {

        CodecRegistry pojoCodecRegistry = fromRegistries(MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));
        String connectionString = "mongodb+srv://gciruzzi:123@mongo.xebsbis.mongodb.net/?retryWrites=true&w=majority";
        //String connectionString = "mongodb+srv://gciruzzi:123@mongodb:27017/?retryWrites=true&w=majority";
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString(connectionString))
                .codecRegistry(pojoCodecRegistry)
                .build();

        MongoClient mongoClient = MongoClients.create(settings);
        return mongoClient.getDatabase("mydb");
        /*try (MongoClient mongoClient = MongoClients.create(settings)) {
            try {
                MongoDatabase database = mongoClient.getDatabase("mydb");
                MongoCollection<User> collection = database.getCollection("Users", User.class);
                User pepe = new User("pepe", "pepe@gmail.com", "pepeHash").setId(54L);
                //Bson condition = Filters.eq("_id", 1L);
                //collection.findOneAndUpdate(condition, Updates.set("user_name","ivana"));
                collection.insertOne(pepe);
                mongoClient.close();
            } catch (MongoException e) {
                e.printStackTrace();
            }
        }*/
    }
}