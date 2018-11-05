/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.camel.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.http4.HttpMethods;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;
import rocks.process.integration.camel.message.Customer;
import rocks.process.integration.camel.message.OrderMessage;

@Component
public class CamelRoutes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("servlet")
                .bindingMode(RestBindingMode.json);

        rest("/order").consumes("application/json").produces("application/json")
                .post("/").type(OrderMessage.class).outType(OrderMessage.class)
                .route().id("Order Fulfillment")
                .to("seda:orderFulfillmentProcess?waitForTaskToComplete=Never") // The seda: component provides asynchronous behavior.
                .endRest();


        from("seda:orderFulfillmentProcess").id("Order Fulfillment Process")
                .to("direct:payment");
                //.to("direct:inventory");
                //.to("direct:shipping");

        from("direct:payment").id("Request Payment")
                .process(new Processor() {
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        OrderMessage orderMessage = (OrderMessage) exchange.getIn().getBody();
                        orderMessage.setStatus("Payment Requested");
                        exchange.getIn().setBody(orderMessage);
                    }
                }).id("Set Payment Requested Status")
                .removeHeaders("CamelHttp*")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader("Accept", constant("application/json"))
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.POST))
                .marshal().json(JsonLibrary.Jackson, OrderMessage.class)
                .convertBodyTo(String.class)
                .to("http4://localhost:8084/api/payment")
                .unmarshal().json(JsonLibrary.Jackson, OrderMessage.class);


        rest("/customer").produces("application/json")
                .get("/{customerId}").outType(Customer.class)
                .route().id("Customer Finder")
                .removeHeaders("CamelHttp*")
                .setHeader("Accept", constant("application/json"))
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.GET))
                .toD("http4://localhost:8082/api/customer/${header.customerId}")
                .unmarshal().json(JsonLibrary.Jackson, Customer.class)
                .endRest();

        rest("/loyalty").consumes("application/json")
                .put("/{customerId}").type(Customer.class)
                .route().id("Loyalty Point Management")
                .removeHeaders("CamelHttp*")
                .setHeader("Content-Type", constant("application/json"))
                .setHeader(Exchange.HTTP_METHOD, constant(HttpMethods.PUT))
                .marshal().json(JsonLibrary.Jackson, Customer.class)
                .convertBodyTo(String.class)
                .toD("http4://localhost:8082/api/loyalty/${header.customerId}")
                .endRest();
    }
}
