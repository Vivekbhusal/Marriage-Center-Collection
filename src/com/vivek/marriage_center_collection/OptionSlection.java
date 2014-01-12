package com.vivek.marriage_center_collection;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class OptionSlection extends Activity {

	RadioButton freepoint, fixedpoint;
	RadioGroup gamemode_radioGroup;
	Spinner playernumber;
	EditText pointvalue, upperlimit, lowerlimit;
	Button continuebotton;
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.optionselection);
		
		
		
		  Typeface face=Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Bold.ttf");
		  Typeface face1=Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Regular.ttf");
		  
		  	playernumber = (Spinner) findViewById(R.id.Spinner_playerNo);
		  	pointvalue = (EditText) findViewById(R.id.EDpointvalue); 
		  	continuebotton = (Button) findViewById(R.id.continueButton);
		  	lowerlimit = (EditText) findViewById(R.id.lowerlimit);
		  	upperlimit = (EditText) findViewById(R.id.upperlimit);
	    	        
	        TextView title = (TextView) findViewById(R.id.title);
	        TextView TVplayer = (TextView) findViewById(R.id.TVplayerdet);
	        TextView TVinfo = (TextView) findViewById(R.id.TVinfo);
	        TextView TVinfo2 = (TextView) findViewById(R.id.TVinfo2);
	        TextView TVinfo3 = (TextView) findViewById(R.id.TVinfo3);
	        TextView TVinfo4 = (TextView) findViewById(R.id.TVinfo4);
	        TextView TVinfo6 = (TextView) findViewById(R.id.TVinfo6);
	        
	        title.setTypeface(face);
	        TVplayer.setTypeface(face1);
	        TVinfo.setTypeface(face1);
	        TVinfo2.setTypeface(face);
	        TVinfo3.setTypeface(face);
	        TVinfo4.setTypeface(face1);
	        TVinfo6.setTypeface(face1);
	        
	        
	        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
	                R.array.playernoarray, android.R.layout.simple_spinner_item);
	        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	        
	        playernumber.setAdapter(adapter);
	        
		  	gamemode_radioGroup = (RadioGroup) findViewById(R.id.Gamemode_radioGroup);
	        freepoint = (RadioButton) findViewById(R.id.radioButton1);
	        fixedpoint = (RadioButton) findViewById(R.id.radioButton2);
	        
	        freepoint.setTypeface(face1);
	        fixedpoint.setTypeface(face1);
	        
	       gamemode_radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
			
				if(checkedId == R.id.radioButton1)
				{
					lowerlimit.setEnabled(false);
					upperlimit.setEnabled(false);
				}
				else{
					lowerlimit.setEnabled(true);
					upperlimit.setEnabled(true);
				}
			}
		});
	        
	        
	        
	        Button ContinueButton = (Button) findViewById(R.id.continueButton);
	        ContinueButton.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					
					String pointforvalue = null;	
					try{
						pointforvalue = pointvalue.getText().toString();
					}catch(Exception e)
					{
						System.out.println(e.toString());
					}
					
					if(pointforvalue.matches("") || Float.parseFloat(pointforvalue)<=0.0)
					{
						Toast.makeText(getApplicationContext(), "Oops Mike... You missed to set value per point",Toast.LENGTH_LONG).show();
					}
					else{
						int counter =0;
						String numberofplayer = playernumber.getSelectedItem().toString();
						int selectedoption = gamemode_radioGroup.getCheckedRadioButtonId();
						int game_mode, lower_limit = 0, upper_limit = 0;
						
						if(selectedoption == R.id.radioButton1)
						{
							game_mode = 0;
							lower_limit = 0;
							upper_limit = 0;
						}else{
							game_mode = 1;
							try{
								String Llimit = lowerlimit.getText().toString();
								String Ulimit = upperlimit.getText().toString();
								
								if(Llimit.matches("") || Ulimit.matches("")){
									counter=1;
									Toast.makeText(getApplicationContext(), "Oops Mike... You missed to set Lower/Upper limit value",Toast.LENGTH_LONG).show();
								}
								else{
									lower_limit = Integer.parseInt(Llimit);
									upper_limit = Integer.parseInt(Ulimit);
									if(upper_limit<= lower_limit)
									{
										counter =1;
										Toast.makeText(getApplicationContext(), "Lower limit cant be greater then Upper Limit", Toast.LENGTH_SHORT).show();
									}
								}
							}catch(Exception e)
							{
								System.out.println(e.toString());
							}
						}
						
						if(counter==0)
						{
							ShareManager SM = new ShareManager(OptionSlection.this);
							SM.setGameMode(game_mode);
							SM.setNoOfPlayer(Integer.parseInt(numberofplayer));
							SM.setUpperLimit(upper_limit);
							SM.setLowerLimit(lower_limit);
							SM.setValuePoint(Float.parseFloat(pointforvalue));
//							valuebook.no_of_player = Integer.parseInt(numberofplayer);
//							valuebook.gamemode = game_mode;
//							valuebook.upperlimit = upper_limit;
//							valuebook.lowerlimit = lower_limit;
//							valuebook.valuepoint = Float.parseFloat(pointforvalue);
							
							CenterCollectionDatabase db = new CenterCollectionDatabase(OptionSlection.this);
							db.open();	
								db.delete_database();
							db.close();
							SM.setround(1);
							
							
							Intent i = new Intent(OptionSlection.this, PlayerEntry.class);
							startActivity(i);
							overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
							finish();
							
						}
						
						
						
					}
					
				}
			});
	        
	}

	
}
