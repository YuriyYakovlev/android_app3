package com.yay.animator.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.ads.Ad;
import com.google.ads.AdListener;
import com.google.ads.AdRequest;
import com.google.ads.AdRequest.ErrorCode;
import com.google.ads.AdSize;
import com.google.ads.AdView;
import com.yay.animator.R;
import com.yay.animator.provider.GamesContract.GamesColumns;
import com.yay.animator.provider.GamesContract.PledgeColumns;
import com.yay.animator.provider.GamesContract.PoetryColumns;
import com.yay.animator.provider.LyricsCategories;
import com.yay.animator.util.UIUtils;


public abstract class AdActivity extends ListActivity {
	
	
    /** Create the adView **/
    protected void initAdView() {
        final LinearLayout layout = (LinearLayout)findViewById(R.id.AdView1);
        if(layout != null) {
        	
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        	boolean isPremium = preferences.getBoolean(Config.REMOVE_ADS_KEY, false);
        	
        	if(isPremium) {
        		layout.setVisibility(View.GONE);
        	} else {
        		AdView adView = new AdView(this, AdSize.BANNER, Config.AD_KEY);
        		layout.addView(adView);
            	adView.loadAd(new AdRequest());
            	adView.setAdListener(new AdListener() {
					@Override
					public void onDismissScreen(Ad arg0) {
						layout.setVisibility(View.GONE);
					}

					@Override
					public void onLeaveApplication(Ad arg0) {
						layout.setVisibility(View.GONE);
					}

					@Override
					public void onPresentScreen(Ad arg0) {
					}

					@Override
					public void onReceiveAd(Ad arg0) {
					}

					@Override
					public void onFailedToReceiveAd(Ad arg0, ErrorCode arg1) {
					}
            	});
            	layout.setVisibility(View.VISIBLE);
        	}
        }
    }
    
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.menu, menu);
    	return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case R.id.games:
		    	onGamesClick(null);
		    	break;
		    case R.id.poetry:
		    	onPoetryClick(null);
		    	break;
		    case R.id.pledge:
		    	onPledgeClick(null);
		    	break;
			default:
		    	break;
	    }
	    return true;
	}
    
    public void onPoetryClick(View view) {
    	Bundle extras = getIntent().getExtras();
    	if(extras != null) {
	    	int starred = extras.getInt(GamesColumns.STARRED.getName());
	    	if(starred == 1) {
	    		Intent intent = new Intent(this, PoetryActivity.class);
	            intent.putExtra(PoetryColumns.STARRED.getName(), 1);
	            startActivity(intent);
	    	} else {
	        	int idp = extras.getInt(GamesColumns.IDP.getName());
	    		Intent intent = new Intent(this, PoetriesActivity.class);
	    		intent.putExtra(PoetryColumns.IDP.getName(), idp);
	    		startActivity(intent);
	    	}
    	}
    	finish();
    }

    public void onPledgeClick(View view) {
    	Bundle extras = getIntent().getExtras();
    	if(extras != null) {
	    	int starred = extras.getInt(GamesColumns.STARRED.getName());
	    	if(starred == 1) {
	    		Intent intent = new Intent(this, PledgeActivity.class);
	            intent.putExtra(PledgeColumns.STARRED.getName(), 1);
	            startActivity(intent);
	    	} else {
	        	int idp = extras.getInt(GamesColumns.IDP.getName());
	    		Intent intent = new Intent(this, PledgesActivity.class);
	    		intent.putExtra(PledgeColumns.IDP.getName(), idp);
	    		startActivity(intent);
	    	}
    	}
    	finish();
    }
    
    public void onGamesClick(View view) {
    	Bundle extras = getIntent().getExtras();
    	if(extras != null) {
	    	int starred = extras.getInt(PledgeColumns.STARRED.getName());
	    	if(starred == 1) {
	    		Intent intent = new Intent(this, GamesActivity.class);
	            intent.putExtra(GamesColumns.STARRED.getName(), 1);
	            startActivity(intent);
	    	} else {
	        	int idp = extras.getInt(PoetryColumns.IDP.getName());
	    		Intent intent = new Intent(this, GamesActivity.class);
	    		intent.putExtra(GamesColumns.IDP.getName(), LyricsCategories.findById(idp).getIdp());
	    		startActivity(intent);
	    	}
    	}
    	finish();
    }
    
    public void onHomeClick(View v) {
		finish();
    }
    
    protected void initTitle() {
    	View ornament = (View) findViewById(R.id.viewOrnament);
		ornament.setBackgroundDrawable(GamesApplication.getInstance().getOrnamentDrawable());
    	((TextView) findViewById(R.id.title_text)).setTypeface(UIUtils.getTypeface());
		((ImageView) findViewById(R.id.bg_bl)).setAnimation(GamesApplication.getInstance().getAlphaAnimation());
	}
    
}
