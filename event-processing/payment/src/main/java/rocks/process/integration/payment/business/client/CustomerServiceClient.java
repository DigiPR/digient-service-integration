/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.payment.business.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rocks.process.integration.payment.business.domain.Customer;

import java.util.UUID;


@Component
public class CustomerServiceClient {

    private Logger logger = LoggerFactory.getLogger(CustomerServiceClient.class);

    public Customer retrieveCustomerById(Long customerId) {
        return new Customer(1L, "Business", 20000, UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    public void editLoyaltyBalance(Customer customer) {
        logger.info("customerId: " + customer.getCustomerId() + ". points " + customer.getLoyaltyPoints());
    }

}
