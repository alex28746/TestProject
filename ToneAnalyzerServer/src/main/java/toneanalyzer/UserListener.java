package toneanalyzer;

import toneanalyzer.model.EmotionModel;
import toneanalyzer.service.WatsonService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class UserListener implements Runnable {

    Scanner scan = new Scanner(System.in);
    String name;
    Socket socket;
    DataInputStream inputStream;
    DataOutputStream outputStream;
    WatsonService watsonService;
    boolean isOnline;

    public UserListener(Socket socket, String username, DataInputStream inputStream, DataOutputStream outputStream, WatsonService watsonService) {
        this.name = username;
        this.socket = socket;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.isOnline = true;
        this.watsonService = watsonService;
    }

    @Override
    public void run() {
        String message;
        while (true) {
            try {
                // receive the string
                message = inputStream.readUTF();
                System.out.println(message);
                if (message.equals("logout")) {
                    System.out.println(name + " disconnected!");
                    for (int i = 0; i < ToneAnalyzerApp.users.size(); i++) {
                        ToneAnalyzerApp.users.get(i).outputStream.writeUTF(this.name + " disconnected!");
                    }
                    this.isOnline = false;
                    closeConnection();
                    break;
                } else if (message.contains("#####")) {
                    this.name = message.replace("#####", "");
                    System.out.println(name + " connected to chat!");
                    for (int i = 0; i < ToneAnalyzerApp.users.size(); i++) {
                        ToneAnalyzerApp.users.get(i).outputStream.writeUTF(this.name + " connected to chat!");
                    }
                } else {
                    EmotionModel emotionModel = new EmotionModel();
                    if (message.length() > 0) {
                        emotionModel = watsonService.getEmotion(message);
                    }
                    System.out.println(name + " is sending: " + message);
                    for (int i = 0; i < ToneAnalyzerApp.users.size(); i++) {
                        System.out.println("Sended to another person model");
                        ToneAnalyzerApp.users.get(i).outputStream.writeUTF(name + " : " + message + prepareEmotionModelForSending(emotionModel));
                    }
                }
            } catch (IOException e) {
                System.err.println("Error when try to reading input stream " + e);
                closeConnection();
                this.isOnline = false;
                break;
            }
        }
        closeConnection();
    }

    private String prepareEmotionModelForSending(EmotionModel emotionModel) {
        return "|[" + emotionModel.getDisplayName() + "]";
    }

    public void closeConnection() {
        try {
            // closing resources
            this.scan.close();
            this.inputStream.close();
            this.outputStream.close();
            this.scan.close();
        } catch (IOException e) {
            System.err.println("Error while trying to close streams " + e);
        }
    }
}
