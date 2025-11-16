package com.example.demo.DTOs;

import lombok.Data;

@Data
public class PaymentRequestDTO {
    private Double amountPaid;
    private String paymentMethod;
}
