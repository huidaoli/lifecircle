package com.weiyoung.yourlifecircle;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.weiyoung.service.YLC_SAVEUSERSERVICE;
import com.weiyoung.utils.YLC_HttpClient;
import com.weiyoung.utils.YLC_SaveUserInfo;
import com.weiyoung.variable.LoginInfo;

import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class YLC_Main extends Activity implements OnTouchListener {
	private Tencent mtencent;
	private static final String AppID = "101027899";
	private static final String Scope = "all";
	private TextView nickname_textview;
	private ImageView imageView;
	private ImageView BDMapService_imageview;
	private ImageView BDMAPLocation_imageview;
	private ImageView BDSet_imageview;
	// ��ͼ������
	private MapView mapView = null;
	private MapController mapController = null;
	private MKMapViewListener mkMapViewListener = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()  
        .detectDiskReads()  
        .detectDiskWrites()  
        .detectNetwork()   // or .detectAll() for all detectable problems  
        .penaltyLog()  
        .build());  
    	StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()  
        .detectLeakedSqlLiteObjects()  
        .detectLeakedClosableObjects()  
        .penaltyLog()  
        .penaltyDeath()  
        .build());
		YLC_APPLICATION ylc_Application = (YLC_APPLICATION) this
				.getApplication();
		if (ylc_Application.bMapManager == null) {
			ylc_Application.bMapManager = new BMapManager(this);
			ylc_Application.bMapManager.init(YLC_APPLICATION.STR_KEY,
					new YLC_APPLICATION.MyMKGeneralListener());
			ylc_Application.bMapManager.start();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ylc__main);
		initMap();
		mtencent = Tencent.createInstance(AppID, this.getApplicationContext());
		findView();
	}

	public void findView() {
		nickname_textview=(TextView) findViewById(R.id.nickname_textview);
		imageView = (ImageView) findViewById(R.id.imageView);
		BDMapService_imageview = (ImageView) findViewById(R.id.BDMapService_imageview);
		BDMAPLocation_imageview = (ImageView) findViewById(R.id.BDMAPLocation_imageview);
		BDSet_imageview = (ImageView) findViewById(R.id.BDSet_imageview);
		BDMapService_imageview.setOnTouchListener(this);
		BDMAPLocation_imageview.setOnTouchListener(this);
		BDSet_imageview.setOnTouchListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mapView.destroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		mapView.onRestoreInstanceState(savedInstanceState);
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
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

	public void initMap() {
		mapView = (MapView) findViewById(R.id.bmapView);
		mapController = mapView.getController();
		mapController.enableClick(true);
		mapController.setZoom(12);
		/**
		 * ����ͼ�ƶ���ָ����
		 * ʹ�ðٶȾ�γ�����꣬����ͨ��http://api.map.baidu.com/lbsapi/getpoint/index
		 * .html��ѯ�������� �����Ҫ�ڰٶȵ�ͼ����ʾʹ����������ϵͳ��λ�ã��뷢�ʼ���mapapi@baidu.com��������ת���ӿ�
		 */
		GeoPoint p;
		double cLat = 39.945;
		double cLon = 116.404;
		Intent intent = getIntent();
		if (intent.hasExtra("x") && intent.hasExtra("y")) {
			// ����intent����ʱ���������ĵ�Ϊָ����
			Bundle b = intent.getExtras();
			p = new GeoPoint(b.getInt("y"), b.getInt("x"));
		} else {
			// �������ĵ�Ϊ�찲��
			p = new GeoPoint((int) (cLat * 1E6), (int) (cLon * 1E6));
		}

		mapController.setCenter(p);

		/**
		 * MapView������������Activityͬ������activity����ʱ�����MapView.onPause()
		 */
		mkMapViewListener = new MKMapViewListener() {
			@Override
			public void onMapMoveFinish() {
				/**
				 * �ڴ˴����ͼ�ƶ���ɻص� ���ţ�ƽ�ƵȲ�����ɺ󣬴˻ص�������
				 */
			}

			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				/**
				 * �ڴ˴����ͼpoi����¼� ��ʾ��ͼpoi���Ʋ��ƶ����õ� ���ù���
				 * mMapController.enableClick(true); ʱ���˻ص����ܱ�����
				 * 
				 */
				String title = "";
				if (mapPoiInfo != null) {
					title = mapPoiInfo.strText;
					Toast.makeText(YLC_Main.this, title, Toast.LENGTH_SHORT)
							.show();
					mapController.animateTo(mapPoiInfo.geoPt);
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				/**
				 * �����ù� mMapView.getCurrentMap()�󣬴˻ص��ᱻ���� ���ڴ˱����ͼ���洢�豸
				 */
			}

			@Override
			public void onMapAnimationFinish() {
				/**
				 * ��ͼ��ɴ������Ĳ�������: animationTo()���󣬴˻ص�������
				 */
			}

			/**
			 * �ڴ˴����ͼ������¼�
			 */
			@Override
			public void onMapLoadFinish() {
				Toast.makeText(YLC_Main.this, "��ͼ�������", Toast.LENGTH_SHORT)
						.show();

			}
		};
		mapView.regMapViewListener(YLC_APPLICATION.getInstance().bMapManager,
				mkMapViewListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__main, menu);
		return true;
	}

	public void userLogin(View view) {
		mtencent.login(YLC_Main.this, Scope, new BaseUiListener());// ��¼
	}

	private class BaseUiListener implements IUiListener {
		@Override
		public void onCancel() {// ȡ����¼
			// TODO Auto-generated method stub

		}

		@Override
		public void onComplete(Object json) {
			// TODO Auto-generated method stub
			JSONObject jsonObject = (JSONObject) json;
			YLC_HttpClient ylc_HttpClient = new YLC_HttpClient();
			Uri uri = ylc_HttpClient.getQQuserInfo(jsonObject);
			LoginInfo.LOGIN_STATE = true; // ��¼�ɹ����õ�¼״̬Ϊtrue;
			nickname_textview.setText(LoginInfo.nickname);
			try {
				Bitmap bitmap = BitmapFactory
						.decodeStream(getImageViewInputStream(uri.toString()));
				imageView.setImageBitmap(bitmap);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//����service�����������������
			Thread saveUserThread=new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					YLC_SaveUserInfo saveUserInfo=new YLC_SaveUserInfo(YLC_Main.this);
					saveUserInfo.saveUser();
				}
			});
			saveUserThread.start();
		}

		@Override
		public void onError(UiError ui) {
			// TODO Auto-generated method stub
			Toast.makeText(YLC_Main.this, "��¼������ȷ�������������" + ui,
					Toast.LENGTH_LONG).show();
		}
	}

	/**
	 * 2 * �������л�ȡͼƬ����������ʽ���� 3 * @return 4
	 */
	public static InputStream getImageViewInputStream(String URL_PATH)
			throws IOException {
		InputStream inputStream = null;
		URL url = new URL(URL_PATH); // ��������ַ
		if (url != null) {
			// ������
			HttpURLConnection httpURLConnection = (HttpURLConnection) url
					.openConnection();
			httpURLConnection.setConnectTimeout(3000);// �����������ӳ�ʱ��ʱ��Ϊ3��
			httpURLConnection.setRequestMethod("GET"); // �������󷽷�ΪGET
			httpURLConnection.setDoInput(true); // ��������
			int responseCode = httpURLConnection.getResponseCode(); // ��ȡ��������Ӧֵ
			if (responseCode == HttpURLConnection.HTTP_OK) { // ��������
				inputStream = httpURLConnection.getInputStream(); // ��ȡ������
			}
		}
		return inputStream;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Color color = new Color();
		switch (v.getId()) {
		case R.id.BDMapService_imageview:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				BDMapService_imageview
						.setBackgroundColor(color.rgb(14, 49, 55));
				BDMapService_imageview.setImageDrawable(getResources()
						.getDrawable(R.drawable.ylc_service_focus));
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				BDMapService_imageview.setBackgroundColor(color.TRANSPARENT);
				BDMapService_imageview.setImageDrawable(getResources()
						.getDrawable(R.drawable.ylc_service));
				/*
				 * Intent intent=new Intent(YLC_Main.this,
				 * LocationOverlayDemoTest.class);
				 * YLC_Main.this.startActivity(intent);
				 */
				showYLCService();
			}
			break;

		case R.id.BDMAPLocation_imageview:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				BDMAPLocation_imageview.setBackgroundColor(color
						.rgb(14, 49, 55));
				BDMAPLocation_imageview.setImageDrawable(getResources()
						.getDrawable(R.drawable.ylc_location_focus));
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				BDMAPLocation_imageview.setBackgroundColor(color.TRANSPARENT);
				BDMAPLocation_imageview.setImageDrawable(getResources()
						.getDrawable(R.drawable.ylc_location));
				Intent intent = new Intent(YLC_Main.this,
						YLC_SelfLocation.class);
				YLC_Main.this.startActivity(intent);
			}
			break;
		case R.id.BDSet_imageview:
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				BDSet_imageview.setBackgroundColor(color.rgb(14, 49, 55));
				BDSet_imageview.setImageDrawable(getResources().getDrawable(
						R.drawable.ylc_set_hover));
			} else if (event.getAction() == MotionEvent.ACTION_UP) {
				BDSet_imageview.setBackgroundColor(color.TRANSPARENT);
				BDSet_imageview.setImageDrawable(getResources().getDrawable(
						R.drawable.ylc_set));
				Intent intent = new Intent(YLC_Main.this, YLC_Set.class);
				YLC_Main.this.startActivity(intent);
			}
			break;
		}
		return false;
	}

	/*
	 * �˵���ť�ĸ���ѡ��
	 */
	public void showYLCService() {
		String[] items = getResources().getStringArray(
				R.array.ylc_string_service);
		AlertDialog dialogService = new AlertDialog.Builder(this)
				.setTitle("�����б�").setItems(items, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = null;
						switch (which) {
						case 0:
							intent = new Intent(YLC_Main.this,
									YLC_POI_Search.class);
							YLC_Main.this.startActivity(intent);
							break;

						case 1:
							intent = new Intent(YLC_Main.this,
									YLC_RoutePlan.class);
							YLC_Main.this.startActivity(intent);
							break;
						case 2:
							intent = new Intent(YLC_Main.this,
									YLC_GeoCoder.class);
							YLC_Main.this.startActivity(intent);
							break;
						case 3:
							intent = new Intent(YLC_Main.this,
									YLC_BusLine.class);
							YLC_Main.this.startActivity(intent);
							break;
						case 4:
							intent = new Intent(YLC_Main.this,
									YLC_YourLifeCircle.class);
							YLC_Main.this.startActivity(intent);
							break;
						}
					}
				}).create();
		WindowManager wm = getWindowManager();
		Display d = wm.getDefaultDisplay();
		WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
		layoutParams.x = -350;
		layoutParams.y = 120;
		layoutParams.gravity = Gravity.BOTTOM;
		layoutParams.width = (int) (d.getWidth() * 0.48);
		layoutParams.height = (int) (d.getHeight() * 0.50);
		layoutParams.alpha = 0.6f;// ���öԻ����͸����
		dialogService.setCanceledOnTouchOutside(true);
		dialogService.show();
		Window window = dialogService.getWindow();

		window.setAttributes(layoutParams);
		// window.setGravity(Gravity.BOTTOM);//���õ����Ի���λ��

	}
	public void showUserInfo(View view){
		if(LoginInfo.userinfo_state){
			List<String> listItemTitle=null;
			List<String> listItemContent=null;
			if(LoginInfo.user_type==1){
				listItemTitle=getQQuserInfoTitle();
				listItemContent=getQQuserInfoContent();
				AlertDialog qquserDialog=new AlertDialog.Builder(this)
											.setTitle("QQ�û�")
											.setAdapter(new UserInfoAdapter(listItemTitle,listItemContent), null)
											.create();
				qquserDialog.show();
											
			}else if(LoginInfo.user_type==2){
				listItemTitle=getQQWBuserInfoTitle();
				listItemContent=getQQWBuserInfoContent();
				AlertDialog qqwbuserDialog=new AlertDialog.Builder(this)
				.setTitle("QQ΢���û�")
				.setAdapter(new UserInfoAdapter(listItemTitle,listItemContent), null)
				.create();
				qqwbuserDialog.show();
			}
		}else{
			Toast.makeText(YLC_Main.this, "�������ڸ��£����Ժ�...", Toast.LENGTH_LONG).show();
		}
	}
	//��sharedpreference��ȡ����
	public List<String> getQQuserInfoTitle(){
		List<String> listItemTitle=new ArrayList<String>();
		listItemTitle.add("QQ�ǳƣ�");
		listItemTitle.add("�Ա�");
		listItemTitle.add("�Ƿ��ǻ���:");
		listItemTitle.add("�Ƿ���VIP��");
		listItemTitle.add("����ȼ���");
		listItemTitle.add("VIP�ȼ���");
		listItemTitle.add("�Ƿ��������:");
		return listItemTitle;
	}
	public List<String> getQQuserInfoContent(){
		List<String> listItemContent=new ArrayList<String>();
		SharedPreferences share=this.getSharedPreferences("qquser", Context.MODE_PRIVATE);
		listItemContent.add(share.getString("nickname", ""));
		listItemContent.add(share.getString("gender", ""));
		if(share.getInt("is_yellow_vip", 10)==0){
			listItemContent.add("��");
		}else{
			listItemContent.add("��");
		}
		if(share.getInt("vip", 10)==0){
			listItemContent.add("��");
		}else{
			listItemContent.add("��");
		}
		if(share.getInt("yellow_vip_level", 10)==0){
			listItemContent.add("��");
		}else{
			listItemContent.add("��");
		}
		if(share.getInt("level", 10)==0){
			listItemContent.add("��");
		}else{
			listItemContent.add("��");
		}
		if(share.getInt("is_yellow_year_vip", 10)==0){
			listItemContent.add("��");
		}else{
			listItemContent.add("��");
		}
		return listItemContent;
	}
	public List<String> getQQWBuserInfoTitle(){
		List<String> listItemTitle=new ArrayList<String>();
		listItemTitle.add("QQ΢���ǳƣ�");
		listItemTitle.add("QQ�ǳƣ�");
		listItemTitle.add("΢���˻���");
		listItemTitle.add("�Ա�");
		listItemTitle.add("�Ƿ��ǻ���:");
		listItemTitle.add("�Ƿ���VIP��");
		listItemTitle.add("����ȼ���");
		listItemTitle.add("VIP�ȼ���");
		listItemTitle.add("�Ƿ��������:");
		listItemTitle.add("�������ڣ�");
		listItemTitle.add("���ڵأ�");
		return listItemTitle;
	}
	public List<String> getQQWBuserInfoContent(){
		List<String> listItemContent=new ArrayList<String>();
		SharedPreferences share=this.getSharedPreferences("qqwbuser", Context.MODE_PRIVATE);
		listItemContent.add(share.getString("nick", ""));
		listItemContent.add(share.getString("nickname", ""));
		listItemContent.add(share.getString("wbcount", ""));
		listItemContent.add(share.getString("gender", ""));
		if(share.getInt("is_yellow_vip", 10)==0){
			listItemContent.add("��");
		}else{
			listItemContent.add("��");
		}
		if(share.getInt("vip", 10)==0){
			listItemContent.add("��");
		}else{
			listItemContent.add("��");
		}
		if(share.getInt("yellow_vip_level", 10)==0){
			listItemContent.add("��");
		}else{
			listItemContent.add("��");
		}
		if(share.getInt("level", 10)==0){
			listItemContent.add("��");
		}else{
			listItemContent.add("��");
		}
		if(share.getInt("is_yellow_year_vip", 10)==0){
			listItemContent.add("��");
		}else{
			listItemContent.add("��");
		}
		listItemContent.add(share.getString("birthday", ""));
		listItemContent.add(share.getString("location", ""));
		return listItemContent;
	}
	class UserInfoAdapter extends BaseAdapter{
		LayoutInflater layoutInflater;
		List<String> listItemTitle;
		List<String> listItemContent;
		public UserInfoAdapter(List<String> listItemTitle,List<String> listItemContent){
			this.listItemTitle=listItemTitle;
			this.listItemContent=listItemContent;
			layoutInflater=(LayoutInflater) YLC_Main.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listItemTitle.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View contentview, ViewGroup parent) {
			// TODO Auto-generated method stub
			UserInfoViewHolder viewholder;
			if(contentview==null){
				viewholder=new UserInfoViewHolder();
				contentview=layoutInflater.inflate(R.layout.activity_ylc_userinfo, parent, false);
				viewholder.tv1=(TextView) contentview.findViewById(R.id.textView_item);
				viewholder.tv2=(TextView) contentview.findViewById(R.id.textView_content);
				contentview.setTag(viewholder);
			}else{
				viewholder=(UserInfoViewHolder) contentview.getTag();
			}
			viewholder.tv1.setText(listItemTitle.get(position));
			viewholder.tv2.setText(listItemContent.get(position));
			return contentview;
		}
		
	}
	static class UserInfoViewHolder{
		TextView tv1;
		TextView tv2;
	}
}
