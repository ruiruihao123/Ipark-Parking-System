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

	public static final int show_response = 0;//��Ϣ�ı�־
	private Button sendRequest;
	private TextView responseText;
	private ProgressBar progressBar;
	private int index=0;
	private String response = null;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case show_response: {
				response = (String) msg.obj; //�ֻ��˽��յ��������������ַ���
		
				if (response != null) {
					responseText.setTextColor(Color.GREEN);
					responseText.setText("����ͣ��·��Ϊ"+"\n"+response);
					String[] strings = response.split(" ");//���ܵ����ַ����Կո���ʽ�ֿ��ŵ�������
					Receive.parkNumber = strings[0];
					Receive.nodeLenth = strings.length; //���ܵ����ַ�������ĳ���
					for (index = 1; index < Receive.nodeLenth; index++) {
						Receive.nodeName[index] = strings[index];
					}
					Toast.makeText(getApplicationContext(), "����Ϊ������·��ͼ...",
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
				Log.e("this", "���е�onclick����");
				if (v.getId() == R.id.send_request) {
					
					responseText.setText(""); //��ԭ���ֻ���Ļ�ϵ��������
					sendRequestWithHttpClient(); //���������������
					progressBar.setVisibility(View.VISIBLE);//�������ɼ�
					new Handler().postDelayed(new Runnable() { //��ʱ�����������·��ҳ��
								public void run() {
									progressBar.setVisibility(View.INVISIBLE);//���������ɼ�
									progressBar.setVisibility(View.GONE);//���������ɼ�
									if (response != null) {
										startActivity(new Intent(
												ToParkActivity.this,
												ResponseImage.class));
									} else {
										responseText.setTextColor(Color.RED);
										responseText.setText("\n"
												+ "������δ��Ӧ���������磡");
										Toast.makeText(getApplicationContext(),
												"������δ��Ӧ����������",
												Toast.LENGTH_LONG).show();
									}
								}
							}, 3000);
				}
			}
		});
	}

	private void sendRequestWithHttpClient() {
		//�����߳���������
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

						//����ɹ�
						HttpEntity entity = httpResponse.getEntity();
						String response = EntityUtils.toString(entity, "utf-8");
						Message message = new Message();
						message.what = show_response;
						//���������صĽ����ŵ�Message��
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
