package com.example.demo;

import com.example.demo.DTOs.KardexCreateDTO;
import com.example.demo.DTOs.ToolDTO;
import com.example.demo.DTOs.ToolEvaluationDTO;
import com.example.demo.Entity.*;
import com.example.demo.Repository.*;
import com.example.demo.Service.KardexService;
import com.example.demo.Service.ToolService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolServiceTest {

    @Mock private ToolRepository toolRepository;
    @Mock private ToolCatalogRepository toolCatalogRepository;
    @Mock private LoanRepository loanRepository;
    @Mock private KardexService kardexService;
    @Mock private UserRepository userRepository;

    @InjectMocks private ToolService toolService;

    // -------------------------------------------------------------
    // validateAndLoanTool()
    // -------------------------------------------------------------
    @Test
    void testValidateAndLoanTool_Success() {
        ToolCatalogEntity catalog = new ToolCatalogEntity();
        catalog.setAvailableUnits(3);

        ToolEntity tool = new ToolEntity();
        tool.setToolId(10L);
        tool.setCurrentToolState(ToolService.ToolStatus.DISPONIBLE);
        tool.setTool_catalogs(catalog);

        when(toolRepository.findById(10L)).thenReturn(Optional.of(tool));
        when(toolRepository.save(any())).thenReturn(tool);

        ToolEntity result = toolService.validateAndLoanTool(10L);

        assertEquals(ToolService.ToolStatus.PRESTADA, result.getCurrentToolState());
        assertEquals(2, catalog.getAvailableUnits());
        verify(toolRepository).save(any(ToolEntity.class));
        verify(toolCatalogRepository).save(catalog);
    }

    @Test
    void testValidateAndLoanTool_NotFound_Throws() {
        when(toolRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> toolService.validateAndLoanTool(99L));
    }

    @Test
    void testValidateAndLoanTool_NotAvailable_Throws() {
        ToolEntity tool = new ToolEntity();
        tool.setCurrentToolState(ToolService.ToolStatus.PRESTADA);

        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));

        assertThrows(RuntimeException.class, () -> toolService.validateAndLoanTool(1L));
    }

    @Test
    void testValidateAndLoanTool_NoStock_Throws() {
        ToolCatalogEntity catalog = new ToolCatalogEntity();
        catalog.setAvailableUnits(0);

        ToolEntity tool = new ToolEntity();
        tool.setCurrentToolState(ToolService.ToolStatus.DISPONIBLE);
        tool.setTool_catalogs(catalog);

        when(toolRepository.findById(5L)).thenReturn(Optional.of(tool));

        assertThrows(RuntimeException.class, () -> toolService.validateAndLoanTool(5L));
    }

    // -------------------------------------------------------------
    // evaluateTools()
    // -------------------------------------------------------------
    @Test
    void testEvaluateTools_DarDeBaja() {

        // --- Tool en reparación ---
        ToolEntity tool = new ToolEntity();
        tool.setToolId(1L);
        tool.setCurrentToolState(ToolService.ToolStatus.EN_REPARACION);

        ToolCatalogEntity catalog = new ToolCatalogEntity();
        catalog.setToolCatalogId(88L);
        tool.setTool_catalogs(catalog);

        // --- Usuario ---
        UserEntity user = new UserEntity();
        user.setUserId(5L);

        // --- Préstamo asociado ---
        LoanEntity loan = new LoanEntity();
        loan.setLoanId(100L);
        ClientEntity client = new ClientEntity();
        client.setClientId(777L);
        loan.setClients(client);

        // herramienta asociada al préstamo
        ToolEntity toolInLoan = new ToolEntity();
        toolInLoan.setToolId(1L);
        loan.setTools(toolInLoan);

        ToolEvaluationDTO dto = new ToolEvaluationDTO();
        dto.setLoanId(100L);
        dto.setDecision(ToolService.ToolEvaluationDecision.DAR_DE_BAJA);

        // --- Mocks ---
        when(toolRepository.findById(1L)).thenReturn(Optional.of(tool));
        when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        when(loanRepository.findById(100L)).thenReturn(Optional.of(loan));
        when(toolRepository.save(any())).thenReturn(tool);

        ToolEntity result = toolService.evaluateTools(1L, 5L, dto);

        assertEquals(ToolService.ToolStatus.DADA_DE_BAJA, result.getCurrentToolState());
        verify(kardexService, times(1)).registerMovement(any(KardexCreateDTO.class));
    }

    @Test
    void testEvaluateTools_Reparada() {

        // --- Tool en reparación ---
        ToolEntity tool = new ToolEntity();
        tool.setToolId(2L);
        tool.setCurrentToolState(ToolService.ToolStatus.EN_REPARACION);

        ToolCatalogEntity catalog = new ToolCatalogEntity();
        catalog.setToolCatalogId(44L);
        catalog.setAvailableUnits(5);
        tool.setTool_catalogs(catalog);

        // --- Usuario ---
        UserEntity user = new UserEntity();
        user.setUserId(20L);

        // --- Préstamo ---
        LoanEntity loan = new LoanEntity();
        loan.setLoanId(300L);
        ClientEntity client = new ClientEntity();
        client.setClientId(900L);
        loan.setClients(client);

        ToolEntity toolInLoan = new ToolEntity();
        toolInLoan.setToolId(2L);
        loan.setTools(toolInLoan);

        ToolEvaluationDTO dto = new ToolEvaluationDTO();
        dto.setLoanId(300L);
        dto.setDecision(ToolService.ToolEvaluationDecision.REPARADA);

        // --- Mocks ---
        when(toolRepository.findById(2L)).thenReturn(Optional.of(tool));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        when(loanRepository.findById(300L)).thenReturn(Optional.of(loan));
        when(toolRepository.save(any())).thenReturn(tool);
        when(toolCatalogRepository.save(any())).thenReturn(catalog);

        ToolEntity result = toolService.evaluateTools(2L, 20L, dto);

        assertEquals(ToolService.ToolStatus.DISPONIBLE, result.getCurrentToolState());
        assertEquals(6, catalog.getAvailableUnits()); // se sumó 1
        verify(kardexService, times(1)).registerMovement(any(KardexCreateDTO.class));
    }

    // -------------------------------------------------------------
    // getAllToolsDTO()
    // -------------------------------------------------------------
    @Test
    void testGetAllToolsDTO_ReturnsList() {
        // Catálogo mock
        ToolCatalogEntity catalog1 = new ToolCatalogEntity();
        catalog1.setToolCatalogId(100L);
        catalog1.setToolName("Martillo");

        ToolCatalogEntity catalog2 = new ToolCatalogEntity();
        catalog2.setToolCatalogId(200L);
        catalog2.setToolName("Destornillador");

        // Herramienta 1
        ToolEntity t1 = new ToolEntity();
        t1.setToolId(1L);
        t1.setCurrentToolState(ToolService.ToolStatus.DISPONIBLE);
        t1.setTool_catalogs(catalog1);

        // Herramienta 2
        ToolEntity t2 = new ToolEntity();
        t2.setToolId(2L);
        t2.setCurrentToolState(ToolService.ToolStatus.PRESTADA);
        t2.setTool_catalogs(catalog2);

        when(toolRepository.findAll()).thenReturn(List.of(t1, t2));

        List<ToolDTO> list = toolService.getAllToolsDTO();

        // Validaciones
        assertEquals(2, list.size());

        ToolDTO dto1 = list.get(0);
        assertEquals(1L, dto1.getToolId());
        assertEquals("DISPONIBLE", dto1.getCurrentToolState());
        assertEquals(100L, dto1.getToolCatalogId());
        assertEquals("Martillo", dto1.getToolCatalogName());

        ToolDTO dto2 = list.get(1);
        assertEquals(2L, dto2.getToolId());
        assertEquals("PRESTADA", dto2.getCurrentToolState());
        assertEquals(200L, dto2.getToolCatalogId());
        assertEquals("Destornillador", dto2.getToolCatalogName());
    }

    // -------------------------------------------------------------
    // createTools()
    // -------------------------------------------------------------
    @Test
    void testCreateTools() {
        ToolEntity tool = new ToolEntity();
        when(toolRepository.save(any())).thenReturn(tool);

        ToolEntity result = toolService.createTools(tool);

        assertEquals(ToolService.ToolStatus.DISPONIBLE, result.getCurrentToolState());
        verify(toolRepository).save(tool);
    }

    // -------------------------------------------------------------
    // deleteToolsById()
    // -------------------------------------------------------------
    @Test
    void testDeleteToolsById_Success() {
        when(toolRepository.existsById(7L)).thenReturn(true);

        toolService.deleteToolsById(7L);

        verify(toolRepository).deleteById(7L);
    }

    @Test
    void testDeleteToolsById_NotFound_Throws() {
        when(toolRepository.existsById(100L)).thenReturn(false);

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> toolService.deleteToolsById(100L)
        );

        assertTrue(ex.getMessage().contains("100"));
        verify(toolRepository, never()).deleteById(any());
    }
}

