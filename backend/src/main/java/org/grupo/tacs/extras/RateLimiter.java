package org.grupo.tacs.extras;

public class RateLimiter {
    private int capacity;
    private int tokens;
    private long lastRefillTime;
    private final long refillInterval;

    public RateLimiter(int capacity, int tokens, long refillInterval) {
        this.capacity = capacity;
        this.tokens = tokens;
        this.refillInterval = refillInterval;
        this.lastRefillTime = System.currentTimeMillis();
    }

    public synchronized boolean allowRequest() {
        refillTokens();
        if (tokens > 0) {
            tokens--;
            return true;
        }
        return false;
    }

    private void refillTokens() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - lastRefillTime;
        int newTokens = (int) (elapsedTime / refillInterval);
        if (newTokens > 0) {
            tokens = Math.min(capacity, tokens + newTokens);
            lastRefillTime = currentTime;
        }
    }
}
