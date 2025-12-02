package com.example.demo;

import com.example.demo.DTOs.KardexCreateDTO;
import com.example.demo.DTOs.ToolCatalogAddUnitsDTO;
import com.example.demo.DTOs.ToolCatalogDTO;
import com.example.demo.Entity.ToolCatalogEntity;
import com.example.demo.Entity.ToolEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.ToolCatalogRepository;
import com.example.demo.Repository.ToolRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.KardexService;
import com.example.demo.Service.ToolCatalogService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ToolCatalogServiceTest {

    @Mock
    private ToolCatalogRepository toolCatalogRepository;

    @Mock
    private ToolRepository toolRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private KardexService kardexService;

    @InjectMocks
    private ToolCatalogService toolCatalogService;

    // -------------------------------------------------------------
    // getAllCatalogsDTO()
    // -------------------------------------------------------------
    @Test
    void testGetAllCatalogsDTO_ReturnsList() {
        ToolCatalogEntity c1 = new ToolCatalogEntity();
        c1.setToolCatalogId(1L);
        c1.setToolName("Taladro");
        c1.setToolCategory("Electricidad");
        c1.setDailyRentalValue(5000.0);
        c1.setReplacementValue(20000.0);
        c1.setDescription("Taladro industrial");
        c1.setAvailableUnits(3);

        when(toolCatalogRepository.findAll()).thenReturn(List.of(c1));

        List<ToolCatalogDTO> result = toolCatalogService.getAllCatalogsDTO();

        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getToolCatalogId());
        assertEquals("Taladro", result.get(0).getToolName());

        verify(toolCatalogRepository, times(1)).findAll();
    }

    // -------------------------------------------------------------
    // getCatalogsById()
    // -------------------------------------------------------------
    @Test
    void testGetCatalogsById_Found() {
        ToolCatalogEntity entity = new ToolCatalogEntity();
        entity.setToolCatalogId(10L);
        entity.setToolName("Martillo");

        when(toolCatalogRepository.findById(10L)).thenReturn(Optional.of(entity));

        ToolCatalogDTO result = toolCatalogService.getCatalogsById(10L);

        assertEquals(10L, result.getToolCatalogId());
        assertEquals("Martillo", result.getToolName());
    }

    @Test
    void testGetCatalogsById_NotFound_Throws() {
        when(toolCatalogRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> toolCatalogService.getCatalogsById(99L)
        );

        assertTrue(ex.getMessage().contains("Id: 99"));
    }

    // -------------------------------------------------------------
    // getCatalogsByName()
    // -------------------------------------------------------------
    @Test
    void testGetCatalogsByName_ReturnsList() {
        ToolCatalogEntity e = new ToolCatalogEntity();
        e.setToolCatalogId(5L);
        e.setToolName("Sierra");

        when(toolCatalogRepository.findByToolNameContainingIgnoreCase("sie"))
                .thenReturn(List.of(e));

        List<ToolCatalogDTO> result = toolCatalogService.getCatalogsByName("sie");

        assertEquals(1, result.size());
        assertEquals("Sierra", result.get(0).getToolName());
    }

    // -------------------------------------------------------------
    // createCatalogs()
    // -------------------------------------------------------------
    @Test
    void testCreateCatalogs_Success() {
        ToolCatalogEntity catalog = new ToolCatalogEntity();
        catalog.setToolCatalogId(1L);
        catalog.setToolName("Lijadora");
        catalog.setAvailableUnits(2);

        UserEntity user = new UserEntity();
        user.setUserId(11L);

        when(toolCatalogRepository.save(any())).thenReturn(catalog);

        ToolCatalogEntity result = toolCatalogService.createCatalogs(catalog, user);

        assertEquals(1L, result.getToolCatalogId());
        verify(toolRepository, times(2)).save(any(ToolEntity.class)); // crea 2 unidades
        verify(kardexService, times(1)).registerMovement(any(KardexCreateDTO.class));
    }

    // -------------------------------------------------------------
    // addUnitsToCatalog()
    // -------------------------------------------------------------
    @Test
    void testAddUnitsToCatalog_Success() {
        ToolCatalogEntity catalog = new ToolCatalogEntity();
        catalog.setToolCatalogId(5L);
        catalog.setToolName("Compresor");
        catalog.setAvailableUnits(3);

        UserEntity user = new UserEntity();
        user.setUserId(20L);

        ToolCatalogAddUnitsDTO dto = new ToolCatalogAddUnitsDTO();
        dto.setUnits(2);
        dto.setUserId(20L);

        when(toolCatalogRepository.findById(5L)).thenReturn(Optional.of(catalog));
        when(userRepository.findById(20L)).thenReturn(Optional.of(user));
        when(toolCatalogRepository.save(any())).thenReturn(catalog);

        ToolCatalogDTO result = toolCatalogService.addUnitsToCatalog(5L, dto);

        assertEquals(5, result.getAvailableUnits());
        verify(toolRepository, times(2)).save(any(ToolEntity.class));
        verify(kardexService, times(1)).registerMovement(any(KardexCreateDTO.class));
    }

    @Test
    void testAddUnitsToCatalog_InvalidUnits_Throws() {
        ToolCatalogAddUnitsDTO dto = new ToolCatalogAddUnitsDTO();
        dto.setUnits(0);
        dto.setUserId(1L);

        ToolCatalogEntity catalog = new ToolCatalogEntity();
        catalog.setAvailableUnits(3);

        when(toolCatalogRepository.findById(1L)).thenReturn(Optional.of(catalog));
        when(userRepository.findById(1L)).thenReturn(Optional.of(new UserEntity()));

        assertThrows(
                IllegalArgumentException.class,
                () -> toolCatalogService.addUnitsToCatalog(1L, dto)
        );
    }

    // -------------------------------------------------------------
    // deleteCatalogsById()
    // -------------------------------------------------------------
    @Test
    void testDeleteCatalogsById_Existing() {
        when(toolCatalogRepository.existsById(7L)).thenReturn(true);

        toolCatalogService.deleteCatalogsById(7L);

        verify(toolCatalogRepository, times(1)).deleteById(7L);
    }

    @Test
    void testDeleteCatalogsById_NotExisting_Throws() {
        when(toolCatalogRepository.existsById(50L)).thenReturn(false);

        Exception ex = assertThrows(
                IllegalArgumentException.class,
                () -> toolCatalogService.deleteCatalogsById(50L)
        );

        assertTrue(ex.getMessage().contains("50"));
        verify(toolCatalogRepository, never()).deleteById(any());
    }
}

