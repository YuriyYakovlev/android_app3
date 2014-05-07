package com.yay.animator.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.yay.animator.provider.GamesContract.GamesColumns;
import com.yay.animator.provider.GamesContract.PledgeColumns;
import com.yay.animator.provider.GamesContract.PoetryColumns;
import com.yay.animator.service.DataProcessor;


public class GamesDatabase extends SQLiteOpenHelper {
	// 4
    private static final int DATABASE_VERSION = 5;


    public interface Tables {
    	String GAMES = "games";
    	String PLEDGE = "pledge";
    	String POETRY = "poetry";
    }

    public GamesDatabase(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase db) {
    	StringBuilder sql = new StringBuilder(1024);
        sql.append("CREATE TABLE ").append(Tables.GAMES).append(" (");
    	for(GamesColumns column : GamesColumns.values()) {
			if(GamesColumns._ID.equals(column)) {
				sql.append(BaseColumns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");	
			} else {
				sql.append(column.getName()).append(column.getType().getName());
			}
		}
        sql.append("UNIQUE (").append(GamesColumns.NAME).append(") ON CONFLICT REPLACE)");
        db.execSQL(sql.toString());
        
        sql.delete(0, sql.length());
        sql.append("CREATE TABLE ").append(Tables.PLEDGE).append(" (");
    	for(PledgeColumns column : PledgeColumns.values()) {
			if(PledgeColumns._ID.equals(column)) {
				sql.append(BaseColumns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");	
			} else {
				sql.append(column.getName()).append(column.getType().getName());
			}
		}
    	sql.deleteCharAt(sql.length()-1);
    	sql.append(")");
        db.execSQL(sql.toString());

        sql.delete(0, sql.length());
        sql.append("CREATE TABLE ").append(Tables.POETRY).append(" (");
    	for(PoetryColumns column : PoetryColumns.values()) {
			if(PoetryColumns._ID.equals(column)) {
				sql.append(BaseColumns._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");	
			} else {
				sql.append(column.getName()).append(column.getType().getName());
			}
		}
    	sql.deleteCharAt(sql.length()-1);
    	sql.append(")");
        db.execSQL(sql.toString());

        DataProcessor processor = new DataProcessor(); 
        processor.initGames(db);
        db.execSQL("CREATE INDEX games_idp_idx ON games(idp)");
        
        processor.initPledge(db);
		db.execSQL("CREATE INDEX pledge_idp_idx ON pledge(idp)");
		processor.initPoetry(db);
		db.execSQL("CREATE INDEX poetry_idp_idx ON poetry(idp)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion != DATABASE_VERSION) {
            db.execSQL("DROP TABLE IF EXISTS " + Tables.GAMES);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.PLEDGE);
            db.execSQL("DROP TABLE IF EXISTS " + Tables.POETRY);
            db.execSQL("DROP INDEX IF EXISTS games_idp_idx");
            db.execSQL("DROP INDEX IF EXISTS pledge_idp_idx");
            db.execSQL("DROP INDEX IF EXISTS poetry_idp_idx");
            onCreate(db);
        }
    }

}
