package com.project.ecommerce.service;

import com.project.ecommerce.entity.OrderItems;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Async("taskExecutor")
    public void sendOrderConfirmationEmail(String toEmail, String customerName, int orderId){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Order Confirmation");
        message.setText("Hello " + customerName +
                ", your order with ID " +
                orderId +
                " has been confirmed.");

        mailSender.send(message);

        System.out.println("Email sent by thread: "+Thread.currentThread().getName());
    }
}
