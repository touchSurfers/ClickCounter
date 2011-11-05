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
    
	LinkedList<user_item> clicks;
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
        return clicks.size();
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
        public TextView note;
        public RatingBar rating;
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
            holder.note=(TextView)vi.findViewById(R.id.note_item);
            holder.image=(ImageView)vi.findViewById(R.id.image);
            holder.rating = (RatingBar)vi.findViewById(R.id.ratingBar_small);
            
            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();
        
       
        holder.date.setText(clicks.get(position).getDate());
        holder.address.setText(clicks.get(position).getAddress());
        holder.note.setText(clicks.get(position).getNotes());
        
        holder.image.setTag(clicks.get(position).getPhoto(1));
        imageLoader.DisplayImage(clicks.get(position).getPhoto(1), activity, holder.image);
        
        String rating_text = clicks.get(position).getRating();
        try{
        	 holder.rating.setRating(Float.valueOf(rating_text));
        }catch(Exception e){
        	holder.rating.setRating(0);
        	rating_text = "";
        	e.getLocalizedMessage();
        }
        rating_text = "";
        return vi;
    }
    
    public void addRow(String newRow) {
        
    	clicks = sharing_class.GetDB(sharing_class.getPeriod());
        notifyDataSetChanged();
    }
    
    public void DeleteRow() {
        
    	clicks = sharing_class.GetDB(sharing_class.getPeriod());
        notifyDataSetChanged();
    }
    
    public void RefreshRow() {
        
    	clicks = sharing_class.GetDB(sharing_class.getPeriod());
        notifyDataSetChanged();
    }
    
    
    public void fillRowsInit(){
       
    	
    	//get data from DB , depending on selecten radiobutton
    	clicks = sharing_class.GetDB(sharing_class.getPeriod());
        notifyDataSetChanged();
    	
    }
}