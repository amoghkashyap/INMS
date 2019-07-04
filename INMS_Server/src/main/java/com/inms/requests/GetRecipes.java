package com.inms.requests;

import com.inms.common.Constants;
import com.inms.helper.RestCalls;
import inms.Inms;
import inms.Inms.GetRecipesRequest;
import inms.Inms.GetRecipesResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class GetRecipes {
    private String containerId;
    private List<String> ingredients;
    private List<String> healthLabels;
    private GetRecipesRequest getRecipesRequest;
    private static Logger logger = LoggerFactory.getLogger(GetRecipes.class);
    JSONObject recipeResponse;
    private String recipeName = Constants.EMPTY_STRING;
    private List<String> ingredientsList ;
    private String imageUrl = Constants.EMPTY_STRING;
    private String preparationTime = Constants.EMPTY_STRING;
    private String calorieCount = Constants.EMPTY_STRING;
    private String preparationUrl = Constants.EMPTY_STRING;

    public GetRecipes(GetRecipesRequest request) {
        this.getRecipesRequest = request;
        this.containerId = request.getContainerId();
        this.ingredients = request.getIngredientsList();
        this.healthLabels = request.getHealthLabelsList();
    }

    public GetRecipesResponse getRecipes() {
        logger.info("request received for adding Ingredients :{}",getRecipesRequest.getAllFields());
        List<Inms.Recipes> recipeFinalList = new ArrayList<>();
        try {
            String recipeUrl = RestCalls.urlBuilder(ingredients,healthLabels);
            logger.error("request received for adding Ingredients :{}",recipeUrl);
            recipeResponse = RestCalls.getCall(recipeUrl);
            logger.error("request received for adding Ingredients :{}",recipeResponse);
            if (recipeResponse.length() == 0) {
                return createGetRecipeResponse(Inms.StatusCode.NOT_FOUND,"No recipes Found", recipeFinalList);
            }
            return buildResponse(recipeResponse, Inms.StatusCode.SUCCESS, "List found");
        }catch (Exception e){
            logger.info("Exception occurred while getting recipes information ",e);
            return createGetRecipeResponse(Inms.StatusCode.FAILURE,"Recipes Not Found",recipeFinalList);
        }
    }

    private GetRecipesResponse buildResponse(JSONObject recipeResponse, Inms.StatusCode statusCode, String
            description) {
        List<Inms.Recipes> recipeFinalList = new ArrayList<>();
        try {
            JSONArray recipeList = recipeResponse.getJSONArray("hits");
            for (int i = 0; i < recipeList.length(); i++) {
                JSONObject recipeJson = recipeList.getJSONObject(i).getJSONObject("recipe");
                initialiseValues(recipeJson);
                Inms.Recipes recipes = Inms.Recipes.newBuilder().setCalorieCount(Double.parseDouble(calorieCount)).setImageUrl(imageUrl)
                        .addAllIngredients(ingredientsList).setPreparationTime(preparationTime).setRecipeName
                                (recipeName).setPreparationUrl(preparationUrl).build();
                recipeFinalList.add(recipes);
            }
            return createGetRecipeResponse(statusCode, description, recipeFinalList);
        }catch (Exception e){
            logger.info("Exception occurred while getting recipes information ",e);
            return createGetRecipeResponse(Inms.StatusCode.FAILURE,e.getMessage(), recipeFinalList);
        }
    }

    private GetRecipesResponse createGetRecipeResponse(Inms.StatusCode statusCode, String description, List<Inms.Recipes> recipeFinalList) {
        GetRecipesResponse getRecipesResponse;
        getRecipesResponse = GetRecipesResponse.newBuilder().setStatus(Inms.Status.newBuilder()
                .setStatusCode(statusCode).setDescription(description).build()).addAllRecipes(recipeFinalList).build();
        logger.info("response sent to get recipes request :{}",getRecipesResponse);
        return getRecipesResponse;
    }

    private void initialiseValues(JSONObject recipeJson) {
        try {
            recipeName = recipeJson.getString("label");
            ingredientsList = getIngredientsList(recipeJson);
            imageUrl = recipeJson.getString("image");
            preparationTime = getTotalTime(recipeJson.getString("totalTime"));
            calorieCount = String.valueOf(Math.round(recipeJson.getDouble("calories")));
            preparationUrl = recipeJson.getString("url");
        }catch (Exception e){
            logger.info("Exception occurred while getting recipes information ",e);
        }
    }

    private String getTotalTime(String totalTime) {
        if(totalTime.startsWith("0")){
            return Constants.DEFAULT_PREPARATION_TIME;
        }
        return totalTime;
    }

    private List<String> getIngredientsList(JSONObject recipeJson) throws JSONException {
        JSONArray ingredientJsonArray = recipeJson.getJSONArray("ingredients");
        ingredientsList = new ArrayList<>();
        for (int i =0;i<ingredientJsonArray.length();i++){
            JSONObject jsonObject = ingredientJsonArray.getJSONObject(i);
            ingredientsList.add(jsonObject.getString("text"));
        }
        return ingredientsList;
    }
}
