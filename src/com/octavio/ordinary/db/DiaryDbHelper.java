package com.octavio.ordinary.db;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.octavio.ordinary.model.Diary;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DiaryDbHelper {

	public static final String KEY_TITLE = "title";
	public static final String KEY_BODY = "body";
	public static final String KEY_ROWID = "_id";
	public static final String KEY_CREATED = "created";
	
	public static final String KEY_MOOD = "mood"; 
	public static final String KEY_PICTURE = "picture"; 
	public static final String KEY_LOCATION = "location"; 

	// private static final String TAG = "DiaryDbAdapter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;

	private static final String DATABASE_CREATE = "create table diary "
			+ "("
			+ "_id integer primary key autoincrement, "
			+ "title text not null, "
			+ "body text not null, "
			+ "created text not null,"
			+ "mood integer,"
			+ "picture text,"
			+ "location text"
			+ ");";

	private static final String DATABASE_NAME = "diary.db";
	private static final String DATABASE_TABLE = "diary";
	private static final int DATABASE_VERSION = 1;

	private final Context mCtx;

	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS diary");
			onCreate(db);
		}
	}

	public DiaryDbHelper(Context ctx) {
		this.mCtx = ctx;
	}

	public DiaryDbHelper open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void closeclose() {
		mDbHelper.close();
	}

	public long createDiary(String title, String body,int mood,String picture,String location) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_BODY, body);
		
		initialValues.put(KEY_MOOD, mood);
		initialValues.put(KEY_PICTURE, picture);
		initialValues.put(KEY_LOCATION, location);
		
		Calendar calendar = Calendar.getInstance();
		
		
		
		String week;
		switch (calendar.get(Calendar.DAY_OF_WEEK)) {
		case 1:
			week = "星期日";
			break;
		case 2:
			week = "星期一";
			break;
		case 3:
			week = "星期二";
			break;
		case 4:
			week = "星期三";
			break;
		case 5:
			week = "星期四";
			break;
		case 6:
			week = "星期五";
			break;
		case 7:
			week = "星期六";
			break;
		default:
			week = "星期八";
			break;
		}
		String created = calendar.get(Calendar.YEAR) + "年" + (calendar.get(Calendar.MONTH)+1 )+ "月"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "日 "+week+" " + calendar.get(Calendar.HOUR_OF_DAY) + "时"
				+ calendar.get(Calendar.MINUTE) + "分";
		
		
		initialValues.put(KEY_CREATED, created);
		
		
		return mDb.insert(DATABASE_TABLE, null, initialValues);
		
		
	}

	public boolean deleteDiary(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	public Cursor getAllNotes() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_BODY, KEY_CREATED ,KEY_MOOD , KEY_PICTURE, KEY_LOCATION }, null, null, null,
				null, null);
	}

	/**
	 * by egeye by octavio 1st December 20:00
	 */
	public List<Diary> queryAll() {
		SQLiteDatabase db = mDbHelper.getReadableDatabase(); // 获得一个只读的数据库对象
		if (db.isOpen()) {

			Cursor cursor = db.rawQuery("select _id, title, body, created from diary;", null);

			if (cursor != null && cursor.getCount() > 0) {
				List<Diary> diaryList = new ArrayList<Diary>();
				int id;
				String title;
				String body;
				String created;
				while (cursor.moveToNext()) {
					id = cursor.getInt(0); // 取第0列的数据 id
					title = cursor.getString(1);  
					body = cursor.getString(2);  
					created = cursor.getString(3);
					diaryList.add(new Diary(body, title, id, created));
				}

				cursor.close();
				db.close();
				return diaryList;
			}
			db.close();
		}

		return null;
	}

	public Cursor getDiary(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE, KEY_BODY, KEY_CREATED },
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	public boolean updateDiary(long rowId, String title, String body) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_BODY, body);
		Calendar calendar = Calendar.getInstance();
		String created = calendar.get(Calendar.YEAR) + "年" + calendar.get(Calendar.MONTH) + "月"
				+ calendar.get(Calendar.DAY_OF_MONTH) + "日" + calendar.get(Calendar.HOUR_OF_DAY) + "时"
				+ calendar.get(Calendar.MINUTE) + "分";
		args.put(KEY_CREATED, created);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}
