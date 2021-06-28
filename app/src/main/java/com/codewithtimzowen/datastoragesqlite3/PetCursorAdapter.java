package com.codewithtimzowen.datastoragesqlite3;

import android.content.Context;
import android.database.Cursor;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.codewithtimzowen.datastoragesqlite3.data.PetContract.PetEntry;


public class PetCursorAdapter extends CursorAdapter {

    //construct new pet cursor adapter
    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    //create new blank view . to be used by the cursor in moving objects
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        //return a layout inflater
        return LayoutInflater.from(context).inflate(R.layout.item_list,parent,false);
    }


    // used for binding pet data to the given list
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // find the views attached in the UI
        TextView tvName = view.findViewById(R.id.name);
        TextView tvSummary = view.findViewById(R.id.summary);

        // find column of pets attribute of intrest
        int nameColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED);

        //Read pet attribute for the current cursor
        String petName = cursor.getString(nameColumnIndex);
        String petBreed = cursor.getString(breedColumnIndex);

        //set the textViews
        tvName.setText(petName);
        tvSummary.setText(petBreed);
    }
}
