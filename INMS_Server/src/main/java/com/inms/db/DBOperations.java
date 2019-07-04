package com.inms.db;

import com.datastax.driver.core.*;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.inms.common.Constants;
import com.inms.helper.ApiKey;
import inms.Inms.DetectedIngredientsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class DBOperations {

    private static Cluster cluster;
    private static Session session;
    private static Logger logger = LoggerFactory.getLogger(
            DBOperations.class);

    public static void initializeCassandra(){
        try {
            System.out.println("initializing DB ..");
            cluster = Cluster.builder().addContactPoints(Constants.CASSANDRA_HOST)
                    .withPort(Constants.CASSANDRA_PORT).build();
            session = cluster.connect();
            if(createTableWithKeyspace()){
                System.out.println("keyspace created !!");
            }
        }catch (Exception e){
            logger.error("Exception occurred while creating the DB connection",e);
        }
    }

    private static void closeDBConnection() {
        logger.info("closing DB session ");
        session.close();
    }

    private static boolean createTableWithKeyspace() {
        boolean isOperationSuccessful = session.execute(Constants.CREATE_KEYSPACE_QUERY).wasApplied() && session.execute
                (Constants.CREATE_TABLE_QUERY).wasApplied() && session.execute(Constants.CREATE_API_TABLE_QUERY).wasApplied() && session.execute
                (Constants.CREATE_API_TABLE_QUERY).wasApplied() ;
        logger.info("Creating Keyspace {} and Table {} was succesfull:{}",Constants.CREATE_KEYSPACE_QUERY,Constants
                .CREATE_TABLE_QUERY,isOperationSuccessful);
        return isOperationSuccessful;
    }

    public static boolean addIngredients(DetectedIngredientsRequest detectedIngredientsRequest){
        try {
            truncateDBEntries();
            System.out.println("adding detected ingredients to the DB:{}" + detectedIngredientsRequest.getAllFields());
            Statement insertQuery = QueryBuilder.insertInto(Constants.KEYSPACE, Constants.TABLE_NAME).value(Constants
                    .DB_CONTAINER_ID, detectedIngredientsRequest.getContainerId().toString()).value(Constants.INGREDIENT,
                    detectedIngredientsRequest.getIngredientsList()).ifNotExists();
           return session.execute(insertQuery).wasApplied();
        }catch (Exception e){
            logger.info("exception",e.getMessage());
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Row getIngredients(String containerId){
        logger.info("fetching ingredients for the containerId:{}",containerId);
        Statement fetchIngredients = QueryBuilder.select().from(Constants.KEYSPACE,Constants.TABLE_NAME).where(QueryBuilder.eq(
                Constants.DB_CONTAINER_ID, containerId));
        List<Row> rows = session.execute(fetchIngredients).all();
        logger.info("data retrieved from the DB :{}",rows);
        return rows.get(0);
    }

    public static boolean truncateDBEntries() {
        logger.info("removing the old entries..");
        Statement truncateTables = QueryBuilder.truncate(Constants.KEYSPACE,Constants.TABLE_NAME);
        return session.execute(truncateTables).wasApplied();
    }

    public static ApiKey getApiKey(int apiId){
        logger.info("fetching ingredients for the containerId:{}",apiId);
        try {
            Statement fetchApiKeys = QueryBuilder.select().from(Constants.KEYSPACE, Constants.API_TABLE_NAME).where
                    (QueryBuilder.eq(Constants.DB_API_ID, apiId));
            List<Row> rows = session.execute(fetchApiKeys).all();
            logger.info("data retrieved from the DB :{}", rows);
            String apiKey = rows.get(0).getString(Constants.DB_API_KEY);
            String apiValue = rows.get(0).getString(Constants.DB_API_VALUE);
            ApiKey apiKeyObject = new ApiKey(apiKey, apiValue);
            return apiKeyObject;
        }catch (Exception e){
            logger.info("getApiKey Exception :!!"+apiId+":",e.getMessage());
            System.out.println("getApiKey Exception :!!"+e.getMessage());
        }
      return null;
    }
}
