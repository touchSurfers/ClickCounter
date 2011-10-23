package com.UI;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;

public class SliderButton_button {

	 private Bitmap img; // the image of the button
	 private int coordX = 0; // the x coordinate at the canvas
	 private int coordY = 0; // the y coordinate at the canvas
	 private int id; // gives every ball his own id, for now not necessary

	 private boolean goRight = true;
	 private boolean goDown = true;
	 
		public SliderButton_button(Context context, int drawable) {

			BitmapFactory.Options opts = new BitmapFactory.Options();
	        opts.inJustDecodeBounds = true;
	        img = BitmapFactory.decodeResource(context.getResources(), drawable); 
	       

		}
		
		public SliderButton_button(Context context, int drawable, Point point) {

			BitmapFactory.Options opts = new BitmapFactory.Options();
	        opts.inJustDecodeBounds = true;
	        img = BitmapFactory.decodeResource(context.getResources(), drawable); 
	        
			coordX= point.x;
			coordY = point.y;

		}
		
		
		void setX(int newValue) {
	        coordX = newValue;
	    }
		
		public int getX() {
			return coordX;
		}

		void setY(int newValue) {
			if(newValue > 0){
	        coordY = newValue;
			}else{
				coordY = 0;	
			}
	   }
		
		public int getY() {
			return coordY;
		}
		
		public int getID() {
			return id;
		}
		
		public Bitmap getBitmap() {
			return img;
		}
		
		public void moveButton(int goX, int goY) {
			// check the borders, and set the direction if a border has reached
			
			if (coordX > 270){
				goRight = false;
			}
			if (coordX < 0){
				goRight = true;
			}
			
			
			if (coordY > 200){
				goDown = false;
			}
			if (coordY < 0){
				goDown = true;
			}
			// move the x and y 
			if (goRight){
				coordX += goX;
			}else
			{
				coordX -= goX;
			}
			if (goDown){
				coordY += goY;
			}else
			{
				coordY -= goY;
			}
			
		}
}
