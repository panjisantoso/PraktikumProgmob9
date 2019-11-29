package com.example.progmobkelompok9;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.progmobkelompok9.offlineSQLite.DBHelper;

import androidx.appcompat.app.AppCompatActivity;

public class OfflineDocumentDetailActivity extends AppCompatActivity {
    Cursor cursor;
    DBHelper dbHelper;
    TextView text1, text2, text3, text4, text5, text7, text6;
    ImageView image1,mRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_document_detail);

        dbHelper = new DBHelper(this);
        String sql = "CREATE TABLE document (" +
                "id_document INT PRIMARY KEY," +
                "judul TEXT," +
                "category TEXT," +
                "penulis TEXT," +
                "penerbit TEXT," +
                "tahun_terbit TEXT," +
                "foto TEXT," +
                "nama_user TEXT," +
                "file_type TEXT,"+
                "deskripsi TEXT," +
                "path TEXT," +
                "tgl_upload TEXT," +
                "id_user TEXT," +
                "status_aktif INT" +
                ");";
        text1 = (TextView) findViewById(R.id.detail_document_namaDocument);
        text2 = (TextView) findViewById(R.id.detail_document_namaCategory);
        text3 = (TextView) findViewById(R.id.detail_document_penulis);
        text4 = (TextView) findViewById(R.id.detail_document_penerbit);
        text5 = (TextView) findViewById(R.id.detail_document_tahunTerbit);
        text6 = (TextView) findViewById(R.id.detail_document_readTxt);
        text7 = (TextView) findViewById(R.id.detail_document_namaUser);
        image1 = (ImageView) findViewById(R.id.detail_document_image);
        mRead = findViewById(R.id.document_detail_read);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM document WHERE judul = '" +
                getIntent().getStringExtra("judul") + "'",null);
        cursor.moveToFirst();
        if (cursor.getCount()>0)
        {
            cursor.moveToPosition(0);
            String gambar = BuildConfig.BASE_URL_IMAGE + image1;

            text1.setText(cursor.getString(1).toString());
            text2.setText(cursor.getString(2).toString());
            text3.setText(cursor.getString(3).toString());
            text4.setText(cursor.getString(4).toString());
            text5.setText(cursor.getString(5).toString());

            mRead.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String url = BuildConfig.BASE_URL_DOCUMENT + cursor.getString(10);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            text7.setText(cursor.getString(7).toString());
//            gambar(cursor.getString(6)).toString();
            
        }
    }
}
