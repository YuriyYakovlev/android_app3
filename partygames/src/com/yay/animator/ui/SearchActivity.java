package com.yay.animator.ui;

import android.app.SearchManager;
import android.app.TabActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;

import com.yay.animator.R;
import com.yay.animator.util.UIUtils;


public class SearchActivity extends TabActivity {
	private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        onNewIntent(getIntent());
    }

    @Override
    public void onNewIntent(Intent intent) {
        mQuery = intent.getStringExtra(SearchManager.QUERY);
        final CharSequence title = getString(R.string.title_search_query, mQuery);

        setTitle(title);
        ((TextView) findViewById(R.id.title_text)).setText(title);

        final TabHost host = getTabHost();
        host.setCurrentTab(0);
        host.clearAllTabs();

        setupGamesTab();
        
        initTitle();
        initBackground();
    }

    public void onSearchClick(View v) {
    	startSearch(null, false, Bundle.EMPTY, false);
    }

    /** Build and add "sessions" tab. */
    private void setupGamesTab() {
        final TabHost host = getTabHost();

        final Intent intent = new Intent(SearchActivity.this, GamesActivity.class);
        intent.putExtra("query", Uri.encode(mQuery));
        intent.addCategory(Intent.CATEGORY_TAB);
        // Recipe content comes from reused activity
        host.clearAllTabs();
        host.addTab(host.newTabSpec(mQuery).setIndicator(buildIndicator(R.string.games)).setContent(intent));
    }

    /**
     * Build a {@link View} to be used as a tab indicator, setting the requested
     * string resource as its label.
     */
    private View buildIndicator(int textRes) {
        final TextView indicator = (TextView) getLayoutInflater().inflate(R.layout.tab_indicator, getTabWidget(), false);
        indicator.setText(textRes);
        indicator.setTypeface(UIUtils.getTypeface());
        return indicator;
    }
    
    private void initBackground() {
		//LinearLayout root = (LinearLayout) findViewById(R.id.search_root);
		//root.setBackgroundDrawable(GamesApplication.getInstance().getBackgroundDrawable());
    }
    
    private void initTitle() {
    	View ornament = (View) findViewById(R.id.viewOrnament);
		ornament.setBackgroundDrawable(GamesApplication.getInstance().getOrnamentDrawable());
    	((TextView) findViewById(R.id.title_text)).setTypeface(UIUtils.getTypeface());
	}
}
