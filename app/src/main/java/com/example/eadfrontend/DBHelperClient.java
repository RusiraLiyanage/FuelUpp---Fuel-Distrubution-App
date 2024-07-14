package com.example.eadfrontend;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelperClient extends SQLiteOpenHelper {
    public static final String DBNAME = "FuelUppClient.db";


    public DBHelperClient(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE ClientRegister(" +
                "fullname TEXT PRIMARY KEY," +
                "username TEXT," +
                "email TEXT," +
                "gender TEXT," +
                "mobileno TEXT," +
                "vehicletype TEXT," +
                "password TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(
                "DROP TABLE IF EXISTS ClientRegister"
        );
    }

    //Register new user
    public Boolean insertData(String fullname, String username, String email, String gender, String mobileNo, String vehicleType){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullname", fullname);
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("gender", gender);
        contentValues.put("mobileNo", mobileNo);
        contentValues.put("vehicleType", vehicleType);
        contentValues.put("password", "");
        long result = sqLiteDatabase.insert("ClientRegister", null, contentValues);
        if (result == -1) return false;
        else return true;
    }

    //Check username available in database
    public Boolean checkEmail(String email){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM ClientRegister WHERE email= ?",
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
                    "SELECT * FROM ClientRegister WHERE email= ? AND password= ?",
                    new String[] {email, ""}
            );
            Log.d("wefw", String.valueOf(cursor.getCount()));
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
            Log.d("email", email);
            Log.d("password", password);
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            Cursor cursor = sqLiteDatabase.rawQuery(
                    "UPDATE ClientRegister SET password = ? WHERE email = ?",
                    new String[] {password, email}
            );
            Log.d("loginCursorCount", String.valueOf(cursor.getCount()));
            return true;

        }else{
            Log.d("not equal", "password is not same");
            return false;
        }
    }

    //Validate login credentials
    public Boolean validateLoginCredentials(String email, String password){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM ClientRegister WHERE email = ? AND password = ?",
                new String[] {email, password}
        );
        if (cursor.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

    //Get user fullname
    public String getUserFullname(String email){
        Log.d("email", email);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM ClientRegister WHERE email = ?",
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

    //Get user vehicle type
    public String getUserVehicleType(String email){
        Log.d("email", email);
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery(
                "SELECT * FROM ClientRegister WHERE email = ?",
                new String[] {email}
        );
        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            String vehicleType = cursor.getString(cursor.getColumnIndexOrThrow("vehicletype"));
            cursor.close();
            return vehicleType;
        }else{
            cursor.close();
            return null;
        }
    }
}
