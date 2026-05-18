package com.payment.main.state;

public interface State {
    void handleRequest( Task task );
}
