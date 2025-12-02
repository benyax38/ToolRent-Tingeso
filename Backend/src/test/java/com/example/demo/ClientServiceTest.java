package com.example.demo;

import com.example.demo.DTOs.ClientDTO;
import com.example.demo.DTOs.ClientRequestDTO;
import com.example.demo.Entity.ClientEntity;
import com.example.demo.Repository.ClientRepository;
import com.example.demo.Repository.LoanRepository;
import com.example.demo.Repository.PenaltyRepository;
import com.example.demo.Service.ClientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private PenaltyRepository penaltyRepository;

    @InjectMocks
    private ClientService clientService;

    // --------------------------------------------------
    // getAllClientsDTO
    // --------------------------------------------------
    @Test
    void whenGetAllClientsDTO_thenReturnDTOList() {
        ClientEntity c = new ClientEntity();
        c.setClientId(1L);
        c.setClientFirstName("Ana");
        c.setClientLastName("Rojas");
        c.setClientRUT("11.111.111-1");
        c.setClientPhone("9999");
        c.setClientEmail("ana@mail.com");
        c.setClientState(ClientService.ClientStatus.ACTIVO);

        when(clientRepository.findAll()).thenReturn(List.of(c));

        List<ClientDTO> result = clientService.getAllClientsDTO();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getClientFirstName()).isEqualTo("Ana");

        verify(clientRepository, times(1)).findAll();
    }

    // --------------------------------------------------
    // createClients
    // --------------------------------------------------
    @Test
    void whenCreateClient_thenClientIsSaved() {
        ClientRequestDTO req = new ClientRequestDTO(
                "Carlos", "Perez", "12.345.678-9", "1234", "carlos@mail.com"
        );

        ClientEntity saved = new ClientEntity();
        saved.setClientId(1L);

        when(clientRepository.save(any(ClientEntity.class))).thenReturn(saved);

        ClientEntity result = clientService.createClients(req);

        assertThat(result.getClientId()).isEqualTo(1L);
        verify(clientRepository, times(1)).save(any(ClientEntity.class));
    }

    // --------------------------------------------------
    // updateClientStatus
    // --------------------------------------------------
    @Test
    void whenUpdateClientStatusValid_thenUpdated() {
        ClientEntity c = new ClientEntity();
        c.setClientId(10L);
        c.setClientState(ClientService.ClientStatus.ACTIVO);

        when(clientRepository.findById(10L)).thenReturn(Optional.of(c));
        when(clientRepository.save(c)).thenReturn(c);

        ClientEntity result = clientService.updateClientStatus(10L, "RESTRINGIDO");

        assertThat(result.getClientState()).isEqualTo(ClientService.ClientStatus.RESTRINGIDO);
    }

    @Test
    void whenUpdateClientStatusInvalid_thenThrowException() {
        ClientEntity c = new ClientEntity();
        c.setClientId(10L);
        c.setClientState(ClientService.ClientStatus.ACTIVO);

        when(clientRepository.findById(10L)).thenReturn(Optional.of(c));

        assertThatThrownBy(() -> clientService.updateClientStatus(10L, "XX"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Estado inválido");
    }

    // --------------------------------------------------
    // validateAndLoadClient
    // --------------------------------------------------
    @Test
    void whenValidateAndLoadClientValid_thenReturnClient() {
        ClientEntity c = new ClientEntity();
        c.setClientId(5L);
        c.setClientState(ClientService.ClientStatus.ACTIVO);

        when(clientRepository.findById(5L)).thenReturn(Optional.of(c));
        when(loanRepository.existsByClients_ClientIdAndLoanStatusIn(eq(5L), anyList()))
                .thenReturn(false);
        when(penaltyRepository.existsByLoan_Clients_ClientIdAndPenaltyStatus(eq(5L), any()))
                .thenReturn(false);
        when(loanRepository.countByClients_ClientIdAndLoanStatusIn(eq(5L), anyList()))
                .thenReturn(1L);

        ClientEntity result = clientService.validateAndLoadClient(5L);

        assertThat(result).isEqualTo(c);
    }

    @Test
    void whenClientNotFound_thenThrowsException() {
        when(clientRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clientService.validateAndLoadClient(99L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Cliente no encontrado");
    }

    @Test
    void whenClientIsRestricted_thenThrowsException() {
        ClientEntity c = new ClientEntity();
        c.setClientState(ClientService.ClientStatus.RESTRINGIDO);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(c));

        assertThatThrownBy(() -> clientService.validateAndLoadClient(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("no está activo");
    }

    @Test
    void whenClientHasDebts_thenThrowsException() {
        ClientEntity c = new ClientEntity();
        c.setClientState(ClientService.ClientStatus.ACTIVO);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(c));
        when(loanRepository.existsByClients_ClientIdAndLoanStatusIn(eq(1L), anyList()))
                .thenReturn(true);

        assertThatThrownBy(() -> clientService.validateAndLoadClient(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("vencidos o por pagar");
    }

    @Test
    void whenClientHasPenalties_thenThrowsException() {
        ClientEntity c = new ClientEntity();
        c.setClientState(ClientService.ClientStatus.ACTIVO);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(c));
        when(loanRepository.existsByClients_ClientIdAndLoanStatusIn(eq(1L), anyList()))
                .thenReturn(false);
        when(penaltyRepository.existsByLoan_Clients_ClientIdAndPenaltyStatus(eq(1L), any()))
                .thenReturn(true);

        assertThatThrownBy(() -> clientService.validateAndLoadClient(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("multas impagas");
    }

    @Test
    void whenClientHasFiveLoans_thenThrowsException() {
        ClientEntity c = new ClientEntity();
        c.setClientState(ClientService.ClientStatus.ACTIVO);

        when(clientRepository.findById(1L)).thenReturn(Optional.of(c));
        when(loanRepository.existsByClients_ClientIdAndLoanStatusIn(eq(1L), anyList()))
                .thenReturn(false);
        when(penaltyRepository.existsByLoan_Clients_ClientIdAndPenaltyStatus(eq(1L), any()))
                .thenReturn(false);
        when(loanRepository.countByClients_ClientIdAndLoanStatusIn(eq(1L), anyList()))
                .thenReturn(5L);

        assertThatThrownBy(() -> clientService.validateAndLoadClient(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("ya tiene 5 préstamos");
    }

    // --------------------------------------------------
    // deleteClientsById
    // --------------------------------------------------
    @Test
    void whenDeleteExistingClient_thenOK() {
        when(clientRepository.existsById(10L)).thenReturn(true);

        clientService.deleteClientsById(10L);

        verify(clientRepository, times(1)).deleteById(10L);
    }

    @Test
    void whenDeleteNonExistingClient_thenThrow() {
        when(clientRepository.existsById(10L)).thenReturn(false);

        assertThatThrownBy(() -> clientService.deleteClientsById(10L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No existe cliente");
    }
}


