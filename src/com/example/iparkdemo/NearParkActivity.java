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
	  //定位需要的声明
	    private AMapLocationClient mLocationClient = null;//定位发起端
	    private AMapLocationClientOption mLocationOption = null;//定位参数
	    private OnLocationChangedListener mListener = null;//定位监听器
	  //标识，用于判断是否只显示一次定位信息和用户重新定位
	    private boolean isFirstLoc = true;
	    private Context context;
	    
	    private EditText chaxun_et;
		private Button dingwei_bt;
		private ListView lv_Result;
		 // 兴趣点查询先关
	    private PoiSearch search;
	    //会返回查询数据的类
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
	        //设置定位监听
	        aMap.setLocationSource(this);
	        // 是否显示定位按钮
	        settings.setMyLocationButtonEnabled(true);
	        // 是否可触发定位并显示定位层
	        aMap.setMyLocationEnabled(true);
	        
	        
	        
	        //定位的小图标 默认是蓝点 这里自定义
	        MyLocationStyle myLocationStyle = new MyLocationStyle();
	        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.dw));
	        myLocationStyle.strokeColor(Color.RED);// 设置圆形的边框颜色   
	        myLocationStyle.strokeColor(R.color.deepskyblue);
	        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
	        aMap.setMyLocationStyle(myLocationStyle);
	        //开始定位
	        initLoc();
	        

	        
	        dingwei_bt.setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					String keyword = chaxun_et.getText().toString();
					//2.判断用户是否输入为空
					if (keyword.trim().length() == 0) {
						Toast.makeText(context, "请输入查询关键字", Toast.LENGTH_LONG).show();
					} else {
					    //3.不为空进行搜索	
						search(keyword);
						aMap.clear();// 清理之前的图标
						
						
						MarkerOptions markerOptions = new MarkerOptions();   					 
				        //添加一个位置--经度，维度---marker对应一个markerOptions，用来设置marker的属性等  
				        markerOptions.position(new LatLng(location.getLatitude(),location.getLongitude()));  
				        //添加图标  
				        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.dw));;  
				        //添加marker  
				        markerOptions.title(location.getCity()+location.getStreet()+location.getAoiName());
				        Marker marker = aMap.addMarker(markerOptions);  
				        marker.showInfoWindow();// 设置默认显示一个infoWinfow
				        
					}					
				}
			});
	        
	        map_type_normal_rb.setOnClickListener(new OnClickListener() {   //设置地图模式为普通模式
				
				@Override
				public void onClick(View v) {
					 aMap.setMapType(AMap.MAP_TYPE_NORMAL);
				}
			});
	    
	    
	        map_type_satellite_rb.setOnClickListener(new OnClickListener() {  //设置地图模式为卫星模式
			
			@Override
			public void onClick(View v) {
				 aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
			}
		});
	        
	        
    }
	        
	       
 
	   
	        private void initView(Bundle savedInstanceState){  //初始化
	        mapView = (MapView) findViewById(R.id.map);
	        map_type_normal_rb = (RadioButton) findViewById(R.id. map_type_normal_rb);
	        map_type_satellite_rb = (RadioButton) findViewById(R.id. map_type_satellite_rb);
	        mapView.onCreate(savedInstanceState);// 必须要写
	        aMap = mapView.getMap();
	        
	        aMap.setTrafficEnabled(true);// 显示实时交通状况
	        //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
	   
	        
	        context=this;
	        chaxun_et = (EditText) findViewById(R.id.chaxun_et);
			dingwei_bt = (Button) findViewById(R.id.dingwei_bt);
			lv_Result = (ListView) findViewById(R.id.lv_result);			
			
	       
	    }
	    
	
		private void search(String keyword) {
			// 初始化查询条件
			query = new Query(keyword, "停车场", city);
			query.setPageSize(50);
			query.setPageNum(0);
			// 查询兴趣点
			search = new PoiSearch(this, query);
			// 异步搜索
			search.searchPOIAsyn();
			search.setOnPoiSearchListener(this);
		}
		
		
	
	    //定位
	    private void initLoc() {
	     //初始化定位
	     mLocationClient = new AMapLocationClient(getApplicationContext());
	     //设置定位回调监听
	     mLocationClient.setLocationListener(this);
	     //初始化定位参数
	     mLocationOption = new AMapLocationClientOption();
	     //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
	     mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
	     //设置是否返回地址信息（默认返回地址信息）
	     mLocationOption.setNeedAddress(true);
	     //设置是否只定位一次,默认为false
	     mLocationOption.setOnceLocation(false);
	     //设置是否强制刷新WIFI，默认为强制刷新
	     mLocationOption.setWifiActiveScan(true);
	     //设置是否允许模拟位置,默认为false，不允许模拟位置
	     mLocationOption.setMockEnable(false);
	     //设置定位间隔,单位毫秒,默认为2000ms
	     mLocationOption.setInterval(2000);
	     //给定位客户端对象设置定位参数
	     mLocationClient.setLocationOption(mLocationOption);
	     //启动定位
	     mLocationClient.startLocation();
	    }
	    
	    
	    
	  //定位回调函数
	    @Override
	    public void onLocationChanged(AMapLocation amapLocation) {
	     if (amapLocation != null) {
	      if (amapLocation.getErrorCode() == 0) {
	       //定位成功回调信息，设置相关消息
	       location = amapLocation;
	       amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见官方定位类型表
	       amapLocation.getLatitude();//获取纬度
	       amapLocation.getLongitude();//获取经度
	       amapLocation.getAccuracy();//获取精度信息
	       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	       Date date = new Date(amapLocation.getTime());
	       df.format(date);//定位时间
	       amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
	       amapLocation.getCountry();//国家信息
	       amapLocation.getProvince();//省信息
	       amapLocation.getCity();//城市信息
	       amapLocation.getDistrict();//城区信息
	       amapLocation.getStreet();//街道信息
	       amapLocation.getStreetNum(); amapLocation.getCountry();//国家信息//街道门牌号信息
	       amapLocation.getCityCode();//城市编码
	       amapLocation.getAdCode();//地区编码
	       city = amapLocation.getProvince();
	    
	       // 如果不设置标志位，此时再拖动地图时，它会不断将地图移动到当前的位置
	       if (isFirstLoc) {
	        //设置缩放级别
	        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
	        //将地图移动到定位点
	        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude())));
	        //点击定位按钮 能够将地图的中心移动到定位点
	        mListener.onLocationChanged(amapLocation);
	        //添加图钉
	       // aMap.addMarker(getMarkerOptions(amapLocation));
	        //获取定位信息
	        StringBuffer buffer = new StringBuffer();
	        buffer.append(amapLocation.getCountry() + " " + amapLocation.getProvince() + " " + amapLocation.getCity() + " " + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum()+ "" + amapLocation.getBuildingId());
	        Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_LONG).show();
	        isFirstLoc = false;
	       }
	    
	      } else {
	       //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
	       Log.e("AmapError", "location Error, ErrCode:"
	         + amapLocation.getErrorCode() + ", errInfo:"
	         + amapLocation.getErrorInfo());	    
	       Toast.makeText(getApplicationContext(), "定位失败", Toast.LENGTH_LONG).show();
	      }
	     }
	    }
	    
	    /*
	  //自定义一个图钉，并且设置图标，当我们点击图钉时，显示设置的信息
	    private MarkerOptions getMarkerOptions(AMapLocation amapLocation) {
	      //设置图钉选项
	     MarkerOptions options = new MarkerOptions();
	     //图标
	     options.icon(BitmapDescriptorFactory.fromResource(R.drawable.car));
	     //位置
	     options.position(new LatLng(amapLocation.getLatitude(), amapLocation.getLongitude()));
	     StringBuffer buffer = new StringBuffer();
	     buffer.append(amapLocation.getCountry() + "" + amapLocation.getProvince() + "" + amapLocation.getCity() + "" + amapLocation.getDistrict() + "" + amapLocation.getStreet() + "" + amapLocation.getStreetNum());
	     //标题
	     options.title(buffer.toString());
	     //子标题
	     options.snippet("我的位置");
	     //设置多少帧刷新一次图片资源
	     options.period(60);
	     return options;
	    
	    }
	    */
	    //激活定位
	    @Override
	    public void activate(OnLocationChangedListener listener) {
	     mListener = listener;
	    }
	    
	    //停止定位
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
	     * 方法必须重写
	     * map的生命周期方法
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
	 					strs.add(item.getTitle()+" 距离您有"+ getDistance(item)+"km");
	 					   
	 					 	MarkerOptions markerOptions = new MarkerOptions();   					 
					        //添加一个位置--经度，维度---marker对应一个markerOptions，用来设置marker的属性等  
					        markerOptions.position(new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude()));  
					        //添加图标  
					        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
					        markerOptions.title(item.getTitle()+"  距离您有"+ getDistance(item) + "km");
					        //添加marker  
					        Marker marker = aMap.addMarker(markerOptions);  
					      
					        marker.showInfoWindow();// 设置默认显示一个infoWinfow
	 				}
	 				
	 				//将地图移动到poi点
	 				item = items.get(0);
	 				 //设置缩放级别
	 		        aMap.moveCamera(CameraUpdateFactory.zoomTo(8));
	 		        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude())));
	 				// 给ListView赋值，显示结果
	 				ArrayAdapter<String> array = new ArrayAdapter<String>(this,
	 						android.R.layout.simple_list_item_1, strs);
	 				lv_Result.setAdapter(array);
	 				
	 				lv_Result.setOnItemClickListener(new OnItemClickListener(){  
	 				    @Override
	 					  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
	 				    	  PoiItem item = items.get(position);
	 				    	 
	 				    	    //设置缩放级别
	 					        aMap.moveCamera(CameraUpdateFactory.zoomTo(15));
	 					        //将地图移动到定位点
	 					        aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(item.getLatLonPoint().getLatitude(), item.getLatLonPoint().getLongitude())));
	 					           		         
	 				        }
	 				});	    
	 				
	 			}else{
	 				Toast.makeText(context, "附近没有找到您要搜索的停车场", Toast.LENGTH_LONG).show();
	 			}
	 		}
	        
	        
	        /*
	    	 * 计算两点之间距离
	    	 * 
	    	 * @param start
	    	 * 
	    	 * @param end
	    	 * 
	    	 * @return 米
	    	 */
	    	public double getDistance(PoiItem end)
	    	{

	    		double lon1 = (Math.PI / 180) * location.getLongitude();
	    		double lon2 = (Math.PI / 180) * end.getLatLonPoint().getLongitude();
	    		double lat1 = (Math.PI / 180) * location.getLatitude();
	    		double lat2 = (Math.PI / 180) * end.getLatLonPoint().getLatitude();

	    		// 地球半径
	    		double R = 6371;

	    		// 两点间距离 km，如果想要米的话，结果*1000就可以了
	    		double d = Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2 - lon1)) * R;
	    		
	    		return Double.parseDouble(String.format("%.2f", d));
	    	}
	       
	             

	    /**
	     * 方法必须重写
	     * map的生命周期方法
	     */
	    @Override
	    public void onSaveInstanceState(Bundle outState) {
	        super.onSaveInstanceState(outState);
	        mapView.onSaveInstanceState(outState);
	    }

	    /**
	     * 方法必须重写
	     * map的生命周期方法
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