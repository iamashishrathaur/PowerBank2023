package com.rathaur.earning;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.security.NetworkSecurityPolicy;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.rathaur.earning.Java.MessageModal;
import com.rathaur.earning.Java.MessageRVAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class ChatBot extends AppCompatActivity {
    private RecyclerView chatsRV;
    private ImageView sendMsgIB;
    private EditText userMsgEdt;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";
    private ArrayList<MessageModal> messageModalArrayList;
    private MessageRVAdapter messageRVAdapter;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot);
        Objects.requireNonNull(getSupportActionBar()).hide();
        chatsRV = findViewById(R.id.idRVChats);
        sendMsgIB = findViewById(R.id.idIBSend);
        userMsgEdt = findViewById(R.id.idEdtMessage);
        NetworkSecurityPolicy.getInstance().isCleartextTrafficPermitted();
        messageModalArrayList = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(ChatBot.this);
        mRequestQueue.getCache().clear();


        sendMsgIB.setOnClickListener(v -> {
            if (userMsgEdt.getText().toString().isEmpty()) {
                Toast.makeText(ChatBot.this, "Please enter your message..", Toast.LENGTH_SHORT).show();
                return;
            }
            sendMessage(userMsgEdt.getText().toString());
            userMsgEdt.setText("");
        });


        messageRVAdapter = new MessageRVAdapter(messageModalArrayList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatBot.this, RecyclerView.VERTICAL, false);
        chatsRV.setLayoutManager(linearLayoutManager);
        chatsRV.setAdapter(messageRVAdapter);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void sendMessage(String userMsg) {

        messageModalArrayList.add(new MessageModal(userMsg, USER_KEY));
        messageRVAdapter.notifyDataSetChanged();

        String url = "http://api.brainshop.ai/get?bid=176144&key=wZVaJiZtDv349tv2&uid=[uid]&msg="+userMsg;

        RequestQueue queue = Volley.newRequestQueue(ChatBot.this);

        @SuppressLint("NotifyDataSetChanged")
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                String botResponse = response.getString("cnt");
                messageModalArrayList.add(new MessageModal(botResponse, BOT_KEY));
                messageRVAdapter.notifyDataSetChanged();
                chatsRV.scrollToPosition(messageModalArrayList.size()-1);
            } catch (JSONException e) {
                e.printStackTrace();

                messageModalArrayList.add(new MessageModal("No response", BOT_KEY));
                messageRVAdapter.notifyDataSetChanged();
            }
        }, error -> messageModalArrayList.add(new MessageModal("Sorry no response found", BOT_KEY)));

        queue.add(jsonObjectRequest);
    }
}