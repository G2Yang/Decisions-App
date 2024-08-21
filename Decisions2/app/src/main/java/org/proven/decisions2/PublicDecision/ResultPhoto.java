package org.proven.decisions2.PublicDecision;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import org.proven.decisions2.R;
import org.proven.decisions2.SocialInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ResultPhoto extends Activity {


    private Button btNo, btYes;
    private Bitmap bitmap;
    String textDecision1;
    String token;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_photo_layout);
        /*Initialize the elements*/
        instantiateElements();
        //call the method for the result
        showResultPhoto();
        //intent for the text decision
        Intent intent = getIntent();
        textDecision1 = intent.getStringExtra("decision1");
        System.out.println(textDecision1);
        //call the method for read token user
        readUser();
        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bitmap != null) {
                    bitmap.recycle();
                }
                // Start camera activity or do other necessary actions here
                finish();
            }
        });

        btYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
                Intent intent = getIntent();
                textDecision1 = intent.getStringExtra("decision1");
                Intent intent2 = new Intent(ResultPhoto.this, SocialInterface.class);
                intent2.putExtra("decision1", textDecision1);
                System.out.println("ResultPhoto: " + textDecision1);
                //intent2.putExtra("decision2", textoDecision2);
                startActivity(intent2);
            }
        });
    }

    private void showResultPhoto() {
        byte[] byteArray = getIntent().getByteArrayExtra("photo");
        bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        ImageView imageView = findViewById(R.id.bitmapResult);
        imageView.setImageBitmap(bitmap);
    }
    /* Initialize the elements */
    private void instantiateElements() {
        btNo = findViewById(R.id.btNo);
        btYes = findViewById(R.id.btYes);
    }
    /* Method for the upload image in the server*/
    private void uploadImage() {
        // Check if you have a valid bitmap to load
        if (bitmap == null) {
            return;
        }

        // Compress the bitmap into a JPEG file in the external cache
        File cacheDir = getExternalCacheDir();
        File imageFile = new File(cacheDir, "result_photo.jpg");
        try {
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create an instance of OkHttpClient
        OkHttpClient client = new OkHttpClient();

        // Create an instance of MultipartBody.Builder to build the HTTP request body
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("textFile", "result_photo.jpg",
                        RequestBody.create(MediaType.parse("image/jpg"), imageFile))
                .addFormDataPart("textDecision1", textDecision1); // Add the text value Decision1

        // Create the HTTP request with the URL of the server
        Request request = new Request.Builder()
                .url("http://5.75.251.56:7070/upload")
                .header("Authorization", token)
                .post(builder.build())
                .build();

        // Execute the HTTP request in a background thread
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle the error in case the HTTP request fails
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the response from the server
                if (response.isSuccessful()) {
                    String responseBody = response.body().string();
                    Log.i("TAG", "Response: " + responseBody);
                } else {
                    Log.e("TAG", "Error: " + response.code() + " " + response.message());
                }
            }
        });
    }


    /*Method to read the login token for use in the activity*/
    private void readUser() {
        File filename = new File(getFilesDir(), "token.txt");
        try {
            FileReader reader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(reader);
            token = bufferedReader.readLine();
            bufferedReader.close();
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}

