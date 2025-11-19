package com.example.demo.Service;

import com.example.demo.DTOs.KardexCreateDTO;
import com.example.demo.DTOs.ToolDTO;
import com.example.demo.DTOs.ToolEvaluationDTO;
import com.example.demo.Entity.LoanEntity;
import com.example.demo.Entity.ToolCatalogEntity;
import com.example.demo.Entity.ToolEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.LoanRepository;
import com.example.demo.Repository.ToolCatalogRepository;
import com.example.demo.Repository.ToolRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ToolService {

    private final ToolRepository toolRepository;
    private final ToolCatalogRepository toolCatalogRepository;
    private final LoanRepository loanRepository;
    private final KardexService kardexService;
    private final UserRepository userRepository;

    @Autowired
    public ToolService(ToolRepository toolRepository, ToolCatalogRepository toolCatalogRepository, LoanRepository loanRepository, KardexService kardexService, UserRepository userRepository) {
        this.toolRepository = toolRepository;
        this.toolCatalogRepository = toolCatalogRepository;
        this.loanRepository = loanRepository;
        this.kardexService = kardexService;
        this.userRepository = userRepository;
    }

    //Se definen los estados posibles de una herramienta
    public enum ToolStatus {
        DISPONIBLE,
        PRESTADA,
        EN_REPARACION,
        DADA_DE_BAJA
    }

    // Se definen las posibles decisiones de evaluacion de daño de una herramienta
    public enum ToolEvaluationDecision {
        DAR_DE_BAJA,
        REPARADA
    }

    public ToolEntity validateAndLoanTool(Long toolId) {

        // Buscar herramienta
        ToolEntity tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

        // Validar estado disponible
        if(tool.getCurrentToolState() != ToolStatus.DISPONIBLE) {
            throw new RuntimeException("No se puede crear el préstamo: la herramienta no tiene estado disponible.");
        }

        // Validar catálogo
        ToolCatalogEntity catalog = tool.getTool_catalogs();
        if (catalog == null) {
            throw new RuntimeException("No se puede crear el préstamo: la herramienta no tiene un catálogo asociado.");
        }

        // Validar stock
        if (catalog.getAvailableUnits() <= 0) {
            throw new RuntimeException("No se puede crear el préstamo: no hay stock disponible.");
        }

        // Actualizar estado
        tool.setCurrentToolState(ToolStatus.PRESTADA);

        // Reducir stock
        catalog.setAvailableUnits(catalog.getAvailableUnits() - 1);

        // Guardar cambios
        toolRepository.save(tool);
        toolCatalogRepository.save(catalog);

        return tool;
    }

    @Transactional
    public ToolEntity evaluateTools(Long toolId, Long userId, ToolEvaluationDTO dto) {

        // Comprueba si existe la herramienta
        ToolEntity tool = toolRepository.findById(toolId)
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

        // Comprueba que el estado de la herramienta sea en reparacion
        if (tool.getCurrentToolState() != ToolStatus.EN_REPARACION) {
            throw new RuntimeException("La herramienta no tiene estado EN_REPARACION");
        }

        // Comprueba que existe el usuario
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Comprueba que existe el prestamo
        LoanEntity loan = loanRepository.findById(dto.getLoanId())
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        // Comprueba que el id de la herramienta corresponda al prestamo
        if (!loan.getTools().getToolId().equals(toolId)) {
            throw new RuntimeException("El préstamo no corresponde a esta herramienta");
        }

        // Se obtiene el catalogo asociado a la herramienta
        ToolCatalogEntity catalog = tool.getTool_catalogs();

        switch (dto.getDecision()) {
            case DAR_DE_BAJA -> {
                tool.setCurrentToolState(ToolStatus.DADA_DE_BAJA);

                // Registrar movimiento en kardex
                kardexService.registerMovement(
                        KardexCreateDTO.builder()
                                .type(KardexService.KardexMovementType.BAJA)
                                .affectedAmount(1)
                                .details("Herramienta dada de baja tras evaluación")
                                .toolId(tool.getToolId())
                                .catalogId(catalog.getToolCatalogId())
                                .loanId(loan.getLoanId())
                                .userId(user.getUserId())
                                .clientId(loan.getClients().getClientId())
                                .build()
                );
            }

            case REPARADA -> {
                tool.setCurrentToolState(ToolStatus.DISPONIBLE);
                catalog.setAvailableUnits(catalog.getAvailableUnits() + 1);
                toolCatalogRepository.save(catalog);

                // Registrar movimiento en kardex
                kardexService.registerMovement(
                        KardexCreateDTO.builder()
                                .type(KardexService.KardexMovementType.REPARACION)
                                .affectedAmount(1)
                                .details("Herramienta reparada y devuelta al inventario")
                                .toolId(tool.getToolId())
                                .catalogId(catalog.getToolCatalogId())
                                .loanId(loan.getLoanId())
                                .userId(user.getUserId())
                                .clientId(loan.getClients().getClientId())
                                .build()
                );
            }
        }

        return toolRepository.save(tool);

    }

    public List<ToolDTO> getAllToolsDTO() {
        List<ToolEntity> tools = toolRepository.findAll();
        return tools.stream()
                .map(ToolDTO::new) // usa el constructor que mapea desde la entidad
                .collect(Collectors.toList());
    }

    public ToolEntity createTools(ToolEntity tool) {
        tool.setCurrentToolState(ToolStatus.DISPONIBLE);
        return toolRepository.save(tool);
    }

    public void deleteToolsById(Long id) {
        if (!toolRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe herramienta con id: " + id);
        }
        toolRepository.deleteById(id);
    }


}
