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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.helpers.user_item;

public class notes extends Activity {
	
	share_class sharing_class;
	 EditText notes;
	 ImageView  photo1;
	 ImageView  photo2;
	 ImageView  photo3;
	 ImageButton camera;
	 ImageButton edittext;
	 TextView notes_plus;
	 
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
       
       photo1 = (ImageView ) findViewById(R.id.photo1);
       photo2 = (ImageView ) findViewById(R.id.photo2);
       photo3 = (ImageView ) findViewById(R.id.photo3);
       ratingBar = (RatingBar)findViewById(R.id.ratingBar);
       
       notes_plus = (TextView)findViewById(R.id.notes_plus);
       
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
       
	    view_notes(click);
       
	    
	    notes_plus.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
           	 
            	 
            	//Start history activity
            
            	sharing_class.SetNote(click.getNotes().toString());
            	Intent i = new Intent().setClass(notes.this, Editbox_activity.class);
             	startActivity(i);
            	
            	
            }  
        });
	    
       //START CAMERA button listener
       camera.setOnClickListener(new View.OnClickListener() {  
           public void onClick(View v) {  
          	 
        	   StartCamera();
        	          	
           }  
       });
       
     //START CAMERA button listener
  
       photo1.setOnClickListener(new View.OnClickListener() {  
           public void onClick(View v) {  
          	 
        	   try{
        	   if(sharing_class.getPhotoCache(0).length()<1){
        		   StartCamera();
        	   }
        	   else{
        	   /*
        	   File photo = new File(Environment.getExternalStorageDirectory()+"/ChickCounter", sharing_class.getPhotoCache(0));
        	
        	   //Intent intent = new Intent(Intent.ACTION_VIEW,Uri.fromFile(photo));
        	   Intent intent = new Intent();
        	   intent.setAction(android.content.Intent.ACTION_VIEW);
        	   intent.setDataAndType(Uri.fromFile(photo), "image/jpg");
        	   
        	   startActivity(intent);
        	   */
        		   
        	   Intent i = new Intent().setClass(notes.this, Image_activity.class);
               startActivity(i);
	
        	   }
        	   }catch(Exception e){
        		   e.getLocalizedMessage();
        	   }
           }  
       });
       
       photo2.setOnClickListener(new View.OnClickListener() {  
           public void onClick(View v) {  
          	 
        	   try{
        	   if(sharing_class.getPhotoCache(1).length()<1){
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
       
       photo3.setOnClickListener(new View.OnClickListener() {  
           public void onClick(View v) {  
          	 
        	   try{
        	   if(sharing_class.getPhotoCache(2).length()<1){
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
       
        }catch(Exception e){
        	e.getLocalizedMessage();
        }
        
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating,
					boolean fromUser) {
				
				// TODO Auto-generated method stub
				save_notes();
			}
		});
        
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
    	
    	if(sharing_class.GetNote().length()!=0){
    	    click.setNotes(sharing_class.GetNote().toString());
    	    sharing_class.SetNote("");
    	}
    	
    	
        if(click.getNotes().length()!=0){
        	notes_plus.setText(click.getNotes());
        }
        else{
        	notes_plus.setText("Click to add notes..");
        	click.setNotes("");
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
    	
    	
    	save_notes();
      
        
    	
    	
    } 
    
    void save_notes(){
    	
    	//Rating from UI
    	 Float rate = ratingBar.getRating();
    	String rating_text = Float.toString(rate);
        click.setRating(rating_text);
    	
    	sharing_class.UpdateDB(actual_id,click.getNotes().toString(),sharing_class.getPhotoCache(0),sharing_class.getPhotoCache(1),sharing_class.getPhotoCache(2), click.getRating(),click.getAddress());
   	    
    }

}