package game.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class GameScreen {

	final int GAME = 0;
	final int LOSE = 1;//游戏失败
	final int WIN = 2;//游戏胜利
	final int LEVEL_UP = 3; // 战机升级
	final int RECOVER = 4;//战机修复
	final int BOSS = 5;//boss出现
	
	GameView gameView;
	Bitmap  mapImg ;//地图图片
	Bitmap score[];//游戏的状态栏图片
	Bitmap number;
	Bitmap plane;
	Bitmap fontImg[] ;//游戏失败
	Bitmap recoverImg;//战机修复图片 
	Bitmap bottomBar;
	
	int gameState;
	int mapY;
	Player player;
	Bullet bullet;
	Enemy enemy;
	Property property;
	Rect[] barSelectRect;
	public GameScreen(GameView view) {
		// TODO Auto-generated constructor stub
		gameView = view;
		mapImg = BitmapFactory.decodeResource(view.getResources(), R.drawable.map);    
		plane = BitmapFactory.decodeResource(view.getResources(), R.drawable.player);
		score = new Bitmap[2];
		score[0] = BitmapFactory.decodeResource(view.getResources(), R.drawable.score);
		score[1] = BitmapFactory.decodeResource(view.getResources(), R.drawable.imghp);
		number = BitmapFactory.decodeResource(view.getResources(), R.drawable.number);
		fontImg  = new Bitmap[5];
		fontImg[0] = BitmapFactory.decodeResource(view.getResources(), R.drawable.font_lose);
		fontImg[1] = BitmapFactory.decodeResource(view.getResources(), R.drawable.font_win);
		fontImg[2] = BitmapFactory.decodeResource(view.getResources(), R.drawable.font_levelup);
		fontImg[3] = BitmapFactory.decodeResource(view.getResources(), R.drawable.font_recover);
		fontImg[4] = BitmapFactory.decodeResource(view.getResources(), R.drawable.font_boss);
		recoverImg = BitmapFactory.decodeResource(view.getResources(), R.drawable.invincible);
		bottomBar = BitmapFactory.decodeResource(view.getResources(), R.drawable.botton_bar);
		player = new Player(plane,this);
		property = new Property(this);
		enemy = new Enemy(this);
		bullet = new Bullet(this,enemy,property);
		mapY = GameView.SCREEN_HEIGHT-mapImg.getHeight();
		barSelectRect = new Rect[2];
		barSelectRect[0] = new Rect(0, 454, 80, 480);
		barSelectRect[1] = new Rect(240,454,320,480);
		gameState = 0;
	}
	
	/**
	 * 游戏界面的初始化方法 
	 */
	void init()
	{
		gameState = 0;
		mapY = GameView.SCREEN_HEIGHT-mapImg.getHeight();
		enemy.init();
		player.init();
		bullet.init();
		property.init();
		gameView.timeCount = 0;
	}
	
	/**
	 * 设置字体
	 * @param type 字体类型
	 */
	void setFont(int type)
	{
		gameState = type;
	}
	
	/**
	 * 绘制游戏界面
	 */
	void paint(Canvas canvas,Paint paint)
	{
		drawMap(canvas, paint);
		player.paint(canvas, paint);
		bullet.paint(canvas, paint);
		enemy.paint(canvas, paint);
		property.paint(canvas, paint);
		drawScore(canvas, paint);
		if (gameState != GAME ) 
		{
			canvas.drawBitmap(fontImg[gameState-1], 0, 130, paint);
			if((gameState == LOSE || gameState == WIN )&& gameView.timeCount%2 ==0)
				canvas.drawBitmap(gameView.splashImg[1], GameView.SCREEN_WIDTH/2-gameView.splashImg[1].getWidth()/2, 280, paint);
		}
		
		
	}
	
	/**
	 * 绘制地图
	 */
	void drawMap(Canvas canvas,Paint paint)
	{
		if(mapY<0)
			canvas.drawBitmap(mapImg, 0, mapY, paint);
		else
		{
			Tools.drawImage(mapImg, 0,  mapY-mapImg.getHeight(), 0, 0, GameView.SCREEN_WIDTH, mapY,canvas,paint);
			Tools.drawImage(mapImg, 0, mapY, 0, mapY, GameView.SCREEN_WIDTH, GameView.SCREEN_HEIGHT-mapY,canvas,paint);
		}
	}
	
	
	/**
	 * 绘制状态栏
	 */
	void drawScore(Canvas canvas,Paint paint)
	{

		canvas.drawBitmap(score[0], 0, 0,paint);
		canvas.drawBitmap(score[1], 2, 4, paint);
		canvas.drawRect(97-(player.getMaxHp()-player.getHp())/3,4,97,11, paint);
		for (int i = 0; i < player.getPropertyNum(); i++) {
			Tools.drawImage(property.image, 110+i*32-property.image.getWidth()/6, 3, 110+i*32, 3, property.image.getWidth()/6, property.image.getHeight(), canvas, paint);
		}
		Tools.drawImage(number, 288-(player.getLifeNum()-1)*number.getWidth()/3, 8, 288, 8, number.getWidth()/3, number.getHeight(), canvas, paint);
		canvas.drawBitmap(bottomBar, 0,GameView.SCREEN_HEIGHT-bottomBar.getHeight(), paint);
	}
	
	
	
	/**
	 * 游戏状态下键盘输入 
	 * @param keyCode 键值
	 */
	void onKeyDown(int keyCode)
	{
		switch (gameState) {
		case GAME:
				switch (keyCode) {
				case KeyEvent.KEYCODE_DPAD_UP:
					player.changeDirection(Player.UP);
					break;
				case KeyEvent.KEYCODE_DPAD_DOWN:
					player.changeDirection(Player.DOWN);
					break;
				case KeyEvent.KEYCODE_DPAD_LEFT:
					player.changeDirection(Player.LEFT);
					break;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					player.changeDirection(Player.RIGHT);
					break;
				case KeyEvent.KEYCODE_DPAD_CENTER:
					player.setState(Player.SKILL_BOOM);
					gameView.gameMusic.starMusic(GameMusic.BOOM_MUSIC);
					break;
				}
			break;

		case LOSE:
		case WIN:
			gameView.gameState = GameView.MAIN_MENU;
			break;
		}
		
	}
	
	/**
	 * 按键按下处理
	 * @param keyCode 键值
	 */
	void onKeyUp(int keyCode)
	{
		switch (gameState) {
		case GAME:
			switch (keyCode) {
			case KeyEvent.KEYCODE_DPAD_UP:
			case KeyEvent.KEYCODE_DPAD_DOWN:
			case KeyEvent.KEYCODE_DPAD_LEFT:
			case KeyEvent.KEYCODE_DPAD_RIGHT:
				player.stop();
				break;
			}
			
			break;

		default:
			break;
		}
		
	}
	
	/**
	 * 触屏时间
	 * @param x  触点x坐标
	 * @param y	  触点y坐标
	 */
	void onTouchEvent(int x,int y) {
		if (barSelectRect[0].contains(x, y)) {
			gameView.gameState = GameView.MAIN_MENU;
		}else if(barSelectRect[1].contains(x, y))
		{
			gameView.gameStore.saveData(gameView.gameScreen.player);
		}
	}
	
	/**
	 * 游戏界面逻辑处理
	 */
	void logic()
	{
		switch (gameState) {
		case GAME:
			mapY +=2;
			if(mapY>= GameView.SCREEN_HEIGHT)
				mapY = GameView.SCREEN_HEIGHT-mapImg.getHeight();
			player.logic();
			bullet.logic();
			enemy.logic();
			property.logic();
			break;

		default:
			break;
		}
		
	}
}
