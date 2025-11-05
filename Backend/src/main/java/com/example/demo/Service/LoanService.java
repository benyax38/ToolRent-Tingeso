package com.example.demo.Service;

import com.example.demo.DTOs.LoanResponseDTO;
import com.example.demo.DTOs.LoanRequestDTO;
import com.example.demo.Entity.ClientEntity;
import com.example.demo.Entity.LoanEntity;
import com.example.demo.Entity.ToolEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.ClientRepository;
import com.example.demo.Repository.LoanRepository;
import com.example.demo.Repository.PenaltyRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;
    private final PenaltyRepository penaltyRepository;
    private final ToolService toolService;

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

    @Transactional
    public LoanResponseDTO createLoans(LoanRequestDTO request) {

        // Buscar cliente
        ClientEntity client = clientRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        // Validar que el cliente esta activo
        if(client.getClientState() != ClientService.ClientStatus.ACTIVO) {
            throw new RuntimeException("No se puede crear el préstamo: el cliente no tiene estado activo.");
        }

        // Comprueba que el cliente no tenga prestamos vencidos
        if(loanRepository.existsByClients_ClientIdAndLoanStatus(client.getClientId(), LoanStatus.VENCIDO)) {
            throw new RuntimeException("No se puede crear el préstamo: el cliente tiene préstamos vencidos.");
        }

        // Valida que el cliente no tenga deudas impagas
        if (penaltyRepository.existsByLoans_Clients_ClientIdAndPenaltyStatus(
                client.getClientId(),
                PenaltyService.PaymentStatus.IMPAGO
        )) {
            throw new RuntimeException("No se puede crear el préstamo: el cliente tiene multas impagas.");
        }

        // Buscar usuario
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        /* Validaciones y movimientos desde ToolService
            * 1. Buscar herramienta
            * 2. Validar estado DISPONIBLE de la herramienta
            * 3. Validar que exista catalogo asociado
            * 4. Validar stock positivo
            * 5. Marcar herramienta como PRESTADA
            * 6. Reducir el stock en el catalogo
            * 7. Guarda los cambios
        */
        ToolEntity tool = toolService.validateAndLoanTool(request.getToolId());

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
