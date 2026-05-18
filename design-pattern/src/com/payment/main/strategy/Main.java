package com.payment.main.strategy;

public class Main {

    public static void main(String[] args) {
        Context context = new Context();
        context.setStrategy(new StrategyA());
        context.executeStrategy();

        context.setStrategy(new StrategyB());
        context.executeStrategy();
    }

}
