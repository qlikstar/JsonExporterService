package com.decision.engines.exporter.controller;

import com.decision.engines.exporter.Application;
import com.decision.engines.exporter.model.Address;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressControllerTest {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;

    @Test
    public void testRetrieveAddresses() {

        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/address"),
                HttpMethod.GET, entity, String.class);
        assert (HttpStatus.OK == response.getStatusCode());
    }

    @Test
    public void testCreateUser() {
        Address address = new Address();
        address.setStreetAddress("575 Dream Boulevard");
        address.setCity("San Diego");
        address.setStateCode("CA");
        address.setZipcode(99809);
        HttpEntity<Address> entity = new HttpEntity<>(address, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/api/v1/address"),
                HttpMethod.POST, entity, String.class);
        System.out.println("Response code: " + response.getStatusCode().toString());

        assert (HttpStatus.CREATED == response.getStatusCode());
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}