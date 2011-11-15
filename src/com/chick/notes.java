package com.chick;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.helpers.user_item;

public class notes extends Activity {
	
	share_class sharing_class;
	 
	
	 ImageButton camera;
	 Button map;
     TextView notes_address;
	 TextView notes_date;
	 String actual_id;
	 user_item click;
	 String new_name;
	 Handler m_handler;  
	 public static int TAKE_IMAGE = 111;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);
        
        try{
       
       
       notes_address = (TextView ) findViewById(R.id.textView_address);
  	   notes_date = (TextView ) findViewById(R.id.textView_date);
       
       map = (Button)findViewById(R.id.button_map);
       
       
       camera = (ImageButton) findViewById(R.id.camera_button);
       sharing_class = ((share_class)getApplicationContext());
       
       //GET ID
       actual_id = sharing_class.getChickSelected();
	       
	      
	     
       //THREAD
       //Search for click in DB
       
       click = sharing_class.GetItem(actual_id);
       
	       if(click == null){
	    	   finish(); //Nic nenalezeno v DB
	       }
       
       //Show data
       
	    // our handler
		   m_handler = new Handler() {
		     @Override
		     public void handleMessage(Message msg) {
		    	 
		    	notes_address.setText(click.getAddress());
		    	 
		        }
		    };
		    
	       
	    view_notes(click);
       
	    
	   
	    
       //START CAMERA button listener
       camera.setOnClickListener(new View.OnClickListener() {  
           public void onClick(View v) {  
          	 
        	   StartCamera();
        	          	
           }  
       });
       
       //StartMap
       map.setOnClickListener(new View.OnClickListener() {  
           public void onClick(View v) {  

        		try{
        			
           	sharing_class.setLocation_set(Double.parseDouble(click.getLat()),Double.parseDouble(click.getLong()));
           	Intent i = new Intent().setClass(notes.this, chicks_map_activity.class);
            startActivity(i);
        		}catch(Exception e){
         		   e.getLocalizedMessage();
         	   }
           
        	          	
           }  
       });
     //START CAMERA button listener
  
       /*
       photo1.setOnClickListener(new View.OnClickListener() {  
           public void onClick(View v) {  
          	 
        	   try{
        	   if(sharing_class.getPhotoCache(0).length()<1){
        		   StartCamera();
        	   }
        	   else{
        
        		   
        	   Intent i = new Intent().setClass(notes.this, Image_activity.class);
               startActivity(i);
	
        	   }
        	   }catch(Exception e){
        		   e.getLocalizedMessage();
        	   }
           }  
       });
       */
       
        
        }catch(Exception e){
        	e.getLocalizedMessage();
        }
        
    }
    
    @Override
    public void onDestroy(){
    	save_notes();
    	super.onDestroy();
    }
    
    
    @Override
    public void onResume(){
    	actual_id = sharing_class.getChickSelected();
    	click = sharing_class.GetItem(actual_id);
    	view_notes(click);
    	super.onResume();
    }
    
 public void StartCamera(){
    	
    	try {
    		Random RanNum = new Random();
		    new_name = sharing_class.md5(Integer.toString(RanNum.nextInt()));
		    new_name = new_name.substring(0,10); 
		    new_name = new_name + ".jpg";
		    sharing_class.setChickPhoto(new_name);
		    
		    //kontrola a vytvoreni ChickCounter dir
		    File Dir;
		    if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
	            Dir=new File(android.os.Environment.getExternalStorageDirectory(),"ChickCounter");
	            
	            if(!Dir.exists()){
		        	Dir.mkdirs();
		        }
		    }
		    else{
		    	//TODO No SDCARD in phone
		    }
		    
    		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    	    File photo = new File(Environment.getExternalStorageDirectory()+"/ChickCounter",  new_name);
    	    intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(photo));
    	   
    	    startActivityForResult(intent, TAKE_IMAGE);
    	    
    	} catch (Exception e) {
    	    Log.e("", "", e);	
        }
    }//start camera
    
    protected void onActivityResult(int requestCode, int resultCode,Intent intent) {
        if ((requestCode == TAKE_IMAGE)
                && (resultCode == RESULT_OK)) {
        	
        	//load image in view
        	
        	sharing_class.addPhotoCache(sharing_class.getChickPhoto());
        	//Start sending image
        	sharing_class.ImageSendThread(sharing_class.getChickPhoto());
        	view_notes(click);
        }
        else{
        	//Image not saved or not captured
        	
        }
    }
    
    private Bitmap getBitmap(String url) 
    {
        //I identify images by hashcode. Not a perfect solution, good for the demo.
        //String filename=String.valueOf(url.hashCode());
        
    	
        File myDir=new File(android.os.Environment.getExternalStorageDirectory(),"ChickCounter");
        File f=new File(myDir, url);
        
        //from SD cache
        Bitmap b = decodeFile(f);
        return b;

    }
    
  //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f){
        try {
        	int orientation = 0;
            //decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
            
            //Find the correct scale value. It should be the power of 2.
            final int REQUIRED_SIZE=70;
            int width_tmp=o.outWidth, height_tmp=o.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            
            try {
				ExifInterface exif = new ExifInterface(f.getAbsolutePath());
				
				orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
				
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            //decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize=scale;
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
    
    void view_notes(user_item click_in){
    	
    	
    	
    	//Show address
    	notes_address.setText(click_in.getAddress().toString()) ;
    	
    	
    	//Show date
    	notes_date.setText(click_in.getDate().toString());
    	

    	save_notes();

    } 
    
    void save_notes(){
    	
    	sharing_class.UpdateDB(actual_id,click.getAddress(),click.getLat(),click.getLong());
   	    
    }

    
    Runnable AddressThreadProc = new Runnable() {
        public void run() {
        	
        	try{
        		String newAddres = sharing_class.requestAdress(sharing_class.location_set_lat, sharing_class.location_set_long);
        		click.setAddress(newAddres);
        		m_handler.sendMessage(m_handler.obtainMessage());
        	}catch(Exception e){
        		e.getLocalizedMessage();
        	}
        	
        };
    };
        	
}