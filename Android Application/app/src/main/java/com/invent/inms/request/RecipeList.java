package com.invent.inms.request;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.invent.inms.helper.Constants;
import com.invent.inms.helper.GetRecipes;
import java.util.Arrays;
import java.util.List;

import inms.BackendRequestsGrpc;
import inms.Inms;
import inms.invent.com.i_invent_inms.R;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class RecipeList extends Activity{

    private Bundle bundle;

    private String[] ingredients;
    private static String[] healthLables;
    private List<String> recipesNames;
    private Inms.Recipes recipeObject;
    Inms.GetRecipesResponse getRecipesResponse;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        showProgressDialogWithTitle("Please wait while fetching Recipes of your choice!");
        setContentView(R.layout.recipe_menu);
        // set the adapter to fill the data in ListView
        bundle = getIntent().getExtras();
        //recipes = new String[]{"Onion", "Bell Pepper", "Carret", "Egg", "Tomato"};
        healthLables = bundle.getStringArray("heathOptionSelected");
        ingredients  = bundle.getStringArray("ingredientsList");
        getRecipesResponse = GetRecipesResponse(ingredients, healthLables);
        recipesNames = GetRecipes.getRecipeName(getRecipesResponse);
        if(recipesNames.size()!=0) {
        hideProgressDialogWithTitle();

            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.recipe_list, recipesNames);
            ListView listView = (ListView) findViewById(R.id.recipe_list);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    processUponGetRecipe(getRecipesResponse,recipesNames.get(position));
                }
            });
        }
        else {
            hideProgressDialogWithTitle();
            Toast.makeText(this, "Recipes Not Found !!", Toast.LENGTH_LONG).show();
        }
    }

    private void showProgressDialogWithTitle(String substring) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage(substring);
        progressDialog.show();
    }

    // Method to hide/ dismiss Progress bar
    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }
    private void processUponGetRecipe(Inms.GetRecipesResponse getRecipesResponse, String recipeName) {
        Intent intent = new Intent(RecipeList.this,RecipeDescription.class);
        recipeObject = GetRecipes.getRecipeObject(getRecipesResponse,recipeName);
        bundle.putString("recipeImageUrl",recipeObject.getImageUrl());
        bundle.putString("recipeName",recipeObject.getRecipeName());
        bundle.putString("preparationTime",recipeObject.getPreparationTime());
        bundle.putString("preparationUrl",recipeObject.getPreparationUrl());
        bundle.putDouble("calorieCount",recipeObject.getCalorieCount());
        bundle.putStringArray("ingredients", recipeObject.getIngredientsList().toArray(new String[recipeObject.getIngredientsList().size()]));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private Inms.GetRecipesResponse GetRecipesResponse(String[] ingredients, String[] healthLables) {
        ManagedChannel channel;
        Inms.GetRecipesResponse getRecipesResponse = null;
        try{
        channel = ManagedChannelBuilder.forAddress(Constants.INMS_HOST, Constants.INMS_PORT).usePlaintext().build();
        BackendRequestsGrpc.BackendRequestsBlockingStub backendRequestsBlockingStub = BackendRequestsGrpc.newBlockingStub(channel);
        getRecipesResponse = backendRequestsBlockingStub.getRecipes(getRecipeRequestBuilder(ingredients,healthLables));
        return getRecipesResponse;}
        catch (Exception e){
            return getRecipesResponse;
        }
    }

    private Inms.GetRecipesRequest getRecipeRequestBuilder(String[] ingredients, String[] healthLables) {
        return Inms.GetRecipesRequest.newBuilder().addAllHealthLabels(Arrays.asList(healthLables)).addAllIngredients(Arrays.asList(ingredients)).setContainerId("inms").build();
    }
}
