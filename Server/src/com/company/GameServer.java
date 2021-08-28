package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

public class GameServer {
    private Connection conn;
    private Properties properties;
    private String propertiesFileName;
    private String conncetionString;

    private ServerSocket ss;
    private int numPlayers;

    private ServerSideConnection player1;
    private ServerSideConnection player2;

    private int questionNumber;
    private List <Question> questions;
    private int numberOfQuestions;


    private boolean player1Answer;
    private boolean player2Answer;

    private boolean p1went;
    private boolean p2went;


    public GameServer() throws IOException, SQLException {
        System.out.println("-- Server --");
        properties = new Properties();
        propertiesFileName = "server.config";

        try (FileInputStream fileInputStream = new FileInputStream(propertiesFileName)) {
            properties.load(fileInputStream);
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }

        conncetionString = "jdbc:postgresql://" +
                properties.getProperty("server.database_host") + "/" +
                properties.getProperty("server.database_name");

        Connection conn = DriverManager.getConnection(conncetionString,
                properties.getProperty("server.database_user"),
                properties.getProperty("server.database_password"));

        numPlayers = 0;
        questionNumber = 0;

        questions = new ArrayList<>();

        String SQL = "SELECT * FROM questions";
        try (
                conn;
                Statement stmt = conn.createStatement();
                ResultSet resultSet = stmt.executeQuery(SQL)) {
            while(resultSet.next()){
                questions.add(new Question(resultSet.getString(2), resultSet.getBoolean(3)));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        Collections.shuffle(questions);

        numberOfQuestions = questions.size();

        p1went = false;
        p2went = false;

        try {
            ss = new ServerSocket(5000);
        } catch (IOException ex) {
            System.out.println("IOEx from GameServer Constructor");
        }
    }


    public void acceptConnection() {
        try {
            System.out.println("Waiting for connection...");
            while (numPlayers < 2) {
                Socket s = ss.accept();
                numPlayers++;
                System.out.println("Player #" + numPlayers + " has connected");

                ServerSideConnection ssc = new ServerSideConnection(s, numPlayers);

                if(numPlayers == 1){
                    player1 = ssc;
                }
                else{
                    player2 = ssc;
                }

                Thread t = new Thread(ssc);
                t.start();
            }
        } catch (IOException ex){
            System.out.println("IOExecption form acceptConnection()");
        }
    }

    private class ServerSideConnection implements Runnable {
        private Socket socket;
        private int playerID;


        private InputStream inputStream;
        private ObjectInputStream objectInputStream;
        private OutputStream outputStream;
        private ObjectOutputStream objectOutputStream;


        public ServerSideConnection(Socket s, int id){
            System.out.println("Constructor ServerSideConnection");

            socket = s;
            playerID = id;

            try{
                outputStream = socket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);

                inputStream = socket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);

            } catch(IOException ex){
                System.out.println("Exception Constructor ServerSideConnection");
            }

        }

        public void run() {
            try{
                System.out.println("Ive sent playerd id: " + playerID);
                objectOutputStream.writeInt(playerID);

                System.out.println("Ive sent number of questions: " + questions.size());
                objectOutputStream.writeInt(numberOfQuestions);


                System.out.println("Ive sent questions: ");
                for (Question q : questions) {
                    System.out.println(" " + q.getQuestion());
                }
                objectOutputStream.writeObject((List<Question>) questions);


                while(true) {
                    if(playerID == 1){
                        player1Answer = objectInputStream.readBoolean();
                        System.out.println("Player 1 answered: " + player1Answer);
                        player2.sendAnswer(player1Answer);
                        p1went = true;
                    }
                    else{
                        player2Answer = objectInputStream.readBoolean();
                        System.out.println("Player 2 answered: " + player2Answer);
                        player1.sendAnswer(player2Answer);
                        p2went = true;
                    }


                    if(p1went == true && p2went == true){
                        questionNumber++;

                        p1went = false;
                        p2went = false;
                    }

                    if(questionNumber == questions.size() - 1){
                        System.out.println("Question nr: " + questionNumber);
                        break;
                    }

                }
                player1.closeConnection();
                player2.closeConnection();
            } catch (IOException ex) {
                System.out.println("Exception run() " + ex.getMessage() + " -> " + ex.getCause() + " -> " + ex.getLocalizedMessage());
                ex.printStackTrace();
            }
        }


        public void sendAnswer(boolean answer){
            try{
                objectOutputStream.writeBoolean(answer);
                objectOutputStream.flush();
            } catch (IOException ex) {
                System.out.println("IOException form sendButtonNum() ");
            }
        }

        public void closeConnection(){
            try{
                socket.close();
                System.out.println(" --CONNECTION CLOSED--");
            } catch(IOException ex){
                System.out.println("IOException from closeConnection()");
            }
        }

    }
}