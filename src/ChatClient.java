
import java.io.*;
import java.util.ArrayList;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 */
public class ChatClient extends AbstractClient implements Serializable {
    //Instance variables **********************************************

    /**
     * The interface type variable. It allows the implementation of the display
     * method in the client.
     */
    ChatIF clientUI;
    TicTacToeConsole tttBoard;

    //Constructors ****************************************************
    /**
     * Constructs an instance of the chat client.
     *
     * @param host The server to connect to.
     * @param port The port number to connect on.
     * @param clientUI The interface type variable.
     */
    public ChatClient(String host, int port, ChatIF clientUI)
            throws IOException {
        super(host, port); //Call the superclass constructor
        //clientUI = new GUIConsole(host, port);
        this.clientUI = clientUI;
        openConnection();
        Envelope envUser = new Envelope("login", "","guest");
        this.sendToServer(envUser);
        Envelope envJoin = new Envelope("join", "", "lobby");
        this.sendToServer(envJoin);
    } 

    //Instance methods ************************************************
    /**
     * This method handles all data that comes in from the server.
     *
     * @param msg The message from the server.
     */
    public void handleMessageFromServer(Object msg) {
        if (msg instanceof Envelope){
            Envelope env = (Envelope)msg;
            handleCommandFromServer(env);
        }
        else{
            clientUI.display(msg.toString());
        }
        
    }
    
    public void handleCommandFromServer(Envelope env){
        if (env.getId().equals("who")){
            ArrayList<String> userList = (ArrayList<String>)env.getContents();
            String room = env.getArgs();
            
            clientUI.displayUserList(userList, room);
            
        }
        else if (env.getId().equals("ttt")){
            TicTacToe TTT = (TicTacToe)env.getContents();
            processTicTacToe(TTT);
        }
    }
    
    
    
    public void processTicTacToe(TicTacToe TTT){
        //handle by case based on game state
        switch(TTT.getGameState()){
            case 1:
                clientUI.display("You have been invited to play tic tac toe with " + TTT.getPlayer1());
                clientUI.display("Send #tttAccept to accept, or #tttDecline to decline");
                tttBoard = new TicTacToeConsole(TTT, this);
                               
                break;
            
             
            case 2:
                clientUI.display("Your game was declined.");
                tttBoard.setVisible(false);
                tttBoard = null;
                break;
            case 3:
                clientUI.display("Your turn to play tic tac toe!");
                tttBoard.TTT = TTT;
                tttBoard.updateBoardDisplay();
//                int initialTurnCounter = thisTTT.getTurnCounter();
//                thisTTT.updateBoardDisplay();
//                int currentTurnCount = thisTTT.getTurnCounter();
//                while (currentTurnCount <= initialTurnCounter){
//                    //wait for user to take turn
//                    currentTurnCount = thisTTT.getTurnCounter();
//                    continue;
//                    
//                }
//                if (currentTurnCount>initialTurnCounter ){
//                    Envelope env = new Envelope("ttt", "", thisTTT);
//                    try{
//                        sendToServer(env);
//                    } catch (IOException ioe){
//                        System.out.println("Could not send TTT turn back to server");
//                        System.out.println(ioe.getMessage());
//                    }
//                    
//                }
                
                break;
            case 4: 
                clientUI.display("You have lost");
                tttBoard.setVisible(false);  
                tttBoard.TTT = null;
                break;
            case 5:
                clientUI.display("The game was a draw");
                tttBoard.setVisible(false);
                break;
        }
    }

    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */
    public void handleMessageFromClientUI(String message) {

        if (message.charAt(0) == '#') {

            handleClientCommand(message);

        } else {
            try {
                sendToServer(message);
            } catch (IOException e) {
                clientUI.display("Could not send message to server.  Terminating client.......");
                quit();
            }
        }
    }

    /**
     * This method terminates the client.
     */
    public void quit() {
        try {
            closeConnection();
        } catch (IOException e) {
        }
        System.exit(0);
    }

    public void connectionClosed() {

        System.out.println("Connection closed");

    }

    public void handleClientCommand(String message) {

        if (message.equals("#quit")) {
            clientUI.display("Shutting Down Client");
            quit();

        }

        else if (message.equals("#logoff")) {
            //closes connection but doesn't close client - need to do this change host or port, then login
            clientUI.display("Disconnecting from server");
            try {
                closeConnection();
            } catch (IOException e) {
            };

        }

        else if (message.indexOf("#setHost") >= 0) {

            if (isConnected()) {
                clientUI.display("Cannot change host while connected");
            } else {
                setHost(message.substring(8, message.length()).trim());
            }

        }

        else if (message.indexOf("#setPort") >= 0) {
            //#setPort 5556
            if (isConnected()) {
                clientUI.display("Cannot change port while connected");
            } else {
                setPort(Integer.parseInt(message.substring(8, message.length()).trim()));
            }
            //trimming whitespace from before/after section

        }

        else if (message.indexOf("#login") == 0) {
            //#login Laura
            if (isConnected()) {
                clientUI.display("Already connected, need to #logoff first");
            } else {

                try {
                    String username = message.substring(6, message.length()).trim();
                    openConnection();
                    clientUI.display("Logging in as "+username);
                    Envelope env = new Envelope("login", "", username);
                    this.sendToServer(env);
                } catch (IOException e) {
                    clientUI.display("failed to connect to server.");
                }
            }
        }
        else if (message.indexOf("#join")==0){
            //#join roomName
            //have a string that we substring, make envelope, then send envelope
            try{
                String roomname = message.substring(5, message.length()).trim();
                Envelope env = new Envelope("join", "", roomname);
                this.sendToServer(env);
            } catch(IOException ie){
                clientUI.display("could not join room");
            }
            
        }
        
        else if (message.indexOf("#pm")==0){
            //#pm Nick Howdy everybody!
            try{
            String targetAndMessage = message.substring(3, message.length()).trim();
            //nick hello
            String target = targetAndMessage.substring(0, targetAndMessage.indexOf(" ")).trim();
            String pm = targetAndMessage.substring(targetAndMessage.indexOf(" "), targetAndMessage.length()).trim();
            Envelope env = new Envelope("pm", target, pm);
            clientUI.display("Target: "+target+ " pm: "+pm);
            this.sendToServer(env);
            } catch(IOException ie){
                clientUI.display("Target cannot be found");
            }
        }
        
        else if (message.indexOf("#yell")==0){
            try{
                String yellMessage = message.substring(5, message.length()).trim();
                Envelope env = new Envelope("yell", "", yellMessage);
                this.sendToServer(env);
            } catch(Exception e){
                clientUI.display("Failed to yell");
            }
        }
        
        if (message.equals("#who")){
            try{
                Envelope env = new Envelope("who", "", "");
                this.sendToServer(env);
            } catch(IOException ie){
                clientUI.display("Failed to acquire user list");
            }
        }
        
        else if (message.equals("#tttDecline")){
            //send decline to server
            try{
                this.sendToServer("#tttDecline");
                tttBoard = null;
            }catch (IOException ioe){
                System.out.println(ioe.getMessage());
            }
            //server handles unpacking object, changing gamestate and resending to player 1
        }
        
        else if (message.equals("#tttAccept")){
            //display ttt board
            tttBoard.setVisible(true);
            //send accept to server, which handles changing gamestate and resending to player 1
            try{
                this.sendToServer("#tttAccept");
            }catch (IOException ioe){
                System.out.println(ioe.getMessage());
            }
        }
    

    }

    protected void connectionException(Exception exception) {
        System.out.println("Server has shutdown.");
        //user can reconnect if server starts again by #login ... if they know to try
        //server cannot send message to previous users to say server has restarted because connections were lost
        
        //maybe best behaviour should be:
        //System.exit(0);
        //otherwise when user sends message it errors and kills anyway
        
    }

}
//End of ChatClient class
