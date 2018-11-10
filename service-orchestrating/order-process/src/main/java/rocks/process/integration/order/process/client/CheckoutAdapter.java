/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.order.process.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rocks.process.integration.order.process.message.OrderMessage;

import java.util.Map;

@Component
public class CheckoutAdapter {

    @Autowired
    private RuntimeService runtimeService;

    public void doCheckout(OrderMessage orderMessage){
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> map = objectMapper.convertValue(orderMessage, new TypeReference<Map<String, Object>>() {});
        runtimeService.startProcessInstanceByKey("order-fulfillment-orchestration", map);
    }
}
