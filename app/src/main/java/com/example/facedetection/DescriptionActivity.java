package com.example.facedetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DescriptionActivity extends AppCompatActivity {
    TextView smile;
    TextView leftEar;
    TextView rightEar;
    TextView leftEye;
    TextView rightEye;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);
        smile = findViewById(R.id.smiling);
//        leftEar = findViewById(R.id.left_ear);
//        rightEar = findViewById(R.id.right_ear);
//        leftEye = findViewById(R.id.left_eye);
//        rightEye = findViewById(R.id.right_eye);

        Intent intent = getIntent();
        float smileProb = intent.getFloatExtra("smile",0);
//        String lEar = intent.getStringExtra("lEar");
//        String rEar = intent.getStringExtra("rEar");
//        String lEye = intent.getStringExtra("lEye");
//        String rEye = intent.getStringExtra("rEye");
        smile.setText(Float.toString(smileProb));
//        leftEye.setText(lEye);
//        leftEar.setText(lEar);
//        rightEye.setText(rEye);
//        rightEar.setText(rEar);


    }
}
