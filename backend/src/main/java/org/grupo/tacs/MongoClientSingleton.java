package org.grupo.tacs;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;

import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoClientSingleton {
    private static MongoClient mongoClient;
    //private static String CONNECTION_STRING = "mongodb://host.docker.internal:27017";
    private static String CONNECTION_STRING = System.getenv("MONGODB_CONNECTION_STRING");

    private MongoClientSingleton() {
    }

    public static synchronized MongoClient getInstance() {
        if (mongoClient == null) {
            CodecRegistry pojoCodecRegistry = fromRegistries(
                    MongoClientSettings.getDefaultCodecRegistry(),
                    fromProviders(PojoCodecProvider.builder().automatic(true).build())
            );
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(new ConnectionString(CONNECTION_STRING))
                    .codecRegistry(pojoCodecRegistry)
                    .build();
            mongoClient = MongoClients.create(settings);
        }
        return mongoClient;
    }
}

