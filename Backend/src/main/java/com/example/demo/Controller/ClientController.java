package com.example.demo.Controller;

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
    public ResponseEntity<List<ClientEntity>> getAllClient() {
        List<ClientEntity> clientList = clientService.getAllClients();
        return ResponseEntity.ok(clientList);
    }

    @PostMapping
    public ResponseEntity<ClientEntity> createClient(@Valid @RequestBody ClientEntity client) {
        ClientEntity createdClient = clientService.createClients(client);
        return ResponseEntity.ok(createdClient);
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
