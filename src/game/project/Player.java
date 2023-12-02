package game.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Player {

	public static final int UP = 0;
	public static final int DOWN = 1;
	public static final int LEFT = 2;
	public static final int RIGHT = 3;
	public static final int NORMAL = 0;
	public static final int LEVELUP = 1;
	public static final int BOOM = 2;
	public static final int RECOVER = 3;
	public static final int SKILL_BOOM = 4;
	final int MAX_HP = 285;
	
	
	
	private int playerState;
	private int x,y;//飞机的x y坐标
	private int speed;//飞机的速度
	private int lifeNum ;//生命条数
	private int hp;//血量
	private int width,height;//飞机的宽高
	private int direction ;
	private int frame ;//当前的画面帧
	private boolean isMove ;
	private int level ;//飞机的等级，等级决定子弹的类型
	private int atk;//子弹的攻击
	private int propertyNum ;//炸弹道具的数量
	Bitmap plane;
	
	GameScreen screen;
	int timeCount;
	
	
	public Player(Bitmap plane,GameScreen screen ) {
		// TODO Auto-generated constructor stub
		this.screen = screen;
		this.plane = plane;
		width = plane.getWidth()/6;
		height = plane.getHeight();
		init();
		
	}
	
	void setData(int x,int y,int lifeNum ,int hp,int direction,int level ,int atk,int propertyNum)
	{
		this.x = x;
		this.y = y;
		this.lifeNum = lifeNum;
		this.hp = hp;
		this.direction = direction;
		this.level = level;
		this.atk = atk;
		this.propertyNum = propertyNum;
	}
	
	int getDirection()
	{
		return direction;
	}
	/**
	 * 返回炸弹道具总数
	 * @return
	 */
	int getPropertyNum()
	{
		return propertyNum;
	}
	
	/**
	 * 炸弹道具增加方法
	 */
	void addPropertyNum()
	{
		if(propertyNum<3)
			propertyNum++; 
	}
	
	/**
	 * 初始化方法
	 */
	void init()
	{
		playerState = NORMAL;
		direction = UP;
		frame = 2;
		x = GameView.SCREEN_WIDTH/2-width/2;
		y = GameView.SCREEN_HEIGHT - height- 40;
		speed = 15;
		lifeNum = 3;
		hp = MAX_HP ;  
		isMove = false;
		level = 0;
		atk = 20;//????暂定
		propertyNum = 1;
	}
	
	int getState()
	{
		return playerState;
	}
	
	void setState(int state)
	{
		switch (state) {
		case LEVELUP:
			if(level <2)
			{
				level =level+1;
				atk += 2*atk;
				playerState = LEVELUP;
				timeCount = 0;
			}
			break;

		case RECOVER:
			hp +=100;
			playerState = RECOVER;
			timeCount = 0;
			break;
		case SKILL_BOOM:
			if(propertyNum>=1)
			{
				playerState = SKILL_BOOM;
				propertyNum--;
				screen.bullet.createPlayerBoom();
			}
			break;
		case NORMAL:
			playerState = NORMAL;
			break;
		}
		
	}
	
	int getAtk()
	{
		return atk;
	}
	int getMaxHp()
	{
		return MAX_HP;
	}
	int getWidth()
	{
		return width;
	}
	
	int getHeight()
	{
		return height;
	}
	int getX()
	{
		return x;
	}
	
	int getY()
	{
		return y;
	}
	void setLevelUp()
	{
		if(level < 2)
			level++;
	}
	int getLevel()
	{
		return level;
	}
	int getLifeNum()
	{
		return lifeNum;
	}
	
	int getHp()
	{
		return hp;
	}
	
	void setHp(int atk)
	{
		if(hp>atk)
			hp -= atk;
		else
		{	hp = 0;
			if(lifeNum>1)
			{
				lifeNum -= 1;
				playerState = BOOM;
				frame = 0;
			}else
				screen.setFont(screen.LOSE);
		}
	}
	
	void changeFrame()
	{
		switch (playerState) {
		case NORMAL:
			switch (direction) {
			case UP:
			case DOWN:
				frame = (frame == 2? 3:2);
				break;
			case LEFT:
				frame = (frame == 0? 1:0);
				break;
			case RIGHT:
				frame = (frame == 42? 5:4);
				break;
			}
			break;
		case LEVELUP:
			if(timeCount == 10)
				playerState = NORMAL;
			else
				timeCount++;
			switch (direction) {
			case UP:
			case DOWN:
				frame = (frame == 2? 3:2);
				break;
			case LEFT:
				frame = (frame == 0? 1:0);
				break;
			case RIGHT:
				frame = (frame == 42? 5:4);
				break;
			}
			break;
		case RECOVER:
			if(timeCount == 20)
				playerState = NORMAL;
			else
				timeCount++;
			switch (direction) {
			case UP:
			case DOWN:
				frame = (frame == 2? 3:2);
				break;
			case LEFT:
				frame = (frame == 0? 1:0);
				break;
			case RIGHT:
				frame = (frame == 42? 5:4);
				break;
			}
			break;
		case BOOM:
			if(frame ==5)
			{
				playerState = NORMAL;
				hp = MAX_HP;
			}else
				frame++;
			
			break;
			
		}
		
	}
	
	void move()
	{
		if(isMove)
			switch (playerState) {
			case NORMAL:
			case LEVELUP:
			case RECOVER:
				switch (direction) {
				case UP:
					if(y >= speed)
						y-= speed;
					break;
				case DOWN:
					if(y <= GameView.SCREEN_HEIGHT-height-speed)
						y += speed;
					break;
				case LEFT:
					if(x>= speed)
						x -= speed;
					break;
				case RIGHT:
					if(x<= GameView.SCREEN_WIDTH- width-speed)
						x += speed;
					break;
				}
				break;

			default:
				break;
			}
			
	}
	
	void changeDirection(int direction)
	{
		isMove = true;
		this.direction  = direction;
	}
	
	void stop()
	{
		isMove = false;
		direction = UP;
	}
	
	void paint(Canvas canvas,Paint paint)
	{
		switch (playerState) {
		case NORMAL:
			Tools.drawImage(plane, x-frame*width, y, x, y, width, height, canvas, paint);
			break;
		case LEVELUP:
			if(timeCount%2== 0)
				canvas.drawBitmap(screen.fontImg[2], 0,90, paint);
			Tools.drawImage(plane, x-frame*width, y, x, y, width, height, canvas, paint);
			break;
		case RECOVER:
			if(timeCount%2== 0)
				canvas.drawBitmap(screen.fontImg[3], 0,90, paint);
			Tools.drawImage(screen.recoverImg,x-12-timeCount%2*60, y-10, x-12, y-10, 60, 60, canvas, paint);
			Tools.drawImage(plane, x-frame*width, y, x, y, width, height, canvas, paint);
			break;
		case BOOM:
			Tools.drawImage(screen.enemy.boomImg, x-frame*screen.enemy.boomImg.getWidth()/6, y, x, y, screen.enemy.boomImg.getWidth()/6, screen.enemy.boomImg.getHeight(), canvas, paint);
			break;
		
		}
		
	}
	
	/**
	 * 飞机逻辑控制
	 */
	void logic()
	{
		changeFrame();
		move();
	}
}
