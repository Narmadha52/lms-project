package com.lms.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HealthControllerTest {

    private HealthController healthController;

    @BeforeEach
    void setUp() {
        healthController = new HealthController();
    }

    @Test
    void testApiHealth_ReturnsExpectedResponse() {
        // Act
        ResponseEntity<Map<String, Object>> response = healthController.apiHealth();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        Map<String, Object> body = response.getBody();
        assertEquals("UP", body.get("status"));
        assertEquals("LMS Backend API", body.get("service"));
        assertEquals("1.0.0", body.get("version"));
        assertTrue(body.containsKey("timestamp"));
    }
}
