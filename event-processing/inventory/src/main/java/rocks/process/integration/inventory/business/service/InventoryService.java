/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.inventory.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rocks.process.integration.inventory.business.domain.OrderItem;
import rocks.process.integration.inventory.business.domain.PackingSlip;

import java.util.List;
import java.util.UUID;

@Service
public class InventoryService {

    private Logger logger = LoggerFactory.getLogger(InventoryService.class);

    public PackingSlip fetchGoods(Long customerId, String reference, List<OrderItem> items) throws Exception {
        logger.info("fetchGoods() with customerId " + customerId + " and reference " + reference + " called and going to pick "+ items.size() +" items in the inventory");
        for(long seconds = items.size(); seconds > 0; seconds--) {
            logger.info(seconds + " items remaining");
            Thread.sleep(1000);
        }
        // ...
        PackingSlip packingSlip = new PackingSlip(UUID.randomUUID().toString());
        logger.info("Packing slip generated with packing slip id: " + packingSlip.getPackingSlipId());
        return packingSlip;
    }
}