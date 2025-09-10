import com.application.network.Client;
import com.application.window.Window;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        Window window = new Window("Example project", 800, 600);
        Client client = new Client();
        window.wait();
        client.close();
    }
}
