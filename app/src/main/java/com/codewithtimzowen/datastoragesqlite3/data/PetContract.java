package com.codewithtimzowen.datastoragesqlite3.data;

import android.provider.BaseColumns;

public final class PetContract {

    //private constructor
    private PetContract(){

    }

    public static final class PetEntry implements BaseColumns{

        public final static String TABLE_NAME = "pets";


    }
}
