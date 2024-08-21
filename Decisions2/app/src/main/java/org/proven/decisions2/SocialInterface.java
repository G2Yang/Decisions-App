package org.proven.decisions2;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.proven.decisions2.Friends.FriendsActivity;
import org.proven.decisions2.Games.ChooseModality;
import org.proven.decisions2.SeePost.VerticalViewPager;
import org.proven.decisions2.SeePost.ViewPagerAdapter;
import org.proven.decisions2.Settings.SettingsActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SocialInterface extends FragmentActivity {
    private VerticalViewPager viewPager;
    private ViewPagerAdapter viewAdapter;
    // The buttons to navigate in the app
    Button btFriends, btDecisions, btSettings;
    // User authentication token
    String token;
    TextView decisions;
    SecureConnection secureConnection = new SecureConnection();
    private int[] imageIds; // Arreglo de IDs de imágenes


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_interface);
        // Initialize the elements
        initializeElements();
        // Request WRITE_EXTERNAL_STORAGE permission at runtime if not granted
        if (isWriteStoragePermissionGranted()) {
            // Call the method to read the user token
            readUser();
            System.out.println("ON CREATE despues de readUser: "+token);
            // Call the method to get the photos
            getPhotos();
            System.out.println("ON CREATE despues de getPhotos: "+token);
        }


        btFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialInterface.this, FriendsActivity.class);
                readUser();
                Log.d("TAG", "userIdSocial: " + token);
                startActivity(intent);
                finish();
            }
        });


        btDecisions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SocialInterface.this, ChooseModality.class));
            }
        });

        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SocialInterface.this, SettingsActivity.class);
                Log.d("TAG", "userIdSocial: " + token);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean isWriteStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.TIRAMISU) {
            // If you are on Android 13 or higher, no need to request the permission directly
            // Use MediaStore.createWriteRequest instead
            ContentValues contentValues = new ContentValues();
            contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, "filename.txt");
            contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "text/plain");

            ContentResolver resolver = getContentResolver();
            Uri contentUri = MediaStore.Files.getContentUri("external");
            Uri itemUri = resolver.insert(contentUri, contentValues);

            if (itemUri != null) {
                try {
                    OutputStream outputStream = resolver.openOutputStream(itemUri);
                    outputStream.close();
                    resolver.delete(itemUri, null, null);
                    return true;
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
            } else {
                return false;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // If you are on a version earlier than Android 13, continue using WRITE_EXTERNAL_STORAGE
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        } else {
            // Permission is automatically granted on devices below API 23
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // You are on Android 13 or higher, WRITE_EXTERNAL_STORAGE permission is not needed
                    // Call the method to read the user and get the photos
                    readUser();
                    getPhotos();
                } else {
                    // You are on a version earlier than Android 13, WRITE_EXTERNAL_STORAGE permission has been granted
                    // You can perform the write operations to the storage here
                }
            } else {
                // Permission denied, handle the scenario accordingly
                Toast.makeText(this, "Write storage permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /* Initialize the elements */
    private void initializeElements() {
        viewPager = (VerticalViewPager) findViewById(R.id.viewPager);
        viewAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewAdapter);
        btFriends = findViewById(R.id.btFriends);
        btDecisions = findViewById(R.id.btDecisions);
        btSettings = findViewById(R.id.btSettings);
        decisions = findViewById(R.id.decision);
    }


    private void getPhotos() {
        System.out.println("Funcion getPhotos antes del Thread"+token);
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient client = new OkHttpClient();
                Request.Builder requestBuilder = new Request.Builder()
                        .url("http://5.75.251.56:7070/friendPhotos")
                        .addHeader("content-type", "application/json");

                // Verificar si el token no es nulo y agregarlo al encabezado
                if (token != null) {
                    requestBuilder.addHeader("Authorization", token);
                    System.out.println("Funcion getPhotos: "+token);
                }

                Request request = requestBuilder.get().build();
                try {
                    Response response = client.newCall(request).execute();
                    if (response.isSuccessful()) {
                        String jsonResponse = response.body().string();
                        JSONArray jsonArray = new JSONArray(jsonResponse);

                        List<Integer> ids = new ArrayList<>();
                        List<String> decisions = new ArrayList<>(); // Lista para almacenar los textos de las decisiones

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            int id = jsonObject.getInt("id");
                            String decision = jsonObject.getString("decision");

                            ids.add(id);
                            decisions.add(decision);
                        }

                        // Actualizar el arreglo imageIds y notificar al adaptador
                        imageIds = new int[ids.size()];
                        for (int i = 0; i < ids.size(); i++) {
                            imageIds[i] = ids.get(i);
                        }
                        updateAdapterWithImageIds();

                        // Mostrar el texto de la decisión correspondiente a la foto inicial
                        if (!decisions.isEmpty()) {
                            String decisionText = decisions.get(0);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    SocialInterface.this.decisions.setText(decisionText);
                                }
                            });
                        }

                        // Establecer el listener para detectar cambios de página en el ViewPager
                        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                            @Override
                            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                // No se requiere implementación
                            }

                            @Override
                            public void onPageSelected(int position) {
                                // Mostrar el texto de la decisión correspondiente a la foto seleccionada
                                if (!decisions.isEmpty() && position < decisions.size()) {
                                    String decisionText = decisions.get(position);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            SocialInterface.this.decisions.setText(decisionText);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onPageScrollStateChanged(int state) {
                                // No se requiere implementación
                            }
                        });
                    } else {
                        // Manejar la respuesta no exitosa
                        System.out.println("Error en la respuesta: " + response.code() + " " + response.message());
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    private void updateAdapterWithImageIds() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                viewAdapter.setImageIds(imageIds);
                viewAdapter.notifyDataSetChanged();
            }
        });
    }


    /*Method to read the login token for use in the activity*/
    private void readUser() {
        File file = new File(getFilesDir(), "token.txt");
        try {
            if (!file.exists()) {
                return;
            }
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String tokenValue = bufferedReader.readLine();
            bufferedReader.close();
            reader.close();

            if (tokenValue != null && !tokenValue.isEmpty()) {
                token = tokenValue;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}