package com.example.demo.Controller;

import com.example.demo.DTOs.ClientDTO;
import com.example.demo.DTOs.ClientRequestDTO;
import com.example.demo.DTOs.ClientStatusResponseDTO;
import com.example.demo.Entity.ClientEntity;
import com.example.demo.Service.ClientService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;
    @Autowired
    public ClientController(ClientService clientService) { this.clientService = clientService; }

    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClient() {
        List<ClientDTO> clientList = clientService.getAllClientsDTO();
        return ResponseEntity.ok(clientList);
    }

    @PostMapping
    public ResponseEntity<ClientEntity> createClient(@Valid @RequestBody ClientRequestDTO request) {
        ClientEntity createdClient = clientService.createClients(request);
        return ResponseEntity.ok(createdClient);
    }

    @PatchMapping("/{clientId}/estado")
    public ResponseEntity<ClientStatusResponseDTO> updateClientState(
            @PathVariable Long clientId,
            @Valid @RequestParam String newState) {

        ClientEntity updatedClient = clientService.updateClientStatus(clientId, newState);

        ClientStatusResponseDTO clientStatusResponseDTO = new ClientStatusResponseDTO(
                updatedClient.getClientId(),
                updatedClient.getClientFirstName(),
                updatedClient.getClientState().name()
        );

        return ResponseEntity.ok(clientStatusResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClientById(@PathVariable Long id) {
        try {
            clientService.deleteClientsById(id);
            return ResponseEntity.ok("Cliente eliminado correctamente");
        } catch (IllegalArgumentException errorDeleteClientById) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDeleteClientById.getMessage());
        }
    }

}
