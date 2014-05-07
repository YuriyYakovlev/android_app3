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
import com.yay.animator.provider.GamesContract.Pledge;
import com.yay.animator.provider.GamesContract.PledgeColumns;
import com.yay.animator.provider.GamesContract.PoetryColumns;
import com.yay.animator.provider.LyricsCategories;
import com.yay.animator.provider.PledgeQuery;
import com.yay.animator.util.CustomProgressDialog;
import com.yay.animator.util.L10N;
import com.yay.animator.util.NotifyingAsyncQueryHandler;
import com.yay.animator.util.NotifyingAsyncQueryHandler.AsyncQueryListener;
import com.yay.animator.util.UIUtils;


public class PledgeActivity extends AdActivity implements AsyncQueryListener, DialogInterface.OnClickListener {
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
	        setContentView(R.layout.activity_pledge);
	        initTitle();
	        initAdView();
	    }
         
        mAdapter = new PledgeAdapter(this);
        setListAdapter(mAdapter);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
        	mHandler = new NotifyingAsyncQueryHandler(getContentResolver(), this);
        	if(getIntent().hasCategory(Intent.CATEGORY_TAB)) {
        		mHandler.startQuery(PledgeQuery._TOKEN, Pledge.buildSearchUri(extras.getString("query")), PledgeQuery.PROJECTION, Pledge.DEFAULT_SORT);
        	} else {
        		int starred = extras.getInt(PoetryColumns.STARRED.getName());
        		if(starred == 1) {
        			((TextView) findViewById(R.id.title_text)).setText(getResources().getString(R.string.starred) + "/" + getResources().getString(R.string.pledge));
	        		((ImageButton) findViewById(R.id.title_delete)).setVisibility(View.VISIBLE);
	        		mHandler.startQuery(PledgeQuery._TOKEN, Pledge.buildStarredUri(), PledgeQuery.PROJECTION, Pledge.DEFAULT_SORT);
	       		} else {
	        		idp = extras.getInt(PoetryColumns.IDP.getName());
	        		((ImageButton) findViewById(R.id.title_delete)).setVisibility(View.GONE);
	        		 LyricsCategories category = LyricsCategories.findById(idp);
	        		((TextView) findViewById(R.id.title_text)).setText(getResources().getString(category.getName()) + "/" + getResources().getString(R.string.pledge));
	        		mHandler.startQuery(PledgeQuery._TOKEN, Pledge.buildPledgesUri(""+idp), PledgeQuery.PROJECTION, Pledge.DEFAULT_SORT);
        		}
        	}
            progressDialog = CustomProgressDialog.show(PledgeActivity.this, null, null);
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
	    	intent.putExtra(Config.PARAM_ID, cursor.getInt(PledgeQuery._ID));
	    	intent.putExtra(Config.PARAM_IDP, idp);
	    	intent.putExtra(Config.PARAM_TYPE, Config.PLEDGE);
			startActivity(intent);
    	}
    }
    
    @Override
    public void onClick(DialogInterface arg0, int arg1) {
        if(mAdapter != null && mAdapter.getCursor() != null) {
        	final ContentValues values = new ContentValues();
   	     	values.put(PledgeColumns.STARRED.getName(), 0);
   	     	mHandler.startUpdate(Pledge.buildStarredUri(), values);
   	     	mAdapter.getCursor().requery();
        }
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

    public void onQueryComplete(int token, Object cookie, Cursor cursor) {
    	 if(PledgeQuery._TOKEN == token && mAdapter != null && cursor != null) {
 	    	startManagingCursor(cursor);
 	        mAdapter.changeCursor(cursor);
 	        if(progressDialog != null) progressDialog.dismiss();
    	 } else {
    		 if(cursor != null) cursor.close();
    	 }
    }
    
    private class PledgeAdapter extends CursorAdapter /*implements SectionIndexer*/ {
    	//AlphabetIndexer alphaIndexer;
    	
    	public PledgeAdapter(Context context) {
            super(context, null);
            //alphaIndexer=new AlphabetIndexer(null, PledgeQuery.NAME, getResources().getString(R.string
            // .alpha_indexer));
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
            String name = cursor.getString(PledgeQuery.NAME);
            title.setText(name.replace("$", " "));
            title.setTypeface(UIUtils.getTypeface());
            title.setTextSize(16);
            
            view.findViewById(R.id.amount).setVisibility(View.GONE);
            title.setPadding(10, 10, 10, 10);
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
    
}
