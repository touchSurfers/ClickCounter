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
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.helpers.user_item;

public class notes extends Activity {
	
	share_class sharing_class;
	 EditText notes;
	 ImageView  photo1;
	 ImageView  photo2;
	 ImageView  photo3;
	 Button camera;
	 Button save;
	 String actual_id;
	 user_item click;
	 String new_name;
	 RatingBar ratingBar;
	
	 public static int TAKE_IMAGE = 111;
	
	
		
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notes);
        
        try{
       notes = (EditText) findViewById(R.id.notes);
       photo1 = (ImageView ) findViewById(R.id.photo1);
       photo2 = (ImageView ) findViewById(R.id.photo2);
       photo3 = (ImageView ) findViewById(R.id.photo3);
       ratingBar = (RatingBar)findViewById(R.id.ratingBar);
       
       camera = (Button) findViewById(R.id.camera_button);
       save = (Button) findViewById(R.id.save_button);
       sharing_class = ((share_class)getApplicationContext());
       
       //GET ID
       actual_id = sharing_class.getChickSelected();
	       
	      
	       Toast.makeText(notes.this, actual_id, Toast.LENGTH_SHORT).show();
       //THREAD
       //Search for click in DB
       
       click = sharing_class.GetItem(actual_id);
       
	       if(click == null){
	    	   finish(); //Nic nenalezeno v DB
	       }
       
       //Show data
       
	    view_notes(click);
       
       //START CAMERA button listener
       camera.setOnClickListener(new View.OnClickListener() {  
           public void onClick(View v) {  
          	 
        	   StartCamera();
        	          	
           }  
       });
       
     //START CAMERA button listener
       save.setOnClickListener(new View.OnClickListener() {  
           public void onClick(View v) {  
          	 
        	   save_notes();
        	          	
           }  
       });

       
       
       photo1.setOnClickListener(new View.OnClickListener() {  
           public void onClick(View v) {  
          	 
        	   try{
        	   if(sharing_class.getPhotoCache(0).length()<1){
        		   return;
        	   }
        	   File photo = new File(Environment.getExternalStorageDirectory()+"/ChickCounter", sharing_class.getPhotoCache(0));
        	
        	   //Intent intent = new Intent(Intent.ACTION_VIEW,Uri.fromFile(photo));
        	   Intent intent = new Intent();
        	   intent.setAction(android.content.Intent.ACTION_VIEW);
        	   intent.setDataAndType(Uri.fromFile(photo), "image/jpg");
        	   
        	   startActivity(intent);
        	          	
        	   }catch(Exception e){
        		   e.getLocalizedMessage();
        	   }
           }  
       });
       
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
    	
    	String notes_text = click.getNotes();
        if(notes_text!=""){
     	   notes.setText(notes_text);
        }
        
        String rating_text = click.getRating();
        try{
        ratingBar.setRating(Float.valueOf(rating_text));
        }catch(Exception e){
        	e.getLocalizedMessage();
        }
    
    	if(!(sharing_class.countPhotoCache()>0)){
    		//Init cache
    		sharing_class.addPhotoCache(click.getPhoto(3));
    		sharing_class.addPhotoCache(click.getPhoto(2));
    		sharing_class.addPhotoCache(click.getPhoto(1)); 
    		
    	}

    	for(int i=0;i<sharing_class.countPhotoCache();i++){
			 
			 if(i == 0){
				 if(sharing_class.getPhotoCache(0).length()>0){
				 photo1.setImageBitmap(getBitmap(sharing_class.getPhotoCache(0)));
				 }
			 	 }
			 
			 if(i == 1){
				 if(sharing_class.getPhotoCache(1).length()>0){
				 photo2.setImageBitmap(getBitmap(sharing_class.getPhotoCache(1)));
				 }
			 	 }
			 
			 if(i == 2){
				 if(sharing_class.getPhotoCache(2).length()>0){
				 photo3.setImageBitmap(getBitmap(sharing_class.getPhotoCache(2)));
				 }
			 	 }
		 }
    	
    	
        //TODO 
      
        
    	
    	
    } 
    
    void save_notes(){
    	
    	//Rating from UI
    	 Float rate = ratingBar.getRating();
    	String rating_text = Float.toString(rate);
        click.setRating(rating_text);
    	
    	sharing_class.UpdateDB(actual_id,notes.getText().toString(),sharing_class.getPhotoCache(0),sharing_class.getPhotoCache(1),sharing_class.getPhotoCache(2), click.getRating(),click.getAddress());
   	    
    }

}