package com.example.demo.Service;

import com.example.demo.DTOs.LoanResponseDTO;
import com.example.demo.DTOs.LoanRequestDTO;
import com.example.demo.Entity.ClientEntity;
import com.example.demo.Entity.LoanEntity;
import com.example.demo.Entity.ToolCatalogEntity;
import com.example.demo.Entity.ToolEntity;
import com.example.demo.Entity.UserEntity;
import com.example.demo.Repository.ClientRepository;
import com.example.demo.Repository.LoanRepository;
import com.example.demo.Repository.PenaltyRepository;
import com.example.demo.Repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
        VENCIDO,
        FINALIZADO
    }

    @Scheduled(cron = "*/30 * * * * *") // se comprueba todos los días a media noche
    public void checkAndUpdateLoanStatus() {

        // Buscar prestamos activos que ya expiraron
        List<LoanEntity> overdue = loanRepository.findByLoanStatusAndReturnDateIsNullAndDeadlineBefore(LoanStatus.ACTIVO, LocalDateTime.now());

        // Actualiza el estado de los prestamos
        overdue.forEach(loan -> loan.setLoanStatus(LoanStatus.VENCIDO));

        // Guarda en la base de datos
        loanRepository.saveAll(overdue);
    }

    public List<LoanResponseDTO> getAllLoans() {

        return loanRepository.findAll().stream()
                .map( loan -> {
                    LoanResponseDTO loanDTO = new LoanResponseDTO();
                    loanDTO.setLoanId(loan.getLoanId());
                    loanDTO.setDeliveryDate(loan.getDeliveryDate());
                    loanDTO.setDeadline(loan.getDeadline());
                    loanDTO.setReturnDate(loan.getReturnDate());
                    loanDTO.setRentalAmount(loan.getRentalAmount());
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
        if (penaltyRepository.existsByLoan_Clients_ClientIdAndPenaltyStatus(
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

        // Validación --> fecha limite de retorno (deadline) no puede ser anterior a fecha de entrega (deliveryDate)
        LocalDateTime deliveryDate = LocalDateTime.now();
        LocalDateTime deadline = request.getDeadline();

        if (deadline != null && deadline.isBefore(deliveryDate)) {
            throw new IllegalArgumentException(
                    "La fecha límite de entrega (deadline) no puede ser anterior a la fecha actual."
            );
        }

        // Crear el prestamo con las entidades asociadas
        LoanEntity loan = LoanEntity.builder()
                .deliveryDate(deliveryDate)
                .deadline(deadline)
                .returnDate(null)
                .loanStatus(LoanStatus.ACTIVO)
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
                savedLoan.getRentalAmount(),
                savedLoan.getLoanStatus().name(),
                client.getClientId(),
                user.getUserId(),
                tool.getToolId()
        );
    }

    public LoanEntity returnLoans(Long loanId) {

        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        // Asignar returnDate = now()
        LocalDateTime now = LocalDateTime.now();
        loan.setReturnDate(now);

        // Se obtienen las fechas de entrega y devolucion pactada del prestamo
        LocalDateTime delivery = loan.getDeliveryDate();
        LocalDateTime deadline = loan.getDeadline();

        // Codigo para pruebas ***
        ToolEntity tool = loan.getTools();
        if (tool == null) throw new RuntimeException("Tool no asociada al préstamo");

        ToolCatalogEntity catalog = tool.getTool_catalogs();
        if (catalog == null) throw new RuntimeException("Catálogo no asociado a la herramienta");

        Double rentalValue = catalog.getDailyRentalValue();
        if (rentalValue == null) throw new RuntimeException("rentalValue es null");


        // Se obtiene el valor de arriendo diario de la herramienta desde el catalogo
        //Double rentalValue = loan.getTools().getTool_catalogs().getDailyRentalValue();
        double rentalAmount;

        // Se calcula la cantidad de dias que duro el prestamo
        long totalDays;

        if (!now.isAfter(deadline)) {
            // No hay retraso
            totalDays = ChronoUnit.DAYS.between(deadline, now);

            if (totalDays < 1) {
                totalDays = 1; // El cobro minimo es de 1 dia
            }

            rentalAmount = rentalValue * totalDays;

        } else {
            // Hay retraso
            long daysToDeadline = ChronoUnit.DAYS.between(delivery, deadline);

            if (daysToDeadline < 1) {
                daysToDeadline = 1;
            }

            rentalAmount = rentalValue * daysToDeadline;

            // Calcular el monto de la multa
        }

        loan.setRentalAmount(rentalAmount);

        // Se actualiza el estado del prestamo
        loan.setLoanStatus(LoanStatus.FINALIZADO);

        // Lógica: calcular días
        // rentalAmount = rentalValue * days
        // asignar returnDate = now()
        // guardar loan

        return loanRepository.save(loan);
    }

    public void deleteLoansById(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe préstamo con id: " + id);
        }
        loanRepository.deleteById(id);
    }
}
