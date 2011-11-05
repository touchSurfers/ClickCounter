package com.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import sharing_class.chicks_server;
import android.os.Handler;
import android.os.Message;

import com.chick.share_class;

public class json_class {
	 
	 
	 public String URL;
	 public List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2); 
	 share_class sharing_class;
	 
	 public void setUrl(String url){
		 URL = url;
	 }
	 
	    Handler handler_=new Handler() {
	    	@Override
	    	public void handleMessage(Message msg) {
	    		parse((String)msg.obj);
	    	}
	    	};
	
	    public void Init(){
	    	nameValuePairs.clear();
	    }
	 
	    Thread trd = new Thread(new Runnable(){
			  @Override
			  public void run(){
				 try{ 
				 String response = execHttpRequest(URL);
				 //parse(response);
				 //handler_.sendMessage(Message.obtain(handler_, UPDATE_UI, response));
				 }catch(Exception e){}
			  }
			});
	    
	    Thread trd2 = new Thread(new Runnable(){
			  @Override
			  public void run(){
				 try{ 
				 String response = execHttpRequest(URL);
				 parse(response);
				 
				 //TODO
				 //Vytvorit handler a poslat zpravu o dokonceni
				 //handler_.sendMessage(Message.obtain(handler_, UPDATE_UI, response));
				 }catch(Exception e){}
			  }
			});
	    
	public void GetData(){
		//If timestamp is OLD (10 minutes) else return with old
		
			trd.setPriority(Thread.MAX_PRIORITY-1);
			trd.run();
	}
	
	public void GetData(share_class share_class2){
		  
		sharing_class = share_class2;
		trd2.setPriority(Thread.MAX_PRIORITY-1);
		trd2.run();
    } 

	 private void parse(String serverResponse) {
		 
		 try{ 

			 JSONObject json = new JSONObject(serverResponse);
			 JSONArray menuitemArray = json.getJSONArray("chicks");
			 
			 
			 sharing_class.chicks_server_map.clear();
			 sharing_class.chicks_server_map_delete();
			 
			 for(int i=0;i< menuitemArray.length() ;i++){
				 
				 
				 chicks_server res = new chicks_server();
				 
				 res.setDeviceID(menuitemArray.getJSONObject(i).getString("device_id").toString()) ;
				 res.setChickID(menuitemArray.getJSONObject(i).getString("chick_id").toString()) ;
				 res.setDate(menuitemArray.getJSONObject(i).getString("date").toString()) ;
				 res.setLat(menuitemArray.getJSONObject(i).getString("lat").toString());
				 res.setLongi(menuitemArray.getJSONObject(i).getString("long").toString());
				 
				
				 sharing_class.chicks_server_map.add(res);
				 
			 }
			 
			 
			     sharing_class.mHandler.sendMessage(sharing_class.mHandler.obtainMessage());
			 
			 
		    } 
			 catch (JSONException e) {
				 
				
				 
				 sharing_class.mHandler.sendMessage(sharing_class.mHandler.obtainMessage());
				 
			 }
			 
		}
	 
	 private String execHttpRequest(String url) {
         try {		
        	 
             HttpClient httpclient = new DefaultHttpClient();
             HttpPost httppost = new HttpPost(url);
             //We want the JSON version of resource
             httppost.addHeader("accept", "application/json");
             //Add data to send 
             
             //ToDo prepsat UrlEncodedFormEntity
             //http://stackoverflow.com/questions/6797057/can-i-upload-images-and-text-using-urlencodedformentity-for-multi-part
             
             httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
             
             //Execute request
             HttpResponse response = httpclient.execute(httppost);
             
             HttpEntity entity = response.getEntity();
             return EntityUtils.toString(entity);
             
         } catch (Exception e) {
                 e.printStackTrace();
                 return e.getMessage();                
         }
 }
	
	
	 
	 
	
}//class
