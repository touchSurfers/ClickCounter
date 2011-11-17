package com.helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class data_helper extends SQLiteOpenHelper {

	 private static final String CLICK_TABLE = "chicks";
    private static final int DATABASE_VERSION = 13;

    private String createStatement = "";
    
    /**
     * Creates the OpenDbHelper 
     * 
     * @param context app-context
     * @param tableName name of the table to open/create
     * @param fields fields of the table to create
     */
    public data_helper(Context context, String tableName, String fields) {

        super(context, tableName, null, DATABASE_VERSION);
        
        this.createStatement  = "CREATE TABLE ";
        this.createStatement += tableName + " (";
        this.createStatement += fields + ");";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(this.createStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int neVersion) {
        // TODO Auto-generated method stub
    	db.execSQL("DROP TABLE IF EXISTS "+CLICK_TABLE);
    	onCreate(db);
    }

}