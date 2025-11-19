package com.example.demo.Service;

import com.example.demo.DTOs.KardexCreateDTO;
import com.example.demo.DTOs.KardexDTO;
import com.example.demo.Entity.KardexEntity;
import com.example.demo.Repository.ClientRepository;
import com.example.demo.Repository.KardexRepository;
import com.example.demo.Repository.LoanRepository;
import com.example.demo.Repository.ToolCatalogRepository;
import com.example.demo.Repository.ToolRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KardexService {

    private final KardexRepository kardexRepository;
    private final ClientRepository clientRepository;
    private final LoanRepository loanRepository;
    private final ToolRepository toolRepository;
    private final ToolCatalogRepository toolCatalogRepository;
    private final UserRepository userRepository;

    @Autowired
    public KardexService(KardexRepository kardexRepository, ClientRepository clientRepository, LoanRepository loanRepository, ToolRepository toolRepository, ToolCatalogRepository toolCatalogRepository, UserRepository userRepository) { this.kardexRepository = kardexRepository;
        this.clientRepository = clientRepository;
        this.loanRepository = loanRepository;
        this.toolRepository = toolRepository;
        this.toolCatalogRepository = toolCatalogRepository;
        this.userRepository = userRepository;
    }

    public enum KardexMovementType {
        INGRESO,
        PRESTAMO,
        DEVOLUCION,
        BAJA,
        REPARACION
    }

    public List<KardexDTO> getAllKardexesDTO() {
        return kardexRepository.findAll().stream()
                .map(kardex -> new KardexDTO(
                        kardex.getMovementId(),
                        kardex.getType(),
                        kardex.getMovementDate(),
                        kardex.getAffectedAmount(),
                        kardex.getDetails(),
                        kardex.getClients() != null ? kardex.getClients().getClientId() : null,
                        kardex.getLoans() != null ? kardex.getLoans().getLoanId() : null,
                        kardex.getTools() != null ? kardex.getTools().getToolId() : null,
                        kardex.getTool_catalogs() != null ? kardex.getTool_catalogs().getToolCatalogId() : null,
                        kardex.getUsers() != null ? kardex.getUsers().getUserId() : null
                ))
                .toList();
    }

    public KardexEntity createKardexes(KardexEntity kardex) {
        return kardexRepository.save(kardex);
    }

    @Transactional
    public void registerMovement(KardexCreateDTO dto) {

        KardexEntity movement = new KardexEntity();
        movement.setType(dto.getType().name());
        movement.setMovementDate(LocalDateTime.now());
        movement.setAffectedAmount(dto.getAffectedAmount());
        movement.setDetails(dto.getDetails());

        // relaciones
        if (dto.getClientId() != null) {
            movement.setClients(clientRepository.getReferenceById(dto.getClientId()));
        }

        if (dto.getLoanId() != null) {
            movement.setLoans(loanRepository.getReferenceById(dto.getLoanId()));
        }

        if (dto.getToolId() != null) {
            movement.setTools(toolRepository.getReferenceById(dto.getToolId()));
        }

        if (dto.getCatalogId() != null) {
            movement.setTool_catalogs(toolCatalogRepository.getReferenceById(dto.getCatalogId()));
        }

        if (dto.getUserId() != null) {
            movement.setUsers(userRepository.getReferenceById(dto.getUserId()));
        }

        kardexRepository.save(movement);
    }

    public void deleteKardexesById(Long id) {
        if (!kardexRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe kardex con id: " + id);
        }
        kardexRepository.deleteById(id);
    }
}
