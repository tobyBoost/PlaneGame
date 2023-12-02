package game.project;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Property {

	static final int BOOM = 0;
	static final int POWER = 1;
	static final int LEVER_UP = 2;
	final int UN_SHOW = 0;
	final int SHOW = 1;
	final int ISVISIABLE = 0;
	final int X = 1;
	final int Y = 2;
	final int WIDTH = 3;
	final int HEIGHT = 4;
	final int TYPE = 5;
	final int FRAME = 6;
	final int SPEED_X = 7;
	final int SPEED_Y = 8;
	
	Random random;
	GameScreen gameScreen;
	Bitmap image;
	int property[][];//道具对象池
	
	public Property(GameScreen gameScreen) {
		// TODO Auto-generated constructor stub
		this.gameScreen = gameScreen;
		property = new int[4][9];
		random = new Random();
		image = BitmapFactory.decodeResource(gameScreen.gameView.getResources(),R.drawable.property);
		
		
	}
	
	/**
	 *初始化方法 
	 */
	void init()
	{
		for (int i = 0; i < property.length; i++) {
			property[i][ISVISIABLE] = UN_SHOW;
		}
	}
	
	
	/**
	 * 道具创建方法
	 * @param type  道具类型
	 * @param x   道具的初始化x坐标 
	 * @param y   道具的初始化y坐标 
	 */
	void creatProperty(int type,int x,int y)
	{
		switch (type) {
		case BOOM:
			for (int i = 0; i < property.length; i++) {
				if(property[i][ISVISIABLE] == UN_SHOW)
				{
					property[i][ISVISIABLE] = SHOW;
					property[i][X] = x;
					property[i][Y] = y;
					property[i][SPEED_X] = random.nextInt()%10;
					property[i][SPEED_Y] = random.nextInt()%10;
					property[i][WIDTH] = image.getWidth()/6;
					property[i][HEIGHT] = image.getHeight();
					property[i][TYPE] = type;
					property[i][FRAME] = 0;
					break;
				}
				
			}
			break;

		case POWER:
			for (int i = 0; i < property.length; i++) {
				if(property[i][ISVISIABLE] == UN_SHOW)
				{
					property[i][ISVISIABLE] = SHOW;
					property[i][X] = x;
					property[i][Y] =  y;
					property[i][SPEED_X] = (random.nextInt()>=0?8:-8);
					property[i][SPEED_Y] = (random.nextInt()>=0?10:-10);
					property[i][WIDTH] = image.getWidth()/6;
					property[i][HEIGHT] = image.getHeight();
					property[i][TYPE] = type;
					property[i][FRAME] = 0;
					break;
				}
			}
			break;
		case LEVER_UP:
			for (int i = 0; i < property.length; i++) {
				if(property[i][ISVISIABLE] == UN_SHOW)
				{
					property[i][ISVISIABLE] = SHOW ;
					property[i][X] = x;
					property[i][Y] = y;
					property[i][SPEED_X]= random.nextInt()%9;
					property[i][SPEED_Y]= random.nextInt()%9;
					property[i][WIDTH]= image.getWidth()/6;
					property[i][HEIGHT]= image.getHeight();
					property[i][TYPE]= type;
					property[i][FRAME]= 0;
					break;
				}
			}
			break;
		}
	}
	
	/**
	 * 道具绘制
	 * @param canvas
	 * @param paint
	 */
	void paint(Canvas canvas,Paint paint)
	{
		for (int i = 0; i < property.length; i++) {
			if(property[i][ISVISIABLE] == SHOW)
				Tools.drawImage(image, property[i][X]-property[i][FRAME]*property[i][WIDTH], property[i][Y], property[i][X], property[i][Y], property[i][WIDTH], property[i][HEIGHT], canvas, paint);		
		}
	}
	
	/**
	 * 道具移动 
	 */
	void move()
	{
		for (int i = 0; i < property.length; i++) {
			if(property[i][ISVISIABLE] == SHOW)
			{
				property[i][X] += property[i][SPEED_X];
				property[i][Y] += property[i][SPEED_Y];
			}
		}
	}
	
	/**
	 * 道具回池方法
	 */
	void  setVisiable()
	{
		for (int i = 0; i < property.length; i++) {
			if(property[i][ISVISIABLE] == SHOW)
			{
				if(property[i][X] <= -property[i][WIDTH] || property[i][X]>= GameView.SCREEN_WIDTH || property[i][Y]<= -property[i][HEIGHT] || property[i][Y]>= GameView.SCREEN_HEIGHT )
					property[i][ISVISIABLE] = UN_SHOW;
			}
		}
	}
	
	/**
	 * 道具碰撞检测
	 * @param p  飞机对象
	 */
	void collodesWith(Player p)
	{
		for (int i = 0; i < property.length; i++) {
			if(property[i][ISVISIABLE] == SHOW && p.getState() == Player.NORMAL)
			{
				if(Tools.collodes(property[i][X], property[i][Y], property[i][WIDTH], property[i][HEIGHT], p.getX(), p.getY(), p.getWidth(), p.getHeight()))
				{
					switch (property[i][TYPE]) {
					case BOOM:
						gameScreen.player.addPropertyNum();
						gameScreen.player.setState(Player.SKILL_BOOM);
						property[i][ISVISIABLE] =UN_SHOW;
						break;
					case POWER:
						gameScreen.player.setState(Player.RECOVER);
						property[i][ISVISIABLE] =UN_SHOW;
						break;
					case LEVER_UP:
						gameScreen.player.setState(Player.LEVELUP);
						property[i][ISVISIABLE] =UN_SHOW;
						break;
					}
				}
					
						
			}
		}
	}
	
	
	void changeFrame()
	{
		for (int i = 0; i < property.length; i++) {
			if(property[i][ISVISIABLE] == SHOW)
			{
				switch (property[i][TYPE]) {
				case BOOM:
					property[i][FRAME] = (property[i][FRAME] == 1? 0:1);
					break;
				case LEVER_UP:
					property[i][FRAME] = (property[i][FRAME] == 4? 5:4);
					break;
				case POWER:
					property[i][FRAME] = (property[i][FRAME] == 2? 3:2);
					break;
				}
			}
		}
	}
	
	/**
	 * 修改路径
	 */
	void setPath()
	{
		for (int i = 0; i < property.length; i++) {
			if(property[i][ISVISIABLE] == SHOW )
			{
				switch (property[i][TYPE]) {
				case BOOM:
					property[i][SPEED_X] = random.nextInt()%10;
					property[i][SPEED_Y] = random.nextInt()%10;
					break;

				case LEVER_UP:
					break;
				case POWER:
					break;
				}
			}
		}
	}
	
	void logic()
	{
		move();
		setVisiable();
		collodesWith(gameScreen.player);
		changeFrame();
		if(gameScreen.gameView.timeCount%15 == 0)
			setPath();
	}
	
	
	
	
	
	
	
	
	
	
}
