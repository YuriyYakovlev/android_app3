package com.yay.animator.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.yay.animator.R;
import com.yay.animator.provider.GamesContract.GamesColumns;
import com.yay.animator.provider.GamesContract.PledgeColumns;
import com.yay.animator.provider.GamesContract.PoetryColumns;
import com.yay.animator.ui.Config;
import com.yay.animator.ui.GamesApplication;


public class DataProcessor {
	private static String T = ",";
    private String QT = "?,";
    private static String SEP = ";";
    

	public void initGames(SQLiteDatabase db) {
    	BufferedReader reader = null;
		InputStreamReader in = null;
        SQLiteStatement stmt = null;
        StringBuilder sql = new StringBuilder(128);
    	StringBuilder cols = new StringBuilder(128);
    	StringBuilder vals = new StringBuilder(128);
        try{
        	in = new InputStreamReader(GamesApplication.getInstance().getResources().openRawResource(R.raw.games));
        	reader = new BufferedReader(in, 1024);
        	String[] data = null;
        	db.beginTransaction();

        	sql.append("insert or replace into games (");
        	int len = GamesColumns.values().length -1;
        	for(int i=1; i<len; i++) {
        		GamesColumns column = GamesColumns.values()[i];
        		cols.append(column.getName()).append(T);
    			vals.append(QT);
    		}
    		if(cols.length() > 0) {
    			cols.deleteCharAt(cols.length()-1);
    			vals.deleteCharAt(vals.length()-1);
    		}
    		sql.append(cols.toString()).append(") values (").append(vals.toString()).append(")");
    		stmt = db.compileStatement(sql.toString());
    		String text = null;
    		while(((text = reader.readLine()) != null)) {
    			data = text.split(SEP);
    			try { stmt.bindLong(1, Integer.valueOf(data[0])); } catch(Exception e) { stmt.bindNull(1); }
    			try { stmt.bindString(2, data[1]); } catch(Exception e) { stmt.bindNull(2); }
    			try { stmt.bindString(3, data[2]); } catch(Exception e) { stmt.bindNull(3); }
                stmt.execute();
             }
        	db.setTransactionSuccessful();
        } catch(Exception e) {
        	Log.e(Config.LOG_TAG, e.toString());
        } finally {
        	try {
        		db.endTransaction();
         		stmt.close();
         		if(reader != null) {
                    reader.close();
                }
                if(in != null) {
                	in.close();
                }
            } catch(Exception e) {
            	Log.e(Config.LOG_TAG, e.toString());
            }
        }
    }
	
	public void initPledge(SQLiteDatabase db) {
    	BufferedReader reader = null;
		InputStreamReader in = null;
        SQLiteStatement stmt = null;
        StringBuilder sql = new StringBuilder(128);
    	StringBuilder cols = new StringBuilder(128);
    	StringBuilder vals = new StringBuilder(128);
        try{
        	in = new InputStreamReader(GamesApplication.getInstance().getResources().openRawResource(R.raw.pledge));
        	reader = new BufferedReader(in, 1024);
        	String[] data = null;
        	db.beginTransaction();

        	sql.append("insert or replace into pledge (");
        	int len = PledgeColumns.values().length -1;
        	for(int i=1; i<len; i++) {
        		PledgeColumns column = PledgeColumns.values()[i];
        		cols.append(column.getName()).append(T);
    			vals.append(QT);
    		}
    		if(cols.length() > 0) {
    			cols.deleteCharAt(cols.length()-1);
    			vals.deleteCharAt(vals.length()-1);
    		}
    		sql.append(cols.toString()).append(") values (").append(vals.toString()).append(")");
    		stmt = db.compileStatement(sql.toString());
    		String text = null;
    		while(((text = reader.readLine()) != null)) {
    			data = text.split(SEP);
    			try { stmt.bindLong(1, Integer.valueOf(data[0])); } catch(Exception e) { stmt.bindNull(1); }
    			if(data[1].length() > 50) {
    				try { stmt.bindString(2, data[1].substring(0, 50)+"..."); } catch(Exception e) { stmt.bindNull(2); }
    			} else {
    				try { stmt.bindString(2, data[1]); } catch(Exception e) { stmt.bindNull(2); }
    			}
    			try { stmt.bindString(3, data[1]); } catch(Exception e) { stmt.bindNull(3); }
                stmt.execute();
             }
        	db.setTransactionSuccessful();
        } catch(Exception e) {
        	Log.e(Config.LOG_TAG, e.toString());
        } finally {
        	try {
        		db.endTransaction();
         		stmt.close();
         		if(reader != null) {
                    reader.close();
                }
                if(in != null) {
                	in.close();
                }
            } catch(Exception e) {
            	Log.e(Config.LOG_TAG, e.toString());
            }
        }
    }
	
	public void initPoetry(SQLiteDatabase db) {
    	BufferedReader reader = null;
		InputStreamReader in = null;
        SQLiteStatement stmt = null;
        StringBuilder sql = new StringBuilder(128);
    	StringBuilder cols = new StringBuilder(128);
    	StringBuilder vals = new StringBuilder(128);
        try{
        	in = new InputStreamReader(GamesApplication.getInstance().getResources().openRawResource(R.raw.poetry));
        	reader = new BufferedReader(in, 1024);
        	String[] data = null;
        	db.beginTransaction();

        	sql.append("insert or replace into poetry (");
        	int len = PoetryColumns.values().length -1;
        	for(int i=1; i<len; i++) {
        		PoetryColumns column = PoetryColumns.values()[i];
        		cols.append(column.getName()).append(T);
    			vals.append(QT);
    		}
    		if(cols.length() > 0) {
    			cols.deleteCharAt(cols.length()-1);
    			vals.deleteCharAt(vals.length()-1);
    		}
    		sql.append(cols.toString()).append(") values (").append(vals.toString()).append(")");
    		stmt = db.compileStatement(sql.toString());
    		String text = null;
    		while(((text = reader.readLine()) != null)) {
    			data = text.split(SEP);
    			try { stmt.bindLong(1, Integer.valueOf(data[0])); } catch(Exception e) { stmt.bindNull(1); }
    			if(data[1].length() > 50) {
    				try { stmt.bindString(2, data[1].substring(0, 50)+"..."); } catch(Exception e) { stmt.bindNull(2); }
    			} else {
    				try { stmt.bindString(2, data[1]); } catch(Exception e) { stmt.bindNull(2); }
    			}
    			try { stmt.bindString(3, data[1]); } catch(Exception e) { stmt.bindNull(3); }
                stmt.execute();
             }
        	db.setTransactionSuccessful();
        } catch(Exception e) {
        	Log.e(Config.LOG_TAG, e.toString());
        } finally {
        	try {
        		db.endTransaction();
         		stmt.close();
         		if(reader != null) {
                    reader.close();
                }
                if(in != null) {
                	in.close();
                }
            } catch(Exception e) {
            	Log.e(Config.LOG_TAG, e.toString());
            }
        }
    }

}
