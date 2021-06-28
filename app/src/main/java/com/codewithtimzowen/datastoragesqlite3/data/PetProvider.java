package com.codewithtimzowen.datastoragesqlite3.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.codewithtimzowen.datastoragesqlite3.data.PetContract.PetEntry;

public class PetProvider extends ContentProvider {

    // Uri matcher code for pets table
    private static final int PETS = 100;

    //Uri matcher code for content Uri for single row in pets table
    private static final int PETS_ID = 101;

    //content uri for corresponding code
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    /*Log for error messages for ease of debugging*/
    private static final String LOG_TAG = PetProvider.class.getSimpleName();

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
    public Uri insert( Uri uri,  ContentValues contentValues) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return insertPet(uri, contentValues);
            default:
                throw new IllegalArgumentException("insertion is not supported for " + uri);
        }
    }

    //insert and return new content uris
    private Uri insertPet(Uri uri, ContentValues values){

        //check that the name is not null
        String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
        //if name is null throw an exception
        if (name==null){
            throw new IllegalArgumentException("Requires pet name");
        }
        //check for gender
        Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
        if (gender==null || !PetEntry.isValidGender(gender)){
            throw new IllegalArgumentException("Pet requires valid Gender");
        }

        //get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //insert new pet with given values
        long id = database.insert(PetEntry.TABLE_NAME, null,values);

        //check if insertion was succesful -1 = error . log it out for ease of debugging.
        if(id==-1){
            Log.e(LOG_TAG,"Error during insertion" + uri);
            return null;
        }
        return ContentUris.withAppendedId(uri, id);
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
