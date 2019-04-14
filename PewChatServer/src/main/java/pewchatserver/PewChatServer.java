package pewchatserver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pewchatserver.service.WatsonService;

import javax.annotation.PostConstruct;
import java.util.ArrayList;

@SpringBootApplication
public class PewChatServer {

    static ArrayList<User> users = new ArrayList<User>();

    @Autowired
    public Server server;

    @Autowired
    public WatsonService watsonServiceImpl;

    public static void main(String[] args) {
        SpringApplication.run(PewChatServer.class, args);
    }

    @PostConstruct
    public void init() {
        watsonServiceImpl.getEmotion("sadness");
        Thread t = new Thread(server);
        server.run();
    }
}
