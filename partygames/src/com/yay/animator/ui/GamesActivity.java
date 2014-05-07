package com.yay.animator.ui;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.yay.animator.R;
import com.yay.animator.provider.Categories;
import com.yay.animator.provider.GameDetailsQuery;
import com.yay.animator.provider.GamesContract.Games;
import com.yay.animator.provider.GamesContract.GamesColumns;
import com.yay.animator.provider.GamesQuery;
import com.yay.animator.util.CustomProgressDialog;
import com.yay.animator.util.L10N;
import com.yay.animator.util.NotifyingAsyncQueryHandler;
import com.yay.animator.util.NotifyingAsyncQueryHandler.AsyncQueryListener;
import com.yay.animator.util.UIUtils;


public class GamesActivity extends AdActivity implements AsyncQueryListener, DialogInterface.OnClickListener {
	private NotifyingAsyncQueryHandler mHandler;
	public static CursorAdapter mAdapter;
	private CustomProgressDialog progressDialog;
	private int idp;
    
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L10N.init(this);
        if(getIntent().hasCategory(Intent.CATEGORY_TAB)) {
        	setContentView(R.layout.activity_content);
        } else {
	        setContentView(R.layout.activity_games);
	        initTitle();
	        initAdView();
        }
         
        mAdapter = new GamesAdapter(this);
        setListAdapter(mAdapter);
        
        Bundle extras = getIntent().getExtras();
        if(extras != null) {
        	mHandler = new NotifyingAsyncQueryHandler(getContentResolver(), this);
        	if(getIntent().hasCategory(Intent.CATEGORY_TAB)) {
        		mHandler.startQuery(GamesQuery._TOKEN, Games.buildSearchUri(extras.getString("query")), GamesQuery.PROJECTION, Games.DEFAULT_SORT);
        	} else {
        		int starred = extras.getInt(GamesColumns.STARRED.getName());
        		if(starred == 1) {
        			mHandler.startQuery(GamesQuery._TOKEN, Games.buildStarredUri(), GamesQuery.PROJECTION, Games.DEFAULT_SORT);
	        		((TextView) findViewById(R.id.title_text)).setText(getResources().getString(R.string.starred) + "/" + getResources().getString(R.string.games));
	        		((ImageButton) findViewById(R.id.title_delete)).setVisibility(View.VISIBLE);
        		} else {
	        		idp = extras.getInt(GamesColumns.IDP.getName());
	        		mHandler.startQuery(GamesQuery._TOKEN, Games.buildGamesUri(""+idp), GamesQuery.PROJECTION, Games.DEFAULT_SORT);
	        		((ImageButton) findViewById(R.id.title_delete)).setVisibility(View.GONE);
	        		for(Categories item : Categories.values()) {
	        			if(item.getId() == idp) {
	        				((TextView) findViewById(R.id.title_text)).setText(getResources().getString(item.getName()) + "/" + getResources().getString(R.string.games));
	        				break;
	        			}
	        		}
        		}
        	}
            progressDialog = CustomProgressDialog.show(GamesActivity.this, null, null);
        }
    }

    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	if(mAdapter != null) {
        	try { mAdapter.changeCursor(null); } catch(Exception e) {}
        	mAdapter = null;
        }
        mHandler = null;
    }
    
    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
    	if(mAdapter != null) {
	    	final Cursor cursor = (Cursor) mAdapter.getItem(position);
	    	Intent intent = new Intent(this, DetailsActivity.class);
	    	intent.putExtra(Config.PARAM_ID, cursor.getInt(GameDetailsQuery._ID));
	    	intent.putExtra(Config.PARAM_IDP, idp);
	    	intent.putExtra(Config.PARAM_TYPE, Config.GAME);
			startActivity(intent);
	    }
    }
    
    @Override
    public void onClick(DialogInterface arg0, int arg1) {
        if(mAdapter != null && mAdapter.getCursor() != null) {
        	final ContentValues values = new ContentValues();
   	     	values.put(GamesColumns.STARRED.getName(), 0);
   	     	mHandler.startUpdate(Games.buildStarredUri(), values);
   	     	mAdapter.getCursor().requery();
        }
   }
    
   public void onQueryComplete(int token, Object cookie, Cursor cursor) {
    	 if(GamesQuery._TOKEN == token && mAdapter != null && cursor != null) {
 	    	startManagingCursor(cursor);
 	        mAdapter.changeCursor(cursor);
 	        if(progressDialog != null) progressDialog.dismiss();
    	 } else {
    		if(cursor != null) cursor.close();
    	 }
    }
    
    private class GamesAdapter extends CursorAdapter /*implements SectionIndexer*/ {
    	//AlphabetIndexer alphaIndexer;
    	
    	public GamesAdapter(Context context) {
            super(context, null);
            //alphaIndexer=new AlphabetIndexer(null, GamesQuery.NAME, getResources().getString(R.string.alpha_indexer));
        }

        /** {@inheritDoc} */
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            //alphaIndexer.setCursor(cursor);
        	return getLayoutInflater().inflate(R.layout.list_item_game, parent, false);
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            final TextView title = (TextView) view.findViewById(R.id.title);
            title.setText(cursor.getString(GamesQuery.NAME));
            title.setTypeface(UIUtils.getTypeface());
        }
    
		/*@Override
		public int getPositionForSection(int section) {
			return alphaIndexer.getPositionForSection(section); //use the indexer
		}

		@Override
		public int getSectionForPosition(int position) {
			return alphaIndexer.getSectionForPosition(position); //use the indexer
		}

		@Override
		public Object[] getSections() {
			return alphaIndexer.getSections(); //use the indexer
		}*/
    }
    
    /** Handle "delete" title-bar action. */
    public void onDeleteClick(View v) {
    	AlertDialog.Builder alert = new AlertDialog.Builder(this)
        	.setTitle(R.string.starred)
        	.setIcon(android.R.drawable.ic_dialog_alert)
        	.setMessage(R.string.delete_confirm)
        	.setNegativeButton(android.R.string.cancel, null)
        	.setPositiveButton(android.R.string.ok, this)
        	.setCancelable(false);
    	alert.show(); 
    }

}
