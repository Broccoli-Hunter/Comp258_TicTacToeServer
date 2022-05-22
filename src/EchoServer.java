
import java.io.IOException;
import java.io.Serializable;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;

public class EchoServer extends AbstractServer implements Serializable {
    //Class variables *************************************************

    /**
     * The default port to listen on.
     */
    final public static int DEFAULT_PORT = 5555;
    private ServerConsole serverConsole;

    //Constructors ****************************************************
    /**
     * Constructs an instance of the echo server.
     *
     * @param port The port number to connect on.
     */
    public EchoServer(int port, ServerConsole serverConsole) {

        super(port);
        this.serverConsole = serverConsole;
        serverConsole.displayOnServerConsole("EchoServer created on port "+ getPort());
    }

    //Instance methods ************************************************
    /**
     * This method handles any messages received from the client.
     *
     * @param msg The message received from the client.
     * @param client The connection from which the message originated.
     */
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        if (msg instanceof Envelope) {
            Envelope env = (Envelope) msg;
            handleEnvelopeFromClient(env, client);
        } 
        else if (msg.toString().charAt(0) == '#'){
            String message = msg.toString();
            handleCommandFromClient(message, client);
        }else {
            System.out.println("Message received: " + msg + " from " + client);

            String userId;
            
            if (client.getInfo("userid") == null) {
                userId = "guest";
                client.setInfo("userId", userId);
                client.setInfo("room", "lobby");
                try{
                  client.sendToClient("Remember to log in first.");
                } catch (IOException ioe){
                    System.out.println(ioe.getMessage());
                }

            } else {
                userId = client.getInfo("userid").toString();
                

            }
            this.sendToAllClientsInRoom(userId + ": " + msg, client);
        }

    }
    
    public void handleCommandFromClient(String message, ConnectionToClient client){
        if (message.equals("#tttDecline")) {
            // extract TTT object from userInfo
            TicTacToe TTT = (TicTacToe)client.getInfo("ttt");
            //change gamestate to 2 (declined)
            TTT.setGameState(2);
            //send object to Player 1
            Envelope env = new Envelope("ttt", "", TTT);
            ConnectionToClient connectToPlayer1 = getConnectionToClient(TTT.getPlayer1());
            try{
                connectToPlayer1.sendToClient(env);
            } catch(IOException ioe){
                System.out.println("Could not sent TTT Decline to "+TTT.getPlayer1());
                System.out.println(ioe.getMessage());
            }
            
        }
        else if (message.equals("#tttAccept")) {
          // extract TTT object from userInfo
          TicTacToe TTT = (TicTacToe)client.getInfo("ttt");
          // change gamestate to 3 (playing)
          TTT.setGameState(3);
          // Send object to Player 1
          Envelope env = new Envelope("ttt", "", TTT);
          ConnectionToClient connectToPlayer1 = getConnectionToClient(TTT.getPlayer1());
          try{
                connectToPlayer1.sendToClient("Your game has been accepted!");
                connectToPlayer1.sendToClient(env);
            } catch(IOException ioe){
                System.out.println("Could not sent TTT Accept to "+TTT.getPlayer1());
                System.out.println(ioe.getMessage());
            }

        }
    }

    public void handleEnvelopeFromClient(Envelope env, ConnectionToClient client) {
        //handle envelopes that come to the server
        if (env.getId().equals("login")) {
            String userID = env.getContents().toString();
            client.setInfo("userid", userID);  //capitalization inconsistency?
            client.setInfo("room", "lobby"); //people join a lobby first, then can join specific rooms
        }
        else if (env.getId().equals("join")){
            String roomID = env.getContents().toString();
            client.setInfo("room", roomID);
            //left off here, not sure if stuff above is correct!
        }
        else if (env.getId().equals("pm")){
            String target = env.getArgs();
            String message = env.getContents().toString();
            sendToAClient(message, target, client);
        }
        else if (env.getId().equals("yell")){
            String yellMessage = env.getContents().toString();
            String userID = client.getInfo("userid").toString();
            this.sendToAllClients(userID + " yells: "+yellMessage);
        }
        else if (env.getId().equals("who")){
            this.sendRoomListToClient(client);
        }
        else if (env.getId().equals("ttt")){
            this.processTicTacToe(env);
        }
        

    }
    public void processTicTacToe(Envelope envReceived){
        //handle by case based on game state
        TicTacToe TTT = (TicTacToe)envReceived.getContents();
        String player1 = TTT.getPlayer1();
        String player2 = TTT.getPlayer2();
        ConnectionToClient connectPlayer1 = getConnectionToClient(player1);
        ConnectionToClient connectPlayer2 = getConnectionToClient(player2); 
        //perform check to make sure Connections to Clients are real
        if ((connectPlayer1.getInetAddress()==null) || (connectPlayer2.getInetAddress()==null)){
            try{
                connectPlayer1.sendToClient("Players could not be found on server");
                connectPlayer2.sendToClient("Players could not be found on server");
            } catch (IOException ioe){
                serverConsole.displayOnServerConsole("A tictactoe game could not be hosted between "+player1+" & "+player2);
            }
        }
        
        Envelope env = new Envelope("ttt", "", "");
        switch(TTT.getGameState()){
            case 1:
                connectPlayer1.setInfo("ttt", TTT);
                connectPlayer2.setInfo("ttt", TTT);
                
                env.setContents(TTT);
                try{
                    connectPlayer2.sendToClient(env);
                }catch (IOException ioe){
                    serverConsole.displayOnServerConsole("TTT(1): An envelope could not be sent to "+player2);
                    serverConsole.displayOnServerConsole(ioe.getMessage());
                }
                break;
            case 2:
               try{
                    connectPlayer1.sendToClient(envReceived);
                }catch (IOException ioe){
                    serverConsole.displayOnServerConsole("TTT(2): An envelope could not be sent to "+player1);
                    serverConsole.displayOnServerConsole(ioe.getMessage());
                }
                break;
            case 3:
                connectPlayer1.setInfo("ttt", TTT);
                connectPlayer2.setInfo("ttt", TTT);
                if (TTT.getActivePlayer()==1){
                    TTT.setActivePlayer(2);
                    env.setContents(TTT);
                    try{
                        connectPlayer2.sendToClient(env);
                    }catch (IOException ioe){
                        serverConsole.displayOnServerConsole("TTT(1): An envelope could not be sent to "+player2);
                        serverConsole.displayOnServerConsole(ioe.getMessage());
                    }
                }
                else{
                    TTT.setActivePlayer(1);
                    env.setContents(TTT);
                    try{
                        connectPlayer1.sendToClient(env);
                    }catch (IOException ioe){
                        serverConsole.displayOnServerConsole("TTT(2): An envelope could not be sent to "+player1);
                        serverConsole.displayOnServerConsole(ioe.getMessage());
                    }
                }
                
                break;
            case 4: 
                if (TTT.getActivePlayer()==1){
                    TTT.setActivePlayer(2);
                    env.setContents(TTT);
                    try{
                        connectPlayer2.sendToClient(env);
                        connectPlayer1.sendToClient("You have won Tic Tac Toe!");
                    }catch (IOException ioe){
                        serverConsole.displayOnServerConsole("TTT(1): An envelope could not be sent to "+player2);
                        serverConsole.displayOnServerConsole(ioe.getMessage());
                    }
                }
                else{
                    TTT.setActivePlayer(1);
                    env.setContents(TTT);
                    try{
                        connectPlayer1.sendToClient(env);
                        connectPlayer2.sendToClient("You have won Tic Tac Toe!");
                    }catch (IOException ioe){
                        serverConsole.displayOnServerConsole("TTT(2): An envelope could not be sent to "+player1);
                        serverConsole.displayOnServerConsole(ioe.getMessage());
                    }
                }
                break;
            case 5:
                //draw
                try{
                        connectPlayer1.sendToClient(envReceived);
                        connectPlayer2.sendToClient(envReceived);
                    }catch (IOException ioe){
                        serverConsole.displayOnServerConsole("TTT(2): An envelope could not be sent to "+player1);
                        serverConsole.displayOnServerConsole(ioe.getMessage());
                    }
                break;
        }
        
       
    }
    
    public ConnectionToClient getConnectionToClient(String username) {
        Thread[] clientThreadList = getClientConnections();
        //make default (empty) ConnectionToClient to return in case username isn't found
        ConnectionToClient targetToReturn = new ConnectionToClient();
        for(int i = 0; i < clientThreadList.length; i++){
            ConnectionToClient target = (ConnectionToClient) clientThreadList[i];
            if (target.getInfo("userid").equals(username)){
                return target;
            }    
        }
        return targetToReturn;
    }
    
    public void sendRoomListToClient(ConnectionToClient client){
        Envelope env = new Envelope();
        env.setId("who");
        ArrayList<String> userList = new ArrayList<String>();
        String room = client.getInfo("room").toString();
        
        Thread[] clientThreadList = getClientConnections();
        for(int i = 0; i < clientThreadList.length; i++){
            ConnectionToClient target = (ConnectionToClient) clientThreadList[i];
            if (target.getInfo("room").equals(room)){
                userList.add(target.getInfo("userid").toString());
            }    
        }
        env.setContents(userList);
        env.setArgs(room);
        
        try{
            client.sendToClient(env);
        } catch(Exception ex){
            System.out.println("Failed to send user list to client");
        }
        
        
    }
    
    public void sendToAClient(Object msg, String pmTarget, ConnectionToClient client){
        Thread[] clientThreadList = getClientConnections();
        for(int i = 0; i < clientThreadList.length; i++){
            ConnectionToClient target = (ConnectionToClient) clientThreadList[i];
            if(target.getInfo("userid").equals(pmTarget)){
                try{
                    target.sendToClient(client.getInfo("userid") + ": "+msg);
                } catch(Exception e){
                    System.out.println("Failed to send private message");
                }
            }
        }
    }

    public void sendToAllClientsInRoom(Object msg, ConnectionToClient client) {
        Thread[] clientThreadList = getClientConnections();
        String room = client.getInfo("room").toString();

        for (int i = 0; i < clientThreadList.length; i++) {
            ConnectionToClient target = ((ConnectionToClient) clientThreadList[i]);
            if (target.getInfo("room").equals(room)) {
                try {
                    target.sendToClient(msg);
                } catch (Exception ex) {
                    System.out.println("Failed to send to client");
                }
            }
        }

    }
    
    public void handleMessageFromServerConsole(String message){
        if (message.charAt(0) == '#') {

            handleServerConsoleCommand(message);

        } else {
            sendToAllClients("<ADMIN>: "+message);
            serverConsole.displayOnServerConsole("<ADMIN>: "+message);
        }
    }
    public void handleServerConsoleCommand(String message){
        if (message.indexOf("#setPort") >= 0) {
            //#setPort 5556
            if (isListening()) {
                serverConsole.displayOnServerConsole("Cannot change port while connected");
            } else {
                setPort(Integer.parseInt(message.substring(8, message.length()).trim()));
                serverConsole.displayOnServerConsole("EchoServer now set to port "+getPort());
            }
            //trimming whitespace from before/after section
        }
           
        if (message.equals("#start")) {
            try {
                this.listen(); //Start listening for connections
                System.out.println("DEBUG - EchoServer listening for clients");
                
            } catch (Exception ex) {
                System.out.println("ERROR - Could not listen for clients!");
            }

        }
        if (message.equals("#stop")) {
            handleMessageFromServerConsole("Stopping Server");
            try{
                close();
            } catch(IOException ioe){
                System.out.println("Could not stop server");
                System.out.println(ioe.getMessage());
            }
        }
        if (message.equals("#quit")) {
            handleMessageFromServerConsole("Server has shut down");
            System.exit(0);
        }
        
        if (message.indexOf("#ison") == 0) {
            //print message stating whether user is on or off, and if on then what room
            //#ison Bob
            //"Bob is on in room lobby" or "Bob is not logged in"
            String userToCheck = message.substring(5, message.length()).trim();
            sendSpecificUserStatus(userToCheck);
        }
        
        if (message.equals("#userstatus")) {
            sendUserStatus();
        }
        
        
        if (message.indexOf("#joinroom") == 0) {
            //#joinroom room1 room2
            //everyone from room1 joins room2
            String bothRooms = message.substring(9, message.length()).trim();
            //room1 room2
            String room1 = bothRooms.substring(0, bothRooms.indexOf(" ")).trim();
            String room2 = bothRooms.substring(bothRooms.indexOf(" "), bothRooms.length()).trim();
            combineRooms(room1, room2);
        }
        
       
        
        
    }
    private void sendSpecificUserStatus(String userToCheck){
        //print message stating whether user is on or off, and if on then what room
            //#ison Bob
            //"Bob is on in room lobby" or "Bob is not logged in"
            
        String userRoom = "";
            
            Thread[] clientThreadList = getClientConnections();
            for (int i = 0; i < clientThreadList.length; i++) {
                ConnectionToClient target = ((ConnectionToClient) clientThreadList[i]);
                if (target.getInfo("userid").equals(userToCheck)) {
                    userRoom = target.getInfo("room").toString();
                }
            }
             
            if (userRoom != ""){
                serverConsole.displayOnServerConsole(userToCheck+" is on in room "+userRoom);
            }
            else{
                serverConsole.displayOnServerConsole(userToCheck+" is not logged in");
            }
    }
    private void sendUserStatus(){
            //print list of all connected users and what room they are in
            String userName = "";
            String userRoom = "";
            boolean noUsersConnected = true;
            serverConsole.displayOnServerConsole("Connected users:");
            Thread[] clientThreadList = getClientConnections();
            for (int i = 0; i < clientThreadList.length; i++) {
                ConnectionToClient target = ((ConnectionToClient) clientThreadList[i]);
                userName = target.getInfo("userid").toString();
                userRoom = target.getInfo("room").toString();
                if (userName != ""){
                    serverConsole.displayOnServerConsole(userName+" - "+userRoom);
                    noUsersConnected = false;
                }
            }
            if (noUsersConnected){
                serverConsole.displayOnServerConsole("No users connected");
            }
        }
    
    private void combineRooms(String room1, String room2){
        //everyone from room1 joins room2
        Thread[] clientThreadList = getClientConnections();
        for (int i = 0; i < clientThreadList.length; i++) {
            ConnectionToClient target = ((ConnectionToClient) clientThreadList[i]);
            if (target.getInfo("room").equals(room1)) {
                try{
                target.sendToClient("<ADMIN>: Attempting to relocate you to room "+room2);
                target.setInfo("room", room2);
                } catch (IOException ioe){
                    
                    serverConsole.displayOnServerConsole("Relocation failed for user "+target.getInfo("userid").toString());
                }
                
            }
        }
    }
    
    
    
    /**
     * This method overrides the one in the superclass. Called when the server
     * starts listening for connections.
     */
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    /**
     * This method overrides the one in the superclass. Called when the server
     * stops listening for connections.
     */
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }

    //Class methods ***************************************************
    /**
     * This method is responsible for the creation of the server instance (there
     * is no UI in this phase).
     *
     * @param args[0] The port number to listen on. Defaults to 5555 if no
     * argument is entered.
     */
//    public static void main(String[] args) {
//        int port = 0; //Port to listen on
//
//        try {
//            port = Integer.parseInt(args[0]);
//        } catch (ArrayIndexOutOfBoundsException oob) {
//            port = DEFAULT_PORT;
//        }
//
//        EchoServer sv = new EchoServer(port);
//
//        try {
//            sv.listen(); //Start listening for connections
//        } catch (Exception ex) {
//            System.out.println("ERROR - Could not listen for clients!");
//        }
//    }
    
    //EXPERIMENTAL
    public static void initiateServer(EchoServer sv, int port){
              
        try {
            sv.listen(); //Start listening for connections
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }

    protected void clientConnected(ConnectionToClient client) {

        System.out.println("<Client Connected:" + client + ">");

    }

    synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
        System.out.println("Client shut down");
    }

}
//End of EchoServer class
