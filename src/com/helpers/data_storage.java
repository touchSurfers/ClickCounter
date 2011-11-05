package com.helpers;

import java.util.LinkedList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class data_storage {

    private data_helper dbHelper;

    private static final String CLICK_TABLE = "click_dates";
    
    private static final String CLICK_ID = "click_id";
    private static final String CLICK_NOTES = "click_notes";
    private static final String CLICK_PHOTO1 = "click_1";
    private static final String CLICK_PHOTO2 = "click_2";
    private static final String CLICK_PHOTO3 = "click_3";
    private static final String CLICK_RATING = "rating";
    private static final String CLICK_ADDRESS = "address";
    private static final String CLICK_DATE = "click_date";
    private static final String CLICK_LAT = "click_lat";
    private static final String CLICK_LONG = "click_long";
    private static final String CLICK_TIMESTAMP = "click_timestamp";
    private static final String ORDER_DESC =" ORDER BY CLICK_TIMESTAMP DESC";
    

    private static final String SELECT_CLICKS = "SELECT * FROM " + CLICK_TABLE + " WHERE CLICK_TIMESTAMP > ";
    private static final String SELECT_ALL_CLICKS = "SELECT * FROM " + CLICK_TABLE;
    private static final String SELECT_TOP_CLICKS = "SELECT * FROM " + CLICK_TABLE + " WHERE rating = \"5.0\" ORDER BY CLICK_TIMESTAMP DESC";
    
    public  data_storage(Context context) {
        dbHelper = new data_helper(context, CLICK_TABLE, CLICK_ID + " TEXT,"  + CLICK_NOTES + " TEXT," + CLICK_PHOTO1 + " TEXT," + CLICK_PHOTO2 + " TEXT," + CLICK_PHOTO3 + " TEXT,"  + CLICK_RATING + " TEXT,"  + CLICK_ADDRESS + " TEXT," + CLICK_DATE + " TEXT,"  + CLICK_LAT + " TEXT," + CLICK_LONG + " TEXT," + CLICK_TIMESTAMP + " TEXT");
    }

    /**
     * Insert a Stock-value into database
     * @param stockId id of the stock
     * @param stockDesc description of the stock
     * @return success or fail
     */
    public boolean insert(String Id, String notes,String photo1,String photo2,String photo3,String rating, String address,String date,String lat,String longi,String timestamp) {
        try {

            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();

            ContentValues initialValues = new ContentValues();

            initialValues.put(CLICK_ID, Id);
            initialValues.put(CLICK_NOTES, notes);
            
            initialValues.put(CLICK_PHOTO1, photo1);
            initialValues.put(CLICK_PHOTO2, photo2);
            initialValues.put(CLICK_PHOTO3, photo3);
            
            initialValues.put(CLICK_RATING, rating);
            initialValues.put(CLICK_ADDRESS, address);
            
            initialValues.put(CLICK_DATE, date);
            initialValues.put(CLICK_LAT, lat);
            initialValues.put(CLICK_LONG, longi);
            initialValues.put(CLICK_TIMESTAMP, timestamp);

            sqlite.insert(CLICK_TABLE, null, initialValues);

        } catch (SQLException sqlerror) {

            Log.v("Insert into table error", sqlerror.getMessage());

            return false;
        }

        return true;

    }

    /**
     * Get all available clicks
     * @return List of stocks
     */
    public LinkedList<user_item> getDB(int period) {
        LinkedList<user_item> clicks = new LinkedList<user_item>();

        SQLiteDatabase sqliteDB = dbHelper.getReadableDatabase();
        String query = "";
        
        String timestamp_day = "";
    	String timestamp_week = "";
	    long dtMili = System.currentTimeMillis();
	    dtMili = dtMili/1000;
	    
	    long dtMili_day = dtMili - 86400;
	    long dtMili_week = dtMili - 604800;
	    
	    timestamp_day = Long.toString(dtMili_day);
	    timestamp_week = Long.toString(dtMili_week);
        
        if(period == 1){
        	query = SELECT_CLICKS + timestamp_day + ORDER_DESC;
        }
        else if(period == 2){
        	 query = SELECT_CLICKS + timestamp_week + ORDER_DESC;
        }
        else if(period == 3){
        	 query = SELECT_ALL_CLICKS + ORDER_DESC;
        }
        else if(period == 4){
       	 query = SELECT_TOP_CLICKS;
       }
        
        Cursor crsr = sqliteDB.rawQuery(query, null);

        crsr.moveToFirst();

        for (int i = 0; i < crsr.getCount(); i++)
        {
        	clicks.add(new user_item(crsr.getString(0), crsr.getString(1), crsr.getString(2),crsr.getString(3),crsr.getString(4),crsr.getString(5),crsr.getString(6),crsr.getString(7),crsr.getString(8),crsr.getString(9),crsr.getString(10)));

            crsr.moveToNext();
        }

        return clicks;
    }
    
    public user_item getItem(String id) {
    	 user_item click;
    	try{

        SQLiteDatabase sqliteDB = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + CLICK_TABLE + " WHERE click_id = '"+id+"'";
        
        Cursor crsr = sqliteDB.rawQuery(query, null);

        crsr.moveToFirst();

        click = new user_item(crsr.getString(0), crsr.getString(1), crsr.getString(2),crsr.getString(3),crsr.getString(4),crsr.getString(5),crsr.getString(6),crsr.getString(7),crsr.getString(8),crsr.getString(9),crsr.getString(10));

    	}catch(Exception e){
    		click = null;
    	}
        return click;
    }
    
    public void update(String id,String notes,String photo1,String photo2, String photo3, String rating, String address, String lat, String longi){
    	
    	SQLiteDatabase sqlite = dbHelper.getWritableDatabase();

        ContentValues initialValues = new ContentValues();

        
      
        initialValues.put(CLICK_NOTES, notes);
       initialValues.put(CLICK_PHOTO1, photo1);
       
        initialValues.put(CLICK_PHOTO2, photo2);
       initialValues.put(CLICK_PHOTO3, photo3);
       
       initialValues.put(CLICK_RATING, rating);
       initialValues.put(CLICK_ADDRESS, address);
       
       initialValues.put(CLICK_LAT, lat);
       initialValues.put(CLICK_LONG, longi);
       

        try{
       int i= sqlite.update(CLICK_TABLE, initialValues, "click_id=?", new String[]{id}); 
        }catch(Exception e){
        	e.getLocalizedMessage();
        }
        
    }
    
    public void delete(String id) {
    	
    	SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
    	sqlite.delete(CLICK_TABLE, "click_id=?", new String[]{id}); 
	
    }

    /**
     * Clear the table
     */
    public void clear() {

        try {

            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();

            sqlite.delete(CLICK_TABLE, "", null);
            
        } catch (SQLException sqlerror) {

            Log.v("delete from table error", sqlerror.getMessage());

        }    
    }
}