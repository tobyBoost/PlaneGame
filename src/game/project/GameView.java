package game.project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class GameView extends SurfaceView implements Callback, Runnable {

	public static final int GAME_MENU = 5; // 游戏中的辅助菜单
	public static final int SPLASH = 0; // logo动画
	public static final int GAME = 6; // 游戏界面
	public static final int RECORD = 1;// 记录存档
	public static final int HELP = 2; // 帮助
	public static final int MUSIC_SET = 3; // 音乐设置
	public static final int MAIN_MENU = 4;// 主菜单

	public static int SCREEN_WIDTH; // 屏幕宽
	public static int SCREEN_HEIGHT; // 屏幕高
	public PlaneGameActivity activity;
	private SurfaceHolder holder;
	private Canvas canvas;
	private Paint paint;
	public int gameState;// 当前游戏状态

	Bitmap[] splashImg;// logo图片数组
	MenuScreen menuScreen;
	GameScreen gameScreen;
	GameMusic gameMusic;
	GameStore gameStore;

	boolean isGame;// 游戏进行的flag
	int timeCount;

	public GameView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		activity = (PlaneGameActivity) context;
		setFocusable(true);
		holder = getHolder();
		holder.addCallback(this);
		canvas = holder.lockCanvas();
		paint = new Paint();
		paint.setAntiAlias(true);
		gameState = SPLASH;
		splashImg = new Bitmap[2];
		splashImg[0] = BitmapFactory.decodeResource(getResources(),
				R.drawable.logo);
		splashImg[1] = BitmapFactory.decodeResource(getResources(),
				R.drawable.anykey);
		gameMusic = new GameMusic(context);
		gameStore = new GameStore(activity);
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub

	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		SCREEN_HEIGHT = getHeight();
		SCREEN_WIDTH = getWidth();
		menuScreen = new MenuScreen(this);
		gameScreen = new GameScreen(this);
		isGame = true;
		gameMusic.starMusic(GameMusic.BG_MUSIC);
		new Thread(this).start();

	}

	public void surfaceDestroyed(SurfaceHolder holder) {

		// TODO Auto-generated method stub
		isGame = false;
		gameMusic.stopMusic();
		gameMusic.recycle();
		activity.finish();
	}

	/**
	 * 绘制splash界面
	 */
	void drawSplash() {
		canvas.drawBitmap(splashImg[0], 0, 0, paint);
		if (timeCount % 2 == 0) {
			canvas.drawBitmap(splashImg[1],
					SCREEN_WIDTH / 2 - splashImg[1].getWidth() / 2,
					SCREEN_HEIGHT - splashImg[1].getHeight() - 20, paint);
		}
	}

	/**
	 * 绘制方法
	 */
	void paint() {
		canvas = holder.lockCanvas();
		switch (gameState) {
		case SPLASH:
			drawSplash();
			break;
		case MAIN_MENU:
		case MUSIC_SET:
		case HELP:
		case RECORD:
			menuScreen.paint(canvas, paint, gameState);
			break;
		case GAME:
			gameScreen.paint(canvas, paint);
			break;

		}
		holder.unlockCanvasAndPost(canvas);
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (gameState) {
		case SPLASH:
			gameState = MAIN_MENU;
			break;
		case MAIN_MENU:
		case MUSIC_SET:
		case HELP:
		case RECORD:
			menuScreen.onKeyDown(keyCode);
			break;
		case GAME:
			gameScreen.onKeyDown(keyCode);
			break;

		}
		return super.onKeyDown(keyCode, event);
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (gameState) {

		case GAME:
			gameScreen.onKeyUp(keyCode);
			break;
		case MUSIC_SET:

			break;
		case HELP:

			break;
		case RECORD:

			break;

		}
		return super.onKeyUp(keyCode, event);
	}

	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x = (int) event.getX();
		int y = (int) event.getY();
		switch (gameState) {
		case SPLASH:
			gameState = MAIN_MENU;
			break;
		case MAIN_MENU:
		case HELP:
		case RECORD:
		case MUSIC_SET:
			menuScreen.onTouchEvent(x, y);
			break;
		case GAME:
			gameScreen.onTouchEvent(x, y);
			break;

		}
		return super.onTouchEvent(event);
	}

	void logic() {
		switch (gameState) {
		case SPLASH:
			timeCount++;
			if (timeCount == 25)
				gameState = MAIN_MENU;
			break;
		case GAME:
			timeCount++;
			gameScreen.logic();
			break;
		case RECORD:

			break;

		}
	}

	public void run() {
		while (isGame) {
			logic();
			paint();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
