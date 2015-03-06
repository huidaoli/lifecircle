package com.weiyoung.yourlifecircle;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.Symbol.Stroke;
import com.baidu.mapapi.map.TextItem;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class YLC_YourLifeCircle extends Activity {
	//��ͼ���
	MapView mMapView = null;
	
	//UI���
	Button resetBtn = null;
	Button clearBtn = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ylc_your_life_circle);
		 //��ʼ����ͼ
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapView.getController().setZoom(12.5f);
        mMapView.getController().enableClick(true);
        
        //UI��ʼ��
        clearBtn = (Button)findViewById(R.id.button1);
        resetBtn = (Button)findViewById(R.id.button2);
        
        OnClickListener clearListener = new OnClickListener(){
            public void onClick(View v){
                clearClick();
            }
        };
        OnClickListener restListener = new OnClickListener(){
            public void onClick(View v){
                resetClick();
            }
        };
        
       clearBtn.setOnClickListener(clearListener);
       resetBtn.setOnClickListener(restListener);
       
       //�������ʱ��ӻ���ͼ��
       addCustomElementsDemo();
	}
	 public void resetClick(){
	    	//��ӻ���Ԫ��
	    	addCustomElementsDemo();
	    }
	   
	    public void clearClick(){
	    	//�������ͼ��
	    	mMapView.getOverlays().clear();
	    }
	    /**
	     * ��ӵ㡢�ߡ�����Ρ�Բ������
	     */
	    public void addCustomElementsDemo(){
	    	GraphicsOverlay graphicsOverlay = new GraphicsOverlay(mMapView);
	        mMapView.getOverlays().add(graphicsOverlay);
	    	//��ӵ�
	        graphicsOverlay.setData(drawPoint());
	    	//�������
	        graphicsOverlay.setData(drawLine());
	    	//��Ӷ����
	        graphicsOverlay.setData(drawPolygon());
	    	//���Բ
	        graphicsOverlay.setData(drawCircle());
	    	//��������
	        TextOverlay textOverlay = new TextOverlay(mMapView);
	        mMapView.getOverlays().add(textOverlay);
	        textOverlay.addText(drawText());
	        //ִ�е�ͼˢ��ʹ��Ч
	        mMapView.refresh();
	    }
	    /**
	     * �������ߣ�������״̬���ͼ״̬�仯
	     * @return ���߶���
	     */
	    public Graphic drawLine(){
	    	double mLat = 39.97923;
	       	double mLon = 116.357428;
	       	
	    	int lat = (int) (mLat*1E6);
		   	int lon = (int) (mLon*1E6);   	
		   	GeoPoint pt1 = new GeoPoint(lat, lon);
		   
		   	mLat = 39.94923;
	       	mLon = 116.397428;
	    	lat = (int) (mLat*1E6);
		   	lon = (int) (mLon*1E6);
		   	GeoPoint pt2 = new GeoPoint(lat, lon);
		   	mLat = 39.97923;
	       	mLon = 116.437428;
			lat = (int) (mLat*1E6);
		   	lon = (int) (mLon*1E6);
		    GeoPoint pt3 = new GeoPoint(lat, lon);
		  
		    //������
	  		Geometry lineGeometry = new Geometry();
	  		//�趨���ߵ�����
	  		GeoPoint[] linePoints = new GeoPoint[3];
	  		linePoints[0] = pt1;
	  		linePoints[1] = pt2;
	  		linePoints[2] = pt3; 
	  		lineGeometry.setPolyLine(linePoints);
	  		//�趨��ʽ
	  		Symbol lineSymbol = new Symbol();
	  		Symbol.Color lineColor = lineSymbol.new Color();
	  		lineColor.red = 255;
	  		lineColor.green = 0;
	  		lineColor.blue = 0;
	  		lineColor.alpha = 255;
	  		lineSymbol.setLineSymbol(lineColor, 10);
	  		//����Graphic����
	  		Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
	  		return lineGraphic;
	    }
	    /**
	     * ���ƶ���Σ��ö�������ͼ״̬�仯
	     * @return ����ζ���
	     */
	     public Graphic drawPolygon(){
	     	double mLat = 39.93923;
	        	double mLon = 116.357428;
	     	int lat = (int) (mLat*1E6);
	 	   	int lon = (int) (mLon*1E6);   	
	 	   	GeoPoint pt1 = new GeoPoint(lat, lon);
	 	   	mLat = 39.91923;
	        	mLon = 116.327428;
	 		lat = (int) (mLat*1E6);
	 	   	lon = (int) (mLon*1E6);
	 	    GeoPoint pt2 = new GeoPoint(lat, lon);
	 	    mLat = 39.89923;
	        	mLon = 116.347428;
	 		lat = (int) (mLat*1E6);
	 	   	lon = (int) (mLon*1E6);
	 	    GeoPoint pt3 = new GeoPoint(lat, lon);
	 	    mLat = 39.89923;
	        	mLon = 116.367428;
	 		lat = (int) (mLat*1E6);
	 	   	lon = (int) (mLon*1E6);
	 	    GeoPoint pt4 = new GeoPoint(lat, lon);
	 	    mLat = 39.91923;
	        	mLon = 116.387428;
	 		lat = (int) (mLat*1E6);
	 	   	lon = (int) (mLon*1E6);
	 	    GeoPoint pt5 = new GeoPoint(lat, lon);
	 	    
	 	    //���������
	   		Geometry polygonGeometry = new Geometry();
	   		//���ö��������
	   		GeoPoint[] polygonPoints = new GeoPoint[5];
	   		polygonPoints[0] = pt1;
	   		polygonPoints[1] = pt2;
	   		polygonPoints[2] = pt3; 
	   		polygonPoints[3] = pt4; 
	   		polygonPoints[4] = pt5; 
	   		polygonGeometry.setPolygon(polygonPoints);
	   		//���ö������ʽ
	   		Symbol polygonSymbol = new Symbol();
	  		Symbol.Color polygonColor = polygonSymbol.new Color();
	  		polygonColor.red = 0;
	  		polygonColor.green = 0;
	  		polygonColor.blue = 255;
	  		polygonColor.alpha = 126;
	  		polygonSymbol.setSurface(polygonColor,1,5);
	   		//����Graphic����
	   		Graphic polygonGraphic = new Graphic(polygonGeometry, polygonSymbol);
	   		return polygonGraphic;
	     }
	    /**
	     * �������֣����������ͼ�仯��͸��Ч��
	     * @return ���ֶ���
	     */
	    public TextItem drawText(){
	       	double mLat = 39.86923;
	       	double mLon = 116.397428;
	    	int lat = (int) (mLat*1E6);
		   	int lon = (int) (mLon*1E6);   	
		   	//��������
		   	TextItem item = new TextItem();
	    	//��������λ��
	    	item.pt = new GeoPoint(lat,lon);
	    	//�����ļ�����
	    	item.text = "�ٶȵ�ͼSDK";
	    	//�����ִ�С
	    	item.fontSize = 40;
	    	Symbol symbol = new Symbol();
	    	Symbol.Color bgColor = symbol.new Color();
	    	//�������ֱ���ɫ
	    	bgColor.red = 0;
	    	bgColor.blue = 0;
	    	bgColor.green = 255;
	    	bgColor.alpha = 50;
	    	
	    	Symbol.Color fontColor = symbol.new Color();
	    	//����������ɫ
	    	fontColor.alpha = 255;
	    	fontColor.red = 0;
	    	fontColor.green = 0;
	    	fontColor.blue  = 255;
	    	//���ö��뷽ʽ
	    	item.align = TextItem.ALIGN_CENTER;
	    	//����������ɫ�ͱ�����ɫ
	    	item.fontColor = fontColor;
	    	item.bgColor  = bgColor ; 
	    	return item;
	    }
	    /**
	     * ����Բ����Բ���ͼ״̬�仯
	     * @return Բ����
	     */
	    public Graphic drawCircle() {
	    	double mLat = 39.90923; 
	       	double mLon = 116.447428; 
	    	int lat = (int) (mLat*1E6);
		   	int lon = (int) (mLon*1E6);   	
		   	GeoPoint pt1 = new GeoPoint(lat, lon);
		   	
		   	//����Բ
	  		Geometry circleGeometry = new Geometry();
	  	
	  		//����Բ���ĵ�����Ͱ뾶
	  		circleGeometry.setCircle(pt1, 2500);
	  		//������ʽ
	  		Symbol circleSymbol = new Symbol();
	 		Symbol.Color circleColor = circleSymbol.new Color();
	 		circleColor.red = 0;
	 		circleColor.green = 255;
	 		circleColor.blue = 0;
	 		circleColor.alpha = 126;
	  		circleSymbol.setSurface(circleColor,1,3, new Stroke(3, circleSymbol.new Color(0xFFFF0000)));
	  		//����Graphic����
	  		Graphic circleGraphic = new Graphic(circleGeometry, circleSymbol);
	  		return circleGraphic;
	   }
	    /**
	     * ���Ƶ��㣬�õ�״̬�����ͼ״̬�仯���仯
	     * @return �����
	     */
	    public Graphic drawPoint(){
	       	double mLat = 39.98923;
	       	double mLon = 116.397428;
	    	int lat = (int) (mLat*1E6);
		   	int lon = (int) (mLon*1E6);   	
		   	GeoPoint pt1 = new GeoPoint(lat, lon);
		   	
		   	//������
	  		Geometry pointGeometry = new Geometry();
	  		//��������
	  		pointGeometry.setPoint(pt1, 10);
	  		//�趨��ʽ
	  		Symbol pointSymbol = new Symbol();
	 		Symbol.Color pointColor = pointSymbol.new Color();
	 		pointColor.red = 0;
	 		pointColor.green = 126;
	 		pointColor.blue = 255;
	 		pointColor.alpha = 255;
	 		pointSymbol.setPointSymbol(pointColor);
	  		//����Graphic����
	  		Graphic pointGraphic = new Graphic(pointGeometry, pointSymbol);
	  		return pointGraphic;
	    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__your_life_circle, menu);
		return true;
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

}
