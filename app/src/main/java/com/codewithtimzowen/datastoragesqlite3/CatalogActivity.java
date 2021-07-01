package com.codewithtimzowen.datastoragesqlite3;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;



import com.codewithtimzowen.datastoragesqlite3.data.PetContract.PetEntry;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

// implement loader callbacks
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PET_LOADER = 0;
    PetCursorAdapter mCursorAdapter;

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

        // Find the ListView which will be populated with the pet data
        ListView petListView = (ListView) findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        petListView.setEmptyView(emptyView);

        //set up adapter to create a loader in the list
        mCursorAdapter = new PetCursorAdapter(this,null);
        petListView.setAdapter(mCursorAdapter);

       //set onClick item listener
        petListView.setOnItemClickListener((parent, view, position, id) -> {
            // open the new Editor activity from the item clicked
            Intent editorIntent = new Intent(CatalogActivity.this, EditorActivity.class);
            //pass the data from CONTENT_URI into Editor activity from the id
            Uri currentPetUri = ContentUris.withAppendedId(PetEntry.CONTENT_URI,id);
            //set uri on the data
            editorIntent.setData(currentPetUri);
            //startActivity
            startActivity(editorIntent);

        });


        //kick off the loader
        getSupportLoaderManager().initLoader(PET_LOADER,null, this);

    }

    // method to insert the data from user to the database.
    private void insertPet() {

        //use content values to store data
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

    //once menu inflate use switch case to update
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                // call the insert method to post to the database
                insertPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                // Do nothing for now
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //loader methods for loading data

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //define projections that specify columns
        String[] projections = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED
        };
        //perform execution on a cursor loader in background
        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                projections,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        //updated UI with new cursor loader
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        //called when data needs to be collected
        mCursorAdapter.swapCursor(null);

    }
}