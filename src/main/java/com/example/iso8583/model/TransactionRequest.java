package com.example.iso8583.model;

import lombok.Data;

@Data
public class TransactionRequest {
    private String pan;
    private String amount;
    private String stan;
    private String institutionCode;
    private String currencyCode;
}