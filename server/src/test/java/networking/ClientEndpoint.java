package networking;

import javax.websocket.*;
import java.io.IOException;

@javax.websocket.ClientEndpoint
public class ClientEndpoint {
    private CloseReason closeReason;
    private Session session;
    private String lastMessage;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        this.closeReason = closeReason;
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        lastMessage = message;
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

    public String getLastMessage() {
        return lastMessage;
    }
}
