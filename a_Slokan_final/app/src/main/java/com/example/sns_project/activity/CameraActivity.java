package com.example.sns_project.activity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;


import com.example.sns_project.R;
import com.example.sns_project.fragment.Camera2BasicFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import static com.example.sns_project.Util.INTENT_PATH;

public class CameraActivity extends BasicActivity {
    private Camera2BasicFragment camera2BasicFragment;
    /**
     * This a callback object for the {@link ImageReader}. "onImageAvailable" will be called when a
     * still image is ready to be saved.
     */
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
            = new ImageReader.OnImageAvailableListener() {

        @Override
        public void onImageAvailable(ImageReader reader) {


            Image mImage = reader.acquireNextImage();
            File mFile = new File(getExternalFilesDir(null), "profileImage.jpg");

            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            FileOutputStream output = null;
            try {
                output = new FileOutputStream(mFile);
                output.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                mImage.close();
                if (null != output) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            Intent intent = new Intent();
            intent.putExtra(INTENT_PATH, mFile.toString());
            setResult(Activity.RESULT_OK, intent);

            camera2BasicFragment.closeCamera();
            finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        setToolbarTitle(getResources().getString(R.string.app_name));
        if (null == savedInstanceState) {
            camera2BasicFragment = new Camera2BasicFragment();
            camera2BasicFragment.setOnImageAvailableListener(mOnImageAvailableListener);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, camera2BasicFragment)
                    .commit();
        }
    }

}
