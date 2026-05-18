package com.payment.main.observer;


public class Main {
    public static void main(String[] args) {

        Task task = new Task("Build Login API", "TODO");

        TeamMember a = new TeamMember("Lan");
        TeamMember b = new TeamMember("Minh");
        TeamMember c = new TeamMember("Tuan");

        task.attach(a);
        task.attach(b);
        task.attach(c);

        task.setStatus("IN PROGRESS");
        task.setStatus("DONE");
    }
}