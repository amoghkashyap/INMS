package com.invent.inms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.invent.inms.request.RecipeList;

import java.util.ArrayList;
import java.util.List;

import inms.invent.com.i_invent_inms.R;

public class HealthOption extends Activity {

    String value;
    String[] healthOptions;
    private Button getRecipesButton;
    private static Bundle bundle;
    private static String[] ingredients;
    static List <String> healthOptionsSelected  = new ArrayList<>();
    private CheckedTextView healthOptionCheckList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.heath_options_activity);
        // initiate a ListView
        healthOptions =  new String[]{"balanced","high-protein","low-fat","low-carb","alcohol-free","vegan","vegetarian","sugar-conscious","tree-nut-free","peanut-free"} ;
        ListView listView = (ListView) findViewById(R.id.healthOptionList);
        // set the adapter to fill the data in ListView
        HealthOptionsAdapter customAdapter = new HealthOptionsAdapter(getApplicationContext(), healthOptions);
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                healthOptionCheckList = (CheckedTextView) view.findViewById(R.id.HealthOptionCheckedText);
                healthOptionCheckList.setText(healthOptions[position]);
                if (!healthOptionCheckList.isChecked()) {
                    value = "Checked";
                    healthOptionCheckList.setCheckMarkDrawable(R.drawable.check_mark);
                    healthOptionCheckList.setChecked(true);
                    healthOptionsSelected.add(healthOptions[position]);
                } else {
                    value = "un-Checked";
                    healthOptionCheckList.setCheckMarkDrawable(0);
                    healthOptionCheckList.setChecked(false);
                    healthOptionsSelected.remove(healthOptions[position]);
                }
            }
        });
        getRecipesButton = findViewById(R.id.healthOptionButton);
        getRecipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processUponHealthOptions();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(healthOptionsSelected.size()!=0){
            healthOptionsSelected.clear();
        }
        finish();
    }

    private void processUponHealthOptions() {

        Intent intent = new Intent(this,RecipeList.class);
        bundle = getIntent().getExtras();
        ingredients = bundle.getStringArray("ingredientsList");
        bundle.putStringArray("ingredientsList", ingredients );
        bundle.putStringArray("heathOptionSelected",healthOptionsSelected.toArray(new String[healthOptionsSelected.size()]) );
        intent.putExtras(bundle);
        startActivity(intent);
    }
}