package com.example.iparkdemo;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;


public class FunctionActivity extends Activity {
	Button user_center;
	Button near_bt;	
	Button get_car;
	Button to_park;
	Button pay_bt;
	TextView data_tv;
	
	ToggleButton data_tb;
	Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.function_activity);
		
		user_center=(Button)findViewById(R.id.user_center_bt);
	    near_bt = (Button)findViewById(R.id.nearpark_bt);
	    get_car=(Button)findViewById(R.id.getcar_bt);
	    to_park=(Button)findViewById(R.id.topark_bt);
	    pay_bt=(Button)findViewById(R.id.pay_bt);
	     
	    context=this;
	    
	    user_center.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(FunctionActivity.this,UserCenterActivity.class));
				
			}
		});
		near_bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(FunctionActivity.this,NearParkActivity.class));
				
			}
		});
		pay_bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(FunctionActivity.this,MyFeeActivity.class));				
			}
		});
		
		to_park.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(FunctionActivity.this,ToParkActivity.class));				
			}
		});
		get_car.setOnClickListener(new OnClickListener() {
		
			public void onClick(View v) {
				startActivity(new Intent(FunctionActivity.this,GetMyCarActivity.class));	
				
			}
		});
		
	}
	
	@Override
	public void finish() {
		super.finish();
			
	}
    

}

