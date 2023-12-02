package game.project;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class Tools {

	
	/**
	 * ���ƾֲ�ͼƬ
	 * @param x   ͼƬ��x 
	 * @param y	  ͼƬ��y 
	 * @param img  ͼƬ����
	 * @param viewX  ��ʾ����Ļ�ϵ�x
	 * @param viewY  ��ʾ����Ļ�ϵ�y
	 * @param width  ��ʾ����Ļ�ϵ�ͼƬ���ݿ��
	 * @param height  ��ʾ����Ļ�ϵ�ͼƬ���ݸ߶�
	 */
	static void drawImage(Bitmap img,int x,int y,int viewX,int viewY,int width,int height,Canvas canvas,Paint paint)
	{
		canvas.save();
		canvas.clipRect(viewX, viewY, viewX+width, viewY+height);
		canvas.drawBitmap(img, x, y, paint);
		canvas.restore();
	}
	
	/**
	 * ��ײ��ⷽ��
	 * @param x  A�����x
	 * @param y  A�����������
	 * @param w  A����Ŀ�
	 * @param h  A����ĸ�
	 * @param x1  B�����x
	 * @param y1  B�����y 
	 * @param w1  B����Ŀ�
	 * @param h1  B����ĸ�
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
