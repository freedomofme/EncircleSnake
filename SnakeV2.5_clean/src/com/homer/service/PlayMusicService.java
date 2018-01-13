//package com.homer.service;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.Button;
//
//
///**
// * @author 	sunboy_2050
// * @date		2012-03-16 
// * @blog		http://blog.csdn.net/sunboy_2050/
// */
//
//public class PlayMusicService extends Activity implements OnClickListener {
//
//	private Button playBtn;
//	private Button stopBtn;
//	private Button pauseBtn;
//	private Button exitBtn;
//	private Button closeBtn;
//
//	private Intent intent;
//	
//	@Override
//	public void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.music_service);
//
//		playBtn = (Button) findViewById(R.id.play);
//		stopBtn = (Button) findViewById(R.id.stop);
//		pauseBtn = (Button) findViewById(R.id.pause);
//		exitBtn = (Button) findViewById(R.id.exit);
//		closeBtn = (Button) findViewById(R.id.close);
//		
//		playBtn.setOnClickListener(this);
//		stopBtn.setOnClickListener(this);
//		pauseBtn.setOnClickListener(this);
//		exitBtn.setOnClickListener(this);
//		closeBtn.setOnClickListener(this);
//
//	}
//
//	@Override
//	public void onClick(View v) {
//		int op = -1;
////		intent = new Intent("com.homer.service.musicService");
//		intent = new Intent(this, MusicService.class);
//
//		switch (v.getId()) {
//		case R.id.play:								// play music
//			op = 1;
//			break;
//		case R.id.stop:								// stop music
//			op = 2;
//			break;
//		case R.id.pause:							// pause music
//			op = 3;
//			break;
//		case R.id.close:							// close activity
//			this.finish();
//			break;
//		case R.id.exit:								// stopService
//			op = 4;
//			stopService(intent);
//			this.finish();
//			break;
//		}
//
//		Bundle bundle = new Bundle();
//		bundle.putInt("op", op);
//		intent.putExtras(bundle);
//		
//		startService(intent);							// startService
//	}
//	
//	@Override
//	public void onDestroy(){
//		super.onDestroy();
//
//		if(intent != null){
//			stopService(intent);
//		}
//	}
//}