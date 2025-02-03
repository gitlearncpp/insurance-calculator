package org.insurance.service;

import org.insurance.model.Client;
import org.insurance.repository.ClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ClientService {
    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Optional<Client> getClientByPesel(String pesel) {
        return clientRepository.findByPesel(pesel);
    }

    @Transactional
    public Client updateClientName(String pesel, String newFirstName, String newLastName) {
        return clientRepository.findByPesel(pesel).map(client -> {
            if (newFirstName != null && !newFirstName.isEmpty()) {
                client.setFirstName(newFirstName);
            }
            if (newLastName != null && !newLastName.isEmpty()) {
                client.setLastName(newLastName);
            }
            return clientRepository.save(client);
        }).orElseThrow(() -> new RuntimeException("Klient o podanym PESEL nie istnieje."));
    }

}
