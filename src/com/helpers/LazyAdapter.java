package com.helpers;

import java.util.LinkedList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
        public TextView text;
        public ImageView image;
       
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        ViewHolder holder;
        if(convertView==null){
            vi = inflater.inflate(R.layout.item, null);
            holder=new ViewHolder();
            holder.text=(TextView)vi.findViewById(R.id.text);
            holder.image=(ImageView)vi.findViewById(R.id.image);
            
            vi.setTag(holder);
        }
        else
            holder=(ViewHolder)vi.getTag();
        
       
        holder.text.setText(clicks.get(position).getDate());
        
        holder.image.setTag(clicks.get(position).getPhoto(1));
        imageLoader.DisplayImage(clicks.get(position).getPhoto(1), activity, holder.image);
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