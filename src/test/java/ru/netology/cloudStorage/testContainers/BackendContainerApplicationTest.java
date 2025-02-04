package ru.netology.cloudStorage.testContainers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.netology.cloudStorage.DTO.UserDTO;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;


@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {BackendContainerApplicationTest.Initializer.class})
class BackendContainerApplicationTest {
    @LocalServerPort
    private Integer PORT;
    private static final String LOGIN = "testLogin@test.ru";
    private static final String PASSWORD = "testPassword";
    private static final PostgreSQLContainer sqlContainer;

    static {
        sqlContainer = new PostgreSQLContainer("postgres:latest")
                .withDatabaseName("integration-tests-db")
                .withUsername("postgres")
                .withPassword("postgres");
        sqlContainer.start();
    }

    static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "spring.datasource.url=" + sqlContainer.getJdbcUrl(),
                    "spring.datasource.username=" + sqlContainer.getUsername(),
                    "spring.datasource.password=" + sqlContainer.getPassword()
            ).applyTo(configurableApplicationContext.getEnvironment());
        }
    }


    @Autowired
    public TestRestTemplate restTemplate;
    private UserDTO userDTO;

    @BeforeEach
    public void init()
    {
        userDTO = UserDTO.builder()
                .login(LOGIN)
                .password(PASSWORD)
                .build();
    }

    @Test
    void createUserTest_ThenSuccess() throws URISyntaxException {
        System.out.println("TEST_USER_DTO IS: " + userDTO);
        System.out.println("TEST_PORT IS: " + PORT);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        URI url = new URI("http://" + sqlContainer.getHost() + ":" + PORT + "/user/register");
        HttpEntity<UserDTO> requestEntity = new HttpEntity<>(userDTO, headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<UserDTO> responseEntity = restTemplate.postForEntity(url, requestEntity, UserDTO.class);
        System.out.println("TEST_RESPONSE_ENTITY IS: " + responseEntity.getStatusCodeValue());
        Assertions.assertEquals(LOGIN, Objects.requireNonNull(responseEntity.getBody()).getLogin());
    }

    @Test
    void loginAppTest_ThenSuccess() {
        String getLoginURI = "http://" + sqlContainer.getHost() + ":" + PORT + "/login";
        String authToken = restTemplate.postForObject(getLoginURI, userDTO, String.class);
        Assertions.assertNotNull(authToken);
    }
}
