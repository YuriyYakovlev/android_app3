package com.yay.animator.ui;

import java.lang.ref.SoftReference;
import java.util.Collections;
import java.util.Map;

import android.app.Application;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.animation.AlphaAnimation;

import com.yay.animator.R;
import com.yay.animator.provider.GamesDatabase;
import com.yay.animator.util.LruCacheLinkedHashMap;


public class GamesApplication extends Application {
	private static GamesApplication instance; 
    private GamesDatabase mOpenHelper;
    private static Map<Object, SoftReference<Drawable>> drawableHashMap;
    private AlphaAnimation alphaAnimation;
     
    
	public static GamesApplication getInstance() {
		return instance;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		drawableHashMap = Collections.synchronizedMap(new LruCacheLinkedHashMap(50));
	}
	
	public GamesDatabase getDatabase() {
		if(mOpenHelper == null) {
			mOpenHelper = new GamesDatabase(getApplicationContext(), getResources().getString(R.string.database_name));
		}
		return mOpenHelper;
	}
	
	public void resetDatabase() {
		if(mOpenHelper != null) {
			mOpenHelper.close();
			mOpenHelper = null;
		}
	}
	
	public Map<Object, SoftReference<Drawable>> getDrawableHashMap() {
		return drawableHashMap;
	}
	
	public Drawable getOrnamentDrawable() {
		SoftReference<Drawable> icOrnament = getDrawableHashMap().get("icOrnament");
		if(icOrnament == null) {
			BitmapDrawable drawable = new BitmapDrawable(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.ornament));
			drawable.setTileModeX(android.graphics.Shader.TileMode.REPEAT);
			icOrnament = new SoftReference<Drawable>(drawable);   
			getDrawableHashMap().put("icOrnament", icOrnament);   
		}
		return icOrnament.get();
	}
    
	public AlphaAnimation getAlphaAnimation() {
    	if(alphaAnimation == null) {
	    	alphaAnimation = new AlphaAnimation(0.5F, 0.5F);
	    	alphaAnimation.setDuration(0); // Make animation instant
	    	alphaAnimation.setFillAfter(true); // Tell it to persist after the animation ends
    	}
    	return alphaAnimation;
    }
	
}
