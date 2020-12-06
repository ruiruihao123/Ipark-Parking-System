package com.example.iparkdemo;

import com.example.iparkdemo.R;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DengluActivity extends Activity{
	private Button denglu_bt;
	private EditText mima;
	private Button zhuce_bt;
	private EditText zhanghao;
	protected void onCreate(Bundle savedInstanceStates){
		super.onCreate(savedInstanceStates);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.denglu_activity);
		
		denglu_bt=(Button)findViewById(R.id.denglu_bt);
		zhuce_bt=(Button)findViewById(R.id.zhuce_bt);
		zhanghao=(EditText)findViewById(R.id.zhanghao_et);
		mima=(EditText)findViewById(R.id.mima_et);
		denglu_bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if((zhanghao.getText().toString()).equals("") || (mima.getText().toString()).equals("")){
					Toast.makeText(DengluActivity.this, "请输入正确的账号和密码！", Toast.LENGTH_SHORT).show();
				
					mima.setText("");
					
				}else if(!Constant.mima.equals(mima.getText().toString())){
					Toast.makeText(DengluActivity.this, "密码错误,请重新输入！", Toast.LENGTH_LONG).show();
				}else{				
				     startActivity(new Intent(DengluActivity.this , FunctionActivity.class));
				     Constant.phoneNumber=zhanghao.getText().toString();
				}
				
			}
		});
		
		zhuce_bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(new Intent(DengluActivity.this , ZhuCeActivity.class));
			}
		});
	}
}