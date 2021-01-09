package com.example.firebaseimagelabeling;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.cloud.FirebaseVisionCloudDetectorOptions;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabel;
import com.google.firebase.ml.vision.cloud.label.FirebaseVisionCloudLabelDetector;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.label.FirebaseVisionLabel;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetector;
import com.google.firebase.ml.vision.label.FirebaseVisionLabelDetectorOptions;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.sql.Time;
import java.util.List;

import dmax.dialog.SpotsDialog;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {

    CameraView cameraView;
    Button btnDetect;
    AlertDialog waitingDialog;
    TextView label_textView, korean_textView, find_textView;
    ImageView correct_imageView, correct_background_imageView, wrong_imageView, wrong_background_imageView;
    ArrayList<String> label_array = new ArrayList<String>();
    ArrayList<String> label_array_kor = new ArrayList<String>();
    Random random;


    int BUTTON_STATUS = 0;
    int current_label_id;
    int WRONG_COUNT = 0;

    @Override
    protected void onResume(){
        super.onResume();
        cameraView.start();
    }

    // On start _____
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        label_array.add("Comics");
        label_array.add("Toy");
        label_array.add("Beard");
        label_array.add("Tights");
        label_array.add("Bird");
        label_array.add("Porcelain");
        label_array.add("Petal");
        label_array.add("Cushion");
        label_array.add("Sunglasses");
        label_array.add("Badminton");
        label_array.add("Bicycle");
        label_array.add("Boat");
        label_array.add("Smile");
        label_array.add("Toe");
        label_array.add("Brick");
        label_array.add("Person");
        label_array.add("Bathroom");
        label_array.add("Laugh");
        label_array.add("Balloon");
        label_array.add("Necklace");
        label_array.add("Computer");
        label_array.add("Chair");
        label_array.add("Clock");
        label_array.add("Dude");
        label_array.add("Desk");
        label_array.add("Cat");
        label_array.add("Juice");
        label_array.add("Stuffed toy");
        label_array.add("Tile");
        label_array.add("Cola");
        label_array.add("Hat");
        label_array.add("Coffee");
        label_array.add("Soccer");
        label_array.add("Food");
        label_array.add("Fruit");

        label_array_kor.add("만화책");  // "Comics"
        label_array_kor.add("장난감");  // "Toy"
        label_array_kor.add("수염");  // "Beard"
        label_array_kor.add("스타킹");  // "Tights"
        label_array_kor.add("새");  // "Bird"
        label_array_kor.add("도자기");  // "Porcelain"
        label_array_kor.add("꽃잎");  // "Petal"
        label_array_kor.add("쿠션");  // "Cushion"
        label_array_kor.add("선글라스");  // "Sunglasses"
        label_array_kor.add("배드민턴");  // "Badminton"
        label_array_kor.add("자전거 ");  // "Bicycle"
        label_array_kor.add("배");  // "Boat"
        label_array_kor.add("미소");  // "Smile"
        label_array_kor.add("발가락");  // "Toe"
        label_array_kor.add("벽돌");  // "Brick"
        label_array_kor.add("사람");  // "Person"
        label_array_kor.add("화장실");  // "Bathroom"
        label_array_kor.add("웃음");  // "Laugh"
        label_array_kor.add("풍선");  // "Balloon"
        label_array_kor.add("목걸이");  // "Necklace"
        label_array_kor.add("컴퓨터");  // "Computer"
        label_array_kor.add("의자");  // "Chair"
        label_array_kor.add("시계");  // "Clock"
        label_array_kor.add("남자");  // "Dude"
        label_array_kor.add("책상");  // "Desk"
        label_array_kor.add("고양이");  // "Cat"
        label_array_kor.add("쥬스");  // "Juice"
        label_array_kor.add("인형");  // "Stuffed toy"
        label_array_kor.add("타일");  // "Tile"
        label_array_kor.add("콜라");  // "Cola"
        label_array_kor.add("모자");  // "Hat"
        label_array_kor.add("커피");  // "Coffee"
        label_array_kor.add("축구");  // "Soccer"
        label_array_kor.add("음식");  // "Food"
        label_array_kor.add("과일");  // "Fruit"

        random = new Random();

        cameraView = (CameraView) findViewById(R.id.camera_view);
        btnDetect = (Button) findViewById(R.id.btn_detect);
        label_textView = (TextView) findViewById(R.id.result_text);
        korean_textView = (TextView) findViewById(R.id.korean_text);
        find_textView = (TextView) findViewById(R.id.find_text);
        correct_imageView  = (ImageView) findViewById(R.id.correct_imageView );
        correct_background_imageView= (ImageView) findViewById(R.id.correct_background_imageView);
        wrong_imageView = (ImageView) findViewById(R.id.wrong_imageView );
        wrong_background_imageView= (ImageView) findViewById(R.id.wrong_background_imageView);


        current_label_id = random.nextInt(label_array.size());   //change label id
        label_textView.setText(label_array.get(current_label_id));


        waitingDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Gae San joong")
                .setCancelable(false).build();

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {
                waitingDialog.show();
                Bitmap bitmap = cameraKitImage.getBitmap();
                bitmap = Bitmap.createScaledBitmap(bitmap,cameraView.getWidth(),cameraView.getHeight(),false);
                //cameraView.stop(); -- black out error

                // Pass on Image for Firebase MLKit
                runDetector(bitmap);
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        btnDetect.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                wrong_imageView.setVisibility(View.INVISIBLE);
                wrong_background_imageView.setVisibility(View.INVISIBLE);
                correct_imageView.setVisibility(View.INVISIBLE);
                correct_background_imageView.setVisibility(View.INVISIBLE);
                // CAPTURE
                if(BUTTON_STATUS == 0) {
                    cameraView.start();
                    cameraView.captureImage();
                }
                // NEXT WORD.
                // Select vocab - random from array
                // TODO: use stack or queue to minimize repetition
                if(BUTTON_STATUS == 1) {
                    showNewWord();
                }
            }

        });

    }

    private void runDetector(Bitmap bitmap) {
        Log.d("EDMTERROR","here2");

        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);

        FirebaseVisionLabelDetectorOptions options =
                new FirebaseVisionLabelDetectorOptions.Builder()
                        .setConfidenceThreshold(0.6f) // confidence 0.8 original
                        .build();

        FirebaseVisionLabelDetector detector =
                FirebaseVision.getInstance().getVisionLabelDetector(options);

        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionLabel>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionLabel> firebaseVisionLabels) {
                        processDataResult(firebaseVisionLabels);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("EDMTERROR",e.getMessage());
                    }
                });
    }

    private void processDataResultCloud(List<FirebaseVisionCloudLabel> firebaseVisionCloudLabels) {
        for(FirebaseVisionCloudLabel label : firebaseVisionCloudLabels){
            Toast.makeText(this, "Cloud result :" + label.getLabel(), Toast.LENGTH_SHORT).show();
        }
        if(waitingDialog.isShowing())
            waitingDialog.dismiss();
    }

    private void processDataResult(List<FirebaseVisionLabel> firebaseVisionCloudLabels) {
/*        for(FirebaseVisionLabel label : firebaseVisionCloudLabels) {
            Toast.makeText(this, "On-device result :" + label.getLabel(), Toast.LENGTH_SHORT).show();
            Log.d("Labeling tags", label.getLabel() + " " + label.getConfidence());
        }
*/
        float confidence = 0.0f;
        boolean wrong_flag = true;

        for(FirebaseVisionLabel label : firebaseVisionCloudLabels) {

            if(label.getLabel().equals(label_array.get(current_label_id))){
                wrong_flag = false;
                confidence = label.getConfidence();
                break;
            }
            // TODO: Erase
            else if(label.getLabel().equals("Bird")){
                // correct_imageView.setVisibility(View.VISIBLE);
                // correct_background_imageView.setVisibility(View.VISIBLE);
                wrong_flag = false;
                confidence = label.getConfidence();
                break;
            }
            Log.d("Labeling tags", label.getLabel() + " " + label.getConfidence());
        }

        String wrong_text_str;

        if(wrong_flag){
            WRONG_COUNT += 1;
            if(!firebaseVisionCloudLabels.isEmpty()) {
                FirebaseVisionLabel label = firebaseVisionCloudLabels.get(0);
                wrong_text_str = "Are you sure it is not " + label.getLabel();

            }else{
                wrong_text_str = "AI cannot Detect!";
            }

            if(WRONG_COUNT == 4){
                    find_textView.setText(wrong_text_str);
                    label_textView.setText("We will give tou a new word");
                    wrong_imageView.setVisibility(View.VISIBLE);
                    WRONG_COUNT = 0;
                    BUTTON_STATUS = 1;
                    btnDetect.setText("Next Word");

            }else{
                find_textView.setText(wrong_text_str);

                // please find "   "
                label_textView.setText("Try Again: " + label_array.get(current_label_id));
                wrong_imageView.setVisibility(View.VISIBLE);
            }


            // wrong_background_imageView.setVisibility(View.VISIBLE);
        }else{
            // confidence
            find_textView.setText("Correct w/ confidence level: " + confidence);

            // correct LABEL
            label_textView.setText(label_array.get(current_label_id));
            correct_imageView.setVisibility(View.VISIBLE);

            // show korean
            korean_textView.setText(label_array_kor.get(current_label_id));
            korean_textView.setVisibility(View.VISIBLE);

            // button - NEW WORD
            BUTTON_STATUS = 1;
            btnDetect.setText("Next Word");
        }

        if(waitingDialog.isShowing())
            waitingDialog.dismiss();
    }

    public void showNewWord(){
        find_textView.setText("Find");
        label_array_kor.remove(current_label_id); // remove current label from vocab
        label_array.remove(current_label_id); // remove current label from vocab
        current_label_id = random.nextInt(label_array.size());     // change label id randomly, globally
        label_textView.setText(label_array.get(current_label_id));     // change target text for user
        BUTTON_STATUS = 0;
        korean_textView.setVisibility(View.INVISIBLE);
        btnDetect.setText("Detect");
    }

}