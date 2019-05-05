package client;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class MyClient {

    private static final String CMD_USERNAME = "#cmd_username:";
    private static final String CMD_ONLINE_USERS = "#cmd_online_users:";
    private static final String CMD_USERS_SUMMARY_EMOTION = "#cmd_users_summary_emotion:";

    private static final String COMMA_SEPARATOR = ",";

    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    Scanner scn = new Scanner(System.in);
    Thread readThread;
    List<String> Messages = new ArrayList<>();
    List<String> UsersStatus = new ArrayList<>();
    List<String> UsersMaxEmotion = new ArrayList<>();
    boolean newMessage = false;
    String status = "";
    Boolean isConnected = false;
    static Map<String, String> OtherUserStatus = new HashMap<String, String>();
    boolean UserStatusChanged = false;

    ClientFrame parent;

    public DataInputStream getInput() {
        return input;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public MyClient(String address, int port, ClientFrame clientFrame) {
        try {
            System.out.println("Connected");
            socket = new Socket(address, port);
            // takes input from terminal
            input = new DataInputStream(socket.getInputStream());

            // sends output to the socket
            out = new DataOutputStream(socket.getOutputStream());
            isConnected = true;

            this.parent = clientFrame;

        } catch (UnknownHostException u) {
            System.out.println("MyClient host exception " + u);
        } catch (IOException i) {
            System.out.println("MyClient io exception " + i);
        }
    }

    void ReadMessage() {
        System.out.println("ReadMessage called.");
        readThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println("ReadMessage while called.");

                        // read the message sent to this client
                        String msg = input.readUTF();

                        System.out.println("Message received: " + msg);

                        if (msg.contains(CMD_USERNAME)) {
                            String username = msg.replace(CMD_USERNAME, "");
                            Messages.add(username + " connected to chat!");
                        } else if (msg.contains(CMD_ONLINE_USERS)) {
                            String onlineUsers = msg.replace(CMD_ONLINE_USERS, "");
                            String[] onlineUsersArray = onlineUsers.split(COMMA_SEPARATOR);

                            UsersStatus.clear();
                            for (String onlineUser : onlineUsersArray) {
                                UsersStatus.add(onlineUser + "\n");
                            }
                        } else if (msg.contains(CMD_USERS_SUMMARY_EMOTION)) {
                            String usersMaxEmotion = msg.replace(CMD_USERS_SUMMARY_EMOTION, "");

                            JsonParser parser = new JsonParser();
                            JsonObject jsonObject = parser.parse(usersMaxEmotion).getAsJsonObject();

                            UsersMaxEmotion.clear();
                            for (Map.Entry<String, JsonElement> entry : jsonObject.entrySet()) {
                                UsersMaxEmotion.add(entry.getKey() + "|[" + entry.getValue() + "]");
                            }
                        } else {
                            Messages.add(msg);
                        }

                        String chatHistory = "";
                        String usersStatus = "";
                        String maxEmotionForUsers = "";
                        for (String message : Messages) {
                            chatHistory += parent.convertMessageToHtml(message);
                        }
                        for (String status : UsersStatus) {
                            usersStatus += status;
                        }
                        for (String emotion : UsersMaxEmotion) {
                            maxEmotionForUsers += parent.convertMessageToHtml(emotion);
                        }

                        parent.setChatComponentText(chatHistory);
                        parent.setStatusAreaText(usersStatus);
                        parent.setMaxUsersEmotionPaneText(maxEmotionForUsers);
                        System.out.println("Messages STRING: " + Messages);
                    }
                } catch (IOException e) {
                    System.out.println("IO Exception while tried to read message " + e);
                    e.printStackTrace();
                } catch (Exception ex) {
                    System.out.println("Normal exception while tried to read message " + ex);
                    ex.printStackTrace();
                }
                System.out.println("HashMap in in the end of the thread in readMessage size " + OtherUserStatus.size());
            }
        });
        System.out.println("HashMap in the end of readMessage size " + OtherUserStatus.size());
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

    void closeConnection() {
        try {
            out.close();
            input.close();
            socket.close();
            scn.close();
        } catch (IOException ex) {
            System.out.println("Error while trying to close connection " + ex);
        }
    }

}
