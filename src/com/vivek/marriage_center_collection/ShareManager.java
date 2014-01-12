package com.vivek.marriage_center_collection;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class ShareManager {
	SharedPreferences prefs;
	
	static String key_no_ofplayer = "no_of_player";
	static String key_valuepoint = "valuepoint";
	static String key_gamemode = "gamemore";
	static String key_upperlimit = "upperlimit";
	static String key_lowerlimit = "lowerlimit";
	static String key_round = "round";
	static String key_nameofplayers = "nameofplayers";
	static String key_addeduser = "addeduser";
	
	Editor edit;
	public ShareManager(Activity a){
		prefs = a.getSharedPreferences("centercollection", 0);
	}
	
	/***** Setter ******/
	public void setNoOfPlayer(int i){
		edit = prefs.edit();
		edit.putInt(key_no_ofplayer, i);
		edit.commit();
	}
	
	public void setValuePoint(Float i){
		edit = prefs.edit();
		edit.putFloat(key_valuepoint, i);
		edit.commit();
	}
	
	public void setGameMode(int i){
		edit = prefs.edit();
		edit.putInt(key_gamemode, i);
		edit.commit();
	}
	
	public void setUpperLimit(int i){
		edit = prefs.edit();
		edit.putInt(key_upperlimit, i);
		edit.commit();
	}
	
	public void setLowerLimit(int i){
		edit = prefs.edit();
		edit.putInt(key_lowerlimit, i);
		edit.commit();
	}
	
	// just comments
	
	public void setround(int i){
		edit = prefs.edit();
		edit.putInt(key_round, i);
		edit.commit();
	}
	
	public void setNameOfplayers(List<String> name){
		JSONArray jsonar = new JSONArray();
		for (String string : name) {
			jsonar.put(string);
		}
		
		String listname = jsonar.toString();
		Log.i("saved name", listname);
		edit = prefs.edit();
		edit.putString(key_nameofplayers, listname);
		edit.commit();
	}
	
	public void setDeletedRound(int round){
		String stringjsonarray = prefs.getString("deleteround", null);
		JSONArray deleteroundJson = null;
		try {
			if(stringjsonarray!=null)
				deleteroundJson = new JSONArray(stringjsonarray);
			else
				deleteroundJson = new JSONArray();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		deleteroundJson.put(round);
			
		edit = prefs.edit();
		edit.putString("deleteround", deleteroundJson.toString());
		edit.commit();
		
	}
	
	public int getNoofDeletedRounds(){
		String stringjsonarray = prefs.getString("deleteround", null);
		JSONArray deleteroundJson = null;
		try {
			if(stringjsonarray!=null)
				deleteroundJson = new JSONArray(stringjsonarray);
			else
				deleteroundJson = new JSONArray();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return deleteroundJson.length();
	}
	
	
	public void removeDeleteround(){
		edit = prefs.edit();
		edit.putString("deleteround", null);
		edit.commit();
	}
	
	public String getDeletedRound(){
		return prefs.getString("deleteround", null);
	}
	
	public void increaseUser(){
		//Increase added users number, as it will be useful to calculate the score
		int no_user = prefs.getInt(key_addeduser, 0);
		edit = prefs.edit();
		edit.putInt(key_addeduser, no_user+1);
		edit.commit();
		
		//Also increase total numberof user
		int olduser = getNoOfPlayer();
		setNoOfPlayer(olduser+1);
		
	}
	
	public void deleteaddeduser(){
		edit = prefs.edit();
		edit.putString(key_addeduser, null);
		edit.commit();
	}
	
/******* GETTER ************/
	
	public int getIncreasedUser(){
		return prefs.getInt(key_addeduser, 0);
	}
	
	public int getNoOfPlayer(){
		return prefs.getInt(key_no_ofplayer, 0);
	}
	
	public Float getValuePoint(){
		return prefs.getFloat(key_valuepoint, 0);
	}
	
	public int getGameMode(){
		return prefs.getInt(key_gamemode, 0);
	}
	
	public int getUpperLimit(){
		return prefs.getInt(key_upperlimit, 0);
		
	}
	
	public int getLowerLimit(){
		return prefs.getInt(key_lowerlimit, 0);
	}
	
	public int getround(){
		return prefs.getInt(key_round, 1);
	}
	
	public List<String> getNameOfplayers(){
		List<String> names = new ArrayList<String>();
		String namelist = prefs.getString(key_nameofplayers, null);
		try {
			JSONArray jsonarr = new JSONArray(namelist);
			for (int i=0; i<jsonarr.length();  i++) {
				names.add(jsonarr.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return names;
		
		
	}
	
/******FUNCTIONS*********/
	public void increaseRound(){
		int i = getround()+1;
		edit = prefs.edit();
		edit.putInt(key_round, i);
		edit.commit();
		
	}
	public void decreaseRound(){
		int i = getround()-1;
		edit = prefs.edit();
		edit.putInt(key_round, i);
		edit.commit();
		
	}
	
	
	
	

}
