/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.payment.business.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rocks.process.integration.payment.business.client.CustomerServiceClient;
import rocks.process.integration.payment.business.client.PaymentServiceProviderClient;
import rocks.process.integration.payment.business.domain.Amount;
import rocks.process.integration.payment.business.domain.Customer;
import rocks.process.integration.payment.business.domain.Discount;
import rocks.process.integration.payment.data.domain.Transaction;
import rocks.process.integration.payment.data.repository.TransactionRepository;

@Service
public class PaymentService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private CustomerServiceClient customerService;
    @Autowired
    private PaymentServiceProviderClient psp;

    public Transaction processPayment(Long customerId, String reference, Amount amount, Integer numberOfItems) throws Exception {
        Customer customer = customerService.retrieveCustomerById(customerId);
        Transaction transaction = new Transaction(customer, reference, amount, numberOfItems);
        Discount discount = calculateDiscount(customerId, amount, numberOfItems);
        transaction.setDiscount(discount.getFactor());
        Amount discountedAmount = applyDiscount(amount, discount);
        Amount remainingAmount = deductLoyaltyPoints(customer, discountedAmount);
        transaction.setChargingAmount(remainingAmount.getValue());
        if (remainingAmount.getValue() > 0) {
            try {
                transaction.setCardTransactionId(chargeCreditCard(customer, remainingAmount, transaction).getCardTransactionId());
                transaction.setCanceled(false);
            } catch (Exception e) {
                restoreLoyaltyPoints(customer, new Amount(discountedAmount.getValue() - remainingAmount.getValue()));
                transaction.setCanceled(true);
                transactionRepository.save(transaction);
                throw new Exception("Credit card transaction failed due to " + e.getMessage() + ".");
            }
        }
        transactionRepository.save(transaction);
        return transaction;
    }

    public Discount calculateDiscount(Long customerId, Amount amount, Integer numberOfItems) {
        Customer customer = customerService.retrieveCustomerById(customerId);
        if (amount.getValue() >= 1000 && customer.getType().equals("Business")) {
            if (numberOfItems >= 10) {
                return new Discount(0.15);
            } else {
                return new Discount(0.10);
            }
        }
        if (amount.getValue() >= 500 && customer.getType().equals("Private")) {
            return new Discount(0.05);
        }
        return new Discount(0.00);
    }

    private Amount applyDiscount(Amount amount, Discount discount) {
        return new Amount(amount.getValue() * (1 - discount.getFactor()));
    }

    private Amount deductLoyaltyPoints(Customer customer, Amount amount) {
        Integer loyaltyMoney = customer.getLoyaltyPoints() / 1000;
        if (loyaltyMoney >= amount.getValue()) {
            customer.setLoyaltyPoints((int) ((loyaltyMoney - amount.getValue())*1000));
            customerService.editLoyaltyBalance(customer);
            return new Amount((double) 0);
        }
        customer.setLoyaltyPoints(0);
        customerService.editLoyaltyBalance(customer);
        return new Amount(amount.getValue() - loyaltyMoney);
    }

    private Transaction chargeCreditCard(Customer customer, Amount amount, Transaction transaction) {
        transaction.setCardTransactionId(psp.chargeCreditCard(amount, customer.getCardNumber(), customer.getSecurityCode()).getTransactionId());
        return transaction;
    }

    private void restoreLoyaltyPoints(Customer customer, Amount amount) {
        customer.setLoyaltyPoints((int) (amount.getValue()*1000));
        customerService.editLoyaltyBalance(customer);
    }

}
