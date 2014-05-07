package com.yay.animator.util;

import android.graphics.Typeface;

import com.yay.animator.ui.GamesApplication;


public class UIUtils {
    public static Typeface typeface;
    public static Typeface getTypeface() {
		if(typeface == null) {
			typeface = Typeface.createFromAsset(GamesApplication.getInstance().getResources().getAssets(), "fonts/a.ttf");
		}
		return typeface;
	}
    
}
