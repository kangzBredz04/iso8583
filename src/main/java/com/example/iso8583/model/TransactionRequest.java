package com.example.iso8583.model;

import lombok.Data;

@Data
public class TransactionRequest {
    private String pan; // PAN: Primary Account Number
    private String amount; // Jumlah transaksi
    private String stan; // System Trace Audit Number
    private String institutionCode; // Kode institusi
    private String currencyCode; // Kode mata uang (misal: 840 untuk USD)
}