package com.inms.requests;

import com.datastax.driver.core.Row;
import com.inms.common.Constants;
import com.inms.db.DBOperations;
import inms.Inms;
import inms.Inms.GetIngredientsRequest;
import inms.Inms.GetIngredientsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GetIngredients {
    private final GetIngredientsRequest getIngredientsRequest;
    private String containerId ;
    private static Logger logger = LoggerFactory.getLogger(GetIngredients.class);

    public GetIngredients(GetIngredientsRequest request) {
        this.containerId = request.getContainerId();
        this.getIngredientsRequest = request;
    }

    public GetIngredientsResponse fetchIngredients() {
        logger.info("request received for GetIngredients :{}", getIngredientsRequest.getAllFields());
        System.out.println("Get Ingredients request received  :"+ getIngredientsRequest.getContainerId());
        try {
            Row getIngredients = DBOperations.getIngredients(containerId);
            List<String> ingredients = getIngredients.getList(Constants.INGREDIENT, String.class);

            if (ingredients.size() == 0) {
                return createResponseForGetIngredients(Inms.StatusCode.SUCCESS, "Ingredients Not Found!!", ingredients);
            }
            return createResponseForGetIngredients(Inms.StatusCode.SUCCESS, "Ingredients  Found!!", ingredients);
        } catch (Exception e) {
            return createResponseForGetIngredients(Inms.StatusCode.NOT_FOUND, "Ingredients not Found!!", null);

        }
    }

    private GetIngredientsResponse createResponseForGetIngredients(Inms.StatusCode success, String description, List<String>
            ingredientsResponse) {
        GetIngredientsResponse response = GetIngredientsResponse.newBuilder().setStatus(Inms.Status.newBuilder().setStatusCode(success)
                .setDescription(description).build()).addAllIngredients(ingredientsResponse).build();
        logger.info("response sent for GetIngredients :{}",response.getAllFields());
        System.out.println("Get Ingredients Response sent:"+ response.getStatus());
        return response;

    }
}
