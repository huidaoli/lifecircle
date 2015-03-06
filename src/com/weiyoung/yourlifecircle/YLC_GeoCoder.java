package com.weiyoung.yourlifecircle;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.os.Bundle;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class YLC_GeoCoder extends Activity {
	//UI���
	Button mBtnReverseGeoCode = null;	// �����귴����Ϊ��ַ
	Button mBtnGeoCode = null;	// ����ַ����Ϊ����
		
	//��ͼ���
	MapView mMapView = null;	// ��ͼView
	//�������
	MKSearch mSearch = null;	// ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		YLC_APPLICATION app=(YLC_APPLICATION) this.getApplication();
		setContentView(R.layout.activity_ylc_geo_coder);
		//��ͼ��ʼ��
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.getController().enableClick(true);
        mMapView.getController().setZoom(12);
     // ��ʼ������ģ�飬ע���¼�����
        mSearch = new MKSearch();
        mSearch.init(app.bMapManager, new MKSearchListener() {
            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
            }
            
			public void onGetAddrResult(MKAddrInfo res, int error) {
				if (error != 0) {
					String str = String.format("����ţ�%d", error);
					Toast.makeText(YLC_GeoCoder.this, str, Toast.LENGTH_LONG).show();
					return;
				}
				//��ͼ�ƶ����õ�
				mMapView.getController().animateTo(res.geoPt);	
				if (res.type == MKAddrInfo.MK_GEOCODE){
					//������룺ͨ����ַ���������
					String strInfo = String.format("γ�ȣ�%f ���ȣ�%f", res.geoPt.getLatitudeE6()/1e6, res.geoPt.getLongitudeE6()/1e6);
					Toast.makeText(YLC_GeoCoder.this, strInfo, Toast.LENGTH_LONG).show();
				}
				if (res.type == MKAddrInfo.MK_REVERSEGEOCODE){
					//��������룺ͨ������������ϸ��ַ���ܱ�poi
					String strInfo = res.strAddr;
					Toast.makeText(YLC_GeoCoder.this, strInfo, Toast.LENGTH_LONG).show();
					
				}
				//����ItemizedOverlayͼ��������ע�����
				ItemizedOverlay<OverlayItem> itemOverlay = new ItemizedOverlay<OverlayItem>(null, mMapView);
				//����Item
				OverlayItem item = new OverlayItem(res.geoPt, "", null);
				//�õ���Ҫ���ڵ�ͼ�ϵ���Դ
				Drawable marker = getResources().getDrawable(R.drawable.icon_markf);  
				//Ϊmaker����λ�úͱ߽�
				marker.setBounds(0, 0, marker.getIntrinsicWidth(), marker.getIntrinsicHeight());
				//��item����marker
				item.setMarker(marker);
				//��ͼ�������item
				itemOverlay.addItem(item);
				
				//�����ͼ����ͼ��
				mMapView.getOverlays().clear();
				//���һ����עItemizedOverlayͼ��
				mMapView.getOverlays().add(itemOverlay);
				//ִ��ˢ��ʹ��Ч
				mMapView.refresh();
			}
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				
			}
			public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
			}
			public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
			}
			public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
			}
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}
			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub
				
			}

        });
        
        // �趨������뼰��������밴ť����Ӧ
        mBtnReverseGeoCode = (Button)findViewById(R.id.reversegeocode);
        mBtnGeoCode = (Button)findViewById(R.id.geocode);
        
        OnClickListener clickListener = new OnClickListener(){
			public void onClick(View v) {
					SearchButtonProcess(v);
			}
        };
        
        mBtnReverseGeoCode.setOnClickListener(clickListener); 
        mBtnGeoCode.setOnClickListener(clickListener); 
	}
	/**
	 * ��������
	 * @param v
	 */
	void SearchButtonProcess(View v) {
		if (mBtnReverseGeoCode.equals(v)) {
			EditText lat = (EditText)findViewById(R.id.lat);
			EditText lon = (EditText)findViewById(R.id.lon);
			GeoPoint ptCenter = new GeoPoint((int)(Float.valueOf(lat.getText().toString())*1e6), (int)(Float.valueOf(lon.getText().toString())*1e6));
			//��Geo����
			mSearch.reverseGeocode(ptCenter);
		} else if (mBtnGeoCode.equals(v)) {
			EditText editCity = (EditText)findViewById(R.id.city);
			EditText editGeoCodeKey = (EditText)findViewById(R.id.geocodekey);
			//Geo����
			mSearch.geocode(editGeoCodeKey.getText().toString(), editCity.getText().toString());
		}
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
        mSearch.destory();
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
		getMenuInflater().inflate(R.menu.ylc__geo_coder, menu);
		return true;
	}

}
