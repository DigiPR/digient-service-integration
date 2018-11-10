/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.payment.api.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import rocks.process.integration.payment.api.model.OrderMessage;
import rocks.process.integration.payment.business.domain.Amount;
import rocks.process.integration.payment.business.domain.Discount;
import rocks.process.integration.payment.business.service.PaymentService;
import rocks.process.integration.payment.data.domain.Transaction;

import javax.validation.ConstraintViolationException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class PaymentEndpoint {
    @Autowired
    private PaymentService paymentService;

    @DeleteMapping(path = "/payment/{transactionId}")
    public ResponseEntity<Void> deleteCanceledPayment(@PathVariable("transactionId") Long transactionId) {
        try {
            paymentService.deleteCanceledPayment(transactionId);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().build();
    }

    @GetMapping(path = "/payment/{transactionId}", produces = "application/json")
    public Transaction getPayment(@PathVariable("transactionId") Long transactionId) {
        return paymentService.retrievePayment(transactionId);
    }

    @GetMapping(path = "/payment", produces = "application/json")
    public List<Transaction> listPayments(@RequestParam(value = "customerId", required = true) String customerId) {
        return paymentService.listPayments(customerId);
    }

    @PostMapping(path = "/payment", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OrderMessage> requestPayment(@RequestBody OrderMessage orderMessage) {
        Transaction transaction = null;

        try {
            transaction = paymentService.processPayment(Long.parseLong(orderMessage.getCustomerId()), orderMessage.getOrderId(), new Amount(orderMessage.getAmount()), orderMessage.getNumberOfItems());
        } catch (ConstraintViolationException e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getConstraintViolations().iterator().next().getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }

        orderMessage.setTransactionId(String.valueOf(transaction.getTransactionId()));
        orderMessage.setStatus("Payment Received");

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{transactionId}")
                .buildAndExpand(transaction.getTransactionId()).toUri();

        return ResponseEntity.created(location).body(orderMessage);
    }

    @PutMapping(path = "/payment/{transactionId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Transaction> updateCanceledPayment(@PathVariable("transactionId") Long transactionId, @RequestBody Transaction transaction) {
        try {
            transaction = paymentService.updateCanceledPayment(transactionId, Long.parseLong(transaction.getCardTransactionId()));
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, e.getMessage());
        }
        return ResponseEntity.accepted().body(transaction);
    }

    @GetMapping(path = "/discount", produces = "application/json")
    public Discount calculateDiscount(@RequestParam(value = "customerId", required = true) String customerId, @RequestParam(value = "amount", required = true) String amount, @RequestParam(value = "numberOfItems", required = true) String numberOfItems) {
        return paymentService.calculateDiscount(Long.valueOf(customerId), new Amount(Double.parseDouble(amount)), Integer.valueOf(numberOfItems));
    }
}
