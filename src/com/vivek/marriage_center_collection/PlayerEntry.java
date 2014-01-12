package com.vivek.marriage_center_collection;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PlayerEntry extends Activity{

	EditText[] nameEDIT;
	LinearLayout Layout;
	int number_of_player;
	ShareManager SM;
	
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
		setContentView(R.layout.playerentry);
		
		SM = new ShareManager(PlayerEntry.this);
		
		Typeface face=Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Bold.ttf");
		 
		TextView title = (TextView) findViewById(R.id.title);
		TextView info = (TextView) findViewById(R.id.TVinfo21);
		Button continuebutton = (Button) findViewById(R.id.continueButton);
				
		title.setTypeface(face);
		info.setTypeface(face);
		continuebutton.setTypeface(face);
	
		number_of_player = SM.getNoOfPlayer();
			
		Layout = (LinearLayout) findViewById(R.id.playernameentrylist);
		
		nameEDIT = new EditText[number_of_player];
		
		for(int i = 0; i<number_of_player; i++)
		{
			nameEDIT[i] = new EditText(this);
			nameEDIT[i].setTag("playerID"+i);
			nameEDIT[i].setId(100+i);
			nameEDIT[i].setSingleLine();
			nameEDIT[i].setHint("Player "+(i+1));
			nameEDIT[i].setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, 100));
			Layout.addView(nameEDIT[i]);
			
			
		}
		
		
		continuebutton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				int c= 0;
				List<String> playersname = new ArrayList<String>();
				String name;
				
				for(int i=0; i<number_of_player; i++)
				{	
					name =  nameEDIT[i].getText().toString();
					playersname.add(name);
					if(name.matches(""))
					{
						c++;
//						Toast.makeText(getApplicationContext(), "empty", Toast.LENGTH_SHORT).show();
					}
					
				}
				
				
				
				if(c!=0)
				{
					Toast.makeText(getApplicationContext(), "Oops Mike... you missed to enter player's name", Toast.LENGTH_SHORT).show();
				}
				else{
					SM.setNameOfplayers(playersname);
					Intent in = new Intent(PlayerEntry.this, ScoreBoard.class);
					startActivity(in);
					overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
					finish();
					
				}
				
				
			}
		});
		
				
	}

}
