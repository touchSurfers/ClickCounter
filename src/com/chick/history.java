package com.chick;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import com.helpers.LazyAdapter;
import com.helpers.list_item;
import com.helpers.user_item;

public class history extends Activity {
	
	 ListView list;
	 LazyAdapter adapter;
	 share_class sharing_class;
	 LinkedList<user_item> clicks;
	 
	 //Buy dialog advert
	 Button start_map;
	 ImageButton buy_button;
	 ImageView buy_dialog;
	 
	 int paid = 0;
	 

	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.hisotry);
	        
	        try{
	        sharing_class = ((share_class)getApplicationContext());
	        
	        start_map = (Button)findViewById(R.id.button_map);
	        buy_button = (ImageButton)findViewById(R.id.buy_button);
	        buy_dialog = (ImageView)findViewById(R.id.BuyView1);
	        
	        CreateList();
	        
	        //Payment logic, show advert
	        if(sharing_class.isPaid()){
	        	buy_button.setVisibility(View.GONE);
	        	buy_dialog.setVisibility(View.GONE);
	        	start_map.setVisibility(View.VISIBLE);
	        }
	        else{
	        	buy_button.setVisibility(View.VISIBLE);
	        	buy_dialog.setVisibility(View.VISIBLE);
	        	start_map.setVisibility(View.GONE);
	        }
	        
	        //Set list view adapter
	        list=(ListView)findViewById(R.id.list);
	        adapter=new LazyAdapter(this,getApplicationContext());
	        list.setAdapter(adapter);
	        
					/*
			      	sharing_class.setPeriod(1);
		        	adapter.RefreshRow();
		        	clicks = sharing_class.GetDB(sharing_class.getPeriod());
		      		*/
			
			
	        list.setOnItemClickListener(new OnItemClickListener() {
	        	@Override
	        	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	        	 
	        	 try{
	        		 sharing_class.chicks_helper = sharing_class.chicks_history.get(position);
	        		/* 
	        		 Iterator<LinkedList<String>> it = sharing_class.chicks_history.iterator();
	        		 LinkedList<String> list_item;  
	        		   while (it.hasNext()) {  
	        			   
	        			   list_item  = it.next();
	        			   sharing_class.chicks_helper = list_item;
	        			   int siz = sharing_class.chicks_helper.size();
	        			  
	  	        		 String idf= sharing_class.chicks_helper.getFirst();
	        		   }
	        		  */
	        		 
		        	 Intent i = new Intent().setClass(history.this, notes.class);
		             startActivity(i);
		             
	        	 }catch(Exception e){
	        		 e.getLocalizedMessage();
	        	 }
	        }
	       });

	       buy_button.setOnClickListener(new View.OnClickListener() {  
	            public void onClick(View v) {   	 
	            	//Start BUY activity
	            	Intent i = new Intent().setClass(history.this, com.billing.BillingActivity.class);
	             	startActivity(i);
	            }  
	        });
	       
	       start_map.setOnClickListener(new View.OnClickListener() {  
	            public void onClick(View v) {  
	            	//Start map activity
	            	Intent i = new Intent().setClass(history.this, chicks_map_activity2.class);
	             	startActivity(i);
	            }  
	        });
	       
	       //Init clicks
	       
	       //Create list of lists
	       
	     }catch(Exception e){
	    	 e.getLocalizedMessage();
	     }
	 }
	 
	 @Override
	    public void onDestroy()
	    {
		 try{
	       adapter.imageLoader.stopThread();
	       list.setAdapter(null);
	        super.onDestroy();
		 }catch(Exception e){
	    	 e.getLocalizedMessage();
	     }
		 
	  }
	 
	 @Override
	    public void onResume()
	    {
	       try{
		    adapter.RefreshRow();
	        clicks = sharing_class.GetDB();
	        
	        if(sharing_class.isPaid()){
	        	buy_button.setVisibility(View.GONE);
	        	buy_dialog.setVisibility(View.GONE);
	        	start_map.setVisibility(View.VISIBLE);
	        }
	        else{
	        	buy_button.setVisibility(View.VISIBLE);
	        	buy_dialog.setVisibility(View.VISIBLE);
	        	start_map.setVisibility(View.GONE);
	        }
	        
	       }catch(Exception e){
		    	 e.getLocalizedMessage();
		     }
	        super.onResume();
	    }
	 
	 
	 
//History list aggregation function
	 
	void CreateList(){
	
		try{
		//Get all data from DB
			
	   LinkedList<String> IDs = new LinkedList<String>();
		//chicks_history	
	    Double store_lat = 0.0;
	    Double store_long = 0.0;
	    user_item this_chick = null; 
	    user_item last_chick = null;
	    boolean change = false;
	    IDs.clear();
	    sharing_class.chicks_list.clear();
	    sharing_class.chicks_history.clear();
	   
	   clicks = sharing_class.GetDB();
	   ListIterator<user_item> it = clicks.listIterator(); 
	   
	   while (it.hasNext()) {  
		   
          this_chick = it.next();
          
          //Init store location 
          if(store_lat == 0.0){
        	  store_lat = Double.valueOf(this_chick.getLat());
        	  store_long = Double.valueOf(this_chick.getLong());
        	  last_chick = this_chick;
          }
          
          change = !sharing_class.isSameLocation(Double.valueOf(this_chick.getLat()),Double.valueOf(this_chick.getLong()),store_lat,store_long);
          
          if(change){
        	  
        	  //Store current IDs list
        	  //sharing_class.chicks_history.add(IDs);

        	  sharing_class.chicks_history.addLast(IDs);
    		  sharing_class.chicks_list.add(new list_item(last_chick.getAddress(), last_chick.getDate(), String.valueOf(sharing_class.chicks_history.getLast().size()), null));
    		  
    		  last_chick = this_chick;
    		  //start new IDs list
    		  IDs = new LinkedList<String>();
    		  IDs.addFirst(this_chick.getId());
        	  
    		  store_lat = Double.valueOf(this_chick.getLat());
    	      store_long = Double.valueOf(this_chick.getLong());
          }
          
          else{
        	  
        	  store_lat = Double.valueOf(this_chick.getLat());
    	      store_long = Double.valueOf(this_chick.getLong());
    	     
    	      IDs.addFirst(this_chick.getId());
          }
          
          
       } //while 
		
	   //Ulozim posledniho
   	   sharing_class.chicks_history.addLast(IDs);
	   sharing_class.chicks_list.add(new list_item(last_chick.getAddress(), last_chick.getDate(), String.valueOf(IDs.size()), null));

		}catch(Exception e){
		}
		
	}
	 
}//End class
