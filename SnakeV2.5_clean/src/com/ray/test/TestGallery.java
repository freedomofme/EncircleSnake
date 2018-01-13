package com.ray.test;

import java.net.ContentHandler;
import net.testin.android.AdManager;
import net.testin.android.onlineconfig.OnlineConfigCallBack;
import net.testin.android.st.SpotManager;

import java.security.PublicKey;

import android.app.Activity;
import android.basic.lesson48.MainActivity;
import android.basic.snaketestxiong.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Gallery;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TestGallery extends Activity {
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		SpotManager.getInstance(context).onStop();
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		SpotManager.getInstance(context).onDestroy();
		super.onDestroy();
	}
	public static int w;
	public static int chooseNumber = 5;
	public static boolean isAdOn = false;
	TextView resulTextView;
	TextView scoreTextView;
	TextView bestTextView;
	Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main2);
        context = this;
        Gallery g = (Gallery) findViewById(R.id.Gallery01);//get Gallery component
        g.setAdapter(new ImageAdapter(this));//set image resource for gallery
    	DisplayMetrics mDisplayMetrics = new DisplayMetrics();
  
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		w = mDisplayMetrics.widthPixels;
		resulTextView = (TextView)findViewById(R.id.result2);
		scoreTextView = (TextView)findViewById(R.id.score2);
		bestTextView = (TextView)findViewById(R.id.best2);
       
		AdManager.getInstance(context).init("8b8d47d69adb7ed8", "4ebd8c7bf829dbb6", false);
		AdManager.getInstance(context).setUserDataCollect(true);
		SpotManager.getInstance(context).loadSpotAds();
		// 方法二： 异步调用（可在任意线程中调用）
		AdManager.getInstance(context).asyncGetOnlineConfig("isAdOn", new OnlineConfigCallBack() {
		    @Override
		    public void onGetOnlineConfigSuccessful(String key, String value) {
		        // TODO Auto-generated method stub
		        // 获取在线参数成功
		    	if(value.equals("1"))
		    		isAdOn = true;
		    	else if(value.equals("0"))
		    		isAdOn = false;
		    }

			@Override
			public void onGetOnlineConfigFailed(String arg0) {
				// TODO Auto-generated method stub
				isAdOn = false;
			}
		});
		
		// 插屏出现动画效果，0:ANIM_NONE为无动画，1:ANIM_SIMPLE为简单动画效果，2:ANIM_ADVANCE为高级动画效果
				SpotManager.getInstance(context).setAnimationType(
						SpotManager.ANIM_ADVANCE);
				SpotManager.getInstance(context).setSpotOrientation(
			            SpotManager.ORIENTATION_PORTRAIT);
		
        //add listener
        g.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView parent, View v, int position, long id) {
            	//just a test,u can start a game activity
            	chooseNumber = position;
//                Toast.makeText(TestGallery.this, "" + position, Toast.LENGTH_SHORT).show();
                Intent i = new Intent();
                i.setClass(TestGallery.this, MainActivity.class);
                startActivity(i);
            }
        });
        
       g.setOnItemSelectedListener(new OnItemSelectedListener() {
       
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			SharedPreferences sharedPreferences = getSharedPreferences("snake", Context.MODE_PRIVATE);
			//getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
			int score = sharedPreferences.getInt("score"+position, 0);
			int best = sharedPreferences.getInt("best"+position, 0);
			
			boolean flag  = false;
			if(best != 0 && score != 0) {
//				Log.i("AAA","4");
				if(score < best) {
//					Log.i("AAA","5");
					best = score;
					flag = true;
				}
			}
			if(best == 0) {
//				Log.i("AAA","6");
				best = score;
				if(score != 0)
				flag = true;
//				Log.i("AAA","best"+ best);
			}
			
			if(best == 0&& score == 0) {
				resulTextView.setText("未战胜！");
//				Log.i("AAA","1");
			}
			//第一次
			if(best == 0 && score != 0 ) {
				resulTextView.setText("第一次获胜！");
//				Log.i("AAA","2");
			}
			//第n次胜利
			if(best != 0) {
				resulTextView.setText("已战胜！");
//				Log.i("AAA","3");
			}
			
			scoreTextView.setText(score+""); 
			if(flag) {
				bestTextView.setText(best+"(新纪录)");
			}else {
				bestTextView.setText(best+"");
			}
			Editor editor = sharedPreferences.edit();//获取编辑器
			editor.putInt("best"+position, best);
			editor.commit();//提交修改  
			
//			 Toast.makeText(TestGallery.this, "" + position, Toast.LENGTH_SHORT).show();
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	}); 
    }
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// TODO Auto-generated method stub
		SharedPreferences sharedPreferences = getSharedPreferences("snake", Context.MODE_PRIVATE);
		//getString()第二个参数为缺省值，如果preference中不存在该key，将返回缺省值
		int score = sharedPreferences.getInt("score"+TestGallery.chooseNumber, 0);
		int best = sharedPreferences.getInt("best"+TestGallery.chooseNumber, 0);
		boolean flag  = false;
		if(best != 0 && score != 0) {
			Log.i("AAA","4");
			if(score < best) {
				
				Log.i("AAA","5");
				best = score;
				flag = true;
			}
		}
		if(best == 0) {
			Log.i("AAA","6");
			best = score;
			if(score != 0)
			flag = true;
			Log.i("AAA","best"+ best);
		}
		
		if(best == 0&& score == 0) {
			resulTextView.setText("未战胜！");
			Log.i("AAA","1");
		}

		//第n次胜利
		if(best != 0) {
			resulTextView.setText("已战胜！");
			Log.i("AAA","3");
		}
		
		scoreTextView.setText(score+""); 
		if(flag) {
			bestTextView.setText(best+"(新纪录)");
		}else {
			bestTextView.setText(best+"");
		}
		Editor editor = sharedPreferences.edit();//获取编辑器
		editor.putInt("best"+TestGallery.chooseNumber, best);
		editor.commit();//提交修改  
		
	}
    
}