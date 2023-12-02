package game.project;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class GameStore {
	SQLiteHelper helper;
	SQLiteDatabase database;
	
	public GameStore(Context context) {
		// TODO Auto-generated constructor stub
		helper = new SQLiteHelper(context, SQLiteHelper.DATABASE_NAME, null, SQLiteHelper.VERSION);
		database = helper.getWritableDatabase();
	}
	
	/**
	 * 游戏存档
	 * @param player  存档数据
	 */
	void saveData(Player player)
	{
		ContentValues contentValues = new ContentValues();
		contentValues.put("flag","true");
		contentValues.put("playerState",player.getState());
		contentValues.put("x",player.getX());
		contentValues.put("y",player.getY());
		contentValues.put("lifeNum",player.getLifeNum());
		contentValues.put("hp",player.getHp());
		contentValues.put("direction",player.getDirection());
		contentValues.put("level",player.getLevel());
		contentValues.put("atk",player.getAtk());
		contentValues.put("propertyNum",player.getPropertyNum());
		database.update("gameStore", contentValues, null, null);
		
	}
	
	/**
	 * 读取存档
	 * @param player  存储数据对象
	 * @return 是否读取成功
	 */
	boolean  getData(Player player)
	{
		Cursor cursor = database.rawQuery("SELECT * FROM gameStore WHERE playerState>=?", new String[]{"0"});
		boolean flag = false;
		while (cursor.moveToNext()) {
			String msg = cursor.getString(cursor.getColumnIndex("flag"));
			if(msg.equals("true"))
			{
				int playerState  = cursor.getInt(cursor.getColumnIndex("playerState"));
				int x = cursor.getInt(cursor.getColumnIndex("x"));
				int y = cursor.getInt(cursor.getColumnIndex("y"));
				int lifeNum = cursor.getInt(cursor.getColumnIndex("lifeNum"));
				int hp = cursor.getInt(cursor.getColumnIndex("hp"));
				int direction = cursor.getInt(cursor.getColumnIndex("direction"));
				int level = cursor.getInt(cursor.getColumnIndex("level"));
				int atk = cursor.getInt(cursor.getColumnIndex("atk"));
				int propertyNum = cursor.getInt(cursor.getColumnIndex("propertyNum"));
				player.setState(playerState);
				player.setData(x, y, lifeNum, hp, direction, level, atk, propertyNum);
				return true;
			}
		}
		return flag;
	}
	
	
	
}

class SQLiteHelper extends SQLiteOpenHelper{
	static final String DATABASE_NAME = "game.db";
	static final int VERSION = 1;
	
	public SQLiteHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE gameStore(_id INTEGER PRIMARY KEY AUTOINCREMENT,flag String,playerState INT,x INT ,y INT, lifeNum INT,hp INT,direction INT ,level INT , atk INT, propertyNum INT)");
		ContentValues contentValues = new ContentValues();
		contentValues.put("flag","false");
		contentValues.put("playerState",0);
		contentValues.put("x",0);
		contentValues.put("y",0);
		contentValues.put("lifeNum",0);
		contentValues.put("hp",0);
		contentValues.put("direction",0);
		contentValues.put("level",0);
		contentValues.put("atk",0);
		contentValues.put("propertyNum",0);
		db.insert("gameStore", null, contentValues);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
}