package com.payment.main;

import com.payment.factory.PaymentFactory;
import com.payment.payment.Payment;
import com.payment.singleton.PaymentLogger;

public class Main {

    public static void main(String[] args) {

        Payment payment1 = PaymentFactory.createPayment("creditcard");
        payment1.pay(100);

        Payment payment2 = PaymentFactory.createPayment("paypal");
        payment2.pay(200);

        Payment payment3 = PaymentFactory.createPayment("momo");
        payment3.pay(300);

        PaymentLogger logger = PaymentLogger.getInstance();

        logger.log("CreditCard payment completed");
        logger.log("Paypal payment completed");
        logger.log("Momo payment completed");
    }

}