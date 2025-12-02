package com.example.demo;

import com.example.demo.DTOs.KardexCreateDTO;
import com.example.demo.DTOs.KardexDTO;
import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.KardexService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KardexServiceTest {

    @Mock
    private KardexRepository kardexRepository;
    @Mock
    private ClientRepository clientRepository;
    @Mock
    private LoanRepository loanRepository;
    @Mock
    private ToolRepository toolRepository;
    @Mock
    private ToolCatalogRepository toolCatalogRepository;
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private KardexService kardexService;

    // ---------------------------------------------------------
    // 1) Test getAllKardexesDTO()
    // ---------------------------------------------------------
    @Test
    void testGetAllKardexesDTO() {

        KardexEntity k = new KardexEntity();
        k.setMovementId(1L);
        k.setType("INGRESO");
        k.setMovementDate(LocalDateTime.now());
        k.setAffectedAmount(5);
        k.setDetails("Ingreso prueba");

        ClientEntity client = new ClientEntity();
        client.setClientId(10L);
        k.setClients(client);

        when(kardexRepository.findAll()).thenReturn(List.of(k));

        List<KardexDTO> result = kardexService.getAllKardexesDTO();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getClientId()).isEqualTo(10L);
        assertThat(result.get(0).getDetails()).isEqualTo("Ingreso prueba");
    }

    // ---------------------------------------------------------
    // 2) Test createKardexes()
    // ---------------------------------------------------------
    @Test
    void testCreateKardexes() {

        KardexEntity movement = new KardexEntity();
        when(kardexRepository.save(movement)).thenReturn(movement);

        KardexEntity result = kardexService.createKardexes(movement);

        assertThat(result).isNotNull();
        verify(kardexRepository, times(1)).save(movement);
    }

    // ---------------------------------------------------------
    // 3) Test registerMovement()
    // ---------------------------------------------------------
    @Test
    void testRegisterMovement() {

        KardexCreateDTO dto = KardexCreateDTO.builder()
                .type(KardexService.KardexMovementType.INGRESO)
                .affectedAmount(3)
                .details("Movimiento test")
                .clientId(1L)
                .loanId(2L)
                .toolId(3L)
                .catalogId(4L)
                .userId(5L)
                .build();

        // Mockear las referencias
        ClientEntity client = new ClientEntity();
        client.setClientId(1L);
        when(clientRepository.getReferenceById(1L)).thenReturn(client);

        LoanEntity loan = new LoanEntity();
        loan.setLoanId(2L);
        when(loanRepository.getReferenceById(2L)).thenReturn(loan);

        ToolEntity tool = new ToolEntity();
        tool.setToolId(3L);
        when(toolRepository.getReferenceById(3L)).thenReturn(tool);

        ToolCatalogEntity catalog = new ToolCatalogEntity();
        catalog.setToolCatalogId(4L);
        when(toolCatalogRepository.getReferenceById(4L)).thenReturn(catalog);

        UserEntity user = new UserEntity();
        user.setUserId(5L);
        when(userRepository.getReferenceById(5L)).thenReturn(user);

        // Ejecutar
        kardexService.registerMovement(dto);

        // Capturar lo que realmente guardó
        ArgumentCaptor<KardexEntity> captor = ArgumentCaptor.forClass(KardexEntity.class);
        verify(kardexRepository).save(captor.capture());

        KardexEntity saved = captor.getValue();

        assertThat(saved.getDetails()).isEqualTo("Movimiento test");
        assertThat(saved.getAffectedAmount()).isEqualTo(3);
        assertThat(saved.getClients().getClientId()).isEqualTo(1L);
        assertThat(saved.getLoans().getLoanId()).isEqualTo(2L);
        assertThat(saved.getTools().getToolId()).isEqualTo(3L);
        assertThat(saved.getTool_catalogs().getToolCatalogId()).isEqualTo(4L);
        assertThat(saved.getUsers().getUserId()).isEqualTo(5L);
    }

    // ---------------------------------------------------------
    // 4) Test deleteKardexesById()
    // ---------------------------------------------------------
    @Test
    void testDeleteKardexesById_success() {

        when(kardexRepository.existsById(1L)).thenReturn(true);

        kardexService.deleteKardexesById(1L);

        verify(kardexRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteKardexesById_notFound() {

        when(kardexRepository.existsById(1L)).thenReturn(false);

        assertThrows(
                IllegalArgumentException.class,
                () -> kardexService.deleteKardexesById(1L)
        );
    }
}

