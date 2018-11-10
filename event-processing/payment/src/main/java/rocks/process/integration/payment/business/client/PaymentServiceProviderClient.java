/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.payment.business.client;

import org.springframework.stereotype.Component;
import rocks.process.integration.payment.business.domain.Amount;
import rocks.process.integration.payment.business.domain.CardTransaction;

import java.util.UUID;

@Component
public class PaymentServiceProviderClient {
    public CardTransaction chargeCreditCard(Amount amount, String cardNumber, String securityCode) {
        return new CardTransaction(UUID.randomUUID().toString());
    }
}
