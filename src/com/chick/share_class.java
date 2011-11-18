package com.chick;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.apache.http.message.BasicNameValuePair;

import sharing_class.chicks_server;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.ExifInterface;
import android.os.Handler;

import com.billing.util.Base64;
import com.helpers.data_storage;
import com.helpers.data_storage_photo;
import com.helpers.json_class;
import com.helpers.list_item;
import com.helpers.photo_item;
import com.helpers.user_item;

public class share_class extends Application {

	
	//List of lists for History list agregation
	public LinkedList<LinkedList<String>> chicks_history = new LinkedList<LinkedList<String>>();
	public LinkedList<list_item> chicks_list = new LinkedList<list_item>();
	public LinkedList<photo_item> chicks_photos = new LinkedList<photo_item>();
	public LinkedList<String> chicks_helper ;
	
	
	public LinkedList<chicks_server> chicks_server_map = new LinkedList<chicks_server>();
	public void chicks_server_map_delete(){
		//TODO
		//smycka
		//chicks_server_map.remove(myObject);
		//myObject = null;
		//chicks_server_map
	}
	
	public static final String PREFS_NAME = "chick_data";
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	private static final String TAG = "share_class";
	
	//Handlers for activity X thread communication
	public Handler mHandler;
	public Handler mHandler1;
	public Handler mHandler2;
	public Handler mHandler3;
	
	//Private variables
	
	private String actual_photo= "";
	private String actual_selected = "";
	private Thread ControlThread;
	Location my_location;
	Double location_set_lat = 20.0;
	Double location_set_long = 20.0;
	
	private Long LastChickSendTimestamp = 0L;
	private String ActualAddress = "";
	
	String send_bitmap;
	
	public LinkedList<String> photos_gallery=new LinkedList<String>();
	
	private String notes_added = "";
	private LinkedList<String> photo_cache=new LinkedList<String>();
	
	//tools
    SQLiteDatabase db;
    json_class sender;
    
    private NotificationManager mNotificationManager;
    private int SIMPLE_NOTFICATION_ID;
	
	data_storage dbMgr =null;
	data_storage_photo dbMgr_photo =null;
	
	  @Override
	  public void onCreate()
	  {
	  super.onCreate();
	  
	  photos_gallery.add("http://a5.sphotos.ak.fbcdn.net/photos-ak-snc1/v1939/169/99/1375196364/n1375196364_197923_6914.jpg");
	  photos_gallery.add("http://a5.sphotos.ak.fbcdn.net/photos-ak-snc1/v1939/169/99/1375196364/n1375196364_197923_6914.jpg");
	  photos_gallery.add("http://a5.sphotos.ak.fbcdn.net/photos-ak-snc1/v1939/169/99/1375196364/n1375196364_197923_6914.jpg");
	  photos_gallery.add("http://a5.sphotos.ak.fbcdn.net/photos-ak-snc1/v1939/169/99/1375196364/n1375196364_197923_6914.jpg");
	  photos_gallery.add("http://a5.sphotos.ak.fbcdn.net/photos-ak-snc1/v1939/169/99/1375196364/n1375196364_197923_6914.jpg");
	  photos_gallery.add("http://a5.sphotos.ak.fbcdn.net/photos-ak-snc1/v1939/169/99/1375196364/n1375196364_197923_6914.jpg");
	  
	      settings = getSharedPreferences(PREFS_NAME, 0);	
	      editor = settings.edit();
	      InitDB();
	      InitDB_photo();
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
	  
	  
	  
	  
	  
	  //Database operations and managers
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
	  public void InsertDB(String id,String address,String date,String lat,String longi,String photo,String timestamp){
		  if(dbMgr != null)
		  dbMgr.insert(id,address,date,lat,longi,photo,timestamp); 
	  }
	  
      public void UpdateDB(String id,String address,String lat, String longi){  
		  if(dbMgr != null)
		  dbMgr.update(id, address,lat,longi);  
	  }
      
      public void UpdateDB_photo(String id,String photo){  
		  if(dbMgr != null)
		  dbMgr.update_photo(id, photo);  
	  }
	  
	  public LinkedList<user_item> GetDB(int limit){
		    return dbMgr.getDB(limit);
	  }
	  
	  public user_item GetItem(String id){
			return dbMgr.getItem(id);
	  }
	  
	  //Generate and store device ID
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
	  
	  
	  //Photo DB 
	  
	  //Database operations and managers
	  public void InitDB_photo(){
		 if(dbMgr_photo == null){
		  dbMgr_photo = new data_storage_photo(getApplicationContext());
		 } 
	  }
	  
	  public void DeleteDB_photo(){
			  dbMgr_photo.clear();
	  }
	  public void DeleteItem_photo(String id){
		  dbMgr_photo.delete(id);
	  }
	  public void InsertDB_photo(String id,String photo,String timestamp){
		  if(dbMgr_photo != null)
		  dbMgr_photo.insert(id,photo,timestamp); 
	  }

	  /*
	  public LinkedList<user_item> GetDB_photo(){
		    return dbMgr_photo.getDB();
	  }
	  */
	  
	  
	  
	  //Store and read payment
	  public boolean isPaid(){
		  
		  String paid = settings.getString("paid_id","");
			if(paid.length() == 0){
				//Not paid user
				return false;
			}
			else{
				//paid user
				return true;
			}
	  }
	  
	  public void setPaid(String paid_id){
		  
		  editor.putString("paid_id", paid_id);
		  editor.commit();
	  }
	  
	  //Set actual address
	  public void setAddress(String adr){
		  ActualAddress = adr;
	  }
	  
	  public String getAddress(){
		  return ActualAddress;
	  }
	  
	  //Location setters and getters
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
	  
	  //Profile functions (delete)
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
	  
	  //Set and get timestamp (for sending control)
	  public long getLastChickSendTimestamp(){
		  
		  return LastChickSendTimestamp ;
	  }
	  
	  public void setLastChickSendTimestamp(long timestamp){
		  
		   LastChickSendTimestamp = timestamp ;
	  }
	  
	  public int getChickCount(){
		  		return settings.getInt("chick_count",0);
	  }
	  
	  public void setChickCount(int s){
		    editor.putInt("chick_count", s);
		    editor.commit();
	  }
	  
	  //Chick timer
	  public Long getChickTimer(){
	  		return settings.getLong("chick_timer",0);
	  }

	  public void setChickTimer(long s){
	    editor.putLong("chick_timer", s);
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
	  
	  
	  /*
	   * Actual chick selected
	   * 
	   */
	  public void setChickSelected(String s){
		    actual_selected = s;
	  }
	  
	  public String getChickSelected(){
	  		return actual_selected;
	  }

	  //If app turned off, store last chick clicked ID
	  public void setChickID(String s){
	    editor.putString("chick_id", s);
	    editor.commit();
	  }
	  
 /*
  * 
  * 
  *  Data senders
  * 
  * 
  */
	  
	  //Insert new chicks to DB and upload info to server
	  public void InsertThread(){
		  
		  ControlThread = new Thread(null, InsertThreadProc, "InsertThread");
		  ControlThread.start();
	  }
	  
	  Runnable InsertThreadProc = new Runnable() {
	        public void run() {
	        	
	        	try{	
	        		//ID click
	        		//Get random number and generate 10 digit ID from it
	        		String ID= "";
	        		Random RanNum = new Random();
        		    ID = md5(Integer.toString(RanNum.nextInt()));
        		    ID = ID.substring(0,10); 
        		    
        		    setChickID(ID);//Set actual chickID
        		    //Get current date in readable format and also timestamp
        		    
        		    //String  date_d = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT,Locale.ENGLISH).format(new Date());
        		    String  date_d = DateFormat.getDateInstance(DateFormat.MEDIUM,Locale.ENGLISH).format(new Date());
        		    String time_d = DateFormat.getTimeInstance(DateFormat.SHORT,Locale.ENGLISH).format(new Date());
        		    String date = time_d+", "+date_d;
        		  
        		    //Timestamp
        		    String timestamp_s = "";
        		    long dtMili = System.currentTimeMillis();
        		    dtMili = dtMili/1000;
        		    timestamp_s = Long.toString(dtMili);
        		    
        		    //save this informations to DB
        		    
        		    String lat = "0.0";
    		    	String longi = "0.0";
        		    try{
        		    	lat = Double.toString(getLocation().getLatitude());
        		    	longi = Double.toString(getLocation().getLongitude());
        		    }catch(Exception e){
        		    	
        		    }
        		    InsertDB(ID,"",date,lat,longi,"",timestamp_s);
        		   
        		    //String address = requestAdress(getLocation().getLatitude(),getLocation().getLongitude());
         		   	String address = getAddress();
        		    UpdateDB(ID,address,lat,longi);
        		    
        		    //Send this information to cloud
        		    
        		    sender.nameValuePairs.clear();
	        		sender.setUrl("http://app.nearley.com/chick_counter/set.php");
	        		sender.nameValuePairs.add(new BasicNameValuePair("device_id", getDeviceId()));
	        		sender.nameValuePairs.add(new BasicNameValuePair("id", ID));
	                sender.nameValuePairs.add(new BasicNameValuePair("lat",lat));
	                sender.nameValuePairs.add(new BasicNameValuePair("long",longi));
	                
	                if(System.currentTimeMillis()>( getLastChickSendTimestamp() +1000)){
	                	sender.GetData();
	                	setLastChickSendTimestamp(System.currentTimeMillis());
	                }
	                	
	        	}catch(Exception e){}
	        }//run
	    };
	      
/*
 * 
 * 
 * Uploade image to stream
 * 
 * 
 */
//Insert image to DB
	    
public void ImageSendThread(String photo_name){
	    		
	   send_bitmap = photo_name;
	   ControlThread = new Thread(null, ImageThreadProc, "ImageThread");
	   ControlThread.start();
      }

    Runnable ImageThreadProc = new Runnable() { //Resize and sends given image to server
	        public void run() {
	        	
	        	try{	
	        		//Load image from SD
	        		File myDir=new File(android.os.Environment.getExternalStorageDirectory(),"ChickCounter");
	                File f=new File(myDir, send_bitmap);
	                //from SD cache
	                Bitmap b = decodeFile(f);
	        		
	        		//Resize image
	        		final int h = 240; // height in pixels
	        		final int w = 320; // width in pixels    

	                int width_tmp=b.getWidth(), height_tmp=b.getHeight();
	                int scale=1;
	                while(true){
	                    if(width_tmp<h || height_tmp<w)
	                        break;
	                    width_tmp/=2;
	                    height_tmp/=2;
	                    scale*=2;
	                }
	        
	        		Bitmap scaled_photo = Bitmap.createScaledBitmap(b, width_tmp, height_tmp,true);
	        		ByteArrayOutputStream bao = new ByteArrayOutputStream();
	        		scaled_photo.compress(Bitmap.CompressFormat.JPEG, 80, bao);
	        		
	        		//Code bitmap to base64
	        		byte [] ba = bao.toByteArray();
	        		String done_image=Base64.encode(ba);

	        		//Get current location
	        		String lat = "0.0";
    		    	String longi = "0.0";
        		    try{
        		    	lat = Double.toString(getLocation().getLatitude());
        		    	longi = Double.toString(getLocation().getLongitude());
        		    }catch(Exception e){}
        		    
        		    final int size_image = done_image.length();        		    
        		    
        		    String id = md5(done_image);
        		   
        		    sender.nameValuePairs.clear();
	        		sender.setUrl("http://app.nearley.com/chick_counter/set.php");
	        		sender.nameValuePairs.add(new BasicNameValuePair("id",id));
	        		sender.nameValuePairs.add(new BasicNameValuePair("photo",done_image));
	                sender.nameValuePairs.add(new BasicNameValuePair("lat",lat));
	                sender.nameValuePairs.add(new BasicNameValuePair("long",longi));
	                sender.GetData();
	                
	        	}catch(Exception e){
	        		e.toString();
	        	}
				   
	        }// END ImageSendThread
	    };
	    
/*
 * 
 * Get chicks from server
 * 
 * 
 */
	    
	    
public void getServerChicks() {
	       
	   try{	
		   
	        //Get current location
	        String lat = "0.0";
    		String longi = "0.0";
        	   try{
        	    	lat = Double.toString(getLocation().getLatitude());
        	    	longi = Double.toString(getLocation().getLongitude());
        	    }catch(Exception e){ }      		    
        		    //Send this information to cloud
        		    
        		    sender.nameValuePairs.clear();
	        		sender.setUrl("http://gi60s.com/get_chicks.php");
	        		sender.nameValuePairs.add(new BasicNameValuePair("device_id", getDeviceId()));
	                sender.nameValuePairs.add(new BasicNameValuePair("lat",lat));
	                sender.nameValuePairs.add(new BasicNameValuePair("long",longi));
	                sender.GetData(share_class.this);
	 
	        	}catch(Exception e){}		
	    }
	    
	    
	    
/*
 * 
 * Notifications
 * 
 * 	    
 */

	    public void notify_user(String title, String text){
	    	try{
			  mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			  final Notification notifyDetails = new Notification(R.drawable.icon,"polib si",System.currentTimeMillis());
			  //notifyDetails.defaults =Notification.DEFAULT_ALL;
			  notifyDetails.flags |= Notification.FLAG_AUTO_CANCEL;
			  
			  CharSequence contentTitle = title;
			  CharSequence contentText = text;

			  Intent notifyIntent = new Intent(this.getApplicationContext(), dashboard.class);

			  PendingIntent intent =
			  PendingIntent.getActivity(this, 0,notifyIntent, android.content.Intent.FLAG_ACTIVITY_NEW_TASK);

			  notifyDetails.setLatestEventInfo(this.getApplicationContext(), contentTitle, contentText, intent);

			  mNotificationManager.notify(SIMPLE_NOTFICATION_ID, notifyDetails);
	    	}catch(Exception e){
	    		e.toString();
	    	}
		  }	
	    
	   
	    /* ******* Helper methods ********** */
	    
/*
 * 
 * request address method	    
 */
	    
	    
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
	 
	    private Bitmap decodeFile(File f){
	        try {
	        	int orientation = 0;
	            //decode image size
	            BitmapFactory.Options o = new BitmapFactory.Options();
	            o.inJustDecodeBounds = true;
	            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
	            
	            //Find the correct scale value. It should be the power of 2.
	            
	            try {
					ExifInterface exif = new ExifInterface(f.getAbsolutePath());
					
					orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
					
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	            
	            //decode with inSampleSize
	            BitmapFactory.Options o2 = new BitmapFactory.Options();
	           
	            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	            
	            if(orientation == 6){
		            Matrix matrix = new Matrix();
					matrix.postRotate(90);
					return Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), matrix, true);
		        }
	            else{
	            	return b;
	            }
	            
	        } catch (FileNotFoundException e) {}
	        return null;
	    }
	 
public boolean isSameLocation(Double lat,Double longi, Double lat2, Double longi2){
	
	Double distance = distanceTo( lat,longi,lat2,longi2); 
	if(distance <= 0.05){
		//under 50m of diference
		return true;
	}
	else{
		return false;
	}
	
	
}	    
	    
	
public double distanceTo(Double lat,Double longi,Double lat2,Double longi2) {
	Double radius = 6371.01;
	return Math.acos(Math.sin(lat) * Math.sin(lat2) +
			Math.cos(lat) * Math.cos(lat2) *
			Math.cos(longi - longi2)) * radius;
}
	    
	  
}//End of share_class