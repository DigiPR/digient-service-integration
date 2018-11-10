/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.eshop.business.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import rocks.process.integration.eshop.business.message.OrderMessage;

@Component
public class CheckoutAdapter {

    @Autowired
    private RestTemplate restTemplate;

    public void doCheckout(OrderMessage orderMessage){

        restTemplate.postForObject("http://localhost:8080/camel/order", new HttpEntity<>(orderMessage), OrderMessage.class);

    }
}
