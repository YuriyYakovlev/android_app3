package com.yay.animator.provider;

import com.yay.animator.provider.GamesContract.GamesColumns;
import com.yay.animator.provider.GamesDatabase.Tables;


public interface GamesQuery {
    int _TOKEN = 0x1;
    String[] PROJECTION = {
    		Tables.GAMES + "." + GamesColumns._ID.getName(),
    		GamesColumns.NAME.getName(),
    };

    int _ID = 0;
    int NAME = 1;
}
