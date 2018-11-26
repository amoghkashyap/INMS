package com.nokia.inms.helper;

import com.nokia.inms.common.Constants;
import org.json.JSONException;
import org.json.JSONObject;
import org.stringtemplate.v4.ST;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;


public class RestCalls {

    public static JSONObject getCall(String EdamamGetUrl) {
        BufferedReader recipeBufferReaderResponse = null;
        HttpURLConnection conn = null;
        JSONObject recipeJsonResponse = null;
        try {
            URL url = new URL(EdamamGetUrl);
            //Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("10.158.100.6", 8080))
            // ;
            //conn = (HttpURLConnection) url.openConnection(proxy);
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
        System.out.println("hello phase 1 url generate Recipe hit!!"+healthLabels);
        StringBuilder recipeUrlBuilder = new StringBuilder(Constants.EDAMAM_URL);
        recipeUrlBuilder.append(String.join("&q=",ingredients));
        recipeUrlBuilder.append(Constants.EDAMAM_KEY);
        if(healthLabels!= null && healthLabels.size()!=0) {
            recipeUrlBuilder.append("&health=");
            recipeUrlBuilder.append(String.join("&health=", healthLabels));
        }
        recipeUrlBuilder.append(Constants.JSON);
        System.out.println("hello phase 1 url generation ending  Recipe hit!!"+ recipeUrlBuilder.toString());
        return recipeUrlBuilder.toString();
    }
}
