package networking;

import javax.websocket.CloseReason;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;

@javax.websocket.ClientEndpoint
public class ClientEndpoint {
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    public boolean isOpen() {
        return session.isOpen();
    }

    public void send(char c) throws IOException {
        session.getBasicRemote().sendText(Character.toString(c));
    }
}
