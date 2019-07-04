package com.inms.helper;

import com.inms.common.Constants;
import com.inms.db.DBOperations;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;
import java.util.Random;


public class RestCalls {

    public static JSONObject getCall(String EdamamGetUrl) {
        BufferedReader recipeBufferReaderResponse = null;
        HttpURLConnection conn = null;
        JSONObject recipeJsonResponse = null;
        try {
            URL url = new URL(EdamamGetUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }
            recipeBufferReaderResponse = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));
            String recipeStringResult;
            StringBuffer responseBuffer = new StringBuffer();
            while ((recipeStringResult = recipeBufferReaderResponse.readLine()) != null) {
                responseBuffer.append(recipeStringResult);
            }
            recipeJsonResponse = new JSONObject(responseBuffer.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return recipeJsonResponse;
    }

    public static String urlBuilder(List<String> ingredients, List<String> healthLabels){
        int apiId = new Random().nextInt(200);
        if (apiId == 0 ){
            apiId = 1;
        }
        ApiKey apiKey = DBOperations.getApiKey(apiId);
        StringBuilder recipeUrlBuilder = new StringBuilder(Constants.URL);
        recipeUrlBuilder.append(String.join("/",ingredients));
        recipeUrlBuilder.append(Constants.URL_API_KEY);
        recipeUrlBuilder.append(apiKey.getApiKey());
        recipeUrlBuilder.append(Constants.URL_API_VALUE);
        recipeUrlBuilder.append(apiKey.getApiValue());
        if(healthLabels!= null && healthLabels.size()!=0) {
            recipeUrlBuilder.append(Constants.URL_HEALTH_LABEL);
            recipeUrlBuilder.append(String.join(Constants.URL_HEALTH_LABEL, healthLabels));
        }
        recipeUrlBuilder.append(Constants.JSON);
        return recipeUrlBuilder.toString();
    }
}
