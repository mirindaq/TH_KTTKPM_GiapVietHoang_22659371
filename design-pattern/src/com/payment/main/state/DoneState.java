package com.payment.main.state;

public class DoneState implements State{
    @Override
    public void handleRequest( Task task) {
        System.out.println("Task xong");
    }
}
