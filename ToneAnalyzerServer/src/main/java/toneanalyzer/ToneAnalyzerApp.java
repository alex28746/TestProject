package toneanalyzer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import toneanalyzer.service.WatsonService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@SpringBootApplication
public class ToneAnalyzerApp {

    static ArrayList<UserListener> users = new ArrayList<UserListener>();

    @Autowired
    public Server server;

    @Autowired
    public WatsonService watsonServiceImpl;

    public static void main(String[] args) {
        SpringApplication.run(ToneAnalyzerApp.class, args);
    }

    @PostConstruct
    public void init() {
        Thread t = new Thread(server);
        server.run();
    }
}
