/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.shipment.business.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import rocks.process.integration.shipment.business.domain.Tracking;

import java.util.UUID;

@Service
public class ShipmentService {

    private Logger logger = LoggerFactory.getLogger(ShipmentService.class);

    public Tracking shipGoods(Long customerId, String reference, String packingSlipId) throws Exception {
        logger.info("shipGoods() with customerId " + customerId + " and reference " + reference + " called and hand over the parcel to the delivery service");
        for(long seconds = 5; seconds > 0; seconds--) {
            logger.info("Delivery service ready in " + seconds + " seconds");
            Thread.sleep(1000);
        }
        // ...
        Tracking tracking = new Tracking(UUID.randomUUID().toString());
        logger.info("Packet transferred to delivery service and tracking number "+ tracking.getTrackingId()+ " received");
        return tracking;
    }
}