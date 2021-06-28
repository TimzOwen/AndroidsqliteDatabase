package com.codewithtimzowen.datastoragesqlite3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.codewithtimzowen.datastoragesqlite3.data.PetContract.PetEntry;
import com.codewithtimzowen.datastoragesqlite3.data.PetDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CatalogActivity extends AppCompatActivity {

    //declare
    private PetDbHelper mDbhelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        //instantiate the class
        mDbhelper = new PetDbHelper(this);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
            startActivity(intent);
        });

        //display text on screen to cehck if db create successfully
        displayDataInfo();
    }

    // override the onStart method to display new data each time the user exits the editor activity
    @Override
    protected void onStart() {
        super.onStart();
        displayDataInfo();
    }

    //Method to check sql creation entry
    private void displayDataInfo() {
        //create an instance of the database class
        PetDbHelper mDbHelper = new PetDbHelper(this);
        //create and/ open a database connection from the helper class.
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //declare the projections of interest
        String[] projections = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };


        // instead use content providers
        Cursor cursor = getContentResolver().query(
                PetEntry.CONTENT_URI,
                projections,
                null,
                null,
                null);

        // Find the view related to the UI in the Catalog
        TextView displayView = findViewById(R.id.text_view_pet);

        try {

            displayView.setText("The pets table contains " + cursor.getCount() + " pets. \n\n");
            displayView.append(PetEntry._ID + " - " +
                    PetEntry.COLUMN_PET_NAME + " - "
                    + PetEntry.COLUMN_PET_BREED
                    + " - " + PetEntry.COLUMN_PET_GENDER
                    + " - " + PetEntry.COLUMN_PET_WEIGHT + "\n");

            //find respective index
            int idColumnIndex = cursor.getColumnIndex(PetEntry._ID);
            int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
            int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);
            int genderColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER);
            int weightColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT);

            //iterate and update as long as the return type is true;
            while (cursor.moveToNext()) {
                int currentId = cursor.getInt(idColumnIndex);
                String currentName = cursor.getString(nameColumnIndex);
                String currentBreed = cursor.getString(breedColumnIndex);
                int currentGender = cursor.getInt(genderColumnIndex);
                int currentWeight = cursor.getInt(weightColumnIndex);

                displayView.append(("\n " + currentId + " - " + currentName +
                        " - " + currentBreed +
                        " - " + currentGender +
                        " - " + currentWeight));
            }

        } finally {
            cursor.close();
        }
    }

    // method to insert the data from user to the database.
    private void insertPet() {
        //get a writable mode of the sqlite
        SQLiteDatabase db = mDbhelper.getWritableDatabase();

        //use content calues to store data
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrie");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

        // insert values
        long newRowId = db.insert(PetEntry.TABLE_NAME, null, values);

        // log to check flow of execution
        Log.v("CatalogActivity", "new row Id inserted" + newRowId);

    }


    //create menus inflation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // call the insert method to post to the database
                insertPet();
                displayDataInfo();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}