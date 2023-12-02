package game.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.KeyEvent;

public class MenuScreen {
	
	GameView gameView;
	Bitmap[] mainMenuImg;//主菜单图片数组
	Bitmap helpImg;//帮助图片
	int menuSelectIndex ;//菜单选项下标
	Rect[] menuSelectRect;
	Bitmap musicSetImg;//声音设置图片
	String  music[] ={"ON","OFF"}; 
	
	public MenuScreen(GameView view) {
		// TODO Auto-generated constructor stub
		gameView = view;
		mainMenuImg = new Bitmap[4];
		mainMenuImg[0] = BitmapFactory.decodeResource(view.getResources(), R.drawable.menu1);
		mainMenuImg[1] = BitmapFactory.decodeResource(view.getResources(), R.drawable.menu2);
		mainMenuImg[2] = BitmapFactory.decodeResource(view.getResources(), R.drawable.menu3);
		mainMenuImg[3] = BitmapFactory.decodeResource(view.getResources(), R.drawable.menu4);
		menuSelectRect = new Rect[5];
		for(int i = 0;i<menuSelectRect.length;i++)
			menuSelectRect[i] = new Rect(85, 45+i*60, 85+148, 45+i*60+24);
		helpImg =  BitmapFactory.decodeResource(view.getResources(), R.drawable.help);
		musicSetImg = BitmapFactory.decodeResource(view.getResources(), R.drawable.set);
	}
	
	
	/**
	 * 绘制主菜单界面
	 */
	void drawMainMenu(Canvas canvas,Paint paint)
	{
		canvas.drawRGB(22, 22, 24);
		canvas.drawBitmap(mainMenuImg[3], GameView.SCREEN_WIDTH/2-mainMenuImg[3].getWidth()/2,GameView.SCREEN_HEIGHT-mainMenuImg[3].getHeight(), paint);
		for (int i = 0; i < 5; i++) {
			canvas.drawBitmap(mainMenuImg[0], 0,45+i*60, paint);
			if(i!= menuSelectIndex)
				Tools.drawImage(mainMenuImg[1], 123,45+i*60-i*24, 123, 45+i*60, 74, 24, canvas, paint);
			else
				Tools.drawImage(mainMenuImg[2], 85,45+i*60-i*24, 85, 45+i*60, 148, 24, canvas, paint);
			
		}
		
		
	}
	
	
	/**
	 * 绘制帮助
	 */
	void drawHelp(Canvas canvas,Paint paint)
	{
		canvas.drawBitmap(helpImg, 0, 0, paint);
	}
	
	void drawMusicSet(Canvas canvas,Paint paint)
	{
		
		canvas.drawBitmap(musicSetImg, 0,185, paint);
		paint.setColor(Color.CYAN);
		if(gameView.gameMusic.isOpen)
			canvas.drawText(music[0], 145, 205, paint);
		else
			canvas.drawText(music[1], 143, 205, paint);
	}
	
	
	void paint(Canvas canvas,Paint paint,int gameState)
	{
		switch (gameState) {
		
		case GameView.MAIN_MENU:
			drawMainMenu(canvas, paint);
			break;
		case GameView.MUSIC_SET:
			drawMusicSet(canvas, paint);
			break;
		case GameView.HELP:
			drawHelp(canvas, paint);
			break;
		case GameView.RECORD:
			paint.setColor(Color.GRAY);
			canvas.drawRect(50, 70, 270, 100, paint);
			paint.setColor(Color.CYAN);
			canvas.drawText("暂无任何存档，请按任意键返回主菜单", 58, 88, paint);
			break;
		}
		
	}
	
	void mainMenuInput(int keyCode)
	{
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_UP:
			menuSelectIndex = (menuSelectIndex==0?4:menuSelectIndex-1);
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			menuSelectIndex = (menuSelectIndex==4?0:menuSelectIndex+1);
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			switch (menuSelectIndex) {
			case 0://开始游戏
				gameView.gameState = GameView.GAME;
				gameView.gameScreen.init();
				
				break;
			case 1://读档
				boolean readFlag = gameView.gameStore.getData(gameView.gameScreen.player);
				if(readFlag)
					gameView.gameState = GameView.GAME;
				else
					gameView.gameState = GameView.RECORD;
				break;
			case 2://帮助
				gameView.gameState = GameView.HELP;
				break;
			case 3://声音设置
				gameView.gameState = GameView.MUSIC_SET;
				break;
			case 4://退出游戏
				gameView.isGame = true;
				gameView.activity.finish();
				break;
			}
			break;
		}
	}
	
	void musicSetInput(int keyCode)
	{
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			gameView.gameMusic.isOpen = !gameView.gameMusic.isOpen;
			break;
		case KeyEvent.KEYCODE_DPAD_CENTER:
			if(!gameView.gameMusic.isOpen)
				gameView.gameMusic.stopMusic();
			gameView.gameState = GameView.MAIN_MENU;
			break;
		}
	}
	
	
	
	void onKeyDown(int keyCode)
	{
		switch (gameView.gameState) {
		case GameView.MAIN_MENU:
			mainMenuInput(keyCode);
			break;
		case GameView.RECORD:
			gameView.gameState = GameView.MAIN_MENU;
			break;
		case GameView.MUSIC_SET:
			musicSetInput(keyCode);
			break;
		case GameView.HELP:
			gameView.gameState = GameView.MAIN_MENU;
			break;
		}
		
	}

	
	void onTouchEvent(int x ,int y)
	{
		switch (gameView.gameState) {
		case GameView.MAIN_MENU:
			if(menuSelectRect[0].contains(x, y))
			{
				menuSelectIndex = 0;
				gameView.gameState = GameView.GAME;
				gameView.gameScreen.init();
				gameView.gameMusic.starMusic(GameMusic.MENU_MUSIC);
			}
			else if(menuSelectRect[1].contains(x, y))
			{
				menuSelectIndex = 1;
				gameView.gameMusic.starMusic(GameMusic.MENU_MUSIC);
				boolean readFlag = gameView.gameStore.getData(gameView.gameScreen.player);
				if(readFlag)
					gameView.gameState = GameView.GAME;
				else
					gameView.gameState = GameView.RECORD;
			}
			else if(menuSelectRect[2].contains(x, y))
			{
				menuSelectIndex = 2;
				gameView.gameState = GameView.HELP;
				gameView.gameMusic.starMusic(GameMusic.MENU_MUSIC);
			}
			else if(menuSelectRect[3].contains(x, y))
			{
				menuSelectIndex = 3;
				gameView.gameState = GameView.MUSIC_SET;
				System.out.println("music set");
				//gameView.gameMusic.starMusic(GameMusic.MENU_MUSIC);
			}
			else if(menuSelectRect[4].contains(x, y))
			{
				menuSelectIndex = 4;
				gameView.isGame = true;
				gameView.activity.finish();
			}
			break;

		case GameView.HELP:
			gameView.gameState = GameView.MAIN_MENU;
			break;
		case GameView.RECORD:
			gameView.gameState = GameView.MAIN_MENU;
			break;
		}
		
	}
	
	
	
	
	
	
}
