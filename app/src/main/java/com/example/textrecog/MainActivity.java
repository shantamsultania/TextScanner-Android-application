package com.example.textrecog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    SurfaceView camerview;
    TextView text;
    CameraSource cameraSource;
    final int Requestcamerapermission = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camerview = findViewById(R.id.surface_view);
        text = findViewById(R.id.text_view);

        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (!textRecognizer.isOperational())
        {
            Toast.makeText(this,"error in dependencies",Toast.LENGTH_SHORT).show();
        }
        else
        {
            cameraSource = new CameraSource.Builder(getApplicationContext(),textRecognizer).setFacing(CameraSource.CAMERA_FACING_BACK).setRequestedPreviewSize(1200,1024).setRequestedFps(2.0f).build();
        camerview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    cameraSource.start(camerview.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
cameraSource.stop();
            }
        });

        textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
            @Override
            public void release() {

            }

            @Override
            public void receiveDetections(Detector.Detections<TextBlock> detections) {
                final SparseArray<TextBlock> Iteams = detections.getDetectedItems();

                if (Iteams.size() != 0)
                {
                    text.post(new Runnable() {
                        @Override
                        public void run() {
                            StringBuilder stringBuilder = new StringBuilder();
                            for (int i = 0;i<Iteams.size();i++)
                            {
                                TextBlock iteam = Iteams.valueAt(i);
                                stringBuilder.append(iteam.getValue());
                                stringBuilder.append("\n");
                            }
                            text.setText(stringBuilder.toString());
                        }
                    });
                }
            }
        });

        }

    }
}
