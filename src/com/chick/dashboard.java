package com.chick;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class dashboard extends Activity {
	
	share_class sharing_class;
	    TextView counter;
	    TextView clock_view;
   		ImageButton chickPlus;
   		ImageButton chickList;
   		ImageButton chickNotes;
   		ImageButton chickMap;
   		com.UI.SliderUI slider_button;
   		
   		
   		ImageView num_1;
   		ImageView num_2;
   		ImageView num_3;
   		ImageView num_4;
   		ImageView num_5;
   		
   		LocationManager locationManager;
   		
   		private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; //metru
   		private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; //milis
   		
   		private Handler mHandler ;
   		long mStartTime;
   		
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 145584;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
     
    	chickPlus = (ImageButton) findViewById(R.id.chick_plus);
    	chickList = (ImageButton) findViewById(R.id.chick_map_button);
    	chickNotes = (ImageButton) findViewById(R.id.chick_add);
    	chickMap = (ImageButton) findViewById(R.id.chick_list);
    	slider_button = (com.UI.SliderUI) findViewById(R.id.slider_button);
    	clock_view = (TextView) findViewById(R.id.clock_view);
    	
    	num_1 = (ImageView) findViewById(R.id.image_num_1);
    	num_2 = (ImageView) findViewById(R.id.image_num_2);
    	num_3 = (ImageView) findViewById(R.id.image_num_3);
    	num_4 = (ImageView) findViewById(R.id.image_num_4);
    	num_5 = (ImageView) findViewById(R.id.image_num_5);
    	
        sharing_class = ((share_class)getApplicationContext());
        mHandler = new Handler();
      //Start geolocation
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);      
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MINIMUM_TIME_BETWEEN_UPDATES,MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, new GeoUpdateHandler());
        Location last_loc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        sharing_class.setLocation(last_loc);
        sharing_class.setAddress(  sharing_class.requestAdress( last_loc.getLatitude() ,last_loc.getLongitude() ) );
        

        	
        InitCounter();//Musi byt volany jako prvni
        
        
        if(sharing_class.getChickCount() < 1){
        	chickNotes.setEnabled(false);
        }
        
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 100);
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


            	
            	//Spravne znicit timer http://android-developers.blogspot.com/2007/11/stitch-in-time.html
            	//pridat onResume a onEnd metody do dashboard a zastavit/rozbehnout v nich timer
            	//Payment logiku
            	//Rozchodit in-app payment
            	
            	
            	//Co jeste dat do mapy? - modal s poctem chicks kolem
            	
            	//notifikace jako visit US, get PRO
            	//server
            	
            	//Help ktery se zobrazi na zacatku
            	
            	//pocet Chicks v listu
            	//v mape dodelat zobrazeni cisla poctu chicks
            	//Mapa - po kliknuti na pin, otevrit profil ,pokud je pinu vic (agregace, tak modal okno s Listem)
            	//Ukladat fotky do zvlastni galerie?/slozky?
            	
            	
            	//IN APP****
            	//In app payment doresit
            	//In app payment hlasky a button
            	//Filtr funkci podle stavu zaplaceni app
            	//pridat paid/notpaid promenou
            	//notification
            	//http://developer.android.com/guide/market/billing/billing_integrate.html
            	
            	//Pridat iAd (List,Mapa) AZ PO PUBLISH (AdMob.com xkucht04)
            	
            	
            	//BUGS****
            	//ochrana kdyz naklikas hodne obrazku
            	
            
            	
            	
            	
            	//udelat PHP server
            	//Rozchodit napojeni na server
            	

            	
            	//OPTIMALIZACE****
            	//try catch
            	//udelat payment dialog lehce pruhledny
            	//If timestamp is OLD (10 minutes) else return with old jason_class GetData()
            	//Loading progress for images and map
            	//Reset button animovat
            	//Podle : http://mobile.tutsplus.com/tutorials/android/android-gesture/
            	

            	//STYLE***
            	//profile vrch
            	//pismo
            	//ikonky
            	//tlacitko mapa
            
            
            	//Taby nahore resize
            	//Vybrat vhodny avatar
            	//prodat kompasek do vsech maps
            	//Zvuk tlacitek http://www.mybringback.com/tutorial-series/531/android-the-basics-11-button-sound-review-final/
           
            	//obrazek zesedly pokud neni zadny v profilu
            	//Rozsvitit aktivni buttony
            	//resize

            	//dodelat vsechny TODO v celem solution          	
            }  
        });
        
        chickList.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
           	 
            	 
            	//Start history activity
            	
            	sharing_class.clearPhotoCache();
            	Intent i = new Intent().setClass(dashboard.this, history.class);
             	startActivity(i);
             	
            	
            	
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
        
        chickMap.setOnClickListener(new View.OnClickListener() {  
            public void onClick(View v) {  
           	 
            	 
            	
            	
            	Intent i = new Intent().setClass(dashboard.this, chicks_map_activity2.class);
             	startActivity(i);
            	
            	
            	
            }  
        });
        
    }
    
    public void IncrementCounter(){
    	int i = sharing_class.getChickCount();
    	i++;
    	sharing_class.setChickCount(i);
    	SetCounterDisplayIncrement(i);
    	
    	if(sharing_class.getChickTimer() == 0){
    		sharing_class.setChickTimer(System.currentTimeMillis());
    	}
    }
    
    public void ResetCounter(){
    	resetInitCounter();
    	sharing_class.setChickCount(0);
    	sharing_class.setChickID("");
    	sharing_class.setChickTimer(0);
    	if(sharing_class.getChickCount() < 1){
        	chickNotes.setEnabled(false);
        }
    }
    
    public void InitCounter(){
    	SetCounterDisplayInit(sharing_class.getChickCount());
    }
    
    public void resetInitCounter(){
    	if(sharing_class.getChickCount() == 0){
    	SetCounterDisplayIncrement(99999);
    	SetCounterDisplayIncrement(00000);
    	}
    	else{
    		SetCounterDisplayIncrement(00000);
    	}
    }
    
    public String SetCouterValue(int count){

    	String value = Integer.toString(count);
    	int nulls = 5 - (value.length());
    	
    	String addNulls = "";
    	for(int i=0;i<nulls;i++){
    		addNulls += "0"; 
    	}
    	
    	addNulls += value;
        
        return addNulls;
    }
    
    
   
     public class GeoUpdateHandler implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {	
		try{
			
			//Zjistit adresu
			sharing_class.setLocation(location);
			sharing_class.setAddress(  sharing_class.requestAdress( location.getLatitude() ,location.getLongitude() ) );
			
			
		}catch(Exception e){}
		finally{}		
		}

		@Override
		public void onProviderDisabled(String provider) {
			//Provider disabled by the user. GPS or WIFI turned off	
			//CheckProvider();
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			//Provider enabled by the user. GPS or WIFI turned on
			//CheckProvider();
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			//Provider status changed
			//CheckProvider();
		}
	}// geo listener
	
    
     private Runnable mUpdateTimeTask = new Runnable() {
    	 public void run() {
    		   mStartTime = System.currentTimeMillis();
    	       if(sharing_class.getChickTimer() != 0){
	    	       long millis = mStartTime - sharing_class.getChickTimer() ;
	    	       int seconds = (int) (millis / 1000);
	    	       int minutes = seconds / 60;
	    	       seconds     = seconds % 60;
	
	    	       if (seconds < 10) {
	    	           clock_view.setText("" + minutes + ":0" + seconds);
	    	       } else {
	    	           clock_view.setText("" + minutes + ":" + seconds);            
	    	       }
    	       }
    	       else{
    	    	   clock_view.setText("0:00");
    	       }
    	       final long next_start = SystemClock.uptimeMillis();
    	       mHandler.postAtTime(this,next_start+1000);
    	   }
    	};
    	
    
public void SetCounterDisplayInit(int count){
    	
    	String strValue;
    	
    	if(count == 0){
    		strValue = "00000";	
    	}
    	else{
    		strValue = SetCouterValue(count);
    	}
    	
    	
    	
    	//NUM 1
    	if(strValue.charAt(0) == '0'){
    		
    		num_1.setBackgroundResource(R.drawable.num_anim_9_0_7);
    		
    		
    	}
    	if(strValue.charAt(0) == '1'){
    		
    		num_1.setBackgroundResource(R.drawable.num_anim_0_1_7);
    		
    		
    	}
    	if(strValue.charAt(0) == '2'){
    		num_1.setBackgroundResource(R.drawable.num_anim_1_2_7);
    		
    	}
    	if(strValue.charAt(0) == '3'){
    		num_1.setBackgroundResource(R.drawable.num_anim_2_3_7);
    		
    	}
    	if(strValue.charAt(0) == '4'){
    		num_1.setBackgroundResource(R.drawable.num_anim_3_4_7);
    		
    	}
    	if(strValue.charAt(0) == '5'){
    		num_1.setBackgroundResource(R.drawable.num_anim_4_5_7);
    		
    	}
    	if(strValue.charAt(0) == '6'){
    		num_1.setBackgroundResource(R.drawable.num_anim_5_6_7);
    	
    	}
    	if(strValue.charAt(0) == '7'){
    		num_1.setBackgroundResource(R.drawable.num_anim_6_7_7);
    		
    	}
    	if(strValue.charAt(0) == '8'){
    		num_1.setBackgroundResource(R.drawable.num_anim_7_8_7);
    		
    	}
    	if(strValue.charAt(0) == '9'){
    		num_1.setBackgroundResource(R.drawable.num_anim_8_9_7);
    	
    	}
    	
    	
    	//NUM 2
    	if(strValue.charAt(1) == '0'){
    		num_2.setBackgroundResource(R.drawable.num_anim_9_0_7);
    		
    	}
    	if(strValue.charAt(1) == '1'){
    		num_2.setBackgroundResource(R.drawable.num_anim_0_1_7);
    		
    	}
    	if(strValue.charAt(1) == '2'){
    		num_2.setBackgroundResource(R.drawable.num_anim_1_2_7);
    	
    	}
    	if(strValue.charAt(1) == '3'){
    		num_2.setBackgroundResource(R.drawable.num_anim_2_3_7);
    		
    	}
    	if(strValue.charAt(1) == '4'){
    		num_2.setBackgroundResource(R.drawable.num_anim_3_4_7);
    	
    	}
    	if(strValue.charAt(1) == '5'){
    		num_2.setBackgroundResource(R.drawable.num_anim_4_5_7);
    		
    	}
    	if(strValue.charAt(1) == '6'){
    		num_2.setBackgroundResource(R.drawable.num_anim_5_6_7);
    	
    	}
    	if(strValue.charAt(1) == '7'){
    		num_2.setBackgroundResource(R.drawable.num_anim_6_7_7);
    	
    	}
    	if(strValue.charAt(1) == '8'){
    		num_2.setBackgroundResource(R.drawable.num_anim_7_8_7);
    	
    	}
    	if(strValue.charAt(1) == '9'){
    		num_2.setBackgroundResource(R.drawable.num_anim_8_9_7);
    	
    	}
    	
    	
    	//NUM 3
    	if(strValue.charAt(2) == '0'){
    		num_3.setBackgroundResource(R.drawable.num_anim_9_0_7);
    	
    	}
    	if(strValue.charAt(2) == '1'){
    		num_3.setBackgroundResource(R.drawable.num_anim_0_1_7);
    		
    	}
    	if(strValue.charAt(2) == '2'){
    		num_3.setBackgroundResource(R.drawable.num_anim_1_2_7);
    		
    	}
    	if(strValue.charAt(2) == '3'){
    		num_3.setBackgroundResource(R.drawable.num_anim_2_3_7);
    	
    	}
    	if(strValue.charAt(2) == '4'){
    		num_3.setBackgroundResource(R.drawable.num_anim_3_4_7);
    	
    	}
    	if(strValue.charAt(2) == '5'){
    		num_3.setBackgroundResource(R.drawable.num_anim_4_5_7);
    	
    	}
    	if(strValue.charAt(2) == '6'){
    		num_3.setBackgroundResource(R.drawable.num_anim_5_6_7);
    	
    	}
    	if(strValue.charAt(2) == '7'){
    		num_3.setBackgroundResource(R.drawable.num_anim_6_7_7);
    	
    	}
    	if(strValue.charAt(2) == '8'){
    		num_3.setBackgroundResource(R.drawable.num_anim_7_8_7);
    		
    	}
    	if(strValue.charAt(2) == '9'){
    		num_3.setBackgroundResource(R.drawable.num_anim_8_9_7);
    		
    	}
    	
    	//NUM 4
    	if(strValue.charAt(3) == '0'){
    		num_4.setBackgroundResource(R.drawable.num_anim_9_0_7);
    		
    	}
    	if(strValue.charAt(3) == '1'){
    		num_4.setBackgroundResource(R.drawable.num_anim_0_1_7);
    		
    	}
    	if(strValue.charAt(3) == '2'){
    		num_4.setBackgroundResource(R.drawable.num_anim_1_2_7);
    		
    	}
    	if(strValue.charAt(3) == '3'){
    		num_4.setBackgroundResource(R.drawable.num_anim_2_3_7);
    	
    	}
    	if(strValue.charAt(3) == '4'){
    		num_4.setBackgroundResource(R.drawable.num_anim_3_4_7);
    		
    	}
    	if(strValue.charAt(3) == '5'){
    		num_4.setBackgroundResource(R.drawable.num_anim_4_5_7);
    		
    	}
    	if(strValue.charAt(3) == '6'){
    		num_4.setBackgroundResource(R.drawable.num_anim_5_6_7);
    		
    	}
    	if(strValue.charAt(3) == '7'){
    		num_4.setBackgroundResource(R.drawable.num_anim_6_7_7);
    		
    	}
    	if(strValue.charAt(3) == '8'){
    		num_4.setBackgroundResource(R.drawable.num_anim_7_8_7);
    		
    	}
    	if(strValue.charAt(3) == '9'){
    		num_4.setBackgroundResource(R.drawable.num_anim_8_9_7);
    		
    	}
    	
    	//NUM 5
    	if(strValue.charAt(4) == '0'){
    		num_5.setBackgroundResource(R.drawable.num_anim_9_0_7);
    		
    	}
    	if(strValue.charAt(4) == '1'){
    		num_5.setBackgroundResource(R.drawable.num_anim_0_1_7);
    		
    	}
    	if(strValue.charAt(4) == '2'){
    		
    		num_5.setBackgroundResource(R.drawable.num_anim_1_2_7);
    	

    	}
    	if(strValue.charAt(4) == '3'){
    		num_5.setBackgroundResource(R.drawable.num_anim_2_3_7);
    	
    	}
    	if(strValue.charAt(4) == '4'){
    		num_5.setBackgroundResource(R.drawable.num_anim_3_4_7);
    	
    	}
    	if(strValue.charAt(4) == '5'){
    		num_5.setBackgroundResource(R.drawable.num_anim_4_5_7);
    	
    	}
    	if(strValue.charAt(4) == '6'){
    		num_5.setBackgroundResource(R.drawable.num_anim_5_6_7);
    	
    	}
    	if(strValue.charAt(4) == '7'){
    		num_5.setBackgroundResource(R.drawable.num_anim_6_7_7);
    	
    	}
    	if(strValue.charAt(4) == '8'){
    		num_5.setBackgroundResource(R.drawable.num_anim_7_8_7);
    		
    	}
    	if(strValue.charAt(4) == '9'){
    		num_5.setBackgroundResource(R.drawable.num_anim_8_9_7);
    		
    	}
    	
    	
    	
    }

    public void SetCounterDisplayIncrement(int count){
    	
    	String strValue;
    	
    	if(count == 0){
    		strValue = "00000";	
    	}
    	else{
    		strValue = SetCouterValue(count);
    	}
    	
    	
    	
    	//NUM 1
    	if(strValue.charAt(0) == '0'){
    		
    		num_1.setBackgroundResource(R.drawable.b_num_anim_9_0);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_1.getBackground();
    		frameAnimation.start();
    		
    	}
    	if(strValue.charAt(0) == '1'){
    		
    		num_1.setBackgroundResource(R.drawable.b_num_anim_0_1);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_1.getBackground();
    		frameAnimation.start();
    		
    	}
    	if(strValue.charAt(0) == '2'){
    		num_1.setBackgroundResource(R.drawable.b_num_anim_1_2);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_1.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(0) == '3'){
    		num_1.setBackgroundResource(R.drawable.b_num_anim_2_3);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_1.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(0) == '4'){
    		num_1.setBackgroundResource(R.drawable.b_num_anim_3_4);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_1.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(0) == '5'){
    		num_1.setBackgroundResource(R.drawable.b_num_anim_4_5);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_1.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(0) == '6'){
    		num_1.setBackgroundResource(R.drawable.b_num_anim_5_6);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_1.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(0) == '7'){
    		num_1.setBackgroundResource(R.drawable.b_num_anim_6_7);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_1.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(0) == '8'){
    		num_1.setBackgroundResource(R.drawable.b_num_anim_7_8);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_1.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(0) == '9'){
    		num_1.setBackgroundResource(R.drawable.b_num_anim_8_9);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_1.getBackground();
    		frameAnimation.start();
    	}
    	
    	
    	//NUM 2
    	if(strValue.charAt(1) == '0'){
    		num_2.setBackgroundResource(R.drawable.b_num_anim_9_0);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_2.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(1) == '1'){
    		num_2.setBackgroundResource(R.drawable.b_num_anim_0_1);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_2.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(1) == '2'){
    		num_2.setBackgroundResource(R.drawable.b_num_anim_1_2);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_2.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(1) == '3'){
    		num_2.setBackgroundResource(R.drawable.b_num_anim_2_3);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_2.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(1) == '4'){
    		num_2.setBackgroundResource(R.drawable.b_num_anim_3_4);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_2.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(1) == '5'){
    		num_2.setBackgroundResource(R.drawable.b_num_anim_4_5);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_2.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(1) == '6'){
    		num_2.setBackgroundResource(R.drawable.b_num_anim_5_6);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_2.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(1) == '7'){
    		num_2.setBackgroundResource(R.drawable.b_num_anim_6_7);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_2.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(1) == '8'){
    		num_2.setBackgroundResource(R.drawable.b_num_anim_7_8);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_2.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(1) == '9'){
    		num_2.setBackgroundResource(R.drawable.b_num_anim_8_9);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_2.getBackground();
    		frameAnimation.start();
    	}
    	//NUM 3
    	if(strValue.charAt(2) == '0'){
    		num_3.setBackgroundResource(R.drawable.b_num_anim_9_0);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_3.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(2) == '1'){
    		num_3.setBackgroundResource(R.drawable.b_num_anim_0_1);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_3.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(2) == '2'){
    		num_3.setBackgroundResource(R.drawable.b_num_anim_1_2);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_3.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(2) == '3'){
    		num_3.setBackgroundResource(R.drawable.b_num_anim_2_3);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_3.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(2) == '4'){
    		num_3.setBackgroundResource(R.drawable.b_num_anim_3_4);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_3.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(2) == '5'){
    		num_3.setBackgroundResource(R.drawable.b_num_anim_4_5);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_3.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(2) == '6'){
    		num_3.setBackgroundResource(R.drawable.b_num_anim_5_6);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_3.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(2) == '7'){
    		num_3.setBackgroundResource(R.drawable.b_num_anim_6_7);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_3.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(2) == '8'){
    		num_3.setBackgroundResource(R.drawable.b_num_anim_7_8);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_3.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(2) == '9'){
    		num_3.setBackgroundResource(R.drawable.b_num_anim_8_9);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_3.getBackground();
    		frameAnimation.start();
    	}
    	
    	//NUM 4
    	if(strValue.charAt(3) == '0'){
    		num_4.setBackgroundResource(R.drawable.b_num_anim_9_0);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_4.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(3) == '1'){
    		num_4.setBackgroundResource(R.drawable.b_num_anim_0_1);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_4.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(3) == '2'){
    		num_4.setBackgroundResource(R.drawable.b_num_anim_1_2);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_4.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(3) == '3'){
    		num_4.setBackgroundResource(R.drawable.b_num_anim_2_3);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_4.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(3) == '4'){
    		num_4.setBackgroundResource(R.drawable.b_num_anim_3_4);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_4.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(3) == '5'){
    		num_4.setBackgroundResource(R.drawable.b_num_anim_4_5);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_4.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(3) == '6'){
    		num_4.setBackgroundResource(R.drawable.b_num_anim_5_6);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_4.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(3) == '7'){
    		num_4.setBackgroundResource(R.drawable.b_num_anim_6_7);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_4.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(3) == '8'){
    		num_4.setBackgroundResource(R.drawable.b_num_anim_7_8);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_4.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(3) == '9'){
    		num_4.setBackgroundResource(R.drawable.b_num_anim_8_9);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_4.getBackground();
    		frameAnimation.start();
    	}
    	
    	//NUM 5
    	if(strValue.charAt(4) == '0'){
    		num_5.setBackgroundResource(R.drawable.b_num_anim_9_0);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_5.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(4) == '1'){
    		num_5.setBackgroundResource(R.drawable.b_num_anim_0_1);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_5.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(4) == '2'){
    		
    		num_5.setBackgroundResource(R.drawable.b_num_anim_1_2);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_5.getBackground();
    		frameAnimation.start();

    	}
    	if(strValue.charAt(4) == '3'){
    		num_5.setBackgroundResource(R.drawable.b_num_anim_2_3);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_5.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(4) == '4'){
    		num_5.setBackgroundResource(R.drawable.b_num_anim_3_4);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_5.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(4) == '5'){
    		num_5.setBackgroundResource(R.drawable.b_num_anim_4_5);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_5.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(4) == '6'){
    		num_5.setBackgroundResource(R.drawable.b_num_anim_5_6);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_5.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(4) == '7'){
    		num_5.setBackgroundResource(R.drawable.b_num_anim_6_7);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_5.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(4) == '8'){
    		num_5.setBackgroundResource(R.drawable.b_num_anim_7_8);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_5.getBackground();
    		frameAnimation.start();
    	}
    	if(strValue.charAt(4) == '9'){
    		num_5.setBackgroundResource(R.drawable.b_num_anim_8_9);
    		AnimationDrawable frameAnimation = (AnimationDrawable) num_5.getBackground();
    		frameAnimation.start();
    	}
    	
    	
    	
    }
   
    
    
}