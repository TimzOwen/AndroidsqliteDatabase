package com.codewithtimzowen.datastoragesqlite3.data;

import android.provider.BaseColumns;

public final class PetContract {

    public static final String CONTENT_AUTHORITY = "com.codewithtimzowen.datastoragesqlite3.data/pets";
    public static final String PATH_PETS = "uri_matcher_object";

    //private constructor
    private PetContract(){

    }

    public static final class PetEntry implements BaseColumns{

        public final static String TABLE_NAME = "pets";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PET_NAME = "name";
        public final static String COLUMN_PET_BREED = "breed";
        public final static String COLUMN_PET_GENDER = "gender";
        public final static String COLUMN_PET_WEIGHT = "weight";


        //Gender for th e menu switch statement
        public static final int GENDER_UNKNOWN = 0;
        public static final int GENDER_MALE = 1;
        public static final int GENDER_FEMALE = 2;



    }
}
