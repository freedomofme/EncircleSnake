package android.basic.lesson48;



import com.homer.service.MusicService;
import com.xiegeixiong.Gesturete.BuileGestureExt;

import android.app.Activity;
import android.basic.snaketestxiong.R;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	public static int screenWidth;
	public static int screenHeight;
	private SnakeView mSnakeView ;
	private static String ICICLE_KEY = "snake-view";
	private GestureDetector gestureDetector;
	public static boolean notGoBack = false;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
		screenWidth = mDisplayMetrics.widthPixels;
		screenHeight = mDisplayMetrics.heightPixels;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mSnakeView = (SnakeView) findViewById(R.id.snake);
		TextView tv = (TextView) findViewById(R.id.text);

		mSnakeView.setStatusTextView(tv);

		// 检查存贮状态以确定是重新开始还是恢复状态
		if (savedInstanceState == null) {
			// 存储状态为空，说明刚启动可以切换到准备状态
			mSnakeView.setMode(SnakeView.READY);
		} else {
			// 已经保存过，那么就去恢复原有状态
			Bundle bundle = savedInstanceState.getBundle(ICICLE_KEY);
			if (bundle != null) {
				// 恢复状态
				mSnakeView.restoreState(bundle);
			} else {
				// 设置状态为暂停
				mSnakeView.setMode(SnakeView.PAUSE);
			}
		}
		gestureDetector = new BuileGestureExt(this,
				new BuileGestureExt.OnGestureResult() {

					@Override
					public void onGestureResult(int direction) {

						mSnakeView.show(direction);

					}

				}

		).Buile();
		
	
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
                && event.getKeyCode() == KeyEvent.KEYCODE_BACK && notGoBack)  {
			
            return true;//消费掉后退键 
        }
        return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return gestureDetector.onTouchEvent(event);

	}

	private void show(String value) {

		Toast.makeText(this, value, Toast.LENGTH_SHORT).show();

	}
	
	// 暂停事件被触发时
	@Override
	protected void onPause() {
		super.onPause();
		// Pause the game along with the activity
		mSnakeView.setMode(SnakeView.PAUSE);
	}

	// 状态保存
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// 存储游戏状态到View里
		outState.putBundle(ICICLE_KEY, mSnakeView.saveState());
	}
	@Override
	public void onDestroy(){
		super.onDestroy();

		if(SnakeView.intent2 != null){
			stopService(SnakeView.intent2);
		}
	}
	
}