package com.vivek.marriage_center_collection;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	Button startnewgame;
	int num;
	ShareManager SM;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		SM = new ShareManager(MainActivity.this);

		Display display = getWindowManager().getDefaultDisplay();
		// int width = display.getWidth(); // deprecated
		int height = display.getHeight();

		RelativeLayout upred = (RelativeLayout) findViewById(R.id.upred);
		upred.setLayoutParams(new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, height / 2));

		Typeface face = Typeface.createFromAsset(getAssets(),
				"fonts/Ubuntu-Bold.ttf");
		Typeface face1 = Typeface.createFromAsset(getAssets(),
				"fonts/Ubuntu-Regular.ttf");

		TextView name = (TextView) findViewById(R.id.name);
		name.setTypeface(face);

		startnewgame = (Button) findViewById(R.id.startgame_button);
		startnewgame.setTypeface(face);

		Button continuegame = (Button) findViewById(R.id.continuegame_button);
		continuegame.setTypeface(face);

		CenterCollectionDatabase db = new CenterCollectionDatabase(
				MainActivity.this);
		db.open();
		num = db.ifdata_in_table();
		Log.i("value of N", String.valueOf(num));
		db.close();

		if (num > 0) {
			TextView notification = (TextView) findViewById(R.id.notification);
			notification.setText("You have your previous game pending");
			notification.setTextSize(11);
			notification.setTypeface(face1);
			notification.setTextColor(Color.rgb(204, 204, 204));
		}

		startnewgame.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				if (num > 0) {
					final Dialog dialog = new Dialog(MainActivity.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.deletegame_dialog);
					Button start = (Button) dialog.findViewById(R.id.start);
					Button cancel = (Button) dialog.findViewById(R.id.cancel);
					start.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							CenterCollectionDatabase db = new CenterCollectionDatabase(
									MainActivity.this);
							db.open();
							db.delete_database();
							db.close();
							SM.removeDeleteround();
							SM.deleteaddeduser();

							dialog.dismiss();
							Intent i = new Intent(MainActivity.this, OptionSlection.class);
							startActivity(i);
							overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
							finish();
						}
					});
					
					cancel.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							dialog.dismiss();
						}
					});
					
					dialog.show();
					
				} else {
					CenterCollectionDatabase db = new CenterCollectionDatabase(
							MainActivity.this);
					db.open();
					db.delete_database();
					db.close();
					SM.removeDeleteround();
					SM.deleteaddeduser();

					Intent i = new Intent(MainActivity.this, OptionSlection.class);
					startActivity(i);
					overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
					finish();
				}

			}
		});

		continuegame.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (num == 0) {
					Toast.makeText(
							getApplicationContext(),
							"You have no previously saved game. Please start a game.",
							Toast.LENGTH_SHORT).show();
				} else {

					Intent i = new Intent(MainActivity.this, ScoreBoard.class);
					startActivity(i);
					overridePendingTransition(R.anim.in_from_right,
							R.anim.out_to_left);
					finish();
				}

			}
		});

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
