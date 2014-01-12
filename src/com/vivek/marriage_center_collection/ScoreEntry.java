package com.vivek.marriage_center_collection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.SQLException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ScoreEntry extends Activity {

	int number_of_player;
//	String[] player_name;
	List<String> playersname;
	EditText[] ETpoint;
	EditText[] ETmaal;
	int selectedID;
	
	Spinner[] PointSpinner;
	
	List<RadioButton> radioButtons;
	ShareManager SM;
	CenterCollectionDatabase db;
	Bundle bundle;
	
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
		setContentView(R.layout.point_entry);
		SM = new ShareManager(ScoreEntry.this);
		
		db = new CenterCollectionDatabase(ScoreEntry.this);
		ArrayList<HashMap<String, Integer>> roundArrayList = null;
		
		bundle = getIntent().getExtras();
		if(bundle!=null){
			if(bundle.containsKey("round")){
				int round = bundle.getInt("round");
				db.open();
				roundArrayList = db.get_data_of_round(round);
				db.close();
			}
		}
		
		
		number_of_player = SM.getNoOfPlayer();
		playersname = SM.getNameOfplayers();
		
		Typeface face=Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Bold.ttf");
		Typeface face1=Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Regular.ttf");
		
		TextView title = (TextView) findViewById(R.id.title);
		TextView info = (TextView) findViewById(R.id.TVinfo);
		Button save = (Button) findViewById(R.id.saveButton);
		
		title.setTypeface(face);
		info.setTypeface(face1);
		save.setTypeface(face);
		
		LinearLayout linearlayout = (LinearLayout) findViewById(R.id.player_point_entry);
		
		TextView[] TVplayer = new TextView[number_of_player];
		ETpoint = new EditText[number_of_player];
		ETmaal = new EditText[number_of_player];
		RadioButton[] won = new RadioButton[number_of_player];
		
		PointSpinner = new Spinner[number_of_player];			
		List<String> point_list = new ArrayList<String>();
		point_list.add("0");
		point_list.add(String.valueOf(SM.getLowerLimit()));
		point_list.add(String.valueOf(SM.getUpperLimit()));
		
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, point_list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		LinearLayout[] dyn_linearLayout = new LinearLayout[number_of_player];
		
		radioButtons = new ArrayList<RadioButton>();

		for(int i =0; i<number_of_player; i++)
		{
			dyn_linearLayout[i] = new LinearLayout(this);
			dyn_linearLayout[i].setPadding(5, 5, 5, 5);
			dyn_linearLayout[i].setOrientation(LinearLayout.HORIZONTAL);
			dyn_linearLayout[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			
				TVplayer[i] = new TextView(this);
					TVplayer[i].setText(playersname.get(i));
					TVplayer[i].setTypeface(face);
					TVplayer[i].setTextSize(15);
					TVplayer[i].setTextColor(Color.BLACK);
					TVplayer[i].setLines(1);
					TVplayer[i].setLayoutParams(new LinearLayout.LayoutParams(70, LayoutParams.WRAP_CONTENT,1f));
					
				ETpoint[i] = new EditText(this);
					if(bundle==null)
						ETpoint[i].setHint("Point");
					else
						ETpoint[i].setText(String.valueOf(roundArrayList.get(i).get("point")));
					
					ETpoint[i].setInputType(InputType.TYPE_CLASS_NUMBER);
					ETpoint[i].setLayoutParams(new LinearLayout.LayoutParams(70, LayoutParams.WRAP_CONTENT, 1f));
										
					
				PointSpinner[i] = new Spinner(this);
					PointSpinner[i].setLayoutParams(new LinearLayout.LayoutParams(90, LayoutParams.WRAP_CONTENT, 1f));
					PointSpinner[i].setAdapter(dataAdapter);
					
					
				ETmaal[i] = new EditText(this);
				if(bundle==null)
					ETmaal[i].setHint("Maal");
				else
					ETmaal[i].setText(String.valueOf(roundArrayList.get(i).get("maal")));
				
					ETmaal[i].setInputType(InputType.TYPE_CLASS_NUMBER);
					ETmaal[i].setLayoutParams(new LinearLayout.LayoutParams(70, LayoutParams.WRAP_CONTENT, 1f));
					
					// start from here
				won[i] = new RadioButton(this);
					won[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
					if(bundle != null){
						if(roundArrayList.get(i).get("won") == 1){
							won[i].setChecked(true);
						}else{
							won[i].setChecked(false);
						}
					}
					
					
					won[i].setId(100+i);
										
				radioButtons.add(won[i]);
				
				dyn_linearLayout[i].addView(TVplayer[i]);
				
				if(bundle == null){
					if(SM.getGameMode()==0) //free
					{
						dyn_linearLayout[i].addView(ETpoint[i]);
					}
					else
					{
						dyn_linearLayout[i].addView(PointSpinner[i]);
						
					}
				}else{
					dyn_linearLayout[i].addView(ETpoint[i]);
				}
				
					
				dyn_linearLayout[i].addView(ETmaal[i]);
				dyn_linearLayout[i].addView(won[i]);
				
				linearlayout.addView(dyn_linearLayout[i]);
			
		}
		
		selectedID =100;
		try{
			
			if(bundle == null){
				won[0].setChecked(true);
				if(SM.getGameMode()==0)
				{
					ETpoint[0].setText("0");
					ETpoint[0].setEnabled(false);
				}else{
					
					PointSpinner[0].setSelection(0);
					PointSpinner[0].setEnabled(false);
				}
			}
			
		}catch(ArrayIndexOutOfBoundsException e)
		{
			Log.i("arrayout of bound in scoreentry", e.toString());
		}
			
		
		
		
		for (RadioButton button : radioButtons){

		    button.setOnCheckedChangeListener(new OnCheckedChangeListener(){
		        
		        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		            if (isChecked)
		            	processRadioButtonClick(buttonView);
		        }

				private void processRadioButtonClick(CompoundButton buttonView) {
					for (RadioButton button : radioButtons){

				        if (button != buttonView ) 
				        {
				        	button.setChecked(false);
				        	int id = button.getId();
				        	int position = id-100;
				        	
				        	if(bundle!=null || SM.getGameMode()==0)
				        	{
				        		ETpoint[position].setEnabled(true);
					        	ETpoint[position].setText("");
				        	}else{
//				        		PointSpinner[position].setSelection(1);
				        		PointSpinner[position].setEnabled(true);
				        	}
				        	
				        }
				        else{
				        	selectedID = button.getId();
				        	int val = selectedID-100;
				        	if(bundle!=null || SM.getGameMode() == 0)
				        	{
				        		ETpoint[val].setText("0");
				        		ETpoint[val].setEnabled(false);
				        	}
				        	else{
				        		PointSpinner[val].setSelection(0);
				        		PointSpinner[val].setEnabled(false);
				        	}
				        		
				        }
				        	
				    }
					
				}   
		    });
		    
		    

		}
		
				
		save.setOnClickListener(new View.OnClickListener() {
			
			int round;
			int playerid;
			int point;
			int maal;
			int won_who;
			
			String[] Spoint;
			String[] Smaal;
			
			int counter;
			
			public void onClick(View v) {
				
				if(bundle!=null && bundle.containsKey("round")){
					round = bundle.getInt("round");
				}else{
					round = SM.getround();
				}
				
				counter=0;
				
				Spoint = new String[number_of_player];
				Smaal = new String[number_of_player];
				
				for(int i=0; i<number_of_player; i++){
					
					if(bundle!=null || SM.getGameMode()==0)
					{
						Spoint[i] = ETpoint[i].getText().toString();						
					}else{
						Spoint[i] =  PointSpinner[i].getSelectedItem().toString();
					}
					
					Smaal[i] = ETmaal[i].getText().toString();
					
					if(Spoint[i].matches("") || Smaal[i].matches("")|| Integer.parseInt(Smaal[i]) ==1 )
					{
						counter++;
//						ETpoint[i].setError("Cant be null");
						ETmaal[i].setError("Can't be blank or 1");
//						if(valuebook.gamemode ==0)
//						{
//							Drawable d = getResources().getDrawable(R.drawable.error_edittext);
//							ETpoint[i].setBackgroundDrawable(d);
//							
//						}
						//blank field lai red mark garne..
					}
					
				}
				
				if(counter>0)
				{
					Toast.makeText(getApplicationContext(), "Oops Mike! Either there is blank field or error with entry(maal should be >1)", Toast.LENGTH_SHORT).show();
				}else{
					
					try{
							
							db.open();
							int wnID = selectedID-100;
							
							for(int i=0; i<number_of_player; i++)
							{
								playerid = i;
								point = Integer.parseInt(Spoint[i]);
								maal = Integer.parseInt(Smaal[i]);
									if(i == wnID)
									{
										won_who = 1;
									}
									else{
										won_who = 0;
									}
													
									if(bundle==null){
										db.add_score(round, playerid, point, maal, won_who);
										
									}else{
										db.update_score(round, playerid, point, maal, won_who);
									}
								
							
							}
							
							if(bundle==null)
								SM.increaseRound();
							
							
							Toast.makeText(getApplicationContext(), "Record Sucessfully Saved", Toast.LENGTH_SHORT).show();
							
							Intent intent = new Intent(ScoreEntry.this, ScoreBoard.class);
							startActivity(intent);
							overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right );
							finish();
						
						}catch(SQLException e){
							 
							if(bundle == null)
								SM.decreaseRound();
							Toast.makeText(getApplicationContext(), "Oh shit!!! Applicaiton crashed, Hope my data are safe", Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(ScoreEntry.this, ScoreBoard.class);
							startActivity(intent);
							finish();
						}finally{
							db.close();
						}
						
				}
				
			}
		});
		
	}

	@Override
	public void onBackPressed() {
//		super.onBackPressed();
		Intent intent = new Intent(ScoreEntry.this, ScoreBoard.class);
		startActivity(intent);
		finish();
	}
	

}
