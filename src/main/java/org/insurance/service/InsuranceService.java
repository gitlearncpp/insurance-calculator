package org.insurance.service;

import org.insurance.model.Client;
import org.insurance.model.InsuranceOffer;
import org.insurance.model.Property;
import org.insurance.repository.ClientRepository;
import org.insurance.repository.InsuranceOfferRepository;
import org.insurance.repository.PropertyRepository;
import org.springdoc.core.converters.AdditionalModelsConverter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class InsuranceService {
    private final ClientRepository clientRepository;
    private final PropertyRepository propertyRepository;
    private final InsuranceOfferRepository offerRepository;
    private final AdditionalModelsConverter additionalModelsConverter;

    public InsuranceService(ClientRepository clientRepository, PropertyRepository propertyRepository, InsuranceOfferRepository offerRepository, AdditionalModelsConverter additionalModelsConverter) {
        this.clientRepository = clientRepository;
        this.propertyRepository = propertyRepository;
        this.offerRepository = offerRepository;
        this.additionalModelsConverter = additionalModelsConverter;
    }

    public InsuranceOffer calculateInsurance(Long pesel, String firstName, String lastName, Property property) {
        Optional<Client> existingClient = clientRepository.findByPesel(pesel);

        Client client;
        if (existingClient.isPresent()) {
            client = existingClient.get();
            if (!client.getFirstName().equalsIgnoreCase(firstName) || !client.getLastName().equalsIgnoreCase(lastName)) {
                throw new RuntimeException("Imię lub nazwisko w bazie nie zgadzają się z podanymi danymi. Proszę o ich aktualizację.");
            }
        } else {
            client = clientRepository.save(new Client(null, firstName, lastName, pesel, property.getLocation(), LocalDateTime.now()));
        }

        property.setClient(client);
        property = propertyRepository.save(property);

        double mul_Value;
        if (property.getValue() > 1000000) {
            mul_Value = 0.1;
        } else if (property.getValue() > 800000) {
            mul_Value = 0.05;
        } else if (property.getValue() > 500000) {
            mul_Value = 0.1;
        } else if (property.getValue() > 300000) {
            mul_Value = 0.05;
        } else if (property.getValue() > 200000) {
            mul_Value = 0.3;
        } else {
            mul_Value = 0.5;
        }


        double insurancePrice = (property.getValue() * mul_Value) + (property.getRooms() * 2 + 500);

        InsuranceOffer offer = InsuranceOffer.builder()
                .client(client)
                .property(property)
                .insurancePrice(insurancePrice)
                .offerNumber(UUID.randomUUID().toString().substring(0, 8))
                .createdAt(LocalDateTime.now())
                .build();

        return offerRepository.save(offer);
    }
}
