package com.yay.animator.provider;

import com.yay.animator.provider.GamesContract.PoetryColumns;
import com.yay.animator.provider.GamesDatabase.Tables;


public interface PoetryDetailsQuery {
    int _TOKEN = 0x4;
    String[] PROJECTION = {
    		Tables.POETRY + "." + PoetryColumns._ID.getName(),
    		PoetryColumns.NAME.getName(),
    		PoetryColumns.DESCRIPTION.getName(),
    		PoetryColumns.STARRED.getName()
    };

    int _ID = 0;
    int NAME = 1;
    int DESCRIPTION = 2;
    int STARRED = 3;
    
}
