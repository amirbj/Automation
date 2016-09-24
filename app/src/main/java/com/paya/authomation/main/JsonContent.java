package com.paya.authomation.main;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 07/03/2016.
 */
public class JsonContent {
    private static final String TYPE = "Type";
    private static final String DATA = "Data";
    private static final String CODE = "Code";
    private static final String CREATED = "CreatedAt";
    private static final String WORKFLOW = "HasWorkFlow";
    private static final String TITLE = "Title";
    private static final String SENDER = "Sender";
    private static final String ID = "ID";
    private static final String ATTACHMENT = "Attachments";
    private static final String ISUNREAD = "IsUnread";
    private static final String RECEIVE = "ReceivedAt";
    private static final String MSGID = "ID";
    private static final String BODY = "Body";
    private static final String HASATTACHMENT ="HasAttachment";
    private static final String MIMETYPE ="MimeType";
    private static final String CREATEDBY ="CreatedBy";
    private static final String USER ="UserTitle";
    private static final String FILENAME ="FileName";
    private static final String SENDERUSER ="SenderUser";
    private static final String SENDERTITLE ="UserTitle";
    private static final String STATUS = "Status";
    public static List<Letters> getData(String response) {

        JSONObject type, createdat;
        JSONArray data;

        List<Letters> listLetter = new ArrayList<>();

        try {
            JSONObject jobg = new JSONObject(response);
            data = jobg.getJSONArray(DATA);
            Log.e("length of array ", "this " + data.length());
            for (int i = 0; i < data.length(); i++) {
                Letters letter = new Letters();
                JSONObject object = data.getJSONObject(i);
                type = object.optJSONObject(TYPE);
                letter.setCode(type.optString(CODE));
                letter.setTitle(object.optString(TITLE));
                letter.setID(object.optString(ID));
                letter.setHasworkflow(object.optBoolean(WORKFLOW));
                listLetter.add(letter);


            }

            return listLetter;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<Letters> kartabl(String res) {

        List<Letters> listKartabl = new ArrayList<>();

        JSONObject sender = null;
        JSONArray data;


        try {
            JSONObject obj = new JSONObject(res);
            data = obj.getJSONArray(DATA);
            Log.e("length of kartabl arr ", "this " + data.length());
            for (int i = 0; i < data.length(); i++) {
                Letters letter = new Letters();
                JSONObject object = data.getJSONObject(i);
                if (data.getJSONObject(i).has(SENDER)) {
                    sender = object.optJSONObject(SENDER);

                    letter.setSender(sender.optString(TITLE));
                }
                if (data.getJSONObject(i).has(TITLE)) {
                    letter.setSubject(object.optString(TITLE));
                }
                if (data.getJSONObject(i).has(WORKFLOW)) {
                    letter.setHasworkflow(object.optBoolean(WORKFLOW));
                }
                if (data.getJSONObject(i).has(RECEIVE)) {
                    letter.setRecieveAt(object.optString(RECEIVE));
                } else {
                    letter.setRecieveAt(object.optString(CREATED));
                }
                letter.setHasAttachment(object.optBoolean(HASATTACHMENT));
                letter.setUnread(object.optBoolean(ISUNREAD));
                letter.setMessageId(object.optString(MSGID));

                listKartabl.add(letter);

            }
            return listKartabl;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }
public static  boolean archive(String response){
    JSONObject status;
    try {
        JSONObject jsonObject = new JSONObject(response);
        status = jsonObject.getJSONObject(STATUS);
        Boolean succeed = status.optBoolean("Succeed");
        return  succeed;

    } catch (JSONException e) {
        e.printStackTrace();
        return false;
    }

}
    public static Map<DetailLetters, ArrayList<Attachment>> detail(String res) {

        ArrayList<Attachment> detailLetterses= new ArrayList<>();
        JSONObject data, createdby, attachment, senderuser, permission;
        JSONArray attachmenttarr;
        Map<DetailLetters, ArrayList<Attachment>> map = new HashMap<>();


        try {

            DetailLetters detailObj = new DetailLetters();
            JSONObject objDetail = new JSONObject(res);
            data = objDetail.getJSONObject(DATA);
            detailObj.setMessageId(data.optString(ID));
            detailObj.setBody(data.optString(BODY));
            detailObj.setHasWorkflow(data.optBoolean(WORKFLOW));
            createdby = data.getJSONObject(CREATEDBY);
            permission = data.getJSONObject("FormPermissions");
            detailObj.setCanconfirm(permission.optBoolean("CanConfirm"));
            detailObj.setCreatedBy(createdby.optString(USER));
            detailObj.setSubject(data.optString(TITLE));
            if(data.has(SENDERUSER)) {
                senderuser = data.getJSONObject(SENDERUSER);
                detailObj.setSender(senderuser.optString(SENDERTITLE));
            }
            if (data.optBoolean(HASATTACHMENT)) {
                detailObj.setHasAttachment(true);
                attachmenttarr = data.getJSONArray(ATTACHMENT);
                detailObj.setAttachmentArray(attachmenttarr);


                for (int i = 0; i < attachmenttarr.length(); i++) {

                    attachment = attachmenttarr.getJSONObject(i);
                    Attachment attachObj= new Attachment();
                    attachObj.setAttachmentId(attachment.optString(ID));
                    attachObj.setFileName(attachment.optString(FILENAME));
                    Log.e("json content ", attachment.optString(MIMETYPE));
                    attachObj.setMimeType(attachment.optString(MIMETYPE));
                    detailLetterses.add(attachObj);
                }

            } else {
                detailObj.setHasAttachment(false);

            }


            if (data.has(BODY)) {
                detailObj.setBody(data.optString(BODY));
            }
            //detailLetterses.add(detailObj);

        map.put(detailObj, detailLetterses);
         return map;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
public static List<DetailLetters> usercontent(String res){

    JSONObject classId, accessRight, data;

    DetailLetters userobj = new DetailLetters();

    try {
        JSONObject objDetail = new JSONObject(res);
        data = objDetail.getJSONObject(DATA);
        classId = data.getJSONObject("Class");
       userobj.setClassId(classId.optString("ID"));
        accessRight = data.getJSONObject("AccessRight");
        userobj.setAccessRight(accessRight.optString("ID"));
        List<DetailLetters> list = new ArrayList<>();
        list.add(userobj);
        return  list;

    } catch (JSONException e) {
        e.printStackTrace();
        return null;
    }

}

    public static ArrayList<User> getuser(String res) {
        JSONArray data;
        ArrayList<User> users = new ArrayList<>();
        try {
            JSONObject jo = new JSONObject(res);
            data = jo.getJSONArray(DATA);
        for (int i = 0; i < data.length(); i++) {
            User user = new User(0);
            JSONObject object = data.getJSONObject(i);
            Log.e("erja", object.optString(ID)+ object.optString(USER));
            user.setUserId(object.optString(ID));
            user.setUserTitle(object.optString(USER));

            users.add(user);

        }
        return users;
    } catch (JSONException e) {
        e.printStackTrace();
        return null;
    }


}

    public static  Map<String, String> getSpinnerData(String response){

        JSONArray data;

        Map<String, String> map = new HashMap<>();
        try {
            JSONObject jobj = new JSONObject(response);
            data= jobj.getJSONArray(DATA);
            for (int i = 0; i < data.length(); i++) {

                JSONObject object = data.getJSONObject(i);

                map.put(object.optString(ID),object.optString("Title"));
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
            return  null;
        }
    }
    public static Boolean doErja(String res) {
        JSONArray data;
        JSONObject status;

        try {
            JSONObject jobg = new JSONObject(res);
            if (jobg.has(DATA)) {

                data = jobg.getJSONArray(DATA);
                Boolean succeed = false;

                Log.e("length of array ", "this " + data.length());
                for (int i = 0; i < data.length(); i++) {
                    JSONObject object = data.getJSONObject(i);
                    status = object.optJSONObject(STATUS);
                    succeed= status.optBoolean("Succeed");

                }
                return succeed;
            }
            else{
                return false;
            }
            }catch(JSONException e1){
                e1.printStackTrace();
            return false;
            }


        }


    public static boolean confirm(String response){
        JSONObject status;
        try {
            JSONObject jobg = new JSONObject(response);
            status = jobg.getJSONObject(STATUS);
            boolean succeed = status.optBoolean("Succeed");
            return succeed;
        } catch (JSONException e) {
            e.printStackTrace();
            return  false;
        }
    }

    }
