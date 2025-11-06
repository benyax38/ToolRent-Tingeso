package com.example.demo.Service;

import com.example.demo.DTOs.ClientDTO;
import com.example.demo.DTOs.ClientRequestDTO;
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

    public List<ClientDTO> getAllClientsDTO() {
        return clientRepository.findAll().stream()
                .map(client -> new ClientDTO(
                        client.getClientId(),
                        client.getClientFirstName(),
                        client.getClientLastName(),
                        client.getClientRUT(),
                        client.getClientPhone(),
                        client.getClientEmail(),
                        client.getClientState().name()
                ))
                .toList();
    }

    public ClientEntity createClients(ClientRequestDTO request) {
        ClientEntity client = new ClientEntity();
        client.setClientFirstName(request.getClientFirstName());
        client.setClientLastName(request.getClientLastName());
        client.setClientRUT(request.getClientRut());
        client.setClientPhone(request.getClientPhone());
        client.setClientEmail(request.getClientEmail());
        // El cliente siempre inicia con estado ACTIVO
        client.setClientState(ClientStatus.ACTIVO);
        return clientRepository.save(client);
    }

    public ClientEntity updateClientStatus(Long clientId, String newState) {
        ClientEntity clientEntity = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        try {
            ClientStatus status = ClientStatus.valueOf(newState.trim().toUpperCase());
            clientEntity.setClientState(status);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Estado inválido. Usa ACTIVO o RESTRINGIDO");
        }

        return clientRepository.save(clientEntity);
    }

    public void deleteClientsById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe cliente con id: " + id);
        }
        clientRepository.deleteById(id);
    }

}
