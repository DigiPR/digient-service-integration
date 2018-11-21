/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.eshop.stream.sender;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import rocks.process.integration.eshop.stream.message.EventMessage;
import rocks.process.integration.eshop.stream.message.OrderMessage;

@Component
@EnableBinding(Source.class)
public class MessageEventSender {

    @Autowired
    @Output(Source.OUTPUT)
    private MessageChannel messageChannel;

    public void send(EventMessage<OrderMessage> eventMessage) {
        messageChannel.send(MessageBuilder.withPayload(eventMessage).setHeader("type", eventMessage.getType()).build());
    }
}
