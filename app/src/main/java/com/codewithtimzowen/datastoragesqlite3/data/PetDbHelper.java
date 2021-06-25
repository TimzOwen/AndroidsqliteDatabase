package com.codewithtimzowen.datastoragesqlite3.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.codewithtimzowen.datastoragesqlite3.data.PetContract.PetEntry;

public class PetDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "shelter.db";

    //constructor (cursor factor = null)
    public PetDbHelper(Context context){
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //create table here and reference from the SQLite data petContract class;
        // make sure to give spaces in the string after double quotes to avoid errors which appear as one word
        String SQL_CREATE_TABLE = " CREATE TABLE " + PetEntry.TABLE_NAME + "("
                + PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetEntry.COLUMN_PET_BREED + " TEXT, "
                + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + PetEntry.COLUMN_PET_WEIGHT + " INT NOT NULL DEFAULT 0);";
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       // TODO upgrade the database if changes exist later
    }
}
