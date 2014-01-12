package com.vivek.marriage_center_collection;

public class valuebook {

	static int no_of_player;
	static float valuepoint;
	static int gamemode; //0 for FreeMode and 1 for FixedMode
	static int upperlimit;
	static int lowerlimit;
	static int round = 1;
	
	static String[] nameofplayer; //array position is the id of player, nameofplayer[1] is name value
	
	static void setplayer_name(String[] name, int i)
	{
		nameofplayer = new String[i];
		for(int j=0; j<i; j++)
		{
			nameofplayer[j] = name[j];
		}
		
	}
	
	static String[] get_playername()
	{
		return nameofplayer;
	}
	
	static int getRound()
	{
		return round;
	}
	
	static void Roundup()
	{
		round++;
	}
	
	static void Rounddown()
	{
		round--;
	}
}
