package com.example.iparkdemo;

import com.example.iparkdemo.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ZhuCeActivity extends Activity {
 private Button zhuce_bt;
 private EditText zhucezhanghao;
 private EditText zhucemima;
 private EditText queren;
 private Button fanhui_bt;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.zhece_activity);
		zhuce_bt =(Button)findViewById(R.id.zhucestart_bt);
		zhucezhanghao=(EditText)findViewById(R.id.zhuceing_et);
		zhucemima=(EditText)findViewById(R.id.mimaing_et);
		queren=(EditText)findViewById(R.id.mima_queren_et);
		
		/*
		fanhui_bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {			
				finish();
			}
		});
		*/
		zhuce_bt.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {	
				if(zhucezhanghao.getText().toString().equals("") || zhucemima.getText().toString().equals("")
						|| !(queren.getText().toString().equals(zhucemima.getText().toString())))
				{
					Toast.makeText(ZhuCeActivity.this, "注册失败，请重新输入！", Toast.LENGTH_SHORT).show();
					zhucemima.setText("");
					queren.setText("");
				}
				else{
			    Toast.makeText(ZhuCeActivity.this, "您已成功注册", Toast.LENGTH_SHORT).show();
				finish();
				}
			}
		});
	}
}