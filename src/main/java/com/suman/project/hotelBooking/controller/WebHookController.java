package com.suman.project.hotelBooking.controller;

import com.stripe.model.Event;
import com.stripe.net.Webhook;
import com.suman.project.hotelBooking.service.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
@RequiredArgsConstructor
public class WebHookController
{
    private final BookingService bookingService;

    @Value("${stripe.webhook.secret}")
    private String endPointSecret;

    @PostMapping("/payments")
    public ResponseEntity<Void> capturePayment(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader)
    {
        try {
            Event event = Webhook.constructEvent(payload, sigHeader, endPointSecret);

            bookingService.capturePayment(event, payload);

            return ResponseEntity.noContent().build();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
