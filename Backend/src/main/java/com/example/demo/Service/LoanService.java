package com.example.demo.Service;

import com.example.demo.DTOs.LoanResponseDTO;
import com.example.demo.DTOs.LoanRequestDTO;
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
    private final PenaltyConfigRepository penaltyConfigRepository;
    private final ToolRepository toolRepository;
    private final ToolCatalogRepository toolCatalogRepository;

    /* Aqui se definen los posibles estados de un prestamo
        * ACTIVO : El prestamo ha sido iniciado y aun no pasa su deadline.
        * VENCIDO : El prestamo ha superado su deadline.
        * POR_PAGAR : Las herramientas han sido devueltas y se espera el pago del cliente.
        * FINALIZADO : El pago del arriendo se ha efectuado.

     */
    public enum LoanStatus {
        ACTIVO,
        VENCIDO,
        POR_PAGAR,
        FINALIZADO
    }

    /* Aqui se definen los estados en que puede ser devuelta una herramienta
        * NINGUNO : La herramienta esta en perfecto estado.
        * LEVE : La herramienta fue devuelta con daños leves.
        * GRAVE : La herramienta fue devuelta con daños considerables.
     */
    public enum DamageLevel {
        NINGUNO,
        LEVE,
        GRAVE
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

    public LoanEntity returnLoans(Long loanId, String damageLevelStr) {

        // Busca el prestamo con el id ingresado
        LoanEntity loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        // Se usara esta variable para returnDate = now()
        LocalDateTime now = LocalDateTime.now();

        // Se obtienen las fechas de entrega y devolucion pactada del prestamo
        LocalDateTime delivery = loan.getDeliveryDate();
        LocalDateTime deadline = loan.getDeadline();

        // Se obtiene la entidad tool asociada al prestamo
        ToolEntity tool = loan.getTools();

        // Se obtiene el catalogo asociado al prestamo
        ToolCatalogEntity catalog = tool.getTool_catalogs();

        // Determina el nivel de daño de la herramienta
        DamageLevel damageLevel;
        if (damageLevelStr == null || damageLevelStr.isBlank()) {
            // En caso de no enviar nada, se asume daño --> NINGUNO
            damageLevel = DamageLevel.NINGUNO;
        } else {
            try {
                damageLevel = DamageLevel.valueOf(damageLevelStr.trim().toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(
                        "Nivel de daño inválido: " + damageLevelStr + ". Valores permitidos: NINGUNO, LEVE, GRAVE."
                );
            }
        }

        // Esta variable es true cuando la fecha actual (devolucion) es posterior a la deadline
        boolean late = now.isAfter(deadline);

        // En esta seccion se comprueba si es necesario el uso de la entidad de tarifas
        PenaltyConfigEntity config = null;

        boolean needsConfig = late || damageLevel == DamageLevel.LEVE;

        if (needsConfig) {
            config = penaltyConfigRepository.findTopByOrderByPenaltyConfigIdDesc()
                    .orElseThrow(() -> new RuntimeException("No existe configuración de multas."));
        }

        // --- Calculo del monto de arriendo (rentalAmount) ---

        // Monto de arriendo
        double rentalAmount;
        // Cantidad de dias que duro el prestamo sin contar atraso
        long rentalDays;
        // Se obtiene el valor de arriendo diario de la herramienta desde el catalogo
        double rentalValue = catalog.getDailyRentalValue();

        // Logica para obtencion del monto de arriendo
        if (!late){
            // Monto de arriendo sin atraso
            rentalDays = ChronoUnit.DAYS.between(delivery, now);
            if (rentalDays < 1) rentalDays = 1; // Cobro minimo de 1 dia
            rentalAmount = rentalValue * rentalDays;

            // Se agrega un monto de multa por daño en caso de ser necesario
            switch (damageLevel) {
                case NINGUNO -> {
                    rentalAmount += 0.0;

                    // Actualizacion de estados
                    tool.setCurrentToolState(ToolService.ToolStatus.DISPONIBLE);
                    catalog.setAvailableUnits(catalog.getAvailableUnits() + 1);
                }

                case LEVE -> {
                    double repairCharge = config.getRepairCharge();
                    rentalAmount += repairCharge;
                    PenaltyEntity penalty = PenaltyEntity.builder()
                            .amount(repairCharge)
                            .reason("Daños leves")
                            .delayDays(0)
                            .dailyFineRate(null)
                            .repairCharge(repairCharge)
                            .penaltyStatus(PenaltyService.PaymentStatus.IMPAGO)
                            .loan(loan)
                            .build();
                    penaltyRepository.save(penalty);

                    // Actualizacion de estado
                    tool.setCurrentToolState(ToolService.ToolStatus.EN_REPARACION);
                }

                case GRAVE -> {
                    double replacementValue = catalog.getReplacementValue();
                    rentalAmount += replacementValue;
                    PenaltyEntity penalty = PenaltyEntity.builder()
                            .amount(replacementValue)
                            .reason("Daños graves")
                            .delayDays(0)
                            .dailyFineRate(null)
                            .repairCharge(null)
                            .penaltyStatus(PenaltyService.PaymentStatus.IMPAGO)
                            .loan(loan)
                            .build();
                    penaltyRepository.save(penalty);

                    // Actualizacion de estado
                    tool.setCurrentToolState(ToolService.ToolStatus.EN_REPARACION);
                }
            }
        } else {
            // Monto de arriendo con atraso
            rentalDays = ChronoUnit.DAYS.between(delivery, deadline);
            if (rentalDays < 1) rentalDays = 1; // Cobro minimo de 1 dia
            rentalAmount = rentalValue * rentalDays;

            // Se obtiene la cantidad de dias atrasos
            int delayDays = (int) ChronoUnit.DAYS.between(deadline, now);
            if (delayDays < 1) delayDays = 1; // Cobro minimo de 1 dia para multa

            // Se obtiene el monto de la multa por atraso
            double dailyFineRate = config.getDailyFineRate();
            double penaltyAmount = delayDays * dailyFineRate;

            switch (damageLevel) {
                case NINGUNO -> {
                    rentalAmount += penaltyAmount;
                    PenaltyEntity penalty = PenaltyEntity.builder()
                            .amount(penaltyAmount)
                            .reason("Atraso")
                            .delayDays(delayDays)
                            .dailyFineRate(dailyFineRate)
                            .repairCharge(null)
                            .penaltyStatus(PenaltyService.PaymentStatus.IMPAGO)
                            .loan(loan)
                            .build();
                    penaltyRepository.save(penalty);

                    // Actualizacion de estados
                    tool.setCurrentToolState(ToolService.ToolStatus.DISPONIBLE);
                    catalog.setAvailableUnits(catalog.getAvailableUnits() + 1);
                }

                case LEVE -> {
                    double repairCharge = config.getRepairCharge();
                    penaltyAmount += repairCharge;
                    rentalAmount += penaltyAmount;
                    PenaltyEntity penalty = PenaltyEntity.builder()
                            .amount(penaltyAmount)
                            .reason("Atraso + daño leve")
                            .delayDays(delayDays)
                            .dailyFineRate(dailyFineRate)
                            .repairCharge(repairCharge)
                            .penaltyStatus(PenaltyService.PaymentStatus.IMPAGO)
                            .loan(loan)
                            .build();
                    penaltyRepository.save(penalty);

                    // Actualizacion de estado
                    tool.setCurrentToolState(ToolService.ToolStatus.EN_REPARACION);
                }

                case GRAVE -> {
                    double replacementValue = catalog.getReplacementValue();
                    penaltyAmount += replacementValue;
                    rentalAmount += penaltyAmount;
                    PenaltyEntity penalty = PenaltyEntity.builder()
                            .amount(penaltyAmount)
                            .reason("Atraso + daño grave")
                            .delayDays(delayDays)
                            .dailyFineRate(dailyFineRate)
                            .repairCharge(null)
                            .penaltyStatus(PenaltyService.PaymentStatus.IMPAGO)
                            .loan(loan)
                            .build();
                    penaltyRepository.save(penalty);

                    // Actualizacion de estado
                    tool.setCurrentToolState(ToolService.ToolStatus.EN_REPARACION);
                }
            }
        }

        // Actualizacion del prestamo en la base de datos
        loan.setReturnDate(now);
        loan.setLoanStatus(LoanStatus.POR_PAGAR);
        loan.setRentalAmount(rentalAmount);

        // Actualizacion de herramienta y catalogo
        toolRepository.save(tool);
        toolCatalogRepository.save(catalog);

        return loanRepository.save(loan);
    }

    public void deleteLoansById(Long id) {
        if (!loanRepository.existsById(id)) {
            throw new IllegalArgumentException("No existe préstamo con id: " + id);
        }
        loanRepository.deleteById(id);
    }
}
