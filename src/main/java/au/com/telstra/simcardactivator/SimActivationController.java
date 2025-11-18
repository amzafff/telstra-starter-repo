package au.com.telstra.simcardactivator;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;

@RestController
public class SimActivationController {

    @PostMapping("/activate-sim")
    public ResponseEntity<String> activateSim(@RequestBody SimActivationRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8444/actuate";
        Map<String, String> payload = Map.of("iccid", request.getIccid());

        ResponseEntity<ActuatorResponse> response = restTemplate.postForEntity(
                url, payload, ActuatorResponse.class);

        boolean success = response.getBody().isSuccess();
        System.out.println("SIM Activation success: " + success);

        return ResponseEntity.ok(success ? "Activation successful!" : "Activation failed.");
    }
}
