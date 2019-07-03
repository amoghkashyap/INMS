package com.nokia.inms.common;

public final class Constants {
    public static final int INMS_PORT = 50051;
    public static final String EDAMAM_URL = "https://api.edamam.com/search?q=";
    public static final String EDAMAM_RECIPE_KEY = "efe12dd6762fc08568a20dc09390176c";
    public static final String EDAMAM_RECIPE_APP_ID = "e7752d50";
    public static final String API_KEY = "&app_id=";
    public static final String API_ID = "api_id";
    public static final String API_VALUE = "&app_key=";
    public static final int CASSANDRA_PORT = 9042;
    public static final String CASSANDRA_HOST = "127.0.0.1";
    public static final String CONTAINER_ID = "container_id";
    public static final String KEYSPACE = "inms";
    public static final String TABLE_NAME = "ingredients";
    public static final String API_TABLE_NAME ="api_keys";
    public static final String INGREDIENT = "ingredient";
    public static final String EMPTY_STRING = "";
    public static final String JSON = "&format=json" ;
    public static String KEYSPACE_WITH_TABLE = KEYSPACE+"."+TABLE_NAME;
    public static final String DEFAULT_PREPARATION_TIME = "30";
    public static String API_KEYSPACE_WITH_TABLE = KEYSPACE+"."+API_TABLE_NAME;
    public static String CREATE_KEYSPACE_QUERY = "CREATE KEYSPACE IF NOT EXISTS "+ KEYSPACE+" WITH " +
            "replication = " +
            "{'class':'SimpleStrategy', 'replication_factor':1};";
    public static String CREATE_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS "+KEYSPACE_WITH_TABLE+" (container_id " +
            "varchar PRIMARY " +
            "KEY,"+"ingredient varchar)";
    public static String CREATE_API_TABLE_QUERY = "CREATE TABLE IF NOT EXISTS "+API_KEYSPACE_WITH_TABLE+" " +
            "(api_id varchar PRIMARY KEY,"+"api_key text, api_value text)";
}
