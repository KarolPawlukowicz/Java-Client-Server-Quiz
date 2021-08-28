package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        /*CreateDatabase createDatabase = new CreateDatabase();
        createDatabase.createDatabase();
        createDatabase.fillTable();*/

        GameServer gameServer = new GameServer();
        gameServer.acceptConnection();
    }
}
