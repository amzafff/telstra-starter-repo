package stepDefinitions;

import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

public class SimCardActivatorStepDefinitions {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private ResponseEntity<String> activationResponse;
    private ResponseEntity<String> queryResponse;

    @Given("the microservice is running")
    public void microservice_is_running() {
        // Usually ensure your application is started separately or is already running.
        // This step can remain empty or include simple health check logic.
    }

    @When("I submit an activation request for ICCID {string} and customer email {string}")
    public void submit_activation_request(String iccid, String email) {
        String url = "http://localhost:" + port + "/activate-sim";

        String json = String.format("{\"iccid\":\"%s\", \"customerEmail\":\"%s\"}", iccid, email);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        activationResponse = restTemplate.postForEntity(url, entity, String.class);
    }

    @Then("the activation is successful")
    public void activation_successful() {
        assertEquals(HttpStatus.OK, activationResponse.getStatusCode());
        assertTrue(activationResponse.getBody().contains("successful"));
    }

    @Then("the activation fails")
    public void activation_failed() {
        assertEquals(HttpStatus.OK, activationResponse.getStatusCode());
        assertTrue(activationResponse.getBody().contains("failed"));
    }

    @Then("querying activation record with id {int} returns success status")
    public void query_activation_record_success(int id) {
        String url = "http://localhost:" + port + "/activation-record?simCardId=" + id;
        queryResponse = restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, queryResponse.getStatusCode());
        assertTrue(queryResponse.getBody().contains("\"active\":true"));
    }

    @Then("querying activation record with id {int} returns failure status")
    public void query_activation_record_failure(int id) {
        String url = "http://localhost:" + port + "/activation-record?simCardId=" + id;
        queryResponse = restTemplate.getForEntity(url, String.class);
        assertEquals(HttpStatus.OK, queryResponse.getStatusCode());
        assertTrue(queryResponse.getBody().contains("\"active\":false"));
    }

}
