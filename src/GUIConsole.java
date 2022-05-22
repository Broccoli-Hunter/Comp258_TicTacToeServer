/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author laura
 */

import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class GUIConsole extends JFrame implements ChatIF{
    private JButton closeB = new JButton("Logoff");
    private JButton openB = new JButton("Login");
    private JButton sendB = new JButton("Send");
    private JButton quitB = new JButton("Quit");
    private JButton whoB = new JButton("User List");
    private JButton pmB = new JButton("PM");
    private JButton tttB = new JButton("Tic Tac Toe");
    
    private JComboBox whoCB = new JComboBox();

    private JTextField portTxF = new JTextField("5555");
    private JTextField hostTxF = new JTextField("127.0.0.1");
    private JTextField messageTxF = new JTextField("");
    private JTextField userTxF = new JTextField("");

    private JLabel portLB = new JLabel("Port: ", JLabel.RIGHT);
    private JLabel hostLB = new JLabel("Host: ", JLabel.RIGHT);
    private JLabel userLB = new JLabel("User ID:", JLabel.RIGHT);
    private JLabel messageLB = new JLabel("Message: ", JLabel.RIGHT);

    private JTextArea messageList = new JTextArea();
    
    ChatClient client;
    
    /**
     * The default port to connect on.
     */
    final public static int DEFAULT_PORT = 5555;

    public  GUIConsole ( String host, int port, String userId){  //?implements ChatIF
	super("Simple Chat GUI");
        setSize(300, 400);

        setLayout( new BorderLayout(5,5));
        JPanel bottom = new JPanel();
        add( "Center", messageList );
        add( "South" , bottom);

        bottom.setLayout( new GridLayout(8,2,5,5)); //5 rows, 2 col, 5 px padding horizontally, 4 px padding vertically
        bottom.add(hostLB); 		
        bottom.add(hostTxF);
        bottom.add(portLB); 		
        bottom.add(portTxF);
        bottom.add(userLB);
        bottom.add(userTxF);
        bottom.add(messageLB); 	
        bottom.add(messageTxF);
        bottom.add(whoB);
        bottom.add(whoCB);
        bottom.add(pmB);		
        bottom.add(sendB);
        bottom.add(tttB);
        bottom.add(openB); 
        bottom.add(quitB);
        bottom.add(closeB);

        sendB.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //send();
                sendMessage(messageTxF.getText());
            }
        });
        
        quitB.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //quit();
                client.quit();
            }
        });
        
        closeB.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //closeConnection();
                closeConnection();
                
            }
        });
        
        openB.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //set new host/port from textboxes
                login(hostTxF.getText(), portTxF.getText(), userTxF.getText());
                
                
            }
        });
        
        whoB.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //set new host/port from textboxes
                sendMessage("#who");
                
                
            }
        });
        
        pmB.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //set new host/port from textboxes
                sendMessage("#pm "+whoCB.getSelectedItem()+" "+messageTxF.getText());
                
                
            }
        });
        
        tttB.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                String player1 = userTxF.getText();
                String player2 = whoCB.getSelectedItem().toString();
                TicTacToe TTT = new TicTacToe(player1, player2);
                client.tttBoard = new TicTacToeConsole(TTT, client);
                client.tttBoard.setVisible(true);
                Envelope env = new Envelope("ttt", "", TTT);
                try {
                    client.sendToServer(env);
                } catch (IOException ioe) {
                    System.out.println("Could not send envelope to server");
                    System.out.println(ioe.getMessage());
                }
            }
        });
        
        portTxF.setText(port+"");
        hostTxF.setText(host);
        userTxF.setText(userId);
        
        try {
            client = new ChatClient(host, port, this);
        } catch (IOException exception) {
            System.out.println("Error: Can't setup connection!!!!"
                    + " Terminating client.");
            System.exit(1);
        }
        closeConnection();
        login(hostTxF.getText(), portTxF.getText(), userTxF.getText());
        setVisible(true);
    }
    
    /**
     * This method is responsible for the creation of the Client UI.
     *
     * @param args[0] The host to connect to.
     */
    public static void main(String[] args) {
        String host = "";
        int port = 0;  //The port number
        String userId = "";

        try {
            host = args[0];
            port = Integer.parseInt(args[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
            host = "localhost";
            port = DEFAULT_PORT;
        }
        
        try{
            userId = args[2];
        }catch(ArrayIndexOutOfBoundsException e){
            userId = "guest";
        }
        //ClientConsole chat= new ClientConsole(host, DEFAULT_PORT);
        GUIConsole chat = new GUIConsole(host, port, userId);
        
    }
    
    public void display( String message ){
        messageList.insert(message+"\n", 0);
    }
    
    
    
    public void sendMessage(String message){
        client.handleMessageFromClientUI(message);
    }
    
    public void login (String host, String port, String userId){
        client.setHost(host);
        client.setPort(Integer.parseInt(port));
        //client.handleMessageFromClientUI("#logoff"); //including this step because the client logs in as guest to start and need to clear that username
        client.handleMessageFromClientUI("#login "+userId);
        try{
            client.openConnection();
        } catch (IOException ioe){
            //outputs to console, not GUI yet
            System.out.println("Connection could not be created");
            System.out.println(ioe.getMessage());
        }
    }
    
    public void closeConnection(){  //logs off
        client.handleMessageFromClientUI("#logoff");
    }
    
    public void displayUserList(ArrayList<String> userList, String room){
        whoCB.removeAllItems();
        for (String user : userList){
            whoCB.addItem(user);
        }
    }
    
    

    

}
