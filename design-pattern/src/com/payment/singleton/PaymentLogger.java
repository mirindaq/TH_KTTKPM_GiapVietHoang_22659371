package com.payment.singleton;

public class PaymentLogger {

    private PaymentLogger() {
    }

    private static class SingletonHelper {
        private static final PaymentLogger INSTANCE = new PaymentLogger();
    }

    public static PaymentLogger getInstance() {
        return SingletonHelper.INSTANCE;
    }

    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}