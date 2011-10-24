package com.chick;

import java.math.BigDecimal;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.paypal.android.MEP.CheckoutButton;
import com.paypal.android.MEP.PayPal;
import com.paypal.android.MEP.PayPalActivity;
import com.paypal.android.MEP.PayPalInvoiceData;
import com.paypal.android.MEP.PayPalInvoiceItem;
import com.paypal.android.MEP.PayPalPayment;

public class SimpleDemo extends Activity implements OnClickListener {
	
	// The PayPal server to be used - can also be ENV_NONE and ENV_LIVE
	private static final int server = PayPal.ENV_SANDBOX;
	// The ID of your application that you received from PayPal
	private static final String appID = "APP-80W284485P519543T";
	// This is passed in for the startActivityForResult() android function, the value used is up to you
	private static final int request = 1;
	
	public static final String build = "10.12.09.8053";
	
	protected static final int INITIALIZE_SUCCESS = 0;
	protected static final int INITIALIZE_FAILURE = 1;

	// Native android items for the application
	ScrollView scroller;
	TextView labelSimplePayment;
	
	TextView labelKey;
	TextView appVersion;
	
	Button exitApp;
	TextView title;
	TextView info;
	TextView extra;
	LinearLayout layoutSimplePayment;

	// You will need at least one CheckoutButton, this application has four for examples
	CheckoutButton launchSimplePayment;
	
	// These are used to display the results of the transaction
	public static String resultTitle;
	public static String resultInfo;
	public static String resultExtra;
	
	// This handler will allow us to properly update the UI. You cannot touch Views from a non-UI thread.
	Handler hRefresh = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch(msg.what){
		    	case INITIALIZE_SUCCESS:
		    		setupButton();
		            break;
		    	case INITIALIZE_FAILURE:
		    		showFailure();
		    		break;
			}
		}
	};

	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Initialize the library. We'll do it in a separate thread because it requires communication with the server
		// which may take some time depending on the connection strength/speed.
		Thread libraryInitializationThread = new Thread() {
			public void run() {
				initLibrary();
				
				// The library is initialized so let's create our CheckoutButton and update the UI.
				if (PayPal.getInstance().isLibraryInitialized()) {
					hRefresh.sendEmptyMessage(INITIALIZE_SUCCESS);
				}
				else {
					hRefresh.sendEmptyMessage(INITIALIZE_FAILURE);
				}
			}
		};
		libraryInitializationThread.start();
		
		// Setup our UI.
		scroller = new ScrollView(this);
		scroller.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		scroller.setBackgroundColor(Color.BLACK);
		
		LinearLayout content = new LinearLayout(this);
		content.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		content.setGravity(Gravity.CENTER_HORIZONTAL);
		content.setOrientation(LinearLayout.VERTICAL);
		content.setPadding(10, 10, 10, 10);
		content.setBackgroundColor(Color.TRANSPARENT);
		
		layoutSimplePayment = new LinearLayout(this);
		layoutSimplePayment.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutSimplePayment.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutSimplePayment.setOrientation(LinearLayout.VERTICAL);
		layoutSimplePayment.setPadding(0, 5, 0, 5);
			
		labelSimplePayment = new TextView(this);
		labelSimplePayment.setGravity(Gravity.CENTER_HORIZONTAL);
		labelSimplePayment.setText("Simple Payment:");
		layoutSimplePayment.addView(labelSimplePayment);
		labelSimplePayment.setVisibility(View.GONE);
			
		content.addView(layoutSimplePayment);
			
		
		LinearLayout layoutKey = new LinearLayout(this);
		layoutKey.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutKey.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutKey.setOrientation(LinearLayout.VERTICAL);
		layoutKey.setPadding(0, 1, 0, 5);
			
		
		labelKey = new TextView(this);
		labelKey.setGravity(Gravity.CENTER_HORIZONTAL);
		labelKey.setPadding(0, -5, 0, 0);
		labelKey.setText("(Required for Preapproval)");
		layoutKey.addView(labelKey);
		labelKey.setVisibility(View.GONE);
		content.addView(layoutKey);
			
		title = new TextView(this);
		title.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		title.setPadding(0, 5, 0, 5);
		title.setGravity(Gravity.CENTER_HORIZONTAL);
		title.setTextSize(30.0f);
		title.setVisibility(TextView.GONE);
		content.addView(title);
			
		info = new TextView(this);
		info.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		info.setPadding(0, 5, 0, 5);
		info.setGravity(Gravity.CENTER_HORIZONTAL);
		info.setTextSize(20.0f);
		info.setVisibility(TextView.VISIBLE);
		info.setText("Initializing Library...");
		content.addView(info);
			
		extra = new TextView(this);
		extra.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		extra.setPadding(0, 5, 0, 5);
		extra.setGravity(Gravity.CENTER_HORIZONTAL);
		extra.setTextSize(12.0f);
		extra.setVisibility(TextView.GONE);
		content.addView(extra);
		
		LinearLayout layoutExit = new LinearLayout(this);
		layoutExit.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		layoutExit.setGravity(Gravity.CENTER_HORIZONTAL);
		layoutExit.setOrientation(LinearLayout.VERTICAL);
		layoutExit.setPadding(0, 15, 0, 5);
		
		exitApp = new Button(this);
		exitApp.setLayoutParams(new LayoutParams(200, LayoutParams.WRAP_CONTENT)); //Semi mimic PP button sizes
		exitApp.setOnClickListener(this);
		exitApp.setText("Cancel");
		layoutExit.addView(exitApp);
		content.addView(layoutExit);
		
		appVersion = new TextView(this);
		appVersion.setGravity(Gravity.CENTER_HORIZONTAL);
		appVersion.setPadding(0, -5, 0, 0);
		appVersion.setText("\n\nSimple Demo Build " + build + "\nMPL Library Build " + PayPal.getBuild());
		content.addView(appVersion);
		appVersion.setVisibility(View.GONE);
		
		scroller.addView(content);
		setContentView(scroller);
	}
	
	/**
	 * Create our CheckoutButton and update the UI.
	 */
	public void setupButton() {
		PayPal pp = PayPal.getInstance();
		// Get the CheckoutButton. There are five different sizes. The text on the button can either be of type TEXT_PAY or TEXT_DONATE.
		launchSimplePayment = pp.getCheckoutButton(this, PayPal.BUTTON_194x37, CheckoutButton.TEXT_PAY);
		// You'll need to have an OnClickListener for the CheckoutButton. For this application, MPL_Example implements OnClickListener and we
		// have the onClick() method below.
		launchSimplePayment.setOnClickListener(this);
		// The CheckoutButton is an android LinearLayout so we can add it to our display like any other View.
		layoutSimplePayment.addView(launchSimplePayment);
	
		
		// Show our labels and the preapproval EditText.
		
		labelKey.setVisibility(View.VISIBLE);
		appVersion.setVisibility(View.VISIBLE);
		
		info.setText("");
		info.setVisibility(View.GONE);
	}
	
	/**
	 * Show a failure message because initialization failed.
	 */
	public void showFailure() {
		title.setText("FAILURE");
		info.setText("Could not initialize the PayPal library.");
		title.setVisibility(View.VISIBLE);
		info.setVisibility(View.VISIBLE);
	}
	
	/**
	 * The initLibrary function takes care of all the basic Library initialization.
	 * 
	 * @return The return will be true if the initialization was successful and false if 
	 */
	private void initLibrary() {
		PayPal pp = PayPal.getInstance();
		// If the library is already initialized, then we don't need to initialize it again.
		if(pp == null) {
			// This is the main initialization call that takes in your Context, the Application ID, and the server you would like to connect to.
			pp = PayPal.initWithAppID(this, appID, server);
   			
			// -- These are required settings.
        	pp.setLanguage("en_US"); // Sets the language for the library.
        	// --
        	
        	// -- These are a few of the optional settings.
        	// Sets the fees payer. If there are fees for the transaction, this person will pay for them. Possible values are FEEPAYER_SENDER,
        	// FEEPAYER_PRIMARYRECEIVER, FEEPAYER_EACHRECEIVER, and FEEPAYER_SECONDARYONLY.
        	pp.setFeesPayer(PayPal.FEEPAYER_EACHRECEIVER); 
        	// Set to true if the transaction will require shipping.
        	pp.setShippingEnabled(false);
        	// Dynamic Amount Calculation allows you to set tax and shipping amounts based on the user's shipping address. Shipping must be
        	// enabled for Dynamic Amount Calculation. This also requires you to create a class that implements PaymentAdjuster and Serializable.
        	pp.setDynamicAmountCalculationEnabled(false);
        	// --
		}
	}
	


	public void onClick(View v) {
		
		/**
		 * For each call to checkout() and preapprove(), we pass in a ResultDelegate. If you want your application
		 * to be notified as soon as a payment is completed, then you need to create a delegate for your application.
		 * The delegate will need to implement PayPalResultDelegate and Serializable. See our ResultDelegate for
		 * more details.
		 */		
		
		if(v == launchSimplePayment) {
			// Use our helper function to create the simple payment.
			     //PayPalPayment payment = exampleSimplePayment();
			PayPalPayment newPayment = new PayPalPayment();
			newPayment.setSubtotal(new BigDecimal("1"));
			newPayment.setCurrencyType("USD");
			newPayment.setRecipient("my@email.com");
			newPayment.setMerchantName("My Company");
			newPayment.setPaymentType(PayPal.PAYMENT_TYPE_SERVICE);
			newPayment.setIpnUrl("http://www.exampleapp.com/ipn");
			newPayment.setCustomID("8873482296");
			
			// Use checkout to create our Intent.
			Intent checkoutIntent = PayPal.getInstance().checkout(newPayment, this);
			// Use the android's startActivityForResult() and pass in our Intent. This will start the library.
	    	
			try{
			startActivityForResult(checkoutIntent, request);
			}catch(Exception e){
				e.getLocalizedMessage();
			}
		}else if(v == exitApp) {
			// The exit button was pressed, so close the application.
			finish();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
    	if(requestCode != request)
    		return;
    	
    	/**
    	 * If you choose not to implement the PayPalResultDelegate, then you will receive the transaction results here.
    	 * Below is a section of code that is commented out. This is an example of how to get result information for
    	 * the transaction. The resultCode will tell you how the transaction ended and other information can be pulled
    	 * from the Intent using getStringExtra.
    	 */
    	switch(resultCode) {
		case Activity.RESULT_OK:
			resultTitle = "SUCCESS";
			resultInfo = "You have successfully completed this payment.";
			//resultExtra = "Transaction ID: " + data.getStringExtra(PayPalActivity.EXTRA_PAY_KEY);
			break;
		case Activity.RESULT_CANCELED:
			resultTitle = "CANCELED";
			resultInfo = "The transaction has been cancelled.";
			resultExtra = "";
			break;
		case PayPalActivity.RESULT_FAILURE:
			resultTitle = "FAILURE";
			resultInfo = data.getStringExtra(PayPalActivity.EXTRA_ERROR_MESSAGE);
			resultExtra = "Error ID: " + data.getStringExtra(PayPalActivity.EXTRA_ERROR_ID);
		}
    	 
    	
    	launchSimplePayment.updateButton();
    	
    	
    	title.setText(resultTitle);
    	title.setVisibility(View.VISIBLE);
    	info.setText(resultInfo);
    	info.setVisibility(View.VISIBLE);
    	extra.setText(resultExtra);
    	extra.setVisibility(View.VISIBLE);
    }
}