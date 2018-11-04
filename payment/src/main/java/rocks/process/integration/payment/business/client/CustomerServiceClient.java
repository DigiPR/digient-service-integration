/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.payment.business.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rocks.process.integration.payment.api.model.OrderMessage;
import rocks.process.integration.payment.business.domain.Customer;


@Component
public class CustomerServiceClient {

    @Autowired
    private RestTemplate restTemplate;

    public Customer retrieveCustomerById(Long customerId) {
        return restTemplate.getForObject("http://localhost:8080/customer/"  + customerId, Customer.class);
    }

    public void editLoyaltyBalance(Customer customer) {
        restTemplate.put("http://localhost:8080/loyalty/" + customer.getCustomerId(), new HttpEntity<>(customer), OrderMessage.class);
    }
}
