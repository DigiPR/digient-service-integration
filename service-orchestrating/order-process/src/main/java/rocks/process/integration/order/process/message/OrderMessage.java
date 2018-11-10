/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.order.process.message;

import java.util.List;

public class OrderMessage {
    private String orderId;
    private String customerId;
    private Double amount;
    private Integer numberOfItems;
    private List<OrderItem> items;
    private String transactionId;
    private String trackingId;
    private String packingSlipId;
    private String status;

    public OrderMessage() {
    }

    public OrderMessage(String orderId, String customerId, Double amount, Integer numberOfItems, List<OrderItem> items, String transactionId, String trackingId, String packingSlipId, String status) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.amount = amount;
        this.numberOfItems = numberOfItems;
        this.items = items;
        this.transactionId = transactionId;
        this.trackingId = trackingId;
        this.packingSlipId = packingSlipId;
        this.status = status;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Integer getNumberOfItems() {
        return numberOfItems;
    }

    public void setNumberOfItems(Integer numberOfItems) {
        this.numberOfItems = numberOfItems;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }

    public String getPackingSlipId() {
        return packingSlipId;
    }

    public void setPackingSlipId(String packingSlipId) {
        this.packingSlipId = packingSlipId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "OrderMessage{" +
                "orderId='" + orderId + '\'' +
                ", customerId='" + customerId + '\'' +
                ", amount=" + amount +
                ", numberOfItems=" + numberOfItems +
                ", items=" + items.toString() +
                ", transactionId='" + transactionId + '\'' +
                ", trackingId='" + trackingId + '\'' +
                ", packingSlipId='" + packingSlipId + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    public static class OrderItem {
        private String itemId;
        private String productId;
        private Integer quantity;

        public OrderItem() {
        }

        public OrderItem(String itemId, String productId, Integer quantity) {
            this.itemId = itemId;
            this.productId = productId;
            this.quantity = quantity;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getProductId() {
            return productId;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public Integer getQuantity() {
            return quantity;
        }

        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }

        @Override
        public String toString() {
            return "OrderItem{" +
                    "itemId='" + itemId + '\'' +
                    ", productId='" + productId + '\'' +
                    ", quantity=" + quantity +
                    '}';
        }
    }
}
