package chat;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
@ServerEndpoint("/endpoint")
public class ChatWebSocket {
    private ChatHelper chatHelper;
    public ChatWebSocket(){
        this.chatHelper = new ChatHelper();
    }
    @OnOpen
    public void onOpen(Session session){
        System.out.println("Open connection: " + session.getId());
        try {
            session.getBasicRemote().sendText("Your Session Id is: " + session.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Thread t1 = new Thread(this.chatHelper,session.getId());
        t1.start();
        ChatHelper.sessionMap.put(session.getId(), session);
    }
    @OnClose
    public void onClose(Session session){
        System.out.println("Closing connection: " + session.getId());
        try {
            session.getBasicRemote().sendText("Your Session with SessionId " + session.getId() + " is closed");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ChatHelper.sessionMap.remove(session.getId());
    }
    @OnMessage
    public void onMessage(String message, Session session)  {
        System.out.println("Received message from: " + session.getId() + " Message is " + message );
        chatHelper.sendMessage(message, session.getId());
    }
}