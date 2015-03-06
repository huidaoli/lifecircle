package com.weiyoung.yourlifecircle;

import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKRoute;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.weiyoung.utils.YLC_BDMap_Utils;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class YLC_BusLine extends Activity {
	//UI���
	Button mBtnSearch = null;
	Button mBtnNextLine = null;
	//���·�߽ڵ����
	Button mBtnPre = null;//��һ���ڵ�
	Button mBtnNext = null;//��һ���ڵ�
	int nodeIndex = -2;//�ڵ�����,������ڵ�ʱʹ��
	MKRoute route = null;//����ݳ�/����·�����ݵı�����������ڵ�ʱʹ��
	private PopupOverlay   pop  = null;//��������ͼ�㣬����ڵ�ʱʹ��
	private TextView  popupText = null;//����view
	private View viewCache = null;
	private List<String> busLineIDList = null;
	int busLineIndex = 0;
	
	//��ͼ��أ�ʹ�ü̳�MapView��MyBusLineMapViewĿ������дtouch�¼�ʵ�����ݴ���
	//���������touch�¼���������̳У�ֱ��ʹ��MapView����
    MapView mMapView = null;	// ��ͼView	
	//�������
	MKSearch mSearch = null;	// ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		YLC_APPLICATION app=(YLC_APPLICATION) this.getApplication();
		setContentView(R.layout.activity_ylc__bus_line);
		 //��ͼ��ʼ��
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.getController().enableClick(true);
        mMapView.getController().setZoom(12);
        busLineIDList = new ArrayList<String>();
      //���� ��������ͼ��
        createPaopao();
      //��ͼ����¼�����
        mMapView.regMapTouchListner(new MKMapTouchListener(){

			@Override
			public void onMapClick(GeoPoint point) {
			  //�ڴ˴����ͼ����¼� 
			  //����pop
			  if ( pop != null ){
				  pop.hidePop();
			  }
			}

			@Override
			public void onMapDoubleClick(GeoPoint point) {
				
			}

			@Override
			public void onMapLongClick(GeoPoint point) {
				
			}
        	
        });
        // ��ʼ������ģ�飬ע���¼�����
        mSearch = new MKSearch();
        mSearch.init(app.bMapManager, new MKSearchListener(){

            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
            }
            
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// ����ſɲο�MKEvent�еĶ���
				if (error != 0 || res == null) {
					Toast.makeText(YLC_BusLine.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
					return;
		        }
				
				// �ҵ�����·��poi node
                MKPoiInfo curPoi = null;
                int totalPoiNum  = res.getCurrentNumPois();
                //��������poi���ҵ�����Ϊ������·��poi
                busLineIDList.clear();
				for( int idx = 0; idx < totalPoiNum; idx++ ) {
                    if ( 2 == res.getPoi(idx).ePoiType ) {
                        // poi���ͣ�0����ͨ�㣬1������վ��2��������·��3������վ��4��������·
                        curPoi = res.getPoi(idx);
                        //ʹ��poi��uid���𹫽��������
                        busLineIDList.add(curPoi.uid);
                        System.out.println(curPoi.uid);
                        
                    }
				}
				SearchNextBusline();
				
				// û���ҵ�������Ϣ
                if (curPoi == null) {
                    Toast.makeText(YLC_BusLine.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
                    return;
                }
				route = null;
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
			/**
			 * ��ȡ����·�߽����չʾ������·
			 */
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
				if (iError != 0 || result == null) {
					Toast.makeText(YLC_BusLine.this, "��Ǹ��δ�ҵ����", Toast.LENGTH_LONG).show();
					return;
		        }

				RouteOverlay routeOverlay = new RouteOverlay(YLC_BusLine.this, mMapView);
			    // �˴���չʾһ��������Ϊʾ��
			    routeOverlay.setData(result.getBusRoute());
			    //�������ͼ��
			    mMapView.getOverlays().clear();
			    //���·��ͼ��
			    mMapView.getOverlays().add(routeOverlay);
			    //ˢ�µ�ͼʹ��Ч
			    mMapView.refresh();
			    //�ƶ���ͼ�����
			    mMapView.getController().animateTo(result.getBusRoute().getStart());
			  //��·�����ݱ����ȫ�ֱ���
			    route = result.getBusRoute();
			    //����·�߽ڵ��������ڵ����ʱʹ��
			    nodeIndex = -1;
			    mBtnPre.setVisibility(View.VISIBLE);
				mBtnNext.setVisibility(View.VISIBLE);
				Toast.makeText(YLC_BusLine.this, 
						       result.getBusName(), 
						       Toast.LENGTH_SHORT).show();
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
     // �趨������ť����Ӧ
        mBtnSearch = (Button)findViewById(R.id.search);
        mBtnNextLine = (Button)findViewById(R.id.nextline);
        mBtnPre = (Button)findViewById(R.id.pre);
        mBtnNext = (Button)findViewById(R.id.next);
        mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		OnClickListener clickListener = new OnClickListener(){
			public void onClick(View v) {
				//��������
				SearchButtonProcess(v);
			}
        };
        OnClickListener nextLineClickListener = new OnClickListener(){
			public void onClick(View v) {
				//������һ��������
				SearchNextBusline();
			}
        };
        OnClickListener nodeClickListener = new OnClickListener(){
			public void onClick(View v) {
				//���·�߽ڵ�
				nodeClick(v);
			}
        };
        mBtnSearch.setOnClickListener(clickListener); 
        mBtnNextLine.setOnClickListener(nextLineClickListener);
        mBtnPre.setOnClickListener(nodeClickListener);
        mBtnNext.setOnClickListener(nodeClickListener);
	}
	/**
	 * �ڵ����ʾ��
	 * @param v
	 */
	public void nodeClick(View v){
	
		if (nodeIndex < -1 || route == null || nodeIndex >= route.getNumSteps())
			return;
		viewCache = getLayoutInflater().inflate(R.layout.ylc_custom_text_view, null);
        popupText =(TextView) viewCache.findViewById(R.id.textcache);
		//��һ���ڵ�
		if (mBtnPre.equals(v) && nodeIndex > 0){
			//������
			nodeIndex--;
			//�ƶ���ָ������������
			mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
			//��������
			popupText.setText(route.getStep(nodeIndex).getContent());
			popupText.setBackgroundResource(R.drawable.popup);
			pop.showPopup(YLC_BDMap_Utils.getBitmapFromView(popupText),
					route.getStep(nodeIndex).getPoint(),
					5);	
		}
		//��һ���ڵ�
		if (mBtnNext.equals(v) && nodeIndex < (route.getNumSteps()-1)){
			//������
			nodeIndex++;
			//�ƶ���ָ������������
			mMapView.getController().animateTo(route.getStep(nodeIndex).getPoint());
			//��������
			popupText.setText(route.getStep(nodeIndex).getContent());
			popupText.setBackgroundDrawable(getResources().getDrawable(R.drawable.popup));
			pop.showPopup(YLC_BDMap_Utils.getBitmapFromView(popupText),
					route.getStep(nodeIndex).getPoint(),
					5);	
		}
	}
	/**
	 * �������
	 * @param v
	 */
	void SearchButtonProcess(View v) {
		busLineIDList.clear();
		busLineIndex = 0;
		mBtnPre.setVisibility(View.INVISIBLE);
		mBtnNext.setVisibility(View.INVISIBLE);
		if (mBtnSearch.equals(v)) {
			EditText editCity = (EditText)findViewById(R.id.city);
			EditText editSearchKey = (EditText)findViewById(R.id.searchkey);
			//����poi�������ӵõ�����poi���ҵ�������·���͵�poi����ʹ�ø�poi��uid���й�����������
			mSearch.poiSearchInCity(editCity.getText().toString(), editSearchKey.getText().toString());
		}
		
	}
	void SearchNextBusline(){
		 if ( busLineIndex >= busLineIDList.size()){
			 busLineIndex =0;
		 }
		 if ( busLineIndex >=0 && busLineIndex < busLineIDList.size() && busLineIDList.size() >0){
			 mSearch.busLineSearch(((EditText)findViewById(R.id.city)).getText().toString(), busLineIDList.get(busLineIndex));
			 busLineIndex ++;
		 }
		 
	}
	/**
	 * ������������ͼ��
	 */
	public void createPaopao(){
		viewCache = getLayoutInflater().inflate(R.layout.ylc_custom_text_view, null);
        popupText =(TextView) viewCache.findViewById(R.id.textcache);
        //���ݵ����Ӧ�ص�
        PopupClickListener popListener = new PopupClickListener(){
			@Override
			public void onClickedPopup(int index) {
				Log.v("click", "clickapoapo");
			}
        };
        pop = new PopupOverlay(mMapView,popListener);
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
		getMenuInflater().inflate(R.menu.ylc__bus_line, menu);
		return true;
	}

}
