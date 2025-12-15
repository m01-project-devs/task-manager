package com.m01project.taskmanager.security;

import com.jayway.jsonpath.JsonPath;
import com.m01project.taskmanager.domain.User;
import com.m01project.taskmanager.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtSecurityIntegrationTest {

    private static final String TEST_JWT_SECRET =
            "test-jwt-secret-test-jwt-secret-test-jwt-secret";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("password"));
        user.setFirstName("Test");
        user.setLastName("User");

        userRepository.save(user);
    }

    @Test
    void shouldAllowAccessToLoginWithoutJwt() throws Exception {
        mockMvc.perform(post("/api/auth/login"))
                .andExpect(status().isUnauthorized());
        // Unauthorized due to missing credentials, NOT JWT
    }

    @Test
    void shouldAllowAccessWithValidJwt() throws Exception {

        // 1️⃣ Login
        String loginPayload = """
                {
                  "email": "test@example.com",
                  "password": "password"
                }
                """;

        MvcResult result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        // 2️⃣ Extract token
        String response = result.getResponse().getContentAsString();
        String token = JsonPath.read(response, "$.token");

        // 3️⃣ Access protected endpoint
        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());
    }

    @Test
    void shouldRejectRequestWithoutJwt() throws Exception {
        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectInvalidJwt() throws Exception {
        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", "Bearer invalid.token.value"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void shouldRejectExpiredJwt() throws Exception {

        String expiredToken = Jwts.builder()
                .setSubject("test@example.com")
                .setIssuedAt(Date.from(Instant.now().minusSeconds(3600)))
                .setExpiration(Date.from(Instant.now().minusSeconds(1800)))
                .signWith(
                        Keys.hmacShaKeyFor(
                                TEST_JWT_SECRET.getBytes(StandardCharsets.UTF_8)
                        ),
                        SignatureAlgorithm.HS256
                )
                .compact();

        mockMvc.perform(get("/api/tasks")
                        .header("Authorization", "Bearer " + expiredToken))
                .andExpect(status().isUnauthorized());
    }
}
