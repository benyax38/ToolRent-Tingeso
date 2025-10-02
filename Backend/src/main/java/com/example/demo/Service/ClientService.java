package com.example.demo.Service;

import com.example.demo.Entity.ClientEntity;
import com.example.demo.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) { this.clientRepository = clientRepository; }

    //Se definen los estados posibles de un cliente
    public enum ClientStatus {
        ACTIVO,
        RESTRINGIDO
    }

    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    public ClientEntity createClients(ClientEntity client) {
        client.setClientState(ClientStatus.ACTIVO);
        return clientRepository.save(client);
    }

    public void deleteClientsById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe cliente con id: " + id);
        }
        clientRepository.deleteById(id);
    }

}
