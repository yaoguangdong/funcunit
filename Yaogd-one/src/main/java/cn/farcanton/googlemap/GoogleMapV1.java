//package cn.farcanton.googlemap;
//
//import java.util.List;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.graphics.Point;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.view.KeyEvent;
//import android.view.Menu;
//import android.view.MenuItem;
//import cn.farcanton.R;
//
//import com.google.android.maps.GeoPoint;
//import com.google.android.maps.MapActivity;
//import com.google.android.maps.MapController;
//import com.google.android.maps.MapView;
//import com.google.android.maps.Overlay;
//
//public class GoogleMapV1 extends MapActivity {
//
//    /** Called when the activity is first created. */
//	private MapController mapController;
//	private MapView mapView;
//	private MyOverLay myOverLay;
//	 
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.google_map_v1);
//       
//        LocationManager locationManager=(LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        mapView=(MapView) this.findViewById(R.id.mapview_v1);
//        //设置交通模式
//        mapView.setTraffic(true);
//        //设置卫星模式
//        mapView.setSatellite(false);
//        //设置街景模式
//        mapView.setStreetView(false);
//        //设置缩放控制
//        mapView.setBuiltInZoomControls(true);
//        mapView.setClickable(true);
//        mapView.setEnabled(true);
//        //得到MapController实例 
//        mapController=mapView.getController();
//        mapController.setZoom(15);
//        
//        myOverLay=new MyOverLay();
//        List<Overlay> overLays=mapView.getOverlays();
//        overLays.add(myOverLay);
//        
//        Criteria criteria=new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//        criteria.setAltitudeRequired(false);
//        criteria.setBearingRequired(false);
//        criteria.setCostAllowed(false);
//        criteria.setPowerRequirement(Criteria.POWER_LOW);
//        //取得效果最好的Criteria
//        String provider=locationManager.getBestProvider(criteria, true);
//        //得到Location
//        Location location=locationManager.getLastKnownLocation(provider);
//        updateWithLocation(location);
//        //注册一个周期性的更新，3秒一次
//        locationManager.requestLocationUpdates(provider, 3000, 0, locationListener);
//        
//    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//    	// TODO Auto-generated method stub
//    	menu.add(0, 1, 1, "交通模式");
//    	menu.add(0,2,2,"卫星模式");
//    	menu.add(0,3,3,"街景模式");
//    	
//    	return super.onCreateOptionsMenu(menu);
//    }
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//    	// TODO Auto-generated method stub
//    	 super.onOptionsItemSelected(item);
//    	 switch (item.getItemId()) {
//		case 1://交通模式
//			mapView.setTraffic(true);
//			mapView.setSatellite(false);
//			mapView.setStreetView(false);
//			break;
//		case 2://卫星模式
//			mapView.setSatellite(true);
//			mapView.setStreetView(false);
//			mapView.setTraffic(false);
//			break;
//		case 3://街景模式
//			mapView.setStreetView(true);
//			mapView.setTraffic(false);
//			mapView.setSatellite(false);
//			break;
//		default:
//			mapView.setTraffic(true);
//			mapView.setSatellite(false);
//			mapView.setStreetView(false);
//			break;
//		}
//    	return true;
//    }
//    private void updateWithLocation(Location location){
//    	if(location!=null){
//    		//为绘制类设置坐标
//    		myOverLay.setLocation(location);
//    		GeoPoint geoPoint=new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
//    	    //定位到指定的坐标
//    		mapController.animateTo(geoPoint);
//    		mapController.setZoom(15);
//    	}
//    }
//    private final LocationListener locationListener=new LocationListener() {
//		
//		@Override
//		public void onStatusChanged(String provider, int status, Bundle extras) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void onProviderEnabled(String provider) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void onProviderDisabled(String provider) {
//			// TODO Auto-generated method stub
//			
//		}
//		//当坐标改变时出发此函数
//		@Override
//		public void onLocationChanged(Location location) {
//			// TODO Auto-generated method stub
//			updateWithLocation(location);
//		}
//	};
//    class MyOverLay extends Overlay{
//    	
//    	private Location location;
//    	public void setLocation(Location location){
//    		this.location=location;
//    	}
//    	
//    	@Override
//    	public boolean draw(Canvas canvas, MapView mapView, boolean shadow,
//    			long when) {
//    		// TODO Auto-generated method stub
//    		super.draw(canvas, mapView, shadow);
//    		Paint paint=new Paint();
//    		Point myScreen=new Point();
//    		//将经纬度换成实际屏幕的坐标。
//    		GeoPoint geoPoint=new GeoPoint((int)(location.getLatitude()*1E6), (int)(location.getLongitude()*1E6));
//    		mapView.getProjection().toPixels(geoPoint, myScreen);
//    		paint.setStrokeWidth(1);
//    		paint.setARGB(255, 255, 0, 0);
//    		paint.setStyle(Paint.Style.STROKE);
//    		Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.maps_pin);
//    		//把这张图片画到相应的位置。
//    		canvas.drawBitmap(bmp, myScreen.x, myScreen.y,paint);
//    		canvas.drawText("天堂没有路", myScreen.x, myScreen.y, paint);
//    		return true;
//    		
//    	}
//    }
//	@Override
//	protected boolean isRouteDisplayed() {
//		// TODO Auto-generated method stub
//		return false;
//	}
//	@Override
//	public boolean onKeyDown(int keyCode, KeyEvent event) {
//		// TODO Auto-generated method stub
//
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setMessage("你确定退出吗？")
//					.setCancelable(false)
//					.setPositiveButton("确定",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int id) {
//									GoogleMapV1.this.finish();
//									android.os.Process
//											.killProcess(android.os.Process
//													.myPid());
//									  android.os.Process.killProcess(android.os.Process.myTid());
//						              android.os.Process.killProcess(android.os.Process.myUid());
//								}
//							})
//					.setNegativeButton("返回",
//							new DialogInterface.OnClickListener() {
//								public void onClick(DialogInterface dialog,
//										int id) {
//									dialog.cancel();
//								}
//							});
//			AlertDialog alert = builder.create();
//			alert.show();
//			return true;
//		}
//
//		return super.onKeyDown(keyCode, event);
//	}
//}