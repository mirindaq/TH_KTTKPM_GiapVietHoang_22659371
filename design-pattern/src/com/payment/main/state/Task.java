package com.payment.main.state;

public class Task {
    private State state;

    public Task(){
        this.state = new TodoState();
    }

    public void setState(State state) {
        this.state = state;
    }

    public void handleRequest() {
        state.handleRequest(this);
    }
}
