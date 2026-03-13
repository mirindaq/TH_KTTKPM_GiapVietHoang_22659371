package com.payment.payment;

public class MomoPayment implements Payment {

    @Override
    public void pay(double amount) {
        System.out.println("Thanh toan " + amount + " bang Momo");
    }

}