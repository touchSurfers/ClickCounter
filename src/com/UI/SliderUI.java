/**
 * 
 */
package com.UI;

import com.chick.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author Ondra
 *
 */
public class SliderUI extends View {

	/**
	 * @param context
	 */
	private SliderButton_button slider_button ; 
	// Listeners
	protected OnColorChangedListener mOnColorChangedListener;

	
	int BordersWidth = 0;
	int BordersHeigth = 0;
	
	boolean movecon = false;
	
	public SliderUI(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	

	public interface OnColorChangedListener {
	 void onResetHit(View v);
	}
	
	public void setOnColorChangedListener(OnColorChangedListener l) {
		
		 this.mOnColorChangedListener = l;
		
	}



	/**
	 * @param context
	 * @param attrs
	 */
	public SliderUI(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		setFocusable(true);
		
		Point point1 = new Point();
        point1.x = 0;
        point1.y = 0;
        
		slider_button = new SliderButton_button(context,R.drawable.front_reset_button, point1);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SliderUI(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	protected void onDraw(Canvas canvas)
	{
	    super.onDraw(canvas);
	    
	    

	    if(movecon == false){
	    	/*
	    		int forY = slider_button.getY();
	    			for(int i = forY;i>0;i--){
	    				
	    				slider_button.setY(i);
	    				canvas.drawBitmap(slider_button.getBitmap(), 0, slider_button .getY(), null);
	    				
	    				invalidate();
	    			}
	    		slider_button.setY(0);
	    		*/
	    	if(slider_button.getY()>0){
	    		slider_button.setY(slider_button.getY()-5);
	    		canvas.drawBitmap(slider_button.getBitmap(), 0, slider_button .getY(), null);
	    		invalidate();
	    	}
	    	
	    }
	    
	    canvas.drawBitmap(slider_button.getBitmap(), 0, slider_button.getY(), null);
    	
	    // TODO draw stuff
	}
	 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
	    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	    setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
	    
	    BordersWidth = widthMeasureSpec;
		BordersHeigth = heightMeasureSpec;
		
		int _height = View.MeasureSpec.getSize(heightMeasureSpec);
	    int _width = View.MeasureSpec.getSize(widthMeasureSpec);
	 
	    setMeasuredDimension(_width, _height);
	    BordersHeigth = _height;
	    // MAKE SURE you call super.onMeasure(int, int) here!!!
	    // At least until we implement our own measuring logic
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
    			//povolit move
    			 movecon = true;
                break;
    		}
    		
             break; 


        case MotionEvent.ACTION_MOVE:   // touch drag with the ball

        	if(movecon){
        		
        		if(slider_button.getY()> BordersHeigth-70){
        			//reset counter
        			 BordersHeigth =  BordersHeigth;
        			int Y2 = slider_button.getY();
        			movecon = false;
        			resetCounter();
        			break;
        		}
        		slider_button.setY(Y-20);
        	}
        	
            break; 
            
         

        case MotionEvent.ACTION_UP: 
       		// touch drop - just do things here after dropping
        	movecon = false;
        	//vratit ho zpet na 0,0
        	
             break; 
        } 
        // redraw the canvas
        invalidate(); 
        return true; 
	
    }
    
void resetCounter(){
	if (this.mOnColorChangedListener != null) {
		
		  this.mOnColorChangedListener.onResetHit(this);
		
	}

}
	
}
