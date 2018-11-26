package com.nokia.inms.requests;

import com.nokia.inms.db.DBOperations;
import inms.Inms;
import inms.Inms.DetectedIngredientsResponse;
import inms.Inms.DetectedIngredientsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UpdateDetectedIngredients {
    DetectedIngredientsRequest detectedIngredients;
    private static Logger logger = LoggerFactory.getLogger(UpdateDetectedIngredients.class);
    public UpdateDetectedIngredients(DetectedIngredientsRequest detectedIngredientsRequest) {
        this.detectedIngredients = detectedIngredientsRequest;
    }

    public DetectedIngredientsResponse updateIngredients() {
        logger.info("request received for adding Ingredients :{}", detectedIngredients.getAllFields());
        if(DBOperations.addIngredients(detectedIngredients)){
            return createResponseForAddIngredientsRequest(Inms.StatusCode.SUCCESS,"Ingredients added!!");
        }
        return createResponseForAddIngredientsRequest(Inms.StatusCode.FAILURE,"Ingredients adding failed- please " +
                "try again!!");
    }

    private DetectedIngredientsResponse createResponseForAddIngredientsRequest(Inms.StatusCode statusCode, String
            description) {
        DetectedIngredientsResponse addIngredientsResponse = DetectedIngredientsResponse.newBuilder().setStatus(Inms.Status.newBuilder().setStatusCode
                (statusCode).setDescription(description).build()).build();
        logger.info("response sent for adding Ingredients :{}", addIngredientsResponse.getAllFields());
        return addIngredientsResponse;
    }
}
