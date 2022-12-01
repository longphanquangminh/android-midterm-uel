package com.phanquangminhlong.midtermtest;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.phanquangminhlong.midtermtest.databinding.ActivityAddBinding;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class AddActivity extends AppCompatActivity {

    ActivityAddBinding binding;
    BottomSheetDialog bottomSheetDialog;
    LinearLayout llOpenCamera, llOpenGallery;

    ActivityResultLauncher<Intent> launcher;

    String capture = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_add);

        binding = ActivityAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        createBottomSheet();
        addEvents();
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result ->{
            if(result.getResultCode()==RESULT_OK && result.getData() != null){
                if(capture.equals("camera") ){

                    Bitmap bitmap = (Bitmap)result.getData().getExtras().get("data");
                    binding.imgPhoto.setImageBitmap(bitmap);
                }
                else if(capture.equals("gallery")){
                    Uri uri = result.getData().getData();

                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        binding.imgPhoto.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void createBottomSheet() {
        if(bottomSheetDialog == null){
            View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet, null);
            llOpenCamera = view.findViewById(R.id.llOpenCamera);
            llOpenCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    capture = "camera";

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    launcher.launch(intent);

                    bottomSheetDialog.dismiss();
                }
            });
            llOpenGallery = view.findViewById(R.id.llOpenGallery);
            llOpenGallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    capture = "gallery";
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    launcher.launch(intent);
                    bottomSheetDialog.dismiss();
                }
            });
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);
        }
    }
    private byte[] convertBitmapToByteArray() {
        BitmapDrawable drawable = (BitmapDrawable) binding.imgPhoto.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);

        return outputStream.toByteArray();
    }
    private void addEvents() {
        binding.btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.show();
            }
        });

        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Insert data into Db
                String name = binding.edtName.getText().toString();
                String description = binding.edtDescription.getText().toString();
                int count = Integer.parseInt(binding.edtCount.getText().toString());
                String schedule = binding.edtSchedule.getText().toString();
                double price = Double.parseDouble(binding.edtPrice.getText().toString());

                ContentValues values = new ContentValues();
                values.put(Utils.COL_NAME, name);
                values.put(Utils.COL_DESCRIPTION, description);
                values.put(Utils.COL_COUNT, count);
                values.put(Utils.COL_SCHEDULE, schedule);
                values.put(Utils.COL_PRICE, price);

                long numbOfRows = MainActivity.db.insert("Tour", null, values);

                if(numbOfRows > 0) {
                    Toast.makeText(AddActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AddActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}