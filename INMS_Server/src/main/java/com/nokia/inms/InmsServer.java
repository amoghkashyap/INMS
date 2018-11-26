package com.nokia.inms;
import com.nokia.inms.common.Constants;
import com.nokia.inms.db.DBOperations;
import com.nokia.inms.serviceImpl.BackendRequestImpl;
import com.nokia.inms.serviceImpl.DetectionServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class InmsServer {

    private static Logger logger = LoggerFactory.getLogger(InmsServer.class);

    public static void main(String[] args) throws IOException, InterruptedException {
        initializeDb();
        Server server = ServerBuilder.forPort(Constants.INMS_PORT).addService(new DetectionServiceImpl())
                .addService(new BackendRequestImpl()).build();
        server.start();
        System.out.println("server started");
        logger.info("Nutrition management service started.. listening in the port:{}",Constants.INMS_PORT);
        server.awaitTermination();
        }

    private static void initializeDb() {
        logger.info("Initializing the database instance..");
        DBOperations.initializeCassandra();
    }
}
