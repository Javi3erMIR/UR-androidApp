package com.example.brazomov.database.models;

import android.content.ContentValues;

public class Directiva {

    private int id_directiva;
    private int id_secuencia;
    private String codigo = "desc";
    private String title;
    private String type;

    public Directiva(){}

    public Directiva(int id_directiva, String title, String tipo){
        this.id_directiva = id_directiva;
        this.title = title;
        this.type = tipo;
    }

    public int getId_directiva() {
        return id_directiva;
    }

    public void setId_directiva(int id_directiva) {
        this.id_directiva = id_directiva;
    }

    public int getId_secuencia() {
        return id_secuencia;
    }

    public void setId_secuencia(int id_secuencia) {
        this.id_secuencia = id_secuencia;
    }


    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put("id_directiva", this.id_directiva);
        contentValues.put("title", this.title);
        contentValues.put("type",this.type);
        contentValues.put("code", this.codigo);
        return contentValues;
    }

}
