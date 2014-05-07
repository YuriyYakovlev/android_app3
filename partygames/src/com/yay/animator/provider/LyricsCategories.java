package com.yay.animator.provider;

import java.util.ArrayList;
import java.util.List;

import com.yay.animator.R;


public enum LyricsCategories {
	L28(28, 1, R.string.l28, 6, 38),
	L6(6, 1, R.string.l6, 12, 52),
	
	L1(1, 2, R.string.l1, 48, 24),
	L37(37, 2, R.string.l37, 15, 25),
	L46(46, 2, R.string.l46, 66, 0),
	L21(21, 2, R.string.l21, 112, 2),
	L22(22, 2, R.string.l22, 1, 0),
	L27(27, 2, R.string.l27, 3, 11),
	
	L12(12, 3, R.string.l12, 0, 38),
	L8(8, 3, R.string.l8, 29, 0),
	L31(31, 3, R.string.l31, 0, 52),
	
	L7(7, 5, R.string.l7, 10, 235),
	L33(33, 5, R.string.l33, 2, 29),
	
	L5(5, 6, R.string.l5, 11, 0),
	L11(11, 6, R.string.l11, 4, 30),
	L35(35, 6, R.string.l35, 42, 125),
	
	L14(14, 7, R.string.l14, 227, 30),
	L38(38, 7, R.string.l38, 44, 2),
	
	L3(3, 8, R.string.l3, 61, 39),
	L41(41, 8, R.string.l41, 143, 3),
	
	L2(2, 9, R.string.l2, 351, 17),
	L34(34, 9, R.string.l34, 87, 86),
	L32(32, 9, R.string.l32, 34, 0),
	
	L10(10, 10, R.string.l10, 2, 50),
	L23(23, 10, R.string.l23, 2, 7),
	L29(29, 10, R.string.l29, 8, 18),
	L40(40, 10, R.string.l40, 41, 0),
	L43(43, 10, R.string.l43, 13, 1),
	L47(47, 10, R.string.l47, 0, 5),
	
	L4(4, 11, R.string.l4, 30, 64),
	L16(16, 11, R.string.l16, 17, 0),
	L17(17, 11, R.string.l17, 18, 0),
	L39(39, 11, R.string.l39, 94, 2),
	
	L24(24, 13, R.string.l24, 25, 0),
	L9(9, 13, R.string.l9, 5, 0),
	L30(30, 13, R.string.l30, 22, 4),
	
	L25(25, 16, R.string.l25, 103, 0),
	L44(44, 16, R.string.l44, 38, 0),
	L45(45, 16, R.string.l45, 35, 0),
	L51(51, 16, R.string.l51, 4, 0);
	
	private int id;
	private int idp;
	private int poetries;
	private int pledges;
	private int name;
	
    LyricsCategories(int id, int idp, int name, int poetries, int pledges) {
    	this.id = id;
        this.idp = idp;
    	this.name = name;
    	this.poetries = poetries;
        this.pledges = pledges;
    }
    
    public int getName() {
    	return name;
    }
    public int getId() {
    	return id;
    }
    public int getIdp() {
    	return idp;
    }
    public int getPoetries() {
    	return poetries;
    }
    public int getPledges() {
    	return pledges;
    }  
    
    // 0 - poetry, 1 - pledge
    public static LyricsCategories[] filterPoetriesByIdp(int idp) {
    	List<LyricsCategories> ret = new ArrayList<LyricsCategories>(); 
    	for(LyricsCategories category : values()) {
			if(category.getIdp() == idp && category.getPoetries() > 0) {
				ret.add(category);
			}
		}
    	return ret.toArray(new LyricsCategories[ret.size()]);
    }
    
    public static LyricsCategories[] filterPledgesByIdp(int idp) {
    	List<LyricsCategories> ret = new ArrayList<LyricsCategories>(); 
    	for(LyricsCategories category : values()) {
			if(category.getIdp() == idp && category.getPledges() > 0) {
				ret.add(category);
			}
		}
    	return ret.toArray(new LyricsCategories[ret.size()]);
    }
    
    public static LyricsCategories findById(int id) {
    	for(LyricsCategories category : values()) {
			if(category.getId() == id) {
				return category;
			}
		}
    	return LyricsCategories.values()[0];
    }
}