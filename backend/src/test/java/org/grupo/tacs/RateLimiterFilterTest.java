package org.grupo.tacs;

import org.grupo.tacs.excepciones.EventClosedException;
import org.grupo.tacs.extras.RateLimitFilter;
import org.grupo.tacs.extras.RateLimiter;
import org.junit.Test;
import spark.HaltException;
import spark.Request;
import spark.Response;
import spark.Route;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class RateLimiterFilterTest {

    @Test
    public void testHandleRequestWithinRateLimit() throws Exception {
        // Creo mock RateLimiter que retorna true en allowRequest
        RateLimiter rateLimiter = mock(RateLimiter.class);
        when(rateLimiter.allowRequest()).thenReturn(true);

        // Mock Request y Response
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        // Crea RateLimitFilter
        RateLimitFilter rateLimitFilter = new RateLimitFilter(rateLimiter);

        // Llamo a handle
        rateLimitFilter.handle(request, response);

        // Verifico que allowRequest() fue llamado en RateLimiter
        verify(rateLimiter, times(1)).allowRequest();

        // Me fijo que nunca tire 429 y que el body no contenga "Rate limit exceeded"
        verify(response, never()).status(429);
        verify(response, never()).body("Rate limit exceeded");
    }

    @Test(expected = HaltException.class)
    public void testHandleRequestExceedsRateLimit() throws Exception {
        // Creo mock RateLimiter que retorna false en allowRequest
        RateLimiter rateLimiter = mock(RateLimiter.class);
        when(rateLimiter.allowRequest()).thenReturn(false);

        // Mock Request y Response
        Request request = mock(Request.class);
        Response response = mock(Response.class);

        // Crea RateLimitFilter
        RateLimitFilter rateLimitFilter = new RateLimitFilter(rateLimiter);

        // Llamo a handle
        rateLimitFilter.handle(request, response);

        // Verifico que allowRequest() fue llamado en RateLimiter
        verify(rateLimiter, times(1)).allowRequest();

        // Me fijo que tire 429 y que el body contenga "Rate limit exceeded"
        verify(response, times(1)).status(429);
        verify(response, times(1)).body("Rate limit exceeded");
    }
}
