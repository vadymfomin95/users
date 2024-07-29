package com.nure.ua.fomin.users.controller;


import com.nure.ua.fomin.users.api.dto.UserDTO;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerTest {

    private static final PostgreSQLContainer<?> postgresFirst = new PostgreSQLContainer<>("postgres:16.1-alpine3.18");
    private static final PostgreSQLContainer<?> postgresSecond = new PostgreSQLContainer<>("postgres:16.1-alpine3.18");

    @LocalServerPort
    private Integer serverPort;

    @BeforeAll
    static void beforeAll() {
        postgresFirst.withInitScript("init_first.sql");
        postgresFirst.start();

        postgresSecond.withInitScript("init_second.sql");
        postgresSecond.start();
    }

    @AfterAll
    static void afterAll() {
        postgresFirst.stop();
        postgresSecond.stop();
    }

    @DynamicPropertySource
    static void setDataSourceProperties(DynamicPropertyRegistry registry) {
        registry.add("ua.nure.fomin.data-sources[0].user", postgresFirst::getUsername);
        registry.add("ua.nure.fomin.data-sources[0].password", postgresFirst::getPassword);
        registry.add("ua.nure.fomin.data-sources[0].url", postgresFirst::getJdbcUrl);
        registry.add("ua.nure.fomin.data-sources[0].table", () -> "users_first");
        registry.add("ua.nure.fomin.data-sources[0].name", () -> "data-base-1");
        registry.add("ua.nure.fomin.data-sources[0].mapping.id", () -> "user_id");
        registry.add("ua.nure.fomin.data-sources[0].mapping.username", () -> "login");
        registry.add("ua.nure.fomin.data-sources[0].mapping.name", () -> "first_name");
        registry.add("ua.nure.fomin.data-sources[0].mapping.surname", () -> "last_name");

        registry.add("ua.nure.fomin.data-sources[1].user", postgresSecond::getUsername);
        registry.add("ua.nure.fomin.data-sources[1].password", postgresSecond::getPassword);
        registry.add("ua.nure.fomin.data-sources[1].url", postgresSecond::getJdbcUrl);
        registry.add("ua.nure.fomin.data-sources[1].table", () -> "users_second");
        registry.add("ua.nure.fomin.data-sources[1].name", () -> "data-base-2");
        registry.add("ua.nure.fomin.data-sources[1].mapping.id", () -> "ldap_id");
        registry.add("ua.nure.fomin.data-sources[1].mapping.username", () -> "ldap_login");
        registry.add("ua.nure.fomin.data-sources[1].mapping.name", () -> "name");
        registry.add("ua.nure.fomin.data-sources[1].mapping.surname", () -> "surname");
    }


    @Test
    @DisplayName("Should aggregate all users from two postgres databases deployed as separate docker containers")
    void shouldGetAllUsers() {
        RestAssured.baseURI = "http://localhost:" + serverPort;
        List<UserDTO> users = Arrays.stream(get("/users").then().assertThat().statusCode(200)
                        .extract()
                        .as(UserDTO[].class))
                .toList();

        assertEquals(2, users.size());

        UserDTO first = users.stream().filter(userDTO -> "111".equals(userDTO.getId())).findFirst().get();
        assertEquals("login", first.getUsername());
        assertEquals("first name", first.getName());
        assertEquals("last name", first.getSurname());

        UserDTO second = users.stream().filter(userDTO -> "222".equals(userDTO.getId())).findFirst().get();
        assertEquals("ldap login", second.getUsername());
        assertEquals("ldap first name", second.getName());
        assertEquals("ldap last name", second.getSurname());
    }

}
