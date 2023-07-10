package org.grupo.tacs.extras;

import redis.clients.jedis.Jedis;
import spark.Filter;
import spark.Request;
import spark.Response;

import java.time.Instant;

import static spark.Spark.halt;

public class DistributedRateLimiterFilter implements Filter  {
    private static final int MAX_REQUESTS = 10;
    private static final int RATE_LIMIT_DURATION_SECONDS = 10;
    private final String redisConnectionString;
    public DistributedRateLimiterFilter(String redisConnectionString){
        this.redisConnectionString = redisConnectionString;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        String clientId = getClientIdentifier(request);

        try (Jedis jedis = new Jedis(redisConnectionString)) {
            String key = "rate_limit:" + clientId + ":" + RATE_LIMIT_DURATION_SECONDS;

            long currentTimestamp = Instant.now().getEpochSecond();
            long rateLimitStartTimestamp = currentTimestamp - RATE_LIMIT_DURATION_SECONDS;

            long requestCount = jedis.zcount(key, rateLimitStartTimestamp, currentTimestamp);

            if (requestCount >= MAX_REQUESTS) {
                response.status(429);
                response.body("Rate limit exceeded");
                halt(429);
            } else {
                jedis.zadd(key, currentTimestamp, String.valueOf(currentTimestamp));
                jedis.zremrangeByScore(key, 0, rateLimitStartTimestamp);
            }
        }
    }

    private String getClientIdentifier(Request request) {
        // Returnar un identificador unico, en este caso la IP, podria armar uno separado con user id.
        return request.ip();
    }
}
