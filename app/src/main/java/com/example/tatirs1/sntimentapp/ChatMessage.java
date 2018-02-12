package com.example.tatirs1.sntimentapp;

/**
 * Created by tatirs1 on 5/30/2017.
 */

public class ChatMessage {

    public String message;
    public boolean isResponse;

    public ChatMessage(String msg, boolean isResp)
    {
        this.message = msg;
        this.isResponse = isResp;
    }

}
