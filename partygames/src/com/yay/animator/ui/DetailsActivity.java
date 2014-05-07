package com.yay.animator.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.yay.animator.R;
import com.yay.animator.provider.GameDetailsQuery;
import com.yay.animator.provider.GamesContract.Games;
import com.yay.animator.provider.GamesContract.GamesColumns;
import com.yay.animator.provider.GamesContract.Pledge;
import com.yay.animator.provider.GamesContract.PledgeColumns;
import com.yay.animator.provider.GamesContract.Poetry;
import com.yay.animator.provider.GamesContract.PoetryColumns;
import com.yay.animator.provider.GamesQuery;
import com.yay.animator.provider.PledgeDetailsQuery;
import com.yay.animator.provider.PledgeQuery;
import com.yay.animator.provider.PoetryDetailsQuery;
import com.yay.animator.provider.PoetryQuery;
import com.yay.animator.util.CustomProgressDialog;
import com.yay.animator.util.NotifyingAsyncQueryHandler;
import com.yay.animator.util.NotifyingAsyncQueryHandler.AsyncQueryListener;
import com.yay.animator.util.UIUtils;


public class DetailsActivity extends Activity implements AsyncQueryListener {
	private int idp;
	private int type;
	private NotifyingAsyncQueryHandler mHandler;
	private CustomProgressDialog progressDialog;
	private Float fontSize;
	private int position = 1;
	private static final int SWIPE_MIN_DISTANCE = 120;
	private static final int SWIPE_MAX_OFF_PATH = 250;
	private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	private GestureDetector gestureDetector;
	    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.game_details);
	    initTitle();
	    mHandler = new NotifyingAsyncQueryHandler(getContentResolver(), this);
	    gestureDetector = new GestureDetector(new MyGestureDetector());
	    
	    Bundle extras = getIntent().getExtras();
	    if (extras != null) {
	    	idp = extras.getInt(Config.PARAM_IDP);
	    	type = extras.getInt(Config.PARAM_TYPE);
	        
		    switch(type) {
		    	case Config.GAME:
		    		mHandler.startQuery(GameDetailsQuery._TOKEN, Games.buildGameUri("" + extras.getInt(Config.PARAM_ID)), GameDetailsQuery.PROJECTION);
		    	    break;
		    	case Config.PLEDGE:
		    		mHandler.startQuery(PledgeDetailsQuery._TOKEN, Pledge.buildPledgeUri("" + extras.getInt(Config.PARAM_ID)), PledgeDetailsQuery.PROJECTION);
		    		break;
		    	case Config.POETRY:
		    	 	mHandler.startQuery(PoetryDetailsQuery._TOKEN, Poetry.buildPoetryUri("" + extras.getInt(Config.PARAM_ID)), PoetryDetailsQuery.PROJECTION);
		    	    break;
		    	default:
		    		break;
		    }
		    progressDialog = CustomProgressDialog.show(DetailsActivity.this, null, null);
	    }
	    
    	ImageButton font = (ImageButton) findViewById(R.id.font);
    	font.setOnClickListener(new OnClickListener() {
    		@Override
			public void onClick(View arg0) {
				fontSize += 2;
            	if(fontSize > 30) fontSize = Config.FONT_SIZE;
            	((TextView) findViewById(R.id.text)).setTextSize(fontSize);
            	SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(DetailsActivity.this);
            	Editor edit = prefs.edit();
        		edit.putFloat(Config.FKEY, fontSize);
        		edit.commit();
			}
    	});
    }
	
    public void onHomeClick(View v) {
    	finish();
    }
    
	public void onQueryComplete(int token, Object cookie, Cursor cursor) {
		 if(GameDetailsQuery._TOKEN == token || PledgeDetailsQuery._TOKEN == token || PoetryDetailsQuery._TOKEN == token) {
			 if(progressDialog != null) progressDialog.dismiss();
			 if(cursor != null && cursor.getCount() > 0) {
				cursor.moveToFirst();
				switch(token) {
					case GameDetailsQuery._TOKEN:
						showDetails(cursor.getInt(GameDetailsQuery._ID), cursor.getString(GameDetailsQuery.NAME),  cursor.getString(GameDetailsQuery.DESCRIPTION).replace("$", "\n\n"), cursor.getInt(GameDetailsQuery.STARRED) != 0);
						break;
		        	case PledgeDetailsQuery._TOKEN:
		        		showDetails(cursor.getInt(PledgeDetailsQuery._ID), cursor.getString(GameDetailsQuery.NAME),  cursor.getString(PledgeDetailsQuery.DESCRIPTION).replace("$", "\n\n"), cursor.getInt(PledgeDetailsQuery.STARRED) != 0);
		    			break;
		        	case PoetryDetailsQuery._TOKEN:
		        		showDetails(cursor.getInt(PoetryDetailsQuery._ID), cursor.getString(GameDetailsQuery.NAME),  cursor.getString(PoetryDetailsQuery.DESCRIPTION).replace("$", "\n\n"), cursor.getInt(PoetryDetailsQuery.STARRED) != 0);
		    			break;
		        	default:
		        		break;
				}
				cursor.close();
			}
    	 } else if(cursor != null && (GamesQuery._TOKEN == token || PledgeQuery._TOKEN == token || PoetryQuery._TOKEN == token)) {
    		 if(progressDialog != null) progressDialog.dismiss();
    		 if(position > 0 && cursor.getCount() > position) {
				 for(int i=0; i<position; i++) {
					 cursor.moveToNext();
				 }
				 
				 switch(type) {
		 	    	case Config.GAME:
		 	    		mHandler.startQuery(GameDetailsQuery._TOKEN, Games.buildGameUri("" + cursor.getInt(GameDetailsQuery._ID)), GameDetailsQuery.PROJECTION);
		 	    	    break;
		 	    	case Config.PLEDGE:
		 	    		mHandler.startQuery(PledgeDetailsQuery._TOKEN, Pledge.buildPledgeUri("" + cursor.getInt(PledgeQuery._ID)), PledgeDetailsQuery.PROJECTION);
		 	    		break;
		 	    	case Config.POETRY:
		 	    	 	mHandler.startQuery(PoetryDetailsQuery._TOKEN, Poetry.buildPoetryUri("" + cursor.getInt(PoetryQuery._ID)), PoetryDetailsQuery.PROJECTION);
		 	    	    break;
		 	    	default:
		 	    		break;
		 	    }
				cursor.close(); 
	         }
    	 } else {
    		 if(cursor != null) cursor.close();
    	 }
	}
	
	private void showDetails(final int id, final String title, final String details, final boolean starred) {
		TextView edtTitle = (TextView) findViewById(R.id.title);
	    edtTitle.setText(title.length() > 40 ? title.substring(0, 40) + "..." : title);
	    edtTitle.setTypeface(UIUtils.getTypeface());
	        
	    final TextView edtDetails = (TextView) findViewById(R.id.text);
	    edtDetails.setText(details);
	    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
	    fontSize = prefs.getFloat(Config.FKEY, Config.FONT_SIZE);
	    edtDetails.setTextSize(fontSize);
	    
	    CompoundButton star = (CompoundButton) findViewById(R.id.star);
        star.setChecked(starred);
        star.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
		        final ContentValues values = new ContentValues();
		        switch(type) {
		        	case Config.GAME:
		        		values.put(GamesColumns.STARRED.getName(), isChecked ? 1 : 0);
				        if(isChecked != starred) mHandler.startUpdate(Games.buildGameUri("" + id), values);
		        		break;
		        	case Config.PLEDGE:
		        		values.put(PledgeColumns.STARRED.getName(), isChecked ? 1 : 0);
		        		if(isChecked != starred) mHandler.startUpdate(Pledge.buildPledgeUri("" + id), values);
		        		break;
		        	case Config.POETRY:
		        		values.put(PoetryColumns.STARRED.getName(), isChecked ? 1 : 0);
		        		if(isChecked != starred) mHandler.startUpdate(Poetry.buildPoetryUri("" + id), values);
		        		break;
		        	default:
		        		break;
		        }
		        
			}
        });
        
        ImageButton share = (ImageButton) findViewById(R.id.share);
    	share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
		    	final Intent intent = new Intent(Intent.ACTION_SEND);
		        intent.setType("text/plain");
		        intent.putExtra(Intent.EXTRA_SUBJECT, title);
		        intent.putExtra(Intent.EXTRA_TEXT, details);
		        startActivity(Intent.createChooser(intent, getText(R.string.title_share)));
			}
    	});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	    mHandler = null;
	}
	
	@Override 
	public boolean dispatchTouchEvent(MotionEvent mEvent){ 
	    super.dispatchTouchEvent(mEvent); 
	    return gestureDetector.onTouchEvent(mEvent); 
	}

	class MyGestureDetector extends SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            try {
                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH)
                    return false;
                // right to left swipe
                if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	position++;
                }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                	position--;
                	if(position < 0) position = 0;
                }
                
            	switch(type) {
	    	    	case Config.GAME:
	    	    		mHandler.startQuery(GamesQuery._TOKEN, Games.buildGamesUri("" + idp), GamesQuery.PROJECTION, Games.DEFAULT_SORT);
	    	    		progressDialog = CustomProgressDialog.show(DetailsActivity.this, null, null);
	    	    		break;
	    	    	case Config.PLEDGE:
	    	    		mHandler.startQuery(PledgeQuery._TOKEN, Pledge.buildPledgesUri("" + idp), PledgeQuery.PROJECTION, Pledge.DEFAULT_SORT);
	    	    		progressDialog = CustomProgressDialog.show(DetailsActivity.this, null, null);
	    	    		break;
	    	    	case Config.POETRY:
	    	    		mHandler.startQuery(PoetryQuery._TOKEN, Poetry.buildPoetriesUri("" + idp), PoetryQuery.PROJECTION, Poetry.DEFAULT_SORT);
	    	    		progressDialog = CustomProgressDialog.show(DetailsActivity.this, null, null);
	    	    		break;
	    	    	default:
	    	    		break;
	    	    }
            } catch (Exception e) {
            	Log.e(Config.LOG_TAG, e.toString());
            }
            return false;
        }
        
        // It is necessary to return true from onDown for the onFling event to register
        @Override
        public boolean onDown(MotionEvent e) {
        	return true;
        }
    }
	
    protected void initTitle() {
    	View ornament = (View) findViewById(R.id.viewOrnament);
		ornament.setBackgroundDrawable(GamesApplication.getInstance().getOrnamentDrawable());
		((ImageView) findViewById(R.id.bg_bl)).setAnimation(GamesApplication.getInstance().getAlphaAnimation());
	}
	
}
