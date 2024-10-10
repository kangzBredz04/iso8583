package com.example.iso8583.service;

import java.io.OutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.jpos.iso.ISOException;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.packager.ISO87APackager;
import org.springframework.stereotype.Service;

import com.example.iso8583.model.TransactionRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ISO8583Service {

    private Map<String, ISOMsg> isoMessageStore = new HashMap<>();

    // Metode untuk membuat pesan ISO 8583 dari request body
    public ISOMsg createISOMessage(TransactionRequest transactionRequest) throws ISOException {
        ISOMsg isoMsg = new ISOMsg();
        isoMsg.setPackager(new ISO87APackager());

        // Mengambil waktu transaksi saat ini dalam format MMDDhhmm
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMddHHmm");
        String transactionTime = LocalDateTime.now().format(formatter);

        isoMsg.setMTI("0200");
        isoMsg.set(2, transactionRequest.getPan());
        isoMsg.set(4, transactionRequest.getAmount());
        isoMsg.set(7, transactionTime);
        isoMsg.set(11, transactionRequest.getStan());
        isoMsg.set(32, transactionRequest.getInstitutionCode());
        isoMsg.set(49, transactionRequest.getCurrencyCode());

        // Menyimpan pesan ISO
        isoMessageStore.put(transactionRequest.getStan(), isoMsg);

        log.info("ISO8583 Message Created: {}", isoMsg.toString());
        return isoMsg;
    }

    // Implementasi pengiriman pesan ISO 8583 menggunakan Socket
    public void sendISOMessage(ISOMsg isoMsg) throws Exception {
        // Simulasi koneksi socket ke server ISO 8583
        String serverHost = "127.0.0.1"; // ganti dengan server tujuan
        int serverPort = 12345; // ganti dengan port yang sesuai

        try (Socket socket = new Socket(serverHost, serverPort)) {
            OutputStream outputStream = socket.getOutputStream();

            // Pack ISO message menjadi byte array dan kirim ke server
            byte[] isoMessageBytes = isoMsg.pack();
            outputStream.write(isoMessageBytes);
            outputStream.flush();

            log.info("ISO8583 Message Sent to {}:{}", serverHost, serverPort);
        } catch (Exception e) {
            log.error("Error while sending ISO8583 message: {}", e.getMessage());
            throw new Exception("Failed to send ISO8583 message", e);
        }
    }

    // Metode untuk mengambil pesan ISO 8583 berdasarkan STAN
    public ISOMsg getISOMessage(String stan) throws ISOException {
        return isoMessageStore.get(stan);
    }
}