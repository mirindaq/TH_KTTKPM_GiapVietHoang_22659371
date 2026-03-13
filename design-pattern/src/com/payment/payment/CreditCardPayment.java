package com.payment.payment;

public class CreditCardPayment implements Payment {

    @Override
    public void pay(double amount) {
        System.out.println("Thanh toan " + amount + " bang Credit Card");
    }

}