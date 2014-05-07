package com.yay.animator.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yay.animator.R;
import com.yay.animator.provider.Categories;
import com.yay.animator.provider.GamesContract.GamesColumns;
import com.yay.animator.provider.GamesContract.PledgeColumns;
import com.yay.animator.provider.GamesContract.PoetryColumns;
import com.yay.animator.provider.LyricsCategories;
import com.yay.animator.util.L10N;
import com.yay.animator.util.UIUtils;


public class PoetriesActivity extends AdActivity {
    private LyricsCategories[] subCategories;
    
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L10N.init(this);
        setContentView(R.layout.activity_poetry);
	    initTitle();
	     
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
        	int idp = extras.getInt(PoetryColumns.IDP.getName());
	        ((ImageButton) findViewById(R.id.title_delete)).setVisibility(View.GONE);
	        
	        Categories category = Categories.findByIdp(idp);
	        ((TextView) findViewById(R.id.title_text)).setText(getResources().getString(category.getName()) + "/" + getResources().getString(R.string.poetry));
	        		
	        subCategories = LyricsCategories.filterPoetriesByIdp(idp);
	        if(subCategories != null && subCategories.length > 0)
	        	setListAdapter(new SubCategoriesAdapter(this, R.layout.list_item_game, subCategories)); 
        }
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    }
    
    public void onPledgeClick(View view) {
    	Bundle extras = getIntent().getExtras();
    	if(extras != null) {
	    	int starred = extras.getInt(PledgeColumns.STARRED.getName());
	    	if(starred == 1) {
	    		Intent intent = new Intent(PoetriesActivity.this, PledgeActivity.class);
	            intent.putExtra(PledgeColumns.STARRED.getName(), 1);
	            startActivity(intent);
	    	} else {
	        	int idp = extras.getInt(PoetryColumns.IDP.getName());
	    		Intent intent = new Intent(PoetriesActivity.this, PledgesActivity.class);
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
	    		Intent intent = new Intent(PoetriesActivity.this, GamesActivity.class);
	            intent.putExtra(GamesColumns.STARRED.getName(), 1);
	            startActivity(intent);
	    	} else {
	        	int idp = extras.getInt(PoetryColumns.IDP.getName());
	    		Intent intent = new Intent(PoetriesActivity.this, GamesActivity.class);
	    		intent.putExtra(GamesColumns.IDP.getName(), idp);
	    		startActivity(intent);
	    	}
    	}
    	finish();
    }
    
    private class SubCategoriesAdapter extends ArrayAdapter<LyricsCategories> {

		public SubCategoriesAdapter(Context context, int resource, LyricsCategories[] objects) {
			super(context, resource, objects);
		}
		
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View row = null;
			try {
				LayoutInflater inflater=getLayoutInflater();
				row=inflater.inflate(R.layout.list_item_game, parent, false);
				final TextView title = (TextView) row.findViewById(R.id.title);
		        title.setText(getResources().getString(subCategories[position].getName()));
		        title.setTypeface(UIUtils.getTypeface());
		        title.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View arg0) {
						Intent intent = new Intent(PoetriesActivity.this, PoetryActivity.class);
			    		intent.putExtra(PoetryColumns.IDP.getName(), subCategories[position].getId());
			    		startActivity(intent);
					}
		        });
		        
		        final TextView amount = (TextView) row.findViewById(R.id.amount);
		        amount.setText("" + subCategories[position].getPoetries());
		        amount.setTypeface(UIUtils.getTypeface());
			} catch(Exception e) {
				Log.w(Config.LOG_TAG, e.toString());
			}
	        return row;
		}
    }
   
    
}
