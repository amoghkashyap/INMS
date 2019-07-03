package com.nokia.inms.requests;

import com.nokia.inms.common.Constants;
import com.nokia.inms.db.DBOperations;
import inms.Inms;
import inms.Inms.GetIngredientsRequest;
import inms.Inms.GetIngredientsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;

public class GetIngredients {
    private final GetIngredientsRequest getIngredientsRequest;
    private String containerId ;
    private static Logger logger = LoggerFactory.getLogger(GetIngredients.class);

    public GetIngredients(GetIngredientsRequest request) {
        this.containerId = request.getContainerId();
        this.getIngredientsRequest = request;
    }

    public GetIngredientsResponse fetchIngredients() {
        System.out.println("request received for GetIngredients :{}"+getIngredientsRequest.getAllFields());
        String IngredientsResponse = DBOperations.getIngredients(containerId);
        if(IngredientsResponse != Constants.EMPTY_STRING){
            return createResponseForGetIngredients(Inms.StatusCode.SUCCESS,"Ingredients Found!!",IngredientsResponse);
        }
        return createResponseForGetIngredients(Inms.StatusCode.NOT_FOUND, "Ingredients not Found!!", IngredientsResponse);
    }

    private GetIngredientsResponse createResponseForGetIngredients(Inms.StatusCode success, String description, String
            ingredientsResponse) {
        Iterable<String> ingredients = Collections.singleton(ingredientsResponse);
        GetIngredientsResponse response = GetIngredientsResponse.newBuilder().setStatus(Inms.Status.newBuilder().setStatusCode(success)
                .setDescription(description).build()).addAllIngredients(ingredients).build();
        logger.info("response sent for GetIngredients :{}",response.getAllFields());
        return response;

    }
}
