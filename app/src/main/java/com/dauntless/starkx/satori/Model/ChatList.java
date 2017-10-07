package com.dauntless.starkx.satori.Model;

/**
 * Created by sonu on 7/10/17.
 */

public class ChatList {
    public String chatUrl = "";
    public String receiverName = "";
    public String receiverNumber = "";

    public ChatList(String chatUrl , String receiverName , String receiverNumber) {
        this.chatUrl = chatUrl;
        this.receiverName = receiverName;
        this.receiverNumber = receiverNumber;
    }
}
