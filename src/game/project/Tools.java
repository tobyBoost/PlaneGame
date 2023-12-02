package game.project;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Tools {

	
	/**
	 * 绘制局部图片
	 * @param x   图片的x 
	 * @param y	  图片的y 
	 * @param img  图片对象
	 * @param viewX  显示在屏幕上的x
	 * @param viewY  显示在屏幕上的y
	 * @param width  显示在屏幕上的图片内容宽度
	 * @param height  显示在屏幕上的图片内容高度
	 */
	static void drawImage(Bitmap img,int x,int y,int viewX,int viewY,int width,int height,Canvas canvas,Paint paint)
	{
		canvas.save();
		canvas.clipRect(viewX, viewY, viewX+width, viewY+height);
		canvas.drawBitmap(img, x, y, paint);
		canvas.restore();
	}
	
	/**
	 * 碰撞检测方法
	 * @param x  A物体的x
	 * @param y  A物体的有坐标
	 * @param w  A物体的宽
	 * @param h  A物体的高
	 * @param x1  B物体的x
	 * @param y1  B物体的y 
	 * @param w1  B物体的宽
	 * @param h1  B物体的高
	 * @return
	 */
	static boolean collodes(int x,int y,int w,int h,int x1,int y1,int w1,int h1)
	{
		if(y1+h1 < y || y+h<y1 || x1+w1 <x || x+w< x1)
			return false;
		else
			return true;
	}
}
