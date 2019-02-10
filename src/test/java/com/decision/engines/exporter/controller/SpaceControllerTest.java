package com.decision.engines.exporter.controller;

import com.decision.engines.exporter.Application;
import com.decision.engines.exporter.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpaceControllerTest {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();

    @LocalServerPort
    private int port;

    @Test
    public void testRetrieveUsers() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/user"),
                HttpMethod.GET, entity, String.class);
        assert (HttpStatus.OK == response.getStatusCode());
    }

    @Test
    public void testCreateUser() {
        HttpEntity<UserDTO> entity = new HttpEntity<>(getOneUser(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/user"),
                HttpMethod.POST, entity, String.class);
        System.out.println("Response code: " + response.getStatusCode().toString());

        assert (HttpStatus.CREATED == response.getStatusCode());
    }

    @Test
    public void testBulkDataPost() {
        HttpEntity<List<UserDTO>> entity = new HttpEntity<>(getListOfUsers(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/user/bulk"),
                HttpMethod.POST, entity, String.class);
        System.out.println("Response code: " + response.getStatusCode().toString());

        assert (HttpStatus.ACCEPTED == response.getStatusCode());
    }

    private UserDTO getOneUser() {
        return getListOfUsers().get(0);
    }

    private List<UserDTO> getListOfUsers() {
        UserDTO tom = new UserDTO();
        tom.setFirstName("Tom");
        tom.setLastName("Hanks");
        tom.setEmail("tom.hanks@gmail.com");
        tom.setAge(50);
        tom.setStreetAddress("425 Highman Ave");
        tom.setCity("Los Angeles");
        tom.setStateCode("CA");
        tom.setZipcode(99890);

        UserDTO russel = new UserDTO();
        russel.setFirstName("Russel");
        russel.setLastName("Peters");
        russel.setEmail("russel@gmail.com");
        russel.setAge(56);
        russel.setStreetAddress("425 Beverly Hills");
        russel.setCity("Los Angeles");
        russel.setStateCode("CA");
        russel.setZipcode(99190);

        UserDTO angelina = new UserDTO();
        angelina.setFirstName("Angelina");
        angelina.setLastName("Jolie");
        angelina.setEmail("angie@gmail.com");
        angelina.setAge(51);
        angelina.setStreetAddress("215 Beverly Hills");
        angelina.setCity("Los Angeles");
        angelina.setStateCode("CA");
        angelina.setZipcode(99190);

        return Arrays.asList(tom, russel, angelina);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}