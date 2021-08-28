package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class CreateDatabase {
    private final Connection conn;

    public CreateDatabase() throws SQLException {
        Properties properties = new Properties();
        String fileName = "server.config";
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            properties.load(fileInputStream);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(properties.getProperty("server.database_host"));
        System.out.println(properties.getProperty("server.database_name"));
        System.out.println(properties.getProperty("server.database_user"));
        System.out.println(properties.getProperty("server.database_password"));

        String conncetionString = "jdbc:postgresql://" + properties.getProperty("server.database_host") + "/" + properties.getProperty("server.database_name");

        conn = DriverManager.getConnection(conncetionString,
                properties.getProperty("server.database_user"),
                properties.getProperty("server.database_password"));


    }

    public void createDatabase(){
        String sql = "CREATE TABLE questions2 " +
                "(id INTEGER not NULL, " +
                " question VARCHAR(255), " +
                " answer boolean, " +
                " PRIMARY KEY ( id ))";

        try (
                //conn;
                Statement stmt = conn.createStatement();
        ) {
            stmt.executeUpdate(sql);
            System.out.println("Created table in given database...");
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }


    public void fillTable(){
        String INSER_DATA[] = {
                "INSERT INTO questions2 (id, question, answer) VALUES (1, \'The earth is the fourth planet from the sun.\', \'false\')",
                "INSERT INTO questions2 (id, question, answer) VALUES (2, \'There are three rivers in Saudi Arabia.\', \'false\')",
                "INSERT INTO questions2 (id, question, answer) VALUES (3, \'The Great Wall of China is visible from space.\', \'false\')",
                "INSERT INTO questions2 (id, question, answer) VALUES (3, \'The Great Wall of China is visible from space.\', \'false\')",
                "INSERT INTO questions2 (id, question, answer) VALUES (5, \'M&M stands for Mars and Moordale.\', \'false\')"
        };

        for(int i = 0 ; i < INSER_DATA.length ; i++){
            System.out.println(INSER_DATA[i]);
            try (
                    //conn;
                    Statement stmt = conn.createStatement();
                    ResultSet rs = stmt.executeQuery(INSER_DATA[i])) {
                System.out.println("Record added");
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }


    }
}
