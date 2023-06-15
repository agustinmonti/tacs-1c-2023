package org.grupo.tacs.extras;
import spark.Filter;
import spark.Request;
import spark.Response;

import static spark.Spark.halt;

public class RateLimitFilter implements Filter {
    private RateLimiter rateLimiter;

    public RateLimitFilter(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        if (!rateLimiter.allowRequest()) {
            response.status(429); // HTTP 429 Too Many Requests
            response.body("Rate limit exceeded");
            halt();
        }
    }
}