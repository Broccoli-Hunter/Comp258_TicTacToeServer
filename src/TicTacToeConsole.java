
import javax.swing.JButton;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;
/**
 *
 * @author laura
 */
public class TicTacToeConsole extends JFrame {
    //UI variables
    private JButton button1 = new JButton("-");
    private JButton button2 = new JButton("-");
    private JButton button3 = new JButton("-");
    private JButton button4 = new JButton("-");
    private JButton button5 = new JButton("-");
    private JButton button6 = new JButton("-");
    private JButton button7 = new JButton("-");
    private JButton button8 = new JButton("-");
    private JButton button9 = new JButton("-");
    private JLabel userLB = new JLabel("Name", JLabel.CENTER);
    
    //TTT object
    TicTacToe TTT;
    
    ChatClient client;
    
    
    //
    
    //constructor
    public TicTacToeConsole(TicTacToe TTT, ChatClient client){
        super("Tic Tac Toe");
        this.TTT = TTT;
        this.client = client;
        String mySymbol = "";
        
        createBoard();
    }
    
    //create board
    public void createBoard(){
        
        setSize(300, 300);
        setLayout( new BorderLayout(5,5));
        JPanel bottom = new JPanel();
        add( "Center" , bottom);
        bottom.setLayout( new GridLayout(3,3,1,1));
        //bottom.add(userLB);
        bottom.add(button1);
        bottom.add(button2);
        bottom.add(button3);
        bottom.add(button4);
        bottom.add(button5);
        bottom.add(button6);
        bottom.add(button7);
        bottom.add(button8);
        bottom.add(button9);
        
        button1.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //button1 pressed - board[1][1]
                if (TTT.getActivePlayer() == 1){
                    button1.setText(String.valueOf('X'));
                }
                else{
                    button1.setText(String.valueOf('O'));
                }
                TTT.setTurnCounter(TTT.getTurnCounter()+1);
                TTT.updateBoard(1);
                TTT.checkWin(1);
                handleEndOfTurn();
            }
        });
        
        button2.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //button2 pressed- board[1][2]
                if (TTT.getActivePlayer() == 1){
                    button2.setText(String.valueOf('X'));
                }
                else{
                    button2.setText(String.valueOf('O'));
                }
                TTT.setTurnCounter(TTT.getTurnCounter()+1);
                TTT.updateBoard(2);
                TTT.checkWin(2);
                handleEndOfTurn();
            }
        });
        
        button3.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //button3 pressed- board[1][3]
                if (TTT.getActivePlayer() == 1){
                    button3.setText(String.valueOf('X'));
                }
                else{
                    button3.setText(String.valueOf('O'));
                }
                TTT.setTurnCounter(TTT.getTurnCounter()+1);
                TTT.updateBoard(3);
                TTT.checkWin(3);
                handleEndOfTurn();
            }
        });
        
        button4.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //button4 pressed - board[2][1]
                if (TTT.getActivePlayer() == 1){
                    button4.setText(String.valueOf('X'));
                }
                else{
                    button4.setText(String.valueOf('O'));
                }
                TTT.setTurnCounter(TTT.getTurnCounter()+1);
                TTT.updateBoard(4);
                TTT.checkWin(4);
                handleEndOfTurn();
            }
        });
        
        button5.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //button5 pressed- board[2][2]
                if (TTT.getActivePlayer() == 1){
                    button5.setText(String.valueOf('X'));
                }
                else{
                    button5.setText(String.valueOf('O'));
                }
                TTT.setTurnCounter(TTT.getTurnCounter()+1);
                TTT.updateBoard(5);
                TTT.checkWin(5);
                handleEndOfTurn();
            }
        });
        
        button6.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //button6 pressed- board[2][3]
                if (TTT.getActivePlayer() == 1){
                    button6.setText(String.valueOf('X'));
                }
                else{
                    button6.setText(String.valueOf('O'));
                }
                TTT.setTurnCounter(TTT.getTurnCounter()+1);
                TTT.updateBoard(6);
                TTT.checkWin(6);
                handleEndOfTurn();
            }
        });
        
        button7.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //button7 pressed- board[3][1]
                if (TTT.getActivePlayer() == 1){
                    button7.setText(String.valueOf('X'));
                }
                else{
                    button7.setText(String.valueOf('O'));
                }
                TTT.setTurnCounter(TTT.getTurnCounter()+1);
                TTT.updateBoard(7);
                TTT.checkWin(7);
                handleEndOfTurn();
            }
        });
        
        button8.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //button8 pressed- board[3][2]
                if (TTT.getActivePlayer() == 1){
                    button8.setText(String.valueOf('X'));
                }
                else{
                    button8.setText(String.valueOf('O'));
                }
                TTT.setTurnCounter(TTT.getTurnCounter()+1);
                TTT.updateBoard(8);
                TTT.checkWin(8);
                handleEndOfTurn();
            }
        });
        
        button9.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e){
                //button9 pressed- board[3][3]
                if (TTT.getActivePlayer() == 1){
                    button9.setText(String.valueOf('X'));
                }
                else{
                    button9.setText(String.valueOf('O'));
                }
                TTT.setTurnCounter(TTT.getTurnCounter()+1);
                TTT.updateBoard(9);
                TTT.checkWin(9);
                handleEndOfTurn();
            }
        });
        
        setVisible(false);
    }
    
    //update board
    public void updateBoardDisplay(){
        char[][] board = TTT.getBoard();
        button1.setText(Character.toString(board[0][0]));
        button2.setText(Character.toString(board[0][1]));
        button3.setText(Character.toString(board[0][2]));
        button4.setText(Character.toString(board[1][0]));
        button5.setText(Character.toString(board[1][1]));
        button6.setText(Character.toString(board[1][2]));
        button7.setText(Character.toString(board[2][0]));
        button8.setText(Character.toString(board[2][1]));
        button9.setText(Character.toString(board[2][2]));
    }
    
    //pack up and send board when turn over
    public void handleEndOfTurn(){
        Envelope env = new Envelope("ttt", "", TTT);
        try{
            if (TTT.getGameState()==4){
                client.handleMessageFromClientUI("You have won Tic Tac Toe!");
                setVisible(false);
                TTT = null;
            }
            client.sendToServer(env);
        }catch(IOException ioe){
            //where does this even display?
            System.out.println("Could not handle end of turn");
            System.out.println(ioe.getMessage());
        }
        
    }
}
