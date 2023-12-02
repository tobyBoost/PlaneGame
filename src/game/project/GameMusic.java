package game.project;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class GameMusic {
	// ��������
	static final int BG_MUSIC = 0;
	static final int BOOM_MUSIC = 2;
	static final int MENU_MUSIC = 1;
	
	SoundPool soundPool;//��Ч������
	MediaPlayer player;//�������ֲ�����
	boolean isOpen;//���ֿ�����
	
	public GameMusic(Context context) {
		// TODO Auto-generated constructor stub
		isOpen = true;
		player = MediaPlayer.create(context, R.raw.game);
		player.setLooping(true);
		soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 5);
		soundPool.load(context, R.raw.menu, 1);
		soundPool.load(context, R.raw.explosion, 1);
	}
	
	/**
	 * ��������
	 * @param id  ָ��id������
	 */
	void starMusic(int id)
	{
		if(isOpen)
		{
			switch (id) {
			case BG_MUSIC:
				player.start();
				break;
			case BOOM_MUSIC:
				soundPool.play(BOOM_MUSIC, 1, 1, 0, 0, 1);
				break;
			case MENU_MUSIC:
				soundPool.play(MENU_MUSIC, 1, 1, 0, 0, 1);
				break;
			}
		}
	}
	
	
	/**
	 * ֹͣ���ֲ���
	 */
	void stopMusic()
	{
			player.stop();
			isOpen = false;
	}
	
	/**
	 * �������ֿ���
	 */
	void setMusicOpen()
	{
		isOpen = true;
		starMusic(BG_MUSIC);
	}
	
	
	/**
	 * ��Ƶ����
	 */
	void recycle()
	{
		player.release();
		soundPool.release();
	}

}
