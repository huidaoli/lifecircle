package com.weiyoung.yourlifecircle;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionInfo;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.weiyoung.yourlifecircle.YLC_APPLICATION.MyMKGeneralListener;

import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

public class YLC_POI_Search extends Activity {
	private MapView myMapView = null;
	private MKSearch mySearch = null;// ����ģ�飬Ҳ����ȥ����ͼ��������
	/*
	 * �����ؼ������봰��
	 */
	private AutoCompleteTextView keyWorldView = null;
	private ArrayAdapter<String> sugAdapter = null;
	private int load_Index;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		YLC_APPLICATION app = (YLC_APPLICATION) this.getApplication();
		if (app.bMapManager == null) {
			app.bMapManager = new BMapManager(this);
			app.bMapManager.init(YLC_APPLICATION.STR_KEY,
					new YLC_APPLICATION.MyMKGeneralListener());
		}
		setContentView(R.layout.activity_ylc__poi__search);
		myMapView = (MapView) findViewById(R.id.bmapView_poi);
		myMapView.getController().enableClick(true);
		myMapView.getController().setZoom(12);
		// ��ʼ������ģ�飬ע������¼�
		mySearch = new MKSearch();
		mySearch.init(app.bMapManager, new MKSearchListener() {

			// �ڴ˴�������ҳ���
			@Override
			public void onGetPoiDetailSearchResult(int type, int error) {
				if (error != 0) {
					Toast.makeText(YLC_POI_Search.this, "��Ǹ��δ�ҵ����",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(YLC_POI_Search.this, "�ɹ����鿴����ҳ��",
							Toast.LENGTH_SHORT).show();
				}
			}

			/**
			 * �ڴ˴���poi�������
			 */
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// ����ſɲο�MKEvent�еĶ���
				if (error != 0 || res == null) {
					Toast.makeText(YLC_POI_Search.this, "��Ǹ��δ�ҵ����",
							Toast.LENGTH_LONG).show();
					return;
				}
				// ����ͼ�ƶ�����һ��POI���ĵ�
				if (res.getCurrentNumPois() > 0) {
					// ��poi�����ʾ����ͼ��
					YLC_PoiOverlay poiOverlay = new YLC_PoiOverlay(
							YLC_POI_Search.this, myMapView, mySearch);
					poiOverlay.setData(res.getAllPoi());
					myMapView.getOverlays().clear();
					myMapView.getOverlays().add(poiOverlay);
					myMapView.refresh();
					// ��ePoiTypeΪ2��������·����4��������·��ʱ�� poi����Ϊ��
					for (MKPoiInfo info : res.getAllPoi()) {
						if (info.pt != null) {
							myMapView.getController().animateTo(info.pt);
							break;
						}
					}
				} else if (res.getCityListNum() > 0) {
					// ������ؼ����ڱ���û���ҵ����������������ҵ�ʱ�����ذ����ùؼ�����Ϣ�ĳ����б�
					String strInfo = "��";
					for (int i = 0; i < res.getCityListNum(); i++) {
						strInfo += res.getCityListInfo(i).city;
						strInfo += ",";
					}
					strInfo += "�ҵ����";
					Toast.makeText(YLC_POI_Search.this, strInfo,
							Toast.LENGTH_LONG).show();
				}
			}

			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
			}

			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
			}

			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
			}

			public void onGetAddrResult(MKAddrInfo res, int error) {
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			/**
			 * ���½����б�
			 */
			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				if (res == null || res.getAllSuggestions() == null) {
					return;
				}
				sugAdapter.clear();
				for (MKSuggestionInfo info : res.getAllSuggestions()) {
					if (info.key != null)
						sugAdapter.add(info.key);
				}
				sugAdapter.notifyDataSetChanged();

			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type,
					int error) {
				// TODO Auto-generated method stub

			}
		});
		keyWorldView = (AutoCompleteTextView) findViewById(R.id.searchkey);
		sugAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_dropdown_item_1line);
		keyWorldView.setAdapter(sugAdapter);

		/**
		 * ������ؼ��ֱ仯ʱ����̬���½����б�
		 */
		keyWorldView.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void afterTextChanged(Editable arg0) {
				
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				
			}
			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				 if ( cs.length() <=0 ){
					 return ;
				 }
				 String city =  ((EditText)findViewById(R.id.city)).getText().toString();
				 /**
				  * ʹ�ý������������ȡ�����б������onSuggestionResult()�и���
				  */
                 mySearch.suggestionSearch(cs.toString(), city);				
			}
		});
	}
	 @Override
	    protected void onPause() {
		 myMapView.onPause();
	        super.onPause();
	    }
	    
	    @Override
	    protected void onResume() {
	    	myMapView.onResume();
	        super.onResume();
	    }
	    
	    @Override
	    protected void onDestroy(){
	    	myMapView.destroy();
	    	mySearch.destory();
	    	super.onDestroy();
	    }
	    
	    @Override
	    protected void onSaveInstanceState(Bundle outState) {
	    	super.onSaveInstanceState(outState);
	    	myMapView.onSaveInstanceState(outState);
	    	
	    }
	    
	    @Override
	    protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    	super.onRestoreInstanceState(savedInstanceState);
	    	myMapView.onRestoreInstanceState(savedInstanceState);
	    }
	    
	    private void initMapView() {
	    	myMapView.setLongClickable(true);
	    	myMapView.getController().setZoom(14);
	    	myMapView.getController().enableClick(true);
	    	myMapView.setBuiltInZoomControls(true);
	    }
	    /**
	     * Ӱ��������ť����¼�
	     * @param v
	     */
	    public void searchButtonProcess(View v) {
	          EditText editCity = (EditText)findViewById(R.id.city);
	          EditText editSearchKey = (EditText)findViewById(R.id.searchkey);
	          mySearch.poiSearchInCity(editCity.getText().toString(), 
	                  editSearchKey.getText().toString());
	    }
	   public void goToNextPage(View v) {
	        //������һ��poi
	        int flag = mySearch.goToPoiPage(++load_Index);
	        if (flag != 0) {
	            Toast.makeText(YLC_POI_Search.this, "��������ʼ��Ȼ����������һ������", Toast.LENGTH_SHORT).show();
	        }
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__poi__search, menu);
		return true;
	}
}
