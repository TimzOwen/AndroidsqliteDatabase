package com.codewithtimzowen.datastoragesqlite3.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.codewithtimzowen.datastoragesqlite3.data.PetContract.PetEntry;

public class PetProvider extends ContentProvider {

    // Uri matcher code for pets table
    private static final int PETS = 100;

    //Uri matcher code for content Uri for single row in pets table
    private static final int PETS_ID = 101;

    //content uri for corresponding code
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // static initializer
    static {

        //all paths defined here
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS, PETS);

        // Wildcard to access specific element in a row of the Pets table
        sUriMatcher.addURI(PetContract.CONTENT_AUTHORITY, PetContract.PATH_PETS + "#",PETS_ID);
    }

    // database helper object;
    private PetDbHelper mDbHelper;

    @Override
    public boolean onCreate() {
        mDbHelper = new PetDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection,  String selection,  String[] selectionArgs,  String sortOder) {

        // get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //define cursor to hold results of the query
        Cursor cursor;

        // switch to match specific code request to the table
        int match = sUriMatcher.match(uri);

        switch (match){
            case PETS:
                //query entire table
                cursor = database.query(PetEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,sortOder);
                break;
            case PETS_ID:
                //query specific table row ?->string entry
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // perform a query on the table
                cursor = database.query(PetEntry.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOder);
                break;
            default:
                throw new IllegalArgumentException("cannot query unknown URI" + uri);
        }

        //return cursor object;
        return cursor;
    }

    @Override
    public String getType( Uri uri) {
        return null;
    }

    @Override
    public Uri insert( Uri uri,  ContentValues values) {
        return null;
    }

    @Override
    public int delete( Uri uri,  String s,  String[] strings) {
        return 0;
    }

    @Override
    public int update( Uri uri,  ContentValues contentValues,  String s,  String[] strings) {
        return 0;
    }
}
