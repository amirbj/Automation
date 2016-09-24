package com.paya.authomation.main;

/**
 * Created by Administrator on 08/02/2016.
 */
public class User {
    String userTitle;
    String userId;
    int stat;
    public User( int sta){
        this.stat= sta;
       // this.userTitle =username;

    }

    public int getStat() {
        return stat;
    }

    public void setStat(int stat) {
        this.stat = stat;
    }




    public String getUserId() {
        return userId;
    }

    public String getUserTitle() {
        return userTitle;
    }



    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUserTitle(String userTitle) {
        this.userTitle = userTitle;
    }


}
