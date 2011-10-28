package com.chick;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Editbox_activity extends Activity {

	share_class sharing_class;
	EditText notes;
	Button saveB;
	Button cancelB;
	
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.editbox);
	        
	        sharing_class = ((share_class)getApplicationContext());
	        notes = (EditText) findViewById(R.id.notes_added);
	        saveB = (Button) findViewById(R.id.button1);
	        cancelB = (Button) findViewById(R.id.button2);
	        
	        notes.setText(sharing_class.GetNote());
	        
	        saveB.setOnClickListener(new View.OnClickListener() {  
	            public void onClick(View v) {  
	           
	            	sharing_class.SetNote(notes.getText().toString());
	            	finish();
	            	
	            }  
	        });
	        
	        cancelB.setOnClickListener(new View.OnClickListener() {  
	            public void onClick(View v) {  
	            	//sharing_class.SetNote("");
	            	finish();
	            }  
	        });
	        
	  }
	  
	  @Override
	    public void onDestroy(){
	    	
	    	super.onDestroy();
	    	//uloz notes
	    	//sharing_class.SetNote(notes.getText().toString());
	    }
	  
	  @Override
	    public void onStop(){
	    	
		  //sharing_class.SetNote(notes.getText().toString());
	    	super.onStop();
	    	//uloz notes
	    	
	    }
	  
	    
	    
	    @Override
	    public void onResume(){
	    	
	    	super.onResume();
	    	//nacti notes
	    	notes.setText(sharing_class.GetNote());
	    }
	    
}
