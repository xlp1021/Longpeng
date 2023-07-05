package algonquin.cst2335.xu000282;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.xu000282.data.ChatMessage;
import algonquin.cst2335.xu000282.data.ChatRoomViewModel;
import algonquin.cst2335.xu000282.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.xu000282.databinding.ActivityMainBinding;
import algonquin.cst2335.xu000282.databinding.ReceiveMessageBinding;
import algonquin.cst2335.xu000282.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {

    ChatRoomViewModel chatModel;
    ArrayList<ChatMessage> messages;
    ActivityChatRoomBinding binding;

    private RecyclerView.Adapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        if (messages == null) {
            messages = new ArrayList<>();
            chatModel.messages.setValue(messages);
        }

        messages = chatModel.messages.getValue();

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.sendButton.setOnClickListener(click -> {
            String messageText = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(messageText, currentDateandTime, true);
            messages.add(chatMessage);
            myAdapter.notifyItemInserted(messages.size() - 1);
            binding.textInput.setText("");
        });

        binding.receiveButton.setOnClickListener(click -> {
            String messageText = binding.textInput.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateAndTime = sdf.format(new Date());
            ChatMessage chatMessage = new ChatMessage(messageText, currentDateAndTime, false);
            messages.add(chatMessage);
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
            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);
        }
    }
}}