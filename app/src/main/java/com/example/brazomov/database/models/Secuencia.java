package com.example.brazomov.database.models;

import android.content.ContentValues;

public class Secuencia {

    private int id_secuencia;
    private String secuencia;

    public Secuencia(){}

    public Secuencia(int id_secuencia, String secuencia){
        this.id_secuencia = id_secuencia;
        this.secuencia = secuencia;
    }

    public void setId_secuencia(int id_secuencia){
        this.id_secuencia = id_secuencia;
    }

    public void setSecuencia(String secuencia){
        this.secuencia = secuencia;
    }

    public int getId_secuencia(){
        return id_secuencia;
    }

    public String getSecuencia(){
        return secuencia;
    }

    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("secuencia", this.secuencia);

        return contentValues;
    }

    public String getQueryDirectivas(){
        return "SELECT * FROM Directivas WHERE id_secuencia="+id_secuencia;
    }

}
