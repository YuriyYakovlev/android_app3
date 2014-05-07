package com.yay.animator.provider;

import com.yay.animator.provider.GamesContract.PoetryColumns;
import com.yay.animator.provider.GamesDatabase.Tables;


public interface PoetryQuery {
    int _TOKEN = 0x1;
    String[] PROJECTION = {
    		Tables.POETRY + "." + PoetryColumns._ID.getName(),
    		PoetryColumns.NAME.getName(),
    };

    int _ID = 0;
    int NAME = 1;
}
