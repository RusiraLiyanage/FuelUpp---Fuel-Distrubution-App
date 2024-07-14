package com.example.eadfrontend;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class DBHelperOwner extends SQLiteOpenHelper {
    public static final String DBNAME = "FuelUppOwner.db";


    public DBHelperOwner(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE OwnerRegister(" +
                        "fullname TEXT PRIMARY KEY," +
                        "username TEXT," +
                        "email TEXT," +
                        "gender TEXT," +
                        "mobileno TEXT," +
                        "shedNameLocation TEXT," +
                        "password TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(
                "DROP TABLE IF EXISTS OwnerRegister"
        );
    }

    //Register new shed owner
    public Boolean insertData(String fullname, String username, String email, String gender, String mobileNo, String shedNameLocation){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullname", fullname);
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("gender", gender);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("shedNameLocation", shedNameLocation);
        contentValues.put("password", "");
        long result = sqLiteDatabase.insert("OwnerRegister", null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    //Check username available in database
    public Boolean checkEmail(String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM OwnerRegister WHERE email = ?",
                new String[] {email}
        );
        if (cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

    //Check login details has an existing password
    public Boolean checkLoginHasPassword(String email){
        Boolean val = checkEmail(email);
        if(val){
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(
                    "SELECT * FROM OwnerRegister WHERE email = ? AND password = ?",
                    new String[] {email, ""}
            );
            if (cursor.getCount() > 0){
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }


    //Creates new password
    public Boolean createNewPassword(String email, String password, String confPassword){
        if(password.equals(confPassword)){
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(
                    "UPDATE OwnerRegister SET password = ? WHERE email = ?",
                    new String[] {password, email}
            );
            Log.d("loginCursorCount", String.valueOf(cursor.getCount()));
            return true;
        }else{
            return false;
        }
    }

    //Validate login credentials
    public Boolean validateLoginCredentials(String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM OwnerRegister WHERE email = ? AND password = ?",
                new String[] {email, password}
        );
        if (cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

    //Get user full name - shed owner
    public String getUserFullname(String email){
        Log.d("email", email);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM OwnerRegister WHERE email = ?",
                new String[] {email}
        );
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            String fullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
            cursor.close();
            return fullname;
        }else{
            cursor.close();
            return null;
        }
    }

    //Get shed name
    public String getUserShedName(String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM OwnerRegister WHERE email = ?",
                new String[] {email}
        );
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            String shedName = cursor.getString(cursor.getColumnIndexOrThrow("shedNameLocation"));
            cursor.close();
            return shedName;
        }else{
            cursor.close();
            return null;
        }
    }

}
