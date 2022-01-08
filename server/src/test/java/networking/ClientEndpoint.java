package networking;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import java.io.IOException;

@javax.websocket.ClientEndpoint
public class ClientEndpoint {
    private CloseReason closeReason;
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        this.closeReason = closeReason;
    }

    public boolean isOpen() {
        return session.isOpen();
    }

    public CloseReason getCloseReason() {
        return closeReason;
    }

    public void send(char c) throws IOException {
        session.getBasicRemote().sendText(Character.toString(c));
    }
}
