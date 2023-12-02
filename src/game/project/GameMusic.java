package game.project;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;

public class GameMusic {
	// 音乐类型
	static final int BG_MUSIC = 0;
	static final int BOOM_MUSIC = 2;
	static final int MENU_MUSIC = 1;
	
	SoundPool soundPool;//音效播放器
	MediaPlayer player;//背景音乐播放器
	boolean isOpen;//音乐开关量
	
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
	 * 开启音乐
	 * @param id  指定id的音乐
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
	 * 停止音乐播放
	 */
	void stopMusic()
	{
			player.stop();
			isOpen = false;
	}
	
	/**
	 * 设置音乐开启
	 */
	void setMusicOpen()
	{
		isOpen = true;
		starMusic(BG_MUSIC);
	}
	
	
	/**
	 * 音频回收
	 */
	void recycle()
	{
		player.release();
		soundPool.release();
	}

}
