package toneanalyzer;

import toneanalyzer.model.Emotion;
import toneanalyzer.model.EmotionModel;
import toneanalyzer.model.EmotionSummaryModel;
import toneanalyzer.service.WatsonService;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.*;

public class UserListener implements Runnable {

    private static final String CMD_SEND_USERNAME = "#cmd_username:";
    private static final String CMD_ONLINE_USERS = "#cmd_online_users:";
    private static final String CMD_USERS_SUMMARY_EMOTION = "#cmd_users_summary_emotion:";
    private static final String CMD_GET_AVERAGE_EMOTION_FOR_ALL_USERS = "#cmd_get_average_emotion_for_all_users";
    private static final String CMD_SEND_AVERAGE_EMOTION_FOR_ALL_USERS = "#cmd_send_average_emotion_for_all_users:";
    private static final String CMD_SEND_MESSAGE_FOR_HISTORY = "#cmd_send_message_for_history:";

    private static final String COMMA_SEPARATOR = ",";

    private static Set<String> onlineUsers = new HashSet<>();
    private static Map<String, EmotionSummaryModel> usersSummaryEmotion = new HashMap<>();

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
                        System.out.println(name + " disconnected from chat!");

                        onlineUsers.remove(name);
                        //return max emotion for all users
                        Map<String, Emotion> maxEmotionForAllUsers = getMaxEmotionForAllUsers(usersSummaryEmotion);

                        for (int i = 0; i < ToneAnalyzerApp.users.size(); i++) {
                            ToneAnalyzerApp.users.get(i).outputStream.writeUTF(this.name + " disconnected!");
                            ToneAnalyzerApp.users.get(i).outputStream.writeUTF(CMD_ONLINE_USERS + prepareUsersStatusForSending(onlineUsers));
                            ToneAnalyzerApp.users.get(i).outputStream.writeUTF(CMD_USERS_SUMMARY_EMOTION + maxEmotionForAllUsers);
                        }
                        this.isOnline = false;
                    closeConnection();
                    break;
                } else if (message.contains("#####")) {
                    System.out.println(name + " connected to chat!");

                    this.name = message.replace("#####", "");
                    onlineUsers.add(name);
                    usersSummaryEmotion.put(name, new EmotionSummaryModel());

                    Map<String, Emotion> maxEmotionForAllUsers = getMaxEmotionForAllUsers(usersSummaryEmotion);

                    for (int i = 0; i < ToneAnalyzerApp.users.size(); i++) {
                        ToneAnalyzerApp.users.get(i).outputStream.writeUTF(CMD_SEND_USERNAME + this.name);
                        ToneAnalyzerApp.users.get(i).outputStream.writeUTF(CMD_ONLINE_USERS + prepareUsersStatusForSending(onlineUsers));
                        ToneAnalyzerApp.users.get(i).outputStream.writeUTF(CMD_USERS_SUMMARY_EMOTION + maxEmotionForAllUsers);
                    }
                } else if(message.contains(CMD_GET_AVERAGE_EMOTION_FOR_ALL_USERS)) {
                    Map<String, Map<Emotion, Double>> averageEmotionForAllUsers = getAverageEmotionForAllUsers(usersSummaryEmotion);
                    for (int i = 0; i < ToneAnalyzerApp.users.size(); i++) {
                        outputStream.writeUTF(CMD_SEND_AVERAGE_EMOTION_FOR_ALL_USERS + averageEmotionForAllUsers);
                    }
                } else {
                    EmotionModel emotionModel = new EmotionModel();
                    if (message.length() > 0) {
                        // send message to IBM tone analyzer service
                        emotionModel = watsonService.getEmotion(message);
                    }
                    System.out.println(name + " is sending: " + message);
                    String preparedEmotionModel = prepareEmotionModelForSending(emotionModel);
                    Map<String, Emotion> maxEmotionForAllUsers = getMaxEmotionForAllUsers(usersSummaryEmotion);
                    for (int i = 0; i < ToneAnalyzerApp.users.size(); i++) {
                        ToneAnalyzerApp.users.get(i).outputStream.writeUTF(name + " : " + message + preparedEmotionModel);
                        ToneAnalyzerApp.users.get(i).outputStream.writeUTF(CMD_SEND_MESSAGE_FOR_HISTORY + name + " : " + message + "[" + emotionModel.getDisplayName() + " | accuracy: " + emotionModel.getScore() + "]");
                        ToneAnalyzerApp.users.get(i).outputStream.writeUTF(CMD_USERS_SUMMARY_EMOTION + maxEmotionForAllUsers);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error when try to read input stream " + e);
                closeConnection();
                this.isOnline = false;
                break;
            }
        }
        closeConnection();
    }

    private Map<String, Map<Emotion, Double>> getAverageEmotionForAllUsers(Map<String, EmotionSummaryModel> emotionSummaryModelMap) {
        Map<String, Map<Emotion, Double>> result = new HashMap<>();

        for (String key: emotionSummaryModelMap.keySet()) {
            Map<Emotion, Double> emotionAverageMap = emotionSummaryModelMap.get(key).getAverage();
            result.put(key, emotionAverageMap);
        }
        return result;
    }

    private Map<String, Emotion> getMaxEmotionForAllUsers(Map<String, EmotionSummaryModel> emotionSummaryModelMap) {
        Map<String, Emotion> result = new HashMap<>();

        for (String key: emotionSummaryModelMap.keySet()) {
            result.put(key, emotionSummaryModelMap.get(key).getMax());
        }
        return result;
    }

    private String prepareUsersStatusForSending(Set<String> statuses) {
        return String.join(COMMA_SEPARATOR, statuses);
    }

    private String prepareEmotionModelForSending(EmotionModel emotionModel) {
        EmotionSummaryModel emotionSummaryModel = usersSummaryEmotion.get(this.name);

        if(emotionSummaryModel == null) {
            emotionSummaryModel = new EmotionSummaryModel();
        }

        if(emotionModel == null) {
            emotionModel = new EmotionModel();
        }

        if(emotionModel.getDisplayName() == null) {
            emotionModel.setDisplayName("unknown");
        }

        switch (emotionModel.getDisplayName().toLowerCase()) {
            case "analytical": {
                emotionSummaryModel.incrementAnalytical();
                break;
            }
            case "anger": {
                emotionSummaryModel.incrementAnger();
                break;
            }
            case "fear": {
                emotionSummaryModel.incrementFear();
                break;
            }
            case "joy": {
                emotionSummaryModel.incrementJoy();
                break;
            }
            case "sadness": {
                emotionSummaryModel.incrementSadness();
                break;
            }
            case "tentative": {
                emotionSummaryModel.incrementTentative();
                break;
            }
            default: {
                System.err.println("Emotion model does not exist! = " + emotionModel.getDisplayName().toLowerCase());
            }
        }
        usersSummaryEmotion.put(this.name, emotionSummaryModel);

        return "[" + emotionModel.getDisplayName() + "]";
    }

    public void closeConnection() {
        try {
            // closing resources
            this.inputStream.close();
            this.outputStream.close();
        } catch (IOException e) {
            System.err.println("Error while trying to close streams " + e);
        }
    }
}
