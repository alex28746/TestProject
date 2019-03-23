package pewchatserver;

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
        server.run();
    }
    
}
