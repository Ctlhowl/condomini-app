package com.ctlfab.condomini.enumeration;

public enum PaymentMethod {
    CREDIT_CARD("Carta di credito"),
    MONEY("Contanti");

    private String paymentMethod;

    PaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }
}
