/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.shipment.stream.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.process.integration.shipment.business.domain.Tracking;
import rocks.process.integration.shipment.business.service.ShipmentService;
import rocks.process.integration.shipment.stream.message.EventMessage;
import rocks.process.integration.shipment.stream.message.OrderMessage;

@Component
@EnableBinding(Sink.class)
public class MessageEventListener {

    @Autowired
    private ShipmentService shipmentService;

    private static Logger logger = LoggerFactory.getLogger(MessageEventListener.class);

    @StreamListener(target = Sink.INPUT,
            condition = "headers['type']=='ShipGoods'")
    @Transactional
    public void payment(@Payload EventMessage<OrderMessage> eventMessage) throws Exception {
        OrderMessage orderMessage = eventMessage.getPayload();
        logger.info("Payload received: " + orderMessage.toString());
        Tracking tracking = shipmentService.shipGoods(Long.valueOf(orderMessage.getCustomerId()), orderMessage.getOrderId(), orderMessage.getPackingSlipId());
        orderMessage.setTrackingId(tracking.getTrackingId());
        orderMessage.setStatus("GoodsShipped");
        logger.info(orderMessage.toString());
    }

    @StreamListener(target = Sink.INPUT)
    public void defaultListener() {}
}