package com.chick;

import java.io.IOException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.http.message.BasicNameValuePair;

import sharing_class.chicks_server;
import android.app.Application;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Handler;

import com.helpers.data_storage;
import com.helpers.json_class;
import com.helpers.user_item;



public class share_class extends Application {

	
	

	public LinkedList<chicks_server> chicks_server_map = new LinkedList<chicks_server>();
	public void chicks_server_map_delete(){
		
		//TODO
		//smycka
		//chicks_server_map.remove(myObject);
		//myObject = null;
		//chicks_server_map
	}
	
	public static final String PREFS_NAME = "chick_data";
	SharedPreferences settings ;
	SharedPreferences.Editor editor;
	private static final String TAG = "share_class";
	
	 public Handler mHandler;
	
	//Private variables

	 public boolean map_changed_address = false;
	private int chick_count;
	private int period = 1;
	private String actual_photo= "";
	private String actual_selected = "";
	private Thread ControlThread;
	Location my_location;
	Double location_set_lat = 20.0;
	Double location_set_long = 20.0;
	
	private String notes_added = "";
	private LinkedList<String> photo_cache=new LinkedList<String>();
	
	//tools
    SQLiteDatabase db;
    json_class sender;
    
	
	data_storage dbMgr =null;
	
	  @Override
	  public void onCreate()
	  {
	  super.onCreate();
	  
	      settings = getSharedPreferences(PREFS_NAME, 0);	
	      editor = settings.edit();
	      InitDB();
	      sender = new json_class();
	  }
	  
	  @Override
	  public void onLowMemory()
	  {
	  super.onLowMemory();
	
	  }
	  @Override
	  public void onTerminate()
	  {
	  super.onTerminate();
	
	  }
	  
	  public void InitDB(){
		  
		 if(dbMgr == null){
		  dbMgr = new data_storage(getApplicationContext());
		 }
		 
		 
	  }
	  
	  public void DeleteDB(){

			  dbMgr.clear();

		  }
	  
	  public void DeleteItem(String id){

		  dbMgr.delete(id);

	  }
	  
	  public void InsertDB(String id,String notes,String photo1,String photo2,String photo3,String rating,String address,String date,String lat,String longi,String timestamp){
		  
		  if(dbMgr != null)
		  dbMgr.insert(id, notes,photo1,photo2,photo3,rating,address,date,lat,longi,timestamp);
          
	  }
	  
      public void UpdateDB(String id,String notes,String photo1,String photo2,String photo3, String rating,String address,String lat, String longi){
		  
		  if(dbMgr != null)
		  dbMgr.update(id, notes, photo1, photo2, photo3, rating,address,lat,longi);
          
	  }
	  
	  
	  public LinkedList<user_item> GetDB(int period_selected){
		
		    return dbMgr.getDB(period_selected);
  
	  }
	  
	  public user_item GetItem(String id){
			
			return dbMgr.getItem(id);
	  
	  }
	  
	  public String getDeviceId(){
		  
		String ID = settings.getString("device_id","");
		if(ID.length() == 0){
  		    Random RanNum = new Random();
		    ID = md5(Integer.toString(RanNum.nextInt()));
		    ID = ID.substring(0,10); 
		    editor.putString("device_id", ID);
		    editor.commit();
		    return ID;
		}
		else{
			return ID;
		}
	}
	  
	  public void clearPhotoCache(){
		 photo_cache.clear();
	  }
	  
	  public int countPhotoCache(){
		  return photo_cache.size();
	  }
	  
	  public void SetNote(String note){
			 notes_added = note;
      }
		  
		  public String GetNote(){
			  return notes_added;
      }
		  
		  public Location getLocation(){
			    return my_location;
		  }
		  public void setLocation(Location location){
			   my_location = location;
		  }
		  
		  public void setLocation_set(Double lat, Double longi){
			  
			   location_set_lat= lat;
			   location_set_long= longi;
		  }
	  
	  public void addPhotoCache(String s){
	try{
		photo_cache.addFirst(s);
	}catch(Exception e){
		
	}
		  
	  }
	  
	  public void addPhotoSCache(LinkedList<String> s){
		  photo_cache = s;
	  }
	  
	  public LinkedList<String> getPhotoSCache(){
		  return photo_cache;
	  }
	  
	  public String getPhotoCache(int num){
		  
		  try{
			  return photo_cache.get(num);
		  }catch(Exception e){
			  
			  e.getLocalizedMessage();
			  return "";
			  
		  }
		  
	  }
	  
	  public int getChickCount(){
		  		return settings.getInt("chick_count",0);
	  }
	  
	  public void setChickCount(int s){
		    editor.putInt("chick_count", s);
		    editor.commit();
	  }
	  
	  public String getChickID(){
		    
	  		return settings.getString("chick_id","");
	  
	  }
	  
	  public void setChickPhoto(String s){
		    actual_photo = s;
	  }
	  
	  public String getChickPhoto(){
		    
	  		return actual_photo;
	  
	  }
	  
	  public void setPeriod(int s){
		    period = s;
	  }
	  
	  public int getPeriod(){
	  		return period;
	  }
	  
	  public void setChickSelected(String s){
		    actual_selected = s;
	  }
	  
	  public String getChickSelected(){
		    
	  		return actual_selected;
	  
	  }

	  public void setChickID(String s){
	    editor.putString("chick_id", s);
	    editor.commit();
	  }
	  
	  public void InsertThread(){
		  
		  ControlThread = new Thread(null, InsertThreadProc, "InsertThread");
		  ControlThread.start();
	  }
	  
      

	  Runnable InsertThreadProc = new Runnable() {
	        public void run() {
	        	
	        	try{	
	        			
	        		//LOCATION
	        		//Get actual chick position
	        		Location actual_location;
	        		LocationManager locationManager;
	        		
	        		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);      
	        		actual_location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	        		
	        		//ID click
	        		//Get random number and generate 10 digit ID from it
	        		String ID= "";
	        		Random RanNum = new Random();
        		    ID = md5(Integer.toString(RanNum.nextInt()));
        		    ID = ID.substring(0,10);        		    
        		    
        		    setChickID(ID);//Set actual chickID
        		    
        		    //Get current date in readable format and also timestamp
        		    String date = DateFormat.getDateTimeInstance().format(new Date());
        		    
        		    //Timestamp
        		    String timestamp_s = "";
        		    long dtMili = System.currentTimeMillis();
        		    dtMili = dtMili/1000;
        		    timestamp_s = Long.toString(dtMili);
        		    
        		    //save this informations to DB
        		    
        		    
        		    String lat = Double.toString(actual_location.getLatitude());
        		    String longi = Double.toString(actual_location.getLongitude());
        		    InsertDB(ID,"","","","","","",date,lat,longi,timestamp_s);
        		   
        		    String address = requestAdress(actual_location.getLatitude(),actual_location.getLongitude());
         		   	UpdateDB(ID,"","","","","",address,lat,longi);
        		    
        		    //Send this information to cloud
        		    
        		    sender.nameValuePairs.clear();
	        		sender.setUrl("http://gi60s.com/save_click.php");
	        		sender.nameValuePairs.add(new BasicNameValuePair("device_id", getDeviceId()));
	        		sender.nameValuePairs.add(new BasicNameValuePair("id", ID));
	                sender.nameValuePairs.add(new BasicNameValuePair("lat",lat));
	                sender.nameValuePairs.add(new BasicNameValuePair("long",longi));
	                //sender.GetData();
	                
	        		
	        	}catch(Exception e){}
				
	              
	        }//run
	    };
	    
	    public void getServerChicks() {
	       
	        	try{	
	        			
	        		//LOCATION
	        		//Get actual chick position
	        		Location actual_location;
	        		LocationManager locationManager;
	        		
	        		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);      
	        		actual_location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        		    //Get current date in readable format and also timestamp
        		    String date = DateFormat.getDateTimeInstance().format(new Date());
        		    
        		    //Timestamp
        		    String timestamp_s = "";
        		    long dtMili = System.currentTimeMillis();
        		    dtMili = dtMili/1000;
        		    timestamp_s = Long.toString(dtMili);        		    
        		    
        		    String lat = Double.toString(actual_location.getLatitude());
        		    String longi = Double.toString(actual_location.getLongitude());
        		    
        		    //Send this information to cloud
        		    
        		    sender.nameValuePairs.clear();
	        		sender.setUrl("http://gi60s.com/get_chicks.php");
	        		sender.nameValuePairs.add(new BasicNameValuePair("device_id", getDeviceId()));
	                sender.nameValuePairs.add(new BasicNameValuePair("lat",lat));
	                sender.nameValuePairs.add(new BasicNameValuePair("long",longi));
	                sender.GetData(share_class.this);
	                
	        		
	        	}catch(Exception e){}
				
	    }
	    
	    public void GetLastLoc(){
			  
			try{  
			//LOCATION
	  		//Get actual user location
	  		
	  		LocationManager locationManager;
	  		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);      
	  		my_location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
			}catch(Exception e){}
			
		  }
	    
	    public static final String md5(final String s) {
	        try {
	            // Create MD5 Hash
	            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	            digest.update(s.getBytes());
	            byte messageDigest[] = digest.digest();
	     
	            // Create Hex String
	            StringBuffer hexString = new StringBuffer();
	            for (int i = 0; i < messageDigest.length; i++) {
	                String h = Integer.toHexString(0xFF & messageDigest[i]);
	                while (h.length() < 2)
	                    h = "0" + h;
	                hexString.append(h);
	            }
	            return hexString.toString();
	     
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        return "";
	    }  
	 
	 
	    public String requestAdress(Double latitude, Double longitude){
			
			Geocoder geocoder = new Geocoder(share_class.this);
			try {
				List<Address> addresses = geocoder.getFromLocation( latitude, longitude , 1);
				
				if(addresses != null) {
					   Address returnedAddress = addresses.get(0);
					   StringBuilder strReturnedAddress = new StringBuilder("");
					   for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
					    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("");
					   }
					
					     //sharing_class.setAdress(strReturnedAddress.toString());
					     //adress.setText(strReturnedAddress.toString());
						    return strReturnedAddress.toString();
					  }
					  else{
						  	return "Address not found";
					    // sharing_class.setAdress("Searching address..");
					     //adress.setText("Searching address.."); 
					  }
				
				
			} catch (IOException e) {
				
				e.printStackTrace();
				return "Address not found";
			}
		}	
			
	    
	    
	  
}