package com.goreena.govindtask;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private ArrayList<ScreenPoints> undonePaths = new ArrayList<ScreenPoints>();
    ArrayList<ScreenPoints> screenPointsArrayList = new ArrayList<>();
    ImageView drawingImageView;
    Button undobtn;
    Paint paint;
    Canvas canvas;
    Bitmap bitmap;
    Random rnd = new Random();


    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        String msg = token;
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    /*  adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                // Check the LogCat to get your test device ID
                .addTestDevice("CB1AEAAA4EB7FAFF7A5CB5CB99A7AA94")
                .build();*/

        drawingImageView = (ImageView) this.findViewById(R.id.ivDrawing);
        undobtn = (Button) this.findViewById(R.id.undobtn);
        undobtn.setOnClickListener(this);


        bitmap = Bitmap.createBitmap((int) getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);


        touchScreenClick();


    }
    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    private void touchScreenClick() {


        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                paint = new Paint();


                float x = event.getX();
                float y = event.getY();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                paint.setColor(color);
                paint.setStrokeWidth(15f);
                screenPointsArrayList.add(new ScreenPoints(x, y, paint));

                if (screenPointsArrayList.size() > 0) {
                    undonePaths.add(new ScreenPoints(x, y, paint));
                }

                drawLinesOnScreen(screenPointsArrayList);
                return false;
            }
        });


    }
    private void touchScreenClick2() {

        bitmap = Bitmap.createBitmap((int) getWindowManager()
                .getDefaultDisplay().getWidth(), (int) getWindowManager()
                .getDefaultDisplay().getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {

                paint = new Paint();


                float x = event.getX();
                float y = event.getY();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                paint.setColor(color);
                paint.setStrokeWidth(15f);
                screenPointsArrayList.add(new ScreenPoints(x, y, paint));

                if (screenPointsArrayList.size() > 0) {
                    undonePaths.add(new ScreenPoints(x, y, paint));
                }

                drawLinesOnScreen(screenPointsArrayList);
                return false;
            }
        });


    }

    private void drawLinesOnScreen(ArrayList<ScreenPoints> pointsArrayList) {

        if (Math.round(pointsArrayList.get(0).getX())==Math.round(pointsArrayList.get((pointsArrayList.size()-1)).getX()) &&
                Math.round(pointsArrayList.get(0).getY())==Math.round(pointsArrayList.get((pointsArrayList.size()-1)).getY())){

            Toast.makeText(this, "Nothing to Draw...", Toast.LENGTH_SHORT).show();

        }else {
            for (int i = 0; i < pointsArrayList.size(); i++) {
                float startx = pointsArrayList.get(i).getX();
                float starty = pointsArrayList.get(i).getY();

                if (i < pointsArrayList.size() - 1) {
                    float endx = pointsArrayList.get(i + 1).getX();
                    float endy = pointsArrayList.get(i + 1).getY();
                    canvas.drawLine(startx, starty, endx, endy, pointsArrayList.get(i).getColorProperty());
                }

                Paint property = new Paint();
                property.setStrokeWidth(15f);
                property.setColor(Color.BLACK);
                canvas.drawPoint(startx, starty, property);

            }

            drawingImageView.setImageBitmap(bitmap);

        }




    }

    @Override
    public void onClick(View v) {

        touchScreenClick2();

    }



    class ScreenPoints {
        float x = 0.0f, y = 0.0f;

        Paint colorProperty;

        public ScreenPoints(float x, float y, Paint colorProperty) {
            this.x = x;
            this.y = y;
            this.colorProperty = colorProperty;
        }

        public float getX() {
            return x;
        }

        public float getY() {
            return y;
        }

        public Paint getColorProperty() {
            return colorProperty;
        }
    }
}

