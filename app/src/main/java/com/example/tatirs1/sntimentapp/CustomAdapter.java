package com.example.tatirs1.sntimentapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tatirs1 on 5/30/2017.
 */

public class CustomAdapter extends ArrayAdapter<ChatMessage> {


    private TextView chatText;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private Context context;

    public CustomAdapter(Context context, int resourceID)
    {
        super(context,resourceID);

    }

    @Override

    public void add(ChatMessage obj)
    {
        chatMessageList.add(obj);
        super.add(obj);
    }

    public ChatMessage getItem(int indexMsg)
    {
        return chatMessageList.get(indexMsg);
    }


    public View getView(int position, View currentView, ViewGroup parent)
    {
        ChatMessage msg = getItem(position);
        View row = currentView;
        LayoutInflater inflater = (LayoutInflater)this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (msg.isResponse)
        {
            row = inflater.inflate(R.layout.left,parent,false);
        }
        else
        {
            row = inflater.inflate(R.layout.right,parent,false);
        }
        chatText = (TextView)row.findViewById(R.id.msgr);
        chatText.setText(msg.message);
        return row;

    }
}
