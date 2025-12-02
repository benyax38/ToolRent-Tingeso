package com.example.demo;

import com.example.demo.DTOs.LoanRequestDTO;
import com.example.demo.DTOs.LoanResponseDTO;
import com.example.demo.DTOs.PaymentRequestDTO;
import com.example.demo.Entity.ClientEntity;
import com.example.demo.Entity.LoanEntity;
import com.example.demo.Entity.PenaltyConfigEntity;
import com.example.demo.Entity.PenaltyEntity;
import com.example.demo.Entity.ToolCatalogEntity;
import com.example.demo.Entity.ToolEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.ClientRepository;
import com.example.demo.Repository.LoanRepository;
import com.example.demo.Repository.PenaltyConfigRepository;
import com.example.demo.Repository.PenaltyRepository;
import com.example.demo.Repository.ToolCatalogRepository;
import com.example.demo.Repository.ToolRepository;
import com.example.demo.Repository.UserRepository;

import com.example.demo.Service.ClientService;
import com.example.demo.Service.KardexService;
import com.example.demo.Service.LoanService;
import com.example.demo.Service.PenaltyService;
import com.example.demo.Service.ToolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanServiceTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ToolRepository toolRepository;

    @Mock
    private ToolCatalogRepository toolCatalogRepository;

    @Mock
    private ToolService toolService;

    @Mock
    private KardexService kardexService;

    @Mock
    private PenaltyRepository penaltyRepository;

    @Mock
    private PenaltyConfigRepository penaltyConfigRepository;

    @InjectMocks
    private LoanService loanService;

    @BeforeEach
    void setup() {
        // Aquí podrías inicializar datos comunes si lo deseas.
    }

    // ============================================================
    // ===================== TEST: getAllLoans =====================
    // ============================================================

    @Test
    void testGetAllLoans_ReturnsMappedDTOs() {

        // --- Datos simulados ---
        LoanEntity loan1 = new LoanEntity();
        loan1.setLoanId(1L);
        loan1.setDeliveryDate(LocalDateTime.of(2024, 1, 10, 10, 0));
        loan1.setDeadline(LocalDateTime.of(2024, 1, 15, 18, 0));
        loan1.setReturnDate(LocalDateTime.of(2024, 1, 14, 9, 30));
        loan1.setLoanStatus(LoanService.LoanStatus.FINALIZADO);
        loan1.setRentalAmount(20000.0);

        ClientEntity client1 = new ClientEntity();
        client1.setClientId(50L);
        loan1.setClients(client1);

        ToolEntity tool1 = new ToolEntity();
        tool1.setToolId(70L);
        loan1.setTools(tool1);

        UserEntity user1 = new UserEntity();
        user1.setUserId(100L);
        loan1.setUsers(user1);

        LoanEntity loan2 = new LoanEntity();
        loan2.setLoanId(2L);
        loan2.setDeliveryDate(LocalDateTime.of(2024, 2, 1, 8, 0));
        loan2.setDeadline(LocalDateTime.of(2024, 2, 5, 17, 0));
        loan2.setReturnDate(null);
        loan2.setLoanStatus(LoanService.LoanStatus.ACTIVO);
        loan2.setRentalAmount(15000.0);

        ClientEntity client2 = new ClientEntity();
        client2.setClientId(60L);
        loan2.setClients(client2);

        UserEntity user2 = new UserEntity();
        user2.setUserId(null);
        loan2.setUsers(user2);

        ToolEntity tool2 = new ToolEntity();
        tool2.setToolId(null);
        loan2.setTools(tool2);

        when(loanRepository.findAll()).thenReturn(List.of(loan1, loan2));

        // --- Ejecutamos ---
        List<LoanResponseDTO> result = loanService.getAllLoans();

        // --- Validaciones ---
        assertThat(result).hasSize(2);

        // Loan 1
        assertThat(result.get(0).getLoanId()).isEqualTo(1L);
        assertThat(result.get(0).getLoanStatus()).isEqualTo("FINALIZADO");
        assertThat(result.get(0).getClientId()).isEqualTo(50L);
        assertThat(result.get(0).getToolId()).isEqualTo(70L);
        assertThat(result.get(0).getUserId()).isEqualTo(100L);

        // Loan 2
        assertThat(result.get(1).getLoanId()).isEqualTo(2L);
        assertThat(result.get(1).getLoanStatus()).isEqualTo("ACTIVO");
        assertThat(result.get(1).getClientId()).isEqualTo(60L);
        assertThat(result.get(1).getToolId()).isNull();
        assertThat(result.get(1).getUserId()).isNull();

        verify(loanRepository, times(1)).findAll();
    }


    // ============================================================
    // ===================== TEST: deleteLoans =====================
    // ============================================================

    @Test
    void testDeleteLoanById_WhenLoanExists_DeletesSuccessfully() {
        Long loanId = 10L;

        when(loanRepository.existsById(loanId)).thenReturn(true);

        loanService.deleteLoansById(loanId);

        verify(loanRepository, times(1)).deleteById(loanId);
    }

    @Test
    void testDeleteLoanById_WhenLoanDoesNotExist_ThrowsException() {
        Long loanId = 99L;

        when(loanRepository.existsById(loanId)).thenReturn(false);

        assertThatThrownBy(() -> loanService.deleteLoansById(loanId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("No existe préstamo con id: 99");

        verify(loanRepository, never()).deleteById(any());
    }

    // ============================================================
    // ===================== TEST: createLoans =====================
    // ============================================================

    @Test
    void createLoans_success() {
        // --- Datos de entrada (LoanRequestDTO) ---
        LocalDateTime requestedDeadline = LocalDateTime.now().plusDays(3);
        LoanRequestDTO request = new LoanRequestDTO();
        request.setDeadline(requestedDeadline);
        request.setClientId(11L);
        request.setUserId(22L);
        request.setToolId(33L);

        // --- Entidades que devolverán los mocks ---
        ClientEntity client = new ClientEntity();
        client.setClientId(11L);
        client.setClientFirstName("Cliente");
        client.setClientLastName("Prueba");
        client.setClientState(ClientService.ClientStatus.ACTIVO);

        UserEntity user = new UserEntity();
        user.setUserId(22L);

        ToolCatalogEntity catalog = new ToolCatalogEntity();
        catalog.setToolCatalogId(77L);
        catalog.setDailyRentalValue(100.0);
        catalog.setAvailableUnits(5);
        catalog.setReplacementValue(1000.0);

        ToolEntity tool = new ToolEntity();
        tool.setToolId(33L);
        tool.setTool_catalogs(catalog);
        // tool state assumed ok in toolService.validateAndLoanTool mock

        // --- Mocks comportamiento ---
        when(clientService.validateAndLoadClient(eq(11L))).thenReturn(client);
        when(userRepository.findById(eq(22L))).thenReturn(Optional.of(user));
        when(toolService.validateAndLoanTool(eq(33L))).thenReturn(tool);

        // Cuando loanRepository.save(...) se llame, devolvemos la misma entidad pasada pero con ID asignado
        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(invocation -> {
            LoanEntity arg = invocation.getArgument(0);
            arg.setLoanId(1L); // simular id generado por BD
            return arg;
        });

        // kardexService debe aceptarlo (no hacemos nada)
        doNothing().when(kardexService).registerMovement(any());

        // --- Ejecución ---
        LoanResponseDTO result = loanService.createLoans(request);

        // --- Asserts ---
        assertThat(result).isNotNull();
        assertThat(result.getLoanId()).isEqualTo(1L);
        assertThat(result.getClientId()).isEqualTo(11L);
        assertThat(result.getUserId()).isEqualTo(22L);
        assertThat(result.getToolId()).isEqualTo(33L);
        // deadline debe ser la misma que la del request
        assertThat(result.getDeadline()).isEqualTo(requestedDeadline);
        // deliveryDate fue generado por el servicio: comprobamos que no sea nulo y sea cercano a ahora
        assertThat(result.getDeliveryDate()).isNotNull();
        assertThat(result.getLoanStatus()).isEqualTo(LoanService.LoanStatus.ACTIVO.name());

        // Verificar que se llamó a los mocks esperados
        verify(clientService, times(1)).validateAndLoadClient(eq(11L));
        verify(userRepository, times(1)).findById(eq(22L));
        verify(toolService, times(1)).validateAndLoanTool(eq(33L));
        verify(loanRepository, times(1)).save(any(LoanEntity.class));
        verify(kardexService, times(1)).registerMovement(any());
    }

    // ============================================================
    // ===================== TEST: returnLoans =====================
    // ============================================================

    @Test
    void testReturnLoans() {
        // --- Datos de entrada ---
        Long loanId = 55L;
        Long userId = 22L;
        String damageLevelStr = "NINGUNO";

        // --- Entidades simuladas ---

        // Cliente
        ClientEntity client = new ClientEntity();
        client.setClientId(100L);
        client.setClientState(ClientService.ClientStatus.ACTIVO);

        // Catálogo
        ToolCatalogEntity catalog = new ToolCatalogEntity();
        catalog.setToolCatalogId(200L);
        catalog.setDailyRentalValue(5000.0);
        catalog.setReplacementValue(20000.0);
        catalog.setAvailableUnits(3);

        // Herramienta
        ToolEntity tool = new ToolEntity();
        tool.setToolId(99L);
        tool.setCurrentToolState(ToolService.ToolStatus.PRESTADA);
        tool.setTool_catalogs(catalog);

        // Préstamo
        LoanEntity loan = new LoanEntity();
        loan.setLoanId(loanId);
        loan.setDeliveryDate(LocalDateTime.of(2024, 1, 10, 10, 0));
        loan.setDeadline(LocalDateTime.of(2024, 1, 15, 18, 0));
        loan.setLoanStatus(LoanService.LoanStatus.POR_PAGAR);
        loan.setRentalAmount(15000.0);
        loan.setClients(client);
        loan.setTools(tool);

        // Usuario
        UserEntity user = new UserEntity();
        user.setUserId(userId);

        // Config de tarifas
        PenaltyConfigEntity config = new PenaltyConfigEntity();
        config.setPenaltyConfigId(1L);
        config.setDailyFineRate(1000.0);
        config.setRepairCharge(3000.0);

        when(userRepository.findById(eq(userId))).thenReturn(Optional.of(user));
        when(loanRepository.findById(eq(loanId))).thenReturn(Optional.of(loan));
        lenient().when(toolRepository.findById(anyLong())).thenReturn(Optional.of(tool));
        when(toolCatalogRepository.save(any(ToolCatalogEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Kardex se mockea
        doNothing().when(kardexService).registerMovement(any());

        // Guardar préstamo devuelve el préstamo actualizado
        when(loanRepository.save(any(LoanEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(penaltyConfigRepository.findTopByOrderByPenaltyConfigIdDesc())
                .thenReturn(Optional.of(config));

        // --- Ejecución ---
        LoanEntity result = loanService.returnLoans(loanId, userId, damageLevelStr);

        // --- Asserts ---
        assertNotNull(result);
        assertEquals(loanId, result.getLoanId());
        assertNotNull(result.getReturnDate());
        assertEquals(LoanService.LoanStatus.POR_PAGAR, result.getLoanStatus());

        assertEquals(loan.getDeliveryDate(), result.getDeliveryDate());
        assertEquals(loan.getDeadline(), result.getDeadline());

        // --- Verificaciones ---
        verify(loanRepository, times(1)).findById(eq(loanId));
        verify(loanRepository, times(1)).save(any(LoanEntity.class));
        verify(toolService, never()).validateAndLoanTool(anyLong());
        verify(kardexService, times(1)).registerMovement(any());
    }


    // ============================================================
    // ===================== TEST: payLoans =====================
    // ============================================================
    @Test
    void testPayLoans_SuccessfulPayment() {

        // --- DATA SETUP ---
        Long loanId = 10L;

        ClientEntity client = new ClientEntity();
        client.setClientId(100L);
        client.setClientState(ClientService.ClientStatus.RESTRINGIDO);

        PenaltyEntity penalty = new PenaltyEntity();
        penalty.setPenaltyId(7L);
        penalty.setPenaltyStatus(PenaltyService.PaymentStatus.IMPAGO);

        LoanEntity loan = new LoanEntity();
        loan.setLoanId(loanId);
        loan.setRentalAmount(5000.0);
        loan.setLoanStatus(LoanService.LoanStatus.POR_PAGAR);
        loan.setClients(client);
        loan.setPenalty(penalty);

        // Payment request
        PaymentRequestDTO request = new PaymentRequestDTO();
        request.setAmountPaid(5000.0);

        // --- MOCKING ---
        when(loanRepository.findById(loanId)).thenReturn(Optional.of(loan));
        when(loanRepository.existsByClients_ClientIdAndLoanStatusIn(
                eq(100L),
                anyList()
        )).thenReturn(false); // no tiene otras deudas

        when(loanRepository.save(any(LoanEntity.class))).thenAnswer(inv -> inv.getArgument(0));

        // --- EXECUTE ---
        LoanEntity result = loanService.payLoans(loanId, request);

        // --- VERIFY RESULTS ---
        assertNotNull(result);
        assertEquals(LoanService.LoanStatus.FINALIZADO, result.getLoanStatus());
        assertEquals(PenaltyService.PaymentStatus.PAGADO, penalty.getPenaltyStatus());
        assertEquals(ClientService.ClientStatus.ACTIVO, client.getClientState());

        // --- VERIFY INTERACTIONS ---
        verify(loanRepository, times(1)).findById(loanId);
        verify(loanRepository, times(1)).save(loan);
        verify(penaltyRepository, times(1)).save(penalty);
        verify(clientRepository, times(1)).save(client);
    }

}
