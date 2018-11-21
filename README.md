# Service Integration

- Spring REST / Web example
- Apache CXF JAX-RS example
- Apache Camel Routes (REST-to-REST)

Links:
- Camel & Hawt.io [http://localhost:8080/actuator/hawtio](http://localhost:8180/actuator/hawtio)
- eShop Checkout: [http://localhost:8086/checkout](http://localhost:8086/checkout)

## Orchestration
- Camunda Cockpit: http://localhost:8080
- Process start: http://localhost:8080/checkout

Camunda REST API `localhost:8080/rest/process-definition/key/order-fulfillment-orchestration/start` `POST`:

```JSON
{
  "variables": {
    "amount": {
      "value": 2000
    },
    "orderId": {
      "value": "b4775923-dd6f-4b9a-9417-77b529e187c8"
    },
    "numberOfItems": {
      "value": 15
    },
    "customerId": {
      "value": "1"
    },
    "items": {
      "value": [
        {
          "itemId": "1",
          "productId": "2ac90e68-b950-4bc7-ad14-5e6d5563d786",
          "quantity": 15
        },
        {
          "itemId": "2",
          "productId": "dd-b950-4bc7-ad14-5e6d5563d786",
          "quantity": 17
        }
      ]
    },
    "status": {
      "value": "Order Placed"
    }
  }
}
```

## Processing:
- http://localhost:8086/checkout
- https://api.cloudamqp.com/console