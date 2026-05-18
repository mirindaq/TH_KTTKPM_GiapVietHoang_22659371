package com.payment.main.state;

public class InProcessState implements State{
    @Override
    public void handleRequest( Task task) {
        System.out.println("Task lam");
        task.setState( new DoneState());
    }
}
