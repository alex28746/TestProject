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
                    this.isOnline = false;
                    this.socket.close();
                    break;
                }

                // break the string into message and recipient part
//                StringTokenizer st = new StringTokenizer(received, "#");
//                String MsgToSend = st.nextToken();
//                String recipient = st.nextToken();
                EmotionModel emotionModel = new EmotionModel();

                if (message.length() > 0) {
                    emotionModel = watsonService.getEmotion(message);
                }

                System.out.println(name + " is sending: " + message);

                for (int i = 0; i < ToneAnalyzerApp.users.size(); i++) {
                    ToneAnalyzerApp.users.get(i).outputStream.writeUTF(name + " : " + emotionModel.getDisplayName());
                }
            } catch (IOException e) {
                System.err.println(e);
            }

        }
        try {
            // closing resources
            this.inputStream.close();
            this.outputStream.close();

        } catch (IOException e) {
            System.err.println(e);
        }
    }

}
