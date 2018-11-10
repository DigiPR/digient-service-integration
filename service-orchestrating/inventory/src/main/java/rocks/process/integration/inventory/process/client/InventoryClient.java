/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.inventory.process.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.backoff.ExponentialBackoffStrategy;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rocks.process.integration.inventory.business.domain.OrderItem;
import rocks.process.integration.inventory.business.domain.PackingSlip;
import rocks.process.integration.inventory.business.service.InventoryService;
import rocks.process.integration.inventory.process.message.OrderMessage;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

@Component
public class InventoryClient {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InventoryService inventoryService;

    private Logger logger = LoggerFactory.getLogger(InventoryClient.class);

    @PostConstruct
    private void subscribeTopics() {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/rest")
                .maxTasks(1)
                .backoffStrategy(new ExponentialBackoffStrategy(2L, 1.1F, 5000L))
                .build();

        // Subscribe to the topic with client function
        client.subscribe("FetchGoods")
                .handler((ExternalTask externalTask, ExternalTaskService externalTaskService) -> {
                    try {
                        OrderMessage orderMessage = objectMapper.convertValue(externalTask.getAllVariablesTyped(), OrderMessage.class);
                        List<OrderItem> orderItems = objectMapper.convertValue(orderMessage.getItems(), new TypeReference<List<OrderItem>>() {});

                        PackingSlip packingSlip = inventoryService.fetchGoods(Long.valueOf(orderMessage.getCustomerId()), orderMessage.getOrderId(), orderItems);
                        orderMessage.setPackingSlipId(packingSlip.getPackingSlipId());
                        orderMessage.setStatus("GoodsFetched");

                        Map<String, Object> map = objectMapper.convertValue(orderMessage, new TypeReference<Map<String, Object>>() {});

                        externalTaskService.complete(externalTask, map);
                    } catch (Exception e) {
                        externalTaskService.handleBpmnError(externalTask, "fetch-goods-failed");
                    }
                })
                .open();
    }
}
