package com.yay.animator.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.yay.animator.R;
import com.yay.animator.provider.Categories;
import com.yay.animator.provider.GamesContract.GamesColumns;
import com.yay.animator.util.L10N;
import com.yay.animator.util.UIUtils;


public class HomeActivity extends Activity {
	protected CharSequence[] _options = {"English", "Русский"};
	protected boolean[] _selections =  {true, false};
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L10N.init(this);
        setContentView(R.layout.activity_home);
        initTitle();
        initCategories();
        
        boolean firstRun = L10N.getPreferences(this).getBoolean(Config.PARAM_FIRST_RUN, true);
        if(firstRun) {
        	showDialog(0);
            SharedPreferences.Editor editor = L10N.getPreferences(this).edit();
        	editor.putBoolean(Config.PARAM_FIRST_RUN, false);
        	editor.commit();
        }
    }
	
	@Override
	protected Dialog onCreateDialog(int id) {
		return new AlertDialog.Builder(this)
        	.setTitle(getResources().getString(R.string.select_language))
        	.setSingleChoiceItems(_options, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int position) {
                    _selections[position] = true;
                }})
        	.setPositiveButton(getResources().getString(R.string.ok), new DialogButtonClickHandler())
        	.create();
	}

	public class DialogButtonClickHandler implements DialogInterface.OnClickListener {
		public void onClick(DialogInterface dialog, int clicked) {
			switch(clicked) {
				case DialogInterface.BUTTON_POSITIVE:
					String locale = "en";
					if(_selections[1]) {
						locale = "ru";
					}
					if(!locale.equals(L10N.getLocale())) {
						L10N.changeLocale(HomeActivity.this, locale);
					    finish();
					    startActivity(getIntent());
					} 
					break;
			}
		}
	}
    
    public void onStarredClick(View view) {
    	Intent intent = new Intent(HomeActivity.this, GamesActivity.class);
        intent.putExtra(GamesColumns.STARRED.getName(), 1);
        startActivity(intent);
    }
    
    public void onSearchClick(View view) {
    	startSearch(null, false, Bundle.EMPTY, false);
    }
    
    private void initTitle() {
    	View ornament = (View) findViewById(R.id.viewOrnament);
		ornament.setBackgroundDrawable(GamesApplication.getInstance().getOrnamentDrawable());
		((TextView) findViewById(R.id.title_text)).setTypeface(UIUtils.getTypeface());
		//((ImageView) findViewById(R.id.bg_bl)).setAnimation(GamesApplication.getInstance().getAlphaAnimation());
	}
    
    private void initCategories() {
    	GridView gridView = (GridView) findViewById(R.id.categories);
 		gridView.setAdapter(new CategoriesAdapter());
    }
    
    
    class CategoriesAdapter extends BaseAdapter {
        public int getCount() {
            return Categories.all().length;
        }

        public Categories getItem(int position) {
            return Categories.all()[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
        	final Categories category = Categories.all()[position];

        	Button button = new Button(HomeActivity.this);
        	Drawable img = getResources().getDrawable(category.getResId());
        	img.setBounds(0, 0, 150, 150);
        	button.setCompoundDrawables(null, img, null, null);
        	button.setText(getResources().getString(category.getName()));
        	button.setTypeface(UIUtils.getTypeface());
        	button.setBackgroundDrawable(getResources().getDrawable(R.drawable.button));
            button.setTextColor(getResources().getColor(R.color.foreground1));
        	button.setTextSize(18);
        	button.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					Intent intent = new Intent(HomeActivity.this, GamesActivity.class);
			        intent.putExtra(GamesColumns.IDP.getName(), category.getId());
			        startActivity(intent);
				}
			});
            return button;
        }
    }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case R.id.russian:
		    	L10N.changeLocale(this, "ru");
		    	finish();
		    	startActivity(getIntent());
		    	return true;
		    case R.id.english:
		    	L10N.changeLocale(this, "en");
		    	finish();
		    	startActivity(getIntent());
				return true;
		    default: 
		    	return true;
	    }
	}
	
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	MenuInflater inflater = getMenuInflater();
    	inflater.inflate(R.menu.language_menu, menu);
    	return true;
	}
    
}
