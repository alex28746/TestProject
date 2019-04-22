/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pewchatclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MyClient {
    // initialize socket and input output streams

    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    Scanner scn = new Scanner(System.in);
    Thread readThread;
    StringBuffer Messages = new StringBuffer();
    boolean newMessage = false;
    String status="";
    Boolean isConnected=false;
    static Map<String,String> OtherUserStatus = new HashMap<String,String>();
    boolean UserStatusChanged=false;

    public DataInputStream getInput() {
        return input;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public MyClient(String address, int port) {
        // establish a connection
        try {
            System.out.println("Connected");
            socket = new Socket(address, port);
            // takes input from terminal
            input = new DataInputStream(socket.getInputStream());

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
            isConnected=true;
            
        } catch (UnknownHostException u) {
            System.out.println("MyClient host exception " + u);
        } catch (IOException i) {
            System.out.println("MyClient io exception " + i);
        }
        System.out.println("aleksej = " + isConnected);
    }

    void ReadMessage() {
        System.out.println("ReadMessage called.");
        readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println("RUNNED1");
                        System.out.println("ReadMessage while called.");

                        // read the message sent to this client
                        String msg = input.readUTF();

                        System.out.println("Message received: " + msg);
                        Messages.append("<br>").append(msg);
                        newMessage = true;
                        System.out.println("Messages STRING: " + Messages);
                    }
                } catch (IOException e) {
                    System.out.println("IO Exception while tried to read message " + e);
                    e.printStackTrace();
                } catch (Exception ex) {
                    System.out.println("Normal exception while tried to read message " + ex);
                    ex.printStackTrace();
                }
                System.out.println("HashMap in in the end of the thread in readMessage size "+OtherUserStatus.size());
            }
        });
        System.out.println("HashMap in the end of readMessage size "+OtherUserStatus.size());
        readThread.start();
    }

    void SendMessage(String message) {
        try {
            // write on the output stream
            out.writeUTF(message);
        } catch (IOException e) {
            System.out.println("Io exception in sending message " + e);
            e.printStackTrace();
        }

    }
    
    void closeConnection(){
        try {
            out.close();
            input.close();
            socket.close();
            readThread.stop();
            scn.close();
        } catch (IOException ex) {
            System.out.println("Error while trying to close connection " + ex);
        }
    }

}
