package com.helpers;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.chick.R;
import com.chick.share_class;

public class LazyAdapter extends BaseAdapter {
    
	
	share_class sharing_class;
    private Activity activity;
    //private String[] data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public LazyAdapter(Activity a, Context context) {
        activity = a;
       
        sharing_class = ((share_class)context);
        
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        fillRowsInit();
    }

    public int getCount() {
        return sharing_class.chicks_list.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public static class ViewHolder{
        public TextView date;
        public TextView address;
        public TextView num_of_chicks;
        public ImageView image;
       
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;
        if(convertView==null){
            vi = inflater.inflate(R.layout.item, null);
            holder=new ViewHolder();
            holder.date=(TextView)vi.findViewById(R.id.date_item);
            holder.address=(TextView)vi.findViewById(R.id.address_item);
            holder.num_of_chicks = (TextView)vi.findViewById(R.id.number_item);
            holder.image=(ImageView)vi.findViewById(R.id.image);
           
            
            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();
        
       
        holder.date.setText(sharing_class.chicks_list.get(position).getDate());
        holder.address.setText(sharing_class.chicks_list.get(position).getAddress());
        holder.num_of_chicks.setText(sharing_class.chicks_list.get(position).getNum());

    
        if(sharing_class.chicks_list.get(position).getPhoto().length()>0){
        	holder.image.setTag(sharing_class.chicks_list.get(position).getPhoto());
        	imageLoader.DisplayImage(sharing_class.chicks_list.get(position).getPhoto(), activity, holder.image);
        }
        else{
        	holder.image.setTag("");
        	imageLoader.DisplayImage("", activity, holder.image);
        }
    
    
        return vi;
    }
    
    public void addRow(String newRow) {
        
    	//clicks = sharing_class.GetDB();
        notifyDataSetChanged();
    }
    
    public void DeleteRow() {
        
    	//clicks = sharing_class.GetDB();
        notifyDataSetChanged();
    }
    
    public void RefreshRow() {
        
    	//clicks = sharing_class.GetDB();
        notifyDataSetChanged();
    }
    
    
    public void fillRowsInit(){
       
    	
    	//get data from DB , depending on selecten radiobutton
    	//clicks = sharing_class.GetDB();
        notifyDataSetChanged();
    	
    }
}