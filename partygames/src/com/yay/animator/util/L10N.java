package com.yay.animator.util;

import java.util.Locale;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.preference.PreferenceManager;

import com.yay.animator.ui.GamesApplication;


public class L10N {
	public static Typeface font;
	public static Locale locale;
	public static final String LANG_KEY = "com.yay.animator.lang";
	private static SharedPreferences prefs;
	public static String FONT = "fonts/a.ttf";
	
	
	public static void init(Context context) {
		String locale = getPreferences(context).getString(L10N.LANG_KEY, null);
		if(locale != null) {
			L10N.locale = new Locale(locale);
	     	Configuration conf = context.getResources().getConfiguration();
			if(!conf.locale.equals(L10N.locale)) {
				conf.locale = L10N.locale;
				context.getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
			}
		}
		initFont(context.getResources());
	}
	
	public static void changeLocale(Context context, String locale) {
		L10N.locale = new Locale(locale);
    	Editor edit = getPreferences(context).edit();
		edit.putString(L10N.LANG_KEY, locale);
		edit.commit();
		Configuration conf = context.getResources().getConfiguration();
		if(!conf.locale.equals(L10N.locale)) {
			conf.locale = L10N.locale;
			context.getResources().updateConfiguration(conf, context.getResources().getDisplayMetrics());
			GamesApplication.getInstance().resetDatabase();
		}
	}
	
	public static String getLocale() {
		String ret = getPreferences(GamesApplication.getInstance()).getString(L10N.LANG_KEY, null);
		if(ret == null) {
			Configuration conf = GamesApplication.getInstance().getResources().getConfiguration();
			ret = conf.locale.getLanguage();
		}
		return ret;
	}
	
	public static SharedPreferences getPreferences(Context context) {
		if(prefs == null) {
			prefs = PreferenceManager.getDefaultSharedPreferences(context);
		}
		return prefs;
	}
	
	// PRIVATE METHODS
	
	private static void initFont(Resources res) {
		if(font == null) {
			font = Typeface.createFromAsset(res.getAssets(), FONT);
		}
	}
	
}
