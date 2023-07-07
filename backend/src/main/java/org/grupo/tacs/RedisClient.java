package org.grupo.tacs;
import redis.clients.jedis.Jedis;

public class RedisClient {
    private static Jedis jedis;

    private RedisClient() {}

    public static Jedis getInstance(String connectionString) {
        if (jedis == null) {
            String redisConnectionString = connectionString;
            if (redisConnectionString == null) {
                throw new IllegalStateException("REDIS_CONNECTION_STRING environment variable is not set");
            }
            jedis = new Jedis(redisConnectionString);
        }
        return jedis;
    }
}


