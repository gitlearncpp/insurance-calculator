package org.insurance.controller;

import jakarta.validation.Valid;
import org.insurance.dto.UpdateClientRequest;
import org.insurance.model.Client;
import org.insurance.model.InsuranceOffer;
import org.insurance.model.Property;
import org.insurance.service.InsuranceService;
import org.insurance.service.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/insurance")
public class InsuranceController {
    private final InsuranceService insuranceService;
    private final ClientService clientService;

    public InsuranceController(InsuranceService insuranceService, ClientService clientService) {
        this.insuranceService = insuranceService;
        this.clientService = clientService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<?> calculateInsurance(@RequestBody Map<String, Object> request) {
        try {
            Long pesel = (request.get("pesel") instanceof Long) ? (Long) request.get("pesel") : Long.parseLong(request.get("pesel").toString());
            String firstName = (String) request.get("firstName");
            String lastName = (String) request.get("lastName");

            Property property = new Property();
            property.setLocation((String) request.get("location"));
            property.setValue(Double.parseDouble(request.get("value").toString()));
            property.setRooms(Integer.parseInt(request.get("rooms").toString()));

            InsuranceOffer offer = insuranceService.calculateInsurance((long)pesel, firstName, lastName, property);
            return ResponseEntity.ok(offer);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/update-client")
    public ResponseEntity<?> updateClient(@Valid @RequestBody UpdateClientRequest request) {
        try {
            Client updatedClient = clientService.updateClientName(request.getPesel(), request.getNewFirstName(), request.getNewLastName());
            return ResponseEntity.ok(updatedClient);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

}
