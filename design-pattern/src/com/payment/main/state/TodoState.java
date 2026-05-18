package com.payment.main.state;

public class TodoState implements State{
    @Override
    public void handleRequest( Task task) {
        System.out.println("Task to o");
        task.setState( new InProcessState());
    }
}
