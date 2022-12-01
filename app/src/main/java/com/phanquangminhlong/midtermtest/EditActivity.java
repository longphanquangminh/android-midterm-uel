package com.phanquangminhlong.midtermtest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.phanquangminhlong.models.Tour;
import com.phanquangminhlong.midtermtest.databinding.ActivityEditBinding;

public class EditActivity extends AppCompatActivity {

    ActivityEditBinding binding;
    Tour t = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_edit);

        binding = ActivityEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getData();
        addEvents();
    }

    private void getData() {
        Intent intent = getIntent();
        t = (Tour) intent.getSerializableExtra("tourInf");
        binding.edtName.setText(t.getTourName());
        binding.edtDescription.setText(t.getTourDescription());
        binding.edtCount.setText(String.valueOf(t.getTourCount()));
        binding.edtSchedule.setText(t.getTourSchedule());
        binding.edtPrice.setText(String.valueOf(t.getTourPrice()));
    }

    private void addEvents() {
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(Utils.COL_NAME, binding.edtName.getText().toString());
                values.put(Utils.COL_DESCRIPTION, binding.edtDescription.getText().toString());
                values.put(Utils.COL_COUNT, Integer.parseInt(binding.edtCount.getText().toString()));
                values.put(Utils.COL_SCHEDULE, binding.edtSchedule.getText().toString());
                values.put(Utils.COL_PRICE, Double.parseDouble(binding.edtPrice.getText().toString()));

                int updatedRows = MainActivity.db.update(Utils.TBL_NAME, values, Utils.COL_ID + "=?", new String[]{String.valueOf(t.getTourId())});

                if(updatedRows > 0){
                    Toast.makeText(EditActivity.this, "Success!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(EditActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }
}