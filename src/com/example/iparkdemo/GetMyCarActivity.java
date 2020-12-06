package com.example.iparkdemo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class GetMyCarActivity extends Activity{

	protected void onCreate(Bundle savedInstanceStates){
		super.onCreate(savedInstanceStates);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.get_my_car);
	}
}
