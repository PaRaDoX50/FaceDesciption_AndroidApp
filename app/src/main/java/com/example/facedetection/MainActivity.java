package com.example.facedetection;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionPoint;
import com.google.firebase.ml.vision.face.FirebaseVisionFace;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetector;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceDetectorOptions;
import com.google.firebase.ml.vision.face.FirebaseVisionFaceLandmark;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity {
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    FirebaseVisionImage image;
    Button button;

    //    List<FirebaseVisionFace> faces = new List<FirebaseVisionFace>() {
//        @Override
//        public int size() {
//            return 0;
//        }
//
//        @Override
//        public boolean isEmpty() {
//            return false;
//        }
//
//        @Override
//        public boolean contains(@Nullable Object o) {
//            return false;
//        }
//
//        @NonNull
//        @Override
//        public Iterator<FirebaseVisionFace> iterator() {
//            return null;
//        }
//
//        @NonNull
//        @Override
//        public Object[] toArray() {
//            return new Object[0];
//        }
//
//        @NonNull
//        @Override
//        public <T> T[] toArray(@NonNull T[] a) {
//            return null;
//        }
//
//        @Override
//        public boolean add(FirebaseVisionFace firebaseVisionFace) {
//            return false;
//        }
//
//        @Override
//        public boolean remove(@Nullable Object o) {
//            return false;
//        }
//
//        @Override
//        public boolean containsAll(@NonNull Collection<?> c) {
//            return false;
//        }
//
//        @Override
//        public boolean addAll(@NonNull Collection<? extends FirebaseVisionFace> c) {
//            return false;
//        }
//
//        @Override
//        public boolean addAll(int index, @NonNull Collection<? extends FirebaseVisionFace> c) {
//            return false;
//        }
//
//        @Override
//        public boolean removeAll(@NonNull Collection<?> c) {
//            return false;
//        }
//
//        @Override
//        public boolean retainAll(@NonNull Collection<?> c) {
//            return false;
//        }
//
//        @Override
//        public void clear() {
//
//        }
//
//        @Override
//        public FirebaseVisionFace get(int index) {
//            return null;
//        }
//
//        @Override
//        public FirebaseVisionFace set(int index, FirebaseVisionFace element) {
//            return null;
//        }
//
//        @Override
//        public void add(int index, FirebaseVisionFace element) {
//
//        }
//
//        @Override
//        public FirebaseVisionFace remove(int index) {
//            return null;
//        }
//
//        @Override
//        public int indexOf(@Nullable Object o) {
//            return 0;
//        }
//
//        @Override
//        public int lastIndexOf(@Nullable Object o) {
//            return 0;
//        }
//
//        @NonNull
//        @Override
//        public ListIterator<FirebaseVisionFace> listIterator() {
//            return null;
//        }
//
//        @NonNull
//        @Override
//        public ListIterator<FirebaseVisionFace> listIterator(int index) {
//            return null;
//        }
//
//        @NonNull
//        @Override
//        public List<FirebaseVisionFace> subList(int fromIndex, int toIndex) {
//            return null;
//        }
//    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.capture);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, MY_CAMERA_PERMISSION_CODE);
                } else {
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Log.d("thisisaboy", "onActivityResult: " + photo.toString());
            image = FirebaseVisionImage.fromBitmap(photo);
            FirebaseVisionFaceDetectorOptions highAccuracyOpts =
                    new FirebaseVisionFaceDetectorOptions.Builder()
                            .setPerformanceMode(FirebaseVisionFaceDetectorOptions.ACCURATE)
                            .setLandmarkMode(FirebaseVisionFaceDetectorOptions.ALL_LANDMARKS)
                            .setClassificationMode(FirebaseVisionFaceDetectorOptions.ALL_CLASSIFICATIONS)
                            .build();
            FirebaseVisionFaceDetector detector = FirebaseVision.getInstance()
                    .getVisionFaceDetector(highAccuracyOpts);
            Log.d("image", "onActivityResult: " + image.toString());
            Task<List<FirebaseVisionFace>> result =
                    detector.detectInImage(image)
                            .addOnSuccessListener(
                                    new OnSuccessListener<List<FirebaseVisionFace>>() {
                                        @Override
                                        public void onSuccess(List<FirebaseVisionFace> faces) {
                                            // Task completed successfully
                                            // ...
                                            if (!faces.isEmpty()) {
                                                for (FirebaseVisionFace face : faces) {
                                                    FirebaseVisionFaceLandmark leftEye = face.getLandmark(FirebaseVisionFaceLandmark.LEFT_EYE);
                                                    FirebaseVisionFaceLandmark rightEye = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EYE);
                                                    FirebaseVisionFaceLandmark rightEar = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR);
                                                    FirebaseVisionFaceLandmark leftEar = face.getLandmark(FirebaseVisionFaceLandmark.RIGHT_EAR);
                                                    float smile = face.getSmilingProbability();
                                                    if (smile != FirebaseVisionFace.UNCOMPUTED_PROBABILITY) {
                                                        Intent intent = new Intent(MainActivity.this, DescriptionActivity.class);
                                                        intent.putExtra("smile", smile);
//                                                intent.putExtra("lEar", leftEar.getPosition().toString());
//                                                intent.putExtra("rEar",rightEar.getPosition().toString());
//                                                intent.putExtra("lEye",leftEye.getPosition().toString());
//                                                intent.putExtra("rEye",rightEye.getPosition().toString());
                                                        startActivity(intent);


                                                    }
                                                }
                                                Log.d("LISTISHERE", "onSuccess: " + faces);
                                            }
                                            else{
                                                Toast.makeText(MainActivity.this, "Face cannot be detected",Toast.LENGTH_LONG).show();
                                            }
                                        }

                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                            Toast.makeText(MainActivity.this, "Something went wrong",Toast.LENGTH_LONG).show();
                                        }
                                    });

        }

    }
}

