package networking;

import javax.websocket.OnOpen;
import javax.websocket.Session;

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
}
