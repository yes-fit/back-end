//package com.gymTracker.GymTracker.Infracstructure.Task;
//
//import com.google.zxing.WriterException;
//import com.gymTracker.GymTracker.Infracstructure.Utils.QRCodeGenerator;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//@Component
//@Slf4j
//
//public class QRCodeScheduler {
//    @Scheduled(cron = "* * * * * *")
//    public void generateHourlyQRCode() {
//
//        String content = "GYM_ACCESS_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH"));
//        String fileName = content + ".png";
//        String qrPath = "C:\\Users\\AbdulBasit\\Desktop\\Coding\\Java Applications\\GymTracker\\src\\main\\resources\\static\\" + fileName;
//        log.info("Attempting to schedule the qr code generator");
//        try {
//            QRCodeGenerator.generateQRcode(content, qrPath, 300, 300);
//            System.out.println("QR Code generated for: " + content);
//        } catch (WriterException | IOException e) {
//            System.err.println("Failed to generate QR code: " + e.getMessage());
//        }
//    }
//
//}
