package com.invent.inms;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.invent.inms.helper.Constants;

import java.util.ArrayList;
import java.util.Arrays;

import inms.DetectionGrpc;
import inms.Inms;
import inms.invent.com.i_invent_inms.R;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import static com.invent.inms.MainActivity.bundle;

public class AddIngredients extends Activity {
    Button addMoreIngredients;
    Button proceedAddedIngredientsButton;
    private ArrayList<String> ingredients;
    EditText addIngredient ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingredients);
        bundle = getIntent().getExtras();
        ingredients = new ArrayList<String>(Arrays.asList(bundle.getStringArray("ingredientsList")));
        addIngredient = findViewById(R.id.add_ingredient);
        addMoreIngredients = findViewById(R.id.add_more);
        proceedAddedIngredientsButton = findViewById(R.id.proceed_add);
        addMoreIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addIngredientsToTheExistingArray();
            }
        });
        proceedAddedIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateIngredientsList();
            }
        });
    }

    private void updateIngredientsList() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(Constants.INMS_HOST, Constants.INMS_PORT).usePlaintext().build();
        inms.DetectionGrpc.DetectionBlockingStub detectionBlockingStub = DetectionGrpc.newBlockingStub(channel);
        try{
        Inms.DetectedIngredientsResponse detectedIngredientsResponse = detectionBlockingStub.updateDetectedIngredients(Inms.DetectedIngredientsRequest.newBuilder().setContainerId(Constants.CONTAINER_ID).addAllIngredients(ingredients).build());
        if (detectedIngredientsResponse.getStatus().getStatusCode() == Inms.StatusCode.SUCCESS) {
            Toast.makeText(this, "Ingredients added Successfully", Toast.LENGTH_LONG).show();
            directToMainActivity();
        }
        else {
            Toast.makeText(this,"unable to add Ingredients",Toast.LENGTH_LONG).show();
        }
        }catch (Exception e){
            Toast.makeText(this,"Exception while adding Ingredients",Toast.LENGTH_LONG).show();
        }

    }

    private void directToMainActivity() {
        Intent intent = new Intent(AddIngredients.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void addIngredientsToTheExistingArray() {
        String text = addIngredient.getText().toString();
        if(text!=null){
            ingredients.add(text);
            addIngredient.setText("");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        directToMainActivity();
    }
}
