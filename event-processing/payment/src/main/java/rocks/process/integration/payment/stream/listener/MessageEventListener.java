/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.payment.stream.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import rocks.process.integration.payment.business.domain.Amount;
import rocks.process.integration.payment.business.service.PaymentService;
import rocks.process.integration.payment.data.domain.Transaction;
import rocks.process.integration.payment.stream.message.EventMessage;
import rocks.process.integration.payment.stream.message.OrderMessage;

@Component
@EnableBinding({Sink.class, Source.class})
public class MessageEventListener {

    @Autowired
    @Output(Source.OUTPUT)
    private MessageChannel messageChannel;

    @Autowired
    private PaymentService paymentService;

    private static Logger logger = LoggerFactory.getLogger(MessageEventListener.class);

    @StreamListener(target = Sink.INPUT,
            condition="headers['type']=='RequestPayment'")
    @Transactional
    public void payment(@Payload EventMessage<OrderMessage> eventMessage) throws Exception {
        OrderMessage orderMessage = eventMessage.getPayload();
        logger.info("Payload received: "+orderMessage.toString());
        Transaction transaction = paymentService.processPayment(Long.valueOf(orderMessage.getCustomerId()), orderMessage.getOrderId(), new Amount(orderMessage.getAmount()), orderMessage.getNumberOfItems());
        orderMessage.setTransactionId(String.valueOf(transaction.getTransactionId()));
        orderMessage.setStatus("PaymentReceived");
        send(new EventMessage<>("FetchGoods", orderMessage));
    }

    public void send(EventMessage<OrderMessage> eventMessage) {
        messageChannel.send(MessageBuilder.withPayload(eventMessage).setHeader("type", eventMessage.getType()).build());
    }

    @StreamListener(target = Sink.INPUT)
    public void defaultListener() {}
}