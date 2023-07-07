package org.grupo.tacs.extras;

import org.grupo.tacs.RedisClient;
import redis.clients.jedis.Jedis;

public class RedisRateLimiter {
    private final int MAX_REQUESTS; // Maximum number of requests allowed within the time window
    private final int TIME_WINDOW_SECONDS; // Time window in seconds
    private final int RATE_LIMIT_WINDOW_MS; // Time window in milliseconds

    private Jedis jedis;

    public RedisRateLimiter(String connectionString, int maxRequests, int timeWindowSeconds, int rateLimitWindowMs) {
        this.MAX_REQUESTS = maxRequests;
        this.TIME_WINDOW_SECONDS = timeWindowSeconds;
        this.RATE_LIMIT_WINDOW_MS = rateLimitWindowMs;
        this.jedis = RedisClient.getInstance(connectionString);
    }

    public boolean isAllowed(String ipAddress) {
        String clientKey = "rate_limit:" + ipAddress;

        long requestCount = jedis.incr(clientKey);
        if (requestCount == 1) {
            jedis.expire(clientKey, TIME_WINDOW_SECONDS);
        } else if (requestCount > MAX_REQUESTS) {
            return false;
        }

        // Calculate the expiration time based on the rate limit window in milliseconds
        long expirationTime = RATE_LIMIT_WINDOW_MS / 1000;
        if (requestCount == MAX_REQUESTS && jedis.ttl(clientKey) == -1) {
            jedis.expire(clientKey, (int) expirationTime);
        }

        return true;
    }

    public void setJedis(Jedis jedisMock) {
        this.jedis = jedisMock;
    }
}
