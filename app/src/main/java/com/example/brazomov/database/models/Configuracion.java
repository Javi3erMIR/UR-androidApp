package com.example.brazomov.database.models;

import android.content.ContentValues;

public class Configuracion {

    private String ip_robot;

    public Configuracion(){}

    public Configuracion(String ip_robot){
        this.ip_robot = ip_robot;
    }

    public String getIpRobot(String ip_robot){
        return ip_robot;
    }

    public void setIpRobot(String ip_robot){
        this.ip_robot = ip_robot;
    }

    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("ip_robot",ip_robot);
        return contentValues;
    }

}
