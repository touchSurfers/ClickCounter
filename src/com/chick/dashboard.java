package com.chick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class dashboard extends Activity {
	
	share_class sharing_class;
	    TextView counter;
   		ImageButton chickPlus;
   		ImageButton chickList;
   		ImageButton chickNotes;
   		com.UI.SliderUI slider_button;
   		
   		
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 145584;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        counter = (TextView) findViewById(R.id.counter_view);
    	chickPlus = (ImageButton) findViewById(R.id.chick_plus);
    	chickList = (ImageButton) findViewById(R.id.chick_list);
    	chickNotes = (ImageButton) findViewById(R.id.chick_notes);
    	slider_button = (com.UI.SliderUI) findViewById(R.id.slider_button);
    	
    	
        sharing_class = ((share_class)getApplicationContext());
        
       // ResetCounter();
        //sharing_class.DeleteDB();
        
        InitCounter();
        if(sharing_class.getChickCount() < 1){
        	chickNotes.setEnabled(false);
        }
        
        //slider_button

       
        slider_button.setOnColorChangedListener(new com.UI.SliderUI.OnColorChangedListener() {

         @Override
           public void onResetHit(View v) {
        	    ResetCounter();
                //sharing_class.DeleteDB();
           }
        });

        
        chickPlus.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
   
            	IncrementCounter();
            	
            	if(sharing_class.getChickCount() > 0){
            		chickNotes.setEnabled(true);
                }
            	
            	//Run thread Insert click to sql and send to cloud
            	sharing_class.InsertThread();
           	  
            	//TODO

            	//vrch v notes 
            	//Funkce predni obrazovky
            	//doresit zobrazeni listu
            	//Add notes tlacitko v predu
            	
            	//udelat PHP server
            	//Prepocitavat num of chicks v mape se zoomem, cim dal tim snizuju presnost a zaokrouhluju

            	//OPTIMALIZACE****
            	//try catch
            	//If timestamp is OLD (10 minutes) else return with old jsno_class GetData()
            	//Loading progress for images and map
            	
            	
            	//BUGS****
            	//ochrana kdyz naklikas hodne obrazku
            	//pri prechodu mezi taby dochazi k errorum - nesedi ID
            	
            	
            	//IN APP****
            	//In app payment doresit
            	//In app payment hlasky a button
            	//Filtr funkci podle stavu zaplaceni app
            	//pridat paid/notpaid promenou
            	//notification
            	
            	
            	//STYLE***
            	//obrazek zesedly pokud neni zadny v profilu
            	//Navrhnout add notes button na hlavni stranku
            	//Rozsvitit add notes button pokud je aktivni
            	//resize
            	//v mape dodelat zobrazeni cisla poctu chicks
            	
            	//dodelat vsechny TODO v celem solution          	
            }  
        });
        
        chickList.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
           	 
            	 
            	//Start history activity
            	
            	sharing_class.clearPhotoCache();
            	Intent i = new Intent().setClass(dashboard.this, history.class);
             	startActivity(i);
             	
            	/*
            	sharing_class.clearPhotoCache();
            	Intent i = new Intent().setClass(dashboard.this, chicks_map_activity.class);
             	startActivity(i);
            	*/
            	
            }  
        });
        
        chickNotes.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
           	 
            	 
            	//Start notes activity
            	sharing_class.clearPhotoCache();
            	sharing_class.setChickSelected(sharing_class.getChickID());
            	Intent i = new Intent().setClass(dashboard.this, notes.class);
             	startActivity(i);
            	
            	
            }  
        });
        
    }
    
    public void IncrementCounter(){
    	int i = sharing_class.getChickCount();
    	i++;
    	sharing_class.setChickCount(i);
    	SetCouterValue(i);
    }
    
    public void ResetCounter(){
    	sharing_class.setChickCount(0);
    	sharing_class.setChickID("");
    	InitCounter();
    	if(sharing_class.getChickCount() < 1){
        	chickNotes.setEnabled(false);
        }
    }
    
    public void InitCounter(){
    	SetCouterValue(sharing_class.getChickCount());
    }
    
    public void SetCouterValue(int count){

    	String value = Integer.toString(count);
    	int nulls = 4 - (value.length());
    	
    	String addNulls = "";
    	for(int i=0;i<nulls;i++){
    		addNulls += "0"; 
    	}
    	
    	addNulls += value;
        counter.setText(addNulls);
        
    }
    
   
    
    
}