package com.yay.animator.provider;

import android.net.Uri;


public class GamesContract {

	public enum DBType {
		INT(" INTEGER,"),
		FLOAT(" FLOAT,"),
		TEXT(" TEXT,");
		
		private String name;
        
        DBType(String name) {
        	this.name = name;
        }
        
        public String getName() {
        	return name;
        }
	}
    						
    public enum GamesColumns {
    	_ID("_id", DBType.INT),
    	IDP("idp", DBType.INT),
        NAME("name", DBType.TEXT),
    	DESCRIPTION("description", DBType.TEXT),
    	STARRED("starred", DBType.INT);
        
    	private String columnName;
        private DBType type;
        
        GamesColumns(String columnName, DBType type) {
        	this.columnName = columnName;
        	this.type = type;
        }
        
        public String getName() {
        	return columnName;
        }
        public DBType getType() {
        	return type;
        }
    }
    
    public enum PoetryColumns {
    	_ID("_id", DBType.INT),
    	IDP("idp", DBType.INT),
        NAME("name", DBType.TEXT),
    	DESCRIPTION("description", DBType.TEXT),
    	STARRED("starred", DBType.INT);
        
    	private String columnName;
        private DBType type;
        
        PoetryColumns(String columnName, DBType type) {
        	this.columnName = columnName;
        	this.type = type;
        }
        
        public String getName() {
        	return columnName;
        }
        public DBType getType() {
        	return type;
        }
    }
    
    public enum PledgeColumns {
    	_ID("_id", DBType.INT),
    	IDP("idp", DBType.INT),
        NAME("name", DBType.TEXT),
    	DESCRIPTION("description", DBType.TEXT),
    	STARRED("starred", DBType.INT);
        
    	private String columnName;
        private DBType type;
        
        PledgeColumns(String columnName, DBType type) {
        	this.columnName = columnName;
        	this.type = type;
        }
        
        public String getName() {
        	return columnName;
        }
        public DBType getType() {
        	return type;
        }
    }
    
    public static final String CONTENT_AUTHORITY = "com.yay.animator";
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    private static final String PATH_GAMES = "games";
    private static final String PATH_POETRY = "poetry";
    private static final String PATH_PLEDGE = "pledge";
    private static final String PATH_ID = "id";
    private static final String PATH_IDP = "idp";
    private static final String PATH_STARRED = "starred";
    private static final String PATH_SEARCH = "search";
    
    /**
     * Games
     */
    public static class Games {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAMES).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.animator.games";

        /** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = GamesColumns.NAME + " ASC";

        public static Uri buildGameUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_ID).appendPath(id).build();
        }

        public static Uri buildGamesUri(String idp) {
            return CONTENT_URI.buildUpon().appendPath(PATH_IDP).appendPath(idp).build();
        }

        public static Uri buildSearchUri(String query) {
            return CONTENT_URI.buildUpon().appendPath(PATH_SEARCH).appendPath(query).build();
        }
        
        public static Uri buildStarredUri() {
            return CONTENT_URI.buildUpon().appendPath(PATH_STARRED).build();
        }

        public static String getSearchQuery(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }
    
    /**
     * Poetry
     */
    public static class Poetry {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_POETRY).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.animator.poetry";

        /** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = GamesColumns.NAME + " ASC";

        public static Uri buildPoetryUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_ID).appendPath(id).build();
        }

        public static Uri buildPoetriesUri(String idp) {
            return CONTENT_URI.buildUpon().appendPath(PATH_IDP).appendPath(idp).build();
        }

        public static Uri buildSearchUri(String query) {
            return CONTENT_URI.buildUpon().appendPath(PATH_SEARCH).appendPath(query).build();
        }
        
        public static Uri buildStarredUri() {
            return CONTENT_URI.buildUpon().appendPath(PATH_STARRED).build();
        }

        public static String getSearchQuery(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }
    
    /**
     * Pledge
     */
    public static class Pledge {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_PLEDGE).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.animator.pledge";

        /** Default "ORDER BY" clause. */
        public static final String DEFAULT_SORT = GamesColumns.NAME + " ASC";

        public static Uri buildPledgeUri(String id) {
            return CONTENT_URI.buildUpon().appendPath(PATH_ID).appendPath(id).build();
        }

        public static Uri buildPledgesUri(String idp) {
            return CONTENT_URI.buildUpon().appendPath(PATH_IDP).appendPath(idp).build();
        }

        public static Uri buildSearchUri(String query) {
            return CONTENT_URI.buildUpon().appendPath(PATH_SEARCH).appendPath(query).build();
        }
        
        public static Uri buildStarredUri() {
            return CONTENT_URI.buildUpon().appendPath(PATH_STARRED).build();
        }

        public static String getSearchQuery(Uri uri) {
            return uri.getPathSegments().get(2);
        }

    }

    private GamesContract() {
    }
    
}
