package au.com.telstra.simcardactivator;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
public class SimActivationController {

    @Autowired
    private SimCardActivationRepository repository;

    @PostMapping("/activate-sim")
    public ResponseEntity<String> activateSim(@RequestBody SimActivationRequest request) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8444/actuate";
        Map<String, String> payload = Map.of("iccid", request.getIccid());

        ResponseEntity<ActuatorResponse> response = restTemplate.postForEntity(
                url, payload, ActuatorResponse.class);

        boolean success = response.getBody().isSuccess();
        System.out.println("SIM Activation success: " + success);

        // Save to database
        SimCardActivationRecord record = new SimCardActivationRecord();
        record.setIccid(request.getIccid());
        record.setCustomerEmail(request.getCustomerEmail());
        record.setActive(success);
        repository.save(record);

        return ResponseEntity.ok(success ? "Activation successful!" : "Activation failed.");
    }

    @GetMapping("/activation-record")
    public ResponseEntity<?> getActivationRecord(@RequestParam("simCardId") Long id) {
        return repository.findById(id)
                .map(record -> ResponseEntity.ok(Map.of(
                        "iccid", record.getIccid(),
                        "customerEmail", record.getCustomerEmail(),
                        "active", record.isActive()
                )))
                .orElse(ResponseEntity.notFound().build());
    }
}
