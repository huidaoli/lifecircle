package com.weiyoung.yourlifecircle;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class YLC_APPLICATION extends Application {
	private static YLC_APPLICATION instance=null;
	BMapManager bMapManager=null;
	public static final String ip="10.120.11.253:8080";
	public static final String STR_KEY="mTz6OcEZL1vsVubcfUMqCUiM";
	public boolean m_bKeyRight = true;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance=this;
		initBMapManager(this);
	}
	public void initBMapManager(Context context) {
		// TODO Auto-generated method stub
		if(bMapManager==null){
			bMapManager=new BMapManager(context);
		}
		if(!bMapManager.init(STR_KEY, new MyMKGeneralListener())){
			Toast.makeText(YLC_APPLICATION.getInstance().getApplicationContext(), 
                    "BMapManager  ��ʼ������!", Toast.LENGTH_LONG).show();
		}
	}
	public static YLC_APPLICATION getInstance(){
		return instance;
	}
	static class MyMKGeneralListener implements MKGeneralListener{

		@Override
		public void onGetNetworkState(int isError) {
			// TODO Auto-generated method stub
			if(isError==MKEvent.ERROR_NETWORK_CONNECT){
				 Toast.makeText(YLC_APPLICATION.getInstance().getApplicationContext(), "���������������",
		                    Toast.LENGTH_LONG).show();
			}else if (isError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(YLC_APPLICATION.getInstance().getApplicationContext(), "������ȷ�ļ���������",
                        Toast.LENGTH_LONG).show();
            }
		}

		@Override
		public void onGetPermissionState(int isError) {
			// TODO Auto-generated method stub
			//����ֵ��ʾkey��֤δͨ��
            if (isError != 0) {
                //��ȨKey����
                Toast.makeText(YLC_APPLICATION.getInstance().getApplicationContext(), 
                        "���� YLC_APPLICATION.java�ļ�������ȷ����ȨKey,������������������Ƿ�������error: "+isError, Toast.LENGTH_LONG).show();
                YLC_APPLICATION.getInstance().m_bKeyRight = false;
            }
            else{
            	YLC_APPLICATION.getInstance().m_bKeyRight = true;
            	Toast.makeText(YLC_APPLICATION.getInstance().getApplicationContext(), 
                        "key��֤�ɹ�", Toast.LENGTH_LONG).show();
            }
		}
		
	}
}
