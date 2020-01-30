package com.company;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        GameServer gs = new GameServer();
        gs.acceptConnection();
    }
}
