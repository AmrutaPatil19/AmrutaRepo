package com.example.textrecognition;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

public class ScannerActivity extends AppCompatActivity {
    private TextView detectedText;
    private Button btnSnap,btnDetect;
    private ImageView ivCaptureImage;
    static final int REQUEST_IMAGE_CAPTURE =12;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        detectedText = findViewById(R.id.detectedText);
        btnSnap = findViewById(R.id.btnSnap);
        btnDetect = findViewById(R.id.btnDetect);
        ivCaptureImage = findViewById(R.id.IVImageCapture);

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectText();
            }
        });

        btnSnap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 if (checkPermission()){
                     captureImage();
                 }
                 else {
                     requestPermission();
                 }
            }
        });

    }

    private void detectText(){
        InputImage image = InputImage.fromBitmap(imageBitmap,0);
        TextRecognizer recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);
        Task<Text> result = recognizer.process(image).addOnSuccessListener(new OnSuccessListener<Text>() {
            @Override
            public void onSuccess(Text text) {
              StringBuilder result = new StringBuilder();
              for(Text.TextBlock block:text.getTextBlocks()){
                  String blockText = block.getText();
                  Point[] blockCornerPoint = block.getCornerPoints();
                  Rect blockFrame = block.getBoundingBox();
                  for (Text.Line line :block.getLines()){

                      //Commited for feature1
                      //Committed for feature2
                      //Commiteee
                      //test rebase
                      String lineText = line.getText();
                      Point[] lineCornerPoint = line.getCornerPoints();
                      Rect lineRect = line.getBoundingBox();
                      for (Text.Element element :line.getElements()){
                          String elementText = element.getText();
                          result.append(elementText);
                      }
                      detectedText.setText(blockText);
                  }
              }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private boolean checkPermission(){
        int cameraPermission = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);
         return cameraPermission == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        int PERMISSION_CODE = 200;
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length>0){
            boolean cameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
            if (cameraPermission){
                Toast.makeText(this,"Permission Granted...",Toast.LENGTH_SHORT).show();
                captureImage();
            }
            else {
                Toast.makeText(this,"Permission Denied...",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_IMAGE_CAPTURE && resultCode==RESULT_OK){
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            ivCaptureImage.setImageBitmap(imageBitmap);

        }
    }
    private void captureImage(){
      Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);

     /* if (takePicture.resolveActivity(getPackageManager())!=null){
          startActivityForResult(takePicture,REQUEST_IMAGE_CAPTURE);

      }*/
    }

}
