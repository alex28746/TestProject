package toneanalyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import toneanalyzer.service.WatsonServiceImpl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class Server implements Runnable {
    private ServerSocket serverSocket;
    private Socket socket;
//    private ArrayList<UserListener> users = new ArrayList<UserListener>();

    private final int PORT = 9999;

    @Autowired
    private WatsonServiceImpl watsonService;

    public Server() throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        System.out.println("Server Started at Port: " + PORT);
    }

    @Override
    public void run() {
        while (true) {
            try {
                // Accept the incoming request
                socket = serverSocket.accept();

                System.out.println("New client request received : " + socket);

                // obtain input and output streams
                DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                System.out.println("Creating a new user handler for this client...");

                // Create a new handler object for handling this request.
                UserListener newUser = new UserListener(socket, "Client", inputStream, outputStream, watsonService);

                // Create a new Thread with this object.
                Thread t = new Thread(newUser);

                System.out.println("Adding this client to active client list");

                // add this client to active clients list
                ToneAnalyzerApp.users.add(newUser);

                // start the UserListener
                t.start();


            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
}
