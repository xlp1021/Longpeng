package algonquin.cst2335.xu000282.data;

public class ChatMessage {

    private final String message;
    private final String timeSent;
    private final boolean isSentButton;

    public ChatMessage(String m, String t, boolean sent){
        this.message = m;
        this.timeSent = t;
        this.isSentButton = sent;
    }

    public String getMessage() {
        return message;
    }

    public String getTimeSent() {
        return timeSent;
    }

    public boolean isSentButton() {
        return isSentButton;
    }
}
