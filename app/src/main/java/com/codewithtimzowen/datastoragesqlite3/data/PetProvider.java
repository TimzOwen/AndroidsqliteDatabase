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
        //check for weight
        Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
        //check if not null and is negative throw an exception
        if (weight != null && weight < 0) {
            throw new IllegalArgumentException("Pet requires valid weight");
        }

        //get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //insert new pet with given values
        long id = database.insert(PetEntry.TABLE_NAME, null,values);

        //check if insertion was successful -1 = error . log it out for ease of debugging.
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
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case PETS_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = PetEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    private int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {


        // If the {@link PetEntry#COLUMN_PET_NAME} key is present,
        // check that the name value is not null.
        if (values.containsKey(PetEntry.COLUMN_PET_NAME)) {
            String name = values.getAsString(PetEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_GENDER} key is present,
        // check that the gender value is valid.
        if (values.containsKey(PetEntry.COLUMN_PET_GENDER)) {
            Integer gender = values.getAsInteger(PetEntry.COLUMN_PET_GENDER);
            if (gender == null || !PetEntry.isValidGender(gender)) {
                throw new IllegalArgumentException("Pet requires valid gender");
            }
        }

        // If the {@link PetEntry#COLUMN_PET_WEIGHT} key is present,
        // check that the weight value is valid.
        if (values.containsKey(PetEntry.COLUMN_PET_WEIGHT)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer weight = values.getAsInteger(PetEntry.COLUMN_PET_WEIGHT);
            if (weight != null && weight < 0) {
                throw new IllegalArgumentException("Pet requires valid weight");
            }
        }
        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }
        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Returns the number of database rows affected by the update statement
        return database.update(PetEntry.TABLE_NAME, values, selection, selectionArgs);
    }

}
