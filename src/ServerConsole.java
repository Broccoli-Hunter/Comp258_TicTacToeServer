
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author laura
 */
public class ServerConsole {

    final public static int DEFAULT_PORT = 5555;
    EchoServer echoServer;

      
    
    public ServerConsole(int port) {
        echoServer = new EchoServer(port, this);
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        int port = 0; //Port to listen on
        
        try {
            port = Integer.parseInt(args[0]);
        } catch (ArrayIndexOutOfBoundsException oob) {
            port = DEFAULT_PORT;
        }

        ServerConsole sc = new ServerConsole(port);
        sc.accept();
        sc.displayOnServerConsole("Server Console created");
        
    }
    
    public void accept() {
        try {
            BufferedReader fromConsole
                    = new BufferedReader(new InputStreamReader(System.in));
            String message;

            while (true) {
                message = fromConsole.readLine();
                echoServer.handleMessageFromServerConsole(message);
            }
        } catch (Exception ex) {
            System.out.println("Unexpected error while reading from console!");
        }
    }
    public void displayOnServerConsole(String message){
        System.out.println(message);
    }
    
}
