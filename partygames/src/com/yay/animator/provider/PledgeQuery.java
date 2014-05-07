package com.yay.animator.provider;

import com.yay.animator.provider.GamesContract.PledgeColumns;
import com.yay.animator.provider.GamesDatabase.Tables;


public interface PledgeQuery {
    int _TOKEN = 0x1;
    String[] PROJECTION = {
    		Tables.PLEDGE + "." + PledgeColumns._ID.getName(),
    		PledgeColumns.NAME.getName(),
    };

    int _ID = 0;
    int NAME = 1;
}
