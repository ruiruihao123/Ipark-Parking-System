package com.example.iparkdemo;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

public class ResponseImage extends Activity{
	private ImageView img;
	private Node[] nodes = new Node[10]; //�����洢�Ÿ��ڵ�Ķ���1~10
	private float[] nodesXY = new float[50];//���δ洢Ҫ���Ľڵ��X��Y����
	Node startNode;//��ڽڵ�
	Node endNode;//���ڽڵ�
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.image);
	    img = (ImageView)findViewById(R.id.response_image); //��img�ؼ�
		
		
		/*//��ȡ�ֻ��ķֱ���
		WindowManager wm = (WindowManager)getSystemService(WINDOW_SERVICE);
		int height = wm.getDefaultDisplay().getHeight();
		int width = wm.getDefaultDisplay().getWidth();*/    
	    
	    DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        float  width=(dm.widthPixels*dm.density);   
        float height=(dm.heightPixels*dm.density);  
  
		
		initNode(width,height);//��ʼ��9���ڵ������
		
		new Handler().postDelayed(new Runnable() {  //��ʱ1������·��
		      public void run() {
		    	  drawPath(); //����ͣ��·������
		      }
		    }, 2000);	
	}
	

	public void drawPath(){//��������ڽ����·��
		
				int index =0;
				 Bitmap photo = BitmapFactory.decodeResource(getResources(), R.drawable.ipark02);  //��bitMap����ͼƬ��ͼ��
			     Bitmap tempBitmap = photo.copy(Bitmap.Config.ARGB_8888, true);
			     Canvas canvas = new Canvas(tempBitmap);
			     Paint paint = new Paint();    
			     paint.setStyle(Paint.Style.STROKE);//�����  
			     paint.setStrokeWidth(35);  //�ߵĿ��
			     paint.setColor(Color.GREEN); 
			     
			     nodesXY[index]=startNode.nodeX;index++;  //�������
			     nodesXY[index]=startNode.nodeY;index++;
			    Log.e("���", startNode.nodeX +" "+startNode.nodeY ); 
			     for(int i=1;i<Receive.nodeLenth;i++ ){
			    	 for(int j =1;j<10;j++){
			    		 if(Receive.nodeName[i].equals(nodes[j].Name)){ 
			    			nodesXY[index]=nodes[j].nodeX;index++;
			    			nodesXY[index]=nodes[j].nodeY;index++;
			    	Log.e(j+"x  y", nodes[j].nodeX+" "+nodes[j].nodeY+"");		
			    			if(i>=1 && i<(Receive.nodeLenth-1)){
			    				nodesXY[index]=nodes[j].nodeX;index++;
			    			    nodesXY[index]=nodes[j].nodeY;index++;
			    			  Log.e(j+"x  y", nodes[j].nodeX+" "+nodes[j].nodeY+"");
			    			}
			    		 }
			    	 }
			    	     
			     }
			     canvas.drawLines(nodesXY, paint);  //paint ���� nodesXY���黭��·��  	     
			     img.setImageBitmap(tempBitmap); 
		
	}
	/*      �������ǵ�pts�ǳ�������:
      {50, 600, 400, 600, 400, 600, 400, 50,400, 50, 50, 50,50, 50, 50, 600}
      ������ȡǰ�ĸ� pts[0], pts[1], pts[2], pts[3]: 50, 600, 400, 600
                 ǰ������ʾ����ֱ�����ĺ������꣬��������ʾ����ֱ���յ�ĺ������ꡣ*/
    
	
	
	public void initNode( float width, float height){  //��ʼ���ڵ�ľŸ�����
	
		
		 nodes[1] = new Node("1",(float)((double)width * 0.03),(float)((double)height * 0.05));
		 nodes[2] = new Node("2",(float)((double)width * 0.265),(float)((double)height * 0.05));
		 nodes[3] = new Node("3",(float)((double)width * 0.488),(float)((double)height * 0.05));
		 nodes[4] = new Node("4",(float)((double)width * 0.03),(float)((double)height * 0.235));
		 nodes[5] = new Node("5",(float)((double)width * 0.265),(float)((double)height * 0.235));
		 nodes[6] = new Node("6",(float)((double)width * 0.488),(float)((double)height * 0.235));
		 nodes[7] = new Node("7",(float)((double)width * 0.03),(float)((double)height * 0.42));		 
		 nodes[8] = new Node("8",(float)((double)width * 0.265),(float)((double)height * 0.42));
		 nodes[9] = new Node("9",(float)((double)width * 0.488),(float)((double)height * 0.42));
		 startNode = new Node("start",(float)((double)width * 0.03),(float)((double)height * 0) );//�������
		 endNode = new Node("end",(float)((double)width * 0.02),(float)((double)height * 1) );//��������
		
		/* nodes[1] = new Node("1",(float)((double)width * 0.06),(float)((double)height * 0.1));
		 nodes[2] = new Node("2",(float)((double)width * 0.535),(float)((double)height * 0.1));
Log.e("2", nodes[2].nodeX +" "+nodes[2].nodeY );
		 nodes[3] = new Node("3",(float)((double)width * 0.98),(float)((double)height * 0.1));
		 nodes[4] = new Node("4",(float)((double)width * 0.06),(float)((double)height * 0.46));
		 nodes[5] = new Node("5",(float)((double)width * 0.535),(float)((double)height * 0.46));
Log.e("5", nodes[5].nodeX +" "+nodes[5].nodeY );
		 nodes[6] = new Node("6",(float)((double)width * 0.98),(float)((double)height * 0.46));
		 nodes[7] = new Node("7",(float)((double)width * 0.06),(float)((double)height * 0.84));		 
		 nodes[8] = new Node("8",(float)((double)width * 0.535),(float)((double)height * 0.84));
Log.e("8", nodes[8].nodeX +" "+nodes[8].nodeY );	
		 nodes[9] = new Node("9",(float)((double)width * 0.98),(float)((double)height * 0.84));
Log.e("9", nodes[9].nodeX +" "+nodes[9].nodeY );	
		 startNode = new Node("start",(float)((double)width * 0.06),(float)((double)height * 0) );//�������
		 endNode = new Node("end",(float)((double)width * 0.06),(float)((double)height * 1) );//��������
*/
		
			}
	
	
}
