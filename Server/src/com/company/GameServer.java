package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
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


    public GameServer() throws IOException {
        System.out.println("-- Server --");
        numPlayers = 0;
        questionNumber = 0;

        questions = new ArrayList<>();

        File file = new File("pytania.txt");
        FileInputStream fileStream = new FileInputStream(file);
        InputStreamReader input = new InputStreamReader(fileStream);
        BufferedReader reader = new BufferedReader(input);

        String line ;
        boolean line2;
        while((line = reader.readLine()) != null)
        {
            line2 = Boolean.parseBoolean(reader.readLine());
            //   System.out.println(line + " - >> " + line2);
            //   System.out.println(q.getQuestion() + " - >> " + q.getAnswear());
            questions.add(new Question(line, line2));
        }

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