package android.basic.lesson48;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.NotActiveException;
import java.net.ContentHandler;
import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.jar.JarEntry;

import net.testin.android.st.SpotDialogListener;
import net.testin.android.st.SpotManager;

import android.R.integer;
import android.R.xml;
import android.basic.snaketestxiong.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
//import android.preference.PreferenceActivity.Header;
import android.util.AttributeSet;
import android.util.Log;
//import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.basic.Snakes.AplusBasicSnake;
import com.basic.Snakes.ApulsBestSnake;
import com.basic.Snakes.BasicSnake;
import com.basic.Snakes.BestSnake;
import com.basic.Snakes.SurviveType;
import com.basic.strategy.*;
import com.homer.service.MusicService;
import com.ray.test.TestGallery;
import com.xiegeixiong.guide.SnakeGuide;

public class SnakeView extends TileView {
	private static final String tag = "yao";
	private static boolean isWin = false;
	private SoundPool mSoundPool = null;
	private HashMap<Integer, Integer> soundMap = new HashMap<Integer, Integer>();

	//声明蛇的对象
	BasicSnake bSnake;
	//声明SurviveType接口
	SurviveType surviveType;
	public static int DELAY = 100;
	public static Intent intent2;
	private  Context context;
	// 游戏状态，默认值是准备状态
	private int mMode = READY;

	// 游戏的四个状态 暂停 准备 运行 和 失败
	public static final int PAUSE = 0;
	public static final int READY = 1;
	public static final int RUNNING = 2;
	public static final int LOSE = 3;
	public static final int WIN = 4;

	// 游戏中蛇的前进方向，默认值北方
	private int mDirection = EAST;
	// 下一步的移动方向，默认值北方
	private int mNextDirection = EAST;
//	private boolean NotStop = false;;
//	// 敌蛇的方向
//	private int enemyNextDirection = SOUTH;
//	private int enemyDirection = NORTH;
	// 游戏方向设定 北 南 东 西
	private static final int NORTH = 1;
	private static final int SOUTH = 2;
	private static final int EAST = 3;
	private static final int WEST = 4;

	// 三种游戏元素
	private static final int RED_STAR = 1;
	private static final int YELLOW_STAR = 2;
//	private static final int GREEN_STAR = 3;
	private static final int GROUND_STAR = 4;
	private static final int Black_STAR = 5;
	private static final int White_STAR = 6;
	private static final int BLACK_HEAD = 7;
	private static final int White_HEAD = 8;
//	private static final int EDGE = 9;
	private int failTimes = 2;

	// 游戏得分
	private long mScore = 0;
	
	//走的步数
	public static int mStep = 0;

	// 移动延迟
	private long mMoveDelay = DELAY;

	// 最后一次移动时的毫秒时刻
	private long mLastMoveTime;

	// 显示游戏状态的文本组件
	private TextView mStatusTextView;

	// 蛇身数组(数组以坐标对象为元素)
	private ArrayList<Coordinate> mSnakeTrail = new ArrayList<Coordinate>();

	// 敌人的蛇的数组
	private ArrayList<Coordinate> enemySnakeTrail = new ArrayList<Coordinate>();

	// 苹果数组(数组以坐标对象为元素)
	private ArrayList<Coordinate> mAppleList = new ArrayList<Coordinate>();

	// 随机数
	private static final Random RNG = new Random();

	public ChooseApple mChooseApple;
	//
	private RefreshHandler mRedrawHandler = new RefreshHandler();

	
	// 一个Handler
	class RefreshHandler extends Handler {

		// 处理消息队列
		@Override
		public void handleMessage(Message msg) {
			// 更新View对象
			SnakeView.this.update();
			// 强制重绘
			SnakeView.this.invalidate();
			//
//			Log.i(tag, "handleMessage|Thread Name="
//					+ Thread.currentThread().getName());
		}

		// 延迟发送消息
		public void sleep(long delayMillis) {
			this.removeMessages(0);
//			Log.i(tag, "sleep|Thread Name=" + Thread.currentThread().getName());
//			BufferedReader strin=new BufferedReader(new InputStreamReader(System.in));
//			int a = 1000;
//			 try {
//				a=Integer.parseInt(strin.readLine());
//			} catch (NumberFormatException e) {
//				// TODO Auto-generated catch block
//				a = 1000;
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				a = 1000;
//			}
//			if(a != 1000)
					sendMessageDelayed(obtainMessage(0), delayMillis);
		}
	};

	// 构造函数
	public SnakeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		
//		Log.i(tag, "SnakeView Constructor");
		// 构造时初始化
		initSnakeView();

	}
	
	// 初始化
	private void initSnakeView() {
		
//		Log.e(tag, "initSnakeView");
//音效
		 mSoundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);
		 soundMap.put(1 , mSoundPool.load(this.getContext(), R.raw.sound1 , 1));
	   
	
		// 可选焦点
		setFocusable(true);

		Resources r = this.getContext().getResources();

		// 设置贴片图片数组
		resetTiles(10);

		// 把三种图片存到Bitmap对象数组
		loadTile(RED_STAR, r.getDrawable(R.drawable.redstar));
		loadTile(YELLOW_STAR, r.getDrawable(R.drawable.yellowstar));
//		loadTile(GREEN_STAR, r.getDrawable(R.drawable.greenstar));
		loadTile2(GROUND_STAR,  r.getDrawable(R.drawable.ground));
		loadTile(Black_STAR,  r.getDrawable(R.drawable.blackstar));
		loadTile(White_STAR,  r.getDrawable(R.drawable.whilestar));
		loadTile(BLACK_HEAD,  r.getDrawable(R.drawable.blackhead));
		loadTile(White_HEAD,  r.getDrawable(R.drawable.whitehead));
//		loadTile(EDGE,  r.getDrawable(R.drawable.edge));
	}

	// 设置游戏状态
	public void setMode(int newMode) {

		// 把当前游戏状态存入oldMode
		int oldMode = mMode;
		// 把游戏状态设置为新状态
		mMode = newMode;

		Resources res = getContext().getResources();
		CharSequence str = "";

		// 如果新状态是运行状态，且原有状态为不运行，那么就开始游戏
		if (newMode == RUNNING & oldMode != RUNNING) {
			play();
			// 设置mStatusTextView隐藏
			mStatusTextView.setVisibility(View.INVISIBLE);
			// 更新
			update();
			return;
		}

		// 如果新状态是暂停状态，那么设置文本内容为暂停内容
		if (newMode == PAUSE) {
			if(TestGallery.isAdOn)
				showAd();
			pause();
			str = res.getText(R.string.mode_pause);
		}

		// 如果新状态是准备状态，那么设置文本内容为准备内容
		if (newMode == READY) {
			str = res.getText(R.string.mode_ready);
		}

		// 如果新状态时失败状态，那么设置文本内容为失败内容
		if (newMode == LOSE) {
			stop();
//			System.out.println("win~~~:" + isWin);
			// 把上轮的得分显示出来
			if(isWin == true) {
				str = res.getString(R.string.mode_win_prefix) 
					+ res.getString(R.string.mode_lose_step) + mStep
					+ res.getString(R.string.mode_lose_step2) 
					+ res.getString(R.string.mode_lose_suffix);

				SharedPreferences sharedPreferences = context.getSharedPreferences("snake", Context.MODE_PRIVATE);
				Editor editor = sharedPreferences.edit();//获取编辑器
				editor.putInt("score"+TestGallery.chooseNumber, mStep);
				editor.commit();//提交修改                  
				
			} else { 
//				失败情况不记录
//				SharedPreferences sharedPreferences = context.getSharedPreferences("snake", Context.MODE_PRIVATE);
//				Editor editor = sharedPreferences.edit();//获取编辑器
//				editor.putInt("score"+TestGallery.chooseNumber, mStep);
//				editor.commit();//提交修改     
				
				if(failTimes++%3 == 0)
					if(TestGallery.isAdOn)
						showAd();
				str = res.getString(R.string.mode_lose_prefix)
						+ res.getString(R.string.mode_lose_fail)
						+ res.getString(R.string.mode_lose_suffix);
				
			}
		}

		// 设置文本
		mStatusTextView.setText(str);
		// 显示该View
		mStatusTextView.setVisibility(View.VISIBLE);
	}

	// 设置状态显示View
	public void setStatusTextView(TextView newView) {
		mStatusTextView = newView;
	}

	// 按键
	public boolean onKeyDown(int keyCode, KeyEvent msg) {

		// 向上键
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			// 准备状态或者失败状态时
			if (mMode == READY | mMode == LOSE) {
				// 初始化游戏
				initNewGame();
				// 设置游戏状态为运行
				setMode(RUNNING);
				// 更新
				update();
				// 消费掉
				return (true);
			}

			// 暂停状态时
			if (mMode == PAUSE) {
				// 设置成运行状态
				setMode(RUNNING);
				update();
				// 消费掉
				return (true);
			}

			// 如果是运行状态时，如果方向原有方向不是向南，那么方向转右
			if (mDirection != SOUTH) {
				mNextDirection = NORTH;
			}
			return (true);
		}

		// 向下键
		if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
			// 原方向不是向上时，方向转右
			if (mDirection != NORTH) {
				mNextDirection = SOUTH;
			}
			// 消费掉
			return (true);
		}

		// 向左键
		if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
			// 原方向不是向右时，方向转右
			if (mDirection != EAST) {
				mNextDirection = WEST;
			}
			// 消费掉
			return (true);
		}

		// 向右键
		if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
			// 原方向不是向左时，方向转右
			if (mDirection != WEST) {
				mNextDirection = EAST;
			}
			// 消费掉
			return (true);
		}

		// 按其他键时按原有功能返回
		return super.onKeyDown(keyCode, msg);
	}

	// 更新
	public void update() {
		// 如果是处于运行状态
		if (mMode == RUNNING) {

			long now = System.currentTimeMillis();

			// 如果当前时间距离最后一次移动的时间超过了延迟时间
			if (now - mLastMoveTime >= mMoveDelay) {
				//
				clearTiles();
//				updateWalls();
//				updateSnake();
				updateEnemySnake();
				updateApples();
				updateSnake();
				mLastMoveTime = now;
			}
			// 会话进程睡一个延迟时间单位
			mRedrawHandler.sleep(mMoveDelay);
		}

	}

	// 更新墙
//	private void updateWalls() {
//		Log.i(tag, "updateWalls");
//		for (int x = 0; x < mXTileCount; x++) {
//			// 给上边线的每个贴片位置设置一个绿色索引标识
//			setTile(EDGE, x, 0);
//			// 给下边线的每个贴片位置设置一个绿色索引标识
//			setTile(EDGE, x, mYTileCount - 1);
//		}
//		for (int y = 1; y < mYTileCount - 1; y++) {
//			// 给左边线的每个贴片位置设置一个绿色索引标识
//			setTile(EDGE, 0, y);
//			// 给右边线的每个贴片位置设置一个绿色索引标识
//			setTile(EDGE, mXTileCount - 1, y);
//		}
//	}

	// 更新苹果
	private void updateApples() {
//		Log.i(tag, "updateApples");

		for (Coordinate c : mAppleList) {
			setTile(YELLOW_STAR, c.x, c.y);
		}
	}

	// 更新蛇
	private void updateSnake() {
		// 生长标志
		boolean growSnake = false;
		
		// 得到蛇头坐标
		Coordinate head = mSnakeTrail.get(0);

		// 初始化一个新的蛇头坐标
		Coordinate newHead = new Coordinate(1, 1);

		// 当前方向改成新的方向
		mDirection = mNextDirection;

		// 根据方向确定蛇头新坐标
		switch (mDirection) {
		// 如果方向向东（右），那么X加1
		case EAST: {
			newHead = new Coordinate(head.x + 1, head.y);
			break;
		}
		// 如果方向向西（左），那么X减1
		case WEST: {
			newHead = new Coordinate(head.x - 1, head.y);
			break;
		}
		// 如果方向向北（上），那么Y减1
		case NORTH: {
			newHead = new Coordinate(head.x, head.y - 1);
			break;
		}
		// 如果方向向南（下），那么Y加1
		case SOUTH: {
			newHead = new Coordinate(head.x, head.y + 1);
			break;
		}
		}

		// 冲突检测 新蛇头是否四面墙重叠，那么游戏结束
		if ((newHead.x < 0) || (newHead.y < 0) || (newHead.x > mXTileCount - 1)
				|| (newHead.y > mYTileCount - 1)) {
			// 设置游戏状态为Lose
			setMode(LOSE);
			
			int index = 0;
			// 为了让游戏结束时显示这条蛇
			// 重新设置一下颜色，蛇头式黄色的，蛇身是红色的
			for (Coordinate c1 : mSnakeTrail) {
				if (index == 0) {
					setTile(White_HEAD, c1.x, c1.y);
				} else {
					setTile(White_STAR, c1.x, c1.y);
				}
				index++;
			}
			
			// 返回
			return;

		}

		// 冲突检测 新蛇头是否和自身坐标重叠，重叠的话游戏也结束
		int snakelength = mSnakeTrail.size();

		for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
			Coordinate c = mSnakeTrail.get(snakeindex);
			if (c.equals(newHead)) {
				// 设置游戏状态为Lose
				setMode(LOSE);
				int index = 0;
				// 为了让游戏结束时显示这条蛇
				// 重新设置一下颜色，蛇头式黄色的，蛇身是红色的
				for (Coordinate c1 : mSnakeTrail) {
					if (index == 0) {
						setTile(White_HEAD, c1.x, c1.y);
					} else {
						setTile(White_STAR, c1.x, c1.y);
					}
					index++;
				}
				
				// 返回
				return;
			}
		}
	
		// 看新蛇头和苹果们是否重叠
		int applecount = mAppleList.size();
		for (int appleindex = 0; appleindex < applecount; appleindex++) {
			Coordinate c = mAppleList.get(appleindex);
			if (c.equals(newHead)) {
				// 如果重叠，苹果坐标从苹果列表中移除
				mAppleList.remove(c);
				mSoundPool.play(soundMap.get(1), 1, 1, 0, 0, 1);
				// 再立刻增加一个新苹果
				addRandomApple();
				// 得分加一
//				mScore++;
				// 延迟是以前的90%
				//mMoveDelay *= 0.9;
				// 蛇增长标志改为真
				growSnake = true;
			}
		}
		
		// 步数增加
		mStep++;
		// 在蛇头的位置增加一个新坐标
		mSnakeTrail.add(0, newHead);
		// 如果没有增长
		if (!growSnake) {
			// 如果蛇头没增长则删去最后一个坐标，也就相当于蛇向前走了一步^_^
			mSnakeTrail.remove(mSnakeTrail.size() - 1);
		}

		int index = 0;
		// 重新设置一下颜色，蛇头式黄色的，蛇身是红色的
		for (Coordinate c : mSnakeTrail) {
			if (index == 0) {
				setTile(White_HEAD, c.x, c.y);
			} else {
				setTile(White_STAR, c.x, c.y);
			}
			index++;
		}

	}

	private void updateEnemySnake() {
		// 生长标志
		boolean growEnemySnake = false;

		// 得到蛇头坐标
		Coordinate enemyHead = enemySnakeTrail.get(0);

		// 初始化一个新的蛇头坐标
		Coordinate newEnemyHead = new Coordinate(1, 1);

//		// 当前方向改成新的方向
//		enemyDirection = EAST;

		// 选择的苹果
		Coordinate aCoordinate = mChooseApple.choose(mAppleList,
				mSnakeTrail.get(0), enemyHead);
	
		//寻找路径，确定下一个位置的走位
		//最后一个参数，不应该这样设计的，偷懒了
		newEnemyHead = bSnake.searchCoordinate(aCoordinate, enemySnakeTrail, mAppleList, mSnakeTrail);
//		System.out.println("enemySnakeTrail :"  + enemySnakeTrail);
		
		// 冲突检测 新蛇头是否四面墙重叠，那么游戏结束
		if ((newEnemyHead.x < 0) || (newEnemyHead.y < 0)
				|| (newEnemyHead.x > mXTileCount - 1)
				|| (newEnemyHead.y > mYTileCount - 1)) {
			// 设置游戏状态为Lose
			// 要改为win
			isWin = true;
			//折衷了，用了个isWin
			setMode(LOSE);
			int index = 0;
			// 为了让游戏结束时显示这条蛇
			// 重新设置一下颜色，蛇头式黄色的，蛇身是红色的
			for (Coordinate c1 : enemySnakeTrail) {
				if (index == 0) {
					setTile(BLACK_HEAD, c1.x, c1.y);
				} else {
					setTile(Black_STAR, c1.x, c1.y);
				}
				index++;
			}
			
			
			// 返回
			return;

		}
		// 冲突检测 新蛇头是否和自身坐标重叠，重叠的话游戏也结束
		int snakelength = enemySnakeTrail.size();

		for (int snakeindex = 0; snakeindex < snakelength; snakeindex++) {
			Coordinate c = enemySnakeTrail.get(snakeindex);
			if (c.equals(newEnemyHead)) {
				// 设置游戏状态为Lose
				// 要改为win
				//折衷了，用了个isWin,isWin要放在setMode前
				isWin = true;
				setMode(LOSE); 
//				System.out.println("isWin" + isWin);
				int index = 0;
				// 为了让游戏结束时显示这条蛇
				// 重新设置一下颜色，蛇头式黄色的，蛇身是红色的
				for (Coordinate c1 : enemySnakeTrail) {
					if (index == 0) {
						setTile(BLACK_HEAD, c1.x, c1.y);
					} else {
						setTile(Black_STAR, c1.x, c1.y);
					}
					index++;
				}
				// 返回
				return;
			}
		}
		//检测蛇头和mSnakeTrail冲突
		for (int snakeindex = 0; snakeindex < mSnakeTrail.size(); snakeindex++) {
			Coordinate c = mSnakeTrail.get(snakeindex);
			if (c.equals(newEnemyHead)) {
				// 设置游戏状态为Lose
				// 要改为win
				//折衷了，用了个isWin,isWin要放在setMode前
				isWin = true;
				setMode(LOSE); 
//				System.out.println("isWin" + isWin);
				int index = 0;
				// 为了让游戏结束时显示这条蛇
				// 重新设置一下颜色，蛇头式黄色的，蛇身是红色的
				for (Coordinate c1 : enemySnakeTrail) {
					if (index == 0) {
						setTile(BLACK_HEAD, c1.x, c1.y);
					} else {
						setTile(Black_STAR, c1.x, c1.y);
					}
					index++;
				}
				// 返回
				return;
			}
		}
		
		// 看新蛇头和苹果们是否重叠
		int applecount = mAppleList.size();
		for (int appleindex = 0; appleindex < applecount; appleindex++) {
			Coordinate c = mAppleList.get(appleindex);
			if (c.equals(newEnemyHead)) {
				// 如果重叠，苹果坐标从苹果列表中移除
				mAppleList.remove(c);
				// 再立刻增加一个新苹果
				addRandomApple();
				// 得分加一
				//mScore++;
				// 延迟是以前的90%
				// mMoveDelay *= 0.9;
				// 蛇增长标志改为真
				growEnemySnake = true;
			}
		}
		// 在蛇头的位置增加一个新坐标
		enemySnakeTrail.add(0, newEnemyHead);
		// 如果没有增长
		if (!growEnemySnake) {
			// 如果蛇头没增长则删去最后一个坐标，也就相当于蛇向前走了一步^_^
			enemySnakeTrail.remove(enemySnakeTrail.size() - 1);
		}
		if(enemySnakeTrail.size() > 100)
			enemySnakeTrail.remove(enemySnakeTrail.size() - 1);

		int index = 0;
		// 重新设置一下颜色，蛇头式黄色的，蛇身是红色的
		for (Coordinate c : enemySnakeTrail) {
			if (index == 0) {
				setTile(BLACK_HEAD, c.x, c.y);
			} else {
				setTile(Black_STAR, c.x, c.y);
			}
			index++;
		}
//		 index = 0;
//		 if(enemyPath != null)
//		 // 重新设置一下颜色，蛇头式黄色的，蛇身是红色的
//		 for (Coordinate c : enemyPath) {
//		 if (index == 0) {
//		 setTile(GREEN_STAR, c.x, c.y);
//		 } else {
//		 setTile(GREEN_STAR, c.x, c.y);
//		 }
//		 index++;
//		 }

	}

	// 添加苹果
	private void addRandomApple() {
		// 新的坐标
		Coordinate newCoord = null;
		//
		boolean flag = true;
		// 为真的话在循环体内一直循环
		while (flag) {
			// 为苹果再找一个坐标,先随机一个X值
			int newX = 0 + RNG.nextInt(mXTileCount - 1);
			// 再随机一个Y值
			int newY = 0 + RNG.nextInt(mYTileCount - 1);
			// 新坐标
			newCoord = new Coordinate(newX, newY);

			// 下面确保新苹果不在蛇身下，先假设没发生冲突
			boolean collision = false;

			int snakelength = mSnakeTrail.size();
			// 和蛇占据的所有坐标比较
			for (int index = 0; index < snakelength; index++) {
				// 只要和蛇占据的任何一个坐标相同，即认为发生冲突了
				if (mSnakeTrail.get(index).equals(newCoord)) {
					collision = true;
				}
			}
			for (int index = 0; index < enemySnakeTrail.size(); index++) {
				// 只要和蛇占据的任何一个坐标相同，即认为发生冲突了
				if (enemySnakeTrail.get(index).equals(newCoord)) {
					collision = true;
				}
			}
			
			// 如果有冲突就继续循环，如果没冲突flag的值就是false,那么自然会退出循环，新坐标也就诞生了
			flag = collision;
		}

//		if (newCoord == null) {
//			Log.e(tag, "Somehow ended up with a null newCoord!");
//		}
		// 生成一个新苹果放在苹果列表中（两个苹果可能会重合吧）
		mAppleList.add(newCoord);
	}

	// 初始化游戏
	private void initNewGame() {

//		Log.e(tag, "initNewGame!");
		
		isWin = false;
		
		int chooseNumber = TestGallery.chooseNumber;
//		Log.i(tag, "chooseNumber" + chooseNumber);
		//创建basic蛇的对象
		if(chooseNumber == 0) {
			ChooseApple chooseApple = new ChooseFirst();
			bSnake = new BasicSnake(50, 50, 220, false, chooseApple);
			mChooseApple = bSnake.getChooseAppleStrategy();
		} else if(chooseNumber == 1) {
			ChooseApple chooseApple2 = new ChooseNearestSimple();
			bSnake = new BestSnake(100, 70, 220, false, chooseApple2);
			mChooseApple = bSnake.getChooseAppleStrategy();
		} else if(chooseNumber == 2){
			ChooseApple chooseApple2 = new ChooseNearestApple();
			bSnake = new BestSnake(100, 40, 220, true, chooseApple2);
			mChooseApple = bSnake.getChooseAppleStrategy();
		}  else if (chooseNumber == 3) {
			ChooseApple chooseApple2 = new ChooseNearestApple();
			bSnake = new AplusBasicSnake(80, 40, 220, true, chooseApple2);
			mChooseApple = bSnake.getChooseAppleStrategy();
		} else if (chooseNumber == 4) {
			ChooseApple chooseApple2 = new ChooseNearestApple();
			bSnake = new ApulsBestSnake(80, 40, 220, true, chooseApple2);
			mChooseApple = bSnake.getChooseAppleStrategy();
		} 
		
		
		// 清空ArrayList列表
		mSnakeTrail.clear();
		mAppleList.clear();

		// 清空敌蛇
		enemySnakeTrail.clear();

		// 创建蛇身
		mSnakeTrail.add(new Coordinate(7, 7));
		mSnakeTrail.add(new Coordinate(6, 7));
		mSnakeTrail.add(new Coordinate(5, 7));
		mSnakeTrail.add(new Coordinate(4, 7));
		mSnakeTrail.add(new Coordinate(3, 7));
		mSnakeTrail.add(new Coordinate(2, 7));

		// 敌蛇蛇身
		
//		enemySnakeTrail.add(new Coordinate(12, 16));
//		enemySnakeTrail.add(new Coordinate(11, 16));

//		enemySnakeTrail.add(new Coordinate(10, 16));
//		enemySnakeTrail.add(new Coordinate(10, 17));
//		enemySnakeTrail.add(new Coordinate(10, 18));
//		
//		enemySnakeTrail.add(new Coordinate(10, 19));
//		enemySnakeTrail.add(new Coordinate(11, 19));
//		enemySnakeTrail.add(new Coordinate(12, 19));
//
//		enemySnakeTrail.add(new Coordinate(13, 19));
//		enemySnakeTrail.add(new Coordinate(14, 19));
//		
//		enemySnakeTrail.add(new Coordinate(15, 19));
//		enemySnakeTrail.add(new Coordinate(15, 18));
//		enemySnakeTrail.add(new Coordinate(15, 17));
//		enemySnakeTrail.add(new Coordinate(15, 16));
		
//		enemySnakeTrail.add(new Coordinate(15, 15));
//		enemySnakeTrail.add(new Coordinate(14, 15));
//		enemySnakeTrail.add(new Coordinate(13, 15));
		enemySnakeTrail.add(new Coordinate(7, 15));
		enemySnakeTrail.add(new Coordinate(6, 15));
		enemySnakeTrail.add(new Coordinate(5, 15));
		enemySnakeTrail.add(new Coordinate(4, 15));
		enemySnakeTrail.add(new Coordinate(3, 15));
		enemySnakeTrail.add(new Coordinate(2, 15));

		// 新的方向 ：北方
		mNextDirection = NORTH;
//		enemyNextDirection = NORTH;

		// 开始都时候有2个苹果
	//	addRandomApple();
	//	addRandomApple();
		mAppleList.add(new Coordinate(13, 16));
		mAppleList.add(new Coordinate(1, 16));

		// 设置移动延迟
		mMoveDelay = DELAY;
		
		// 得分0
		mScore = 0;
		
		//步数0
		mStep = 0;
		
//		System.out.println("mMoveDelay"  + mMoveDelay);
	}

	// 保存状态
	public Bundle saveState() {

		Bundle bundle = new Bundle();

		bundle.putIntArray("mAppleList", coordArrayListToArray(mAppleList));
		bundle.putIntArray("mSnakeTrail", coordArrayListToArray(mSnakeTrail));

		bundle.putInt("mDirection", Integer.valueOf(mDirection));
		bundle.putInt("mNextDirection", Integer.valueOf(mNextDirection));

		bundle.putLong("mMoveDelay", Long.valueOf(mMoveDelay));
		bundle.putLong("mScore", Long.valueOf(mScore));
		bundle.putInt("mStep", mStep);

		return bundle;
	}

	// 恢复状态
	public void restoreState(Bundle icicle) {

		setMode(PAUSE);

		mAppleList = coordArrayToArrayList(icicle.getIntArray("mAppleList"));
		mDirection = icicle.getInt("mDirection");
		mNextDirection = icicle.getInt("mNextDirection");
		mMoveDelay = icicle.getLong("mMoveDelay");
		mScore = icicle.getLong("mScore");
		mStep = icicle.getInt("mStep");
		mSnakeTrail = coordArrayToArrayList(icicle.getIntArray("mSnakeTrail"));
	}

	// 整数数组转坐标数组
	private ArrayList<Coordinate> coordArrayToArrayList(int[] rawArray) {
		ArrayList<Coordinate> coordArrayList = new ArrayList<Coordinate>();

		int coordCount = rawArray.length;
		for (int index = 0; index < coordCount; index += 2) {
			Coordinate c = new Coordinate(rawArray[index], rawArray[index + 1]);
			coordArrayList.add(c);
		}
		return coordArrayList;
	}

	// 坐标数组转整数数组
	private int[] coordArrayListToArray(ArrayList<Coordinate> cvec) {
		int count = cvec.size();
		int[] rawArray = new int[count * 2];
		for (int index = 0; index < count; index++) {
			Coordinate c = cvec.get(index);
			rawArray[2 * index] = c.x;
			rawArray[2 * index + 1] = c.y;
		}
		return rawArray;
	}

	public boolean show(int string) {
		// TODO Auto-generated method stub

		// 向上键
		if (string == 0) {
			// 准备状态或者失败状态时
			if (mMode == READY | mMode == LOSE) {
				// 初始化游戏
				initNewGame();
				// 设置游戏状态为运行
				setMode(RUNNING);
				// 更新
				update();
				// 消费掉
				return (true);
			}

			// 暂停状态时
			if (mMode == PAUSE) {
				// 设置成运行状态
				setMode(RUNNING);
				update();
				// 消费掉
				return (true);
			}

			// 如果是运行状态时，如果方向原有方向不是向南，那么方向转右
			if (mDirection != SOUTH) {
				mNextDirection = NORTH;
			}
			return (true);
		}

		// 向下键
		if (string == 1) {
			// 原方向不是向上时，方向转右
			if (mDirection != NORTH) {
				mNextDirection = SOUTH;
			}
			// 消费掉
			return (true);
		}

		// 向左键
		if (string == 2) {
			// 原方向不是向右时，方向转右
			if (mDirection != EAST) {
				mNextDirection = WEST;
			}
			// 消费掉
			return (true);
		}

		// 向右键
		if (string == 3) {
			// 原方向不是向左时，方向转右
			if (mDirection != WEST) {
				mNextDirection = EAST;
			}
			// 消费掉
			return (true);
		}
		return true;

	}
	
	void play() {
		int op = 1;
		intent2 = new Intent(context, MusicService.class);
		Bundle bundle = new Bundle();
		bundle.putInt("op", op);
		intent2.putExtras(bundle);
		if(context != null) {
		context.startService(intent2);							// startService
//		Log.v("AAA", "play");
		}
	}
	void pause() {
		int op = 3;
		intent2 = new Intent(context, MusicService.class);
		Bundle bundle = new Bundle();
		bundle.putInt("op", op);
		intent2.putExtras(bundle);
//		Log.v("AAA", "pause");
		context.startService(intent2);	
	}
	void stop() {
		int op = 2;
//		Log.v("AAA", "stop");
		intent2 = new Intent(context, MusicService.class);
		Bundle bundle = new Bundle();
		bundle.putInt("op", op);
		intent2.putExtras(bundle);
		
		context.startService(intent2);	
	}
	
	private void showAd() {
		SpotManager.getInstance(context).showSpotAds(context,
				new SpotDialogListener() {
					@Override
					public void onShowSuccess() {
						Log.i("YoumiAdDemo", "展示成功");
						MainActivity.notGoBack = true;
					}

					@Override
					public void onShowFailed() {
						Log.i("YoumiAdDemo", "展示失败");
						MainActivity.notGoBack = false;
					}

					@Override
					public void onSpotClosed() {
						Log.i("YoumiAdDemo", "展示关闭");
						MainActivity.notGoBack = false;
					}

				}); // //


	}

}
