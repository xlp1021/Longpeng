package algonquin.cst2335.xu000282.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey (autoGenerate = true)
    @ColumnInfo(name = "id")
    public long id;
    @ColumnInfo(name="message")
    protected String message;
    @ColumnInfo(name="TimeSent")
    protected String timeSent;
    @ColumnInfo(name="SendOrReceive")
    protected boolean isSentButton;

    public ChatMessage() {
        // Empty constructor required by Room database
    }

    public ChatMessage(String m, String t, boolean sent){
        this.message = m;
        this.timeSent = t;
        this.isSentButton = sent;
    }

    public String getMessage() {
        return message;
    }
    public String getTimeSent() {return timeSent;}

    public boolean isSentButton() {
        return isSentButton;
    }
}
