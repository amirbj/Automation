package com.paya.authomation.main;

/**
 * Created by Administrator on 06/24/2016.
 */
public class DrawerItem {


    private int ItemName;
    private int imgResID;

    public DrawerItem(int itemName) {
        super();
        ItemName = itemName;
        //this.imgResID = imgResID;
    }

    public int getImgResID() {
        return imgResID;
    }

    public void setImgResID(int imgResID) {
        this.imgResID = imgResID;
    }

    public int getItemName() {
        return ItemName;
    }

    public void setItemName(int itemName) {
        ItemName = itemName;
    }


}

