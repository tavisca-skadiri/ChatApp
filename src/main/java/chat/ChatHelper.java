package chat;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class ChatHelper implements Runnable{
    static HashMap<String, Session> sessionMap = new HashMap<>();
    void sendMessage(String message, String senderId) {
        Pattern pattern = Pattern.compile("@([0-9]*)(\\s*:\\s*)(.*)");
        Matcher matcher = pattern.matcher(message);
        if(matcher.find()){
            String receiverId = matcher.group(1);
            String messageToSend = matcher.group(3);
            Session receiverSession = sessionMap.get(receiverId);
            try {
                receiverSession.getBasicRemote().sendText("From " + senderId + " : " + messageToSend);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void routineMessage(String message, String name){
        try {
            sessionMap.get(name).getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        try {
            while(true){
                Thread.sleep(5000);
                this.routineMessage("Routine Message from server by thread " +
                        Thread.currentThread().getId(), Thread.currentThread().getName() );
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}