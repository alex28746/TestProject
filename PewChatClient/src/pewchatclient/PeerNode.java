/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pewchatclient;

/**
 *
 * @author ToniGeorge
 */
import java.io.*;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PeerNode implements Runnable {

    int portNum;
    String UserName;
    String IPaddress;
    StringBuffer MessageHistory;
    ServerSocket serverSocket;
    Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    //PeerNode acting as a Server constructor
    void PeerNode(String UserName) {
        this.UserName = UserName;
        this.MessageHistory = new StringBuffer();

        try {
            serverSocket = new ServerSocket(0);
            this.portNum = serverSocket.getLocalPort();
            this.IPaddress = serverSocket.getInetAddress().toString();
            sendInfo();
            socket = serverSocket.accept();
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {

        }

    }

    //PeerNode acting as a client constructor
    void PeerNode(String UserName, String IPaddress, String portNum) {
        this.UserName = UserName;
        this.IPaddress = IPaddress;
        this.portNum = Integer.parseInt(portNum);
        this.MessageHistory = new StringBuffer();
        try {
            socket = new Socket(this.IPaddress, this.portNum);
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
        } catch (IOException ex) {

        }

    }

    void sendInfo() {
        String InfoMessage = "### p2p " + this.IPaddress + " " + this.portNum + " " + this.UserName;
        PewChatClient.frame.client.SendMessage(InfoMessage);
    }

    void SendMessage(String message) {
        try {
            output.writeUTF(message);
        } catch (IOException ex) {

        }

    }

    @Override
    public void run() {
        try {
            String message = input.readUTF();
            MessageHistory.append("\n" + this.UserName + ": " + message);

        } catch (IOException ex) {

        }

    }

}
