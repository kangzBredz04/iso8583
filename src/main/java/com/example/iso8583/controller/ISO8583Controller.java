package com.example.iso8583.controller;

import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.iso8583.model.TransactionRequest;
import com.example.iso8583.service.ISO8583Service;

@RestController
@RequestMapping("/api/iso8583")
public class ISO8583Controller {

    @Autowired
    private ISO8583Service iso8583Service;

    // Endpoint untuk membuat dan mengirim pesan ISO 8583
    @PostMapping("/send")
    public ResponseEntity<String> sendISOMessage(@RequestBody TransactionRequest request) {
        try {
            ISOMsg isoMsg = iso8583Service.createISOMessage(request);
            iso8583Service.sendISOMessage(isoMsg);
            return ResponseEntity.ok("ISO8583 Message Sent Successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send ISO8583 message: " + e.getMessage());
        }
    }

    // Endpoint untuk mendapatkan pesan ISO 8583 berdasarkan STAN
    @GetMapping("/get/{stan}")
    public ResponseEntity<String> getISOMessage(@PathVariable String stan) {
        try {
            ISOMsg isoMsg = iso8583Service.getISOMessage(stan);
            if (isoMsg == null) {
                return ResponseEntity.status(404).body("ISO8583 Message not found for STAN: " + stan);
            }
            return ResponseEntity.ok(isoMsg.toString()); // Mengembalikan pesan ISO 8583 sebagai string
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to retrieve ISO8583 message: " + e.getMessage());
        }
    }
}