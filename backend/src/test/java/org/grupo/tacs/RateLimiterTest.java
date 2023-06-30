package org.grupo.tacs;

import org.grupo.tacs.extras.RateLimiter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RateLimiterTest {
    private RateLimiter rateLimiter;

    @Before
    public void setUp() {
        // Inicializo el rate limiter con una capacidad de 10 requests cada 10 segundos
        rateLimiter = new RateLimiter(10, 10, 1000);
    }

    @Test
    public void testRateLimiter() {
        // 10 requests dentro del limite
        for (int i = 0; i < 10; i++) {
            assertTrue(rateLimiter.allowRequest());
        }

        // 6 requests, excediendo el limite
        for (int j = 0; j < 6; j++) {
            assertFalse(rateLimiter.allowRequest());
        }

        // Espero la recarga de 5 tokens
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 5 request despues de la recarga de tokens
        for (int k = 0; k < 5; k++) {
            assertTrue(rateLimiter.allowRequest());
        }
        // 1 request excediendo lo que recuperamos con refill.
        assertFalse(rateLimiter.allowRequest());
    }
}