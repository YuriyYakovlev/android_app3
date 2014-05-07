package com.yay.animator.provider;

import com.yay.animator.provider.GamesContract.PledgeColumns;
import com.yay.animator.provider.GamesDatabase.Tables;


public interface PledgeDetailsQuery {
    int _TOKEN = 0x3;
    String[] PROJECTION = {
    		Tables.PLEDGE + "." + PledgeColumns._ID.getName(),
    		PledgeColumns.NAME.getName(),
    		PledgeColumns.DESCRIPTION.getName(),
    		PledgeColumns.STARRED.getName()
    };

    int _ID = 0;
    int NAME = 1;
    int DESCRIPTION = 2;
    int STARRED = 3;
    
}
