package com.paya.authomation.main;

import android.graphics.Bitmap;

import org.json.JSONArray;

/**
 * Created by Administrator on 07/15/2016.
 */
public class DetailLetters  {

    private boolean hasAttachment;
    private boolean hasWorkflow;
    private boolean canconfirm;
    private String  CreatedBy, Body, messageId;
    private String accessRight, classId;

    public boolean isCanconfirm() {
        return canconfirm;
    }

    public void setCanconfirm(boolean canconfirm) {
        this.canconfirm = canconfirm;
    }



    public String getClassId() {
        return classId;
    }

    public String getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(String accessRight) {
        this.accessRight = accessRight;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }



    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    private String filename, subject, sender;

    private Bitmap bitmap;
    private JSONArray attachmentArray;
    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }



    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }








    public JSONArray getAttachmentArray() {
        return attachmentArray;
    }

    public void setAttachmentArray(JSONArray attachmentArray) {
        this.attachmentArray = attachmentArray;
    }









    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }










    public void setHasAttachment(boolean hasAttachment) {
        this.hasAttachment = hasAttachment;
    }

    public void setHasWorkflow(boolean hasWorkflow) {
        this.hasWorkflow = hasWorkflow;
    }


    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public void setBody(String body) {
        Body = body;
    }



    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }





    public boolean isHasAttachment() {
        return hasAttachment;
    }

    public boolean isHasWorkflow() {
        return hasWorkflow;
    }



    public String getCreatedBy() {
        return CreatedBy;
    }

    public String getBody() {
        return Body;
    }



    public String getMessageId() {
        return messageId;
    }

}
