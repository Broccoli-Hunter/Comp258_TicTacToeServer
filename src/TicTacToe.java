
import java.io.Serializable;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.IOException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author laura
 */
public class TicTacToe implements Serializable{

    
    private String Player1;
    private String Player2;
    private int activePlayer; //1 or 2
    private int gameState; //1=invite; 2=declined; 3=playing; 4=won; 5=draw
    private char[][] board;
    private int turnCounter;
    
    
    public TicTacToe(String p1, String p2){
        
        Player1 = p1;
        Player2 = p2;
        activePlayer = 1;
        gameState = 1;
        board = new char[3][3];
        turnCounter = 0;

        
    }
    public TicTacToe(){}

    public String getPlayer1() {
        return Player1;
    }

    public String getPlayer2() {
        return Player2;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public int getGameState() {
        return gameState;
    }

    public char[][] getBoard() {
        return board;
    }
    
    public int getTurnCounter(){
        return turnCounter;
    }
    

    public void setPlayer1(String Player1) {
        this.Player1 = Player1;
    }

    public void setPlayer2(String Player2) {
        this.Player2 = Player2;
    }

    public void setActivePlayer(int activePlayer) {
        this.activePlayer = activePlayer;
    }

    public void setGameState(int gameState) {
        this.gameState = gameState;
    }

    public void setBoard(char[][] board) {
        this.board = board;
    }
    
    public void setTurnCounter(int turnCount){
        this.turnCounter = turnCount;
    }
    
    
    
    
    public void checkWin(int buttonPressed){
        //call from button press
        //checking if player 1 or 2 (X or O) has all the spots claimed for a win
        boolean winCond1, winCond2, winCond3, winCond4;
        switch(buttonPressed){
            case 1:
                
                winCond1 = checkWinRow1();
                winCond2 = checkWinCol1();
                winCond3 = checkWinDiagDown();
                if ((winCond1 == true) ||
                        (winCond2 == true)||
                        (winCond3 == true)){
                    gameState = 4;
                }
                break;
            case 2:
                
                winCond1 = checkWinRow1();
                winCond2 = checkWinCol2();
                if ((winCond1 == true) ||
                        (winCond2 == true)){
                    gameState = 4;
                }
                break;
            case 3:
                
                winCond1 = checkWinRow1();
                winCond2 = checkWinCol3();
                winCond3 = checkWinDiagUp();
                if ((winCond1 == true)||
                        (winCond2 == true)||
                        (winCond3 == true)){
                    gameState = 4;
                }
                break;
            case 4:
                
                winCond1 = checkWinRow2();
                winCond2 = checkWinCol2();
                if ((winCond1 == true) ||
                        (winCond2 == true)){
                    gameState = 4;
                }
                break;
            case 5:
                
                winCond1 = checkWinRow2();
                winCond2 = checkWinCol2();
                winCond3 = checkWinDiagDown();
                winCond4 = checkWinDiagUp();
                if ((winCond1 == true) ||
                        (winCond2 == true)||
                        (winCond3 ==true)||
                        (winCond4== true)){
                    gameState = 4;
                }
                break;
            case 6:
                
                winCond1 = checkWinRow2();
                winCond2 = checkWinCol3();
                if ((winCond1 == true) ||
                        (winCond2 == true)){
                    gameState = 4;
                }
                break;
            case 7:
                
                winCond1 = checkWinRow3();
                winCond2 = checkWinCol1();
                winCond3 = checkWinDiagUp();
                if ((winCond1 == true)||
                        (winCond2 == true)||
                        (winCond3 == true)){
                    gameState = 4;
                }
                break;
            case 8:
                
                winCond1 = checkWinRow3();
                winCond2 = checkWinCol2();
                if ((winCond1 == true)||
                        (winCond2 == true)){
                    gameState = 4;
                }
                break;
            case 9:
                
                winCond1 = checkWinRow3();
                winCond2 = checkWinCol3();
                winCond3 = checkWinDiagDown();
                if ((winCond1 == true)||
                        (winCond2 == true)||
                        (winCond3 == true)){
                    gameState = 4;
                }
                break;
        }
        
        checkDraw();
    }
    
    public boolean checkWinRow1(){
        boolean isWon = false;
        boolean[] check3 = new boolean[3];
        if (activePlayer==1){
            for (int i = 0; i <3; i++){
                if (board[0][i] == 'X'){
                    check3[i] = true;
                }
            }        
        }
        else{
            for (int i = 0; i <3; i++){
                if (board[0][i] == 'O'){
                    check3[i] = true;
                }
            }
        }
        if ((check3[0]==true) &&
                (check3[1]==true)&&
                (check3[2]==true)
                ){
            isWon = true;
        }
        return isWon;
    }
    
    public boolean checkWinRow2(){
        boolean isWon = false;
        boolean[] check3 = new boolean[3];
        if (activePlayer==1){
            for (int i = 0; i <3; i++){
                if (board[1][i] == 'X'){
                    check3[i] = true;
                }
            }
        }
        else{
            for (int i = 0; i <3; i++){
                if (board[1][i] == 'O'){
                    check3[i] = true;
                }
            }
        }
        if ((check3[0]==true) &&
                (check3[1]==true)&&
                (check3[2]==true)
                ){
            isWon = true;
        }
        return isWon;
    }
    public boolean checkWinRow3(){
        boolean isWon = false;
        boolean[] check3 = new boolean[3];
        if (activePlayer==1){
            for (int i = 0; i <3; i++){
                if (board[2][i] == 'X'){
                    check3[i] = true;
                }
            }
        }
        else{
            for (int i = 0; i <3; i++){
                if (board[2][i] == 'O'){
                    check3[i] = true;
                }
            }
        }
        if ((check3[0]==true) &&
                (check3[1]==true)&&
                (check3[2]==true)
                ){
            isWon = true;
        }
        return isWon;
    }
    public boolean checkWinCol1(){
        boolean isWon = false;
        boolean[] check3 = new boolean[3];
        if (activePlayer==1){
            for (int i = 0; i <3; i++){
                if (board[i][0] == 'X'){
                    check3[i] = true;
                }
            }
        }
        else{
            for (int i = 0; i <3; i++){
                if (board[i][0] == 'O'){
                    check3[i] = true;
                }
            }
        }
        if ((check3[0]==true) &&
                (check3[1]==true)&&
                (check3[2]==true)
                ){
            isWon = true;
        }
        return isWon;
    }
    public boolean checkWinCol2(){
        boolean isWon = false;
        boolean[] check3 = new boolean[3];
        if (activePlayer==1){
            for (int i = 0; i <3; i++){
                if (board[i][1] == 'X'){
                    check3[i] = true;
                }
            }
        }
        else{
            for (int i = 0; i <3; i++){
                if (board[i][1] == 'O'){
                    check3[i] = true;
                }
            }
        }
        if ((check3[0]==true) &&
                (check3[1]==true)&&
                (check3[2]==true)
                ){
            isWon = true;
        }
        return isWon;
    }
    public boolean checkWinCol3(){
        boolean isWon = false;
        boolean[] check3 = new boolean[3];
        if (activePlayer==1){
            for (int i = 0; i <3; i++){
                if (board[i][2] == 'X'){
                    check3[i] = true;
                }
            }
        }
        else{
            for (int i = 0; i <3; i++){
                if (board[i][2] == 'O'){
                    check3[i] = true;
                }
            }
        }
        if ((check3[0]==true) &&
                (check3[1]==true)&&
                (check3[2]==true)
                ){
            isWon = true;
        }
        return isWon;
    }
    public boolean checkWinDiagUp(){
        boolean isWon = false;
        boolean[] check3 = new boolean[3];
        if (activePlayer==1){
            for (int i = 0; i <3; i++){
                if (board[2-i][i] == 'X'){
                    check3[i] = true;
                }
            }
        }
        else{
            for (int i = 0; i <3; i++){
                if (board[2-i][i] == 'O'){
                    check3[i] = true;
                }
            }
        }
        if ((check3[0]==true) &&
                (check3[1]==true)&&
                (check3[2]==true)
                ){
            isWon = true;
        }
        return isWon;
    }
    public boolean checkWinDiagDown(){
        boolean isWon = false;
        boolean[] check3 = new boolean[3];
        if (activePlayer==1){
            for (int i = 0; i <3; i++){
                if (board[i][i] == 'X'){
                    check3[i] = true;
                }
            }
        }
        else{
            for (int i = 0; i <3; i++){
                if (board[i][i] == 'O'){
                    check3[i] = true;
                }
            }
        }
        if ((check3[0]==true) &&
                (check3[1]==true)&&
                (check3[2]==true)
                ){
            isWon = true;
        }
        return isWon;
    }
    
    public void checkDraw(){
        if ((turnCounter == 9) && (gameState != 4)){
            //game is a draw (all 9 buttons have been pressed and no win detected)
            gameState = 5; //draw - I added this state
        }
        
    }
    
    public void updateBoard(int buttonPressed){  //buttonPressed = 'move' from assignment sheet
        //change board array to reflect new symbol
        switch(buttonPressed){
            case 1:
                //button1 pressed - board[1][1]
                if (activePlayer == 1){
                    board[0][0]='X';
                }
                else{
                    board[0][0]='O';
                }
                break;
            case 2:
                //button2 pressed - board[1][2]
                if (activePlayer == 1){
                    board[0][1]='X';
                }
                else{
                    board[0][1]='O';
                }
                break;
            case 3:
                //button3 pressed - board[1][3]
                if (activePlayer == 1){
                    board[0][2]='X';
                }
                else{
                    board[0][2]='O';
                }
                break;
            case 4:
                //button4 pressed - board[2][1]
                if (activePlayer == 1){
                    board[1][0]='X';
                }
                else{
                    board[1][0]='O';
                }
                break;
            case 5:
                //button5 pressed - board[2][2]
                if (activePlayer == 1){
                    board[1][1]='X';
                }
                else{
                    board[1][1]='O';
                }
                break;
            case 6:
                //button6 pressed - board[2][3]
                if (activePlayer == 1){
                    board[1][2]='X';
                }
                else{
                    board[1][2]='O';
                }
                break;
            case 7:
                //button7 pressed - board[3][1]
                if (activePlayer == 1){
                    board[2][0]='X';
                }
                else{
                    board[2][0]='O';
                }
                break;
            case 8:
                //button8 pressed - board[3][2]
                if (activePlayer == 1){
                    board[2][1]='X';
                }
                else{
                    board[2][1]='O';
                }
                break;
            case 9:
                //button9 pressed - board[3][3]
                if (activePlayer == 1){
                    board[2][2]='X';
                }
                else{
                    board[2][2]='O';
                }
                break;
        }
        
    }
    
    

}
