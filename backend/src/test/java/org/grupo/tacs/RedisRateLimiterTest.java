package org.grupo.tacs;

import org.grupo.tacs.extras.RedisRateLimiter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
public class RedisRateLimiterTest {
    private RedisRateLimiter rateLimiter;
    @Mock
    private Jedis jedisMock;

    @Before
    public void initialize() {
        MockitoAnnotations.openMocks(this);
        rateLimiter = new RedisRateLimiter("redis://localhost:6379", 5, 10, 1000);
        rateLimiter.setJedis(jedisMock);
    }


    @Test
    public void testRateLimiterWithinLimit() {
        // Probar req dentro del limite
        String ipAddress = "127.0.0.1";

        // Mock Redis response
        when(jedisMock.incr("rate_limit:" + ipAddress)).thenReturn(1L);
        when(jedisMock.expire("rate_limit:" + ipAddress, 10L)).thenReturn(1L);

        // 3 req
        boolean isAllowed1 = rateLimiter.isAllowed(ipAddress);
        boolean isAllowed2 = rateLimiter.isAllowed(ipAddress);
        boolean isAllowed3 = rateLimiter.isAllowed(ipAddress);

        // Assert 3 allowed true
        assertTrue(isAllowed1);
        assertTrue(isAllowed2);
        assertTrue(isAllowed3);

        // Verifico invocacion de metodos
        verify(jedisMock, times(3)).incr("rate_limit:" + ipAddress);
        verify(jedisMock, atMost(1)).expire("rate_limit:" + ipAddress, 10L);
    }


    @Test
    public void testRateLimiterExceedLimit() {
        // Pruebo exceder el rate limit
        String ipAddress = "127.0.0.1";

        // Mock Redis response
        when(jedisMock.incr("rate_limit:" + ipAddress)).thenReturn(1L, 2L, 3L, 4L, 5L, 6L);

        // 6 req
        boolean isAllowed1 = rateLimiter.isAllowed(ipAddress);
        boolean isAllowed2 = rateLimiter.isAllowed(ipAddress);
        boolean isAllowed3 = rateLimiter.isAllowed(ipAddress);
        boolean isAllowed4 = rateLimiter.isAllowed(ipAddress);
        boolean isAllowed5 = rateLimiter.isAllowed(ipAddress);
        boolean isAllowed6 = rateLimiter.isAllowed(ipAddress);

        // Assert 5 req allowed y el 6to req not allowed
        assertTrue(isAllowed1);
        assertTrue(isAllowed2);
        assertTrue(isAllowed3);
        assertTrue(isAllowed4);
        assertTrue(isAllowed5);
        assertFalse(isAllowed6);

        // Verifico invocacion de metodos
        verify(jedisMock, times(6)).incr("rate_limit:" + ipAddress);
    }
}
