package org.proven.decisions2.PublicDecision;


import android.app.Activity;
import android.app.ActivityManager;

import android.content.Context;

import android.content.Intent;

import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.Manifest;


import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;


import java.io.ByteArrayOutputStream;

import java.io.IOException;
import java.util.List;
import org.proven.decisions2.R;


public class CameraActivity extends Activity {

    // Boolean variable to check if the camera is initialized
    private boolean isCameraInitialized;
    // Camera object
    private Camera mCamera = null;
    // Camera ID
    private static int mCameraId;
    // SurfaceHolder object
    private static SurfaceHolder myHolder;
    // CameraPreview object
    private static CameraPreview mPreview;
    // FrameLayout object for preview
    private FrameLayout preview;
    // Button object for flash
    private Button flashB;
    // Boolean variable for flash mode
    private static boolean fM;
    // Camera parameters object
    private static Camera.Parameters p;
    // Flash mode string
    String flashMode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_layout);
    }

    @Override
    public void onConfigurationChanged(Configuration c){
        super.onConfigurationChanged(c);
        // Rotate the camera
        rotateCamera();
    }

    // String array of permissions
    private static final String[] PERMISSIONS={
            Manifest.permission.CAMERA,
    };

    // This line declares a constant variable named REQUEST_PERMISSIONS and assigns it the value 34.
    private static final int REQUEST_PERMISSIONS = 34;

    // This line declares a constant variable named PERMISSIONS_COUNT and assigns it the value 1.
    private static final int PERMISSIONS_COUNT = 1;

    // This method checks if any permissions are denied.
    private boolean arePermissionsDenied(){
        // This loop iterates from 0 to PERMISSIONS_COUNT.
        for (int i = 0; i< PERMISSIONS_COUNT ; i++){
            // This condition checks if the permission at index i is not granted.
            if (checkSelfPermission(PERMISSIONS[i])!= PackageManager.PERMISSION_GRANTED){
                // If a permission is denied, it returns true.
                return true;
            }
        }
        // If all permissions are granted, it returns false.
        return false;
    }

    // This method is called when the result of a permission request is received.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // This condition checks if any permissions are still denied.
        if (requestCode==REQUEST_PERMISSIONS && grantResults.length>0){
            if (arePermissionsDenied()){
                ((ActivityManager) (this.getSystemService(ACTIVITY_SERVICE))).clearApplicationUserData();
                recreate();
            }else{
                // If all permissions are granted, it calls the onResume() method.
                onResume();
            }
        }
    }
    private void initCam() {
        // Determine the camera facing based on the value of whichCamera
        int cameraFacing = whichCamera ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
        // Open the camera with the specified facing
        mCamera = Camera.open(cameraFacing);
        // Get the camera parameters
        p = mCamera.getParameters();
        // Create a new instance of CameraPreview with the current activity and camera
        mPreview = new CameraPreview(this, mCamera);
        // Find the camera preview view in the layout
        preview = findViewById(R.id.camera_preview);
        // Add the camera preview to the preview view
        preview.addView(mPreview);
        // Rotate the camera if necessary
        rotateCamera();

        // Find the flash button in the layout
        flashB = findViewById(R.id.flash);
        // Initialize the flash
        flash();
    }

    private void init() {
        // Initialize the camera
        initCam();

        // Find the switch camera button in the layout
        final Button switchCameraButton = findViewById(R.id.rotate_camera);
        // Set an onClickListener for the switch camera button
        switchCameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Release the camera
                mCamera.release();
                // Switch the camera facing
                switchCamera();
                // Rotate the camera if necessary
                rotateCamera();
                try {
                    // Set the preview display to the holder
                    mCamera.setPreviewDisplay(myHolder);
                } catch (Exception e) {

                }
                // Start the camera preview
                mCamera.startPreview();
                // Check the camera facing and update the flash button accordingly
                if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_no_flash));
                    flashB.setEnabled(false);
                } else {
                    flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_flash));
                    flashB.setEnabled(true);
                    // Initialize the flash
                    flash();
                }

                // Set updated parameters to the camera
                mCamera.setParameters(p);
            }
        });

        // Find the take photo button in the layout
        final Button takePhotoButton = findViewById(R.id.takePhoto);
        // Set an onClickListener for the take photo button
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // Take a picture with the camera
                    mCamera.takePicture(null, null, mPicture);
                } catch (Exception e) {
                    System.out.println("Error taken picture");
                }
            }
        });

        // Set an onLongClickListener for the preview view
        preview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Check if the camera is the back camera
                if (whichCamera) {
                    // Toggle between continuous focus mode and auto focus mode
                    if (fM) {
                        p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
                    } else {
                        p.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    }
                    try {
                        // Set the updated parameters to the camera
                        mCamera.setParameters(p);
                    } catch (Exception e) {

                    }
                    // Toggle the value of fM
                    fM = !fM;
                }
                return true;
            }
        });

        // Initialize the flash
        flash();

        // Set updated parameters to the camera
        mCamera.setParameters(p);
    }

    private void flash(){
        // Check if the device has a flash
        if (hasFlash()) {
            // Turn off the flash
            p.setFlashMode(p.FLASH_MODE_OFF);
            flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_no_flash));
            try {
                mCamera.setParameters(p);
            } catch (Exception e) {
                Log.e("TAG", "Error setting camera parameters: " + e.getMessage());
            }

            // Set a click listener for the flash button
            flashB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the current camera parameters
                    p = mCamera.getParameters();
                    flashMode = p.getFlashMode();

                    // If the flash is off, turn it on
                    if (p.FLASH_MODE_OFF.equals(flashMode)) {
                        p.setFlashMode(p.FLASH_MODE_ON);
                        try {
                            mCamera.setParameters(p);
                        } catch (Exception e) {
                            Log.e("TAG", "Error setting camera parameters: " + e.getMessage());
                        }
                        flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_flash));
                        System.out.println("FLASH ON");
                    }
                    // If the flash is on, turn it off
                    else if (p.FLASH_MODE_ON.equals(flashMode)) {
                        p.setFlashMode(p.FLASH_MODE_OFF);
                        try {
                            mCamera.setParameters(p);
                        } catch (Exception e) {
                            Log.e("TAG", "Error setting camera parameters: " + e.getMessage());
                        }
                        flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_no_flash));
                        System.out.println("FLASH OFF");
                    }
                    // If there are problems with the flash, print a message
                    else {
                        System.out.println("Problems with flash: init");
                    }
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if the device is running on Android Nougat (API level 24) or higher and if the necessary permissions are denied
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && arePermissionsDenied()) {
            // Request the necessary permissions
            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            return;
        }

        // Check if the camera is not initialized
        if (!isCameraInitialized) {
            // Initialize the camera
            init();
            isCameraInitialized = true;
        } else {
            // Initialize the camera again
            initCam();
        }
    }


    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // Decodes the byte array of the image into a Bitmap object
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            // Rotate the image if necessary
            bitmap = rotateBitmap(bitmap, getCameraDisplayOrientation(CameraActivity.this, camera));

            // Convert the bitmap to a byte array
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, stream);
            byte[] byteArray = stream.toByteArray();

            // Create an Intent to pass the image bytes to the next activity
            Intent intent = getIntent();
            String textoDecision1 = intent.getStringExtra("decision1");
            String textoDecision2 = intent.getStringExtra("decision2");
            System.out.println("CameraActivity "+textoDecision1);
            System.out.println("CameraActivity "+textoDecision2);
            Intent intent4 = new Intent(CameraActivity.this, ResultPhoto.class);
            intent4.putExtra("photo", byteArray);
            intent4.putExtra("decision1", textoDecision1);
            intent4.putExtra("decision2", textoDecision2);
            startActivity(intent4);

        }
    };


    private static Bitmap rotateBitmap(Bitmap bitmap, int rotation) {
        // Create a new matrix to perform the rotation
        Matrix matrix = new Matrix();
        matrix.postRotate(rotation);
        // Check if the camera is facing front and adjust the scale accordingly
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            matrix.postScale(-1, 1, bitmap.getWidth() / 2f, bitmap.getHeight() / 2f);
        }
        // Create a new rotated bitmap using the matrix transformation
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private static int getCameraDisplayOrientation(Activity activity, Camera camera) {
        // Get the rotation of the display
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        // Determine the corresponding degrees based on the rotation
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        // Get the camera information
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mCameraId, info); // pass the current camera ID here
        int result;
        // Adjust the orientation based on the camera facing direction
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation - degrees + 360) % 360;
            System.out.println("A");
        } else { // back-facing
            result = (info.orientation + degrees) % 360;
            System.out.println("B");
        }
        // Return the final orientation value
        return result;
    }

    private void switchCamera() {
        // Release the current camera
        mCamera.release();
        // Switch to the front camera if currently using the back camera, or vice versa
        if (whichCamera) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        } else {
            mCamera = Camera.open(); // Open the back camera
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        // Check if the camera has flash and update the parameters accordingly
        if (hasFlash()) {
            p = mCamera.getParameters();
            p.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            flashB.setBackgroundDrawable(ContextCompat.getDrawable(CameraActivity.this, R.drawable.ic_no_flash));
            mCamera.setParameters(p);
        }
        // Toggle the camera flag
        whichCamera = !whichCamera;
        // Get the updated parameters for the new camera
        p = mCamera.getParameters();
        // Set the updated parameters for the new camera
        mCamera.setParameters(p);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Release the camera when the activity is paused
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            // Remove the preview from the layout
            preview.removeView(mPreview);
            // Release the camera resources
            mCamera.release();
            mCamera = null;
        }
    }


    // Check if the device has a flash
    private boolean hasFlash() {
        if (mCamera == null) {
            return false;
        }

        Camera.Parameters parameters = mCamera.getParameters();
        List<String> flashModes = parameters.getSupportedFlashModes();
        if (flashModes == null) {
            flashB.setVisibility(View.INVISIBLE);
            return false;
        }

        for (String flashMode : flashModes) {
            if (Camera.Parameters.FLASH_MODE_ON.equals(flashMode)) {
                flashB.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return false;
    }

    private static int rotation;

    private static boolean whichCamera = true;

    // Rotate the camera
    private void rotateCamera() {
        if (mCamera != null) {
            rotation = this.getWindowManager().getDefaultDisplay().getRotation();
            if (rotation == 0) {
                rotation = 90;
            } else if (rotation == 1) {
                rotation = 0;
            } else if (rotation == 2) {
                rotation = 270;
            } else {
                rotation = 180;
            }
            mCamera.setDisplayOrientation(rotation);
            if (!whichCamera) {
                if (rotation == 90) {
                    rotation = 270;
                } else if (rotation == 270) {
                    rotation = 90;
                }
            }
            p = mCamera.getParameters();
            p.setRotation(rotation);
            mCamera.setParameters(p);
        }
    }

    // CameraPreview class
    private static class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {
        private static SurfaceHolder mHolder;
        private static Camera mCamera;

        private CameraPreview(Context context, Camera camera) {
            super(context);
            mCamera = camera;
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }

        // Surface created event
        public void surfaceCreated(SurfaceHolder holder) {
            myHolder = holder;
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.startPreview();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Surface destroyed event
        public void surfaceDestroyed(SurfaceHolder holder) {

        }

        // Surface changed event
        public void surfaceChanged(SurfaceHolder holder, int f, int w, int h) {

        }
    }
}



