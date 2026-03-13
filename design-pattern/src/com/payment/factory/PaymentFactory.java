package com.payment.factory;

import com.payment.payment.*;

public class PaymentFactory {

    public static Payment createPayment(String type) {

        if (type.equalsIgnoreCase("creditcard")) {
            return new CreditCardPayment();
        }

        if (type.equalsIgnoreCase("paypal")) {
            return new PaypalPayment();
        }

        if (type.equalsIgnoreCase("momo")) {
            return new MomoPayment();
        }

        throw new IllegalArgumentException("Loai thanh toan khong hop le");
    }

}