package com.invent.inms;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.invent.inms.helper.Constants;
import com.invent.inms.helper.CustomAdapter;
import com.invent.inms.helper.Ingredients;
import com.invent.inms.helper.IngredientsData;
import com.invent.inms.helper.SendImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import inms.invent.com.i_invent_inms.R;

public class MainActivity extends Activity{

    private static final String TAG = "MainActivity";
    String[] checkedText;
    TypedArray imageId;
    private static final int CAMERA_REQUEST = 1888;
    List<IngredientsData> ingredientsData;
    static ListView ingredientsList;
    CheckedTextView simpleCheckedTextView ;
    Button button;
    static Bundle bundle;
    private String value;
    static List<String> ingredients = new ArrayList<>();
    ProgressBar progressBar;
    private ProgressDialog progressDialog;
    ImageView scanImage;
    private String [] permissions = {"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.READ_PHONE_STATE", "android.permission.SYSTEM_ALERT_WINDOW","android.permission.CAMERA"};
    private String currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        showProgressDialogWithTitle("Please wait while,Fetching ingredients from the Refrigerator!");
        setContentView(R.layout.activity_main);
        int requestCode = 200;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
        Button refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Button addIngredients = (Button) findViewById(R.id.add);
        addIngredients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddIngredients.class);
                Bundle addIngredientBundle = new Bundle();
                addIngredientBundle.putStringArray("ingredientsList", ingredients.toArray(new String[ingredients.size()]) );
                intent.putExtras(addIngredientBundle);
                startActivity(intent);
            }
        });
        scanImage = findViewById(R.id.scan);
        scanImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCameraAndCapture();
            }
        });

        ingredientsData = new ArrayList<IngredientsData>();
        ingredients = new ArrayList<>(Ingredients.getIngredientsResponse());
        hideProgressDialogWithTitle();
        setUpView();
    }

    private void launchCameraAndCapture() {
        Intent takePictureIntent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile =  createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent,CAMERA_REQUEST);
            }
        }
    }

    private File createImageFile() {
        String imageFileName = "upload";
        File image = null;
        try {
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            image = File.createTempFile(imageFileName,".jpg",storageDir);
            currentImagePath = image.getPath();
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Exception occoured while capturing image",Toast.LENGTH_LONG).show();
        }
        return image;
    }

    private void setUpView() {
        if(ingredients.size()!=0) {
            checkedText = ingredients.toArray(new String[ingredients.size()]);
            int[] a = {R.drawable.onion, R.drawable.bellpepper, R.drawable.carrot, R.drawable.egg, R.drawable.tomato};
            imageId = obtainStyledAttributes(a);
            for (int i = 0; i < checkedText.length; i++) {
                IngredientsData item = new IngredientsData(getResourseId(this,checkedText[i],"drawable",getPackageName()),
                        checkedText[i]);
                ingredientsData.add(item);
            }
            //ingredients = new LinkedList<String>(Arrays.asList(checkedText));
            bundle = new Bundle();
            ingredientsList = (ListView) findViewById(R.id.ingredients_list);
            CustomAdapter adapter = new CustomAdapter(this, ingredientsData);
            ingredientsList.setAdapter(adapter);
            ingredientsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    simpleCheckedTextView = (CheckedTextView) view.findViewById(R.id.simpleCheckedTextView);
                    simpleCheckedTextView.setText(checkedText[position]);
                    if (simpleCheckedTextView.isChecked()) {
// set cheek mark drawable and set checked property to false
                        value = "un-Checked";
                        simpleCheckedTextView.setCheckMarkDrawable(0);
                        simpleCheckedTextView.setChecked(false);
                        ingredients.remove((String) simpleCheckedTextView.getText());
                    } else {
// set cheek mark drawable and set checked property to true
                        value = "Checked";
                        simpleCheckedTextView.setCheckMarkDrawable(R.drawable.check_mark);
                        simpleCheckedTextView.setChecked(true);
                        ingredients.add((String) simpleCheckedTextView.getText());
                    }
                }
            });

            button = (Button) findViewById(R.id.proceed);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(ingredients.size()!=0) {
                        processHealthOptions();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Please select at least One ingredient!!", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "No Ingredients Found!!", Toast.LENGTH_LONG).show();
        }
    }

    private void showProgressDialogWithTitle(String substring) {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //Without this user can hide loader by tapping outside screen
        progressDialog.setCancelable(false);
        progressDialog.setMessage(substring);
        progressDialog.show();
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Log.i(TAG, "RESULT CODE " + resultCode);
            if (resultCode == RESULT_OK) {
                if(currentImagePath.length() > 0)
                    new SendImage().sendImageToServer(currentImagePath);
            }else{
                currentImagePath = Constants.EMPTY_STRING;
                Toast.makeText(MainActivity.this,"Error capturing image",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    // Method to hide/ dismiss Progress bar
    private void hideProgressDialogWithTitle() {
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.dismiss();
    }
    private void processHealthOptions() {
        Intent intent = new Intent(this,HealthOption.class);
        bundle.putStringArray("ingredientsList", ingredients.toArray(new String[ingredients.size()]) );
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public static int getResourseId(Context context, String pVariableName, String pResourcename, String pPackageName) throws RuntimeException {
        try {
            return context.getResources().getIdentifier(pVariableName, pResourcename, pPackageName);
        } catch (Exception e) {
            throw new RuntimeException("Error getting Resource ID.", e);
        }
    }
}