package com.example.iso8583.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.stereotype.Service;

import com.example.iso8583.model.TransactionRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ISO8583Service {

    private final Map<String, ISOMsg> isoMessageStore = new HashMap<>();

    // Metode untuk mendapatkan semua data transaksi
    public List<ISOMsg> getAllISOTransactions() {
        return isoMessageStore.values().stream().collect(Collectors.toList());
    }

    // Membuat pesan ISO 8583 dari request body
    public ISOMsg createISOMessage(TransactionRequest transactionRequest) throws ISOException {
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(new ISO87APackager());

        // Mengambil waktu transaksi saat ini dalam format MMDDhhmmss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmmss");
        String transactionTime = LocalDateTime.now().format(formatter);

        isoMsg.setMTI("0200"); // Message Type Identifier untuk transaksi
        isoMsg.set(2, transactionRequest.getPan()); // PAN (Primary Account Number)
        isoMsg.set(4, transactionRequest.getAmount()); // Amount
        isoMsg.set(7, transactionTime); // Transaction time
        isoMsg.set(11, transactionRequest.getStan()); // STAN (System Trace Audit Number)
        isoMsg.set(32, transactionRequest.getInstitutionCode()); // Institution Code
        isoMsg.set(49, transactionRequest.getCurrencyCode()); // Currency Code

        // Simpan pesan ISO ke HashMap menggunakan STAN sebagai kunci
        isoMessageStore.put(transactionRequest.getStan(), isoMsg);

        log.info("ISO8583 Message Created: {}", isoMsg);
        return isoMsg;
    }

    // Mengirim pesan ISO 8583 (dummy, belum mengirim ke server nyata)
    public void sendISOMessage(ISOMsg isoMsg) throws ISOException {
        // Untuk simulasi, hanya tampilkan pesan yang akan dikirim
        log.info("Sending ISO8583 Message: {}", isoMsg);
    }

    // Mengambil pesan ISO 8583 berdasarkan STAN
    public ISOMsg getISOMessage(String stan) throws ISOException {
        return isoMessageStore.get(stan);
    }
}