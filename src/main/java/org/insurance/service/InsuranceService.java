package org.insurance.service;

import org.insurance.model.Client;
import org.insurance.model.InsuranceOffer;
import org.insurance.model.Property;
import org.insurance.repository.ClientRepository;
import org.insurance.repository.InsuranceOfferRepository;
import org.insurance.repository.PropertyRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class InsuranceService {
    private final ClientRepository clientRepository;
    private final PropertyRepository propertyRepository;
    private final InsuranceOfferRepository offerRepository;

    public InsuranceService(ClientRepository clientRepository, PropertyRepository propertyRepository, InsuranceOfferRepository offerRepository) {
        this.clientRepository = clientRepository;
        this.propertyRepository = propertyRepository;
        this.offerRepository = offerRepository;
    }

    public InsuranceOffer calculateInsurance(String pesel, String firstName, String lastName, Property property) {
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

        double insurancePrice = (property.getValue() * 0.01) + (property.getRooms() * 50);

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
