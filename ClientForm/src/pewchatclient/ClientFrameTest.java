package pewchatclient;

import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class ClientFrameTest {

    private static final String NORMAL_MESSAGE = "Hello, world!";
    private static final String IMG_MESSAGE = "Hello, I am sadness |[Sadness]";

    private static final String EXPECTED_NORMAL_MESSAGE = "<p>Hello, world!</p><br/>";
    private static final String EXPECTED_IMG_MESSAGE = "<img src='file:/C:/Home/java/java-chat/TestProject/ClientForm/ClientForm/images/sadness.jpg' width='20' height='20'/>&nbsp;&nbsp;Hello, I am sadness <br>";


    @Test
    public void convertMessageToHtml() {
        // given
        ClientFrame clientFrame = new ClientFrame();
        // when
        String normalMessage = clientFrame.convertMessageToHtml(NORMAL_MESSAGE);
        String imgMessage = clientFrame.convertMessageToHtml(IMG_MESSAGE);
        // then
        assertEquals(EXPECTED_NORMAL_MESSAGE, normalMessage);
        assertEquals(EXPECTED_IMG_MESSAGE, imgMessage);
    }
}