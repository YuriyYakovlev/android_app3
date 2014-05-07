package com.yay.animator.provider;

import java.io.FileNotFoundException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.yay.animator.provider.GamesContract.Games;
import com.yay.animator.provider.GamesContract.Pledge;
import com.yay.animator.provider.GamesContract.Poetry;
import com.yay.animator.provider.GamesDatabase.Tables;
import com.yay.animator.ui.GamesApplication;
import com.yay.animator.util.SelectionBuilder;


public class GamesProvider extends ContentProvider {
    private static final String TAG = "GamesProvider";
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    
    private static final int GAMES = 100;
    private static final int GAMES_ID = 200;
    private static final int GAMES_IDP = 300;
    private static final int GAMES_SEARCH = 400;
    private static final int GAMES_STARRED = 500;
    
    private static final int PLEDGE = 600;
    private static final int PLEDGE_ID = 700;
    private static final int PLEDGE_IDP = 800;
    private static final int PLEDGE_SEARCH = 900;
    private static final int PLEDGE_STARRED = 1000;
    
    private static final int POETRY = 1100;
    private static final int POETRY_ID = 1200;
    private static final int POETRY_IDP = 1300;
    private static final int POETRY_SEARCH = 1400;
    private static final int POETRY_STARRED = 1500;
    
    /**
     * Build and return a {@link UriMatcher} that catches all {@link Uri}
     * variations supported by this {@link ContentProvider}.
     */
    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GamesContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "games", GAMES);
        matcher.addURI(authority, "games/id/*", GAMES_ID);
        matcher.addURI(authority, "games/idp/*", GAMES_IDP);
        matcher.addURI(authority, "games/search/*", GAMES_SEARCH);
        matcher.addURI(authority, "games/starred", GAMES_STARRED);
        
        matcher.addURI(authority, "pledge", PLEDGE);
        matcher.addURI(authority, "pledge/id/*", PLEDGE_ID);
        matcher.addURI(authority, "pledge/idp/*", PLEDGE_IDP);
        matcher.addURI(authority, "pledge/search/*", PLEDGE_SEARCH);
        matcher.addURI(authority, "pledge/starred", PLEDGE_STARRED);
        
        matcher.addURI(authority, "poetry", POETRY);
        matcher.addURI(authority, "poetry/id/*", POETRY_ID);
        matcher.addURI(authority, "poetry/idp/*", POETRY_IDP);
        matcher.addURI(authority, "poetry/search/*", POETRY_SEARCH);
        matcher.addURI(authority, "poetry/starred", POETRY_STARRED);
        
        return matcher;
    }

    @Override
    public boolean onCreate() {
        return true;
    }
    
    public SQLiteDatabase getReadableDatabase() {
    	SQLiteDatabase db = null;
    	try {
    		db = GamesApplication.getInstance().getDatabase().getReadableDatabase();
    	}catch(SQLiteException e) {
    		db = GamesApplication.getInstance().getDatabase().getReadableDatabase();
    	}
    	return db;
    }
    
    public SQLiteDatabase getWritableDatabase() {
    	SQLiteDatabase db = null;
    	try {
    		db = GamesApplication.getInstance().getDatabase().getWritableDatabase();
    	}catch(SQLiteException e) {
    		db = GamesApplication.getInstance().getDatabase().getWritableDatabase();
    	}
    	return db;
    }

    /** {@inheritDoc} */
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                return Games.CONTENT_TYPE;
            case GAMES_ID:
                return Games.CONTENT_TYPE;
            case GAMES_IDP:
                return Games.CONTENT_TYPE;
            case GAMES_SEARCH:
                return Games.CONTENT_TYPE;
            case GAMES_STARRED:
                return Games.CONTENT_TYPE;

            case PLEDGE:
                return Pledge.CONTENT_TYPE;
            case PLEDGE_ID:
                return Pledge.CONTENT_TYPE;
            case PLEDGE_IDP:
                return Pledge.CONTENT_TYPE;
            case PLEDGE_SEARCH:
                return Pledge.CONTENT_TYPE;
            case PLEDGE_STARRED:
                return Pledge.CONTENT_TYPE;

            case POETRY:
                return Poetry.CONTENT_TYPE;
            case POETRY_ID:
                return Poetry.CONTENT_TYPE;
            case POETRY_IDP:
                return Poetry.CONTENT_TYPE;
            case POETRY_SEARCH:
                return Poetry.CONTENT_TYPE;
            case POETRY_STARRED:
                return Poetry.CONTENT_TYPE;
                
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    /** {@inheritDoc} */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        final SelectionBuilder builder = buildExpandedSelection(uri, match);
        return builder.where(selection, selectionArgs).query(db, projection, sortOrder);
    }

    /** {@inheritDoc} */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return uri;
    }

    /** {@inheritDoc} */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.d(TAG, "update(uri=" + uri + ", values=" + values.toString() + ")");
        final SQLiteDatabase db = getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        return builder.where(selection, selectionArgs).update(db, values);
    }

    /** {@inheritDoc} */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = getWritableDatabase();
        final SelectionBuilder builder = buildSimpleSelection(uri);
        return builder.where(selection, selectionArgs).delete(db);
    }

    /**
     * Build a simple {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually enough to support {@link #insert},
     * {@link #update}, and {@link #delete} operations.
     */
    private SelectionBuilder buildSimpleSelection(Uri uri) {
        final SelectionBuilder builder = new SelectionBuilder();
        final int match = sUriMatcher.match(uri);
        switch (match) {
	        case GAMES: {
	        	return builder.table(Tables.GAMES);
            }
	        case GAMES_ID: {
	        	final String id = Uri.decode(Games.getSearchQuery(uri));
	        	return builder.table(Tables.GAMES).where("_id="+id+"");
            }
	        case GAMES_IDP: {
	        	final String idp = Uri.decode(Games.getSearchQuery(uri));
	        	return builder.table(Tables.GAMES).where("idp="+idp+"");
            }
	        case GAMES_SEARCH: {
	        	String query = Uri.decode(Games.getSearchQuery(uri));
	        	if(query != null && query.length() > 0) {
	        		String c = query.substring(0, 1);
	        		query = c.toUpperCase() + query.substring(1, query.length());
	        	}
	        	return builder.table(Tables.GAMES).where("name like '"+query+"%'");
            }
	        case GAMES_STARRED: {
	        	return builder.table(Tables.GAMES).where("starred=1");
            }

	        case PLEDGE: {
	        	return builder.table(Tables.PLEDGE);
            }
	        case PLEDGE_ID: {
	        	final String id = Uri.decode(Pledge.getSearchQuery(uri));
	        	return builder.table(Tables.PLEDGE).where("_id="+id+"");
            }
	        case PLEDGE_IDP: {
	        	final String idp = Uri.decode(Pledge.getSearchQuery(uri));
	        	return builder.table(Tables.PLEDGE).where("idp="+idp+"");
            }
	        case PLEDGE_SEARCH: {
	        	String query = Uri.decode(Pledge.getSearchQuery(uri));
	        	if(query != null && query.length() > 0) {
	        		String c = query.substring(0, 1);
	        		query = c.toUpperCase() + query.substring(1, query.length());
	        	}
	        	return builder.table(Tables.PLEDGE).where("name like '"+query+"%'");
            }
	        case PLEDGE_STARRED: {
	        	return builder.table(Tables.PLEDGE).where("starred=1");
            }

	        case POETRY: {
	        	return builder.table(Tables.POETRY);
            }
	        case POETRY_ID: {
	        	final String id = Uri.decode(Poetry.getSearchQuery(uri));
	        	return builder.table(Tables.POETRY).where("_id="+id+"");
            }
	        case POETRY_IDP: {
	        	final String idp = Uri.decode(Poetry.getSearchQuery(uri));
	        	return builder.table(Tables.POETRY).where("idp="+idp+"");
            }
	        case POETRY_SEARCH: {
	        	String query = Uri.decode(Poetry.getSearchQuery(uri));
	        	if(query != null && query.length() > 0) {
	        		String c = query.substring(0, 1);
	        		query = c.toUpperCase() + query.substring(1, query.length());
	        	}
	        	return builder.table(Tables.POETRY).where("name like '"+query+"%'");
            }
	        case POETRY_STARRED: {
	        	return builder.table(Tables.POETRY).where("starred=1");
            }

	        default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    /**
     * Build an advanced {@link SelectionBuilder} to match the requested
     * {@link Uri}. This is usually only used by {@link #query}, since it
     * performs table joins useful for {@link Cursor} data.
     */
    private SelectionBuilder buildExpandedSelection(Uri uri, int match) {
        final SelectionBuilder builder = new SelectionBuilder();
        switch (match) {
	        case GAMES: {
	        	return builder.table(Tables.GAMES);
	        }
	        case GAMES_ID: {
	        	final String id = Uri.decode(Games.getSearchQuery(uri));
	        	return builder.table(Tables.GAMES).where("_id="+id+"");
            }
	        case GAMES_IDP: {
	        	final String idp = Uri.decode(Games.getSearchQuery(uri));
	        	return builder.table(Tables.GAMES).where("idp="+idp+"");
            }
	        case GAMES_SEARCH: {
	        	String query = Uri.decode(Games.getSearchQuery(uri));
	        	if(query != null && query.length() > 0) {
	        		String c = query.substring(0, 1);
	        		query = c.toUpperCase() + query.substring(1, query.length());
	        	}
	        	return builder.table(Tables.GAMES).where("name like '"+query+"%'");
            }
	        case GAMES_STARRED: {
	        	return builder.table(Tables.GAMES).where("starred=1");
            }
	        
	        case PLEDGE: {
	        	return builder.table(Tables.PLEDGE);
            }
	        case PLEDGE_ID: {
	        	final String id = Uri.decode(Pledge.getSearchQuery(uri));
	        	return builder.table(Tables.PLEDGE).where("_id="+id+"");
            }
	        case PLEDGE_IDP: {
	        	final String idp = Uri.decode(Pledge.getSearchQuery(uri));
	        	return builder.table(Tables.PLEDGE).where("idp="+idp+"");
            }
	        case PLEDGE_SEARCH: {
	        	String query = Uri.decode(Pledge.getSearchQuery(uri));
	        	if(query != null && query.length() > 0) {
	        		String c = query.substring(0, 1);
	        		query = c.toUpperCase() + query.substring(1, query.length());
	        	}
	        	return builder.table(Tables.PLEDGE).where("name like '"+query+"%'");
            }
	        case PLEDGE_STARRED: {
	        	return builder.table(Tables.PLEDGE).where("starred=1");
            }

	        case POETRY: {
	        	return builder.table(Tables.POETRY);
            }
	        case POETRY_ID: {
	        	final String id = Uri.decode(Poetry.getSearchQuery(uri));
	        	return builder.table(Tables.POETRY).where("_id="+id+"");
            }
	        case POETRY_IDP: {
	        	final String idp = Uri.decode(Poetry.getSearchQuery(uri));
	        	return builder.table(Tables.POETRY).where("idp="+idp+"");
            }
	        case POETRY_SEARCH: {
	        	String query = Uri.decode(Poetry.getSearchQuery(uri));
	        	if(query != null && query.length() > 0) {
	        		String c = query.substring(0, 1);
	        		query = c.toUpperCase() + query.substring(1, query.length());
	        	}
	        	return builder.table(Tables.POETRY).where("name like '"+query+"%'");
            }
	        case POETRY_STARRED: {
	        	return builder.table(Tables.POETRY).where("starred=1");
            }
	        
	        default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }
  
}
