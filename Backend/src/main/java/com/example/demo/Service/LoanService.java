package com.example.demo.Service;

import com.example.demo.DTOs.LoanResponseDTO;
import com.example.demo.DTOs.LoanRequestDTO;
import com.example.demo.Entity.ClientEntity;
import com.example.demo.Entity.LoanEntity;
import com.example.demo.Entity.ToolEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.ClientRepository;
import com.example.demo.Repository.LoanRepository;
import com.example.demo.Repository.ToolRepository;
import com.example.demo.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final ToolRepository toolRepository;

    // Aqui se definen los roles del sistema
    public enum LoanStatus {
        ACTIVO,
        VENCIDO
    }

    public List<LoanResponseDTO> getAllLoans() {

        return loanRepository.findAll().stream()
                .map( loan -> {
                    LoanResponseDTO loanDTO = new LoanResponseDTO();
                    loanDTO.setLoanId(loan.getLoanId());
                    loanDTO.setDeliveryDate(loan.getDeliveryDate());
                    loanDTO.setDeadline(loan.getDeadline());
                    loanDTO.setReturnDate(loan.getReturnDate());
                    loanDTO.setLoanStatus(loan.getLoanStatus().name());
                    loanDTO.setClientId(loan.getClients().getClientId());
                    loanDTO.setUserId(loan.getUsers().getUserId());
                    loanDTO.setToolId(loan.getTools().getToolId());
                    return loanDTO;
                })
                .toList();
    }

    public LoanResponseDTO createLoans(LoanRequestDTO request) {

        // Buscar cliente
        ClientEntity client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Validar que el cliente esta activo
        if(client.getClientState() != ClientService.ClientStatus.ACTIVO) {
            throw new RuntimeException("No se puede crear el préstamo: el cliente no tiene estado activo.");
        }

        // Buscar usuario
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Buscar herramienta
        ToolEntity tool = toolRepository.findById(request.getToolId())
                .orElseThrow(() -> new RuntimeException("Herramienta no encontrada"));

        // Crear el prestamo con las entidades asociadas
        LoanEntity loan = LoanEntity.builder()
                .deliveryDate(request.getDeliveryDate())
                .deadline(request.getDeadline())
                .returnDate(request.getReturnDate())
                .loanStatus(LoanStatus.valueOf(request.getLoanStatus()))
                .clients(client)
                .users(user)
                .tools(tool)
                .build();

        // Guardar en la base de datos
        LoanEntity savedLoan = loanRepository.save(loan);

        // Retornar un DTO como respuesta
        return new LoanResponseDTO(
                savedLoan.getLoanId(),
                savedLoan.getDeliveryDate(),
                savedLoan.getDeadline(),
                savedLoan.getReturnDate(),
                savedLoan.getLoanStatus().name(),
                client.getClientId(),
                user.getUserId(),
                tool.getToolId()
        );
    }

    public void deleteLoansById(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe préstamo con id: " + id);
        }
        loanRepository.deleteById(id);
    }
}
