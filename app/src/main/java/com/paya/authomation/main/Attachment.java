package com.paya.authomation.main;

import android.graphics.Bitmap;

/**
 * Created by Administrator on 07/17/2016.
 */
public class Attachment
{

    private String AttachmentId, fileName,MimeType;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;

    public String getMimeType() {
        return MimeType;
    }

    public void setMimeType(String mimeType) {
        MimeType = mimeType;
    }



    public String getAttachmentId() {
        return AttachmentId;
    }

    public String getFileName() {
        return fileName;
    }



    public void setAttachmentId(String attachmentId) {
        AttachmentId = attachmentId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }




}
