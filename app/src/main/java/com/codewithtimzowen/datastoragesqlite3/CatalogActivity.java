package com.codewithtimzowen.datastoragesqlite3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.codewithtimzowen.datastoragesqlite3.data.PetContract.PetEntry;
import com.codewithtimzowen.datastoragesqlite3.data.PetDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CatalogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
            startActivity(intent);
        });

        //display text on screen to check if db create successfully
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
        //Listview
        ListView petListView = findViewById(R.id.list);

        //set cursor
        PetCursorAdapter adapter = new PetCursorAdapter(this, cursor);

        petListView.setAdapter(adapter);
    }

    // method to insert the data from user to the database.
    private void insertPet() {

        //use content calues to store data
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Toto");
        values.put(PetEntry.COLUMN_PET_BREED, "Terrie");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_MALE);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 7);

       // use content Uri to get access and add data to the database
        Uri newUri = getContentResolver().insert(PetEntry.CONTENT_URI, values);

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