package game.project;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Enemy {

	// 敌机类型
	static final int TYPE_YELLOW = 0;
	static final int TYPE_RED = 1;
	static final int TYPE_PURPLE = 2;
	static final int TYPE_GREEN =3;
	static final int TYPE_BOSS = 4;
		
	//敌机属性下标值
	final int SHOW = 1;
	final int UN_SHOW = 0;
	final int BOOM = 2;
	final int ISVISABLE = 0;//下标对应的值： 0 不可视  1可视
	final int TYPE = 1;
	final int X = 2;
	final int Y = 3;
	final int WIDTH = 4;
	final int HEIGHT = 5;
	final int SPEED_X = 6;
	final int SPEED_Y = 7;
	final int HP = 8;
	final int SETP_COUNT = 9;
	final int SETP_INDEX = 10;
	final int ATK = 11;
	final int FRAME = 12;
	GameScreen gameScreen;
	int enemy[][];//敌机对象池数组
	Bitmap eImg[];//敌机图片数组
	Bitmap boomImg;//爆炸图片
	int timeCount;
	Random random;
	//不同类型敌机的初始位置
	int randomX[][] ={{50,120,175 ,260},
					  {35,250},
					  {65,215},
					  {0,280,60,220},
					  {}
					  };
	//紫色敌机飞行路径设定数组
	int purpleStep[] ={15,5,10,5,8};

	Property property;
	public Enemy(GameScreen gameScreen) {
		// TODO Auto-generated constructor stub
		this.gameScreen = gameScreen;
		property = gameScreen.property;
		random = new Random();
		enemy = new int[20][13];
		eImg = new Bitmap[5];
		eImg[0] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.enemy0);
		eImg[1] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.enemy1);
		eImg[2] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.enemy2);
		eImg[3] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.enemy3);
		eImg[4] = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.enemy4);
		boomImg = BitmapFactory.decodeResource(gameScreen.gameView.getResources(), R.drawable.boomimg);
	}
	
	/**
	 * 初始化敌机对象池 
	 */
	void init()
	{
		for (int i = 0; i < enemy.length; i++) {
			for (int j = 0; j < enemy[i].length; j++) 
				enemy[i][ISVISABLE] = UN_SHOW;//不可视 
				
			
		}
	}

	/**
	 * 敌机绘制
	 */
	void paint(Canvas canvas,Paint paint)
	{
		for (int i = 0; i < enemy.length; i++) {
			switch (enemy[i][ISVISABLE]) {
			case SHOW:
				int type = enemy[i][TYPE];
				canvas.drawBitmap(eImg[type], enemy[i][X], enemy[i][Y], paint);
				break;

			case BOOM:
				Tools.drawImage(boomImg, enemy[i][X]-enemy[i][FRAME]*boomImg.getWidth()/6,  enemy[i][Y], enemy[i][X], enemy[i][Y], boomImg.getWidth()/6, boomImg.getHeight(), canvas, paint);
				break;
			}
		}
		
	}
	
	/**
	 * 移动方法
	 */
	void move()
	{
		for (int i = 0; i < enemy.length; i++) {
			if(enemy[i][ISVISABLE] == SHOW)
			{
				enemy[i][X] += enemy[i][SPEED_X];
				enemy[i][Y] += enemy[i][SPEED_Y];
			}
		}
	}
	
	/**
	 * 创建敌机
	 */
	void createEnemy(int type)
	{
		switch (type) {
		case TYPE_YELLOW://直线
			for (int i = 0; i < enemy.length; i++) {
				if(enemy[i][ISVISABLE] == UN_SHOW)
				{
					enemy[i][ISVISABLE] = SHOW;
					enemy[i][TYPE] = TYPE_YELLOW;
					enemy[i][HP] = 18;//暂定？
					enemy[i][WIDTH] = eImg[type].getWidth();
					enemy[i][HEIGHT] = eImg[type].getHeight();
					enemy[i][X] = randomX[TYPE_YELLOW][Math.abs(random.nextInt()%4)];
					enemy[i][Y] = -enemy[i][HEIGHT];
					enemy[i][SPEED_X] = 0;
					enemy[i][SPEED_Y] = 8;
					enemy[i][ATK] = 2*enemy[i][HP];
					enemy[i][FRAME] = 0;
					break;
				}
			}
			break;
		case TYPE_GREEN://交叉
			int count = 0;
			int index = Math.abs(random.nextInt()%2);
			for (int i = 0; i < enemy.length; i++) {
				if(enemy[i][ISVISABLE] == UN_SHOW)
				{
					enemy[i][ISVISABLE] = SHOW;
					enemy[i][TYPE] = TYPE_GREEN;
					enemy[i][HP] = 20;//暂定？
					enemy[i][WIDTH]  = eImg[type].getWidth();
					enemy[i][HEIGHT] = eImg[type].getHeight() ;
					if(index == 0)
						enemy[i][X]  = randomX[TYPE_GREEN][count];
					else
						enemy[i][X]  = randomX[TYPE_GREEN][count+2];
					enemy[i][Y]  = -enemy[i][HEIGHT];
					enemy[i][SPEED_X]  = 8+count*-16;
					enemy[i][SPEED_Y] = 15;
					enemy[i][ATK] = 30;
					enemy[i][FRAME] = 0;
					count++;
					if(count == 2)
						break;
				}
			}
			break;
		case TYPE_RED://来回横扫
			index = Math.abs(random.nextInt()%2);
			for (int i = 0; i < enemy.length; i++) {
				if(enemy[i][ISVISABLE] == UN_SHOW)
				{
					enemy[i][ISVISABLE] = SHOW;
					enemy[i][TYPE] = TYPE_RED;
					enemy[i][HP] = 24;
					enemy[i][WIDTH] = eImg[type].getWidth();
					enemy[i][HEIGHT] = eImg[type].getHeight();
					enemy[i][X] = randomX[TYPE_RED][index];
					enemy[i][Y] = -enemy[i][HEIGHT];
					enemy[i][SPEED_X] = 14+index*-28;
					enemy[i][SPEED_Y] = 6;
					enemy[i][ATK] = 2*enemy[i][HP];
					enemy[i][FRAME] = 0;
					break;
				}
			}
			
			break;
		case TYPE_PURPLE://前进后退，左右小范围扫射
			count = 0;
			for (int i = 0; i < enemy.length; i++) {
				if(enemy[i][ISVISABLE] == UN_SHOW)
				{
					enemy[i][ISVISABLE] = SHOW;
					enemy[i][TYPE] = TYPE_PURPLE;
					enemy[i][HP] = 35;
					enemy[i][WIDTH] = eImg[type].getWidth();
					enemy[i][HEIGHT] = eImg[type].getHeight();
					enemy[i][X] = randomX[TYPE_PURPLE][count];
					enemy[i][Y] = -enemy[i][HEIGHT];
					enemy[i][SPEED_X] = 0;
					enemy[i][SPEED_Y] = 12;
					enemy[i][SETP_INDEX] = 0;
					enemy[i][SETP_COUNT] = 0;
					enemy[i][ATK] = 2*enemy[i][HP];
					enemy[i][FRAME] = 0;
					count++;
					if(count == 2)
						break;
				}
			}
			break;
		case TYPE_BOSS:
			for(int i = 0;i<enemy.length;i++)
				enemy[i][ISVISABLE] = UN_SHOW;
			int i = 0;
			enemy[i][ISVISABLE] = SHOW;
			enemy[i][TYPE] = TYPE_BOSS;
			enemy[i][HP] = 1000;
			enemy[i][WIDTH] = eImg[type].getWidth();
			enemy[i][HEIGHT] = eImg[type].getHeight();
			enemy[i][X] = GameView.SCREEN_WIDTH/2-enemy[i][WIDTH]/2;
			enemy[i][Y] = -enemy[i][HEIGHT];
			enemy[i][SPEED_X] = 0;
			enemy[i][SPEED_Y] = 12;
			enemy[i][SETP_INDEX] = 0;
			enemy[i][SETP_COUNT] = 0;
			enemy[i][ATK] = enemy[i][HP]/2;
			enemy[i][FRAME] = 0;
			break;
		}
		
	}
	
	/**
	 * 敌机回池
	 */
	void setVisiable()
	{
		for (int i = 0; i < enemy.length; i++) {
			if(enemy[i][ISVISABLE] == SHOW)
			{
				if(enemy[i][X]+enemy[i][WIDTH] <= 0 || enemy[i][X] >= GameView.SCREEN_WIDTH || enemy[i][Y] >= GameView.SCREEN_HEIGHT)
				{
					enemy[i][ISVISABLE] = UN_SHOW;
					
				}
			}
		}
	}
	
	/**
	 * 碰撞判定
	 */
	void collodesWith()
	{
		for (int i = 0; i < enemy.length; i++) {
			switch (enemy[i][ISVISABLE]) {
			case SHOW:
				switch (gameScreen.player.getState()) {
				case Player.NORMAL:
					if(Tools.collodes(enemy[i][X], enemy[i][Y], enemy[i][WIDTH], enemy[i][HEIGHT], gameScreen.player.getX(), gameScreen.player.getY(), gameScreen.player.getWidth(), gameScreen.player.getHeight()))
					{
						if(enemy[i][TYPE] != TYPE_BOSS)
							enemy[i][ISVISABLE] = BOOM;
						else
							enemy[i][HP] -= gameScreen.player.getAtk()*2;
						gameScreen.player.setHp(enemy[i][ATK]);
						
						
					}
					break;

				default:
					break;
				}
				
				break;

			case BOOM:
				if(enemy[i][FRAME] == 5)
				{
					int ranNum = Math.abs(random.nextInt()%10)+1;
					switch (enemy[i][TYPE]) {
					case Enemy.TYPE_GREEN:
						if(ranNum<=3)	
							property.creatProperty(Property.POWER, enemy[i][X], enemy[i][Y]);
						break;
					case Enemy.TYPE_PURPLE:
						if(ranNum >= 6 && ranNum<=7)
							property.creatProperty(Property.BOOM, enemy[i][X], enemy[i][Y]);
						break;
					case Enemy.TYPE_RED:
						if(ranNum >=8 && ranNum<=10)
							property.creatProperty(Property.LEVER_UP, enemy[i][X], enemy[i][Y]);
						break;
					}
					enemy[i][ISVISABLE] = UN_SHOW;
				}
				else
					enemy[i][FRAME]++;
				break;
			}
		}
	}
	
	/**
	 * 设置敌机飞行路径
	 */
	void setPath()
	{
		for(int i = 0;i<enemy.length ;i++)
		{
			if(enemy[i][ISVISABLE] == SHOW)
			{
				switch (enemy[i][TYPE]) {
				case TYPE_RED:
					if(enemy[i][X] >= GameView.SCREEN_WIDTH-enemy[i][WIDTH])
						enemy[i][SPEED_X] = -14;
					else if(enemy[i][X] <= 8)
						enemy[i][SPEED_X] = 14;
					break;
				
				case TYPE_PURPLE:
					enemy[i][SETP_COUNT]++;
					switch (enemy[i][SETP_INDEX]) {
					case 0:// 下
						if(enemy[i][SETP_COUNT] == purpleStep[0])
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 1;
							enemy[i][SPEED_X] = -8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					case 1://左
						if(enemy[i][SETP_COUNT] == purpleStep[1])
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 2;
							enemy[i][SPEED_X] = 8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					case 2://右
						if(enemy[i][SETP_COUNT] ==purpleStep[2])
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX]= 3;
							enemy[i][SPEED_X] = -8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					
					case 3://左
						if(enemy[i][SETP_COUNT] == purpleStep[3])
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 4;
							enemy[i][SPEED_X] = 0;
							enemy[i][SPEED_Y] = -10;
						}
						break;
					case 4://上
						if(enemy[i][SETP_COUNT] == purpleStep[4])
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 0;
							enemy[i][SPEED_X] = 0;
							enemy[i][SPEED_Y] = 12;
						}
						break;
					}
					break;
				case TYPE_BOSS:
					enemy[i][SETP_COUNT]++;
					switch (enemy[i][SETP_INDEX]) {
					case 0:// 下
						if(enemy[i][SETP_COUNT] == 10)
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 1;
							enemy[i][SPEED_X] = -8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					case 1://左
						if(enemy[i][SETP_COUNT] == 5)
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 2;
							enemy[i][SPEED_X] = 8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					case 2://右
						if(enemy[i][SETP_COUNT] ==10)
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX]= 3;
							enemy[i][SPEED_X] = -8;
							enemy[i][SPEED_Y] = 0;
						}
						break;
					
					case 3://左
						if(enemy[i][SETP_COUNT] == 5)
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 4;
							enemy[i][SPEED_X] = 0;
							enemy[i][SPEED_Y] = -10;
						}
						break;
					case 4://上
						if(enemy[i][SETP_COUNT] == 8)
						{
							enemy[i][SETP_COUNT] = 0;
							enemy[i][SETP_INDEX] = 0;
							enemy[i][SPEED_X] = 0;
							enemy[i][SPEED_Y] = 12;
						}
						break;
					}
					break;
				}
			}
		}
	}
	
	/**
	 * 敌机类逻辑处理
	 */
	void logic()
	{
		timeCount++;
		if(gameScreen.player.getState() != Player.SKILL_BOOM)
		{
			if(timeCount < 400)
			{
				if(timeCount%15 == 0)
					createEnemy(TYPE_YELLOW);
				if (timeCount%37 == 0) {
					createEnemy(TYPE_GREEN);
				}
				if(timeCount%65 == 0)
					createEnemy(TYPE_RED);
				if (timeCount %170 == 0) {
					createEnemy(TYPE_PURPLE);
				}
			}else if(timeCount == 400)
				createEnemy(TYPE_BOSS);
		}
		move();
		setPath();
		setVisiable();
		collodesWith();
		
	}
}
	
	
	
	
	
	
	
	
	
	