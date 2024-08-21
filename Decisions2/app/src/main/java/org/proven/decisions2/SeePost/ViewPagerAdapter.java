package org.proven.decisions2.SeePost;


import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;


import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


//Esto funciona
//public class ViewPagerAdapter extends FragmentStatePagerAdapter {
//    Handler handler;
//
//    private int[] imageIds = {6, 7, 8, 9,10,11,12};
//
//    public ViewPagerAdapter(FragmentManager fm) {
//        super(fm);
//        handler = new Handler(Looper.getMainLooper());
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        ChildFragment child = new ChildFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("target", String.valueOf(position));
//        child.setArguments(bundle);
//
//        if (imageIds == null) {
//            // Descargar las imágenes antes de cargar la imagen en Glide
//            getPhotos();
//        } else {
//            // Obtener la URL de la imagen correspondiente a esta posición
//            String imageUrl = "http://5.75.251.56:7070/imagen/" + imageIds[position];
//            System.out.println(imageUrl);
//
//            // Cargar la imagen utilizando Glide
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//                    Glide.with(child)
//                            .load(imageUrl)
//                            .into(child.imageView);
//                }
//            });
//        }
//
//        return child;
//    }
//
//    @Override
//    public int getCount() {
//        return imageIds == null ? 0 : imageIds.length;
//    }
//
//    private void getPhotos() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                OkHttpClient client = new OkHttpClient();
//                Request request = new Request.Builder()
//                        .url("http://5.75.251.56:7070/imagen/")
//                        .get()
//                        .build();
//
//                try {
//                    Response response = client.newCall(request).execute();
//                    if (response.isSuccessful()) {
//                        String jsonResponse = response.body().string();
//                        JSONArray jsonArray = new JSONArray(jsonResponse);
//
//                        List<Integer> ids = new ArrayList<>();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            int imageId = jsonArray.getInt(i);
//                            ids.add(imageId);
//                        }
//
//                        // Ejecutar la actualización del ViewPagerAdapter en el hilo principal utilizando Handler
//                        final List<Integer> finalIds = ids;
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                                    imageIds = finalIds.stream().mapToInt(Integer::intValue).toArray();
//                                }
//                                notifyDataSetChanged();
//                            }
//                        });
//                    } else {
//                        // Manejar la respuesta no exitosa
//                        System.out.println("Error en la respuesta: " + response.code() + " " + response.message());
//                    }
//                } catch (IOException | JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
//}

public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    private Handler handler;
    private int[] imageIds;

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
        handler = new Handler(Looper.getMainLooper());
        imageIds = new int[0]; // Inicializar con un arreglo vacío
    }

    @Override
    public Fragment getItem(int position) {
        ChildFragment child = new ChildFragment();
        Bundle bundle = new Bundle();
        bundle.putString("target", String.valueOf(position));
        child.setArguments(bundle);

        if (imageIds.length == 0) {

            // No hay IDs de imagen disponibles, puedes mostrar una imagen de carga o un mensaje de espera
        } else {
            int imageId = imageIds[position];
            // Cargar la imagen utilizando Glide
            handler.post(() -> {
                Glide.with(child)
                        .load("http://5.75.251.56:7070/imagen/" + imageId)
                        .into(child.imageView);
            });
        }

        return child;
    }

    @Override
    public int getCount() {
        return imageIds.length;
    }

    public void setImageIds(int[] ids) {
        imageIds = ids;
    }

}








