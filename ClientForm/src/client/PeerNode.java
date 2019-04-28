package client;

import java.io.*;
import java.net.*;

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
            System.out.println("PeerNode2 exception " + ex);
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
            System.out.println("Peer node exception " + ex);
        }

    }

    void sendInfo() {
        String InfoMessage = "### p2p " + this.IPaddress + " " + this.portNum + " " + this.UserName;
        ClientRunner.frame.client.SendMessage(InfoMessage);
    }

    void SendMessage(String message) {
        try {
            output.writeUTF(message);
        } catch (IOException ex) {
            System.out.println("Send message exception " + ex);
        }

    }

    @Override
    public void run() {
        try {
            String message = input.readUTF();
            MessageHistory.append("\n" + this.UserName + ": " + message);

        } catch (IOException ex) {
            System.out.println("Run exception " + ex);
        }

    }

}
