package com.example.tatirs1.sntimentapp;

import android.provider.Telephony;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static java.lang.Thread.sleep;


public class MainActivity extends AppCompatActivity {

    private Map<String,Object> context = new HashMap<>();
    private Button sendButton;
    private EditText textBox;
    private ListView lstView;
    private ChatMessage responseMsg;
    private CustomAdapter adapter;
    private String textMessage;

    private ConversationService _ConvService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendButton = (Button)findViewById(R.id.send);
        textBox = (EditText)findViewById(R.id.msg);
        lstView = (ListView)findViewById(R.id.msgview);

        adapter = new CustomAdapter(this.getApplicationContext(),R.layout.right);

        lstView.setAdapter(adapter);

        _ConvService = new ConversationService(ConversationService.VERSION_DATE_2016_07_11);
        _ConvService.setUsernameAndPassword("1c9b843e-1ca5-4fd4-8950-25b0e742887b", "uCqOeuKPSbIF");

        sendButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View args) {
                textMessage = textBox.getText().toString();
                adapter.add(new ChatMessage(textMessage, false));
                textBox.setText("");

                Thread thread = new Thread(new Runnable() {
                    public void run() {
                        try{
                            //adapter.add(new ChatMessage("Hello, thanks for reaching us!\n How can i help you today?",true) );

                            //ConversationService service = new ConversationService(ConversationService.VERSION_DATE_2016_07_11);
                            //service.setUsernameAndPassword("1c9b843e-1ca5-4fd4-8950-25b0e742887b", "uCqOeuKPSbIF");

                            //Send the text from uset to watson

                            MessageRequest newMessage = new MessageRequest.Builder().inputText(textMessage).context(context).build();
                            MessageResponse response = _ConvService.message("e0119e04-486d-4d21-b90a-e3309742888c", newMessage).execute();

                            //Passing Context of last conversation
                            if(response.getContext() !=null)
                            {
                                context.clear();
                                context = response.getContext();
                                String isEndOfChat = (String) context.get("end_of_chat");
                                if (isEndOfChat != null && isEndOfChat.equals("1"))
                                {
                                    runOnUiThread(new Runnable() {
                                        public void run() {
                                            ChatMessage endMessage = new ChatMessage("Your session has ended...", true);
                                            adapter.add(endMessage);
                                        }
                                    });
                                   sendButton.setEnabled(false);

                                  return;
                                }



                            }
                            // Now get the response for the message
                            if (response != null) {
                                if (response.getOutput() != null && response.getOutput().containsKey("text")) {

                                    ArrayList responseList = (ArrayList) response.getOutput().get("text");
                                    if (null != responseList && responseList.size() > 0) {
                                        Iterator itr = responseList.iterator();
                                        String respMsg = "";
                                        while(itr.hasNext())
                                        {
                                            respMsg = respMsg + "\n" + itr.next();
                                        }

                                        responseMsg = new ChatMessage(respMsg, true);


                                    }
                                }
                            }
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    adapter.add(responseMsg);
                                }
                            });
                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }
                    //till here

                });
                thread.start();

            }
        });
    }
}

