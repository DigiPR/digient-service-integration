/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.crm.api.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rocks.process.integration.crm.api.CustomerAPI;
import rocks.process.integration.crm.api.model.Customer;

import java.util.UUID;

@Component
public class CustomerAPIImpl implements CustomerAPI {

    private Logger logger = LoggerFactory.getLogger(CustomerAPIImpl.class);

    @Override
    public Customer findCustomer(String customerId) {
        return new Customer(1L, "Business", 20000, UUID.randomUUID().toString(), UUID.randomUUID().toString());
    }

    @Override
    public void editLoyaltyBalance(String customerId, Customer customer) {
        logger.info("customerId: " + customerId + ". points " + customer.getLoyaltyPoints());
    }
}
