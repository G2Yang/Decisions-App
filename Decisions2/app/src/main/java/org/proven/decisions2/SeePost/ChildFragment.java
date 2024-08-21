package org.proven.decisions2.SeePost;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.proven.decisions2.R;


public class ChildFragment extends Fragment {

    TextView tvTarget;
    ImageView imageView;
    String imageUrl;

    public ChildFragment() {

    }

    public static ChildFragment newInstance(String target, String imageUrl) {
        Bundle args = new Bundle();
        args.putString("target", target);
        args.putString("imageUrl", imageUrl);
        ChildFragment fragment = new ChildFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_child, container, false);
       // tvTarget = (TextView) view.findViewById(R.id.tvTarget);

        Bundle bundle = getArguments();
        //tvTarget.setText("Target: " + bundle.getString("target"));

        // Obtiene la URL de la imagen del Bundle
        imageUrl = bundle.getString("imageUrl");

        imageView = view.findViewById(R.id.fragmentImageView);

        if (imageView == null) {
            Log.e("ChildFragment", "imageView is null");
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle = getArguments();
        String imageUrl = bundle.getString("imageUrl");
        setImage(imageUrl);
    }

    public void setImage(String imageUrl) {
        if (imageView != null && imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this)
                    .load(imageUrl)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("ChildFragment", "Error al cargar la imagen", e);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(imageView);
        }
    }

}










