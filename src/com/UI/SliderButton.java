package com.UI;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;

import com.chick.R;

public class SliderButton extends View{

	private SliderButton_button slider_button ; // array that holds the balls
	private int balID = 0; // variable to know what ball is being dragged
	   
	public SliderButton(Context context) {
		super(context);
		setFocusable(true);
		
		Point point1 = new Point();
        point1.x = 50;
        point1.y = 20;
        
        slider_button = new SliderButton_button(context,R.drawable.bol_groen, point1);
	}
	
	//Method for drawing the button
	@Override protected void onDraw(Canvas canvas) {
        //canvas.drawColor(0xFFCCCCCC);     //if you want another background color       
        //Set to alpha
		canvas.drawBitmap(slider_button.getBitmap(), slider_button .getX(), slider_button .getY(), null);
    }

	// events when touching the screen
    public boolean onTouchEvent(MotionEvent event) {
        int eventaction = event.getAction(); 
        
        int X = (int)event.getX(); 
        int Y = (int)event.getY(); 

        switch (eventaction ) { 

        case MotionEvent.ACTION_DOWN: // touch down so check if the finger is on a ball
        	int centerX = slider_button.getX() + 25;
    		int centerY = slider_button.getY() + 25;
    		
    		// calculate the radius from the touch to the center of the ball
    		double radCircle  = Math.sqrt( (double) (((centerX-X)*(centerX-X)) + (centerY-Y)*(centerY-Y)));
    		
    		// if the radius is smaller then 23 (radius of a ball is 22), then it must be on the ball
    		if (radCircle < 23){
    			balID = slider_button.getID();
                break;
    		}
    		
             break; 


        case MotionEvent.ACTION_MOVE:   // touch drag with the ball

        	slider_button.setX(X-25);
        	slider_button.setY(Y-25);
        	
            break; 

        case MotionEvent.ACTION_UP: 
       		// touch drop - just do things here after dropping

             break; 
        } 
        // redraw the canvas
        invalidate(); 
        return true; 
	
    }
	
}