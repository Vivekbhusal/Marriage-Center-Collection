package com.vivek.marriage_center_collection;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.vivek.marriage_center_collection.R.color;

public class ScoreBoard extends Activity {

	int number_of_player;

	// String[] player_name;
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

	TextView[] TVheader;
	Typeface face, face1;
	int round_number;
	ShareManager SM;
	List<String> namelist;
	ImageView adduser, menubar;
	CenterCollectionDatabase db;
	JSONArray deleteround;
	String Stringdeletedround;
	int numberofDeletedROunds;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.scoreboard);
		SM = new ShareManager(ScoreBoard.this);

		number_of_player = SM.getNoOfPlayer();
		namelist = SM.getNameOfplayers();
		round_number = SM.getround();
		db = new CenterCollectionDatabase(ScoreBoard.this);
		
		Stringdeletedround = SM.getDeletedRound();
		numberofDeletedROunds = SM.getNoofDeletedRounds();
		
		if(Stringdeletedround!=null){
//			Log.i("deleted round", Stringdeletedround);
			try {
				deleteround = new JSONArray(Stringdeletedround);
			} catch (JSONException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		

		face = Typeface.createFromAsset(getAssets(), "fonts/Ubuntu-Bold.ttf");
		face1 = Typeface.createFromAsset(getAssets(),
				"fonts/Ubuntu-Regular.ttf");

		TextView title = (TextView) findViewById(R.id.title);
		TextView scoreboard_title = (TextView) findViewById(R.id.Scoreboard_title);
		Button endgame = (Button) findViewById(R.id.Button_endgame);
		Button addscore = (Button) findViewById(R.id.Button_addscore);
		TextView roundTV = (TextView) findViewById(R.id.roundTV);
		TextView TotalTV = (TextView) findViewById(R.id.totalTV);
		menubar = (ImageView) findViewById(R.id.menubar);
		menubar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				RelativeLayout parent = (RelativeLayout) findViewById(R.id.scorecardParent);
				Bitmap bitmap;
				View v1 = (View) parent;
				v1.setDrawingCacheEnabled(true);
				bitmap = Bitmap.createBitmap(v1.getDrawingCache());
				v1.setDrawingCacheEnabled(false);
				Calendar cal = Calendar.getInstance();

				String file_path = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/marriagecentercollection";
				File dir = new File(file_path);
				if (!dir.exists())
					dir.mkdirs();

				File file = new File(dir, "game" + cal.getTimeInMillis()
						+ ".png");
				FileOutputStream fOut;
				try {
					fOut = new FileOutputStream(file);
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
					fOut.flush();
					fOut.close();
				} catch (FileNotFoundException e1) {
					e1.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("image/png");
				share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
				share.putExtra(Intent.EXTRA_TEXT,
						"ScoreBoard from Marriage Center Collection");
				startActivity(Intent.createChooser(share,
						"Share Screenshot of Game"));
			}
		});

		adduser = (ImageView) findViewById(R.id.adduser);

		adduser.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// dialog to add user
				final Dialog dialog = new Dialog(ScoreBoard.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.custom_dialog_adduser);
				Button Badd = (Button) dialog.findViewById(R.id.Add_user);
				Button Bcancel = (Button) dialog.findViewById(R.id.CANCEL);
				final EditText ETname = (EditText) dialog
						.findViewById(R.id.ETnewuser);

				Badd.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						String newname = ETname.getText().toString();
						List<String> oldnamelist = SM.getNameOfplayers();
						if (newname.length() < 1) {
							Toast.makeText(getApplicationContext(),
									"Please fill the name to add",
									Toast.LENGTH_LONG).show();
						} else if (oldnamelist.contains(newname)) {
							Toast.makeText(getApplicationContext(),
									"Name already exists", Toast.LENGTH_LONG)
									.show();
						} else {
							oldnamelist.add(newname);
							SM.setNameOfplayers(oldnamelist);
							if (round_number != 1) {
								SM.increaseUser();
								db.open();
								db.insertnewuser(SM.getNoOfPlayer(),
										round_number);
								db.close();
							} else {
								int oldenumberofuser = SM.getNoOfPlayer();
								SM.setNoOfPlayer(oldenumberofuser + 1);
							}

							dialog.dismiss();
							// SM.setround(round_number);
							ScoreBoard.this.finish();
							startActivity(getIntent().setFlags(
									Intent.FLAG_ACTIVITY_CLEAR_TOP));
						}
					}
				});

				Bcancel.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});

				dialog.show();

			}
		});

		roundTV.setTypeface(face);
		TotalTV.setTypeface(face);

		title.setTypeface(face);
		scoreboard_title.setTypeface(face);
		endgame.setTypeface(face);
		addscore.setTypeface(face);

		/**************************** scoreboard heading works **********************************************/
		TableRow row_header = (TableRow) findViewById(R.id.row_header);

		TVheader = new TextView[number_of_player];

		for (int i = 0; i < number_of_player; i++) {
			TVheader[i] = new TextView(this);
			TVheader[i].setText(namelist.get(i));
			TVheader[i].setLayoutParams(new TableRow.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1f));
			TVheader[i].setTextColor(Color.BLACK);
			TVheader[i].setTypeface(face);
			TVheader[i].setTextSize(13);
			TVheader[i].setGravity(Gravity.CENTER);

			row_header.addView(TVheader[i]);
		}

		/**************************** scoreboard body works **********************************************/

		db.open();

		int num = db.ifdata_in_table();

		if (num > 0) {
			float point = SM.getValuePoint();
			float[][] amount_ofplayer = new float[round_number][number_of_player];

			for (int i = 1; i < round_number; i++) {
//				if(Stringdeletedround.contains(String.valueOf(i))){
//					continue;
//				}
				int winnerID_ofround = db.check_winner(i);
				int totalmaal_ofround = db.get_total_maal(i);

				for (int j = 0; j < number_of_player; j++) {
					int point_of_player = db.point_of_player((i), j);
					int maal_of_player = db.maal_of_player((i), j);

					if (j != winnerID_ofround) {

						amount_ofplayer[i][j] = (maal_of_player
								* number_of_player - totalmaal_ofround
								- point_of_player - (SM.getIncreasedUser() * maal_of_player))
								* point;
					}

				}
				amount_ofplayer[i][winnerID_ofround] = calculate_amount_of_winner(
						amount_ofplayer, winnerID_ofround, i);

			}
			int[] winnerlist = db.getting_all_winner();
			embedd_to_scoreboard(amount_ofplayer, winnerlist);
			display_footer(amount_ofplayer);

		}

		db.close();

		/**************************** scoreboard Footer work **********************************************/

		/**************************** Action work for Add point and End game Buttons **********************/

		addscore.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				Intent intent = new Intent(ScoreBoard.this, ScoreEntry.class);
				startActivity(intent);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
				finish();

			}
		});

		endgame.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				final Dialog dialog = new Dialog(ScoreBoard.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.endup_dialog);

				TextView up_notice = (TextView) dialog
						.findViewById(R.id.up_notice);
				TextView down_notice = (TextView) dialog
						.findViewById(R.id.down_notice);

				up_notice.setTypeface(face);
				down_notice.setTypeface(face1);

				Button quit = (Button) dialog.findViewById(R.id.QUIT);
				Button cancel = (Button) dialog.findViewById(R.id.CANCEL);
				Button continue_later = (Button) dialog
						.findViewById(R.id.CONTINUE_LATER);

				quit.setTypeface(face1);
				cancel.setTypeface(face1);
				continue_later.setTypeface(face1);

				quit.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						CenterCollectionDatabase db = new CenterCollectionDatabase(
								ScoreBoard.this);
						db.open();
						db.delete_database();
						db.close();
						dialog.dismiss();
						finish();
						startActivity(new Intent(ScoreBoard.this,
								MainActivity.class));

					}
				});

				cancel.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						dialog.dismiss();

					}
				});

				continue_later.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {

						finish();
						dialog.dismiss();
					}
				});

				dialog.show();

			}
		});
	}

	int[] colorarray = { Color.parseColor("#cccccc"),
			Color.parseColor("#b4b4b4") };

	private void embedd_to_scoreboard(float[][] amount_ofplayer,
			int[] winnerlist) {

		TableLayout tablelayout = (TableLayout) findViewById(R.id.scoreboard_body);
		tablelayout.removeAllViews();

		TextView[][] score = new TextView[round_number][number_of_player];
		TableRow[] dyn_row = new TableRow[round_number];
		TextView[] roundView = new TextView[round_number];

		for (int i = 1; i < round_number; i++) {
//			if(Stringdeletedround.contains(String.valueOf(i))){
//				continue;
//			}
			
			dyn_row[i] = new TableRow(this);
			dyn_row[i].setLayoutParams(new TableLayout.LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));

			roundView[i] = new TextView(this);
			roundView[i].setText(String.valueOf(i));
			roundView[i].setPadding(8, 0, 0, 0);
			roundView[i].setLayoutParams(new TableRow.LayoutParams(0, 35, 1f));
			roundView[i].setTextColor(Color.rgb(68, 43, (int) 0F));
			roundView[i].setTypeface(face);
			roundView[i].setTextSize(15);
			// roundView[i].setBackgroundResource(R.drawable.tablegrid);
			
//			Log.i("scoreboard", "Round "+i+"  "+namelist.get(i));

			dyn_row[i].addView(roundView[i]);

			for (int j = 0; j < number_of_player; j++) {
				score[i][j] = new TextView(this);
				score[i][j].setText(String.valueOf(String.valueOf(amount_ofplayer[i][j])));
				score[i][j]
						.setLayoutParams(new TableRow.LayoutParams(0, 35, 1f));
				score[i][j].setTextColor(Color.rgb(00, 00, 00));
				score[i][j].setTypeface(face);
				score[i][j].setGravity(Gravity.CENTER);
				score[i][j].setTextSize(15);
				// score[i][j].setBackgroundResource(R.drawable.tablegrid);

				dyn_row[i].addView(score[i][j]);
				Log.i(namelist.get(j), String.valueOf(String.valueOf(amount_ofplayer[i][j])) );

			}

			final int l = i;

			dyn_row[i].setBackgroundColor(colorarray[i % 2]);
			dyn_row[i].setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
//					Toast.makeText(getApplicationContext(), String.valueOf(l),
//							Toast.LENGTH_SHORT).show();
					db.open();
					ArrayList<HashMap<String, Integer>> roundArrayList = db
							.get_data_of_round(l);
					db.close();
					final Dialog dialog = new Dialog(ScoreBoard.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.point_update);
					
					TextView info = (TextView) dialog.findViewById(R.id.TVinfo);
					info.setText("Score Details of Round "+l);
					LinearLayout linearlayout = (LinearLayout) dialog.findViewById(R.id.player_point_entry);

					/*************** Dialog to show points and all of round ***************************/
					TextView[] TVplayer = new TextView[number_of_player];
					TextView[] ETpoint = new TextView[number_of_player];
					TextView[] ETmaal = new TextView[number_of_player];
					RadioButton[] won = new RadioButton[number_of_player];

					LinearLayout[] dyn_linearLayout = new LinearLayout[number_of_player];
					for (int i = 0; i < number_of_player; i++) {
						dyn_linearLayout[i] = new LinearLayout(ScoreBoard.this);
						dyn_linearLayout[i].setPadding(5, 5, 5, 5);
						dyn_linearLayout[i].setOrientation(LinearLayout.HORIZONTAL);
						dyn_linearLayout[i].setLayoutParams(new LinearLayout.LayoutParams(
										LayoutParams.FILL_PARENT,
										LayoutParams.WRAP_CONTENT));

						TVplayer[i] = new TextView(ScoreBoard.this);
						TVplayer[i].setText(namelist.get(i));
						TVplayer[i].setTypeface(face);
						TVplayer[i].setTextSize(15);
						TVplayer[i].setTextColor(Color.BLACK);
						TVplayer[i].setLines(1);
						TVplayer[i].setLayoutParams(new LinearLayout.LayoutParams(
										70, LayoutParams.WRAP_CONTENT, 1f));

						ETpoint[i] = new TextView(ScoreBoard.this);
						ETpoint[i].setText(String.valueOf(roundArrayList.get(i).get("point")));
						ETpoint[i].setTextColor(Color.BLACK);
						ETpoint[i].setLayoutParams(new LinearLayout.LayoutParams(70, LayoutParams.WRAP_CONTENT, 1f));

						ETmaal[i] = new TextView(ScoreBoard.this);
						ETmaal[i].setText(String.valueOf(roundArrayList.get(i).get("maal")));
						ETmaal[i].setTextColor(Color.BLACK);
						ETmaal[i].setLayoutParams(new LinearLayout.LayoutParams(70, LayoutParams.WRAP_CONTENT, 1f));

						won[i] = new RadioButton(ScoreBoard.this);
						won[i].setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
						
						if (roundArrayList.get(i).get("won") == 1) {
							won[i].setChecked(true);
						} else {
							won[i].setChecked(false);
						}
						
						won[i].setEnabled(false);
						
						dyn_linearLayout[i].addView(TVplayer[i]);
						dyn_linearLayout[i].addView(ETpoint[i]);
						dyn_linearLayout[i].addView(ETmaal[i]);
						dyn_linearLayout[i].addView(won[i]);

						linearlayout.addView(dyn_linearLayout[i]);

					}
					
					Button edit = (Button) dialog.findViewById(R.id.Edit);
					edit.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							dialog.dismiss();
							Intent intent = new Intent(ScoreBoard.this, ScoreEntry.class);
							intent.putExtra("round", l);
							startActivity(intent);
							overridePendingTransition(R.anim.in_from_right,
									R.anim.out_to_left);
							finish();
						}
					});
					
					Button delete = (Button) dialog.findViewById(R.id.Delete);
					delete.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							db.open();
							long response = db.deleteround(l);
							db.close();
							if(response!=-1){
								SM.setDeletedRound(l);
							}
							dialog.dismiss();
							finish();
							startActivity(getIntent());
						}
					});
					

					dialog.show();
				}
			});
			tablelayout.addView(dyn_row[i]);
		}

		for (int i = 1; i < round_number; i++) {
//			if(Stringdeletedround.contains(String.valueOf(i))){
//				continue;
//			}
			
			score[i][winnerlist[i - 1]].setBackgroundDrawable(getResources()
					.getDrawable(R.drawable.winnerred));
			score[i][winnerlist[i - 1]].setTextColor(Color.rgb(255, 255, 255));
		}

	}

	private void display_footer(float[][] amount_ofplayer) {

		// TableLayout footer = (TableLayout)
		// findViewById(R.id.scoreboard_footer);
		TableRow footer_row = (TableRow) findViewById(R.id.row_footer);

		TextView[] total_score = new TextView[number_of_player];
		float[] total_amt = new float[number_of_player];

		for (int i = 0; i < number_of_player; i++) {
			for (int j = 1; j < round_number; j++) {
//				if(Stringdeletedround.contains(String.valueOf(j))){
//					continue;
//				}
				total_amt[i] = total_amt[i] + amount_ofplayer[j][i];
			}

			total_score[i] = new TextView(this);
			total_score[i].setText(String.valueOf(total_amt[i]));
			total_score[i].setLayoutParams(new TableRow.LayoutParams(0,
					LayoutParams.WRAP_CONTENT, 1f));
			total_score[i].setTextColor(color.brown);
			total_score[i].setTypeface(face);
			total_score[i].setGravity(Gravity.CENTER);
			total_score[i].setTextSize(19);

			footer_row.addView(total_score[i]);

		}

		int highest_scorer = get_higest(total_amt);
		try {
			total_score[highest_scorer].setBackgroundColor(Color.rgb(222, 71,
					60));
			total_score[highest_scorer].setTextColor(Color.rgb(255, 255, 255));
		} catch (ArrayIndexOutOfBoundsException e) {
			Log.i("array out of bound exception", e.toString());
		}

	}

	public float calculate_amount_of_winner(float[][] amount, int winnerId,
			int round) {
		float amount_of_winner = 0;
		int negative = -1;
		for (int i = 0; i < number_of_player; i++) {
			if (i != winnerId) {
				amount_of_winner = amount_of_winner + amount[round][i];
			}

		}
		float winner_amt = amount_of_winner * negative;
		return (winner_amt);
	}

	public int get_higest(float[] amount) {
		float a = 0;
		int c = 0;
		int size = amount.length;
		// Log.i("Size of amount", String.valueOf(size));
		for (int i = 0; i < size; i++) {
			if (amount[i] > a) {
				a = amount[i];
				c = i;
			}
		}
		// Log.i("heighest position", String.valueOf(c));
		return c;
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		final Dialog dialog = new Dialog(ScoreBoard.this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.endup_dialog);

		TextView up_notice = (TextView) dialog.findViewById(R.id.up_notice);
		TextView down_notice = (TextView) dialog.findViewById(R.id.down_notice);

		up_notice.setTypeface(face);
		down_notice.setTypeface(face1);

		Button quit = (Button) dialog.findViewById(R.id.QUIT);
		Button cancel = (Button) dialog.findViewById(R.id.CANCEL);
		Button continue_later = (Button) dialog
				.findViewById(R.id.CONTINUE_LATER);

		quit.setTypeface(face1);
		cancel.setTypeface(face1);
		continue_later.setTypeface(face1);

		quit.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				CenterCollectionDatabase db = new CenterCollectionDatabase(
						ScoreBoard.this);
				db.open();
				db.delete_database();
				db.close();
				dialog.dismiss();
				finish();

			}
		});

		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		continue_later.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				finish();

			}
		});

		dialog.show();
	}

}
