package com.chick;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.helpers.LazyAdapter;
import com.helpers.user_item;

public class history extends Activity {
	
	 ListView list;
	 LazyAdapter adapter;
	 share_class sharing_class;
	 LinkedList<user_item> clicks;
	
	    
	 private TabHost mTabHost;

		private void setupTabHost() {
			mTabHost = (TabHost) findViewById(android.R.id.tabhost);
			mTabHost.setup();
		}
		
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.hisotry);
	        
	        try{
	        sharing_class = ((share_class)getApplicationContext());
	        
	        list=(ListView)findViewById(R.id.list);
	        adapter=new LazyAdapter(this,getApplicationContext());
	        list.setAdapter(adapter);
	        

	        setupTabHost();
			mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

			setupTab(new TextView(this), "last 24h");
			setupTab(new TextView(this), "last week");
			setupTab(new TextView(this), "anytime");
			setupTab(new TextView(this), "top chicks");
			
			mTabHost.getTabWidget().getChildAt(0).setOnClickListener(new OnClickListener() { 
		        @Override 
		        public void onClick(View v) { 
		        	
		        	mTabHost.setCurrentTab(0);   
		        	Toast.makeText(history.this, mTabHost.getCurrentTabTag(), Toast.LENGTH_SHORT).show();
		        	sharing_class.setPeriod(1);
		        	adapter.RefreshRow();
		        } 
		    });
			
			mTabHost.getTabWidget().getChildAt(2).setOnClickListener(new OnClickListener() { 
		        @Override 
		        public void onClick(View v) { 
		        	
		        	mTabHost.setCurrentTab(1);   
		        	Toast.makeText(history.this, mTabHost.getCurrentTabTag(), Toast.LENGTH_SHORT).show();
		        	sharing_class.setPeriod(2);
		        	adapter.RefreshRow();
		        } 
		    });
			
			
			mTabHost.getTabWidget().getChildAt(4).setOnClickListener(new OnClickListener() { 
		        @Override 
		        public void onClick(View v) { 
		        	
		        	mTabHost.setCurrentTab(2);   
		        	Toast.makeText(history.this, mTabHost.getCurrentTabTag(), Toast.LENGTH_SHORT).show();
		        	sharing_class.setPeriod(3);
		        	adapter.RefreshRow();
		        } 
		    });
			
			mTabHost.getTabWidget().getChildAt(6).setOnClickListener(new OnClickListener() { 
		        @Override 
		        public void onClick(View v) { 
		        	
		        	mTabHost.setCurrentTab(3);   
		        	Toast.makeText(history.this, mTabHost.getCurrentTabTag(), Toast.LENGTH_SHORT).show();

		        } 
		    });
	        
	        list.setOnItemClickListener(new OnItemClickListener() {
	        	@Override
	        	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	        	
	        	 
	        	 try{
	        		 sharing_class.setChickSelected(clicks.get(position).getId());
		        	 sharing_class.setChickPhoto("");
		        	 sharing_class.clearPhotoCache();
		        	 Intent i = new Intent().setClass(history.this, notes.class);
		             startActivity(i);
		             
	        	 }catch(Exception e){
	        		 e.getLocalizedMessage();
	        	 }
	        }
	       });
	       
	     
	       list.setOnItemLongClickListener(new OnItemLongClickListener(){
	    	     @Override
	    	     public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
	        	
	        	 try{
	        		 
		             sharing_class.DeleteItem(clicks.get(position).getId());
		             clicks = sharing_class.GetDB(sharing_class.getPeriod());
		            
		             adapter.DeleteRow();
		             return true;
	        	 }catch(Exception e){
	        		 e.getLocalizedMessage();
	        		 return false;
	        	 }
	        }
	       });
	       
	       clicks = sharing_class.GetDB(sharing_class.getPeriod());
	       
	     }catch(Exception e){
	    	 e.getLocalizedMessage();
	     }
	 }
	 
	 @Override
	    public void onDestroy()
	    {
	        adapter.imageLoader.stopThread();
	        list.setAdapter(null);
	        super.onDestroy();
	    }
	 
	 @Override
	    public void onResume()
	    {
	        adapter.RefreshRow();
	        super.onResume();
	    }
	 
	 
	 private void setupTab(final View view, final String tag) {
			View tabview = createTabView(mTabHost.getContext(), tag);

			
			TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview).setContent(new TabContentFactory() {
				public View createTabContent(String tag) {return view;}
			});
			
			
			mTabHost.addTab(setContent);

		}

		private static View createTabView(final Context context, final String text) {
			View view = LayoutInflater.from(context).inflate(R.layout.tabs_bg, null);
			TextView tv = (TextView) view.findViewById(R.id.tabsText);
			tv.setText(text);
			return view;
		}
	 
	
	
}
