package com.weiyoung.yourlifecircle;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.MyLocationOverlay.LocationMode;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.weiyoung.utils.YLC_BDMap_Utils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class YLC_SelfLocation extends Activity {
	private enum Button_Type {
		LOC, COMPASS, FOLLOW
	}

	private Button_Type button_Type;
	// ��λ���
	LocationClient mLocClient; //
	LocationData locData = null; //
	public MyLocationListenner myListener = new MyLocationListenner();
	// ��λͼ��
	locationOverlay myLocationOverlay = null;
	// ��������ͼ��
	private PopupOverlay pop = null;// ��������ͼ�㣬����ڵ�ʱʹ��
	private TextView popupText = null;// ����view
	private View viewCache = null;
	// ��ͼ��أ�ʹ�ü̳�MapView��MyLocationMapViewĿ������дtouch�¼�ʵ�����ݴ���
	// ���������touch�¼���������̳У�ֱ��ʹ��MapView����
	MyLocationMapView mapView = null; // ��ͼView
	private MapController mMapController = null;

	// UI���
	OnCheckedChangeListener radioButtonListener = null;
	Button requestLocButton = null;
	boolean isRequest = false;// �Ƿ��ֶ���������λ
	boolean isFirstLoc = true;// �Ƿ��״ζ�λ

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//�˳�ʱ���ٶ�λ
        if (mLocClient != null)
            mLocClient.stop();
        mapView.destroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		mapView.onPause();
		super.onPause();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		mapView.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		YLC_APPLICATION ylc_Application = (YLC_APPLICATION) this.getApplication();
		/*if (ylc_Application.bMapManager == null) {
			ylc_Application.bMapManager = new BMapManager(this);
			ylc_Application.bMapManager.init(YLC_APPLICATION.STR_KEY,
					new YLC_APPLICATION.MyMKGeneralListener());
		}
		ylc_Application.bMapManager.start();*/
		setContentView(R.layout.activity_ylc_self_location);
		CharSequence titleLable = "���Ҷ�λ";
		setTitle(titleLable);
		requestLocButton = (Button) findViewById(R.id.requestLocButton);
		button_Type = Button_Type.LOC;
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (button_Type) {
				case LOC:
					// �ֶ���λ����
					requestLocClick();
					Log.i("TAG", "requestLocClick()");
					break;
				case COMPASS:
					myLocationOverlay.setLocationMode(LocationMode.NORMAL);
					requestLocButton.setText("��λ");
					button_Type = Button_Type.LOC;
					Log.i("TAG", "COMPASS");
					break;
				case FOLLOW:
					myLocationOverlay.setLocationMode(LocationMode.COMPASS);
					requestLocButton.setText("����");
					button_Type = Button_Type.COMPASS;
					Log.i("TAG", "FOLLOW");
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);
		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioButtonListener = new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.defaulticon) {
					// ����null�򣬻ָ�Ĭ��ͼ��
					modifyLocationOverlayIcon(null);
				}
				if (checkedId == R.id.customicon) {
					// �޸�Ϊ�Զ���marker
					modifyLocationOverlayIcon(getResources().getDrawable(
							R.drawable.icon_geo));
				}
			}
		};
		group.setOnCheckedChangeListener(radioButtonListener);
		// ��ͼ��ʼ��
		mapView = (MyLocationMapView) findViewById(R.id.bmapView_selflocation);
		mMapController = mapView.getController();
		mapView.getController().setZoom(14);
		mapView.getController().enableClick(true);
		mapView.setBuiltInZoomControls(true);
		// ���� ��������ͼ��
		createPaopao();

		// ��λ��ʼ��
		mLocClient = new LocationClient(this);
		locData = new LocationData();
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// ��gps
		option.setCoorType("bd09ll"); // ������������
		option.setScanSpan(1000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		// ��λͼ���ʼ��
		myLocationOverlay = new locationOverlay(mapView);
		// ���ö�λ����
		myLocationOverlay.setData(locData);
		// ��Ӷ�λͼ��
		mapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		// �޸Ķ�λ���ݺ�ˢ��ͼ����Ч
		mapView.refresh();
	}

	/**
	 * ������������ͼ��
	 */
	public void createPaopao() {
		viewCache = getLayoutInflater().inflate(R.layout.ylc_custom_text_view,
				null);
		popupText = (TextView) viewCache.findViewById(R.id.textcache);
		// ���ݵ����Ӧ�ص�
		PopupClickListener popListener = new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
		};
		pop = new PopupOverlay(mapView, popListener);
		MyLocationMapView.pop = pop;
	}

	/**
	 * �޸�λ��ͼ��
	 * 
	 * @param marker
	 */
	public void modifyLocationOverlayIcon(Drawable marker) {
		// ������markerΪnullʱ��ʹ��Ĭ��ͼ�����
		myLocationOverlay.setMarker(marker);
		// �޸�ͼ�㣬��Ҫˢ��MapView��Ч
		mapView.refresh();
	}

	/**
	 * �ֶ�����һ�ζ�λ����
	 */
	public void requestLocClick() {
		isRequest = true;
		mLocClient.requestLocation();
		Toast.makeText(YLC_SelfLocation.this, "���ڶ�λ����", Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * ��λSDK��������
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;

			locData.latitude = location.getLatitude();
			locData.longitude = location.getLongitude();
			// �������ʾ��λ����Ȧ����accuracy��ֵΪ0����
			locData.accuracy = location.getRadius();
			// �˴��������� locData�ķ�����Ϣ, �����λ SDK δ���ط�����Ϣ���û������Լ�ʵ�����̹�����ӷ�����Ϣ��
			locData.direction = location.getDerect();
			// ���¶�λ����
			myLocationOverlay.setData(locData);
			// ����ͼ������ִ��ˢ�º���Ч
			mapView.refresh();
			// ���ֶ�����������״ζ�λʱ���ƶ�����λ��
			if (isRequest || isFirstLoc) {
				// �ƶ���ͼ����λ��
				Log.d("LocationOverlay", "receive location, animate to it");
				mMapController.animateTo(new GeoPoint(
						(int) (locData.latitude * 1e6),
						(int) (locData.longitude * 1e6)));
				isRequest = false;
				myLocationOverlay.setLocationMode(LocationMode.FOLLOWING);
				requestLocButton.setText("����");
				button_Type = Button_Type.FOLLOW;
			}
			// �״ζ�λ���
			isFirstLoc = false;
		}

		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	// �̳�MyLocationOverlay��дdispatchTapʵ�ֵ������
	public class locationOverlay extends MyLocationOverlay {

		public locationOverlay(MapView mapView) {
			super(mapView);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected boolean dispatchTap() {
			// TODO Auto-generated method stub
			// �������¼�,��������
			popupText.setBackgroundResource(R.drawable.popup);
			popupText.setText("�ҵ�λ��");
			pop.showPopup(YLC_BDMap_Utils.getBitmapFromView(popupText),
					new GeoPoint((int) (locData.latitude * 1e6),
							(int) (locData.longitude * 1e6)), 8);
			return true;
		}

	}

}

/**
 * �̳�MapView��дonTouchEventʵ�����ݴ������
 * 
 * @author hejin
 * 
 */
class MyLocationMapView extends MapView {
	static PopupOverlay pop = null;// ��������ͼ�㣬���ͼ��ʹ��

	public MyLocationMapView(Context context) {
		super(context);
	}

	public MyLocationMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyLocationMapView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (!super.onTouchEvent(event)) {
			// ��������
			if (pop != null && event.getAction() == MotionEvent.ACTION_UP)
				pop.hidePop();
		}
		return true;
	}
}
