/*
 * Copyright (c) 2018. University of Applied Sciences and Arts Northwestern Switzerland FHNW.
 * All rights reserved.
 */

package rocks.process.integration.crm.api.model;

public class Customer {
    private Long customerId;
    private String type;
    private Integer loyaltyPoints;
    private String cardNumber;
    private String securityCode;

    public Customer() {
    }

    public Customer(Long customerId, String type, Integer loyaltyPoints, String cardNumber, String securityCode) {
        this.customerId = customerId;
        this.type = type;
        this.loyaltyPoints = loyaltyPoints;
        this.cardNumber = cardNumber;
        this.securityCode = securityCode;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLoyaltyPoints() {
        return loyaltyPoints;
    }

    public void setLoyaltyPoints(Integer loyaltyPoints) {
        this.loyaltyPoints = loyaltyPoints;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }
}
