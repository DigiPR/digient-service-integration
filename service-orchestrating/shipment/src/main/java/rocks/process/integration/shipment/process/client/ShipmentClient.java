/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.shipment.process.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.backoff.ExponentialBackoffStrategy;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import rocks.process.integration.shipment.business.domain.Tracking;
import rocks.process.integration.shipment.business.service.ShipmentService;
import rocks.process.integration.shipment.process.message.OrderMessage;

import javax.annotation.PostConstruct;
import java.util.Map;

@Component
public class ShipmentClient {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ShipmentService shipmentService;

    @Value("${order-process.url}")
    private String orderProcessUrl;

    private Logger logger = LoggerFactory.getLogger(ShipmentClient.class);

    @PostConstruct
    private void subscribeTopics() {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl(orderProcessUrl)
                .maxTasks(1)
                .backoffStrategy(new ExponentialBackoffStrategy(2L, 1.1F, 5000L))
                .build();

        // Subscribe to the topic with client function
        client.subscribe("ShipGoods")
                .handler((ExternalTask externalTask, ExternalTaskService externalTaskService) -> {
                    try {
                        OrderMessage orderMessage = objectMapper.convertValue(externalTask.getAllVariablesTyped(), OrderMessage.class);

                        Tracking tracking = shipmentService.shipGoods(Long.valueOf(orderMessage.getCustomerId()), orderMessage.getOrderId(), orderMessage.getPackingSlipId());
                        orderMessage.setPackingSlipId(tracking.getTrackingId());
                        orderMessage.setStatus("GoodsShipped");

                        Map<String, Object> map = objectMapper.convertValue(orderMessage, new TypeReference<Map<String, Object>>() {});

                        externalTaskService.complete(externalTask, map);
                    } catch (Exception e) {
                        externalTaskService.handleBpmnError(externalTask, "shipping-failed");
                    }
                })
                .open();
    }
}
