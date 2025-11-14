package com.example.demo.DTOs;

import lombok.Data;

@Data

/*
    * El proposito de este DTO es darle al forma al
    * JSON que sera solicitado al momento de activar
    * el PATCH de devolucion de herramienta. El JSON
    * debera contener una string llamado "damageLevel"
    * que se encarga de indicar cual es nivel de daño
    * con el cual, la herramienta fue devuelta, y asi,
    * poder calcular el monto de la multa.
 */
public class ReturnLoanRequestDTO {
    private String damageLevel; // indica el nivel de daño de la herramienta
}
