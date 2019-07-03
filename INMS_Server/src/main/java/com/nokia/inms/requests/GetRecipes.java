package com.nokia.inms.requests;

import com.nokia.inms.common.Constants;
import com.nokia.inms.helper.RestCalls;
import inms.Inms;
import inms.Inms.GetRecipesRequest;
import inms.Inms.GetRecipesResponse;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetRecipes {
    private List<String> allergicIngredients;
    private String containerId;
    private List<String> ingredients;
    private GetRecipesRequest getRecipesRequest;
    private static Logger logger = LoggerFactory.getLogger(GetRecipes.class);
    JSONObject recipeResponse;
    private String recipeName = Constants.EMPTY_STRING;
    private String ingredientsList = Constants.EMPTY_STRING;
    private String imageUrl = Constants.EMPTY_STRING;
    private String preparationTime = Constants.EMPTY_STRING;
    private String calorieCount = Constants.EMPTY_STRING;

    public GetRecipes(GetRecipesRequest request) {
    this.getRecipesRequest = request;
    this.containerId = request.getContainerId();
    this.ingredients = request.getIngredientsList();
    this.allergicIngredients = request.getAllergicIngredientsList();
    }

    public GetRecipesResponse getRecipes() {
        logger.info("request received for adding Ingredients :{}",getRecipesRequest.getAllFields());
        try {
           if (isIngredientsAllergicPresent(allergicIngredients)) {
                ingredients = removeFromIngredientsList();
            }
            String recipeUrl = RestCalls.urlBuilder(ingredients);
            recipeResponse = RestCalls.getCall(recipeUrl);
            if (recipeResponse.length() == 0) {
                return createGetRecipeResponse(Inms.StatusCode.NOT_FOUND,"No recipes Found", null);
            }
            return buildResponse(recipeResponse, Inms.StatusCode.SUCCESS, "List found");
        }catch (Exception e){
            logger.info("Exception occurred while getting recipes information ",e);
            return createGetRecipeResponse(Inms.StatusCode.FAILURE,e.getMessage(), null);
        }
    }

    private GetRecipesResponse buildResponse(JSONObject recipeResponse, Inms.StatusCode statusCode, String
            description) {
        try {
            List<Inms.Recipes> recipeFinalList = new ArrayList<>();
            JSONArray recipeList = recipeResponse.getJSONArray("hits");

           for (int i = 0; i < recipeList.length(); i++) {
           JSONObject recipeJson = recipeList.getJSONObject(i).getJSONObject("recipe");
           initialiseValues(recipeJson);
               Inms.Recipes recipes = Inms.Recipes.newBuilder().setCalorieCount(Double.parseDouble(calorieCount)).setImageUrl(imageUrl)
                       .addAllIngredients(Arrays.asList(ingredientsList.split(","))).setPreparationTime(preparationTime).setRecipeName
                               (recipeName).build();
               recipeFinalList.add(recipes);
           }
            return createGetRecipeResponse(statusCode, description, recipeFinalList);
        }catch (Exception e){
            logger.info("Exception occurred while getting recipes information ",e);
            return createGetRecipeResponse(Inms.StatusCode.FAILURE,e.getMessage(), null);
        }
    }

    private GetRecipesResponse createGetRecipeResponse(Inms.StatusCode statusCode, String description, List<Inms.Recipes> recipeFinalList) {
        GetRecipesResponse getRecipesResponse;
        getRecipesResponse = GetRecipesResponse.newBuilder().setStatus(Inms.Status.newBuilder()
                .setStatusCode(statusCode).setDescription(description).build())
                .addAllRecipes(recipeFinalList).build();
        logger.info("response sent to get recipes request :{}",getRecipesResponse);
        return getRecipesResponse;
    }

    private void initialiseValues(JSONObject recipeJson) {
        try {
            recipeName = recipeJson.getString("label");
            ingredientsList = recipeJson.getString("ingredientLines");
            imageUrl = recipeJson.getString("image");
            preparationTime = recipeJson.getString("totalTime");
            calorieCount = recipeJson.getString("calories");
        }catch (Exception e){
            logger.info("Exception occurred while getting recipes information ",e);
        }
    }

    private boolean isIngredientsAllergicPresent(List<String> allergicIngredients) {
        return !allergicIngredients.isEmpty();
    }
    private List<String> removeFromIngredientsList() {
        for (String allergicIngredient : allergicIngredients){
            ingredients.remove(allergicIngredient);
        }
        return ingredients;
    }

}
