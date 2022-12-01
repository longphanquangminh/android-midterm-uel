package com.phanquangminhlong.midtermtest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.phanquangminhlong.midtermtest.databinding.ActivityMainBinding;
import com.phanquangminhlong.models.Tour;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    ArrayList<Tour> tours;
    ArrayAdapter<Tour> adapter;

    public static SQLiteDatabase db = null;

    Tour selectedTour = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        copyDB();
        addEvents();
        registerForContextMenu(binding.lvTour);
    }

    private void addEvents() {
        binding.lvTour.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                selectedTour = adapter.getItem(i);

                return false;
            }
        });
    }

    private void copyDB() {
        File dbPath = getDatabasePath(Utils.DB_NAME);
        if (!dbPath.exists()) {
            if (copyDBFromAssets()) {
                Toast.makeText(this, "Success!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Fail!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean copyDBFromAssets() {
        String dbPath = getApplicationInfo().dataDir + Utils.DB_PATH_SUFFIX +
                Utils.DB_NAME;
        try {
            InputStream inputStream = getAssets().open(Utils.DB_NAME);
            File f = new File(getApplicationInfo().dataDir + Utils.DB_PATH_SUFFIX);
            if (!f.exists()) {
                f.mkdir();
            }
            OutputStream outputStream = new FileOutputStream(dbPath);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    @Override
    protected void onResume() {
        loadData();
        super.onResume();
    }

    //=====MENU=====
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.mn_Add){
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.context_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.mn_Edit){
            Intent intent = new Intent(MainActivity.this, EditActivity.class);

            //Attach data
            if(selectedTour != null){
                intent.putExtra("tourInf", selectedTour);
                startActivity(intent);
            }
        }
        if(item.getItemId() == R.id.mn_Delete){
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Xác nhận xóa!");
            builder.setMessage("Bạn có chắc muốn xóa tour: " + selectedTour.getTourName() + "?");
            builder.setIcon(android.R.drawable.ic_delete);

            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    int deletedRows = db.delete(Utils.TBL_NAME, Utils.COL_ID + "=?", new String[]{String.valueOf(selectedTour.getTourId())});
                    if(deletedRows > 0)
                        Toast.makeText(MainActivity.this, "Success!", Toast.LENGTH_SHORT).show();

                    else
                        Toast.makeText(MainActivity.this, "Fail!", Toast.LENGTH_SHORT).show();
                    loadData();
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });

            builder.create().show();
        }
        return super.onContextItemSelected(item);
    }

    private void loadData() {
        tours = new ArrayList<>();

        db = openOrCreateDatabase(Utils.DB_NAME, MODE_PRIVATE, null);

        Cursor cursor = db.query(Utils.TBL_NAME, null, "TourId>?", new String[]{"2"}, null, null, null);

        while (cursor.moveToNext()){
            tours.add(new Tour(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getDouble(5)));
        }

        adapter = new ArrayAdapter<Tour>(MainActivity.this, android.R.layout.simple_list_item_1, tours);

        binding.lvTour.setAdapter(adapter);
    }

}