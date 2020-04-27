package com.in.mars.ui;

import android.Manifest;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.Rect;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;

import android.provider.MediaStore;

import android.view.View;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.in.mars.R;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageSelectorActivity extends AppCompatActivity {

    public static final String KEY_OUTPUT_URI = "output_uri";
    private static final int MY_PERMISSIONS_REQUEST_OPEN_CAMERA = 1001;
    private static final int REQUEST_CAMERA = 1002;
    private String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cameraIntent();

    }

    private void cameraIntent() {

        int permissionCheck = ContextCompat.checkSelfPermission(ImageSelectorActivity.this,
                Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ImageSelectorActivity.this, Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(
                        ImageSelectorActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_OPEN_CAMERA);

            } else {
                ActivityCompat.requestPermissions(
                        ImageSelectorActivity.this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_OPEN_CAMERA);
            }

        } else {

            openCamera();

        }

    }

    private void openCamera() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {

            File pictureFile = null;
            try {
                pictureFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this,
                        "Photo file can't be created, please try again",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            if (pictureFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.in.mars.fileprovider",
                        pictureFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {


            case MY_PERMISSIONS_REQUEST_OPEN_CAMERA:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera();

                } else {

                    Toast.makeText(getApplicationContext(), "dennied", Toast.LENGTH_SHORT).show();
                    finish();

                }
                return;

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                try {
                    openCropdialog(mCurrentPhotoPath, null);

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

        }

    }

    private void openCropdialog(String path, String from) throws IOException {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            openCropdialog(bitmap, from);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void openCropdialog(final Bitmap bitmap, final String from) {

        final Dialog cropdialog = new Dialog(ImageSelectorActivity.this, R.style.MyTheme);
        cropdialog.setContentView(R.layout.dialog_crop);

        final TextView tvCrop = cropdialog.findViewById(R.id.tvCrop);
        final CropImageView mCropImageView = cropdialog.findViewById(R.id.CropWindow);
        mCropImageView.setAutoZoomEnabled(true);
        cropdialog.show();
        cropdialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                finish();
            }
        });
        mCropImageView.setImageBitmap(bitmap);
        mCropImageView.setCropRect(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
        mCropImageView.setAspectRatio(50, 50);
        mCropImageView.setFixedAspectRatio(false);

        tvCrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropdialog.dismiss();
                try {
                    File photoFile = createImageFile();
                    if (photoFile != null && photoFile.exists()) {
                        saveBitmap(ImageSelectorActivity.this, photoFile, mCropImageView.getCroppedImage());
                        Intent intent = new Intent();
                        intent.putExtra(KEY_OUTPUT_URI, mCurrentPhotoPath);
                        setResult(RESULT_OK, intent);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static void saveBitmap(Context context, File file, Bitmap bitmap) {

        OutputStream out = null;

        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        try {
            if (out != null) {
                out.flush();
                out.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
