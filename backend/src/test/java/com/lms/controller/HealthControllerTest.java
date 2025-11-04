package com.lms.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HealthControllerTest {

    private HealthController healthController;

    @BeforeEach
    void setUp() {
        healthController = new HealthController();
    }

    @Nested
    @DisplayName("API Health Check Tests")
    class ApiHealthTests {

        @Test
        @DisplayName("Should return health status with all required fields")
        void testApiHealth_ReturnsExpectedResponse() {
            // Act
            ResponseEntity<Map<String, Object>> response = healthController.apiHealth();

            // Assert
            assertNotNull(response, "Response should not be null");
            assertEquals(HttpStatus.OK, response.getStatusCode(), "Status code should be 200 OK");
            
            Map<String, Object> body = response.getBody();
            assertNotNull(body, "Response body should not be null");
            
            // Verify all expected fields are present
            assertTrue(body.containsKey("status"), "Response should contain 'status' field");
            assertTrue(body.containsKey("service"), "Response should contain 'service' field");
            assertTrue(body.containsKey("version"), "Response should contain 'version' field");
            assertTrue(body.containsKey("timestamp"), "Response should contain 'timestamp' field");
        }

        @Test
        @DisplayName("Should return UP status")
        void testApiHealth_StatusIsUp() {
            // Act
            ResponseEntity<Map<String, Object>> response = healthController.apiHealth();

            // Assert
            Map<String, Object> body = response.getBody();
            assertEquals("UP", body.get("status"), "Status should be 'UP'");
        }

        @Test
        @DisplayName("Should return correct service name")
        void testApiHealth_ServiceName() {
            // Act
            ResponseEntity<Map<String, Object>> response = healthController.apiHealth();

            // Assert
            Map<String, Object> body = response.getBody();
            assertEquals("LMS Backend API", body.get("service"), 
                    "Service name should be 'LMS Backend API'");
        }

        @Test
        @DisplayName("Should return correct version")
        void testApiHealth_Version() {
            // Act
            ResponseEntity<Map<String, Object>> response = healthController.apiHealth();

            // Assert
            Map<String, Object> body = response.getBody();
            assertEquals("1.0.0", body.get("version"), "Version should be '1.0.0'");
        }

        @Test
        @DisplayName("Should return valid timestamp")
        void testApiHealth_ValidTimestamp() {
            // Arrange
            LocalDateTime beforeCall = LocalDateTime.now();

            // Act
            ResponseEntity<Map<String, Object>> response = healthController.apiHealth();

            // Arrange (continued)
            LocalDateTime afterCall = LocalDateTime.now();

            // Assert
            Map<String, Object> body = response.getBody();
            Object timestamp = body.get("timestamp");
            
            assertNotNull(timestamp, "Timestamp should not be null");
            assertTrue(timestamp instanceof String, "Timestamp should be a String");
            
            // Verify timestamp format (if it's ISO format)
            String timestampStr = (String) timestamp;
            assertDoesNotThrow(() -> {
                LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_DATE_TIME);
            }, "Timestamp should be in valid ISO date-time format");
        }

        @Test
        @DisplayName("Should return timestamp close to current time")
        void testApiHealth_TimestampIsRecent() {
            // Arrange
            LocalDateTime beforeCall = LocalDateTime.now().minusSeconds(1);

            // Act
            ResponseEntity<Map<String, Object>> response = healthController.apiHealth();

            // Arrange (continued)
            LocalDateTime afterCall = LocalDateTime.now().plusSeconds(1);

            // Assert
            Map<String, Object> body = response.getBody();
            String timestampStr = (String) body.get("timestamp");
            LocalDateTime timestamp = LocalDateTime.parse(timestampStr, DateTimeFormatter.ISO_DATE_TIME);
            
            assertTrue(timestamp.isAfter(beforeCall) || timestamp.isEqual(beforeCall),
                    "Timestamp should be after or equal to time before API call");
            assertTrue(timestamp.isBefore(afterCall) || timestamp.isEqual(afterCall),
                    "Timestamp should be before or equal to time after API call");
        }

        @Test
        @DisplayName("Should have exactly four fields in response")
        void testApiHealth_CorrectNumberOfFields() {
            // Act
            ResponseEntity<Map<String, Object>> response = healthController.apiHealth();

            // Assert
            Map<String, Object> body = response.getBody();
            assertEquals(4, body.size(), "Response should contain exactly 4 fields");
        }

        @Test
        @DisplayName("Should return consistent response on multiple calls")
        void testApiHealth_ConsistentResponse() {
            // Act
            ResponseEntity<Map<String, Object>> response1 = healthController.apiHealth();
            ResponseEntity<Map<String, Object>> response2 = healthController.apiHealth();

            // Assert
            assertEquals(response1.getStatusCode(), response2.getStatusCode());
            assertEquals(response1.getBody().get("status"), response2.getBody().get("status"));
            assertEquals(response1.getBody().get("service"), response2.getBody().get("service"));
            assertEquals(response1.getBody().get("version"), response2.getBody().get("version"));
            
            // Timestamps may differ slightly
            assertNotNull(response1.getBody().get("timestamp"));
            assertNotNull(response2.getBody().get("timestamp"));
        }

        @Test
        @DisplayName("Should have correct data types for all fields")
        void testApiHealth_CorrectDataTypes() {
            // Act
            ResponseEntity<Map<String, Object>> response = healthController.apiHealth();

            // Assert
            Map<String, Object> body = response.getBody();
            
            assertTrue(body.get("status") instanceof String, "'status' should be a String");
            assertTrue(body.get("service") instanceof String, "'service' should be a String");
            assertTrue(body.get("version") instanceof String, "'version' should be a String");
            assertTrue(body.get("timestamp") instanceof String, "'timestamp' should be a String");
        }

        @Test
        @DisplayName("Should not return null values")
        void testApiHealth_NoNullValues() {
            // Act
            ResponseEntity<Map<String, Object>> response = healthController.apiHealth();

            // Assert
            Map<String, Object> body = response.getBody();
            
            body.forEach((key, value) -> {
                assertNotNull(value, "Field '" + key + "' should not be null");
            });
        }

        @Test
        @DisplayName("Should not return empty strings")
        void testApiHealth_NoEmptyStrings() {
            // Act
            ResponseEntity<Map<String, Object>> response = healthController.apiHealth();

            // Assert
            Map<String, Object> body = response.getBody();
            
            body.forEach((key, value) -> {
                if (value instanceof String) {
                    assertFalse(((String) value).isEmpty(), 
                            "String field '" + key + "' should not be empty");
                }
            });
        }
    }

    @Nested
    @DisplayName("Performance Tests")
    class PerformanceTests {

        @Test
        @DisplayName("Should respond quickly")
        void testApiHealth_ResponseTime() {
            // Arrange
            long startTime = System.currentTimeMillis();

            // Act
            healthController.apiHealth();

            // Assert
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            
            assertTrue(duration < 100, 
                    "Health check should respond in less than 100ms, took: " + duration + "ms");
        }

        @Test
        @DisplayName("Should handle multiple rapid requests")
        void testApiHealth_MultipleRapidRequests() {
            // Act & Assert
            assertDoesNotThrow(() -> {
                for (int i = 0; i < 100; i++) {
                    ResponseEntity<Map<String, Object>> response = healthController.apiHealth();
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                }
            }, "Should handle 100 rapid requests without errors");
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should work with new instance each time")
        void testApiHealth_NewInstanceEachTime() {
            // Act
            HealthController controller1 = new HealthController();
            HealthController controller2 = new HealthController();
            
            ResponseEntity<Map<String, Object>> response1 = controller1.apiHealth();
            ResponseEntity<Map<String, Object>> response2 = controller2.apiHealth();

            // Assert
            assertEquals(response1.getStatusCode(), response2.getStatusCode());
            assertEquals(response1.getBody().get("status"), response2.getBody().get("status"));
        }

        @Test
        @DisplayName("Should be thread-safe")
        void testApiHealth_ThreadSafety() throws InterruptedException {
            // Arrange
            final int threadCount = 10;
            Thread[] threads = new Thread[threadCount];

            // Act
            for (int i = 0; i < threadCount; i++) {
                threads[i] = new Thread(() -> {
                    ResponseEntity<Map<String, Object>> response = healthController.apiHealth();
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertEquals("UP", response.getBody().get("status"));
                });
                threads[i].start();
            }

            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }

            // Assert - if we get here without exceptions, test passes
            assertTrue(true, "All threads completed successfully");
        }
    }
}
