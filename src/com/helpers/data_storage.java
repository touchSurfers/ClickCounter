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

    private static final String CLICK_TABLE = "chicks";
    private static final String CLICK_ID = "click_id";
    private static final String CLICK_ADDRESS = "address";
    private static final String CLICK_DATE = "click_date";
    private static final String CLICK_LAT = "click_lat";
    private static final String CLICK_LONG = "click_long";
    private static final String CLICK_PHOTO = "photo";
    private static final String CLICK_TIMESTAMP = "click_timestamp";
    private static final String ORDER_DESC =" ORDER BY CLICK_TIMESTAMP DESC";
    
    private static final String LIMIT = " LIMIT ";

   // private static final String SELECT_CLICKS = "SELECT * FROM " + CLICK_TABLE + " WHERE CLICK_TIMESTAMP > ";
    private static final String SELECT_CLICKS = "SELECT * FROM " + CLICK_TABLE;
   
    public  data_storage(Context context) {
        dbHelper = new data_helper(context, CLICK_TABLE, CLICK_ID + " TEXT,"  + CLICK_ADDRESS + " TEXT," + CLICK_DATE + " TEXT,"  + CLICK_LAT + " TEXT," + CLICK_LONG + " TEXT," + CLICK_PHOTO + " TEXT," + CLICK_TIMESTAMP + " TEXT");
    }

    /**
     * Insert a Stock-value into database
     * @param stockId id of the stock
     * @param stockDesc description of the stock
     * @return success or fail
     */
    public boolean insert(String Id, String address,String date,String lat,String longi,String photo,String timestamp) {
        try {
            SQLiteDatabase sqlite = dbHelper.getWritableDatabase();
            ContentValues initialValues = new ContentValues();

            initialValues.put(CLICK_ID, Id);
            initialValues.put(CLICK_ADDRESS, address);
            initialValues.put(CLICK_DATE, date);
            initialValues.put(CLICK_LAT, lat);
            initialValues.put(CLICK_LONG, longi);
            initialValues.put(CLICK_PHOTO, photo);
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
    public LinkedList<user_item> getDB(int limit) {
        LinkedList<user_item> clicks = new LinkedList<user_item>();

        SQLiteDatabase sqliteDB = dbHelper.getReadableDatabase();
        String query = "";
        
      
        
        query = SELECT_CLICKS + ORDER_DESC + LIMIT + String.valueOf(limit);

        Cursor crsr = sqliteDB.rawQuery(query, null);

        crsr.moveToFirst();

        for (int i = 0; i < crsr.getCount(); i++)
        {
        	clicks.add(new user_item(crsr.getString(0), crsr.getString(1), crsr.getString(2),crsr.getString(3),crsr.getString(4),crsr.getString(5),crsr.getString(6)));

            crsr.moveToNext();
        }

        return clicks;
    }
    
    public int getDBsize() {
        
        SQLiteDatabase sqliteDB = dbHelper.getReadableDatabase();
        String query = "";

        query = SELECT_CLICKS + ORDER_DESC ;

        Cursor crsr = sqliteDB.rawQuery(query, null);
        return crsr.getCount();
        
    }
    
    public user_item getItem(String id) {
    	 user_item click;
    	try{

        SQLiteDatabase sqliteDB = dbHelper.getReadableDatabase();
        String query = "SELECT * FROM " + CLICK_TABLE + " WHERE click_id = '"+id+"'";
        
        Cursor crsr = sqliteDB.rawQuery(query, null);

        crsr.moveToFirst();

        click = new user_item(crsr.getString(0), crsr.getString(1), crsr.getString(2),crsr.getString(3),crsr.getString(4),crsr.getString(5),crsr.getString(6));

    	}catch(Exception e){
    		click = null;
    	}
        return click;
    }
    
    public void update_photo(String id, String photo){
    	
    	SQLiteDatabase sqlite = dbHelper.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
      // initialValues.put(CLICK_ADDRESS, address);    
      // initialValues.put(CLICK_LAT, lat);
      // initialValues.put(CLICK_LONG, longi);
       
       initialValues.put(CLICK_PHOTO, photo);
       
        try{
        	int i= sqlite.update(CLICK_TABLE, initialValues, "click_id=?", new String[]{id}); 
        }catch(Exception e){
        	e.getLocalizedMessage();
        }
        
    }
    
public void update(String id,String address, String lat, String longi){
    	
    	SQLiteDatabase sqlite = dbHelper.getWritableDatabase();

        ContentValues initialValues = new ContentValues();
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