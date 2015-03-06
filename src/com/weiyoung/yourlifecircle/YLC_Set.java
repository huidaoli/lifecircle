package com.weiyoung.yourlifecircle;

import java.util.ArrayList;
import java.util.List;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.RequestType;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.UMImage;
import com.weiyoung.variable.LoginInfo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class YLC_Set extends Activity {
	// private Drawable
	// ����������Activity��������³�Ա����
	final UMSocialService mController = UMServiceFactory.getUMSocialService("com.umeng.share",
	                                                                             RequestType.SOCIAL);
	private ListView ylc_setting;
	private List<Drawable> listDrawable;
	private List<String> listString;
	private Drawable orientationDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ylc_set);
		initData();
		ylc_setting.setAdapter(new Ylc_Set_Adapter());
		ylc_setting.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Intent intent=new Intent();
				switch (position) {
				case 0:
					intent.setClass(YLC_Set.this, YLC_Set_System.class);
					startActivity(intent);
					break;
				case 1:
					if(LoginInfo.LOGIN_STATE){
						intent.setClass(YLC_Set.this, YLC_User_Feedback.class);
						startActivity(intent);
					}else{
						Toast.makeText(YLC_Set.this, "���¼�󣬷������", Toast.LENGTH_LONG).show();
					}
					break;
				case 2:
					intent.setClass(YLC_Set.this, YLC_UseInstructions.class);
					startActivity(intent);
					break;
				case 3:
//					intent.setClass(YLC_Set.this, YLC_TellFriends.class);
//					startActivity(intent);
					// �Ƿ�ֻ���ѵ�¼�û����ܴ򿪷���ѡ��ҳ
			        mController.openShare(YLC_Set.this, false);
					break;
				case 4:
					intent.setClass(YLC_Set.this, YLC_AboutUS.class);
					startActivity(intent);
					break;

				}
			}
		});

	}

	class Ylc_Set_Adapter extends BaseAdapter {
		LayoutInflater layoutInflater;

		public Ylc_Set_Adapter() {
			// TODO Auto-generated constructor stub
			layoutInflater = (LayoutInflater) YLC_Set.this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listString.size();
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
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder;
			if (convertView == null) {
				viewHolder = new ViewHolder();
				convertView = layoutInflater.inflate(
						R.layout.activity_ylc_set_item, parent, false);
				viewHolder.image1 = (ImageView) convertView
						.findViewById(R.id.imageView1);
				viewHolder.image2 = (ImageView) convertView
						.findViewById(R.id.imageView2);
				viewHolder.textView1 = (TextView) convertView
						.findViewById(R.id.textView1);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}
			viewHolder.image1.setImageDrawable(listDrawable.get(position));
			viewHolder.image2.setImageDrawable(orientationDrawable);
			viewHolder.textView1.setText(listString.get(position));
			return convertView;
		}

	}

	static class ViewHolder {
		ImageView image1, image2;
		TextView textView1;
	}

	public void initData() {
		// ���÷�������
		mController.setShareContent("����켣�����Բ鿴���ѵ�ʵʱλ�ã�Ҳ���Բ鿴�Լ������ѵ�����켣��http://yun.baidu.com/share/link?shareid=2472312309&uk=3376324121");
		// ���÷���ͼƬ, ����2ΪͼƬ��url��ַ
		mController.setShareMedia(new UMImage(YLC_Set.this, 
		                                      "http://www.umeng.com/images/pic/banner_module_social.png"));
		mController.getConfig().removePlatform( SHARE_MEDIA.RENREN, SHARE_MEDIA.DOUBAN);
		
		ylc_setting = (ListView) findViewById(R.id.ylc_setting);
		listDrawable = new ArrayList<Drawable>();
		listDrawable.add(getResources().getDrawable(R.drawable.ylc_set_power));
		listDrawable.add(getResources()
				.getDrawable(R.drawable.ylc_set_feedback));
		listDrawable.add(getResources().getDrawable(R.drawable.ylc_set_help));
		listDrawable.add(getResources().getDrawable(
				R.drawable.ylc_set_tellfriend));
		listDrawable
				.add(getResources().getDrawable(R.drawable.ylc_set_aboutus));
		String[] ylc_string_setting = getResources().getStringArray(
				R.array.ylc_string_setting);
		listString = new ArrayList<String>();
		for (String str : ylc_string_setting) {
			listString.add(str);
		}
		orientationDrawable = getResources().getDrawable(
				R.drawable.ylc_direction_normal);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ylc__set, menu);
		return true;
	}

}
