package com.vivek.marriage_center_collection;

import java.util.ArrayList;
import java.util.HashMap;

import android.R.array;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CenterCollectionDatabase extends Activity {
	
	public static final String KEY_ID = "id";
	public static final String KEY_ROUNDID = "round";
	public static final String KEY_PLAYERID = "playerid";
	public static final String KEY_MAAL = "maal";
	public static final String KEY_POINT = "point";
	public static final String KEY_WON = "won";
	private static final String DATABASE_NAME ="center_collection";
	private static final String DATABASE_TABLE= "collection";
	private static final int DATABASE_VERSION = 2;
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			
			db.execSQL("CREATE TABLE " + DATABASE_TABLE +" ("+
					KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
					KEY_ROUNDID + " INTEGER, "+
					KEY_PLAYERID + " INTEGER, "+
					KEY_POINT + " INTEGER, "+
					KEY_MAAL + " INTEGER, "+
					KEY_WON+ " INTEGER );"			
				);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {			
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	
	public CenterCollectionDatabase(Context c)
	{
		ourContext = c;
	}
	
	public CenterCollectionDatabase open() throws SQLException{
		ourHelper = new DbHelper(ourContext);
		ourDatabase = ourHelper.getWritableDatabase();
		return this;
		
	}
	public void close()
	{
		ourHelper.close();
	}

	/************************Putting data to database**************************************************/
	public long add_score(int round, int playerid, int point, int maal, int won) {  
		
		ContentValues cv = new ContentValues();
		cv.put(KEY_ROUNDID, round);
		cv.put(KEY_PLAYERID, playerid);
		cv.put(KEY_POINT, point);
		cv.put(KEY_MAAL, maal);
		cv.put(KEY_WON, won);
		
		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	
	public long update_score(int round, int playerid, int point, int maal, int won) {  
		
		ContentValues cv = new ContentValues();
		cv.put(KEY_POINT, point);
		cv.put(KEY_MAAL, maal);
		cv.put(KEY_WON, won);
		
		return ourDatabase.update(DATABASE_TABLE, cv, KEY_ROUNDID+"=? AND "+KEY_PLAYERID+"=?", new String[]{String.valueOf(round), String.valueOf(playerid)});
//		return ourDatabase.insert(DATABASE_TABLE, null, cv);
	}
	
	/***********************Checking if there exist some data in database******************************/
	public int ifdata_in_table() 
	{
		Cursor c = ourDatabase.rawQuery("select count(round) as number from collection", null);
		c.moveToFirst();
		
		int num_index = c.getColumnIndex("number");
			
		int number = c.getInt(num_index);
//		Log.i("totalrecord", Integer.toString(num_index));
		
		c.close();
		
		return number;
	}
	
	/***********************Checking who is the winner of round****************************************/
	public int check_winner(int r)
	{
		String query = "select playerid from collection where round="+r+" and won=1";
//		Log.i("check winner query", query);
		
		Cursor c = ourDatabase.rawQuery(query , null);
		c.moveToFirst();
				
//		Log.i("size of data", String.valueOf(c.getCount()));
		int winnerId = c.getInt(0);	
		c.close();
	
		return winnerId;
	}
	
	/***********************getting total maal of the round  (Mt) *************************************/
	
	public int get_total_maal(int r)
	{
		Cursor c = ourDatabase.rawQuery("select maal from collection where round= "+r+"", null);
		
		int totalmaal=0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			totalmaal = totalmaal+c.getInt(0);
			
		}
		
		c.close();
		return totalmaal;
	}
	
	/***********************getting point of specific player on specific round (Pp)*******************/
			
	public int point_of_player(int r, int pid)
	{
		Cursor c = ourDatabase.rawQuery("select point from collection where round= "+r+" and playerid= "+pid+"", null);
				
		c.moveToFirst();
		int playerpoint = c.getInt(0);
		
		c.close();
		
		return playerpoint;
	}
	
	/***********************getting maal of specific player on specific round (Mp)*********************/
	public int maal_of_player(int r, int pid)
	{
		Cursor c = ourDatabase.rawQuery("select maal from collection where round= "+r+" and playerid= "+pid+"", null);
		c.moveToFirst();
		
		int playermaal = c.getInt(0);
		c.close();
		
		return playermaal;
	}
	
	/**********************Getting the list of winners in each round***********************************/
	public int[] getting_all_winner()
	{
		
		Cursor c = ourDatabase.rawQuery("select playerid from collection where won='1'", null);
		c.moveToFirst();
		
		int number_of_row = c.getCount();
		
		int[] winnerlist = new int[number_of_row];
		int i=0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			winnerlist[i] = c.getInt(0);
//			Log.i("winner of "+i, String.valueOf(winnerlist[i]));
			i++;
		}
		
		c.close();
		
		return winnerlist;
	}
	
	
	
	/***********************Deleting all the records from database**************************************/
	public void delete_database()
	{
		if(ifdata_in_table()> 0)
		{
			ourDatabase.delete(DATABASE_TABLE, null, null);
			ourDatabase.execSQL("delete from sqlite_sequence where name='"+DATABASE_TABLE+"';");
			Log.i("Data Deleted", "Data Successfully Deleted");
		}
		
		
	}

	public void insertnewuser(int noOfPlayer, int noOfRound) {
		Log.i("insert user-> Noof round", String.valueOf(noOfRound));
//		Log.i("insert user-> Noof player", String.valueOf(noOfPlayer));
		
		for(int i=1; i<noOfRound; i++){
//			Log.i("insert user-> round", String.valueOf(i));
			ContentValues cv = new ContentValues();
			cv.put(KEY_ROUNDID, i);
			cv.put(KEY_PLAYERID, noOfPlayer-1);
			cv.put(KEY_POINT, get_total_maal(i)*(-1));
			cv.put(KEY_MAAL, 0);
			cv.put(KEY_WON, 0);
			
			long response = ourDatabase.insert(DATABASE_TABLE, null, cv);
//			Log.i("inserted new users", String.valueOf(response));
		}
		
	}

	public ArrayList<HashMap<String, Integer>> get_data_of_round(int round) {
		ArrayList<HashMap<String, Integer>> arraylist = new ArrayList<HashMap<String,Integer>>();
		
		Cursor c = ourDatabase.query(DATABASE_TABLE, new String[]{KEY_PLAYERID,KEY_POINT, KEY_MAAL, KEY_WON}, KEY_ROUNDID+"=?", new String[]{ String.valueOf(round)}, null, null, null);
		for(c.moveToFirst();!c.isAfterLast(); c.moveToNext()){
			int playerid = c.getInt(c.getColumnIndex(KEY_PLAYERID));
			int point = c.getInt(c.getColumnIndex(KEY_POINT));
			int maal = c.getInt(c.getColumnIndex(KEY_MAAL));
			int won = c.getInt(c.getColumnIndex(KEY_WON));
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			map.put("playerid", playerid);
			map.put("point", point);
			map.put("maal", maal);
			map.put("won", won);
			
			arraylist.add(map);
//			
//			Log.i("Round","round:"+round);
//			Log.i("data of round", "Player id:"+playerid+" point:"+point+" maal:"+maal+" Won:"+won);
//			
		}
			
		
		return arraylist;
	}
	
	public long deleteround(int round){
		ContentValues cv = new ContentValues();
		cv.put(KEY_POINT, 0);
		cv.put(KEY_MAAL, 0);
		
		return ourDatabase.update(DATABASE_TABLE, cv, KEY_ROUNDID+"=?", new String[]{String.valueOf(round)});
		
//		return ourDatabase.delete(DATABASE_TABLE, KEY_ROUNDID+"=?", new String[]{String.valueOf(round)});
	}
}
