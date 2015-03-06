package com.weiyoung.yourlifecircle;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class YLC_CustomRouteOverlay extends Activity {
	//��ͼ���
		MapView mMapView = null;	// ��ͼView
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ylc_custom_route_overlay);
		CharSequence titleLable="�û��Զ���·��";
        setTitle(titleLable);
		//��ʼ����ͼ
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.getController().enableClick(true);
        mMapView.getController().setZoom(13);
        
        /** ��ʾ�Զ���·��ʹ�÷���	
		 *  �ڱ�����ͼ�ϻ�һ����������
		 *  ��֪��ĳ����İٶȾ�γ������������http://api.map.baidu.com/lbsapi/getpoint/index.html	
		 */
		GeoPoint p1 = new GeoPoint((int)(39.9411 * 1E6),(int)(116.3714 * 1E6));
		GeoPoint p2 = new GeoPoint((int)(39.9498 * 1E6),(int)(116.3785 * 1E6));
		GeoPoint p3 = new GeoPoint((int)(39.9436 * 1E6),(int)(116.4029 * 1E6));
		GeoPoint p4 = new GeoPoint((int)(39.9329 * 1E6),(int)(116.4035 * 1E6));
		GeoPoint p5 = new GeoPoint((int)(39.9218 * 1E6),(int)(116.4115 * 1E6));
		GeoPoint p6 = new GeoPoint((int)(39.9144 * 1E6),(int)(116.4230 * 1E6));
		GeoPoint p7 = new GeoPoint((int)(39.9126 * 1E6),(int)(116.4387 * 1E6));
	    //�������
		GeoPoint start = p1;
		//�յ�����
		GeoPoint stop  = p7;
		//��һվ��վ������Ϊp3,����p1,p2
		GeoPoint[] step1 = new GeoPoint[3];
		step1[0] = p1;
		step1[1] = p2 ;
		step1[2] = p3;
		//�ڶ�վ��վ������Ϊp5,����p4
		GeoPoint[] step2 = new GeoPoint[2];
		step2[0] = p4;
		step2[1] = p5;
		//����վ��վ������Ϊp7,����p6
		GeoPoint[] step3 = new GeoPoint[2];
		step3[0] = p6;
		step3[1] = p7;
		//վ�����ݱ�����һ����ά������
		GeoPoint [][] routeData = new GeoPoint[3][];
		routeData[0] = step1;
		routeData[1] = step2;
		routeData[2] = step3;
		//��վ�����ݹ���һ��MKRoute
		MKRoute route = new MKRoute();
		route.customizeRoute(start, stop, routeData);	
		//������վ����Ϣ��MKRoute��ӵ�RouteOverlay��
		RouteOverlay routeOverlay = new RouteOverlay(YLC_CustomRouteOverlay.this, mMapView);		
		routeOverlay.setData(route);
		//���ͼ��ӹ���õ�RouteOverlay
		mMapView.getOverlays().add(routeOverlay);
		//ִ��ˢ��ʹ��Ч
	    mMapView.refresh();
	}
	@Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        mMapView.destroy();
        super.onDestroy();
    }
	
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__custom_route_overlay, menu);
		return true;
	}

}
