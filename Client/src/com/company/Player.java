package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Player extends JFrame {
    private int width;
    private int height;

    private Container contentPane;
    private JTextArea message;
    private JTextArea messageQ;
    private JButton b1;
    private JButton b2;

    private int playerID;
    private int otherPlayer;

    private List<Question> questions;
    private int questionNumber;

    private int numberOfQuestions;
    private int turnsMade;
    private int myPoints;
    private int enemyPoints;

    private boolean buttonsEnabled;

    private Question question;
    private boolean answer;


    private ClientSideConnection csc;

    public Player()
    {
        width = 500;
        height = 200;

        contentPane = this.getContentPane();
        message = new JTextArea();
        messageQ = new JTextArea();
        b1 = new JButton("false");
        b2 = new JButton("true");

        answer = false;

        questions = new ArrayList<>();
        questionNumber = 0;

        numberOfQuestions = 10;
        turnsMade = 0;
        myPoints = 0;
        enemyPoints = 0;
    }

    public void setGUI()
    {
        this.setSize(width, height);
        this.setTitle("Quiz, player #" + playerID);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        contentPane.setLayout(new GridLayout(1, 5));
        contentPane.add(message);
        contentPane.add(messageQ);
        message.setText("Creating Quiz");
        message.setWrapStyleWord(true);
        message.setLineWrap(true);
        message.setEditable(false);

        messageQ.setText("Question");
        messageQ.setWrapStyleWord(true);
        messageQ.setLineWrap(true);
        messageQ.setEditable(false);

        contentPane.add(b1);
        contentPane.add(b2);


        if(playerID == 1)
        {
            System.out.println("You are player 1");
            message.setText("you are player nr 1, you go first");
            messageQ.setText(questions.get(questionNumber).getQuestion());
            otherPlayer = 2;
            buttonsEnabled = true;
        }
        else
        {
            System.out.println("You are player 2");
            message.setText("you are player nr 2, wait for ur turn");
            messageQ.setText(questions.get(questionNumber).getQuestion());
            otherPlayer = 1;
            buttonsEnabled = false;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    updateTurn();
                }
            });
            t.start();
        }

        toggleButtons();

        this.setVisible(true);
    }

    public void connectoToServer(){
        csc = new ClientSideConnection();
    }

    public void setUpButtons()
    {
        ActionListener al = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                JButton b = (JButton) ae.getSource();
                boolean answear_b = Boolean.parseBoolean(b.getText());
                message.setText("Your answear is: " + answear_b + " wait for player #" + otherPlayer);

                buttonsEnabled = false;
                toggleButtons();

                System.out.println("Question nr #" + questionNumber + " --> " + questions.get(questionNumber).getQuestion() + " --> " + answear_b);

                if(questions.get(questionNumber).getAnswer() == answear_b) {
                    // System.out.println("Question nr #" + questionNumber + " --> " + questions.get(questionNumber).getQuestion());
                    myPoints += 1;
                    System.out.println("My points: " + myPoints);
                }

                csc.sendAnswer(answear_b);

                if(playerID == 2 && questionNumber == numberOfQuestions - 1){
                    checkWinner();
                } else{
                    Thread t = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            updateTurn();
                        }
                    });
                    t.start();
                }

            }
        };

        b1.addActionListener(al);
        b2.addActionListener(al);
    }


    public void toggleButtons() {
        b1.setEnabled(buttonsEnabled);
        b2.setEnabled(buttonsEnabled);
    }


    public void updateTurn(){
        boolean answer = csc.receiveAnswer();
        message.setText("Ur enemy answered" + answer + " your turn.");
        messageQ.setText(questions.get(questionNumber).getQuestion());


        System.out.println("Question nr #" + questionNumber + " --> " + questions.get(questionNumber).getQuestion() + " --> " + answer);


        if(questions.get(questionNumber).getAnswer() == answer) {
            //  System.out.println("Question nr #" + questionNumber + " --> " + questions.get(questionNumber).getQuestion());
            enemyPoints += 1;
            System.out.println("Your enemy has: " + enemyPoints + " points");
        }
        questionNumber++;
        System.out.println(" qn --> " + questionNumber);


        if(playerID == 1 && questionNumber == numberOfQuestions - 1){
            checkWinner();
        }
        else{
            buttonsEnabled = true;
        }
        toggleButtons();
    }


    private void checkWinner(){
        buttonsEnabled = false;

        if(myPoints > enemyPoints){
            message.setText("You won. \nYOU: " + myPoints + "\nENEMY: " + enemyPoints);
            messageQ.setText("The end \nYou WON");
        }
        else if(myPoints < enemyPoints){
            message.setText("You lost. \nYOU: " + myPoints + "\nENEMY: " + enemyPoints);
            messageQ.setText("The end \nYou LOST");
        }
        else{
            message.setText("It's a tie. \n YOU: " + myPoints + "\nENEMY: " + enemyPoints);
            messageQ.setText("The end \nYt's a TIE");
        }

        csc.closeConnection();
    }



    /////////////// CSC
    private class ClientSideConnection{
        private Socket socket;

        private InputStream inputStream;
        private ObjectInputStream objectInputStream;
        private OutputStream outputStream;
        private ObjectOutputStream objectOutputStream;

        public ClientSideConnection()
        {
            System.out.println(" -- ClientSideConnection Cosntructor --");

            try{
                socket = new Socket( "localhost",5000);

                inputStream = socket.getInputStream();
                objectInputStream = new ObjectInputStream(inputStream);

                outputStream = socket.getOutputStream();
                objectOutputStream = new ObjectOutputStream(outputStream);


                playerID = objectInputStream.readInt();
                System.out.println("Connected to server as: " + playerID + ". ");

                numberOfQuestions = objectInputStream.readInt();
                System.out.println("Number of questions: " + numberOfQuestions + ". ");

                questions = (List<Question>) objectInputStream.readObject();
                System.out.println("Ive received questions: ");
                for (Question q : questions) {
                    System.out.println(" " + q.getQuestion());
                }


            } catch (IOException | ClassNotFoundException ex){
                System.out.println("IOException from clientSideConnection construct");
                ex.printStackTrace();
            }
        }


        public void sendAnswer(boolean answer)
        {
            try {
                objectOutputStream.writeBoolean(answer);
                System.out.println("Ive sent answer: " + answer);
                objectOutputStream.flush();
            } catch (IOException ex) {
                System.out.println("IOExecption from sendButtonNum() ");
            }
        }


        public boolean receiveAnswer()
        {
            boolean answer = false;
            try{
                answer = objectInputStream.readBoolean();
                System.out.println("Player #" + otherPlayer + " answered: " + answer + " --> " + questionNumber);
            }catch (IOException ex) {
                System.out.println("IOEception from receiveButtonNum()");
            }
            return answer;
        }


        public void closeConnection(){
            try{
                socket.close();
                System.out.println("-- Connection closed --");
            } catch(IOException ex){
                System.out.println("IOException from closeConnection. ");
            }
        }

    }

}
