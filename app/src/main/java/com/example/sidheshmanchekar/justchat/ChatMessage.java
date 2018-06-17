package com.example.sidheshmanchekar.justchat;

class ChatMessage {

    private String msgtxt;
    private String msguser;
    private long msgtime;

    public ChatMessage(String msgtxt, String msguser) {
        this.msgtxt = msgtxt;
        this.msguser = msguser;
        this.msgtime = msgtime;
    }

    public String getMsgtxt() {
        return msgtxt;
    }

    public void setMsgtxt(String msgtxt) {
        this.msgtxt = msgtxt;
    }

    public String getMsguser() {
        return msguser;
    }

    public void setMsguser(String msguser) {
        this.msguser = msguser;
    }

    public long getMsgtime() {
        return msgtime;
    }

    public void setMsgtime(long msgtime) {
        this.msgtime = msgtime;
    }
}
