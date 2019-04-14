package pewchatserver;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server implements Runnable {

    private final String address;
    private final int port;
    private ServerSocket serverSocket;
    private Socket socket;
    private int i = 1;
//    private ArrayList<User> users = new ArrayList<User>();

    public Server(String address, int port) throws IOException{
        this.address = address;
        this.port = port;
        this.serverSocket = new ServerSocket(port);
        System.out.println("Server Started at Port: "+port);
    }

    @Override
    public void run() {
         while (true) 
        {
             try {
                 // Accept the incoming request
                 socket = serverSocket.accept();
                 
                 System.out.println("New client request received : " + socket);
                 
                 // obtain input and output streams
                 DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                 DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                 
                 System.out.println("Creating a new user handler for this client...");
                 
                 // Create a new handler object for handling this request.
                 User newUser = new User(socket, "Client"+i, inputStream, outputStream);
                 
                 // Create a new Thread with this object.
                 Thread t = new Thread(newUser);
                 
                 System.out.println("Adding this client to active client list");
                 
                 // add this client to active clients list
                 PewChatServer.users.add(newUser);
                 
                 // start the thread.
                 t.start();
                 
                 // increment i for new client.
                 // i is used for naming only, and can be replaced
                 // by any naming scheme
                 i++;
                 for(int i=0; i<PewChatServer.users.size(); i++){
                     System.out.println(PewChatServer.users.get(i).name+" is connected");
                 }
             } catch (IOException ex) {
                 Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
             }
 
        }
    }
}
