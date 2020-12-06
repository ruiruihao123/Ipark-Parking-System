package com.example.iparkdemo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ToParkActivity extends Activity {

	public static final int show_response = 0;//消息的标志
	private Button sendRequest;
	private TextView responseText;
	private ProgressBar progressBar;
	private int index=0;
	private String response = null;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case show_response: {
				response = (String) msg.obj; //手机端接收到服务器发来的字符串
		
				if (response != null) {
					responseText.setTextColor(Color.GREEN);
					responseText.setText("您的停车路径为"+"\n"+response);
					String[] strings = response.split(" ");//接受到的字符串以空格形式分开放到数组里
					Receive.parkNumber = strings[0];
					Receive.nodeLenth = strings.length; //接受到的字符串数组的长度
					for (index = 1; index < Receive.nodeLenth; index++) {
						Receive.nodeName[index] = strings[index];
					}
					Toast.makeText(getApplicationContext(), "正在为您加载路线图...",
							Toast.LENGTH_LONG).show();
				}
			}
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.to_park);
		responseText = (TextView) findViewById(R.id.response);
		sendRequest = (Button) findViewById(R.id.send_request);
		
		progressBar = (ProgressBar) findViewById(R.id.progressBar1);

		sendRequest.setOnClickListener(new OnClickListener() {
			@Override
			
			public void onClick(View v) {
				Log.e("this", "运行到onclick后了");
				if (v.getId() == R.id.send_request) {
					
					responseText.setText(""); //把原来手机屏幕上的内容清空
					sendRequestWithHttpClient(); //向服务器发送请求
					progressBar.setVisibility(View.VISIBLE);//进度条可见
					new Handler().postDelayed(new Runnable() { //延时三秒后进入绘制路径页面
								public void run() {
									progressBar.setVisibility(View.INVISIBLE);//进度条不可见
									progressBar.setVisibility(View.GONE);//进度条不可见
									if (response != null) {
										startActivity(new Intent(
												ToParkActivity.this,
												ResponseImage.class));
									} else {
										responseText.setTextColor(Color.RED);
										responseText.setText("\n"
												+ "服务器未响应，请检查网络！");
										Toast.makeText(getApplicationContext(),
												"服务器未响应！请检查网络",
												Toast.LENGTH_LONG).show();
									}
								}
							}, 3000);
				}
			}
		});
	}

	private void sendRequestWithHttpClient() {
		//开启线程网络请求
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					//http://192.168.1.100:8080/IparkJSP/servlet/LisaiServlet
					HttpClient httpClient = new DefaultHttpClient();
					HttpGet httpGet = new HttpGet(
							"http://192.168.1.100:8080/webServlet/MyServlet");
					HttpResponse httpResponse = httpClient.execute(httpGet);
					int code = httpResponse.getStatusLine().getStatusCode();
					if (code == 200) {

						//请求成功
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity, "utf-8");
						Message message = new Message();
						message.what = show_response;
						//服务器返回的结果存放到Message中
						message.obj = response.toString();
						handler.sendMessage(message);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}).start();

	}

}
