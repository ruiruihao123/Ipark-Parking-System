package com.example.iparkdemo;

import com.example.iparkdemo.R;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

public class UserCenterActivity extends Activity{
	private	TextView user;
	private	Button back_bt;
	protected void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_center);
		
		user=(TextView)findViewById(R.id.phone_number);
		back_bt=(Button)findViewById(R.id.back_bt);
		user.setText(Constant.phoneNumber);
		

		initEvent();
	}
	
	protected void initEvent(){
		back_bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
	}
	
	
	
}
