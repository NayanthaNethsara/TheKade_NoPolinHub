package com.thekade.nopolin.auth_service;

import com.thekade.nopolin.auth_service.dto.AuthenticationRequest;
import com.thekade.nopolin.auth_service.dto.RegisterRequest;
import com.thekade.nopolin.auth_service.entity.Role;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

import org.json.JSONException;
import org.json.JSONObject;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class AuthControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    void registerTestUser() {
    RegisterRequest request = new RegisterRequest();
    request.setUsername("testuser");
    request.setPassword("TestPassword123");
    request.setEmail("testemail@etest.com");
    request.setPhoneNumber("1234567890");
    request.setRole(Role.CITIZEN);

    restTemplate.postForEntity("/api/auth/register", request, String.class);
}

@Test
void testRegister() throws JSONException {
    RegisterRequest request = new RegisterRequest();
    request.setUsername("testuser");
    request.setPassword("TestPassword123");
    request.setEmail("testemail@etest.com");
    request.setPhoneNumber("1234567890");
    request.setRole(Role.CITIZEN);

    ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/register", request, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

    // Parse the JSON response
    JSONObject jsonResponse = new JSONObject(response.getBody());

    // Assert that access_token and refresh_token are present and not empty
    assertThat(jsonResponse.has("access_token")).isTrue();
    assertThat(jsonResponse.getString("access_token")).isNotEmpty();

    assertThat(jsonResponse.has("refresh_token")).isTrue();
    assertThat(jsonResponse.getString("refresh_token")).isNotEmpty();
}

    @Test
    void testLogin() {
    registerTestUser();  // make sure user exists before login

    AuthenticationRequest loginRequest = new AuthenticationRequest();
    loginRequest.setUsername("testuser");
    loginRequest.setPassword("TestPassword123");

    ResponseEntity<String> response = restTemplate.postForEntity("/api/auth/authenticate", loginRequest, String.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).contains("token");
}
}
