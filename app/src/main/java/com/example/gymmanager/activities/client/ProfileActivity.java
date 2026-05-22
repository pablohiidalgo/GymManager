package com.example.gymmanager.activities.client;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.gymmanager.R;

import java.io.File;
import java.io.FileOutputStream;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imgProfile;
    private Button btnGallery, btnCamera;
    private TextView tvProfileName;

    private SharedPreferences preferences;

    private ActivityResultLauncher<String> galleryLauncher;
    private ActivityResultLauncher<Void> cameraLauncher;
    private ActivityResultLauncher<String> cameraPermissionLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        preferences = getSharedPreferences("profile_prefs", Context.MODE_PRIVATE);

        imgProfile = findViewById(R.id.imgProfile);
        btnGallery = findViewById(R.id.btnGallery);
        btnCamera = findViewById(R.id.btnCamera);
        tvProfileName = findViewById(R.id.tvProfileName);

        setupLaunchers();
        loadSavedImage();

        btnGallery.setOnClickListener(v -> galleryLauncher.launch("image/*"));

        btnCamera.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {
                cameraLauncher.launch(null);
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            }
        });
    }

    private void setupLaunchers() {
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imgProfile.setImageURI(uri);
                        saveImageUri(uri.toString());
                    }
                }
        );

        cameraLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicturePreview(),
                bitmap -> {
                    if (bitmap != null) {
                        Uri uri = saveBitmapToInternalStorage(bitmap);
                        if (uri != null) {
                            imgProfile.setImageURI(uri);
                            saveImageUri(uri.toString());
                        }
                    }
                }
        );

        cameraPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                granted -> {
                    if (granted) {
                        cameraLauncher.launch(null);
                    } else {
                        Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void saveImageUri(String uri) {
        preferences.edit()
                .putString("profile_image", uri)
                .apply();
    }

    private void loadSavedImage() {
        String savedUri = preferences.getString("profile_image", null);

        if (savedUri != null) {
            imgProfile.setImageURI(Uri.parse(savedUri));
        }
    }

    private Uri saveBitmapToInternalStorage(Bitmap bitmap) {
        try {
            File file = new File(getFilesDir(), "profile_photo.jpg");

            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
            outputStream.close();

            return Uri.fromFile(file);

        } catch (Exception e) {
            Toast.makeText(this, "No se pudo guardar la foto", Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}