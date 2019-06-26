package com.example.fridge;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

// Custom camera kit https://camerakit.io/
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

// Class for identifying objects add them to a list

public class Scanning extends AppCompatActivity {

    // Pre trained TensorFlow lite model
    private static final String MODEL_PATH = "mobilenet_quant_v1_224.tflite";
    private static final boolean QUANT = true;

    // Label list with all identifiable objects
    private static final String LABEL_PATH = "labels.txt";

    // Input image size
    private static final int INPUT_SIZE = 224;

    private Classifier classifier;

    private Executor executor = Executors.newSingleThreadExecutor();

    // "Scanning" button
    private Button btnDetectObject;

    private CameraView cameraView;
    private TextView getTitle;
    private String result;
    public static ArrayList<String> ingredientList = new ArrayList<>();

    // Adding ingredients to list
    private FloatingActionButton add_button;

    private String add;
    private String already_added;
    private String added_to_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        cameraView = findViewById(R.id.cameraView);
        getTitle = findViewById(R.id.ingredient);
        add_button = findViewById(R.id.fab);

        // Show add button first when an object is detected
        add_button.hide();

        btnDetectObject = findViewById(R.id.btnDetectObject);

        // Custom camera kit
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {
            }

            @Override
            public void onError(CameraKitError cameraKitError) {
            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                Bitmap bitmap = cameraKitImage.getBitmap();

                // Scaling bitmap to 224x224 for TensorFlow
                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);

                // Get (detected object title) result from bitmap
                final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);

                add_button.show();
                result = results.get(0).getTitle();

                // Checking if detected object is already scanned or not
                if (ingredientList.contains(result)) {
                    already_added = result + " already in list";
                    getTitle.setText(already_added);

                } else {
                    add = "Add " + result;
                    getTitle.setText(add);
                }

                // Adding detected object to list
                add_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        result = results.get(0).getTitle();

                        if (!ingredientList.contains(result)) {
                            ingredientList.add(result);
                            added_to_list = result + " added to list";
                            getTitle.setText(added_to_list);
                        } else {
                            already_added = result + " already in list";
                            getTitle.setText(already_added);
                        }
                    }
                });
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {
            }
        });

        // Clicking to scan new ingredient
        btnDetectObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.captureImage();
            }
        });
        initAndLoadTensorFlowModel();
    }


    @Override
    protected void onResume() {
        super.onResume();
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }

    private void initAndLoadTensorFlowModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE,
                            QUANT);
                    makeButtonVisible();
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    // Only show scan button if TensorFlow was loaded successfully
    private void makeButtonVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btnDetectObject.setVisibility(View.VISIBLE);
            }
        });
    }

    public void goToIngredientList(View v) {
        Intent intent = new Intent(this, IngredientList.class);
        intent.putExtra("ingredientList", ingredientList);
        startActivity(intent);
    }
}