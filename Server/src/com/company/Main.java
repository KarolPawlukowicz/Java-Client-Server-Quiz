package com.company;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException, SQLException {
        /*
        Properties properties = new Properties();
        String fileName = "server.config";
        try (FileInputStream fileInputStream = new FileInputStream(fileName)) {
            properties.load(fileInputStream);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(properties.getProperty("server.database_host"));
        System.out.println(properties.getProperty("server.database_name"));
        System.out.println(properties.getProperty("server.database_user"));
        System.out.println(properties.getProperty("server.database_password"));

        String conncetionString = "jdbc:postgresql://" + properties.getProperty("server.database_host") + "/" + properties.getProperty("server.database_name");

        Connection conn = DriverManager.getConnection(conncetionString,
                properties.getProperty("server.database_user"),
                properties.getProperty("server.database_password"));

        List<Question> questions = new ArrayList<>();

        String SQL = "SELECT * FROM questions";
        try (
                conn;
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(SQL)) {
            while(resultSet.next()){
               // System.out.println(resultSet.getString(2));
               // System.out.println(resultSet.getBoolean(3));
                questions.add(new Question(resultSet.getString(2), resultSet.getBoolean(3)));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

        Collections.shuffle(questions);
        System.out.println("--");
        for (Question q : questions) {
            System.out.println(" " + q.getQuestion());
        }*/



       /* Set<Integer> questionNumbers = new HashSet<>();

        for(int i = 1 ; i <= 10 ; i++){
            questionNumbers.add(i);
        }

        for(int number: questionNumbers) {
            System.out.println(number);
        }*/



        /*
        String url = "jdbc:postgresql://localhost/Quiz";
        String user = "postgres";
        String password = "postgres";

        String SQL = "SELECT * FROM questions";
       // SQL = "INSERT INTO questions (id, question, answer) VALUES (1, \'{The earth is the fourth planet from the sun.}\', \'false\')";
       // SQL = "INSERT INTO questions (id, question, answer) VALUES (2, \'{There are three rivers in Saudi Arabia.}\', \'false\')";
       // SQL = "INSERT INTO questions (id, question, answer) VALUES (3, \'{The Great Wall of China is visible from space.}\', \'false\')";
       // SQL = "INSERT INTO questions (id, question, answer) VALUES (4, \'{There are more ancient pyramids in Sudan than in Egypt.}\', \'true\')";
       // SQL = "INSERT INTO questions (id, question, answer) VALUES (5, \'{M&M stands for Mars and Moordale\n.}\', \'false\')";


        Connection conn = DriverManager.getConnection(url, user, password);

        try (
                conn;
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SQL)) {
            rs.next();
            System.out.println(rs.getInt(1));

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }*/


        GameServer gameServer = new GameServer();
        gameServer.acceptConnection();
    }
}
