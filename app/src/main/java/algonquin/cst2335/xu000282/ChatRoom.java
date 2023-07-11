package algonquin.cst2335.xu000282;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.xu000282.data.ChatMessage;
import algonquin.cst2335.xu000282.data.ChatMessageDAO;
import algonquin.cst2335.xu000282.data.ChatRoomViewModel;
import algonquin.cst2335.xu000282.data.MessageDatabase;
import algonquin.cst2335.xu000282.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.xu000282.databinding.ReceiveMessageBinding;
import algonquin.cst2335.xu000282.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ChatRoomViewModel chatModel;
    ArrayList<ChatMessage> messages;
    ActivityChatRoomBinding binding;

    ChatMessageDAO mDAO;

    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        if (messages == null) {
            messages = new ArrayList<>();
            chatModel.messages.setValue(messages);
        }



        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        mDAO = db.cmDAO();

        binding.sendButton.setOnClickListener(click -> {
            String messageText = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(messageText, currentDateAndTime, true);
            messages.add(chatMessage);

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage); // Insert message into database
            });


            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click -> {
            String messageText = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(messageText, currentDateAndTime, false);
            messages.add(chatMessage);
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                mDAO.insertMessage(chatMessage); // Insert message into database
            });
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
        });

        binding.recycleView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                if(viewType == 0) {
                    SentMessageBinding binding = SentMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }else {
                    ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);
                    return new MyRowHolder(binding.getRoot());
                }
            }

            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position){
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
                holder.timeText.setText(obj.getTimeSent());
            }

            @Override
            public int getItemCount() {
                return messages.size();
            }

            @Override
            public  int getItemViewType(int position){
                ChatMessage message = messages.get(position);
                if(message.isSentButton()){
                    return 0;
                }else return 1;
            }
        });

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    public class MyRowHolder extends RecyclerView.ViewHolder {
        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk ->{
                int position = getAbsoluteAdapterPosition();


                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message:" + messageText.getText());
                builder.setTitle("Question:");
                builder.setNegativeButton("No",(dialog, cl) -> {});
                builder.setPositiveButton("Yes",(dialog,cl) -> {
                    Executor thread = Executors.newSingleThreadExecutor();
                    thread.execute(() -> {
                        ChatMessage m = messages.get(position);
                        mDAO.deleteMessage(m);
                        runOnUiThread(() -> {
                            messages.remove(position);
                            myAdapter.notifyItemRemoved(position);

                            Snackbar.make(messageText,"You deleted message #"+ position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", c ->{
                                        messages.add(position,m);
                                        myAdapter.notifyItemInserted(position);
                                    })
                                    .show();
                        });
                    });
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}