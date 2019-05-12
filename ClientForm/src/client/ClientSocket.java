package client;

import client.frames.ClientFrame;
import client.frames.SummaryFrame;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import toneanalyzer.model.Emotion;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class ClientSocket {

    private static final String CMD_USERNAME = "#cmd_username:";
    private static final String CMD_ONLINE_USERS = "#cmd_online_users:";
    private static final String CMD_USERS_SUMMARY_EMOTION = "#cmd_users_summary_emotion:";
    private static final String CMD_SEND_AVERAGE_EMOTION_FOR_ALL_USERS = "#cmd_send_average_emotion_for_all_users:";
    private static final String CMD_SEND_MESSAGE_FOR_HISTORY = "#cmd_send_message_for_history:";

    private static final String COMMA_SEPARATOR = ",";

    private Socket socket = null;
    private DataInputStream input = null;
    private DataOutputStream out = null;
    Thread readThread;
    List<String> Messages = new ArrayList<>();
    List<String> UsersStatus = new ArrayList<>();
    List<String> UsersMaxEmotion = new ArrayList<>();
    boolean newMessage = false;
    public String status = "";
    public Boolean isConnected = false;
    public static Map<String, String> OtherUserStatus = new HashMap<String, String>();
    public boolean UserStatusChanged = false;

    ClientFrame parent;

    private String chatHistoryForFile = "";

    public DataInputStream getInput() {
        return input;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public ClientSocket(String address, int port, ClientFrame clientFrame) {
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
            System.out.println("ClientSocket host exception " + u);
        } catch (IOException i) {
            System.out.println("ClientSocket io exception " + i);
        }
    }

    public void ReadMessage() {
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
                                UsersMaxEmotion.add(entry.getKey() + "[" + entry.getValue() + "]");
                            }
                        } else if(msg.contains(CMD_SEND_AVERAGE_EMOTION_FOR_ALL_USERS)) {
                            msg = msg.replaceAll(CMD_SEND_AVERAGE_EMOTION_FOR_ALL_USERS, "");
                            Gson gson = new Gson();
                            Map<String, Map<Emotion, Double>> averageEmotionForAllUsers = gson.fromJson(msg, Map.class);
                            new SummaryFrame(averageEmotionForAllUsers);
                        } else if(msg.contains(CMD_SEND_MESSAGE_FOR_HISTORY)) {
                            msg = msg.replaceAll(CMD_SEND_MESSAGE_FOR_HISTORY, "");
                            chatHistoryForFile += msg + "\n";
                        } else {
                            Messages.add(msg);
                        }

                        String chatHistory = "";
                        String usersStatus = "";
                        String maxEmotionForUsers = "";
                        for (String message : Messages) {
                            chatHistory += parent.convertMessageToHtml(message);
                        }
                        for (String emotion : UsersMaxEmotion) {
                            maxEmotionForUsers += parent.convertMessageToHtml(emotion);
                        }

                        parent.setChatComponentText(chatHistory);
                        parent.setStatusAreaText(usersStatus);
                        parent.setMaxUsersEmotionPaneText(maxEmotionForUsers);
                        parent.setUsersStatusArea(UsersStatus);
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

    public String getChatHistoryForFile() {
        return chatHistoryForFile;
    }

    public void SendMessage(String message) {
        try {
            // write on the output stream
            out.writeUTF(message);
        } catch (IOException e) {
            System.out.println("Io exception in sending message " + e);
            e.printStackTrace();
        }

    }

    public void closeConnection() {
        try {
            out.close();
            input.close();
            socket.close();
        } catch (IOException ex) {
            System.out.println("Error while trying to close connection " + ex);
        }
    }

}
