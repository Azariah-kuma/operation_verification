package com.example.databasebasic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SimpleDatabaseHelper helper =null;
    private EditText txtIsbn = null;
    private EditText txtTitle = null;
    private EditText txtPrice = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = new SimpleDatabaseHelper(this);
        txtIsbn = findViewById(R.id.txtIsbn);
        txtTitle = findViewById(R.id.txtTitle);
        txtPrice = findViewById(R.id.txtPrice);
    }

    public void onSave(View view){
        try (SQLiteDatabase db = helper.getWritableDatabase()){
            ContentValues cv = new ContentValues();
            cv.put("isbn", txtIsbn.getText().toString());
            cv.put("title", txtTitle.getText().toString());
            cv.put("price", txtPrice.getText().toString());
            db.insertWithOnConflict("books", null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            Toast.makeText(this, "データの登録に成功しました", Toast.LENGTH_SHORT).show();
        }
    }

    public void onDelete(View view){
        try (SQLiteDatabase db = helper.getWritableDatabase()){
            String[] params = {txtIsbn.getText().toString()};
            db.delete("books", "isbn = ?", params);
            Toast.makeText(this, "データの削除に成功しました", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSearch(View view){
        String[] cols = {"isbn", "title", "price"};
        String[] params = {txtIsbn.getText().toString()};
        try (SQLiteDatabase db = helper.getReadableDatabase();
             Cursor cs = db.query("books", cols, "isbn = ?",
                     params, null, null, null, null)){
            if (cs.moveToFirst()){
                txtTitle.setText(cs.getString(1));
                txtPrice.setText(cs.getString(2));
            }else{
                Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
