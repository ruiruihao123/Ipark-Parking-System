package com.example.iparkdemo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;

import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.poisearch.PoiSearch.Query;

public class NearParkActivity extends Activity implements LocationSource, AMapLocationListener, OnPoiSearchListener {
    	private MapView mapView;
	    private AMap aMap;
	  //��λ��Ҫ������
	    private AMapLocationClient mLocationClient = null;//��λ�����
	    private AMapLocationClientOption mLocationOption = null;//��λ����
	    private OnLocationChangedListener mListener = null;//��λ������
	  //��ʶ�������ж��Ƿ�ֻ��ʾһ�ζ�λ��Ϣ���û����¶�λ
	    private boolean isFirstLoc = true;
	    private Context context;
	    
	    private EditText chaxun_et;
		private Button dingwei_bt;
		private ListView lv_Result;
		 // ��Ȥ���ѯ�ȹ�
	    private PoiSearch search;
	    //�᷵�ز�ѯ���ݵ���
	    private PoiSearch.Query query;
	    private String city;
	    private  RadioButton  map_type_normal_rb;
	    private  RadioButton  map_type_satellite_rb;
	    private ArrayList<PoiItem> items;
	    private AMapLocation location = null;
	   
	   
	    
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.near_park_activity);
	        
	        initView(savedInstanceState);
	        UiSettings settings = aMap.getUiSettings();
	        //���ö�λ����
	        aMap.setLocationSource(this);
	        // �Ƿ���ʾ��λ��ť
	        settings.setMyLocationButtonEnabled(true);
	        // �Ƿ�ɴ�����λ����ʾ��λ��
	        aMap.setMyLocationEnabled(true);
	        
	        
	        
	        //��λ��Сͼ�� Ĭ�������� �����Զ���
	        MyLocationStyle myLocationStyle = new MyLocationStyle();
	        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.dw));
	        myLocationStyle.strokeColor(Color.RED);// ����Բ�εı߿���ɫ   
	        myLocationStyle.strokeColor(R.color.deepskyblue);
	        myLocationStyle.strokeWidth(1.0f);// ����Բ�εı߿��ϸ
	        aMap.setMyLocationStyle(myLocationStyle);
	        //��ʼ��λ
	        initLoc();
	        

	        
	        dingwei_bt.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					String keyword = chaxun_et.getText().toString();
					//2.�ж��û��Ƿ�����Ϊ��
					if (keyword.trim().length() == 0) {
						Toast.makeText(context, "�������ѯ�ؼ���", Toast.LENGTH_LONG).show();
					} else {
					    //3.��Ϊ�ս�������	
						search(keyword);
						aMap.clear();// ����֮ǰ��ͼ��
						
						
						MarkerOptions markerOptions = new MarkerOptions();   					 
				        //���һ��λ��--���ȣ�ά��---marker��Ӧһ��markerOptions����������marker�����Ե�  
				        markerOptions.position(new LatLng(location.getLatitude(),location.getLongitude()));  
				        //���ͼ��  
				        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dw));;  
				        //���marker  
				        markerOptions.title(location.getCity()+location.getStreet()+location.getAoiName());
				        Marker marker = aMap.addMarker(markerOptions);  
				        marker.showInfoWindow();// ����Ĭ����ʾһ��infoWinfow
				        
					}					
				}
			});
	        
	        map_type_normal_rb.setOnClickListener(new OnClickListener() {   //���õ�ͼģʽΪ��ͨģʽ
				
				@Override
				public void onClick(View v) {
					 aMap.setMapType(AMap.MAP_TYPE_NORMAL);
				}
			});
	    
	    
	        map_type_satellite_rb.setOnClickListener(new OnClickListener() {  //���õ�ͼģʽΪ����ģʽ
			
			@Override
			public void onClick(View v) {
				 aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
			}
		});
	        
	        
    }
	        
	       
 
	   
	        private void initView(Bundle savedInstanceState){  //��ʼ��
	        mapView = (MapView) findViewById(R.id.map);
	        map_type_normal_rb = (RadioButton) findViewById(R.id. map_type_normal_rb);
	        map_type_satellite_rb = (RadioButton) findViewById(R.id. map_type_satellite_rb);
	        mapView.onCreate(savedInstanceState);// ����Ҫд
	        aMap = mapView.getMap();
	        
	        aMap.setTrafficEnabled(true);// ��ʾʵʱ��ͨ״��
	        //��ͼģʽ��ѡ���ͣ�MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
	   
	        
	        context=this;
	        chaxun_et = (EditText) findViewById(R.id.chaxun_et);
			dingwei_bt = (Button) findViewById(R.id.dingwei_bt);
			lv_Result = (ListView) findViewById(R.id.lv_result);			
			
	       
	    }
	    
	
		private void search(String keyword) {
			// ��ʼ����ѯ����
			query = new Query(keyword, "ͣ����", city);
			query.setPageSize(50);
			query.setPageNum(0);
			// ��ѯ��Ȥ��
			search = new PoiSearch(this, query);
			// �첽����
			search.searchPOIAsyn();
			search.setOnPoiSearchListener(this);
		}
		
		
	
	    //��λ
	    private void initLoc() {
	     //��ʼ����λ
	     mLocationClient = new AMapLocationClient(getApplicationContext());
	     //���ö�λ�ص�����
	     mLocationClient.setLocationListener(this);
	     //��ʼ����λ����
	     mLocationOption = new AMapLocationClientOption();
	     //���ö�λģʽΪ�߾���ģʽ��Battery_SavingΪ�͹���ģʽ��Device_Sensors�ǽ��豸ģʽ
	     mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
	     //�����Ƿ񷵻ص�ַ��Ϣ��Ĭ�Ϸ��ص�ַ��Ϣ��
	     mLocationOption.setNeedAddress(true);
	     //�����Ƿ�ֻ��λһ��,Ĭ��Ϊfalse
	     mLocationOption.setOnceLocation(false);
	     //�����Ƿ�ǿ��ˢ��WIFI��Ĭ��Ϊǿ��ˢ��
	     mLocationOption.setWifiActiveScan(true);
	     //�����Ƿ�����ģ��λ��,Ĭ��Ϊfalse��������ģ��λ��
	     mLocationOption.setMockEnable(false);
	     //���ö�λ���,��λ����,Ĭ��Ϊ2000ms
	     mLocationOption.setInterval(2000);
	     //����λ�ͻ��˶������ö�λ����
	     mLocationClient.setLocationOption(mLocationOption);
	     //������λ
	     mLocationClient.startLocation();
	    }
	    
	    
	    
	  //��λ�ص�����
	    @Override
	    public void onLocationChanged(AMapLocation amapLocation) {
	     if (amapLocation != null) {
	      if (amapLocation.getErrorCode() == 0) {
	       //��λ�ɹ��ص���Ϣ�����������Ϣ
	       location = amapLocation;
	       amapLocation.getLocationType();//��ȡ��ǰ��λ�����Դ�������綨λ���������ٷ���λ���ͱ�
	       amapLocation.getLatitude();//��ȡγ��
	       amapLocation.getLongitude();//��ȡ����
	       amapLocation.getAccuracy();//��ȡ������Ϣ
	       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	       Date date = new Date(amapLocation.getTime());
	       df.format(date);//��λʱ��
	       amapLocation.getAddress();//��ַ�����option������isNeedAddressΪfalse����û�д˽�������綨λ����л��е�ַ��Ϣ��GPS��λ�����ص�ַ��Ϣ��
	       amapLocation.getCountry();//������Ϣ
	       amapLocation.getProvince();//ʡ��Ϣ
	       amapLocation.getCity();//������Ϣ
	       amapLocation.getDistrict();//������Ϣ
	       amapLocation.getStreet();//�ֵ���Ϣ
	       amapLocation.getStreetNum(); amapLocation.getCountry();//������Ϣ//�ֵ����ƺ���Ϣ
	       amapLocation.getCityCode();//���б���
	       amapLocation.getAdCode();//��������
	       city = amapLocation.getProvince();
	    
	       // ��������ñ�־λ����ʱ���϶���ͼʱ�����᲻�Ͻ���ͼ�ƶ�����ǰ��λ��
	       if (isFirstLoc) {
	        //�������ż���
	        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
	        //����ͼ�ƶ�����λ��
	        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
	        //�����λ��ť �ܹ�����ͼ�������ƶ�����λ��
	        mListener.onLocationChanged(amapLocation);
	        //���ͼ��
	       // aMap.addMarker(getMarkerOptions(amapLocation));
	        //��ȡ��λ��Ϣ
	        StringBuffer buffer = new StringBuffer();
	        buffer.append(amapLocation.getCountry() + " " + amapLocation.getProvince() + " " + amapLocation.getCity() + " " + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum()+ "" + amapLocation.getBuildingId());
	        Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
	        isFirstLoc = false;
	       }
	    
	      } else {
	       //��ʾ������ϢErrCode�Ǵ����룬errInfo�Ǵ�����Ϣ������������
	       Log.e("AmapError", "location Error, ErrCode:"
	         + amapLocation.getErrorCode() + ", errInfo:"
	         + amapLocation.getErrorInfo());	    
	       Toast.makeText(getApplicationContext(), "��λʧ��", Toast.LENGTH_LONG).show();
	      }
	     }
	    }
	    
	    /*
	  //�Զ���һ��ͼ������������ͼ�꣬�����ǵ��ͼ��ʱ����ʾ���õ���Ϣ
	    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
	      //����ͼ��ѡ��
	     MarkerOptions options = new MarkerOptions();
	     //ͼ��
	     options.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
	     //λ��
	     options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
	     StringBuffer buffer = new StringBuffer();
	     buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
	     //����
	     options.title(buffer.toString());
	     //�ӱ���
	     options.snippet("�ҵ�λ��");
	     //���ö���֡ˢ��һ��ͼƬ��Դ
	     options.period(60);
	     return options;
	    
	    }
	    */
	    //���λ
	    @Override
	    public void activate(OnLocationChangedListener listener) {
	     mListener = listener;
	    }
	    
	    //ֹͣ��λ
	    @Override
	    public void deactivate() {
	     mListener = null;
	    }
	    
	    
	    @Override
	    public void onResume() {
	        super.onResume();
	        mapView.onResume();

	    }

	    /**
	     * ����������д
	     * map���������ڷ���
	     */
	    @Override
	    public void onPause() {
	        super.onPause();
	        mapView.onPause();

	    }
	    
	        @Override
	 		public void onPoiSearched(PoiResult poiResult, int rCode) {
	 			List<String> strs = new ArrayList<String>();
	 			 items = poiResult.getPois();
	 			if (items != null && items.size() > 0) {
	 				PoiItem item = null;
	 				for (int i = 0, count = items.size(); i < count; i++) {
	 					item = items.get(i);
	 					strs.add(item.getTitle()+" ��������"+ getDistance(item)+"km");
	 					   
	 					 	MarkerOptions markerOptions = new MarkerOptions();   					 
					        //���һ��λ��--���ȣ�ά��---marker��Ӧһ��markerOptions����������marker�����Ե�  
					        markerOptions.position(new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude()));  
					        //���ͼ��  
					        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
					        markerOptions.title(item.getTitle()+"  ��������"+ getDistance(item) + "km");
					        //���marker  
					        Marker marker = aMap.addMarker(markerOptions);  
					      
					        marker.showInfoWindow();// ����Ĭ����ʾһ��infoWinfow
	 				}
	 				
	 				//����ͼ�ƶ���poi��
	 				item = items.get(0);
	 				 //�������ż���
	 		        aMap.moveCamera(CameraUpdateFactory.zoomTo(8));
	 		        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude())));
	 				// ��ListView��ֵ����ʾ���
	 				ArrayAdapter<String> array = new ArrayAdapter<String>(this,
	 						android.R.layout.simple_list_item_1, strs);
	 				lv_Result.setAdapter(array);
	 				
	 				lv_Result.setOnItemClickListener(new OnItemClickListener(){  
	 				    @Override
	 					  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	 				    	  PoiItem item = items.get(position);
	 				    	 
	 				    	    //�������ż���
	 					        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
	 					        //����ͼ�ƶ�����λ��
	 					        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude())));
	 					           		         
	 				        }
	 				});	    
	 				
	 			}else{
	 				Toast.makeText(context, "����û���ҵ���Ҫ������ͣ����", Toast.LENGTH_LONG).show();
	 			}
	 		}
	        
	        
	        /*
	    	 * ��������֮�����
	    	 * 
	    	 * @param start
	    	 * 
	    	 * @param end
	    	 * 
	    	 * @return ��
	    	 */
	    	public double getDistance(PoiItem end)
	    	{

	    		double lon1 = (Math.PI / 180) * location.getLongitude();
	    		double lon2 = (Math.PI / 180) * end.getLatLonPoint().getLongitude();
	    		double lat1 = (Math.PI / 180) * location.getLatitude();
	    		double lat2 = (Math.PI / 180) * end.getLatLonPoint().getLatitude();

	    		// ����뾶
	    		double R = 6371;

	    		// �������� km�������Ҫ�׵Ļ������*1000�Ϳ�����
	    		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
	    		
	    		return Double.parseDouble(String.format("%.2f", d));
	    	}
	       
	             

	    /**
	     * ����������д
	     * map���������ڷ���
	     */
	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        mapView.onSaveInstanceState(outState);
	    }

	    /**
	     * ����������д
	     * map���������ڷ���
	    */
	    
	    @Override
	    public void onDestroy() {
	        super.onDestroy();
	        mapView.onDestroy();
	    }

	    @Override
		public void onPoiItemSearched(PoiItem arg0, int arg1) {
			
			
		}

		
	    
	    
}