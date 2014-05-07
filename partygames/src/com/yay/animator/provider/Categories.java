package com.yay.animator.provider;

import java.util.ArrayList;
import java.util.List;

import com.yay.animator.R;
import com.yay.animator.util.L10N;


public enum Categories {
	C1(1, R.string.c1, R.drawable.c1, "en_ru"),
	C2(2, R.string.c2, R.drawable.c2, "en_ru"),
	C3(3, R.string.c3, R.drawable.c3, "ru"),
	C4(4, R.string.c4, R.drawable.c4, "ru"),
	C5(5, R.string.c5, R.drawable.c5, "en_ru"),
	C6(6, R.string.c6, R.drawable.c6, "ru"),
	C7(7, R.string.c7, R.drawable.c7, "en_ru"),
	C8(8, R.string.c8, R.drawable.c8, "en_ru"),
	C9(9, R.string.c9, R.drawable.c9, "en_ru"),
	C10(10, R.string.c10, R.drawable.c10, "ru"),
	C11(11, R.string.c11, R.drawable.c11, "ru"),
	C13(13, R.string.c13, R.drawable.c13, "en_ru"),
	C16(16, R.string.c16, R.drawable.c16, "en_ru"),
	C17(17, R.string.c17, R.drawable.c17, "ru"),
	C18(18, R.string.c18, R.drawable.c18, "ru"),
	C19(19, R.string.c19, R.drawable.c19, "ru");

	
	private int id;
	private int name;
	private int resId;
	private String locale;
	
    Categories(int id, int name, int resId, String locale) {
    	this.id = id;
    	this.name = name;
    	this.resId = resId;
    	this.locale = locale;
    }
    
    public int getName() {
    	return name;
    }
    public int getId() {
    	return id;
    }
    public int getResId() {
    	return resId;
    }
    
    public static Categories findByIdp(int id) {
    	for(Categories category : values()) {
			if(category.getId() == id) {
				return category;
			}
		}
    	return Categories.values()[0];
    }
    
    public static Categories[] all() {
    	List<Categories> ret = new ArrayList<Categories>();
    	String locale = L10N.getLocale();
    	if(!locale.equals("ru") && !locale.equals("en")) locale = "en";
    	for(Categories category : values()) {
			if(category.locale.indexOf(locale) >= 0) {
				ret.add(category);
			}
		}
    	if(ret.size() == 0) {
    		return values();
    	} else {
    		return ret.toArray(new Categories[ret.size()]);
    	}
    }
    
}