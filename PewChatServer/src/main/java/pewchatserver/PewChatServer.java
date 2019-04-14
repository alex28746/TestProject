package pewchatserver;

import com.ibm.cloud.sdk.core.service.security.IamOptions;
import com.ibm.watson.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.tone_analyzer.v3.model.ToneOptions;

import java.io.IOException;
import java.util.ArrayList;

public class PewChatServer {

    /**
     * @param args the command line arguments
     */
    static ArrayList<User> users = new ArrayList<User>();
    
    public static void main(String[] args) throws IOException {
        Server server = new Server("localhost", 9999);
        Thread t = new Thread(server);
        init();
        server.run();
    }

    public static void init() {
        System.out.println("init()");
        IamOptions options = new IamOptions.Builder()
                .apiKey("DTSfHsv5c3YNjWXceqb68GhNJBvDQFjqbvnnopzh6riI")
                .build();

        ToneAnalyzer toneAnalyzer = new ToneAnalyzer("2017-09-21", options);
        // toneAnalyzer.setUsernameAndPassword("alex28746@gmail.com","sej974raM");
        toneAnalyzer.setEndPoint("https://gateway-lon.watsonplatform.net/tone-analyzer/api");

        String text = "I am sadness!";

        ToneOptions toneOptions = new ToneOptions.Builder()
                .text(text)
                .build();

        ToneAnalysis toneAnalysis = toneAnalyzer.tone(toneOptions).execute().getResult();
        System.out.println(toneAnalysis);
        System.out.println("done");
    }
    
}
