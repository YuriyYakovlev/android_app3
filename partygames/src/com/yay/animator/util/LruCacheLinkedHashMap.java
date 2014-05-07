package com.yay.animator.util;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


public class LruCacheLinkedHashMap extends LinkedHashMap<Object, SoftReference<Drawable>> {   
	private static final long serialVersionUID = 3192904121420497193L;
	private final int maxEntries;   

	public LruCacheLinkedHashMap(final int maxEntries) {   
		super(maxEntries + 1, 1.0f, true);   
		this.maxEntries = maxEntries;   
	}   

	@Override   
	protected boolean removeEldestEntry(final Map.Entry<Object, SoftReference<Drawable>> eldest) {   
		return super.size() > maxEntries;   
	}   
  
	private String icOrnament = "icOrnament";
	private String icTitle = "icTitle";
	
	@Override   
	public SoftReference<Drawable> remove(Object key) {   
		if(key instanceof String && (icOrnament.equals(key) || icTitle.equals(key))) return null;
		
		SoftReference<Drawable> softReferanceDrawable = (SoftReference<Drawable>) get(key);   
		unBindDrawable(softReferanceDrawable.get());   
		return super.remove(key);   
	}   
   
	private void unBindDrawable(Drawable drawable) {   
		if(drawable != null) {   
			drawable.setCallback(null);     
			if(!((BitmapDrawable)drawable).getBitmap().isRecycled()) {     
				((BitmapDrawable)drawable).getBitmap().recycle();     
			}     
			drawable = null;   
		}   
	}   
    
}   
