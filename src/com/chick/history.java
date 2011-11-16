package com.chick;

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
	        		 
	        		 //TODO Create list of selected Chicks (IDs)
	        		 sharing_class.setChickSelected(clicks.get(position).getId());

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
	       clicks = sharing_class.GetDB();
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
	    clicks = sharing_class.GetDB();
	    LinkedList<String> IDs = new LinkedList<String>();
	
		//chicks_history	
	    Double store_lat = 0.0;
	    Double store_long = 0.0;
	    user_item this_chick = null; 
	    boolean change = false;
	   IDs.clear();
	   sharing_class.chicks_list.clear();
	   sharing_class.chicks_history.clear();
	   
	   ListIterator<user_item> it = clicks.listIterator(); 
	   
	   
	   while (it.hasNext()) {  
		   
          this_chick = it.next(); 
          change = !sharing_class.isSameLocation(Double.valueOf(this_chick.getLat()),Double.valueOf(this_chick.getLong()),store_lat,store_long);
          
          if(change){
        	  
        	  if(store_lat!= 0.0){
        		  sharing_class.chicks_history.add(IDs);
        		  sharing_class.chicks_list.add(new list_item(this_chick.getAddress(), this_chick.getDate(), String.valueOf(IDs.size()), null));
        		  IDs.clear();
        	  }
        	  else{
        		  store_lat = Double.valueOf(this_chick.getLat());
            	  store_long = Double.valueOf(this_chick.getLong());
            	  IDs.add(this_chick.getId()); 
        	  }
          }
          else{
        	  store_lat = Double.valueOf(this_chick.getLat());
        	  store_long = Double.valueOf(this_chick.getLong());
        	  IDs.add(this_chick.getId());
          }
          
          
       }  
		
		
		if(sharing_class.chicks_history.size()==0  && this_chick!=null && IDs.size()!=0){
		  sharing_class.chicks_history.add(IDs);
  		  sharing_class.chicks_list.add(new list_item(this_chick.getAddress(), this_chick.getDate(), String.valueOf(IDs.size()), null));
  		  IDs.clear();
		}
	   
		}catch(Exception e){
		}
		
	}
	 
}//End class
