package com.chick;

import com.chick.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;

public class Editbox_activity extends Activity {

	share_class sharing_class;
	EditText notes;
	
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.editbox);
	        
	        sharing_class = ((share_class)getApplicationContext());
	        notes = (EditText) findViewById(R.id.notes_added);
	        sharing_class.SetNote("AHOOOJ");
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
