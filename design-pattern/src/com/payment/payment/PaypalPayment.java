package com.payment.payment;

public class PaypalPayment implements Payment {

    @Override
    public void pay(double amount) {
        System.out.println("Thanh toan " + amount + " bang PayPal");
    }

}