package com.example.iso8583.controller;

import java.util.List;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.iso8583.model.TransactionRequest;
import com.example.iso8583.service.ISO8583Service;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/iso8583")
@RequiredArgsConstructor
public class ISO8583Controller {

    private final ISO8583Service iso8583Service;

    // Endpoint untuk mendapatkan semua transaksi ISO 8583
    @GetMapping("/transactions")
    public ResponseEntity<List<ISOMsg>> getAllTransactions() {
        List<ISOMsg> transactions = iso8583Service.getAllISOTransactions();
        return ResponseEntity.ok(transactions);
    }

    // Endpoint untuk membuat dan mengirim pesan ISO 8583
    @PostMapping("/send")
    public ResponseEntity<String> sendISOMessage(@RequestBody TransactionRequest request) {
        try {
            ISOMsg isoMsg = iso8583Service.createISOMessage(request);
            iso8583Service.sendISOMessage(isoMsg);
            return ResponseEntity.ok("ISO8583 Message Sent Successfully!");
        } catch (ISOException e) {
            return ResponseEntity.status(500).body("Failed to create ISO8583 message: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send ISO8583 message: " + e.getMessage());
        }
    }

    // Endpoint untuk mendapatkan pesan ISO 8583 berdasarkan STAN
    @GetMapping("/message/{stan}")
    public ResponseEntity<String> getISOMessage(@PathVariable String stan) {
        try {
            ISOMsg isoMsg = iso8583Service.getISOMessage(stan);
            if (isoMsg != null) {
                return ResponseEntity.ok(isoMsg.toString());
            } else {
                return ResponseEntity.status(404).body("ISO8583 message not found for STAN: " + stan);
            }
        } catch (ISOException e) {
            return ResponseEntity.status(500).body("Error retrieving ISO8583 message: " + e.getMessage());
        }
    }
}