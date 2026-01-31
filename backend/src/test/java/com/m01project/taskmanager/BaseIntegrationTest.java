package com.m01project.taskmanager;

import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Base class for integration tests that require a real database.
 *
 * <p>This class provides:</p>
 * <ul>
 *   <li>A PostgreSQL database running in a Docker container (Testcontainers)</li>
 *   <li>Automatic database cleanup before each test</li>
 *   <li>Helper methods for creating test data</li>
 *   <li>MockMvc configuration for testing REST endpoints</li>
 * </ul>
 *
 * <p><strong>Usage:</strong> Extend this class in your integration test classes
 * to get automatic database setup and cleanup.</p>
 */
@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
public abstract class BaseIntegrationTest {

    /**
     * PostgreSQL container for integration tests.
     *
     * <p><strong>Important:</strong> The container is explicitly started in a static block
     * to ensure it's running before @DynamicPropertySource accesses its properties.
     * Without this, you would get "Mapped port can only be obtained after the container is started" error.</p>
     *
     * <p>The @Container annotation still manages the container lifecycle (start/stop),
     * but we ensure it starts early enough for property registration.</p>
     */
    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("taskmanager_test")
                    .withUsername("postgres")
                    .withPassword("postgres");

    /**
     * Static initializer to start the container before @DynamicPropertySource is called.
     * This prevents "Mapped port can only be obtained after the container is started" errors.
     */
    static {
        postgres.start();
    }

    /**
     * Dynamically registers database connection properties from the running container.
     *
     * <p>This method is called after the container is started (thanks to the static block above)
     * and configures Spring Boot to connect to the test database.</p>
     *
     * @param registry the property registry to add dynamic properties to
     */
    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
        registry.add("spring.flyway.enabled", () -> "true");
        registry.add("jwt.secret", () -> "test-jwt-secret-test-jwt-secret-test-jwt-secret");
    }

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    /**
     * Cleans the database before each test to ensure test isolation.
     *
     * <p>This prevents test data from one test affecting another test's results.
     * Each test starts with a clean slate.</p>
     */
    @BeforeEach
    void cleanDatabase() {
        userRepository.deleteAll();
    }

    /**
     * Helper method to create a test user with default values.
     *
     * <p>Creates and saves a user with:</p>
     * <ul>
     *   <li>Email: test@example.com</li>
     *   <li>Password: "password" (encoded)</li>
     *   <li>First Name: "Test"</li>
     *   <li>Last Name: "User"</li>
     *   <li>Role: USER (default)</li>
     * </ul>
     *
     * @return the saved User entity
     */
    protected User createTestUser() {
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setFirstName("Test");
        user.setLastName("User");
        return userRepository.save(user);
    }
}
