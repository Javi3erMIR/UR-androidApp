package com.example.brazomov.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context, "BrazoMov.db",null,1);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE Secuencia(id_secuencia INTEGER primary key, secuencia TEXT)");
        sqLiteDatabase.execSQL("CREATE TABLE Directiva(id_directiva INTEGER primary key, title TEXT, type TEXT, code TEXT )");
        sqLiteDatabase.execSQL("CREATE TABLE Configuracion(id_configuracion INTEGER primary key, ip_robot TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Secuencia");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Directiva");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS Configuracion");
    }

    public boolean insertInto(String table,ContentValues contentValues){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        long result = sqLiteDatabase.insert(table,null,contentValues);

        if(result == -1){
            return false;
        }

        return true;
    }

    public boolean updateInto(String table, ContentValues contentValues,int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+table+" WHERE id_"+table.toLowerCase()+"=?",new String[]{String.valueOf(id)});
        if(cursor.getCount() > 0){
            long result = sqLiteDatabase.update(table, contentValues, "id_"+table.toLowerCase()+"=?", new String[]{String.valueOf(id)});
            if(result == -1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public boolean deleteInto(String table, int id){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+table+" WHERE id_"+table.toLowerCase()+"=?",new String[]{String.valueOf(id)});
        if(cursor.getCount() > 0){
            long result = sqLiteDatabase.delete(table, "id_"+table.toLowerCase()+"=?", new String[]{String.valueOf(id)});
            if(result == -1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public boolean deleteAllFrom(String table){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+table,new String[]{});
        if(cursor.getCount() > 0){
            long result = sqLiteDatabase.delete(table, "", new String[]{});
            if(result == -1){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    public Cursor getAllDataFrom(String table){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM "+table,null);

        return cursor;
    }

    public Cursor executeQuery(String query){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);

        return cursor;
    }

}
