package org.grupo.tacs.extras;
import spark.Filter;
import spark.Request;
import spark.Response;
public class CorsOptionsFilter implements Filter {
    private long maxAge;

    public CorsOptionsFilter(long maxAgeInSeconds) {
        this.maxAge = maxAgeInSeconds;
    }

    @Override
    public void handle(Request request, Response response) throws Exception {
        if (request.requestMethod().equalsIgnoreCase("OPTIONS")) {
            response.header("Access-Control-Max-Age", String.valueOf(maxAge));
        }
    }
}
