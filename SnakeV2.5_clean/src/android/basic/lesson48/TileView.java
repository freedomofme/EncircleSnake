package android.basic.lesson48;


import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class TileView extends View {

	private int c = Color.rgb(136, 136, 255);
	private static final String tag = "yao";

	// 贴片大小
	protected static int mTileSize = 40;

	// X轴的贴片数量
	public static int mXTileCount;
	// Y轴的贴片数量
	public static int mYTileCount;

	// X偏移量
	private static int mXOffset;
	// Y偏移量
	private static int mYOffset;

	// 三种贴片图像的图像数组
	private Bitmap[] mTileArray;

	// 存每个贴片的索引
	private int[][] mTileGrid;

	// Paint对象（画笔、颜料）
	private final Paint mPaint = new Paint();

	// 构造函数
	public TileView(Context context, AttributeSet attrs) {
		super(context, attrs);
//		Log.i(tag, "TileView Constructor");
//		Log.i(tag, "mTileSize=" + mTileSize);
		mPaint.setTextSize(50); 
		mPaint.setColor(c);
	}

	// 设置贴片图片数组
	public void resetTiles(int tilecount) {
		mTileArray = new Bitmap[tilecount];
	}

	// 当该View的尺寸改变时调用，在onDraw()方法调用之前就会被调用，所以用来设置一些变量的初始值
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

//		Log.i(tag, "onSizeChanged," + "w=" + w + " h=" + h + " oldw=" + oldw + " oldh=" + oldh);

		// 定义X轴贴片数量
		mXTileCount = (int) Math.floor(w / mTileSize);
		mYTileCount = (int) Math.floor(h / mTileSize);
//		Log.i(tag, "w=" + w);
//		Log.i(tag, "h=" + h);
//		Log.i(tag, "mTileSize=" + mTileSize);
//
//		Log.i(tag, "mXTileCount=" + mXTileCount);
//		Log.i(tag, "mYTileCount=" + mYTileCount);

		// X轴偏移量
		mXOffset = ((w - (mTileSize * mXTileCount)) / 2);

		// Y轴偏移量
		mYOffset = ((h - (mTileSize * mYTileCount)) / 2);

//		Log.i(tag, "mXOffset=" + mXOffset);
//		Log.i(tag, "mYOffset=" + mYOffset);

		// 定义贴片的二维数组
		mTileGrid = new int[50	][50];

		// 清空所有切片
		clearTiles();
	}

	// 给mTileArray这个Bitmap图片数组设置值
	public void loadTile(int key, Drawable tile) {
		Bitmap bitmap = Bitmap.createBitmap(mTileSize, mTileSize, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		tile.setBounds(0, 0, mTileSize, mTileSize);
		//把一个drawable转成一个Bitmap
		tile.draw(canvas);
		//在数组里存入该Bitmap
		mTileArray[key] = bitmap;
	}
	public void loadTile2(int key, Drawable tile) {
		Bitmap bitmap = Bitmap.createBitmap(MainActivity.screenWidth, MainActivity.screenHeight, Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		tile.setBounds(0, 0, MainActivity.screenWidth, MainActivity.screenHeight);
		//把一个drawable转成一个Bitmap
		tile.draw(canvas);
		//在数组里存入该Bitmap
		mTileArray[key] = bitmap;
	}

	// 清空所有贴片
	public void clearTiles() {
//		Log.i(tag, "TileView.clearTiles");
		for (int x = 0; x < mXTileCount; x++) {
			for (int y = 0; y < mYTileCount; y++) {
				// 全部设置为0
				setTile(0, x, y);
			}
		}
	}

	// 给某个贴片位置设置一个状态索引
	public void setTile(int tileindex, int x, int y) {
		mTileGrid[x][y] = tileindex;
	}

	// onDraw
	@Override
	public void onDraw(Canvas canvas) {

//		Log.i(tag, "onDraw");
		super.onDraw(canvas);

		Bitmap bmp;
		float left;
		float top;
		canvas.drawBitmap(mTileArray[4], 0, 0, mPaint);
		for (int x = 0; x < mXTileCount; x++) {
			for (int y = 0; y < mYTileCount; y++) {
				// 当索引大于零，也就是不空时
				if (mTileGrid[x][y] > 0) {
					bmp = mTileArray[mTileGrid[x][y]];
					left = x * mTileSize + mXOffset;
					top = y * mTileSize + mYOffset;
					// mTileGrid中不为零时画此贴片
					canvas.drawBitmap(bmp, left, top, mPaint);
				}
			}
		}
	
		canvas.drawText(SnakeView.mStep+"", 20, 50, mPaint);

	}
}
