package com.yay.animator.provider;

import com.yay.animator.provider.GamesContract.GamesColumns;
import com.yay.animator.provider.GamesDatabase.Tables;


public interface GameDetailsQuery {
    int _TOKEN = 0x2;
    String[] PROJECTION = {
    		Tables.GAMES + "." + GamesColumns._ID.getName(),
    		GamesColumns.NAME.getName(),
    		GamesColumns.DESCRIPTION.getName(),
    		GamesColumns.STARRED.getName()
    };

    int _ID = 0;
    int NAME = 1;
    int DESCRIPTION = 2;
    int STARRED = 3;
    
}
