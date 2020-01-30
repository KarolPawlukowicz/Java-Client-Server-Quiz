package com.company;

public class Main {

    public static void main(String[] args) {
        Player p = new Player();
        p.connectoToServer();
        p.setGUI();
        p.setUpButtons();
    }
}
