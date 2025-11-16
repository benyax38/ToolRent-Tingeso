package com.example.demo.Service;

import com.example.demo.DTOs.ClientDTO;
import com.example.demo.DTOs.ClientRequestDTO;
import com.example.demo.Entity.ClientEntity;
import com.example.demo.Repository.ClientRepository;
import com.example.demo.Repository.LoanRepository;
import com.example.demo.Repository.PenaltyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientService {

    private final ClientRepository clientRepository;
    private final LoanRepository loanRepository;
    private final PenaltyRepository penaltyRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository, LoanRepository loanRepository, PenaltyRepository penaltyRepository) { this.clientRepository = clientRepository;
        this.loanRepository = loanRepository;
        this.penaltyRepository = penaltyRepository;
    }

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

    public ClientEntity validateAndLoadClient(Long clientId) {

        // Buscar cliente
        ClientEntity client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Validar que esta activo
        if (client.getClientState() != ClientStatus.ACTIVO) {
            throw new RuntimeException("No se puede crear el préstamo: el cliente no está activo.");
        }

        // Validar que no tenga prestamos vencidos o por pagar
        if (loanRepository.existsByClients_ClientIdAndLoanStatusIn(clientId, List.of(LoanService.LoanStatus.VENCIDO, LoanService.LoanStatus.POR_PAGAR))) {
            throw new RuntimeException("No se puede crear el préstamo: el cliente tiene préstamos vencidos o por pagar.");
        }

        // Validar que no tenga multas impagas
        if (penaltyRepository.existsByLoan_Clients_ClientIdAndPenaltyStatus(
                clientId, PenaltyService.PaymentStatus.IMPAGO)) {
            throw new RuntimeException("No se puede crear el préstamo: el cliente tiene multas impagas.");
        }

        // 5. Validar máximo de 5 préstamos simultáneos
        List<LoanService.LoanStatus> activeStatuses = List.of(
                LoanService.LoanStatus.ACTIVO,
                LoanService.LoanStatus.POR_PAGAR,
                LoanService.LoanStatus.VENCIDO
        );

        long activeLoans = loanRepository.countByClients_ClientIdAndLoanStatusIn(clientId, activeStatuses);

        if (activeLoans >= 5) {
            throw new RuntimeException("El cliente ya tiene 5 préstamos simultáneos.");
        }

        return client;
    }

    public void deleteClientsById(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe cliente con id: " + id);
        }
        clientRepository.deleteById(id);
    }

}
