package client;

import client.frames.ClientFrame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientRunner {

    static ClientFrame frame;

    public static void main(String[] args) {
        frame = new ClientFrame();
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.closeFrame();
                System.exit(0);
            }
        });
        frame.setVisible(true);

    }

}
