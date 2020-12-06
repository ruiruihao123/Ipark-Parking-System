package com.example.iparkdemo;

import com.example.iparkdemo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class OverFlowActivity extends Activity  {
	 private final int SPLASH_DISPLAY_LENGHT = 3000; // —”≥Ÿ3√Î
	  @Override
	  protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    setContentView(R.layout.activity_main);
	    
	    new Handler().postDelayed(new Runnable() {
		      public void run() {
		        Intent mainIntent = new Intent(OverFlowActivity.this, DengluActivity.class);
		        startActivity(mainIntent);
		        finish();
		      }
		    }, SPLASH_DISPLAY_LENGHT);
	  }
}
