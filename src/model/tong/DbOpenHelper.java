package model.tong;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DbOpenHelper {
	private static final String DATABASE_NAME = "call.db";
	private static final int DATABASE_VERSION = 1;
	public static SQLiteDatabase mDB;
	private DatabaseHelper mDBHelper;
	private Context mCtx;
	
	private class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DataBases.CreateDB._CREATE);		
			Log.d("test", "Table onCreate Complete");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS" + DataBases.CreateDB._TABLENAME);
			onCreate(db);			
			Log.d("test", "Table onUpgrade Complete");
		}

	}
	
	public DbOpenHelper(Context context)	{
		this.mCtx = context;
	}
	
	public DbOpenHelper open() throws SQLException	{
		mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
		mDB = mDBHelper.getWritableDatabase();	
		return this;
	}
	
	public void close()	{
		mDB.close();
	}
	
	// Insert DB
	public long insertColumn(String callID, String name, String date, String duration, String type)	{
		ContentValues values = new ContentValues();
		values.put(DataBases.CreateDB.callID, callID);
		values.put(DataBases.CreateDB.NAME, name);
		values.put(DataBases.CreateDB.DATE, date);
		values.put(DataBases.CreateDB.DURATION, duration);
		values.put(DataBases.CreateDB.TYPE, type);
		
		return mDB.insert(DataBases.CreateDB._TABLENAME, null, values);
	}
	
	// Update DB
	public boolean updateColumn(long id, String callID, String name, String date, String duration, String type)	{
		ContentValues values = new ContentValues();
		values.put(DataBases.CreateDB.callID, callID);
		values.put(DataBases.CreateDB.NAME, name);
		values.put(DataBases.CreateDB.DATE, date);
		values.put(DataBases.CreateDB.DURATION, duration);
		values.put(DataBases.CreateDB.TYPE, type);
		
		return mDB.update(DataBases.CreateDB._TABLENAME, values, "_ID="+id, null) > 0;
	}
	
	public boolean isDuplicateID(String id)	{
		String query = "SELECT callID FROM calldb WHERE callID=" + id;
		Cursor c = mDB.rawQuery(query, null);
		
//		Toast.makeText(mCtx, c.getCount()+"", Toast.LENGTH_SHORT).show();
		
		if(c.getCount() > 0)
			return true;
		else
			return false;
	}
	
	// Delete DB
	public boolean deleteColumn(long id)	{
		return mDB.delete(DataBases.CreateDB._TABLENAME, "_ID="+id, null) > 0;
	}
	
	// Select All
	public Cursor getAllColumns()	{
		return mDB.query(DataBases.CreateDB._TABLENAME, null, null, null, null, null, null);
	}
	
	public Cursor getAllSums()	{
		String query = "SELECT NAME, SUM(DURATION) FROM " + DataBases.CreateDB._TABLENAME + " GROUP BY NAME HAVING SUM(DURATION)>0 ORDER BY SUM(DURATION) DESC";
		Log.d("query", "getAllSums has completed!");
		return mDB.rawQuery(query, null);
	}
	
	public Cursor getAllIncalls()	{
		String query = "SELECT NAME, SUM(DURATION) FROM " + DataBases.CreateDB._TABLENAME + " WHERE TYPE=1 GROUP BY NAME HAVING SUM(DURATION)>0 ORDER BY SUM(DURATION) DESC";
		Log.d("query", "getAllIncomes has completed!");
		return mDB.rawQuery(query, null);
	}
	
	public Cursor getAllOutcalls()	{
		String query = "SELECT NAME, SUM(DURATION) FROM " + DataBases.CreateDB._TABLENAME + " WHERE TYPE=2 GROUP BY NAME HAVING SUM(DURATION)>0 ORDER BY SUM(DURATION) DESC";
		Log.d("query", "getAllOutcomes has completed!");
		return mDB.rawQuery(query, null);
	}	
	
} 
