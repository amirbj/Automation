package com.paya.authomation.main;

import java.util.ArrayList;


public class Letters {

    private String RecieveAt;
    private String Subject;
    private String Code;
    private String Refertype;
    private String Sender;
    private Boolean hasworkflow, hasAttachment, isUnread;
    private String Title;
    private String ID, MessageId;
    private ArrayList<String> Itemtitle, ItemId;

    public String getMessageId() {
        return MessageId;
    }

    public void setMessageId(String messageId) {
        MessageId = messageId;
    }




    public String getRecieveAt() {
        return RecieveAt;
    }

    public void setRecieveAt(String recieveAt) {
        RecieveAt = recieveAt;
    }



    public Boolean getUnread() {
        return isUnread;
    }

    public void setUnread(Boolean unread) {
        isUnread = unread;
    }



    public Boolean getHasAttachment() {
        return hasAttachment;
    }

    public void setHasAttachment(Boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }



    public ArrayList<String> getItemtitle() {
        return Itemtitle;
    }

    public ArrayList<String> getItemId() {
        return ItemId;
    }

    public void setItemtitle(ArrayList<String> itemtitle) {
        Itemtitle = itemtitle;
    }

    public void setItemId(ArrayList<String> itemId) {
        ItemId = itemId;
    }




    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }








    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }



    public Boolean getHasworkflow() {
        return hasworkflow;
    }

    public void setHasworkflow(Boolean hasworkflow) {
        this.hasworkflow = hasworkflow;
    }



    public void setSubject(String subject) {
        Subject = subject;
    }



    public void setCode(String code) {
        Code = code;
    }

    public void setRefertype(String refertype) {
        Refertype = refertype;
    }

    public void setSender(String sender) {
        Sender = sender;
    }


    public String getSubject() {
        return Subject;
    }



    public String getCode() {
        return Code;
    }

    public String getRefertype() {
        return Refertype;
    }

    public String getSender() {
        return Sender;
    }


}
