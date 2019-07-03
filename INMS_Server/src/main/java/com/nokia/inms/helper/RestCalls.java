package com.nokia.inms.helper;

import com.nokia.inms.common.Constants;
import com.nokia.inms.db.DBOperations;
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
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.158.100.6", 8080));
            conn = (HttpURLConnection) url.openConnection(proxy);
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

    public static String urlBuilder(List<String> ingredients){
        ApiKey apiKey = DBOperations.getApiKey(new Random().nextInt(200));
      StringBuilder recipeUrlBuilder = new StringBuilder(Constants.EDAMAM_URL);
        recipeUrlBuilder.append(String.join("/",ingredients));
        recipeUrlBuilder.append(Constants.API_KEY);
        recipeUrlBuilder.append(apiKey.getApiKey());
        recipeUrlBuilder.append(Constants.API_VALUE);
        recipeUrlBuilder.append(apiKey.getApiValue());
        recipeUrlBuilder.append(Constants.JSON);
        return recipeUrlBuilder.toString();
    }
}
